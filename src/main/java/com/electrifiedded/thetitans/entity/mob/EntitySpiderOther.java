package com.electrifiedded.thetitans.entity.mob;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntitySpiderOther extends EntitySpider {
    private static final DataParameter<Integer> KIND_MOB = EntityDataManager.createKey(EntitySpiderOther.class, DataSerializers.VARINT);

    public EntitySpiderOther(World p_i1743_1_) {
        super(p_i1743_1_);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Type", this.fetchKindMob());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.assignKindMob(tagCompund.getInteger("Type"));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_MOB, Integer.valueOf(0));
    }

    public int fetchKindMob() {
        return this.dataManager.get(KIND_MOB).intValue();
    }

    public void assignKindMob(int miniontype) {
        this.dataManager.set(KIND_MOB, Integer.valueOf(miniontype));
        switch (this.fetchKindMob()) {
            case 1: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7D);
                    this.heal(80F);
                }

            case 2: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
                    this.heal(50F);
                }

            case 3: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.275D);
                    this.heal(12F);
                }

            case 4: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D);
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225D);
                    this.heal(24F);
                }

            case 5: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2375D);
                    this.heal(30F);
                }

            default: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D);
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
                    this.heal(20F);
                }
        }
    }

    public int getTotalArmorValue() {
        int i = super.getTotalArmorValue() + (this.fetchKindMob() == 4 ? 10 : 0);
        if (i > 20) {
            i = 20;
        }

        return i;
    }

    public String getName() {
        switch (this.fetchKindMob()) {
            case 1:
                return I18n.translateToLocal("entity.SpiderGiant.name");
            case 2:
                return I18n.translateToLocal("entity.SpiderLarge.name");
            case 3:
                return I18n.translateToLocal("entity.SpiderSmall.name");
            case 4:
                return I18n.translateToLocal("entity.SpiderArmored.name");
            case 5:
                return I18n.translateToLocal("entity.SpiderOrb.name");
            default:
                return I18n.translateToLocal("entity.SpiderUndead.name");
        }
    }
}
