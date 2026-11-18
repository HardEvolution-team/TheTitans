package com.electrifiedded.thetitans.entity.titan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityTitanPart extends Entity {
    private ITitanHitbox entity;
    private String partName;
    private boolean needsCrush;

    public EntityTitanPart(World world) {
        super(world);
        preventEntitySpawning = true;
        noClip = true;
    }

    public EntityTitanPart(World world, ITitanHitbox entity, String partName, float width, float height) {
        this(world);
        // assign entity BEFORE setSize — getEntityBoundingBox() below returns null while entity==null, and vanilla setSize dereferences the old box.
        this.entity = entity;
        this.partName = partName;
        setSize(width, height);
        if (entity instanceof Entity)
            setPosition(((Entity)entity).posX, ((Entity)entity).posY, ((Entity)entity).posZ);
    }

    public EntityTitanPart(World world, ITitanHitbox entity, String partName, float width, float height, boolean needsCrush) {
        this(world, entity, partName, width, height);
        this.needsCrush = needsCrush;
    }

    /**Returns the parent entity that this hitbox is apart of*/
    public ITitanHitbox getParent() {
        return entity;
    }

    /**Returns if the hitbox should crush other entities that collide with it*/
    public boolean needsCrush() {
        return needsCrush;
    }

    public String getName() {
        return partName;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean isBurning() {
        return entity != null && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isBurning();
    }

    protected void entityInit() {
    }

    protected void readEntityFromNBT(NBTTagCompound arg70037n1) {
    }

    protected void writeEntityToNBT(NBTTagCompound arg70014n1) {
    }

    public boolean canBeCollidedWith() {
        return entity != null && !isInvisible();
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return isInvisible() || super.isEntityInvulnerable(source);
    }

    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (isEntityInvulnerable(source) || entity == null)
            return false;
        return entity.attackEntityFromPart(this, source, damage);
    }

    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        // null collision box = no collision (vanilla-safe); parts still keep a real bbox for physics.
        return canBeCollidedWith() ? super.getEntityBoundingBox() : null;
    }

    // 1.12.2 runs water/fire physics through getEntityBoundingBox(); returning null (the old 1.7.10 no-collide trick) NPEs in handleWaterMovement. Keep a real box; gate collision via canBeCollidedWith/getCollisionBox instead.

    public void setLocationAndAngles(double arg70012n1, double arg70012n3, double arg70012n5, float arg70012n7, float arg70012n8) {
        if (entity != null && entity instanceof EntityLivingBase) {
            arg70012n1 += ((EntityLivingBase)entity).motionX;
            arg70012n5 += ((EntityLivingBase)entity).motionZ;
        }

        super.setLocationAndAngles(arg70012n1, arg70012n3, arg70012n5, arg70012n7, arg70012n8);
    }

    public void onUpdate() {
        if (entity != null && entity instanceof EntityLivingBase) {
            rotationYaw = ((EntityLivingBase)entity).renderYawOffset;
            if (entity instanceof EntityTitan)
                setInvisible(((EntityTitan)entity).fetchWaiting() || ((EntityTitan)entity).getAnimID() == 13);
            else
                setInvisible(((EntityLivingBase)entity).isInvisible());
        }

        if (entity == null || world == null || (entity instanceof EntityLivingBase && (!((EntityLivingBase)entity).isEntityAlive() || ((EntityLivingBase)entity).isDead)))
            setDead();
        super.onUpdate();
    }

    public boolean isEntityEqual(Entity entityIn) {
        return this == entityIn || entity == entityIn;
    }
}
