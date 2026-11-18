package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.item.armor.ItemTitanArmor;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EventStore {
    public static TitanSavedData saveInstance;
    public static WorldServer deleteWorld;

    public static boolean isGiantStatePre(World world) {
        return !getBool(world, "PreTitanComplete") && !getBool(world, "TitanComplete") && !getBool(world, "PostTitanComplete");
    }

    public static boolean isStateGiant(World world) {
        return !getBool(world, "TitanComplete") && !getBool(world, "PostTitanComplete") && getBool(world, "PreTitanComplete");
    }

    public static boolean isGiantStatePost(World world) {
        return !getBool(world, "PostTitanComplete") && getBool(world, "TitanComplete") && getBool(world, "PreTitanComplete");
    }

    public static boolean isGameEnd(World world) {
        return getBool(world, "PostTitanComplete") && getBool(world, "TitanComplete") && getBool(world, "PreTitanComplete");
    }

    public static boolean getBool(World world, String key) {
        if (saveInstance == null) {
            saveInstance = TitanSavedData.fetchInfoStorage(world);
            TheTitans.error("saveInstance returned null. saveInstance should of been initialized first", new NullPointerException());
            return false;
        }

        return saveInstance.getBoolean(world, key);
    }

    public static void setBool(World world, String key, boolean flag) {
        if (saveInstance == null) {
            saveInstance = TitanSavedData.fetchInfoStorage(world);
            TheTitans.error("saveInstance returned null. saveInstance should of been initialized first", new NullPointerException());
            return;
        }

        saveInstance.setBoolean(world, key, flag);
    }

    public static void sendMessage(World world, String message) {
        if (!world.isRemote && !world.playerEntities.isEmpty())
            sendMessage(world, message, world.playerEntities.toArray());
    }

    public static void sendMessage(World world, String message, Object... players) {
        if (!world.isRemote)
            for (int i = 0; i < players.length; i++)
            if (players[i] != null && players[i] instanceof EntityPlayer)
            ((EntityPlayer)players[i]).sendMessage(new TextComponentTranslation(message));
    }

    public static void queueRealmDelete(String message) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        TheTitans.reflect.set(MinecraftServer.class, server, true, "worldIsBeingDeleted", "field_71290_O");
        server.initiateShutdown();
        deleteWorld = server.worlds[0];
        TheTitans.info("Stopping server");
        if (server.getNetworkSystem() != null)
            server.getNetworkSystem().terminateEndpoints();
        if (server.getPlayerList() != null) {
            TheTitans.info("Remove players");
            for (int i = 0; i < server.getPlayerList().getPlayers().size(); ++i) {
                server.getPlayerList().getPlayers().get(i).connection.disconnect(new TextComponentTranslation(message));
            }
        }

        if (server.worlds != null) {
            for (int i = 0; i < server.worlds.length; ++i) {
                WorldServer worldserver = server.worlds[i];
                MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(worldserver));
                worldserver.flush();
            }
        }

        if (server.getPlayerUsageSnooper().isSnooperRunning())
            server.getPlayerUsageSnooper().stopSnooper();
    }

    public static void deleteWorld(WorldServer world) {
        deleteWorld = null;
        if (world == null)
            return;
        TheTitans.debug("Attempting to delete " + world.getWorldInfo().getWorldName());
        MinecraftServer server = (MinecraftServer)TheTitans.reflect.get(WorldServer.class, world, "mcServer", "field_73061_a");
        server.getActiveAnvilConverter().flushCache();
        if (!server.getActiveAnvilConverter().deleteWorldDirectory(world.getWorldInfo().getWorldName())) {
            TheTitans.warn("Deletion Failed. Retrying...");
            deleteWorld = world;
        }
    }

    public static void damageEntity(EntityLivingBase entity, DamageSource source, float damage) {
        float armor = 0.0F;
        if (damage >= entity.getHealth())
            damage = entity.getHealth() - 0.01F;
        if (damage > 0.0F) {
            if (entity instanceof EntityPlayer) {
                net.minecraft.inventory.EntityEquipmentSlot[] slots = {
                    net.minecraft.inventory.EntityEquipmentSlot.FEET,
                            net.minecraft.inventory.EntityEquipmentSlot.LEGS,
                            net.minecraft.inventory.EntityEquipmentSlot.CHEST,
                            net.minecraft.inventory.EntityEquipmentSlot.HEAD
                };
                for (int i = 0; i < 4; ++i) {
                    net.minecraft.item.ItemStack stack = entity.getItemStackFromSlot(slots[i]);
                    if (!stack.isEmpty()) {
                        stack.setItemDamage((int)Math.min((double)stack.getItemDamage() + (double)damage / 100.0D, (double)stack.getMaxDamage()));
                        if (stack.getItem() instanceof com.electrifiedded.thetitans.item.armor.ItemTitanArmor) {
                            damage *= ((com.electrifiedded.thetitans.item.armor.ItemTitanArmor)stack.getItem()).fetchResistanceGiant();
                            armor += ((com.electrifiedded.thetitans.item.armor.ItemTitanArmor)stack.getItem()).fetchArmourGiant();
                        }
                    }
                }

                if (armor > 24.0F)
                    damage /= armor + 1.0F;
                else
                    damage = (damage * (25.0F - armor)) / 25.0F;
            }

            damage = applyArmorCalculations(entity, source, damage);
            damage = applyPotionDamageCalculations(entity, source, damage);
            // BUGFIX — Inf*0 in the armor math gives NaN, and setHealth(NaN) strands the entity half-dead.
            if (Float.isNaN(damage))
                return;
            float f1 = damage;
            damage = Math.max(damage - entity.getAbsorptionAmount(), 0.0F);
            entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (f1 - damage));
            if (damage != 0.0F) {
                float f2 = entity.getHealth();
                entity.setHealth(f2 - damage);
                entity.getCombatTracker().trackDamage(source, f2, damage);
                entity.setAbsorptionAmount(entity.getAbsorptionAmount() - damage);
                if (!entity.isEntityAlive() && entity.getHealth() <= 0.0F) {
                    entity.onDeath(source);
                    entity.world.setEntityState(entity, (byte)3);
                }
            }
        }
    }

    protected static float applyArmorCalculations(EntityLivingBase entity, DamageSource source, float damage) {
        if (!source.isUnblockable()) {
            int i = 25 - entity.getTotalArmorValue();
            float f1 = damage * (float)i;
            damage = f1 / 25.0F;
        }

        return damage;
    }

    protected static float applyPotionDamageCalculations(EntityLivingBase entity, DamageSource source, float damage) {
        if (source.isDamageAbsolute())
            return damage;
        else {
            int i;
            int j;
            float f1;
            if (entity.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD) {
                i = (entity.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                j = 25 - i;
                f1 = damage * (float)j;
                damage = f1 / 25.0F;
            }

            if (damage <= 0.0F)
                return 0.0F;
            else {
                i = EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), source);
                if (i > 20) {
                    i = 20;
                }

                if (i > 0 && i <= 20) {
                    j = 25 - i;
                    f1 = damage * (float)j;
                    damage = f1 / 25.0F;
                }

                return damage;
            }
        }
    }
}
