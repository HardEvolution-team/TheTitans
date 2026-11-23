package com.electrifiedded.thetitans.item.armor;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.misc.EntityImmortalItem;
import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemAdminiumArmor extends ItemTitanArmor {
    public ItemAdminiumArmor(String materialName, ItemArmor.ArmorMaterial material, int type, float armourGiant, float resistanceGiant) {
        super(materialName, material, type, new double[] {
            5.0E8D, 8.0E8D, 7.0E8D, 4.0E8D
        }

        , armourGiant, resistanceGiant);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityImmortalItem(world, location, itemstack);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return TheTitans.godlyV;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    public void whenTickAudio(World world, EntityLivingBase entity, ItemStack itemStack) {
        if (beatsAudio % 80 == 0)
            if (TheTitans.equippedAll(entity, TitanGear.armourSetAdminium))
            SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.9F, 0.87F, 123);
        else {
            if (TheTitans.equipped(entity, TitanGear.helmetAdminium, 1))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.25F, 0.87F, 123);
            if (TheTitans.equipped(entity, TitanGear.chestplateAdminium, 2))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.25F, 0.87F, 123);
            if (TheTitans.equipped(entity, TitanGear.leggingsAdminium, 3))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.25F, 0.87F, 123);
            if (TheTitans.equipped(entity, TitanGear.bootsAdminium, 4))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.25F, 0.87F, 123);
        }

        super.whenTickAudio(world, entity, itemStack);
    }

    public void armourTick(World world, net.minecraft.entity.EntityLivingBase player, ItemStack itemStack) {
        itemStack.setItemDamage(itemStack.getItemDamage() - 10);
        if (TheTitans.equipped(player, TitanGear.helmetAdminium, 1)) {
            appendLongEffect(player, MobEffects.NIGHT_VISION, 0);
            addEffect(player, MobEffects.WATER_BREATHING, 0);
            discardEffect(player, MobEffects.BLINDNESS);
        }

        if (TheTitans.equipped(player, TitanGear.chestplateAdminium, 2)) {
            addEffect(player, MobEffects.HASTE, 99);
            addEffect(player, MobEffects.RESISTANCE, 3);
            addEffect(player, MobEffects.STRENGTH, 999);
            addEffect(player, MobEffects.FIRE_RESISTANCE, 0);
            discardEffect(player, MobEffects.WEAKNESS);
            discardEffect(player, MobEffects.MINING_FATIGUE);
        }

        if (TheTitans.equipped(player, TitanGear.leggingsAdminium, 3)) {
            discardEffect(player, MobEffects.NAUSEA);
            discardEffect(player, MobEffects.HUNGER);
            discardEffect(player, MobEffects.POISON);
        }

        if (TheTitans.equipped(player, TitanGear.bootsAdminium, 4)) {
            addEffect(player, MobEffects.JUMP_BOOST, 19);
            addEffect(player, MobEffects.SPEED, 39);
            discardEffect(player, MobEffects.SLOWNESS);
            player.stepHeight = 4F;
        }

        if (TheTitans.equippedAll(player, TitanGear.armourSetAdminium)) {
            // TODO re-op player via server.getPlayerList().addOp(profile) — needs MinecraftServer handle
            player.maxHurtResistantTime = 40;
            player.extinguish();
            player.fallDistance *= 0.0F;
            if (player.getHealth() >= player.getMaxHealth() && player.getAbsorptionAmount() <= player.getHealth() * 0.5F)
                player.setAbsorptionAmount(player.getAbsorptionAmount() + player.getHealth() * 1.0E-6F);
            addEffect(player, MobEffects.SATURATION, 199);
            if (player.posY <= -45.0D)
                player.setPosition(player.posX, 200.0D, player.posZ);
            if ((player.motionY > 5.0D) && (!player.isDead))player.motionY = 5.0D;
            if (player.getHealth() < player.getMaxHealth())addEffect(player, MobEffects.INSTANT_HEALTH, 2);
            if (player.posY <= -45D)
                player.setPosition(player.posX, player.world.getTopSolidOrLiquidBlock(new BlockPos((int)player.posX, 0, (int)player.posZ)).getY(), player.posZ);

            for (int i = 0; i < 2; i++) {
                player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, player.posX + (player.getRNG().nextDouble() - 0.5D) * player.width, player.posY - 1.8D + player.getRNG().nextDouble() * player.height, player.posZ + (player.getRNG().nextDouble() - 0.5D) * player.width, 0.0D, 0.0D, 0.0D);
                player.world.spawnParticle(EnumParticleTypes.PORTAL, player.posX + (player.getRNG().nextDouble() - 0.5D) * player.width, player.posY - 1.8D + player.getRNG().nextDouble() * player.height, player.posZ + (player.getRNG().nextDouble() - 0.5D) * player.width, (player.getRNG().nextDouble() - 0.5D) * 2.0D, -player.getRNG().nextDouble(), (player.getRNG().nextDouble() - 0.5D) * 2.0D);
            }

            BlockPos posGround = new BlockPos((int)player.posX, (int)(player.posY - 1D), (int)player.posZ);
            List<?> list;
            if (player.isSneaking() && !player.onGround && player.world.getBlockState(posGround).getMaterial().isSolid()) {
                list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(32.0D, 8.0D, 32.0D));
                player.world.createExplosion(player, player.posX, player.posY - 2D, player.posZ, 2F, player.world.getGameRules().getBoolean("mobGriefing"));
                if ((list != null) && (!list.isEmpty())) {
                    for (int i1 = 0; i1 < list.size(); i1++) {
                        Entity entity = (Entity)list.get(i1);
                        if ((entity != null)) {
                            if (!TheTitans.checkFriendlyFire(player, entity, false) && !(entity instanceof EntityTitan)) {
                                entity.attackEntityFrom((new EntityDamageSource("explosion.player", player)).setDifficultyScaled().setExplosion().setDamageBypassesArmor(), 300.0F);
                                ++entity.motionY;
                                ++entity.posY;
                            }
                        }
                    }
                }
            }

            list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(3.0D, 3.0D, 3.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if (!TheTitans.checkEntityBlacklist(entity, false) && !TheTitans.checkFriendlyFire(player, entity, false) && !(entity instanceof EntityTitan) && !(entity instanceof EntityHarcadiumArrow)) {
                        entity.attackEntityFrom(EntityTitan.isSpawnBossToExemptOre(entity) ? (player instanceof EntityPlayer ? DamageSource.causePlayerDamage((EntityPlayer)player) : DamageSource.causeMobDamage(player)).setDamageBypassesArmor() : DamageSourceExtra.radiation, EntityTitan.isSpawnBossToExemptOre(entity) ? 50000F : 500F);
                        entity.hurtResistantTime = 0;

                        if (entity instanceof EntityLivingBase)
                            addEffect((EntityLivingBase)entity, ClientProxy.giantRadiationCreeper, 5000, 1);
                    }
                }
            }

            list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(64D, 64D, 64D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if (!TheTitans.checkFriendlyFire(player, entity, false) && !(entity instanceof EntityTitan) && !(entity instanceof EntityHarcadiumArrow)) {
                        double d1 = (player.posX - entity.posX) / 64D;
                        double d2 = (player.posY + 1D - entity.posY) / 64D;
                        double d3 = (player.posZ - entity.posZ) / 64D;
                        double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                        double d5 = 1.0D - d4;
                        if (d5 > 0D) {
                            d5 *= d5;
                            entity.motionX += d1 / d4 * d5 * 0.1D;
                            entity.motionY += d2 / d4 * d5 * 0.1D;
                            entity.motionZ += d3 / d4 * d5 * 0.1D;
                        }
                    }
                }
            }

            player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        }

        else {
            player.maxHurtResistantTime = 20;
            player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0D);
        }

        super.armourTick(world, player, itemStack);
    }
}
