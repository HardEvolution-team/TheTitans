package com.electrifiedded.thetitans.entity.deity;

import net.minecraft.world.World;

public class EntityGod extends EntityDeity {
    public EntityGod(World worldIn) {
        super(worldIn);
    }

    public boolean needsCrush() {
        return false;
    }

    public EnumDeityType fetchKindDeity() {
        return EnumDeityType.God;
    }

    public EnumGodStatus fetchConditionGod() {
        return EnumGodStatus.LESSER;
    }
}
