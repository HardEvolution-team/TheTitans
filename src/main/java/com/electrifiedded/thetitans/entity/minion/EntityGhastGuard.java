package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.ITitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAIHurtByTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityGhastGuard extends EntityCreature implements IMob, IMinion {
    private static final DataParameter<Byte> ATTACKING = EntityDataManager.createKey(EntityGhastGuard.class, DataSerializers.BYTE);
    public EntityLiving master;
    public int courseChangeCooldown;
    public double xWaypoint;
    public double yWaypoint;
    public double zWaypoint;
    private Entity targetedEntity;
    private int rechargeAggro;
    public int strikeCounterPrev;
    public int counterStrike;
    private int explosionStrength = 3;

    public EntityGhastGuard(World worldIn) {
        super(worldIn);
        enablePersistence();
        setSize(4.5F, 4.5F);
        this.isImmuneToFire = true;
        this.experienceValue = 20;
        this.targetTasks.addTask(0, new EntityAIHurtByTargetTitan(this, true, new Class[] {
            EntityPigZombieMinion.class, EntityPigZombieTitan.class, EntityGhastGuard.class
        }
        ));
        if (TheTitans.ffaModeTitans)
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.zombieGiantSorterPig));
        else
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    public void setDead() {
        super.setDead();
        if ((this.master != null) && ((this.master instanceof EntityTitan))) {
            ((EntityTitan)this.master).retractNumFromKindMinion(fetchKindMinion());
        }
    }

    public EnumMinionType fetchKindMinion() {
        return EnumMinionType.SPECIAL;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityPigZombieMinion.class) && (arg70686n1 != EntityGhastGuard.class) && ((arg70686n1 != EntityPigZombieTitan.class) || (arg70686n1 == EntityZombieTitan.class));
    }

    // render texture switch; guard is "attacking" while its shoot-charge counter is winding up
    public boolean isAttacking() {
        return this.counterStrike > 0;
    }

    protected void despawnEntity() {
        this.idleTime = 0;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void updateFallState(double y, boolean onGround, net.minecraft.block.state.IBlockState state, BlockPos pos) {
    }

    public void travel(float arg70612n1, float arg70612n2, float arg70612n3) {
        if (isInWater()) {
            moveRelative(arg70612n1, arg70612n2, arg70612n3, 0.02F);
            move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }

        else if (isInLava()) {
            moveRelative(arg70612n1, arg70612n2, arg70612n3, 0.02F);
            move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }

        else {
            float f2 = 0.91F;
            if (this.onGround) {
                f2 = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            moveRelative(arg70612n1, arg70612n2, arg70612n3, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= f2;
            this.motionY *= f2;
            this.motionZ *= f2;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    public boolean isOnLadder() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean funcBF110182() {
        return this.dataManager.get(ATTACKING).byteValue() != 0;
    }

    public void funcA175454(boolean arg175454n1) {
        this.dataManager.set(ATTACKING, Byte.valueOf((byte)(arg175454n1 ? 1 : 0)));
    }

    public int funcCd175453() {
        return this.explosionStrength;
    }

    public void onUpdate() {
        super.onUpdate();
        this.ignoreFrustumCheck = true;
        if ((!this.world.isRemote) && (this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            setDead();
        }

        if (getAttackTarget() != null) {
            getLookHelper().setLookPosition(getAttackTarget().posX, getAttackTarget().posY + getAttackTarget().getEyeHeight(), getAttackTarget().posZ, 180.0F, 180.0F);
        }
    }

    protected void updateAITasks() {
        this.explosionStrength = 3;
        if (this.master != null) {
            if ((this.master.getAttackTarget() != null)) {
                setAttackTarget(this.master.getAttackTarget());
            }
        }

        else {
            List<?> list = this.world.getEntitiesWithinAABB(EntityPigZombieTitan.class, this.getEntityBoundingBox().grow(100.0D, 100.0D, 100.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntityPigZombieTitan))) {
                        this.master = ((EntityPigZombieTitan)entity);
                    }
                }
            }
        }

        if ((getAttackTarget() != null) && (!canEntityBeSeen(getAttackTarget())) && (this.rand.nextInt(150) == 0)) {
            setAttackTarget(null);
        }

        if ((!this.world.isRemote) && (this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            setDead();
        }

        despawnEntity();
        this.strikeCounterPrev = this.counterStrike;
        double d0 = this.xWaypoint - this.posX;
        double d1 = this.yWaypoint - this.posY;
        double d2 = this.zWaypoint - this.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if ((d3 < 1.0D) || (d3 > 3600.0D)) {
            if (this.master != null) {
                double extra = 0.0D;
                if ((getAttackTarget() != null) && (getAttackTarget().height > 4.0F)) {
                    extra = 64.0D;
                }

                double x = this.master.posX;
                double y = this.master.posY + 32.0D + extra;
                double z = this.master.posZ;
                d0 += this.rand.nextDouble() * 96D - 48D;
                d1 += this.rand.nextDouble() * 96D - 48D;
                d2 += this.rand.nextDouble() * 96D - 48D;
                this.xWaypoint = (x + d0);
                this.yWaypoint = (y + d1);
                this.zWaypoint = (z + d2);
            }

            else if (getAttackTarget() != null) {
                this.xWaypoint = getAttackTarget().posX + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                this.yWaypoint = getAttackTarget().posY + 32D + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                this.zWaypoint = getAttackTarget().posZ + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
            }

            else {
                EntityPlayer player = this.world.getClosestPlayerToEntity(this, 512D);
                if (player != null) {
                    this.xWaypoint = player.posX + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                    this.yWaypoint = player.posY + 32D + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                    this.zWaypoint = player.posZ + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                }

                else {
                    this.xWaypoint = this.posX + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                    this.yWaypoint = this.posY + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                    this.zWaypoint = this.posZ + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                }
            }
        }

        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;
            d3 = MathHelper.sqrt(d3);
            if (isTraversableCourse(this.xWaypoint, this.yWaypoint, this.zWaypoint, d3)) {
                if ((this.master != null) && (getDistanceSq(this.master) > 2304.0D)) {
                    this.motionX += d0 / d3 * 0.3D;
                    this.motionY += d1 / d3 * 0.3D;
                    this.motionZ += d2 / d3 * 0.3D;
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

        if ((this.targetedEntity != null) && (this.targetedEntity.isDead)) {
            this.targetedEntity = null;
        }

        if ((this.targetedEntity == null) || (this.rechargeAggro-- <= 0)) {
            this.targetedEntity = getAttackTarget();
            if (this.targetedEntity != null) {
                this.rechargeAggro = 20;
            }
        }

        double d4 = 100.0D;
        if ((this.targetedEntity != null) && (this.targetedEntity.getDistanceSq(this) < d4 * d4)) {
            double d8 = 2D;
            Vec3d vec3 = getLook(1.0F);
            double d5 = this.targetedEntity.posX - (this.posX + vec3.x * d8);
            double d6 = this.targetedEntity.posY - (this.posY + vec3.y * d8 + 1D);
            double d7 = this.targetedEntity.posZ - (this.posZ + vec3.z * d8);
            this.renderYawOffset = (this.rotationYaw = -(float)Math.atan2(d5, d7) * 180.0F / 3.1415927F);
            if (canEntityBeSeen(this.targetedEntity)) {
                if (this.counterStrike == 10) {
                    this.world.playEvent((EntityPlayer)null, 1015, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                }

                this.counterStrike += 1;
                if (this.counterStrike == 20) {
                    this.world.playEvent((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                    EntityGhastGuardFireball entitylargefireball = new EntityGhastGuardFireball(this.world, this, d5, d6, d7);
                    entitylargefireball.explosionPower = this.explosionStrength;
                    entitylargefireball.posX = (this.posX + vec3.x * d8);
                    entitylargefireball.posY = (this.posY + vec3.y * d8 + 1D);
                    entitylargefireball.posZ = (this.posZ + vec3.z * d8);
                    this.world.spawnEntity(entitylargefireball);
                    this.counterStrike = -40;
                }
            }

            else if (this.counterStrike > 0) {
                this.counterStrike -= 1;
            }
        }

        else {
            this.renderYawOffset = (this.rotationYaw = -(float)Math.atan2(this.motionX, this.motionZ) * 180.0F / 3.1415927F);
            if (this.counterStrike > 0) {
                this.counterStrike -= 1;
            }
        }

        if (!this.world.isRemote) {
            byte b1 = this.dataManager.get(ATTACKING).byteValue();
            byte b0 = (byte)(this.counterStrike > 10 ? 1 : 0);
            if (b1 != b0) {
                this.dataManager.set(ATTACKING, Byte.valueOf(b0));
            }
        }

        super.updateAITasks();
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

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ATTACKING, Byte.valueOf((byte)0));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_GHAST_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }

    protected Item getDropItem() {
        return Items.GUNPOWDER;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        int j = this.rand.nextInt(2) + this.rand.nextInt(1 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.GHAST_TEAR, 1);
        }

        j = this.rand.nextInt(3) + this.rand.nextInt(1 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.GUNPOWDER, 1);
        }
    }

    protected float getSoundVolume() {
        return 10.0F;
    }

    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0) && (super.getCanSpawnHere()) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL);
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionStrength);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = tagCompund.getInteger("ExplosionPower");
        }
    }

    public float getEyeHeight() {
        return 3.0F;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntityPigZombieMinion)) || ((source.getTrueSource() instanceof EntityPigZombieTitan)) || ((source.getTrueSource() instanceof EntityGhastGuard))) {
            return false;
        }

        if (("fireball".equals(source.getDamageType())) && ((source.getTrueSource() instanceof EntityPlayer))) {
            super.attackEntityFrom(source, 1000.0F);
            return true;
        }

        return super.attackEntityFrom(source, amount);
    }

    class AILookAround extends EntityAIBase {
        private EntityGhastGuard fld179472a = EntityGhastGuard.this;
        public AILookAround() {
            setMutexBits(2);
        }

        public boolean shouldExecute() {
            return true;
        }

        public void updateTask() {
            if (this.fld179472a.getAttackTarget() == null) {
                this.fld179472a.renderYawOffset = (this.fld179472a.rotationYaw = -(float)Math.atan2(this.fld179472a.motionX, this.fld179472a.motionZ) * 180.0F / 3.1415927F);
            }

            else {
                EntityLivingBase entitylivingbase = this.fld179472a.getAttackTarget();
                double d0 = 100.0D;
                if (entitylivingbase.getDistanceSq(this.fld179472a) < d0 * d0) {
                    double d1 = entitylivingbase.posX - this.fld179472a.posX;
                    double d2 = entitylivingbase.posZ - this.fld179472a.posZ;
                    this.fld179472a.renderYawOffset = (this.fld179472a.rotationYaw = -(float)Math.atan2(d1, d2) * 180.0F / 3.1415927F);
                }
            }
        }
    }

    @Override
    public EntityLiving getMaster() {
        return master;
    }

    @Override
    public void assignMaster(EntityLiving entity) {
        master = entity;
    }

    @Override
    public float fetchVolumeSummon() {
        return 10F;
    }

    @Override
    public float fetchPitchSummon() {
        return 0.5F;
    }

    @Override
    public void setMinionType(int type) {
    }
}
