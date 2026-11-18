package com.electrifiedded.thetitans.entity.deity;

import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCaveSpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGhastGuard;
import com.electrifiedded.thetitans.entity.minion.EntityGhastMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGiantZombieBetter;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySilverfishMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossusCrystal;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EnumTitanStatus;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.base.Predicate;

public interface IDeity {
    public static final Predicate<Entity> bloodUser = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (e instanceof EntityPlayer && ((EntityPlayer)e).capabilities.isCreativeMode ? false : (EntityTitan.isDudMobA(e) ? false : e.isEntityAlive() && !e.isDead && (e instanceof EntityPlayer || e instanceof EntityVillager || e instanceof EntityGolem || e instanceof EntityTameable)));
        }
    };

    public static final Predicate<Entity> forAThingToKillSearch = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : (e instanceof EntityTitan && !(e instanceof EntitySnowGolemTitan) && !(e instanceof EntityIronGolemTitan) && !(e instanceof EntityGargoyleTitan) && !(e instanceof EntityWitherzilla) ? false : (e instanceof EntityPlayer && ((EntityPlayer)e).capabilities.isCreativeMode ? false : (e instanceof EntityAnimal && !(e instanceof EntityTameable) ? false : (e instanceof EntityAmbientCreature ? false : (e instanceof EntityWaterMob ? false : (e instanceof IMob ? false : (e instanceof IMinion ? false : (EntityTitan.isDudMobA(e) ? false : e.isEntityAlive() && !e.isDead))))))))));
        }
    };

    public static final Predicate<Entity> giantSorterBlaze = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntityBlazeMinion) && !(e instanceof EntityBlazeTitan)));
        }
    };

    public static final Predicate<Entity> spiderGiantSorterCave = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntityCaveSpiderMinion) && !(e instanceof EntityCaveSpiderTitan)));
        }
    };

    public static final Predicate<Entity> giantSorterCreeper = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntityCreeperMinion) && !(e instanceof EntityCreeperTitan)));
        }
    };

    public static final Predicate<Entity> colossusSorterEnder = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntityEndermanMinion) && !(e instanceof EntityEnderColossus) && !(e instanceof EntityDragon) && !(e instanceof EntityDragonMinion) && !(e instanceof EntityEnderColossusCrystal)));
        }
    };

    public static final Predicate<Entity> giantSorterGhast = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntityGhastMinion) && !(e instanceof EntityGhastTitan)));
        }
    };

    public static final Predicate<Entity> cubeGiantSorterMagma = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntityMagmaCube) && (!(e instanceof EntityMagmaCubeTitan) || e instanceof EntitySlimeTitan)));
        }
    };

    public static final Predicate<Entity> zombieGiantSorterPig = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntityPigZombieMinion) && !(e instanceof EntityGhastGuard) && !(e instanceof EntityPigZombieTitan)));
        }
    };

    public static final Predicate<Entity> giantSorterSilverfish = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntitySilverfishMinion) && !(e instanceof EntitySilverfishTitan)));
        }
    };

    public static final Predicate<Entity> giantSorterSkeleton = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntitySkeletonMinion) && !(e instanceof EntitySkeletonTitan) && !(e instanceof EntityWitherMinion)));
        }
    };

    public static final Predicate<Entity> giantSorterSlime = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : (!(e instanceof EntitySlime) || e instanceof EntityMagmaCube) && (!(e instanceof EntitySlimeTitan) || e instanceof EntityMagmaCubeTitan)));
        }
    };

    public static final Predicate<Entity> giantSorterSpider = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.quackosIs(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntitySpiderMinion) && (!(e instanceof EntitySpiderTitan) || e instanceof EntityCaveSpiderTitan)));
        }
    };

    public static final Predicate<Entity> giantSorterZombie = new Predicate<Entity>() {
        public boolean apply(Entity e) {
            return e == null ? false : (EntityTitan.isDudMobA(e) ? false : (e instanceof EntityTitanSpirit ? false : !(e instanceof EntityZombieMinion) && !(e instanceof EntityZombieTitan) && !(e instanceof EntityGiantZombieBetter)));
        }
    };

    int fetchTintLabel();

    boolean needsCrush();

    EnumDeityType fetchKindDeity();

    EnumGodStatus fetchConditionGod();

    EnumTitanStatus fetchConditionGiant();
}
