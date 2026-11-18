package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.entity.minion.IMinion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;

public class EntityEnderColossusCrystal extends EntityFlying implements IMinion {
    public int courseChangeCooldown;
    public double xWaypoint;
    public double yWaypoint;
    public double zWaypoint;
    public int innerRotation;
    public EntityEnderColossus owner;

    public EntityEnderColossusCrystal(World p_i1735_1_) {
        super(p_i1735_1_);
        enablePersistence();
        setSize(2.0F, 2.0F);
        this.innerRotation = this.rand.nextInt(100000);
        this.experienceValue = 10;
        this.isAirBorne = true;
        this.onGround = false;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public void setDead() {
        super.setDead();
        if (this.owner != null) {
            this.owner.ofCrystalsNum -= 1;
        }
    }

    protected void onDeathUpdate() {
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.99D;
        this.motionY *= 0.99D;
        this.motionZ *= 0.99D;
        this.motionY -= 0.1D;
        if (this.isBurning())
            this.onGround = true;
        BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
        BlockPos below = new BlockPos(this.posX, this.posY - 1, this.posZ);
        if (!this.world.isRemote && (this.isEntityInsideOpaqueBlock() || !this.world.isBlockLoaded(pos) || this.onGround || this.world.getBlockState(below).getMaterial().isSolid())) {
            if ((!this.world.isRemote) && (this.world.getGameRules().getBoolean("doMobLoot"))) {
                entityDropItem(new ItemStack(Blocks.GLASS, 1), 20.0F);
                entityDropItem(new ItemStack(Blocks.GLASS, 1), 28.0F);
                int i = 50;
                while (i > 0) {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 20.0D, this.posZ, j));
                }
            }

            for (int i = 0; i < 200; i++) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, d0, d1);
            }

            setDead();
            if (!this.world.isRemote) {
                this.world.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 6.0F * this.rand.nextInt(9) + 6.0F, true);
            }
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.refreshActionModeCrystal();
        this.refreshColossusEnderCrystalEnder();
        this.ignoreFrustumCheck = true;
        this.innerRotation += 1;
    }

    private void refreshActionModeCrystal() {
        double d0 = this.xWaypoint - this.posX;
        double d1 = this.yWaypoint - this.posY;
        double d2 = this.zWaypoint - this.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (this.getDistanceSq(xWaypoint, yWaypoint, zWaypoint) > 10000D) {
            double d5 = (double)MathHelper.sqrt(d3);
            this.motionX += (d0 / d5 * 0.8D - this.motionX);
            this.motionY += (d1 / d5 * 0.8D - this.motionY);
            this.motionZ += (d2 / d5 * 0.8D - this.motionZ);
        }

        if ((d3 < 1.0D) || (d3 > 20000.0D)) {
            if (this.owner != null) {
                this.xWaypoint = (this.owner.posX + (this.rand.nextFloat() * 4.0F - 2.0F) * (this.owner.width * 2F));
                this.yWaypoint = (this.owner.posY + this.owner.height + 48F + (this.rand.nextFloat() * 2.0F - 1.0F) * 24.0F);
                this.zWaypoint = (this.owner.posZ + (this.rand.nextFloat() * 4.0F - 2.0F) * (this.owner.width * 2F));
            }

            else {
                this.xWaypoint = (this.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.yWaypoint = (this.posY + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.zWaypoint = (this.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            }
        }

        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;
            d3 = MathHelper.sqrt(d3);
            if (isTraversableCourse(this.xWaypoint, this.yWaypoint, this.zWaypoint, d3)) {
                if (d3 > 4048.0D) {
                    this.motionX += d0 / d3 * 0.2D;
                    this.motionY += d1 / d3 * 0.2D;
                    this.motionZ += d2 / d3 * 0.2D;
                }

                else {
                    this.motionX += d0 / d3 * 0.1D;
                    this.motionY += d1 / d3 * 0.1D;
                    this.motionZ += d2 / d3 * 0.1D;
                }
            }

            else {
                this.xWaypoint = this.posX;
                this.yWaypoint = this.posY;
                this.zWaypoint = this.posZ;
            }
        }

        if (this.owner == null) {
            List<?> found1111 = this.world.getEntitiesWithinAABB(EntityEnderColossus.class, this.getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
            if ((found1111 != null) && (!found1111.isEmpty())) {
                for (int i1 = 0; i1 < found1111.size(); i1++) {
                    Entity mobRef1 = (Entity)found1111.get(i1);
                    if ((mobRef1 != null) && ((mobRef1 instanceof EntityEnderColossus)) && (((EntityEnderColossus)mobRef1).ofCrystalsNum < 20)) {
                        this.owner = ((EntityEnderColossus)mobRef1);
                        this.owner.ofCrystalsNum += 1;
                    }
                }
            }
        }
    }

    private boolean isTraversableCourse(double arg70790n1, double arg70790n3, double arg70790n5, double arg70790n7) {
        double d4 = (this.xWaypoint - this.posX) / arg70790n7;
        double d5 = (this.yWaypoint - this.posY) / arg70790n7;
        double d6 = (this.zWaypoint - this.posZ) / arg70790n7;
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        for (int i = 1; i < arg70790n7; i++) {
            axisalignedbb = axisalignedbb.offset(d4, d5, d6);
            if (!this.world.getCollisionBoxes(this, axisalignedbb).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void refreshColossusEnderCrystalEnder() {
        if (this.owner != null) {
            if (this.isDead) {
                this.owner.attackEntityFromPart(owner.irisRight, (new DamageSource("blindness")).setDamageBypassesArmor().setDamageIsAbsolute(), 1000.0F);
                this.owner.hurtResistantTime = 0;
                this.owner.attackEntityFromPart(owner.irisLeft, (new DamageSource("blindness")).setDamageBypassesArmor().setDamageIsAbsolute(), 1000.0F);
                this.owner.hurtResistantTime = 0;
                if (!this.owner.stunnedIs)
                    ++this.owner.crystalsDestroyed;
                this.owner = null;
            }

            else if (this.isEntityAlive() && !this.owner.stunnedIs) {
                this.owner.heal(60F);
            }
        }

        if (this.owner == null && this.rand.nextInt(10) == 0) {
            float f = 256.0F;
            List<?> list = this.world.getEntitiesWithinAABB(EntityEnderColossus.class, this.getEntityBoundingBox().grow(f, f, f));
            EntityEnderColossus entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            Iterator<?> iterator = list.iterator();
            while (iterator.hasNext()) {
                EntityEnderColossus entityendercrystal1 = (EntityEnderColossus)iterator.next();
                double d1 = entityendercrystal1.getDistanceSq(this);
                if (d1 < d0) {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.owner = entityendercrystal;
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntityEndermanMinion)) || ((source.getTrueSource() instanceof EntityEnderColossus)) || ((source.getTrueSource() instanceof EntityDragon)) || ((source.getTrueSource() instanceof EntityEnderColossusCrystal)) || ((source.getTrueSource() instanceof EntityDragonMinion))) {
            return false;
        }

        if (source.isExplosion() || source.isFireDamage())
            this.onGround = true;
        return super.attackEntityFrom(source, amount);
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_IRONGOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRONGOLEM_DEATH;
    }

    public EnumMinionType fetchKindMinion() {
        return EnumMinionType.SPECIAL;
    }

    @Override
    public void setMinionType(int type) {
        // TODO Auto-generated method stub
    }

    @Override
    public EntityLiving getMaster() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void assignMaster(EntityLiving entity) {
        // TODO Auto-generated method stub
    }
}
