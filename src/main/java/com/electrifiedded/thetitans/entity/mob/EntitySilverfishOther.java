package com.electrifiedded.thetitans.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntitySilverfishOther extends EntitySilverfish {
    private static final net.minecraft.network.datasync.DataParameter<Integer> KIND_MOB = net.minecraft.network.datasync.EntityDataManager.createKey(EntitySilverfishOther.class, net.minecraft.network.datasync.DataSerializers.VARINT);

    public EntitySilverfishOther(World p_i1740_1_) {
        super(p_i1740_1_);
        this.setSize(0.3F, 0.3F);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(7, new EntitySilverfishOther.EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_MOB, Integer.valueOf(0));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Type", this.fetchKindMob());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.assignKindMob(tagCompund.getInteger("Type"));
    }

    public int fetchKindMob() {
        return this.dataManager.get(KIND_MOB).intValue();
    }

    public void assignKindMob(int miniontype) {
        this.dataManager.set(KIND_MOB, Integer.valueOf(miniontype));
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        if (super.attackEntityAsMob(arg70652n1)) {
            if (arg70652n1 instanceof EntityLivingBase && fetchKindMob() == 0) {
                byte b0 = 0;
                if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 7;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    b0 = 15;
                }

                if (b0 > 0) {
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.POISON, b0 * 20, 0));
                }
            }

            return true;
        }

        else {
            return false;
        }
    }

    public void onUpdate() {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    public boolean isPotionApplicable(PotionEffect arg70687n1) {
        return arg70687n1.getPotion() == MobEffects.POISON && fetchKindMob() == 0 ? false : super.isPotionApplicable(arg70687n1);
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData arg110161n1) {
        IEntityLivingData p_110161_1_1 = super.onInitialSpawn(difficulty, arg110161n1);
        if (this.world.rand.nextInt(25) == 0) {
            EntityZombie entityskeleton = new EntityZombie(this.world);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(this.world.getDifficultyForLocation(new net.minecraft.util.math.BlockPos(entityskeleton)), (IEntityLivingData)null);
            this.world.spawnEntity(entityskeleton);
            entityskeleton.setChild(true);
            entityskeleton.startRiding(this);
        }

        return p_110161_1_1;
    }

    public String getName() {
        switch (this.fetchKindMob()) {
            case 1:
                return I18n.translateToLocal("entity.Gigafish.name");
            case 2:
                return I18n.translateToLocal("entity.Ironfish.name");
            case 3:
                return I18n.translateToLocal("entity.Scalefish.name");
            case 4:
                return I18n.translateToLocal("entity.Motherfish.name");
            case 5:
                return I18n.translateToLocal("entity.Enderfish.name");
            case 6:
                return I18n.translateToLocal("entity.Endermite.name");
            case 7:
                return I18n.translateToLocal("entity.Dustworm.name");
            default:
                return I18n.translateToLocal("entity.Poisonfish.name");
        }
    }

    public class EntityAIWander extends EntityAIBase {
        private EntityCreature entity;
        private double posXZ;
        private double posYZ;
        private double posZZ;
        private double speed;
        public EntityAIWander(EntityCreature p_i1648_1_, double p_i1648_2_) {
            this.entity = p_i1648_1_;
            this.speed = p_i1648_2_;
            setMutexBits(1);
        }

        public boolean shouldExecute() {
            if (this.entity.ticksExisted >= 100) {
                return false;
            }

            else if (this.entity.getRNG().nextInt(10) != 0) {
                return false;
            }

            else {
                Vec3d vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
                if (vec3 == null) {
                    return false;
                }

                else {
                    this.posXZ = vec3.x;
                    this.posYZ = vec3.y;
                    this.posZZ = vec3.z;
                    return true;
                }
            }
        }

        public boolean shouldContinueExecuting() {
            return !this.entity.getNavigator().noPath();
        }

        public void startExecuting() {
            this.entity.getNavigator().tryMoveToXYZ(this.posXZ, this.posYZ, this.posZZ, this.speed);
        }
    }
}
