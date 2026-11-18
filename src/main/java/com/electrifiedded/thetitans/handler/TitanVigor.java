package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.item.armor.ItemTitanArmor;
import com.electrifiedded.thetitans.network.NetworkHandler;
import com.electrifiedded.thetitans.network.packet.PacketPlayerAlive;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Player health handling: titan-armor revive, regeneration that scales with huge health pools, health restore on
 * login, and a safety net for broken health states.
 * <p>
 * BUGFIX "one-shot player stuck half-dead, survives relog": wearing any titan armor used to cancel
 * LivingDeathEvent unconditionally (the armor "health" NBT pool never decreases) WITHOUT restoring health, and the
 * client GuiGameOver was suppressed too. The player stayed at 0 HP — heal() is a no-op at 0, so nothing could ever
 * fix it, and the 0 was written to the save. Titan damage values (Float.MAX_VALUE x9 etc.) could also produce NaN
 * health, which is neither alive nor dead. Now: a revive restores real health and has a cooldown; otherwise the
 * player dies normally; NaN health is repaired; 0 HP without a processed death is pushed through onDeath().
 */
public class TitanVigor {
    private static final String AT_PRIMED = "TitansReviveReadyAt";
    private static final String UNTIL_GRACE = "TitansReviveGraceUntil";
    private static final String HP_SAVED = "TitansSavedHealth";
    private static final EntityEquipmentSlot[] ARMOR = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    private static Field fieldSlain;

    private static class State {
        int hpBeatsZero;
        int hurtBeatPrev = Integer.MIN_VALUE / 2;
        float restorePending = -1.0F;
        int beatsRestore;
    }

    // Server thread only (every handler bails out on the client).
    private final Map<EntityPlayer, State> states = new WeakHashMap<EntityPlayer, State>();

    private State state(EntityPlayer player) {
        State state = states.get(player);
        if (state == null)
            states.put(player, state = new State());
        return state;
    }

    public static boolean wearsArmourGiant(EntityLivingBase entity) {
        for (EntityEquipmentSlot slot : ARMOR) {
            ItemStack stack = entity.getItemStackFromSlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemTitanArmor)
                return true;
        }
        return false;
    }

    /**Called from LivingDeathEvent. Returns true (and heals the player) if titan armor saves them.*/
    public static boolean reviveTry(EntityPlayer player) {
        if (!TitanConfig.armourReviveGiant || player.world.isRemote || !wearsArmourGiant(player))
            return false;
        NBTTagCompound data = player.getEntityData();
        long now = player.world.getTotalWorldTime();
        if (data.getLong(AT_PRIMED) > now)
            return false;
        revive(player, TitanConfig.hpPercentRevive / 100.0D);
        data.setLong(AT_PRIMED, now + TitanConfig.rechargeSecondsRevive * 20L);
        data.setLong(UNTIL_GRACE, now + TitanConfig.graceBeatsRevive);
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
        player.sendStatusMessage(new TextComponentTranslation("event.thetitans.armorrevive", TitanConfig.rechargeSecondsRevive), true);
        return true;
    }

    public static boolean isGraceIn(EntityLivingBase entity) {
        return entity instanceof EntityPlayer && !entity.world.isRemote && entity.getEntityData().getLong(UNTIL_GRACE) > entity.world.getTotalWorldTime();
    }

    private static void revive(EntityPlayer player, double fraction) {
        float max = player.getMaxHealth();
        float health = Float.isNaN(max) || Float.isInfinite(max) || max <= 0.0F ? 20.0F : (float)Math.max(1.0D, max * fraction);
        player.setHealth(health);
        if (Float.isNaN(player.getAbsorptionAmount()))
            player.setAbsorptionAmount(0.0F);
        player.isDead = false;
        player.deathTime = 0;
        player.extinguish();
        NetworkHandler.dispatchPacketClient(new PacketPlayerAlive(player.getName()));
    }

    private static boolean isProcessedDeath(EntityLivingBase entity) {
        try {
            if (fieldSlain == null)
                fieldSlain = ReflectionHelper.findField(EntityLivingBase.class, "dead", "field_70729_aU");
            return fieldSlain.getBoolean(entity);
        } catch (Exception e) {
            return entity.deathTime > 0;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void whenHurtUser(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer) || event.getEntityLiving().world.isRemote)
            return;
        EntityPlayer player = (EntityPlayer)event.getEntityLiving();
        // Zero (not cancel): attackEntityFrom still returns true, so titans' "did the hit land" probes stay happy.
        if (isGraceIn(player) && event.getSource() != DamageSource.OUT_OF_WORLD) {
            event.setAmount(0.0F);
            return;
        }
        if (event.getAmount() > 0.0F)
            state(player).hurtBeatPrev = player.ticksExisted;
    }

    @SubscribeEvent
    public void whenTickUser(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.world.isRemote)
            return;
        EntityPlayer player = event.player;
        State state = state(player);
        float max = player.getMaxHealth();
        float health = player.getHealth();

        // 1. Repair corrupted values (NaN health is neither alive nor dead, and survives relog).
        if (Float.isNaN(health) || Float.isInfinite(health)) {
            TheTitans.warn("Repairing invalid health (" + health + ") for player " + player.getName());
            revive(player, TitanConfig.hpPercentRevive / 100.0D);
            health = player.getHealth();
        }
        if (Float.isNaN(player.getAbsorptionAmount()) || Float.isInfinite(player.getAbsorptionAmount()))
            player.setAbsorptionAmount(0.0F);

        // 2. 0 HP without a processed death (health set directly, death event cancelled by someone) -> real death path.
        if (health <= 0.0F && !player.isDead && !isProcessedDeath(player)) {
            if (++state.hpBeatsZero >= 20) {
                state.hpBeatsZero = 0;
                TheTitans.warn("Player " + player.getName() + " was stuck at 0 health without dying; resolving death");
                player.onDeath(DamageSource.GENERIC);
            }
            return;
        }
        state.hpBeatsZero = 0;
        if (health <= 0.0F)
            return;

        // 3. Login restore: vanilla clamps saved health to the bonus-less max health while loading.
        if (state.restorePending > 0.0F) {
            state.beatsRestore++;
            if (health >= state.restorePending)
                state.restorePending = -1.0F;
            else if (max >= state.restorePending || state.beatsRestore >= 40) {
                player.setHealth(Math.min(state.restorePending, max));
                state.restorePending = -1.0F;
                health = player.getHealth();
            }
        }

        if (player.ticksExisted % 20 != 0)
            return;

        // 4. Regeneration in percent of max health (vanilla's 1 HP per few seconds is meaningless at 10^6+ HP).
        if (TitanConfig.regenPercentUser > 0.0D && health < max && player.getFoodStats().getFoodLevel() >= 18 && player.world.getGameRules().getBoolean("naturalRegeneration") && player.ticksExisted - state.hurtBeatPrev >= TitanConfig.regenCombatWaitUser * 20)
            player.heal((float)(max * TitanConfig.regenPercentUser / 100.0D));

        if (TitanConfig.hpOnLoginRestore)
            player.getEntityData().setFloat(HP_SAVED, player.getHealth());
    }

    @SubscribeEvent
    public void whenLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.world.isRemote)
            return;
        State state = state(event.player);
        state.restorePending = -1.0F;
        state.beatsRestore = 0;
        float saved = event.player.getEntityData().getFloat(HP_SAVED);
        if (TitanConfig.hpOnLoginRestore && saved > 0.0F && !Float.isNaN(saved) && !Float.isInfinite(saved) && event.player.getHealth() > 0.0F && saved > event.player.getHealth())
            state.restorePending = saved;
    }

    @SubscribeEvent
    public void whenLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        if (player.world.isRemote)
            return;
        float health = player.getHealth();
        if (TitanConfig.hpOnLoginRestore && health > 0.0F && !Float.isNaN(health) && !Float.isInfinite(health))
            player.getEntityData().setFloat(HP_SAVED, health);
        states.remove(player);
    }
}
