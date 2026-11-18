package com.electrifiedded.thetitans.effect;

import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.proxy.CommonProxy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;

import java.util.List;

/** Single class for all five custom Titans potion effects; behaviour is selected by {@link Kind}. */
public class TitanPotion extends Potion {
    public enum Kind {
        BLEEDING(20, false),
                RADIATION(20, false),
                ADVANCED_WITHER(40, false),
                DEATH(40, true),
                ELECTRIC_JUDGEMENT(60, true);

        final int baseInterval;
        final boolean capAmplifier;

        Kind(int baseInterval, boolean capAmplifier) {
            this.baseInterval = baseInterval;
            this.capAmplifier = capAmplifier;
        }
    }

    private final Kind kind;

    public TitanPotion(Kind kind, boolean isBadEffect, int liquidColor) {
        super(isBadEffect, liquidColor);
        this.kind = kind;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int k = kind.baseInterval >> amplifier;
        boolean ok = kind.capAmplifier ? (k > 0 && amplifier < kind.baseInterval) : k > 0;
        return ok ? duration % k == 0 : true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        switch (kind) {
            case BLEEDING:
                entity.hurtResistantTime = 0;
                entity.attackEntityFrom(new DamageSource("bleeding").setDamageBypassesArmor().setMagicDamage(), 1.0F + (float)amplifier);
                break;
            case RADIATION:
                if (entity.isEntityAlive()) {
                    entity.attackEntityFrom(DamageSourceExtra.radiation, 2.0F * amplifier);
                    entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.HOSTILE, 2F, 2F);
                }
                break;
            case ADVANCED_WITHER:
                entity.hurtResistantTime = 0;
                entity.attackEntityFrom((new DamageSourceExtra("advancedwither")).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), 1F * amplifier);
                break;
            case DEATH:
                entity.attackEntityFrom(DamageSourceExtra.destroy, 40F * amplifier);
                entity.setFire(20);
                if (entity.deathTime > 0) {
                    ++entity.deathTime;
                    if (entity.deathTime > 20)
                        entity.setDead();
                }
                if (entity.getMaxHealth() > 1000000000F && isVictimMortal(entity))
                    halveDetonateOr(entity, false);
                break;
            case ELECTRIC_JUDGEMENT:
                if (entity.isEntityAlive()) {
                    if (!entity.world.isRemote)
                        entity.world.addWeatherEffect(new EntityLightningBolt(entity.world, entity.posX - 0.5D, entity.posY, entity.posZ - 0.5D, false));
                    entity.attackEntityFrom(DamageSourceExtra.lightningBolt, 5.0F * amplifier + amplifier);
                    entity.setFire(20 + amplifier);
                    if ((entity.getMaxHealth() > 1000000000F || entity.height < 0.5F) && isVictimMortal(entity))
                        halveDetonateOr(entity, true);
                }
                break;
        }
        super.performEffect(entity, amplifier);
    }

    private static boolean isVictimMortal(EntityLivingBase e) {
        return !(e instanceof EntityTitanSpirit) && !(e instanceof EntityTitan) && !(e instanceof EntityPlayer);
    }

    private static void halveDetonateOr(EntityLivingBase entity, boolean spreadJudgement) {
        entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F, 1F + entity.getRNG().nextFloat());
        entity.setHealth(entity.getHealth() / 2F);
        if (entity.getHealth() > 1F) return;
        entity.world.createExplosion(null, entity.posX, entity.posY, entity.posZ, 7F, false);
        entity.setDead();
        if (!spreadJudgement) return;
        List<EntityLivingBase> nearby = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(7, 7, 7), e -> e != entity);
        for (EntityLivingBase other : nearby)
            other.addPotionEffect(new PotionEffect(CommonProxy.electricJudgment, Integer.MAX_VALUE, 9));
    }
}
