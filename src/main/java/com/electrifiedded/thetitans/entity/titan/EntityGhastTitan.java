package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityGhastMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanLookIdle;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGhastTitan extends EntityTitan {
    private static final DataParameter<Byte> ATTACKING = EntityDataManager.<Byte>createKey(EntityGhastTitan.class, DataSerializers.BYTE);
    public int courseChangeCooldown;
    public double xWaypoint;
    public double yWaypoint;
    public double zWaypoint;
    public int strikeCounterPrev;
    public int counterStrike;

    public EntityGhastTitan(World worldIn) {
        super(worldIn);
        particlesBeUpwardNeeds = true;
        noClip = true;
        setSize(110F, 110F);
        experienceValue = 750000;
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
        tasks.removeTask(new EntityAITitanLookIdle(this));
    }

    protected void applyEntityAI() {
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterGhast));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    protected void whenUpdateBounds() {
        if (ticksExisted % 5 == 0) {
            setSize(4.5F * fetchScaleMultiplierGiant(), 4.5F * fetchScaleMultiplierGiant());
        }

        super.whenUpdateBounds();
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 10.0F;
    }

    public float getEyeHeight() {
        return height * 0.5454545454545455F;
    }

    @SideOnly(Side.CLIENT)
    public boolean isAttacking() {
        return this.dataManager.get(ATTACKING) != 0;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityGhastMinion.class) && (arg70686n1 != EntityGhastTitan.class) && (arg70686n1 != EntityTitanFireball.class);
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(250) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (super.getCanSpawnHere());
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.giantServantSpawnrateGhast;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ATTACKING, Byte.valueOf((byte)0));
    }

    public int getParticleCount() {
        return 100;
    }

    public String fetchParticle() {
        return "largesmoke";
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.GREATER;
    }

    protected String fetchLivingAudioGiant() {
        return "thetitans:titanGhastLiving";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:titanGhastGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:titanGhastDeath";
    }

    protected void playStepSound(BlockPos pos, Block block) {
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public double getSpeed() {
        return 0.5D + fetchPowerExtra() * 0.001D;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void updateFallState(double arg70064n1, boolean arg70064n3, net.minecraft.block.state.IBlockState state, BlockPos pos) {
    }

    public void travel(float arg70612n1, float arg70612n2, float arg70612n3) {
        move(MoverType.SELF, motionX, motionY, motionZ);
        motionX *= 0.91F;
        motionY *= 0.91F;
        motionZ *= 0.91F;
        prevLimbSwingAmount = limbSwingAmount;
        double d1 = posX - prevPosX;
        double d0 = posZ - prevPosZ;
        float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
        limbSwing += limbSwingAmount;
    }

    public boolean isOnLadder() {
        return false;
    }

    public String getName() {
        switch (fetchVariantGiant()) {
            case 0:
                return "§f" + I18n.translateToLocal("entity.GhastTitan.name") + "§f";
            case 1:
                return "§b" + I18n.translateToLocal("entity.GhastTitanLightning.name") + "§f";
            case 2:
                return "§0" + I18n.translateToLocal("entity.GhastTitanObsidian.name") + "§f";
        }

        return null;
    }

    public void onLivingUpdate() {
        noClip = true;

        for (int i = 0; i < 90; i++) {
            double d0 = posX + (rand.nextFloat() * 90.0F - 45.0F);
            double d1 = posY + rand.nextFloat() * 30.0F;
            double d2 = posZ + (rand.nextFloat() * 90.0F - 45.0F);
            BlockPos pos = new BlockPos((int)d0, (int)d1 + (int)getEyeHeight(), (int)d2);
            if ((!world.isRemote) && (world.getBlockState(pos).getMaterial() != Material.AIR)) {
                setPosition(posX, posY + 0.1D, posZ);
            }
        }

        EntityPlayer entity = world.getClosestPlayerToEntity(this, -1.0D);
        if (((entity instanceof EntityPlayer)) && (entity != null) && (entity == getAttackTarget()) && (getAttackTarget() != null)) {
            entity.setFire(50);
            if ((rand.nextInt(200) == 0) && (getAttackTarget() != null) && (getHealth() <= getMaxHealth() / 100.0F)) {
                entity.attackEntityFrom(DamageSourceExtra.onFire.setDamageIsAbsolute().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
            }

            if ((entity.getAbsorptionAmount() <= 0.0F) && (ticksExisted % 10 == 0)) {
                entity.attackEntityFrom(DamageSourceExtra.onFire.setDamageIsAbsolute().setDamageAllowedInCreativeMode(), 12.0F);
                entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 9));
                if (entity.getHealth() <= 5.0F) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                }
            }

            else if ((entity.getAbsorptionAmount() >= 0.0F) && (ticksExisted % 20 == 0)) {
                entity.attackEntityFrom(DamageSourceExtra.onFire.setDamageIsAbsolute().setDamageAllowedInCreativeMode(), 12.0F);
            }
        }

        if (world.isRemote) {
            for (int i1 = 0; i1 < getParticleCount() && com.electrifiedded.thetitans.util.perf.TitanParticles.allow(world); i1++) {
                world.spawnParticle(net.minecraft.util.EnumParticleTypes.SMOKE_LARGE, posX + (rand.nextDouble() - 0.5D) * 96D, posY + rand.nextDouble() * 96D, posZ + (rand.nextDouble() - 0.5D) * 96D, 0.0D, 0.5D, 0.0D);
            }
        }

        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        strikeCounterPrev = counterStrike;
        double d0 = xWaypoint - posX;
        double d1 = yWaypoint - posY;
        double d2 = zWaypoint - posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (d3 < 400.0D || d3 > 40000.0D) {
            if (getAttackTarget() != null) {
                xWaypoint = getAttackTarget().posX + ((rand.nextDouble() * 2.0D - 1.0D) * 128.0D);
                yWaypoint = 150.0D + ((rand.nextDouble() * 2.0D - 1.0D) * 32D);
                zWaypoint = getAttackTarget().posZ + ((rand.nextDouble() * 2.0D - 1.0D) * 128.0D);
            }

            else {
                xWaypoint = posX + ((rand.nextDouble() * 2.0D - 1.0D) * 128.0D);
                yWaypoint = 200.0D + ((rand.nextDouble() * 2.0D - 1.0D) * 32D);
                zWaypoint = posZ + ((rand.nextDouble() * 2.0D - 1.0D) * 128.0D);
            }
        }

        if (courseChangeCooldown-- <= 0) {
            courseChangeCooldown += rand.nextInt(5) + 2;
            d3 = MathHelper.sqrt(d3);
            motionX += d0 / d3 * 0.3D;
            motionY += d1 / d3 * 0.3D;
            motionZ += d2 / d3 * 0.3D;
        }

        double d4 = 1024.0D;
        renderYawOffset = rotationYaw = rotationYawHead;
        if ((getAttackTarget() != null) && (getAttackTarget().getDistance(this) < d4)) {
            getLookHelper().setLookPositionWithEntity(getAttackTarget(), 180F, 180F);
            double d8 = width * 0.5D;
            Vec3d vec3 = getLook(1.0F);
            double px = (posX + vec3.x * d8);
            double py = (posY + vec3.y * d8) + 10D;
            double pz = (posZ + vec3.z * d8);
            double d11 = getAttackTarget().posX - (px);
            double d21 = getAttackTarget().posY - (py);
            double d31 = getAttackTarget().posZ - (pz);
            if (canEntityBeSeen(getAttackTarget())) {
                if (counterStrike == 10) {
                    triggerAudioGiant("thetitans:titanGhastCharge", Float.MAX_VALUE, getSoundPitch());
                }

                counterStrike += 1;
                if (counterStrike >= 50) {
                    faceEntity(getAttackTarget(), 180F, 180F);
                    if (getDistance(getAttackTarget()) <= fetchReachMelee() && ticksExisted % 10 == 0)
                        strikeMobChoosen(getAttackTarget(), (float)fetchAmountStrike(3.0F), fetchAmountKnockback());
                    triggerAudioGiant("thetitans:titanGhastFireball", Float.MAX_VALUE, 1F);
                    EntityTitanFireball entitysmallfireball = new EntityTitanFireball(world, this, d11 + (getRNG().nextGaussian() * 16D), d21, d31 + (getRNG().nextGaussian() * 16D));
                    entitysmallfireball.posX = (px);
                    entitysmallfireball.posY = (py);
                    entitysmallfireball.posZ = (pz);
                    world.spawnEntity(entitysmallfireball);
                    if (counterStrike == 100)
                        counterStrike = -80;
                }
            }

            else if (counterStrike > 0) {
                counterStrike -= 1;
            }
        }

        else {
            if (getAttackTarget() == null)
                renderYawOffset = rotationYaw = rotationYawHead = -(float)Math.atan2(motionX, motionZ) * 180.0F / 3.1415927F;
            if (counterStrike > 0) {
                counterStrike -= 1;
            }
        }

        if (!world.isRemote) {
            byte b1 = this.dataManager.get(ATTACKING);
            byte b0 = (byte)(counterStrike > 20 ? 1 : 0);
            if (b1 != b0) {
                this.dataManager.set(ATTACKING, Byte.valueOf(b0));
            }
        }

        super.updateAITasks();
    }

    public int getVerticalFaceSpeed() {
        return 180;
    }

    protected Item getDropItem() {
        return Items.BLAZE_ROD;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < 80; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(26000);
                world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 16; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GUNPOWDER));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 16; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GHAST_TEAR));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 12; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.END_STONE));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 12; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.OBSIDIAN));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.COAL_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 6; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.IRON_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.GOLD_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.EMERALD_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.DIAMOND_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.harcadium_block));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanGear.stackVoid));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.BEDROCK));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GOLDEN_APPLE, 1, 1));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }
        }
    }

    protected void appendArmourRandom() {
        dropItem(Items.BREWING_STAND, 64);
    }

    public boolean isCharging() {
        return (this.dataManager.get(ATTACKING) & 0x1) != 0;
    }

    public void assignStrikeStateIn(boolean arg70844n1) {
        byte b0 = this.dataManager.get(ATTACKING);
        if (arg70844n1) {
            b0 = (byte)(b0 | 0x1);
        }

        else {
            b0 = (byte)(b0 & 0xFFFFFFFE);
        }

        this.dataManager.set(ATTACKING, Byte.valueOf(b0));
    }

    protected boolean isValidLightLevel() {
        return true;
    }

    protected void collideWithEntity(Entity arg82167n1) {
        if ((!(arg82167n1 instanceof EntitySmallFireball)) || (!(arg82167n1 instanceof EntityLargeFireball))) {
            arg82167n1.applyEntityCollision(this);
        }
    }

    public String fetchAchievement() {
        return TitanTrophies.ghasttitan;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntityGhastMinion)) || ((source.getTrueSource() instanceof EntityGhastTitan))) {
            return false;
        }

        if (source.isFireDamage()) {
            heal(amount);
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    protected void inactActionDeath() {
        if (!world.isRemote) {
            triggerAudioGiant("mob.ghast.death", getSoundVolume(), getSoundPitch());
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
                shedDropRare(1);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(false);
            entitytitan.assignKindSpirit(11);
        }
    }

    protected EntityLiving fetchMinion() {
        return new EntityGhastMinion(world);
    }

    protected double cap() {
        return super.cap();
    }
}
