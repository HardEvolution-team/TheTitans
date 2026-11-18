package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityXPBomb extends EntityThrowable {
    private static final DataParameter<Integer> TOTAL_XP = EntityDataManager.<Integer>createKey(EntityXPBomb.class, DataSerializers.VARINT);
    public int xpColor;
    private int capXp;
    private int ageBomb;

    public EntityXPBomb(World p_i1773_1_) {
        super(p_i1773_1_);
        capXp = 0;
        rotationYaw = (float)(Math.random() * 360.0D);
        motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 3F);
        motionY = (double)((float)(Math.random() * 0.2D) * 2.0F) + 0.5D;
        motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 3F);
    }

    public EntityXPBomb(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        this(p_i1775_1_);
        setPosition(p_i1775_2_, p_i1775_4_, p_i1775_6_);
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(TOTAL_XP, Integer.valueOf(0));
    }

    public int fetchTotalXP() {
        return dataManager.get(TOTAL_XP);
    }

    public void assignTotalXP(int arg82215n1) {
        dataManager.set(TOTAL_XP, Integer.valueOf(arg82215n1));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound arg70014n1) {
        super.writeEntityToNBT(arg70014n1);
        arg70014n1.setShort("Value", (short)fetchTotalXP());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound arg70037n1) {
        super.readEntityFromNBT(arg70037n1);
        assignTotalXP(arg70037n1.getShort("Value"));
    }

    public boolean isBurning() {
        return false;
    }

    protected float getGravityVelocity() {
        return 0.05F;
    }

    protected void onImpact(RayTraceResult movingObject) {
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    protected void whenCollide(EntityPlayer player) {
        if (fetchTotalXP() > 0) {
            player.addExperience(MathHelper.clamp(fetchTotalXP() - 1000, 0, 1000));
            assignTotalXP(Math.max(fetchTotalXP() - 1000, 0));
        }

        else
            setDead();
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source))
            return false;
        else {
            if (!world.isRemote && !isDead && source.getTrueSource() != null && !source.isExplosion()) {
                for (int i1 = 0; i1 < 48; i1++) {
                    int i = fetchTotalXP() / 48;
                    EntityXPOrb orb = new EntityXPOrb(world, posX, posY + 1D, posZ, i);
                    orb.motionY += 0.5D;
                    orb.xpValue = i;
                    world.spawnEntity(orb);
                }

                setDead();
            }

            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        float f1 = 0.5F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f1 * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public void setDead() {
        super.setDead();
        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("random.explode", world, this, 5F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("random.orb", world, this, 5F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        world.spawnParticle(fetchTotalXP() >= 2000 ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_LARGE, posX + ((rand.nextDouble() - 0.5F) * width), posY + 3D + ((rand.nextDouble() - 0.5F) * height), posZ + ((rand.nextDouble() - 0.5F) * width), 0.0D, 0.0D, 0.0D);
    }

    public void onUpdate() {
        if (!world.isRemote && ++ageBomb >= 6000) {
            setDead();
            return;
        }
        if (fetchTotalXP() > capXp)
            capXp = fetchTotalXP();
        // PERF — setSize rebuilds the AABB every call; only do it when the size actually changes. With
        // hundreds of orbs (Witherzilla drops 250) the redundant per-tick AABB churn added up.
        float size = 3F * Math.max((float)fetchTotalXP() / (float)capXp, 0.0F);
        if (size != width)
            setSize(size, size);
        EntityPlayer closestPlayer = world.getClosestPlayerToEntity(this, 32D);
        if (closestPlayer != null) {
            double d1 = (closestPlayer.posX - posX) / 32D;
            double d2 = (closestPlayer.posY + (double)closestPlayer.getEyeHeight() - posY) / 32D;
            double d3 = (closestPlayer.posZ - posZ) / 32D;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;
            if (d5 > 0.0D) {
                d5 *= d5;
                motionX += d1 / d4 * d5 * 0.1D;
                motionY += d2 / d4 * d5 * 0.1D;
                motionZ += d3 / d4 * d5 * 0.1D;
            }

            if (getDistanceSq(closestPlayer) < 4.0D)
                whenCollide(closestPlayer);
        }
        onEntityUpdate();
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.03999999910593033D;
        noClip = pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);
        move(MoverType.SELF, motionX, motionY, motionZ);
        float f = 0.98F;
        if (onGround) {
            BlockPos pos = new BlockPos(MathHelper.floor(posX), MathHelper.floor(getEntityBoundingBox().minY) - 1, MathHelper.floor(posZ));
            f = world.getBlockState(pos).getBlock().slipperiness * 0.98F;
        }

        motionX *= (double)f;
        motionY *= 0.9800000190734863D;
        motionZ *= (double)f;
        if (onGround)
            motionY *= -0.5D;
        ++xpColor;
    }
}
