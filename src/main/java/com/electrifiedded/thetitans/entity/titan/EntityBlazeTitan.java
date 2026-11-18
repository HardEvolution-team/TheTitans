package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAIHurtByTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.registry.TitanTrophies;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBlazeTitan extends EntityTitan implements ITitanHitbox {
    private static final DataParameter<Byte> ON_FIRE = EntityDataManager.<Byte>createKey(EntityBlazeTitan.class, DataSerializers.BYTE);
    private float heightOffset = 32.0F;
    private int heightOffsetUpdateTime;
    public boolean burnNeeds;
    public EntityTitanPart head;
    public EntityTitanPart[] rodsV = new EntityTitanPart[12];

    public EntityBlazeTitan(World worldIn) {
        super(worldIn);
        head = new EntityTitanPart(worldIn, this, "head", 8.0F, 8.0F);
        rodsV[0] = new EntityTitanPart(worldIn, this, "rod1", 2.0F, 8.0F);
        rodsV[1] = new EntityTitanPart(worldIn, this, "rod2", 2.0F, 8.0F);
        rodsV[2] = new EntityTitanPart(worldIn, this, "rod3", 2.0F, 8.0F);
        rodsV[3] = new EntityTitanPart(worldIn, this, "rod4", 2.0F, 8.0F);
        rodsV[4] = new EntityTitanPart(worldIn, this, "rod5", 2.0F, 8.0F);
        rodsV[5] = new EntityTitanPart(worldIn, this, "rod6", 2.0F, 8.0F);
        rodsV[6] = new EntityTitanPart(worldIn, this, "rod7", 2.0F, 8.0F);
        rodsV[7] = new EntityTitanPart(worldIn, this, "rod8", 2.0F, 8.0F);
        rodsV[8] = new EntityTitanPart(worldIn, this, "rod9", 2.0F, 8.0F);
        rodsV[9] = new EntityTitanPart(worldIn, this, "rod10", 2.0F, 8.0F);
        rodsV[10] = new EntityTitanPart(worldIn, this, "rod11", 2.0F, 8.0F);
        rodsV[11] = new EntityTitanPart(worldIn, this, "rod12", 2.0F, 8.0F);

        setPiece.add(head);
        setPiece.add(rodsV[0]);
        setPiece.add(rodsV[1]);
        setPiece.add(rodsV[2]);
        setPiece.add(rodsV[3]);
        setPiece.add(rodsV[4]);
        setPiece.add(rodsV[5]);
        setPiece.add(rodsV[6]);
        setPiece.add(rodsV[7]);
        setPiece.add(rodsV[8]);
        setPiece.add(rodsV[9]);
        setPiece.add(rodsV[10]);
        setPiece.add(rodsV[11]);

        particlesBeUpwardNeeds = true;
        setSize(8.0F, 8.0F);
        experienceValue = 50000;
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAIHurtByTargetTitan(this, true, new Class[] {
            EntityBlazeMinion.class, EntityBlazeTitan.class
        }
        ));
        world.spawnEntity(head);
        world.spawnEntity(rodsV[0]);
        world.spawnEntity(rodsV[1]);
        world.spawnEntity(rodsV[2]);
        world.spawnEntity(rodsV[3]);
        world.spawnEntity(rodsV[4]);
        world.spawnEntity(rodsV[5]);
        world.spawnEntity(rodsV[6]);
        world.spawnEntity(rodsV[7]);
        world.spawnEntity(rodsV[8]);
        world.spawnEntity(rodsV[9]);
        world.spawnEntity(rodsV[10]);
        world.spawnEntity(rodsV[11]);
    }

    protected void whenUpdateBounds() {
        if (ticksExisted > 5) {
            head.height = head.width = height;
            for (int f = 0; f < 12; f++) {
                rodsV[f].height = height;
                rodsV[f].width = width * 0.25F;
            }

            float f = renderYawOffset * 3.1415927F / 180.0F;
            float f6 = (ticksExisted) * 3.1415927F * 0.008F + 0.15F;
            int i;

            for (i = 0; i < 4; i++) {
                rodsV[i].setLocationAndAngles(posX - (MathHelper.cos(f6 + f) * (0.625F * fetchScaleMultiplierGiant())), posY - ((0.25F * fetchScaleMultiplierGiant())), posZ - (MathHelper.sin(f6 + f) * (0.625F * fetchScaleMultiplierGiant())), 0F, 0F);
                f6 += 1.5F;
            }

            f6 = 0.7853982F + (ticksExisted) * 3.1415927F * -0.005F - 1.4F;
            for (i = 4; i < 8; i++) {
                rodsV[i].setLocationAndAngles(posX - (MathHelper.cos(f6 + f) * (0.4375F * fetchScaleMultiplierGiant())), posY - ((0.625F * fetchScaleMultiplierGiant())), posZ - (MathHelper.sin(f6 + f) * (0.4375F * fetchScaleMultiplierGiant())), 0F, 0F);
                f6 += 1.5F;
            }

            f6 = 0.47123894F + (ticksExisted) * 3.1415927F * 0.003F - 0.8F;
            for (i = 8; i < 12; i++) {
                rodsV[i].setLocationAndAngles( posX - (MathHelper.cos(f6 + f) * (0.25F * fetchScaleMultiplierGiant())), posY - ((1.0625F * fetchScaleMultiplierGiant())), posZ - (MathHelper.sin(f6 + f) * (0.25F * fetchScaleMultiplierGiant())), 0F, 0F);
                f6 += 1.5F;
            }

            head.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            for (int u = 0; u < getParticleCount(); u++)
                for (int w = 0; w < rodsV.length; w++)
                world.spawnParticle(forParticle(fetchParticle()), rodsV[w].posX + (rand.nextDouble() - 0.5D) * rodsV[w].width, rodsV[w].posY + rand.nextDouble() * rodsV[w].height, rodsV[w].posZ + (rand.nextDouble() - 0.5D) * rodsV[w].width, 0D, 0D, 0D);
        }

        super.whenUpdateBounds();
    }

    protected void applyEntityAI() {
        tasks.addTask(4, new AIFireballAttack());
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterBlaze));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 20.0F;
    }

    public float getEyeHeight() {
        return 0.5F * height;
    }

    public boolean isArmored() {
        return getHealth() <= getMaxHealth() / 8.0D;
    }

    public void fall(float arg70069n1) {
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return arg70686n1 != head.getClass() && arg70686n1 != rodsV[0].getClass() && arg70686n1 != rodsV[1].getClass() && arg70686n1 != rodsV[2].getClass() && arg70686n1 != rodsV[3].getClass() && arg70686n1 != rodsV[4].getClass() && arg70686n1 != rodsV[5].getClass() && arg70686n1 != rodsV[6].getClass() && arg70686n1 != rodsV[7].getClass() && arg70686n1 != rodsV[8].getClass() && arg70686n1 != rodsV[9].getClass() && arg70686n1 != rodsV[10].getClass() && arg70686n1 != rodsV[11].getClass() && (arg70686n1 != EntityTitanFireball.class) && (arg70686n1 != EntityBlazeMinion.class) && (arg70686n1 != EntityBlazeTitan.class);
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(250) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (super.getCanSpawnHere());
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.giantServantSpawnrateBlaze;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ON_FIRE, Byte.valueOf((byte)0));
    }

    public int getParticleCount() {
        return 40;
    }

    public String fetchParticle() {
        if (rand.nextInt(isWet() ? 2 : 6) == 0) {
            return "explode";
        }

        return "largesmoke";
    }

    // base forParticle(String) is private, local mapper for this class's particle strings
    private EnumParticleTypes forParticle(String s) {
        if ("explode".equals(s)) return EnumParticleTypes.EXPLOSION_NORMAL;
        return EnumParticleTypes.SMOKE_LARGE;
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.AVERAGE;
    }

    protected String fetchLivingAudioGiant() {
        return "thetitans:titanBlazeBreathe";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:titanBlazeGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:titanBlazeDeath";
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public double getSpeed() {
        return 0.75D + fetchPowerExtra() * 0.001D;
    }

    public String getName() {
        switch (fetchVariantGiant()) {
            case 0:
                switch (rand.nextInt(2)) {
                    case 0:return "§c" + I18n.translateToLocal("entity.BlazeTitan.name") + "§f";
                    case 1:return "§6" + I18n.translateToLocal("entity.BlazeTitan.name") + "§f";
                }

            case 1:
                return "§b" + I18n.translateToLocal("entity.BlazeTitanIce.name") + "§f";
            case 2:
                return "§a" + I18n.translateToLocal("entity.BlazeTitanAcid.name") + "§f";
            case 3:
                return "§4" + I18n.translateToLocal("entity.BlazeTitanDirt.name") + "§f";
            case 4:
                return "§0" + "§l" + I18n.translateToLocal("entity.BlazeTitanVoid.name") + "§f";
        }

        return null;
    }

    public void onLivingUpdate() {
        switch (fetchVariantGiant()) {
            case 0: {
                    enactAuraEffect(3, getEntityBoundingBox().grow(super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant()).offset(0, -8, 0));
                    break;
                }

            case 3: {
                    enactAuraEffect(5, getEntityBoundingBox().grow(super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant()).offset(0, -8, 0));
                    break;
                }

            case 4: {
                    enactAuraEffect(4, getEntityBoundingBox().grow(super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant()));
                    break;
                }
        }

        if (fetchWaiting()) {
            if (allowAwaken()) {
                awaken();
            }
        }

        setSize(0.5F * fetchScaleMultiplierGiant(), 0.5F * fetchScaleMultiplierGiant());
        isAirBorne = true;
        onGround = false;
        if ((world.isRemote) && (deathTicks < fetchHoldThreash())) {
            for (int i = 0; i < getParticleCount(); i++)
                world.spawnParticle(forParticle(fetchParticle()), posX + (rand.nextDouble() - 0.5D) * width, posY - 24.0D + rand.nextDouble() * 32.0D, posZ + (rand.nextDouble() - 0.5D) * width, 0D, 0D, 0D);
            if ((TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V) == true) && (rand.nextInt(20) == 0)) {
                for (int i = 0; i < 2; i++) {
                    world.spawnParticle(EnumParticleTypes.FLAME, posX + (rand.nextDouble() - 0.5D) * width, posY - 16.0D + rand.nextDouble() * 16.0D, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                }
            }
        }

        // PERF — these probes are server-only (the client never acted on them but still allocated 1008
        // BlockPos per tick), and the 1000-sample hover probe only needs its first hit.
        if (!world.isRemote) {
            BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
            for (int i = 0; i < 8; i++) {
                double d0 = posX + (rand.nextFloat() * 8.0F - 4.0F);
                double d1 = posY + rand.nextFloat() * (fetchScaleMultiplierGiant() * 1.75F - height);
                double d2 = posZ + (rand.nextFloat() * 8.0F - 4.0F);
                bp.setPos((int)d0, (int)d1 + (int)getEyeHeight(), (int)d2);
                IBlockState state = world.getBlockState(bp);
                if (state.isOpaqueCube() || state.getMaterial().isLiquid()) {
                    setPosition(posX, posY + 0.1D, posZ);
                }
            }

            for (int in = 0; in < 1000; in++) {
                double d0 = posX + (rand.nextFloat() * 8.0F - 4.0F);
                double d1 = posY - rand.nextFloat() * (fetchScaleMultiplierGiant() * 1.75F - height);
                double d2 = posZ + (rand.nextFloat() * 8.0F - 4.0F);
                bp.setPos((int)d0, (int)d1, (int)d2);
                IBlockState state = world.getBlockState(bp);
                if (state.isOpaqueCube() || state.getMaterial().isLiquid()) {
                    motionY = 0.25D;
                    break;
                }
            }
        }

        if (world.isRemote) {
            if (rand.nextInt(48) == 0) {
                world.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.HOSTILE, 10.0F + rand.nextFloat(), rand.nextFloat() * 0.3F + 0.2F, false);
            }
        }

        if (getAttackTarget() != null && !world.isRemote) {
            faceEntity(getAttackTarget(), 5.0F, 180.0F);
            getMoveHelper().setMoveTo(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 20.0D);
            if (posY < getAttackTarget().posY + 20D) {
                if (motionY < 0.0D) {
                    motionY = 0.0D;
                }

                motionY += (0.8D - motionY) * 0.8D;
            }
        }

        else if ((!onGround) && (motionY < 0.0D)) {
            motionY *= 0.25D;
        }

        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        if (getInvulTime() < 0) {
            heightOffsetUpdateTime -= 1;
            if (heightOffsetUpdateTime <= 0) {
                heightOffsetUpdateTime = 300;
                heightOffset = (40.0F + (float)rand.nextGaussian() * 16.0F);
            }

            EntityLivingBase entitylivingbase = getAttackTarget();
            if ((entitylivingbase != null) && (entitylivingbase.posY + entitylivingbase.getEyeHeight() > posY + getEyeHeight() + heightOffset)) {
                motionY += (0.9D - motionY) * 0.9D;
                isAirBorne = true;
            }
        }

        super.updateAITasks();
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected Item getDropItem() {
        return Items.BLAZE_ROD;
    }

    public int getVerticalFaceSpeed() {
        return 180;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < 32; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(18000);
                world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.BLAZE_ROD));
                itembomb.assignTotalStack(24 + rand.nextInt(24 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.BLAZE_POWDER));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 6; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.COAL));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.EMERALD));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.DIAMOND));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 3; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GOLD_INGOT));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanGear.harcadium));
                itembomb.assignTotalStack(8 + rand.nextInt(8 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            if (rand.nextInt(5) == 0)
                for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.BEDROCK));
                itembomb.assignTotalStack(1);
                world.spawnEntity(itembomb);
            }

            if (rand.nextInt(5) == 0)
                for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.BEDROCK));
                itembomb.assignTotalStack(1);
                world.spawnEntity(itembomb);
            }
        }
    }

    public boolean isChargedBlaze() {
        return fetchVariantGiant() != 0 ? false : (this.dataManager.get(ON_FIRE).byteValue() & 0x1) != 0;
    }

    public void assignChargedBlaze(boolean arg70844n1) {
        byte b0 = this.dataManager.get(ON_FIRE).byteValue();
        if (arg70844n1) {
            b0 = (byte)(b0 | 0x1);
        }

        else {
            b0 = (byte)(b0 & 0xFFFFFFFE);
        }

        this.dataManager.set(ON_FIRE, Byte.valueOf(b0));
    }

    public boolean isBurning() {
        return isChargedBlaze() && hurtResistantTime < 20;
    }

    protected boolean isValidLightLevel() {
        return true;
    }

    public Entity[] getParts() {
        // List.toArray() returns Object[], not Entity[]; typed overload avoids the CCE at world-add.
        return setPiece.toArray(new Entity[0]);
    }

    public boolean processMovementLava() {
        return false;
    }

    protected void collideWithEntity(Entity arg82167n1) {
        if ((!(arg82167n1 instanceof EntitySmallFireball)) || (!(arg82167n1 instanceof EntityLargeFireball))) {
            arg82167n1.applyEntityCollision(this);
        }
    }

    public String fetchAchievement() {
        return TitanTrophies.blazetitan;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntityBlazeMinion)) || ((source.getTrueSource() instanceof EntityBlazeTitan))) {
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
            playSound(SoundEvents.ENTITY_BLAZE_DEATH, getSoundVolume(), getSoundPitch());
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(false);
            entitytitan.assignKindSpirit(9);
            entitytitan.setSubType(fetchVariantGiant());
        }
    }

    class AIFireballAttack extends EntityAIBase {
        private EntityBlazeTitan blazeThe = EntityBlazeTitan.this;
        private int attackStep;
        private int attackTime;
        public AIFireballAttack() {
            setMutexBits(4);
        }

        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = blazeThe.getAttackTarget();
            return (entitylivingbase != null) && (entitylivingbase.isEntityAlive());
        }

        public void startExecuting() {
            attackStep = 0;
            if (blazeThe.fetchVariantGiant() == 0)
                blazeThe.assignChargedBlaze(true);
        }

        public void resetTask() {
            blazeThe.assignChargedBlaze(false);
        }

        public void updateTask() {
            attackTime -= 1;
            EntityLivingBase entitylivingbase = blazeThe.getAttackTarget();
            double d0 = blazeThe.getDistance(entitylivingbase);
            if (d0 <= blazeThe.fetchReachMelee()) {
                if (attackTime <= 0)
                    attackTime = 30;
                if (attackTime == 18) {
                    float f = (float)fetchAmountStrike(4.0F);
                    int i = blazeThe.fetchAmountKnockback();
                    blazeThe.strikeMobChoosen(entitylivingbase, f, i);
                }
            }

            else if (d0 > blazeThe.fetchReachMelee()) {
                double d1 = entitylivingbase.posX - blazeThe.posX;
                double d2 = entitylivingbase.posY - (blazeThe.posY + blazeThe.height * 0.5F);
                double d3 = entitylivingbase.posZ - blazeThe.posZ;
                if (attackTime <= 0) {
                    attackStep += 1;
                    if (attackStep == 1) {
                        attackTime = (blazeThe.isArmored() ? 10 : (20 + blazeThe.getRNG().nextInt(40)));
                    }

                    else if (attackStep <= 4) {
                        attackTime = 6;
                    }

                    else {
                        attackTime = (blazeThe.isArmored() ? 10 : (20 + blazeThe.getRNG().nextInt(40)));
                        attackStep = 0;
                    }

                    if (attackStep > 1) {
                        float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.75F;
                        blazeThe.playSound(TitanAudio.get("thetitans:titanGhastFireball"), 100F, 1.1F);
                        for (int i = 0; i < 5; i++) {
                            EntityTitanFireball entitylargefireball = new EntityTitanFireball(blazeThe.world, blazeThe, d1 + blazeThe.getRNG().nextGaussian() * f, d2, d3 + blazeThe.getRNG().nextGaussian() * f, 2);
                            double d8 = 10D;
                            Vec3d vec3 = blazeThe.getLook(1.0F);
                            entitylargefireball.posX = (blazeThe.posX + vec3.x * d8);
                            entitylargefireball.posY = (blazeThe.posY + 4.0D + vec3.y * d8);
                            entitylargefireball.posZ = (blazeThe.posZ + vec3.z * d8);
                            blazeThe.world.spawnEntity(entitylargefireball);
                            entitylargefireball.assignIDFireball(2);
                            if (blazeThe.getRNG().nextInt(50) == 0) {
                                entitylivingbase.attackEntityFrom(DamageSourceExtra.lightningBolt, 49.0F);
                                float f2 = (float)fetchAmountStrike(4.0F);
                                int i1 = fetchAmountKnockback();
                                strikeMobChoosen(entitylivingbase, f2, i1);
                                blazeThe.world.addWeatherEffect(new EntityGammaLightning(blazeThe.world, blazeThe.posX, blazeThe.posY + blazeThe.getEyeHeight(), blazeThe.posZ, 1.0F, 0.8F, 0F));
                                blazeThe.world.addWeatherEffect(new EntityGammaLightning(blazeThe.world, entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0F, 0.8F, 0F));
                            }
                        }
                    }
                }
            }

            else {
                blazeThe.getNavigator().clearPath();
            }

            super.updateTask();
        }
    }

    public boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource source, float amount) {
        dealFireDamage(source, amount);
        return true;
    }

    protected boolean dealFireDamage(DamageSource arg82195n1, float arg82195n2) {
        return attackEntityFrom(arg82195n1, arg82195n2);
    }

    public World fetchRealmBlaze() {
        return world;
    }

    protected EntityLiving fetchMinion() {
        return new EntityBlazeMinion(world);
    }

    protected double cap() {
        return super.cap();
    }
}
