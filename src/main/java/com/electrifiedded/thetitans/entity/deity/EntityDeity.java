package com.electrifiedded.thetitans.entity.deity;

import com.electrifiedded.thetitans.entity.titan.EnumTitanStatus;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDeity extends EntityCreature implements IDeity {
    public int deathTicks;

    public EntityDeity(World worldIn) {
        super(worldIn);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        // HP is plain vanilla health; the maxHealth cap is lifted by mixin.MixinRangedAttribute.
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2000.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(512.0D);
    }

    @SideOnly(Side.CLIENT)
    public float fetchScaleShadow() {
        return 0.0F;
    }

    public boolean isEntityAlive() {
        return this.getHealth() > 0.0F;
    }

    protected void despawnEntity() {
        this.idleTime = 0;
    }

    public int getTalkInterval() {
        return 120;
    }

    public void heal(double value) {
        if (value > 0.0D && getHealth() > 0.0F)
            setHealth((float)Math.min((double)getMaxHealth(), getHealth() + value));
    }

    public void hurt(double value) {
        if (!getIsInvulnerable())
            setHealth((float)Math.max(0.0D, getHealth() - MathHelper.clamp(value, 0.0D, cap())));
    }

    protected double cap() {
        return 0.0D;
    }

    public boolean needsCrush() {
        return false;
    }

    public EnumDeityType fetchKindDeity() {
        return null;
    }

    public EnumGodStatus fetchConditionGod() {
        return null;
    }

    public EnumTitanStatus fetchConditionGiant() {
        return null;
    }

    public int fetchTintLabel() {
        return 16777215;
    }
}
