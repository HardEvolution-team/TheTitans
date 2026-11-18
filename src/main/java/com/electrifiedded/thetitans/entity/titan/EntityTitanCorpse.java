package com.electrifiedded.thetitans.entity.titan;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntityTitanCorpse extends Entity {
    private static final DataParameter<Integer> KIND_CORPSE = EntityDataManager.createKey(EntityTitanCorpse.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> KIND_SUB = EntityDataManager.createKey(EntityTitanCorpse.class, DataSerializers.VARINT);

    public EntityTitanCorpse(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.ignoreFrustumCheck = true;
    }

    protected void entityInit() {
        this.dataManager.register(KIND_CORPSE, Integer.valueOf(0));
        this.dataManager.register(KIND_SUB, Integer.valueOf(0));
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

    protected void writeEntityToNBT(NBTTagCompound tagCompound) {}

    public int fetchKindCorpse() {
        return this.dataManager.get(KIND_CORPSE).intValue();
    }

    public void assignKindCorpse(int type) {
        this.dataManager.set(KIND_CORPSE, Integer.valueOf(type));
    }

    public int getSubType() {
        return this.dataManager.get(KIND_SUB).intValue();
    }

    public void setSubType(int type) {
        this.dataManager.set(KIND_SUB, Integer.valueOf(type));
    }

    public String getName() {
        switch (this.fetchKindCorpse()) {
            case 1:
                return I18n.translateToLocal("entity.thetitans.SilverfishTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 2:
                return I18n.translateToLocal("entity.thetitans.CaveSpiderTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 3:
                return I18n.translateToLocal("entity.thetitans.SpiderTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 4:
                return I18n.translateToLocal("entity.thetitans.SkeletonTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 5:
                return I18n.translateToLocal("entity.thetitans.WitherSkeletonTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 6:
                return I18n.translateToLocal("entity.thetitans.ZombieTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 7:
                return I18n.translateToLocal("entity.thetitans.CreeperTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 8:
                return I18n.translateToLocal("entity.thetitans.PigZombieTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 9:
                return I18n.translateToLocal("entity.thetitans.BlazeTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 10:
                return I18n.translateToLocal("entity.thetitans.EndermanTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            case 11:
                return I18n.translateToLocal("entity.thetitans.GhastTitan." + this.getSubType() + ".name") + I18n.translateToLocal("entity.TitanCorpse.name");
            default:
                return "Titan" + I18n.translateToLocal("entity.TitanCorpse.name");
        }
    }

    public void onUpdate() {}
}
