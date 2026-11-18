package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityChaff extends Entity {
    private static final DataParameter<Integer> LIFESPAN_V = EntityDataManager.<Integer>createKey(EntityChaff.class, DataSerializers.VARINT);
    public int lifespan;

    public EntityChaff(World worldIn) {
        super(worldIn);
        setSize(1.0F, 1.0F);
        this.preventEntitySpawning = true;
    }

    protected void entityInit() {
        this.dataManager.register(LIFESPAN_V, Integer.valueOf(this.lifespan));
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.dataManager.set(LIFESPAN_V, Integer.valueOf(this.lifespan));
        MathHelper.floor(this.posX);
        MathHelper.floor(this.posY);
        MathHelper.floor(this.posZ);
        float f = (this.rand.nextFloat() - 0.5F) * 16.0F;
        float f1 = (this.rand.nextFloat() - 0.5F) * 16.0F;
        float f2 = (this.rand.nextFloat() - 0.5F) * 16.0F;
        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
        this.lifespan += 1;
        if (this.lifespan == 1) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:chaffDeployment", this.world, this, 5.0F, 1.0F);
        }

        if (this.lifespan == 300) {
            setDead();
        }

        List<?> list = this.world.getEntitiesWithinAABB(EntityHomingWitherSkull.class, this.getEntityBoundingBox().grow(24.0D, 24.0D, 24.0D));
        if ((list != null) && (!list.isEmpty())) {
            for (int i1 = 0; i1 < list.size(); i1++) {
                Entity entity = (Entity)list.get(i1);
                if (((entity instanceof EntityHomingWitherSkull)) && (entity != null)) {
                    ((EntityHomingWitherSkull)entity).mobAssgined = null;
                }
            }
        }
    }

    protected void collideWithNearbyEntities() {
    }
}
