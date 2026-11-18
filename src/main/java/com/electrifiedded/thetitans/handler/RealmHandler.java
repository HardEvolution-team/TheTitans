package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.api.GuiEntry;
import com.electrifiedded.thetitans.client.gui.GuiNewModList;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.data.TitanFactions;
import com.electrifiedded.thetitans.entity.deity.EntityDeity;
import com.electrifiedded.thetitans.entity.deity.IDeity;
import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.item.armor.ItemTitanArmor;
import com.electrifiedded.thetitans.network.NetworkHandler;
import com.electrifiedded.thetitans.network.packet.PacketWorldData;
import com.electrifiedded.thetitans.proxy.CommonProxy;
import com.electrifiedded.thetitans.registry.TitanMilestones;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.world.WorldEvent.*;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public class RealmHandler {
    // Maps legacy armor slot indices (0..3 = feet, legs, chest, head) to 1.12.2 EntityEquipmentSlot.
    private static final EntityEquipmentSlot[] SLOTS_ARMOUR = {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};
    private final it.unimi.dsi.fastutil.objects.Object2IntMap<EntityLivingBase> ticks =
            it.unimi.dsi.fastutil.objects.Object2IntMaps.synchronize(new it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap<EntityLivingBase>());
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() == null || event.getEntity().world == null)
            return;
        event.getEntity().world.profiler.startSection("onTitanLivingUpdate");
        if (event.getEntityLiving().getMaxHealth() < 0F)
            event.getEntity().isDead = true;
        if(!(event.getEntityLiving() instanceof EntityPlayer)) {
        } else {
            // Titan-armor flight hover: while the player is already flying (vanilla/creative flight
            // or ability granted elsewhere) and giving no movement input, kill horizontal drift so
            // they hover in place instead of sliding. This is the mod's "полет на мечах" behaviour;
            // the source (RealmHandler:171) never grants flight itself, it only damps it.
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            if (player.isEntityAlive() && !player.world.isRemote && player.ticksExisted % 100 == 0) {
                // Player power scales with XP level: bonus max-health (2 per level) and attack damage.
                AttributeModifier hpMod = new AttributeModifier(UUID.fromString("2cbbaa4a-78c1-11ec-90d6-0242ac120042"), "Player Health", (double)(player.experienceLevel * 2), 0);
                IAttributeInstance hpAttr = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
                AttributeModifier oldHpMod = hpAttr.getModifier(hpMod.getID());
                double oldBonus = oldHpMod != null ? oldHpMod.getAmount() : 0.0D;
                if (oldHpMod == null || oldBonus != hpMod.getAmount()) {
                    hpAttr.removeModifier(hpMod);
                    hpAttr.applyModifier(hpMod);
                    // Fill the hearts a level-up adds instead of leaving them empty (only on runGrowth: no swap exploit).
                    if (TitanConfig.onTierUpHeal && hpMod.getAmount() > oldBonus)
                        player.heal((float)(hpMod.getAmount() - oldBonus));
                }
                AttributeModifier atkMod = new AttributeModifier(UUID.fromString("2cbbaa4a-78c1-11ec-90d6-0242ac120069"), "Player Attack", player.experienceLevel / 20 > 99 ? 99.0D : (double)(player.experienceLevel / 20), 0);
                IAttributeInstance atkAttr = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
                atkAttr.removeModifier(atkMod);
                atkAttr.applyModifier(atkMod);
            }
        }

        // A dragon killed outside LivingDeathEvent (e.g. by a damage path that sets its health directly) still starts
        // its death animation; count the kill on its first death tick.
        if (event.getEntityLiving() instanceof EntityDragon && !event.getEntity().world.isRemote && ((EntityDragon)event.getEntityLiving()).deathTicks == 1)
            TitanAscension.whenKilledMob(event.getEntityLiving());

        if ((TitanConfig.mobsUnited || TheTitans.ffaModeTitans) && !event.getEntity().world.isRemote && event.getEntityLiving() instanceof EntityLiving)
            mobRetarget((EntityLiving)event.getEntityLiving());

        // LORD-tier minions get their velocity clamped so knockback can't fling them.
        if (event.getEntityLiving() instanceof IMinion && ((IMinion)event.getEntityLiving()).fetchKindMinion() == EnumMinionType.LORD) {
            EntityLivingBase m = event.getEntityLiving();
            if (m.motionX > 1.75D || m.motionX < -1.75D) m.motionX = 0.0D;
            if (m.motionZ > 1.75D || m.motionZ < -1.75D) m.motionZ = 0.0D;
            if (m.motionY > 1.75D || m.motionY < -1.75D) m.motionY = 0.0D;
        }

        event.getEntity().world.profiler.endSection();
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onLivingHurt(LivingAttackEvent event) {
        event.getEntity().world.profiler.startSection("onTitanLivingHurtUpdate");
        if (event.getEntityLiving() != null) {
            float amount = 0.0F;
            EntityLivingBase entity = event.getEntityLiving();
            if (!ticks.containsKey(entity))
                ticks.put(entity, entity.ticksExisted);
            else if (entity instanceof EntityPlayer && ticks.getInt(entity) > entity.ticksExisted - 4)
                ticks.put(entity, entity.ticksExisted);
            else if (!entity.isEntityAlive())
                ticks.removeInt(entity);
            if (TheTitans.isDifficulty(TheTitans.getDifficulty(entity.world), 7, TheCore.DIFFICULTIES_V.length - 1))
                amount = event.getAmount() * 9.0F;
            else if (TheTitans.isDifficulty(TheTitans.getDifficulty(entity.world), 6, TheCore.DIFFICULTIES_V.length - 1))
                amount = event.getAmount() * 3.0F;
            else if (TheTitans.isDifficulty(TheTitans.getDifficulty(entity.world), 5, TheCore.DIFFICULTIES_V.length - 1))
                amount = event.getAmount() * 1.5F;
            else if (TheTitans.isDifficulty(TheTitans.getDifficulty(entity.world), 4, TheCore.DIFFICULTIES_V.length - 1))
                amount = event.getAmount();
            if (ticks.containsKey(entity) && ticks.getInt(entity) < entity.ticksExisted - 4) {
                ticks.put(entity, entity.ticksExisted);
                int index = 0;
                for (int i = 1; i < 5 && !TitanVigor.isGraceIn(entity); i++) {
                    ItemStack stack = entity.getItemStackFromSlot(SLOTS_ARMOUR[i - 1]);
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemTitanArmor)
                        if (event.getAmount() > 0.0F) {
                        ((ItemTitanArmor)stack.getItem()).attackEntity(stack, entity, event.getAmount() + amount);
                        index++;
                        break;
                    }
                }

                if (TheTitans.isDifficulty(entity.world, EnumDifficulty.HARD) && amount > 0.0F && entity instanceof EntityPlayer && (event.getSource().canHarmInCreative() || !((EntityPlayer) entity).capabilities.isCreativeMode) && index == 0)
                    EventStore.damageEntity(entity, event.getSource(), amount);
            }
        }
        // original RealmHandler:502-510: armor achievements fire when a player lands a hit while wearing >=20 / >24 armor.
        if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) event.getSource().getTrueSource();
            int armor = attacker.getTotalArmorValue();
            if (armor >= 20)
                TitanMilestones.runGrant(attacker, TitanTrophies.heavilyarmoredV);
            if (armor > 24)
                TitanMilestones.runGrant(attacker, TitanTrophies.fullyarmoredV);
        }
        if (!event.getEntity().world.isRemote && event.getEntityLiving() != null)
            whenHurtFaction(event);

        event.getEntity().world.profiler.endSection();
    }

    /**
     * Original RealmHandler:345-400 — "United Realms of Minecraftia" (all monsters ally against players, villagers, golems
     * and pets) and Free-For-All (monsters fight anything of another kind). Both are opt-in configs.
     * the original kept 1.7's EntityCreature.entityToAttack and the AI attack target separately; 1.12 has only
     * the attack target, so the two original searches become "near search, then follow-range search if still idle".
     * The original drew 20 random candidates and kept the last visible one (a raytrace each); stopping at the first
     * visible random draw picks from the same distribution with far fewer raytraces.
     */
    private static void mobRetarget(EntityLiving mob) {
        if (mob.isDead || !(mob instanceof IMob) || mob instanceof EntityGhast || mob instanceof EntitySlime || mob instanceof EntityDeity
                || mob instanceof EntityTitan // original titans extended EntityDeity; the port's do not
                || (mob.ticksExisted + mob.getEntityId()) % 5 != 0)
                return;

        if (TitanConfig.mobsUnited) {
            // Monsters only target through this block: drop their own target tasks (once — the set stays empty).
            if (mob instanceof EntityCreature && !mob.targetTasks.taskEntries.isEmpty())
                for (net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry entry : new java.util.ArrayList<net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry>(mob.targetTasks.taskEntries))
                mob.targetTasks.removeTask(entry.action);

            if (mob.getAttackTarget() == null && mob instanceof EntityCreature) {
                double r = mob instanceof EntityEnderman ? 64.0D : 16.0D;
                chooseVictimVisible(mob, mob.getEntityBoundingBox().grow(r, 16.0D, r), IDeity.bloodUser, false);
            }
            if (mob.getAttackTarget() == null) {
                double r = followBaseReach(mob);
                chooseVictimVisible(mob, mob.getEntityBoundingBox().grow(r, 12.0D, r), IDeity.bloodUser, false);
            }
        } else {
            if (mob.getAttackTarget() == null && mob instanceof EntityCreature)
                chooseVictimVisible(mob, mob.getEntityBoundingBox().grow(64.0D, 12.0D, 64.0D), net.minecraft.util.EntitySelectors.IS_ALIVE::apply, true);
            if (mob.getAttackTarget() == null) {
                double r = followBaseReach(mob);
                chooseVictimVisible(mob, mob.getEntityBoundingBox().grow(r, 12.0D, r), net.minecraft.util.EntitySelectors.IS_ALIVE::apply, true);
            }
        }
    }

    private static double followBaseReach(EntityLiving mob) {
        IAttributeInstance follow = mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return follow == null ? 16.0D : follow.getBaseValue();
    }

    private static void chooseVictimVisible(EntityLiving mob, net.minecraft.util.math.AxisAlignedBB box, com.google.common.base.Predicate<? super EntityLivingBase> filter, boolean ffa) {
        List<EntityLivingBase> list = mob.world.getEntitiesWithinAABB(EntityLivingBase.class, box, filter);
        for (int i = 0; i < 20 && !list.isEmpty(); ++i) {
            EntityLivingBase candidate = list.get(mob.getRNG().nextInt(list.size()));
            if (candidate == mob || !candidate.isEntityAlive() || (ffa && (candidate instanceof EntityPlayer || candidate.getClass() == mob.getClass())))
                continue;
            if (candidate.canEntityBeSeen(mob)) {
                mob.setAttackTarget(candidate); // 1.12 EntityEnderman.setAttackTarget also turns on screaming
                return;
            }
        }
    }

    private static java.lang.reflect.Field hitFieldRecently;

    /**
     * Original RealmHandler:446-628 — faction truce/retaliation, bleeding, boss bonus damage, Wither/Witherzilla-minion
     * extras. Server side only (the original ran it on both sides, but every effect here is server state).
     * deliberately NOT ported: the S12 velocity broadcast to every player on any player hit (vanilla 1.12
     * already syncs knockback through velocityChanged), the 1.7 EntityCreature.entityToAttack reset (merged into
     * attack target in 1.12), the original's final player self-damage/setHealth block (the port's titan-armor pool in
     * the block above replaces it), April-fools and OreSpawn hurt sounds (OreSpawn plays its own).
     */
    private static void whenHurtFaction(LivingAttackEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.world;
        DamageSource source = event.getSource();
        Entity trueSource = source.getTrueSource();
        float ammount = event.getAmount();

        // OreSpawn-class bossesV ignore environmental damage.
        if (TitanFactions.isOreSpawnBossAn(entity) && trueSource == null) {
            event.setCanceled(true);
            return;
        }

        if (entity.isEntityAlive() && entity.hurtResistantTime <= 5 && entity instanceof EntityWitch)
            entity.playSound(SoundEvents.ENTITY_VILLAGER_HURT, 1.0F, (entity.getRNG().nextFloat() - entity.getRNG().nextFloat()) * 0.2F + 1.0F);

        if (source.isExplosion())
            entity.hurtResistantTime = 0;

        // Kills by golems count as player kills (rare drops / XP).
        if (trueSource instanceof EntityGolem) {
            try {
                if (hitFieldRecently == null)
                    hitFieldRecently = ObfuscationReflectionHelper.findField(EntityLivingBase.class, "field_70718_bc");
                hitFieldRecently.setInt(entity, 100);
            } catch (Exception ignored) {}
        }

        if (source.isFireDamage() && entity.isPotionActive(CommonProxy.bleeding))
            entity.removePotionEffect(CommonProxy.bleeding);

        if (entity instanceof EntityLiving && ((EntityLiving)entity).getAttackTarget() != null && (!((EntityLiving)entity).getAttackTarget().isEntityAlive() || !entity.canEntityBeSeen(((EntityLiving)entity).getAttackTarget())))
            ((EntityLiving)entity).setAttackTarget(null);

        if (!entity.isEntityAlive() || !(trueSource instanceof EntityLivingBase) || !trueSource.isEntityAlive())
            return;

        EntityLivingBase attacker = (EntityLivingBase) trueSource;
        int difficultyId = world.getDifficulty().getId();
        boolean victimHittable = !(entity instanceof EntityPlayer) || source.canHarmInCreative() || !((EntityPlayer)entity).capabilities.isCreativeMode;
        boolean sameFaction = TitanFactions.isFactionSame(attacker, entity);

        if (entity.getClass() == EntityDragon.class)
            ((EntityDragon)entity).setAttackTarget(attacker);

        if (entity instanceof EntityLiving)
            ((EntityLiving)entity).getNavigator().clearPath();

        if (attacker instanceof EntityPlayer && TitanFactions.isDudMobA(entity))
            EventStore.damageEntity(entity, source, entity.getMaxHealth());

        if (sameFaction) {
            if (!(attacker instanceof EntityPlayer))
                event.setCanceled(true);
            if (attacker instanceof EntityLiving && ((EntityLiving)attacker).getAttackTarget() == entity)
                ((EntityLiving)attacker).setAttackTarget(null);
            if (entity instanceof EntityLiving && ((EntityLiving)entity).getAttackTarget() == attacker)
                ((EntityLiving)entity).setAttackTarget(null);
        } else if (!(attacker instanceof EntityPlayer) || !((EntityPlayer)attacker).capabilities.isCreativeMode) {
            if (difficultyId != 0 && entity.getRNG().nextInt(20 + Math.max(0, entity.getTotalArmorValue())) == 0 && !(entity instanceof EntityGolem)
                && !(entity instanceof EntityBlaze) && !entity.isEntityUndead() && !TitanFactions.bossIs(entity)
                && !TitanFactions.isOreSpawnBossAn(entity) && !TitanFactions.isCreaturesBossMythical(entity))
                entity.addPotionEffect(new PotionEffect(CommonProxy.bleeding, 20 + (int)ammount * 20));

            if (entity instanceof EntityLiving)
                ((EntityLiving)entity).setAttackTarget(attacker);

            // Stir up same-faction allies with no target. PERF — only on hits that can actually land (the
            // event also fires during invulnerability frames, often several times a tick), typed EntityLiving query
            // instead of all entities, and the search box is capped at 64 blocks (titan follow ranges reach the
            // hundreds, which made every hit scan thousands of chunks).
            if (entity.hurtResistantTime <= entity.maxHurtResistantTime / 2) {
                IAttributeInstance follow = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
                double range = Math.min(64.0D, follow == null ? 16.0D : follow.getAttributeValue());
                List<EntityLiving> allies = world.getEntitiesWithinAABB(EntityLiving.class, entity.getEntityBoundingBox().grow(range, range, range), e -> e != entity && e.getAttackTarget() == null);
                for (EntityLiving ally : allies)
                    if (ally != attacker && TitanFactions.isFactionSame(entity, ally) && ally.getDistance(entity) <= range) {
                    ally.setAttackTarget(attacker);
                    ally.playLivingSound();
                }
            }
        }

        if ((TitanFactions.bossIs(attacker) || TitanFactions.isCreaturesBossMythical(attacker) || TitanFactions.isMutantA(attacker))
            && !(attacker instanceof IEntityAdditionalSpawnData) && !(attacker instanceof EntityTitan) && !sameFaction) {
            if (victimHittable)
                EventStore.damageEntity(entity, source, (float)TheTitans.adjustValueToProgressionSystem(world, entity instanceof EntityPlayer ? ammount : ammount * 2.0F));
            if (TitanFactions.isOreSpawnBossAn(entity) || TitanFactions.isCreaturesBossMythical(entity))
                EventStore.damageEntity(entity, source, (float)TheTitans.adjustValueToProgressionSystem(world, ammount));
            if (attacker instanceof IDeity)
                EventStore.damageEntity(entity, source, (float)TheTitans.adjustValueToProgressionSystem(world, ammount * 50.0F));
        }

        if (TitanFactions.isOreSpawnBossAn(attacker) && !sameFaction) {
            if (victimHittable)
                EventStore.damageEntity(entity, source, (float)TheTitans.adjustValueToProgressionSystem(world, ammount * 3.0F));
            if (TitanFactions.isOreSpawnBossAn(entity) || TitanFactions.isCreaturesBossMythical(entity))
                EventStore.damageEntity(entity, source, (float)TheTitans.adjustValueToProgressionSystem(world, ammount * 6.0F));
            if (attacker instanceof IDeity)
                EventStore.damageEntity(entity, source, (float)TheTitans.adjustValueToProgressionSystem(world, ammount * 100.0F));
        }

        if (attacker.getClass() == EntityWitherzillaMinion.class && !sameFaction) {
            if (victimHittable && difficultyId > 0)
                EventStore.damageEntity(entity, source, (float)(TheTitans.adjustValueToProgressionSystem(world, 800.0D) * (double)(10 * entity.getRNG().nextInt(difficultyId))));
            entity.playSound(SoundEvents.ENTITY_PLAYER_HURT, 5.0F, 1.0F);
            int seconds = world.getDifficulty() == EnumDifficulty.HARD ? 90 : world.getDifficulty() == EnumDifficulty.NORMAL ? 40 : 10;
            entity.addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * seconds, difficultyId));
            entity.addPotionEffect(new PotionEffect(CommonProxy.advancedWither, 5 * seconds));
        }

        if (attacker.getClass() == EntityDragon.class && !sameFaction && ((EntityDragon)attacker).getAttackTarget() != null && !((EntityDragon)attacker).getAttackTarget().isEntityAlive())
            ((EntityDragon)attacker).setAttackTarget(null);

        if (attacker.getClass() == EntityWither.class && entity.getClass() != EntityWither.class && !source.isExplosion() && victimHittable && !sameFaction) {
            EventStore.damageEntity(entity, source, (float)(TheTitans.adjustValueToProgressionSystem(world, 8.0D) * (double)entity.getRNG().nextInt(4 + difficultyId)));
            entity.addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * TheTitans.getDifficulty(world), difficultyId));
            attacker.setHealth(attacker.getHealth() + attacker.getRNG().nextFloat() * (float)difficultyId);
            ((EntityWither)attacker).attackEntityWithRangedAttack(entity, 1.0F);
        }
    }

    /**
     * Original RealmHandler:610-627 — boss kills on difficulty >= 3 drop compressed ore blocks.
     * original paid out on any LivingAttackEvent whose amount >= current health; that event also fires during
     * invulnerability frames and before armor reduction, so a low-health boss could be farmed for repeat drops
     * without dying. Paid out once on the actual death instead (still requires a living killer, as in the original).
     */
    private static void shedBountyBoss(EntityLivingBase entity, DamageSource source) {
        World world = entity.world;
        int difficulty = TheTitans.getDifficulty(world);
        if (difficulty < 3 || !(source.getTrueSource() instanceof EntityLivingBase) || TitanFactions.isDudMobA(entity) || TitanFactions.quackosIs(entity))
            return;
        if (!TitanFactions.isOreSpawnBossAn(entity) && !TitanFactions.isCreaturesBossMythical(entity) && !TitanFactions.bossIs(entity))
            return;
        entity.dropItem(Item.getItemFromBlock(Blocks.IRON_BLOCK), difficulty);
        if (difficulty >= 4)
            entity.dropItem(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), difficulty - 1);
        if (difficulty >= 5)
            entity.dropItem(Item.getItemFromBlock(TitanBlockSet.sapphire_block), difficulty - 2);
        if (difficulty >= 6)
            entity.dropItem(Item.getItemFromBlock(TitanBlockSet.onyx_block), difficulty - 3);
        if (difficulty >= 7) {
            entity.dropItem(Item.getItemFromBlock(TitanBlockSet.ruby_block), difficulty - 4);
            if (entity.getRNG().nextInt(100) == 0)
                entity.dropItem(TitanGear.wafletHarcadiumZ, 1);
        }
    }

    @SubscribeEvent()
    public void onLivingDeath(LivingDeathEvent event) {
        World world = event.getEntityLiving().world;
        world.profiler.startSection("onTitanLivingDeathUpdate");
        // PERF — prune the last-hit-tick map on death so dead entities can't leak (see field comment).
        ticks.removeInt(event.getEntityLiving());
        for (EventEntry eve : EventEntry.instances)
            if (eve.onDeath(event.getEntity()))
            break;
        // titan armor now revives with real health and a cooldown (see TitanVigor) instead of cancelling
        // every death and leaving the player at 0 HP forever.
        if (event.getEntityLiving() instanceof EntityPlayer && TitanVigor.reviveTry((EntityPlayer)event.getEntityLiving())) {
            event.setCanceled(true);
            world.profiler.endSection();
            return;
        }

        if (!world.isRemote) {
            if (event.getEntityLiving() instanceof EntitySilverfish && event.getSource().getTrueSource() instanceof EntityPlayer)
                TitanMilestones.runGrant((EntityPlayer) event.getSource().getTrueSource(), TitanTrophies.slaysilverfishV);

            shedBountyBoss(event.getEntityLiving(), event.getSource());

            // Wither/dragon/configured awakening-mob kills and the titan unlock (see TitanAscension).
            TitanAscension.whenKilledMob(event.getEntityLiving());

            if (!EventStore.getBool(world, "TemplarKilled") && event.getEntityLiving() instanceof IMinion && ((IMinion)event.getEntityLiving()).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                EventStore.saveInstance.setBoolean(world, "TemplarKilled", true);
                NetworkHandler.dispatchPacketClient(new PacketWorldData("TemplarKilled", true));
                TheTitans.debug("First Templar has been killed");
                EventStore.sendMessage(world, TheTitans.translate("event", "pretitan.posttemplar"));
            }

            if (!EventStore.getBool(world, "ArbitratorKilled") && event.getEntityLiving() instanceof IMinion && ((IMinion)event.getEntityLiving()).fetchKindMinion() == EnumMinionType.ARBITRATOR) {
                EventStore.saveInstance.setBoolean(world, "ArbitratorKilled", true);
                NetworkHandler.dispatchPacketClient(new PacketWorldData("ArbitratorKilled", true));
                TheTitans.debug("First Arbitrator has been killed");
                EventStore.sendMessage(world, TheTitans.translate("event", "pretitan.postarbitrator"));
            }

            if (!EventStore.getBool(world, "GrandmasterKilled") && event.getEntityLiving() instanceof IMinion && ((IMinion)event.getEntityLiving()).fetchKindMinion() == EnumMinionType.GRANDMASTER) {
                EventStore.saveInstance.setBoolean(world, "GrandmasterKilled", true);
                NetworkHandler.dispatchPacketClient(new PacketWorldData("GrandmasterKilled", true));
                TheTitans.debug("First Grandmaster has been killed");
                EventStore.sendMessage(world, TheTitans.translate("event", "pretitan.postgrandmaster"));
            }

            if (!EventStore.getBool(world, "PostLord") && event.getEntityLiving() instanceof IMinion && ((IMinion)event.getEntityLiving()).fetchKindMinion() == EnumMinionType.LORD) {
                EventStore.saveInstance.setBoolean(world, "PostLord", true);
                NetworkHandler.dispatchPacketClient(new PacketWorldData("PostLord", true));
                TheTitans.debug("The world is now post lord");
                EventStore.sendMessage(world, TheTitans.translate("event", "pretitan.postlord"));
            }

            if (!EventStore.getBool(world, "PostFirstTitan") && event.getEntityLiving() instanceof EntityTitan && !((EntityTitan)event.getEntityLiving()).hasSoulNo()) {
                EventStore.saveInstance.setBoolean(world, "PostFirstTitan", true);
                NetworkHandler.dispatchPacketClient(new PacketWorldData("PostFirstTitan", true));
                TheTitans.debug("You beat your first Titan");
                EventStore.sendMessage(world, TheTitans.translate("event", "posttitan.firsttitan"));
            }
        }
        world.profiler.endSection();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() != null) {
            if (TitanConfig.coreModdingIs && event.getGui() instanceof GuiModList)
                // back-target must be the screen we came from (main menu), not a new GuiIngameMenu — the latter NPEs at the main menu (no world) when its Disconnect button is used.
                event.setGui(new GuiNewModList(Minecraft.getMinecraft().currentScreen != null ? Minecraft.getMinecraft().currentScreen : new net.minecraft.client.gui.GuiMainMenu()));
            else if (event.getGui() instanceof GuiGameOver && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getHealth() > 0.0F)
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void whenLoadRealm(Load event) {
        if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
            EventStore.saveInstance = TitanSavedData.fetchInfoStorage(event.getWorld());
        else if (EventStore.saveInstance == null)
            EventStore.saveInstance = TitanSavedData.fetchInfoStorage(event.getWorld());
    }

    @SubscribeEvent
    public void whenUnloadRealm(Unload event) {
        if (event.getWorld().isRemote)
            GuiEntry.instances.clear();
        if (event.getWorld() instanceof WorldServer && EventStore.deleteWorld != null && event.getWorld().equals(EventStore.deleteWorld))
            EventStore.deleteWorld((WorldServer)event.getWorld());
        //EventStore.saveInstance = null;
    }

    @SubscribeEvent
    public void whenGenMap(InitMapGenEvent event) {
        if (event.getType() == EventType.NETHER_BRIDGE) {
            event.setNewGen(new MapGenNetherBridge());
            ((MapGenNetherBridge)event.getNewGen()).getSpawnList().add(new Biome.SpawnListEntry(EntityBlazeMinion.class, 10, 2, 3));
            ((MapGenNetherBridge)event.getNewGen()).getSpawnList().add(new Biome.SpawnListEntry(EntitySkeletonMinion.class, 10, 4, 4));
            ((MapGenNetherBridge)event.getNewGen()).getSpawnList().add(new Biome.SpawnListEntry(EntityPigZombieMinion.class, 5, 4, 4));
        }
    }

    @SubscribeEvent
    public void whenCheckSpawn(CheckSpawn event) {
        //if (!EventStore.getBool(event.getWorld(), "PostWither") && IMinion.class.isAssignableFrom(event.getEntity().getClass()))
        //event.setResult(Result.DENY);
    }

    @SubscribeEvent
    public void createEvent(LivingSpawnEvent event) {
        if (event.getEntityLiving() instanceof EntityVillager)
            ((EntityVillager)event.getEntityLiving()).tasks.addTask(0, new EntityAIAvoidEntity((EntityVillager)event.getEntityLiving(), EntityZombieTitan.class, 96.0F, 1.2D, 1.2D));
    }
}
