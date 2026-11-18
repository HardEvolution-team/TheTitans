package com.electrifiedded.thetitans.effect;

import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class DamageSourceExtra extends DamageSource {
    public static DamageSource wip = new DamageSourceExtra("wip").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute();
    public static DamageSource destroy = new DamageSourceExtra("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute().setDifficultyScaled();
    public static DamageSource lightningBolt = new DamageSourceExtra("lightningBolt").setFireDamage().setDamageBypassesArmor().setDamageIsAbsolute();
    public static DamageSource crushMind = new DamageSourceExtra("mindcrush").setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode();
    public static DamageSource radiation = new DamageSourceExtra("radiation").setDamageBypassesArmor().setDamageIsAbsolute();
    public static DamageSource onFire = new DamageSourceExtra("onFire").setFireDamage();

    public DamageSourceExtra(String damageTypeIn) {
        super(damageTypeIn);
    }

    public static DamageSource causeStealingHarmSoul(Entity arg92087n0) {
        return new EntityDamageSource("soulSucking", arg92087n0).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode().setMagicDamage();
    }

    public static DamageSource causeHarmSquishing(Entity arg92087n0) {
        return new EntityDamageSource("squash", arg92087n0).setDamageBypassesArmor().setDifficultyScaled();
    }

    public static DamageSource causePiercingMobHarmArmour(Entity arg92087n0) {
        return new EntityDamageSource("mob", arg92087n0).setDamageBypassesArmor().setDamageIsAbsolute();
    }

    public static DamageSource causeGiantHarmAnti(Entity arg92087n0) {
        return new EntityDamageSource("mob", arg92087n0).setDamageBypassesArmor().setDamageIsAbsolute();
    }

    public static DamageSource causeArrowHarmHarcadium(EntityHarcadiumArrow arrow, Entity arg76353n1) {
        return new EntityDamageSourceIndirect("arrow", arrow, arg76353n1).setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute();
    }

    public static DamageSource causeSkullHarmHoming(Entity arg92087n0) {
        return new EntityDamageSource("arrow", arg92087n0).setDamageBypassesArmor();
    }

    public static DamageSource causeSkullHarmVSEnderDragonHoming(Entity arg92087n0) {
        return new EntityDamageSource("arrow", arg92087n0).setDamageBypassesArmor().setExplosion();
    }

    public static DamageSource causeHarmVaroization(Entity arg92087n0) {
        return new EntityDamageSource("vaporize", arg92087n0).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource causeGiantExplosionHarmCreeper(Entity arg92087n0) {
        return new EntityDamageSource("explosion.player", arg92087n0).setDifficultyScaled().setDamageBypassesArmor().setExplosion();
    }
}
