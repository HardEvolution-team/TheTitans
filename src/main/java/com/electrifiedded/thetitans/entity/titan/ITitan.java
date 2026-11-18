package com.electrifiedded.thetitans.entity.titan;

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

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Loader;

import com.google.common.base.Predicate;

public abstract interface ITitan extends ICanAttack {
    public static final Predicate<Entity> forAThingToKillSearch = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            if (arg179983n1 == null)
                return false;
            else if (arg179983n1 instanceof EntityTitanSpirit)
                return false;
            else if (arg179983n1 instanceof EntityTitan && !(arg179983n1 instanceof EntitySnowGolemTitan) && !(arg179983n1 instanceof EntityIronGolemTitan) && !(arg179983n1 instanceof EntityGargoyleTitan) && !(arg179983n1 instanceof EntityWitherzilla))
                return false;
            else if (arg179983n1 instanceof IMinion)
                return false;
            else if (arg179983n1 instanceof EntityPlayer && ((EntityPlayer)arg179983n1).capabilities.isCreativeMode)
                return false;
            else if ((arg179983n1 instanceof EntityAnimal && !(arg179983n1 instanceof EntityTameable)) || arg179983n1 instanceof EntityZombie || arg179983n1 instanceof EntitySkeleton || arg179983n1 instanceof EntitySpider || arg179983n1 instanceof EntityCreeper || arg179983n1 instanceof EntityEnderman || arg179983n1 instanceof EntityBlaze || arg179983n1 instanceof EntityGhast || arg179983n1 instanceof EntityWither || arg179983n1 instanceof EntityDragon || arg179983n1 instanceof EntitySilverfish || arg179983n1 instanceof EntitySlime || arg179983n1 instanceof EntityGiantZombieBetter)
                return false;
            else
                return arg179983n1.isEntityAlive();
        }
    };

    public static final Predicate<Entity> giantSorterBlaze = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntityBlazeMinion)) && (!(arg179983n1 instanceof EntityBlazeTitan));
        }
    };

    public static final Predicate<Entity> spiderGiantSorterCave = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntityCaveSpiderMinion)) && (!(arg179983n1 instanceof EntityCaveSpiderTitan));
        }
    };

    public static final Predicate<Entity> giantSorterCreeper = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntityCreeperMinion)) && (!(arg179983n1 instanceof EntityCreeperTitan));
        }
    };

    public static final Predicate<Entity> colossusSorterEnder = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntityEndermanMinion)) && (!(arg179983n1 instanceof EntityEnderColossus)) && (!(arg179983n1 instanceof EntityDragon)) && (!(arg179983n1 instanceof EntityDragonMinion)) && (!(arg179983n1 instanceof EntityEnderColossusCrystal));
        }
    };

    public static final Predicate<Entity> giantSorterGhast = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntityGhastMinion)) && (!(arg179983n1 instanceof EntityGhastTitan));
        }
    };

    public static final Predicate<Entity> cubeGiantSorterMagma = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntityMagmaCube)) && ((!(arg179983n1 instanceof EntityMagmaCubeTitan)) || ((arg179983n1 instanceof EntitySlimeTitan)));
        }
    };

    public static final Predicate<Entity> zombieGiantSorterPig = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntityPigZombieMinion)) && (!(arg179983n1 instanceof EntityGhastGuard)) && (!(arg179983n1 instanceof EntityPigZombieTitan));
        }
    };

    public static final Predicate<Entity> giantSorterSilverfish = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntitySilverfishMinion)) && (!(arg179983n1 instanceof EntitySilverfishTitan));
        }
    };

    public static final Predicate<Entity> giantSorterSkeleton = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntitySkeletonMinion)) && (!(arg179983n1 instanceof EntitySkeletonTitan)) && (!(arg179983n1 instanceof EntityWitherMinion));
        }
    };

    public static final Predicate<Entity> giantSorterSlime = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return ((!(arg179983n1 instanceof EntitySlime)) || ((arg179983n1 instanceof EntityMagmaCube))) && ((!(arg179983n1 instanceof EntitySlimeTitan)) || ((arg179983n1 instanceof EntityMagmaCubeTitan)));
        }
    };

    public static final Predicate<Entity> giantSorterSpider = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntitySpiderMinion)) && (!(arg179983n1 instanceof EntitySpiderTitan) || (arg179983n1 instanceof EntityCaveSpiderTitan));
        }
    };

    public static final Predicate<Entity> giantSorterZombie = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return (!(arg179983n1 instanceof EntityZombieMinion)) && (!(arg179983n1 instanceof EntityZombieTitan)) && (!(arg179983n1 instanceof EntityGiantZombieBetter));
        }
    };

    public boolean needsCrush();
    public EnumTitanStatus fetchConditionGiant();
}
