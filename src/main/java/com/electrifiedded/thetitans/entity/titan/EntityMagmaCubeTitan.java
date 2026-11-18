package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMagmaCubeTitan extends EntitySlimeTitan {
    public EntityMagmaCubeTitan(World worldIn) {
        super(worldIn);
        particlesBeUpwardNeeds = true;
    }

    protected void applyEntityAI() {
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.cubeGiantSorterMagma));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp();
    }

    protected String fetchParticleSlime() {
        return "flame";
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityMagmaCube.class && arg70686n1 != EntityMagmaCubeTitan.class);
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(50) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL);
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.cubeGiantServantSpawnrateMagma;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    protected boolean isValidLightLevel() {
        return true;
    }

    public boolean processMovementLava() {
        return false;
    }

    protected boolean makesOnLandAudio() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected String funcN180487() {
        return "flame";
    }

    protected EntitySlime buildInstanceProto() {
        return new EntityMagmaCube(world);
    }

    protected EntitySlimeTitan createInstance() {
        return new EntityMagmaCubeTitan(world);
    }

    protected Item getDropItem() {
        return Items.MAGMA_CREAM;
    }

    public boolean isBurning() {
        return false;
    }

    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }

    protected String fetchDeathAudioGiant() {
        return "mob.magmacube.jump";
    }

    protected void alterSquishAmount() {
        squishAmount *= 0.95F;
    }

    protected void jump() {
        motionY = (4.0D + getSlimeSize() * 0.33F);
        isAirBorne = true;
        if (getAttackTarget() != null) {
            double d01 = getAttackTarget().posX - posX;
            double d11 = getAttackTarget().posZ - posZ;
            float f21 = MathHelper.sqrt(d01 * d01 + d11 * d11);
            double hor = 1.0D + getSlimeSize() * 0.25F;
            motionX = (d01 / f21 * hor * hor + motionX * hor);
            motionZ = (d11 / f21 * hor * hor + motionZ * hor);
        }
    }

    protected void funcBG180466() {
        motionY = (2.5F + getSlimeSize() * 0.05F);
        isAirBorne = true;
        if (getAttackTarget() != null) {
            double d01 = getAttackTarget().posX - posX;
            double d11 = getAttackTarget().posZ - posZ;
            float f21 = MathHelper.sqrt(d01 * d01 + d11 * d11);
            double hor = 1.0D + getSlimeSize() * 0.25F;
            motionX = (d01 / f21 * hor * hor + motionX * hor);
            motionZ = (d11 / f21 * hor * hor + motionZ * hor);
        }
    }

    protected boolean canDamagePlayer() {
        return true;
    }

    protected int getAttackStrength() {
        if (TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V) == true) {
            return getSlimeSize() * 180;
        }

        return getSlimeSize() * 60;
    }

    protected String getJumpSound() {
        return "mob.magmacube.big";
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        if (source.isFireDamage()) {
            heal(amount);
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    protected double cap() {
        return super.cap();
    }
}
