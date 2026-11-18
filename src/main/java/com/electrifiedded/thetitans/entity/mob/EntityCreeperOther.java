package com.electrifiedded.thetitans.entity.mob;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCreeperOther extends EntityCreeper {
    private static final DataParameter<Integer> KIND_MOB = EntityDataManager.createKey(EntityCreeperOther.class, DataSerializers.VARINT);

    public EntityCreeperOther(World p_i1733_1_) {
        super(p_i1733_1_);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_MOB, Integer.valueOf(0));
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.assignKindMob(tagCompund.getInteger("Type"));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Type", this.fetchKindMob());
    }

    public int fetchKindMob() {
        return this.dataManager.get(KIND_MOB).intValue();
    }

    public void assignKindMob(int miniontype) {
        this.dataManager.set(KIND_MOB, Integer.valueOf(miniontype));
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CREEPER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }

    // 1.7.10 override hand-copied EntityCreeper.onUpdate (fuse + explode) plus the whole
    // EntityLiving render/head-turn loop via private SRG fields. 1.12.2 EntityCreeper.onUpdate already
    // runs the identical fuse/explode logic, so we only keep the extra peaceful-despawn behavior.
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }
}
