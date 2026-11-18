package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.entity.titan.ICanAttack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;

public interface IMinion extends IMob, ICanAttack {
    public void setMinionType(int type);
    public EnumMinionType fetchKindMinion();
    public static EnumMinionType typesMinion(int type) {
        switch (type) {
            case 1: return EnumMinionType.PRIEST;
            case 2: return EnumMinionType.ZEALOT;
            case 3: return EnumMinionType.BISHOP;
            case 4: return EnumMinionType.GUARDIAN;
            case 5: return EnumMinionType.TEMPLAR;
            case 6: return EnumMinionType.ARBITRATOR;
            case 7: return EnumMinionType.GRANDMASTER;
            case 8: return EnumMinionType.LORD;
            default: return EnumMinionType.LOYALIST;
        }
    }

    // variable-rank minions override this to read the dataManager int directly; fixed-rank minions
    // (Dragon/Wither/GhastGuard/GiantZombie) fall back to their constant type's id. Lets callers
    // (TitanBossBarGui, herald gates) read the rank int off IMinion without a per-class downcast.
    default int fetchKindIntMinion() {
        return fetchKindMinion().getID();
    }

    public EntityLiving getMaster();
    public void assignMaster(EntityLiving entity);
    // default attack-everything; minions with an exclusion list override this.
    default boolean allowStrikeGiant(Class<?> clazz) {
        return true;
    }

    default boolean friendlyIs(Entity entity) {
        return false;
    }

    default boolean beginGrounded() {
        return true;
    }

    default String fetchAudioSummon() {
        return "thetitans:titansummonminion";
    }

    default float fetchVolumeSummon() {
        return 2.0F;
    }

    default float fetchPitchSummon() {
        return 1.0F;
    }

    default double fetchYOffsetSummon() {
        return 0.0D;
    }

    static double fetchHpMultiplierMinion(double basestat, int miniontype, int miniontitanstatusid) {
        ++miniontitanstatusid;
        double lordpercent = 0.0D;
        if (miniontitanstatusid == 1) lordpercent = 4.8E10D;
        if (miniontitanstatusid == 2) lordpercent = 4.8E13D;
        if (miniontitanstatusid == 3) lordpercent = 4.8E16D;
        switch (miniontype) {
            case 1: return basestat * 4.0D;
            case 2: return basestat * 15.0D;
            case 3: return basestat * 300.0D;
            case 4: return basestat * 600.0D;
            case 5: return basestat * lordpercent * 0.001D;
            case 6: return basestat * lordpercent * 0.01D;
            case 7: return basestat * lordpercent * 0.1D;
            case 8: return basestat * lordpercent;
            default: return basestat * 1.5D;
        }
    }

    static double fetchStrikeMultiplierMinion(double basestat, int miniontype, int miniontitanstatusid) {
        ++miniontitanstatusid;
        double lordpercent = 0.0D;
        if (miniontitanstatusid == 1) lordpercent = 480000.0D;
        if (miniontitanstatusid == 2) lordpercent = 4.8E7D;
        if (miniontitanstatusid == 3) lordpercent = 4.8E9D;
        switch (miniontype) {
            case 1: return basestat * 1.5D * miniontitanstatusid;
            case 2: return basestat * 3.0D * miniontitanstatusid;
            case 3: return basestat * 30.0D * miniontitanstatusid;
            case 4: return basestat * 60.0D * miniontitanstatusid;
            case 5: return basestat * lordpercent * 0.001D * miniontitanstatusid;
            case 6: return basestat * lordpercent * 0.01D * miniontitanstatusid;
            case 7: return basestat * lordpercent * 0.1D * miniontitanstatusid;
            case 8: return basestat * lordpercent * miniontitanstatusid;
            default: return basestat * 1.0D;
        }
    }

    static double fetchPaceMultiplierMinion(double basestat, int miniontype, int miniontitanstatusid) {
        switch (miniontype) {
            case 1: return basestat * 1.1D;
            case 2: return basestat * 1.2D;
            case 3: return basestat * 1.3D;
            case 4: return basestat * 1.3D;
            case 5: return basestat * 1.4D;
            case 6: return basestat * 1.5D;
            case 7: return basestat * 1.75D;
            case 8: return basestat * 2.0D;
            default: return basestat * 1.0D;
        }
    }

    static int fetchExpMultiplierMinion(int basestat, int miniontype) {
        switch (miniontype) {
            case 1: return basestat * 3;
            case 2: return basestat * 10;
            case 3: return basestat * 500;
            case 4: return basestat * 1000;
            case 5: return basestat * 2000;
            case 6: return basestat * 32000;
            case 7: return basestat * 480000;
            case 8: return basestat * 6400000;
            default: return basestat * 1;
        }
    }
}
