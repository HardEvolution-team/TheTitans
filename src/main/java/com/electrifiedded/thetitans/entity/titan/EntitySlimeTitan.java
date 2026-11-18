package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class EntitySlimeTitan extends EntityTitan {
    private static final DataParameter<Byte> SLIME_SIZE = EntityDataManager.<Byte>createKey(EntitySlimeTitan.class, DataSerializers.BYTE);
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean fld175452bi;
    private int jumpWaitSlime;
    public boolean jumpedDouble;

    public EntitySlimeTitan(World worldIn) {
        super(worldIn);
        setSize(8.0F * getSlimeSize(), 8.0F * getSlimeSize());
        jumpWaitSlime = 0;
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
    }

    protected void whenUpdateBounds() {
        if (ticksExisted % 5 == 0) {
            destroyBlocksInAABB(getEntityBoundingBox().offset(motionX, motionY > 0D ? motionY : 0D, motionZ));
            performStrikeArea(this, (EntityTitanPart)null, (double)getSlimeSize(), (float)getSlimeSize() * 0.25F, (float)getAttackStrength(), (int)((float)getSlimeSize() * 0.5F), 0.0F, 0.0F, 0.0F, false);
        }
        super.whenUpdateBounds();
    }

    public float fetchScaleMultiplierGiant() {
        return 16F;
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * (getSlimeSize() * getSlimeSize());
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.SIMPLE;
    }

    public boolean allowStrike() {
        return true;
    }

    protected void applyEntityAI() {
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterSlime));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    protected void updateAITasks() {
        super.updateAITasks();
        EntityLivingBase entity = getAttackTarget();
        if ((onGround) && (jumpWaitSlime-- <= 0) && (getInvulTime() <= 0)) {
            jumpWaitSlime = getJumpDelay();
            if (entity != null) {
                jumpWaitSlime /= 3;
                getLookHelper().setLookPositionWithEntity(entity, 180.0F, 60.0F);
            }

            jump();
            if (makesSoundOnJump()) {
                triggerAudioGiant(getJumpSound(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            moveForward = (4 * getSlimeSize());
            travel(moveStrafing, 0.0F, moveForward);
        }

        else {
            isJumping = false;
            if (onGround) {
                moveStrafing = (moveForward = 0.0F);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 == EntityMagmaCube.class || arg70686n1 != EntitySlime.class) && (arg70686n1 == EntityMagmaCubeTitan.class || arg70686n1 != EntitySlimeTitan.class);
    }

    public boolean hasSoulNo() {
        return true;
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(20) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL);
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.giantServantSpawnrateSlime;
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(SLIME_SIZE, Byte.valueOf((byte)1));
    }

    protected void setSlimeSize(int arg70799n1) {
        dataManager.set(SLIME_SIZE, Byte.valueOf((byte)arg70799n1));
        setSize(8.0F * arg70799n1, 8.0F * arg70799n1);
        setPosition(posX, posY, posZ);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5F + 0.1F * arg70799n1);
        setHealth(getMaxHealth());
        if ((this instanceof EntityMagmaCubeTitan)) {
            experienceValue = (arg70799n1 * 3000);
        }

        else {
            experienceValue = (arg70799n1 * 1000);
        }
    }

    public int getParticleCount() {
        return 4;
    }

    public String fetchParticle() {
        return funcN180487();
    }

    public int getSlimeSize() {
        return dataManager.get(SLIME_SIZE);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Size", getSlimeSize() - 1);
        tagCompound.setBoolean("wasOnGround", fld175452bi);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        int i = tagCompund.getInteger("Size");
        if (i < 0) {
            i = 0;
        }

        setSlimeSize(i + 1);
        fld175452bi = tagCompund.getBoolean("wasOnGround");
    }

    protected String funcN180487() {
        return "slime";
    }

    protected String getJumpSound() {
        return "mob.slime.big";
    }

    protected String fetchParticleSlime() {
        return "slime";
    }

    public void onUpdate() {
        squishFactor += (squishAmount - squishFactor) * 0.5F;
        prevSquishFactor = squishFactor;
        boolean flag = onGround;
        super.onUpdate();
        List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(0.5D, 0.5D, 0.5D));
        if (list != null && !list.isEmpty()) {
            for (int j = 0; j < list.size(); ++j) {
                Entity entity = (Entity)list.get(j);
                if (entity instanceof EntityFireball && !(entity instanceof EntityLightningBall) && !(entity instanceof EntityGargoyleTitanFireball) && !(entity instanceof EntityWebShot) && !(entity instanceof EntityMortarWitherSkull)) {
                    ((EntityFireball)entity).setDead();
                }

                if (entity instanceof EntityTitanFireball && ((EntityTitanFireball)entity).shootingEntity != null) {
                    ((EntityTitanFireball)entity).whenPublicImpact(this);
                }

                if (entity instanceof EntityGargoyleTitanFireball) {
                    triggerAudioGiant("thetitans:titanpunch", 10.0F, 1.0F);
                    world.newExplosion(((EntityGargoyleTitanFireball)entity).shootingEntity != null ? ((EntityGargoyleTitanFireball)entity).shootingEntity : ((EntityGargoyleTitanFireball)entity), ((EntityGargoyleTitanFireball)entity).posX, ((EntityGargoyleTitanFireball)entity).posY, ((EntityGargoyleTitanFireball)entity).posZ, 8F, false, false);
                    attackEntityFrom(DamageSource.causeFireballDamage((EntityGargoyleTitanFireball)entity, ((EntityGargoyleTitanFireball)entity).shootingEntity != null ? ((EntityGargoyleTitanFireball)entity).shootingEntity : (EntityGargoyleTitanFireball)entity), 1000F);
                    entity.setDead();
                }

                if (entity instanceof EntityGargoyleTitanFireball) {
                    triggerAudioGiant("thetitans:titanpunch", 10.0F, 1.0F);
                    world.newExplosion(((EntityGargoyleTitanFireball)entity).shootingEntity != null ? ((EntityGargoyleTitanFireball)entity).shootingEntity : ((EntityGargoyleTitanFireball)entity), ((EntityGargoyleTitanFireball)entity).posX, ((EntityGargoyleTitanFireball)entity).posY, ((EntityGargoyleTitanFireball)entity).posZ, 8F, false, false);
                    attackEntityFrom(DamageSource.causeFireballDamage((EntityGargoyleTitanFireball)entity, ((EntityGargoyleTitanFireball)entity).shootingEntity != null ? ((EntityGargoyleTitanFireball)entity).shootingEntity : (EntityGargoyleTitanFireball)entity), 1000F);
                    entity.setDead();
                }

                if (entity instanceof EntityHarcadiumArrow) {
                    triggerAudioGiant("thetitans:titanpunch", 10.0F, 1.0F);
                    attackEntityFrom(DamageSourceExtra.causeArrowHarmHarcadium((EntityHarcadiumArrow)entity, ((EntityHarcadiumArrow)entity).shootingEntity != null ? ((EntityHarcadiumArrow)entity).shootingEntity : (EntityHarcadiumArrow)entity), 500F);
                    entity.setDead();
                }

                if (entity instanceof EntityWebShot) {
                    triggerAudioGiant("thetitans:titanpunch", 10.0F, 1.0F);
                    attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(((EntityWebShot)entity).shootingEntity), 300F);
                    int i1 = MathHelper.floor(posY);
                    int i11 = MathHelper.floor(posX);
                    int j1 = MathHelper.floor(posZ);
                    for (int l1 = -2 - rand.nextInt(4); l1 <= 2 + rand.nextInt(4); l1++) {
                        for (int i2 = -2 - rand.nextInt(4); i2 <= 2 + rand.nextInt(4); i2++) {
                            for (int h = -2; h <= 2 + rand.nextInt(5); h++) {
                                int j2 = i11 + l1;
                                int k = i1 + h;
                                int l = j1 + i2;
                                BlockPos pos = new BlockPos(j2, k, l);
                                IBlockState block1 = world.getBlockState(pos);
                                if (!block1.isOpaqueCube()) {
                                    world.setBlockState(pos, Blocks.WEB.getDefaultState());
                                }
                            }
                        }
                    }

                    entity.setDead();
                }
            }
        }

        if ((onGround) && (!flag)) {
            int i = getSlimeSize();
            for (int j = 0; j < i * 32; j++) {
                float f = rand.nextFloat() * 3.1415927F * 2.0F;
                float f1 = rand.nextFloat() * 8.0F + 8.0F;
                float f2 = MathHelper.sin(f) * i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * i * 0.5F * f1;
                world.spawnParticle(EnumParticleTypes.SLIME, posX + f2, getEntityBoundingBox().minY, posZ + f3, 0.0D, 0.0D, 0.0D);
            }

            if (makesOnLandAudio()) {
                triggerAudioGiant(getJumpSound(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            squishAmount = -0.5F;
        }

        else if ((!onGround) && (flag)) {
            squishAmount = 1.0F;
        }

        alterSquishAmount();
    }

    public boolean needsMove() {
        return false;
    }

    public String getName() {
        return (buildInstanceProto() instanceof EntityMagmaCube ? "§4" : "§2") + super.getName();
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        renderYawOffset = rotationYaw;
        if (!isEntityAlive()) {
            if (this instanceof EntityMagmaCubeTitan)
                squishAmount = 1.0F;
            else
                squishAmount = -0.5F;
        }

        List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(0.1D, 0.1D, 0.1D));
        if (list != null && !list.isEmpty() && isEntityAlive()) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity)list.get(i);
                if (entity != null && entity instanceof EntityLivingBase && allowStrikeGiant(entity.getClass())) {
                    funcE175451((EntityLivingBase) entity);
                }
            }
        }

        giantMelee = true;
        if ((buildInstanceProto() instanceof EntityMagmaCube)) {
            jumpMovementFactor = 0.75F;
            // PERF — the body only acts with a target; class-filtered query instead of every entity.
            List<?> found1 = getAttackTarget() != null ? world.getEntitiesWithinAABB(EntityMagmaCube.class, getEntityBoundingBox().grow(128D, 128D, 128D)) : java.util.Collections.emptyList();
            if (!found1.isEmpty()) {
                for (int i1 = 0; i1 < found1.size(); i1++) {
                    Entity entity = (Entity)found1.get(i1);
                    if ((entity != null) && (entity instanceof EntityMagmaCube)) {
                        if (getAttackTarget() != null) {
                            ((EntityMagmaCube)entity).faceEntity(getAttackTarget(), 180.0F, 180.0F);
                            if (((EntityMagmaCube)entity).onGround || ((EntityMagmaCube)entity).isInWater() || ((EntityMagmaCube)entity).isInLava())
                                ((EntityMagmaCube)entity).motionY = (double)(0.6F + ((float)((EntityMagmaCube)entity).getSlimeSize() * 0.1F));
                            ((EntityMagmaCube)entity).setMoveForward(2F);
                        }
                    }
                }
            }
        }

        else {
            jumpMovementFactor = 0.5F;
            List<?> found1 = getAttackTarget() != null ? world.getEntitiesWithinAABB(EntitySlime.class, getEntityBoundingBox().grow(128D, 128D, 128D)) : java.util.Collections.emptyList();
            if (!found1.isEmpty()) {
                for (int i1 = 0; i1 < found1.size(); i1++) {
                    Entity entity = (Entity)found1.get(i1);
                    if ((entity != null) && entity != this && (entity instanceof EntitySlime) && !(entity instanceof EntityMagmaCube)) {
                        if (getAttackTarget() != null) {
                            ((EntitySlime)entity).faceEntity(getAttackTarget(), 180.0F, 180.0F);
                            if (((EntitySlime)entity).onGround || ((EntitySlime)entity).isInWater() || ((EntitySlime)entity).isInLava())
                                ((EntitySlime)entity).motionY = 0.5D;
                            ((EntitySlime)entity).setMoveForward(2F);
                        }
                    }
                }
            }
        }

        if (getAttackTarget() != null) {
            faceEntity(getAttackTarget(), 180F, 40F);
        }

        if (((getAttackTarget() == null) && (jumpedDouble == true)) || (onGround)) {
            jumpedDouble = false;
        }

        if ((getAttackTarget() != null) && (!jumpedDouble) && (rand.nextInt(100) == 0) && (!onGround)) {
            squishAmount = 2.0F;
            jump();
            jumpedDouble = true;
            triggerAudioGiant(getJumpSound(), getSoundVolume(), ((getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }

        if ((rand.nextInt(fetchSpawnRateMinion()) == 0) && (getHealth() > 0.0F) && (!world.isRemote)) {
            EntitySlime entitychicken = buildInstanceProto();
            entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
            world.spawnEntity(entitychicken);
            entitychicken.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 40, 4, false, true));
            entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
        }
    }

    protected void alterSquishAmount() {
        squishAmount *= 0.85F;
    }

    protected int getJumpDelay() {
        return rand.nextInt(40) + 20;
    }

    protected EntitySlimeTitan createInstance() {
        return new EntitySlimeTitan(world);
    }

    protected EntitySlime buildInstanceProto() {
        return new EntitySlime(world);
    }

    public void notifyDataManagerChange(net.minecraft.network.datasync.DataParameter<?> key) {
        if (SLIME_SIZE.equals(key)) {
            int j = getSlimeSize();
            setSize(8.0F * j, 8.0F * j);
        }
        super.notifyDataManagerChange(key);
    }

    public void applyEntityCollision(Entity entityIn) {
        if (entityIn instanceof EntityTitan)
            super.applyEntityCollision(entityIn);
        if (allowStrikeGiant(entityIn.getClass())) {
            getSlimeSize();
            if (ticksExisted % 5 == 0 && isEntityAlive()) {
                float f = (float)fetchAmountStrike(getSlimeSize());
                int i1 = fetchAmountKnockback();
                renderYawOffset = rotationYaw = rotationYawHead;
                strikeMobChoosen(entityIn, f, i1);
                if (!entityIn.isEntityAlive() && !(entityIn instanceof EntityTitan)) {
                    triggerAudioGiant("mob.slime.attack", 100F, 0.5F);
                    if (entityIn instanceof EntityLivingBase)
                        heal(((EntityLivingBase)entityIn).getMaxHealth());
                    entityIn.isDead = true;
                    if (!world.isRemote) {
                        EntitySlime entitychicken = buildInstanceProto();
                        entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                        entitychicken.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 4, false, true));
                        entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                        world.spawnEntity(entitychicken);
                    }
                }
            }
        }
    }

    public void onCollideWithPlayer(EntityPlayer entityIn) {
        funcE175451(entityIn);
    }

    protected void funcE175451(EntityLivingBase arg175451n1) {
    }

    public float getEyeHeight() {
        return 0.625F * height;
    }

    protected int getAttackStrength() {
        if (TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V) == true) {
            return getSlimeSize() * 30;
        }

        return getSlimeSize() * 10;
    }

    protected String fetchHurtAudioGiant() {
        return "mob.slime.big";
    }

    protected String fetchDeathAudioGiant() {
        return "mob.slime.big";
    }

    protected Item getDropItem() {
        return Items.SLIME_BALL;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < getSlimeSize(); x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                entitylargefireball.motionY += 0.5D;
                entitylargefireball.assignTotalXP(this instanceof EntityMagmaCubeTitan ? 2000 : 1000);
                world.spawnEntity(entitylargefireball);
            }

            if (getSlimeSize() <= 1) {
                for (int x = 0; x < 2; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(getDropItem()));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 1; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.COAL));
                    itembomb.assignTotalStack(8 + rand.nextInt(8 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 1; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.EMERALD));
                    itembomb.assignTotalStack(2 + rand.nextInt(4 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 1; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.DIAMOND));
                    itembomb.assignTotalStack(2 + rand.nextInt(4 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                if ((rand.nextInt(2) == 0 || rand.nextInt(1 + arg70628n2) > 0))
                    for (int x = 0; x < 1; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(TitanGear.wafletHarcadiumZ));
                    itembomb.assignTotalStack(1 + rand.nextInt(1 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }
            }
        }
    }

    protected boolean isValidLightLevel() {
        return true;
    }

    public int getVerticalFaceSpeed() {
        return 30;
    }

    protected boolean makesSoundOnJump() {
        return true;
    }

    protected boolean makesOnLandAudio() {
        return true;
    }

    protected void jump() {
        motionY = (1.5D + getSlimeSize() * 0.2F);
        isAirBorne = true;
        if (getAttackTarget() != null) {
            double d01 = getAttackTarget().posX - posX;
            double d11 = getAttackTarget().posZ - posZ;
            float f21 = MathHelper.sqrt(d01 * d01 + d11 * d11);
            double hor = 1.5D + (getSlimeSize() * 0.25F);
            motionX = (d01 / f21 * hor * hor + motionX * hor);
            motionZ = (d11 / f21 * hor * hor + motionZ * hor);
        }
    }

    public void fall(float arg70069n1, float damageMultiplier) {
        onGround = true;
        isAirBorne = false;
        if (!world.isRemote && rand.nextInt(5) == 0 && getAttackTarget() == null) {
            renderYawOffset = rotationYaw = rotationYawHead = rand.nextFloat() * 360F;
        }

        float[] fallRet = ForgeHooks.onLivingFall(this, arg70069n1, damageMultiplier);
        if (fallRet == null) return;
        arg70069n1 = fallRet[0];
        if (arg70069n1 <= 0.0F) return;
        PotionEffect potioneffect = getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f1 = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0.0F;
        int i = MathHelper.ceil(arg70069n1 - 12F - f1);
        if (i > 0) {
            shakeUserCamerasNearby(50D);
            triggerAudioGiant("thetitans:titanSlam", 5F * getSlimeSize(), 2F - (getSlimeSize() / 4));
            triggerAudioGiant("mob.slime.big", 5F * getSlimeSize(), 2F - (getSlimeSize() / 8));
            List<?> found11 = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(6.0D * getSlimeSize(), 6.0D * getSlimeSize(), 6.0D * getSlimeSize()), e -> e != this);
            if ((found11 != null) && (!found11.isEmpty())) {
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (((entity instanceof EntityLivingBase)) && allowStrikeGiant(entity.getClass()) && getDistance(entity) <= fetchReachMelee()) {
                        float f = (float)fetchAmountStrike(getSlimeSize());
                        int i11 = fetchAmountKnockback();
                        strikeMobChoosen(entity, f, i11);
                        double d0 = getEntityBoundingBox().minX + getEntityBoundingBox().maxX;
                        double d1 = getEntityBoundingBox().minZ + getEntityBoundingBox().maxZ;
                        double d2 = entity.posX - d0;
                        double d3 = entity.posZ - d1;
                        double d4 = d2 * d2 + d3 * d3;
                        if (jumpedDouble == true) {
                            entity.addVelocity(d2 / d4 * 16.0D, 2.0D, d3 / d4 * 16.0D);
                        }

                        else {
                            entity.addVelocity(d2 / d4 * 8.0D, 1.0D, d3 / d4 * 8.0D);
                        }
                    }
                }
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!world.isRemote) {
            EntitySlime entitychicken = buildInstanceProto();
            entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
            entitychicken.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 4, false, true));
            entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitychicken);
        }

        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (this instanceof EntityMagmaCubeTitan ? source.getTrueSource() instanceof EntityMagmaCubeTitan : source.getTrueSource() instanceof EntitySlimeTitan && !(source.getTrueSource() instanceof EntityMagmaCubeTitan)) {
            return false;
        }

        else {
            Entity entity = source.getTrueSource();
            if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source)) && (amount > 25.0F)) {
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(100.0D, 100.0D, 100.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if (this instanceof EntityMagmaCubeTitan ? (mobRef1 instanceof EntityMagmaCubeTitan) : (mobRef1 instanceof EntitySlimeTitan && !(mobRef1 instanceof EntityMagmaCubeTitan))) {
                        EntitySlimeTitan entitypigzombie = (EntitySlimeTitan)mobRef1;
                        entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                        entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                    }

                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                }
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        int i = rand.nextInt(3);
        if ((i < 2) && (rand.nextFloat() < 0.5F)) {
            i++;
        }

        int j = 1 << i;
        setSlimeSize(j);
        return super.onInitialSpawn(difficulty, arg180482n2);
    }

    public String fetchAchievement() {
        if ((buildInstanceProto() instanceof EntityMagmaCube)) {
            return TitanTrophies.magmacubetitan;
        }

        return TitanTrophies.slimetitan;
    }

    protected void inactActionDeath() {
        world.newExplosion(this, posX, posY + 3.0D, posZ, 0.0F, false, false);
        if (!world.isRemote && world.getGameRules().getBoolean("doMobLoot")) {
            dropFewItems(true, 0);
            dropEquipment(true, 0);
        }

        int i = getSlimeSize();
        if (!world.isRemote)
            for (int i1 = 0; i1 < 8 * getSlimeSize() + world.rand.nextInt(8 * getSlimeSize()); i1++) {
            EntitySlime entitychicken = buildInstanceProto();
            entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
            entitychicken.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 4, false, true));
            entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitychicken);
        }

        if ((!world.isRemote) && (i > 1)) {
            int j = 2 + rand.nextInt(3);
            for (int k = 0; k < j; k++) {
                float f = (k % 2 - 0.5F) * i / 4.0F;
                float f1 = (k / 2 - 0.5F) * i / 4.0F;
                EntitySlimeTitan entityslime = createInstance();
                if (hasCustomName()) {
                    entityslime.setCustomNameTag(getCustomNameTag());
                }

                if (isNoDespawnRequired()) {
                    entityslime.enablePersistence();
                }

                entityslime.setSlimeSize(i / 2);
                entityslime.setLocationAndAngles(posX + f, posY + 0.5D, posZ + f1, rand.nextFloat() * 360.0F, 0.0F);
                world.spawnEntity(entityslime);
            }
        }
    }

    protected double cap() {
        return super.cap();
    }

    @Override
    public boolean needsCrush() {
        return true;
    }
}
