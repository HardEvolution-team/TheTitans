package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.registry.TitanMilestones;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.registry.TitanTrophies;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityUndeadOther extends EntityZombie {
    private static final DataParameter<Integer> KIND_MOB = EntityDataManager.createKey(EntityUndeadOther.class, DataSerializers.VARINT);

    public EntityUndeadOther(World p_i1745_1_) {
        super(p_i1745_1_);
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityGolem.class, 0, false, false, null));
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

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (cause.getTrueSource() instanceof EntityPlayer && this.fetchKindMob() > 0) {
            EntityPlayer player = (EntityPlayer) cause.getTrueSource();
            switch (this.fetchKindMob()) {
                case 1: TitanMilestones.runGrant(player, TitanTrophies.slaygoldguardV); break;
                case 12: TitanMilestones.runGrant(player, TitanTrophies.slayghoulV); break;
                case 13: TitanMilestones.runGrant(player, TitanTrophies.slaywightV);
            }
            TitanMilestones.runGrant(player, TitanTrophies.slayotherzombieV);
        }
    }

    public int fetchKindMob() {
        return this.dataManager.get(KIND_MOB).intValue();
    }

    public void assignKindMob(int miniontype) {
        this.dataManager.set(KIND_MOB, Integer.valueOf(miniontype));
        this.inflictAttributesKind();
        this.setHealth(this.getMaxHealth());
    }

    private void assignAttrKind(double hp, double atk, double spd) {
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(hp);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(atk);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(spd);
    }

    private void inflictAttributesKind() {
        switch (this.fetchKindMob()) {
            case 1:  this.assignAttrKind(80D,   6D,   0.25D);  break; // GoldenGuard
            case 2:  this.assignAttrKind(25D,   4D,   0.23D);  break; // ZombieGray
            case 3:  this.assignAttrKind(35D,   5D,   0.23D);  break; // ZombieGrunt
            case 4:  this.assignAttrKind(14D,   2D,   0.3D);   break; // ZombieStar
            case 5:  this.assignAttrKind(22D,   3D,   0.23D);  break; // ZombieScoffer
            case 6:  this.assignAttrKind(25D,   3D,   0.23D);  break; // ZombieMorge
            case 7:  this.assignAttrKind(30D,   4D,   0.23D);  break; // ZombieFresh
            case 8:  this.assignAttrKind(10D,   1D,   0.23D);  break; // ZombieRotting
            case 9:  this.assignAttrKind(16D,   2D,   0.23D);  break; // ZombieBrat
            case 10: this.assignAttrKind(16D,   2D,   0.23D);  break; // ZombieMutilated
            case 11: this.assignAttrKind(60D,   8D,   0.235D); break; // ZombieHeadless
            case 12: this.assignAttrKind(400D,  30D,  0.3D);   break; // ZombieGhoul (boss)
            case 13: this.assignAttrKind(3000D, 200D, 0.25D);  break; // ZombieWight (boss)
            case 14: this.assignAttrKind(40D,   6D,   0.23D);  break; // ZombieEnd
            default: this.assignAttrKind(20D,   3D,   0.23D);
        }
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        boolean flag = super.attackEntityAsMob(arg70652n1);
        if (flag) {
            if (this.fetchKindMob() == 12 && arg70652n1 instanceof EntityLivingBase) {
                byte b0 = 0;
                if (this.world.getDifficulty() == EnumDifficulty.EASY) {
                    b0 = 5;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 15;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    b0 = 40;
                }

                if (b0 > 0) {
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, b0 * 20, 3));
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, b0 * 20, 0));
                }
            }

            if (this.fetchKindMob() == 13 && arg70652n1 instanceof EntityLivingBase) {
                byte b0 = 0;
                if (this.world.getDifficulty() == EnumDifficulty.EASY) {
                    b0 = 5;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 15;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    b0 = 40;
                }

                if (b0 > 0) {
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, b0 * 20, 11));
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, b0 * 20, 11));
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, b0 * 20, 0));
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, b0 * 20, 0));
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.HUNGER, b0 * 20, 0));
                }
            }

            return true;
        }

        return flag;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.fetchKindMob() < 0)
            this.assignKindMob(0);
        this.isImmuneToFire = this.fetchKindMob() == 1;
        this.experienceValue = (int)(this.getMaxHealth() * 0.25D);
        if (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue() < 32D)
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.inflictAttributesKind();
    }

    public int getTotalArmorValue() {
        switch (this.fetchKindMob()) {
            case 1:
                return super.getTotalArmorValue() + 8;
            case 3:
                return super.getTotalArmorValue() + 6;
            case 11:
                return super.getTotalArmorValue() + 8;
            case 12:
                return super.getTotalArmorValue() + 10;
            case 13:
                return super.getTotalArmorValue() + 14;
            default:
                return super.getTotalArmorValue() + 2;
        }
    }

    public String getName() {
        switch (this.fetchKindMob()) {
            case 1:
                return I18n.translateToLocal("entity.GoldenGuard.name");
            case 2:
                return I18n.translateToLocal("entity.ZombieGray.name");
            case 3:
                return I18n.translateToLocal("entity.ZombieGrunt.name");
            case 4:
                return I18n.translateToLocal("entity.ZombieStar.name");
            case 5:
                return I18n.translateToLocal("entity.ZombieScoffer.name");
            case 6:
                return I18n.translateToLocal("entity.ZombieMorge.name");
            case 7:
                return I18n.translateToLocal("entity.ZombieFresh.name");
            case 8:
                return I18n.translateToLocal("entity.ZombieRotting.name");
            case 9:
                return I18n.translateToLocal("entity.ZombieBrat.name");
            case 10:
                return I18n.translateToLocal("entity.ZombieMutilated.name");
            case 11:
                return I18n.translateToLocal("entity.ZombieHeadless.name");
            case 12:
                return I18n.translateToLocal("entity.ZombieGhoul.name");
            case 13:
                return I18n.translateToLocal("entity.ZombieWight.name");
            case 14:
                return I18n.translateToLocal("entity.ZombieEnd.name");
            default:
                return I18n.translateToLocal("entity.Zombie.name");
        }
    }

    protected SoundEvent getAmbientSound() {
        switch (this.fetchKindMob()) {
            case 1:
                return SoundEvents.ENTITY_BLAZE_AMBIENT;
                // TODO sounds — thetitans:ghoulLiving/wightLiving are unregistered; wrapped raw, resolves via sounds.json
            case 12:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("ghoulLiving");
            case 13:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("wightLiving");
            default:
                return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        switch (this.fetchKindMob()) {
            case 1:
                return SoundEvents.ENTITY_IRONGOLEM_HURT;
                // TODO sounds — thetitans:ghoulGrunt/wightGrunt unregistered
            case 12:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("ghoulGrunt");
            case 13:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("wightGrunt");
            default:
                return SoundEvents.ENTITY_ZOMBIE_HURT;
        }
    }

    protected SoundEvent getDeathSound() {
        switch (this.fetchKindMob()) {
            case 1:
                return SoundEvents.ENTITY_IRONGOLEM_DEATH;
                // TODO sounds — thetitans:ghoulDeath/wightDeath unregistered
            case 12:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("ghoulDeath");
            case 13:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("wightDeath");
            default:
                return SoundEvents.ENTITY_ZOMBIE_DEATH;
        }
    }
}
