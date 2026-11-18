package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.entity.titan.ai.EntityAIDefendVillage;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityGargoyle extends EntityGolem {
    private static final DataParameter<Byte> KIND_GARGOYLE = EntityDataManager.<Byte>createKey(EntityGargoyle.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.<Byte>createKey(EntityGargoyle.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> KEY_VICTIM = EntityDataManager.<Integer>createKey(EntityGargoyle.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> AGGRESSIVE = EntityDataManager.<Byte>createKey(EntityGargoyle.class, DataSerializers.BYTE);
    public double xWaypoint;
    public double yWaypoint;
    public double zWaypoint;
    private int attackTimer;
    private int fld175479bo;
    private EntityLivingBase fld175478bn;
    private int homeCheckTimer;
    Village objVillage;

    public EntityGargoyle(World p_i1694_1_) {
        super(p_i1694_1_);
        switch (this.fetchKindGargoyle()) {
            case 0:
            default:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
                this.setHealth(50F);
                break;
            case 1:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
                this.setHealth(30F);
                break;
            case 2:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
                this.setHealth(200F);
                break;
            case 3:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
                this.setHealth(80F);
                break;
            case 4:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
                this.setHealth(100F);
                break;
            case 5:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
                this.setHealth(120F);
                break;
            case 6:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
                this.setHealth(60F);
                break;
        }

        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Blocks.STONE));
        this.setSize(0.75F, 2.5F);
        if (this.getNavigator() instanceof net.minecraft.pathfinding.PathNavigateGround)
            ((net.minecraft.pathfinding.PathNavigateGround)this.getNavigator()).setCanSwim(false);
        this.tasks.addTask(1, new EntityGargoyle.AIPerch());
        this.tasks.addTask(2, new EntityGargoyle.AIBeamAttack());
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(6, new EntityAIWander(this, 0.75D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new EntityAIDefendVillage(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, IMob.MOB_SELECTOR));
    }

    protected void updateAITasks() {
        if (--this.homeCheckTimer <= 0) {
            this.homeCheckTimer = 70 + this.rand.nextInt(50);
            this.objVillage = this.world.getVillageCollection().getNearestVillage(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)), 32);
            if (this.objVillage == null) {
                this.detachHome();
            }

            else {
                BlockPos blockpos = this.objVillage.getCenter();
                this.setHomePosAndDistance(blockpos, (int)((float)this.objVillage.getVillageRadius() * 0.6F));
            }
        }

        super.updateAITasks();
    }

    public void onDeath(DamageSource arg70645n1) {
        if (!this.isPlayerCreated() && this.attackingPlayer != null && this.objVillage != null) {
            this.objVillage.modifyPlayerReputation(this.attackingPlayer.getName(), -5);
        }

        super.onDeath(arg70645n1);
    }

    public Village getVillage() {
        return this.objVillage;
    }

    public float getBlockPathWeight(BlockPos pos) {
        Block block = this.world.getBlockState(pos.down()).getBlock();
        return (block == Blocks.PLANKS || block == Blocks.COBBLESTONE) ? 10.0F : this.world.getLightBrightness(pos) - 0.5F;
    }

    public float funcP175477(float arg175477n1) {
        return ((float)this.fld175479bo + arg175477n1) / 80F;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_GARGOYLE, Byte.valueOf((byte)0));
        this.dataManager.register(PLAYER_CREATED, Byte.valueOf((byte)0));
        this.dataManager.register(KEY_VICTIM, Integer.valueOf(0));
        this.dataManager.register(AGGRESSIVE, Byte.valueOf((byte)0));
    }

    public int fetchKindGargoyle() {
        return this.dataManager.get(KIND_GARGOYLE);
    }

    public void assignKindGargoyle(int arg82201n1) {
        this.dataManager.set(KIND_GARGOYLE, Byte.valueOf((byte)arg82201n1));
        switch (arg82201n1) {
            case 0:
            default:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
                this.setHealth(50F);
                break;
            case 1:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
                this.setHealth(30F);
                break;
            case 2:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
                this.setHealth(200F);
                break;
            case 3:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
                this.setHealth(80F);
                break;
            case 4:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
                this.setHealth(100F);
                break;
            case 5:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
                this.setHealth(120F);
                break;
            case 6:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
                this.setHealth(60F);
                break;
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
        if (tagCompund.hasKey("GargoyleType", 99)) {
            assignKindGargoyle(tagCompund.getByte("GargoyleType"));
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("PlayerCreated", isPlayerCreated());
        tagCompound.setByte("GargoyleType", (byte)fetchKindGargoyle());
    }

    public void setAggressive(boolean arg82197n1) {
        this.dataManager.set(AGGRESSIVE, Byte.valueOf((byte)(arg82197n1 ? 1 : 0)));
    }

    public boolean fetchAggressive() {
        return this.dataManager.get(AGGRESSIVE) == 1;
    }

    protected float applyPotionDamageCalculations(DamageSource arg70672n1, float arg70672n2) {
        arg70672n2 = super.applyPotionDamageCalculations(arg70672n1, arg70672n2);
        if (arg70672n1.getTrueSource() == this) {
            arg70672n2 = 0.0F;
        }

        if (arg70672n1.isExplosion()) {
            arg70672n2 = (float)((double)arg70672n2 * 2D);
        }

        if (arg70672n1.isFireDamage()) {
            arg70672n2 = 0F;
        }

        return arg70672n2;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 == EntityGargoyle.class) || (arg70686n1 == EntityGargoyleTitan.class) ? false : (isPlayerCreated() && EntityPlayer.class.isAssignableFrom(arg70686n1) ? false : true /* vanilla canAttackClass default */);
    }

    public boolean isPlayerCreated() {
        return (this.dataManager.get(PLAYER_CREATED).byteValue() & 0x1) != 0;
    }

    public void setPlayerCreated(boolean arg70849n1) {
        byte b0 = this.dataManager.get(PLAYER_CREATED).byteValue();
        if (arg70849n1) {
            this.dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 | 0x1)));
        }

        else {
            this.dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isEnabledAI() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    /**
     * Decrements the entity's air supply when underwater
     */
    protected int decreaseAirSupply(int arg70682n1) {
        return arg70682n1;
    }

    protected void collideWithEntity(Entity arg82167n1) {
        if (arg82167n1 instanceof IMob && this.getRNG().nextInt(20) == 0) {
            this.setAttackTarget((EntityLivingBase)arg82167n1);
        }

        if (arg82167n1 instanceof EntityGargoyle && this.getAttackTarget() == null && ((EntityGargoyle)arg82167n1).getAttackTarget() == null) {
            if (this.onGround && ((EntityGargoyle)arg82167n1).onGround && this.getDistanceSq(((EntityGargoyle)arg82167n1).xWaypoint, ((EntityGargoyle)arg82167n1).yWaypoint, ((EntityGargoyle)arg82167n1).zWaypoint) < 4D) {
                ++this.yWaypoint;
                this.noClip = false;
            }
        }

        super.collideWithEntity(arg82167n1);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.ticksExisted == 1) {
            switch (this.fetchKindGargoyle()) {
                case 0:
                default:
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
                    this.setHealth(50F);
                case 1:
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
                    this.setHealth(30F);
                case 2:
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
                    this.setHealth(200F);
                case 3:
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
                    this.setHealth(80F);
                case 4:
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
                    this.setHealth(100F);
                case 5:
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
                    this.setHealth(120F);
                case 6:
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
                    this.setHealth(60F);
            }
        }

        if (this.fetchKindGargoyle() == 6) {
            this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
        }

        if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        if (this.getVillage() != null && !this.world.isRemote && this.getAttackTarget() == null && this.getDistanceSq(this.getVillage().getCenter().getX(), this.getVillage().getCenter().getY(), this.getVillage().getCenter().getZ()) > 32D * 32D) {
            double d0 = this.getVillage().getCenter().getX() - this.posX;
            double d1 = this.getVillage().getCenter().getZ() - this.posZ;
            double d3 = d0 * d0 + d1 * d1;
            if (this.posY <= this.getVillage().getCenter().getY() + 1D)
                this.motionY += 0.6D - this.motionY;
            double d5 = MathHelper.sqrt(d3);
            this.motionX += d0 / d5 * 0.6D - this.motionX;
            this.motionZ += d1 / d5 * 0.6D - this.motionZ;
        }

        if (this.getVillage() != null && !this.world.isRemote && this.getAttackTarget() == null) {
            List<?> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double)(this.getVillage().getCenter().getX() - this.getVillage().getVillageRadius()), (double)(this.getVillage().getCenter().getY() - 4), (double)(this.getVillage().getCenter().getZ() - this.getVillage().getVillageRadius()), (double)(this.getVillage().getCenter().getX() + this.getVillage().getVillageRadius()), (double)(this.getVillage().getCenter().getY() + 4), (double)(this.getVillage().getCenter().getZ() + this.getVillage().getVillageRadius())));
            if (list != null && !list.isEmpty() && this.rand.nextInt(2) == 0) {
                for (Object entityObj : list) {
                    Entity entity = (Entity) entityObj;
                    if (this.isEntityAlive() && entity.isEntityAlive() && this.allowStrikeGiant(entity.getClass()) && entity instanceof EntityLivingBase && entity instanceof IMob) {
                        this.setAttackTarget((EntityLivingBase)entity);
                    }
                }
            }
        }

        if (!this.world.isRemote && this.getAttackTarget() == null) {
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(16D, 32D, 16D));
            if (list != null && !list.isEmpty() && this.rand.nextInt(20) == 0) {
                for (Object entityObj : list) {
                    Entity entity = (Entity) entityObj;
                    if (this.isEntityAlive() && entity.isEntityAlive() && this.allowStrikeGiant(entity.getClass()) && entity instanceof EntityLivingBase && entity instanceof IMob) {
                        this.setAttackTarget((EntityLivingBase)entity);
                    }
                }
            }
        }

        if ((this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive())) {
            this.setAttackTarget(null);
        }

        BlockPos waypointPos = new BlockPos((int)xWaypoint, (int)yWaypoint, (int)zWaypoint);
        if (this.getAttackTarget() == null && this.fetchCubeNature(this.world.getBlockState(waypointPos).getBlock()) && this.fetchCubeNature(this.world.getBlockState(waypointPos).getBlock()) && this.world.canBlockSeeSky(new BlockPos((int)xWaypoint, (int)yWaypoint + 1, (int)zWaypoint))) {
            double d0 = this.xWaypoint - this.posX;
            double d1 = this.yWaypoint + 1 - this.posY;
            double d2 = this.zWaypoint - this.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 == 4D)
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead += 180F;
            if (d3 > 3D) {
                double d5 = MathHelper.sqrt(d3);
                this.motionX += d0 / d5 * 0.6D - this.motionX;
                this.motionY += d1 / d5 * 0.6D - this.motionY;
                this.motionZ += d2 / d5 * 0.6D - this.motionZ;
                this.getLookHelper().setLookPosition(xWaypoint, yWaypoint, zWaypoint, 180F, 0F);
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
                this.noClip = true;
            }

            else {
                this.setLocationAndAngles(xWaypoint + 0.5D, yWaypoint + 1D, zWaypoint + 0.5D, this.rotationYawHead, 40F);
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
                this.noClip = false;
                this.extinguish();
                BlockPos belowPos = new BlockPos((int)posX, (int)posY - 1, (int)posZ);
                if (this.ticksExisted % (this.world.getBlockState(belowPos).getBlock() == this.fetchCubeToPerchFavorite() ? 20 : 40) == 0)
                    this.heal(this.world.getBlockState(belowPos).getBlock() == this.fetchCubeToPerchFavorite() ? 2F : 1F);
            }
        }

        Entity entity = this.getAttackTarget();
        double move = entity != null ? 0.2D + (this.rand.nextDouble() * 0.6D) : 0.6D;
        if (entity != null)
            this.setAggressive(true);
        else
            this.setAggressive(false);
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= move;
        }

        if (!this.world.isRemote && entity != null) {
            double d0 = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            double d3 = d0 * d0 + d1 * d1;
            if ((d3 > (this.fetchKindGargoyle() == 3 ? 256D : 4D)) && (this.canEntityBeSeen(entity) || this.isEntityInsideOpaqueBlock() || this.posY <= 0D)) {
                if (this.posY <= entity.posY + 1D)
                    this.motionY += move - this.motionY;
                double d5 = MathHelper.sqrt(d3);
                this.motionX += d0 / d5 * move - this.motionX;
                this.motionZ += d1 / d5 * move - this.motionZ;
                this.faceEntity(entity, 180F, 40F);
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
            }
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor(this.posZ);
            BlockPos crackPos = new BlockPos(i, j, k);
            IBlockState state = this.world.getBlockState(crackPos);
            if (state.getMaterial() != Material.AIR) {
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, Block.getStateId(state));
            }
        }

        if (this.fetchCubeNature(this.world.getBlockState(new BlockPos((int)xWaypoint, (int)yWaypoint + 1, (int)zWaypoint)).getBlock()))
            ++this.yWaypoint;
        if (!this.world.isRemote && this.ticksExisted % 10 == 0 && !fetchCubeNature(this.world.getBlockState(waypointPos).getBlock()) && (this.world.getBlockState(new BlockPos((int)posX, (int)posY, (int)posZ)).getBlock() != this.fetchCubeToPerchFavorite() || this.world.getBlockState(waypointPos).getBlock() != this.fetchCubeToPerchFavorite() || this.world.getBlockState(new BlockPos((int)xWaypoint, (int)yWaypoint + 1, (int)zWaypoint)).getMaterial() != Material.AIR || this.world.getBlockState(new BlockPos((int)xWaypoint, (int)yWaypoint + 2, (int)zWaypoint)).getMaterial() != Material.AIR || !this.world.canBlockSeeSky(new BlockPos((int)xWaypoint, (int)yWaypoint + 1, (int)zWaypoint)))) {
            int i = MathHelper.floor(this.posY);
            int i1 = MathHelper.floor(this.posX);
            int j1 = MathHelper.floor(this.posZ);
            for (int l1 = -6; l1 <= 6; l1++) {
                for (int i2 = -6; i2 <= 6; i2++) {
                    for (int j = -6; j <= 6; j++) {
                        int j2 = i1 + l1;
                        int k = i + j;
                        int l = j1 + i2;
                        IBlockState statemain = this.world.getBlockState(new BlockPos(j2, k, l));
                        Block blockmain = statemain.getBlock();
                        IBlockState state1 = this.world.getBlockState(new BlockPos(j2, k + 1, l));
                        IBlockState state2 = this.world.getBlockState(new BlockPos(j2, k + 2, l));
                        if (!this.world.isRemote && fetchCubeNature(blockmain) && ((blockmain == this.fetchCubeToPerchFavorite() && this.rand.nextInt(20) == 0) || this.rand.nextInt(300) == 0) && this.world.canBlockSeeSky(new BlockPos(j2, k + 1, l)) && state1.getMaterial() == Material.AIR && state2.getMaterial() == Material.AIR) {
                            this.xWaypoint = j2;
                            this.yWaypoint = k;
                            this.zWaypoint = l;
                        }
                    }
                }
            }
        }
    }

    public boolean fetchCubeNature(Block block) {
        return (block.getDefaultState().isOpaqueCube() || block == fetchCubeToPerchFavorite()) && block != Blocks.STONE && block != Blocks.BEDROCK && block != Blocks.LOG && block != Blocks.LOG2 && block != Blocks.SAND && block != Blocks.DIRT && block != Blocks.GRAVEL && block != Blocks.GRASS && block != Blocks.MYCELIUM && block != Blocks.NETHERRACK && block != Blocks.SOUL_SAND;
    }

    public boolean fetchNatureCubeUN(Block block) {
        return (!block.getDefaultState().isOpaqueCube() || block != fetchCubeToPerchFavorite()) && (block == Blocks.STONE || block == Blocks.BEDROCK || block == Blocks.LOG || block == Blocks.LOG2 || block == Blocks.SAND || block == Blocks.DIRT || block == Blocks.GRAVEL || block == Blocks.GRASS || block == Blocks.MYCELIUM || block == Blocks.NETHERRACK || block == Blocks.SOUL_SAND);
    }

    public Block fetchCubeToPerchFavorite() {
        switch (this.fetchKindGargoyle()) {
            case 0:
            default:
                return TitanBlockSet.stoneperch;
            case 1:
                return TitanBlockSet.sandstoneperch;
            case 2:
                return TitanBlockSet.obsidianperch;
            case 3:
                return TitanBlockSet.goldperch;
            case 4:
                return TitanBlockSet.ironperch;
            case 5:
                return TitanBlockSet.endstoneperch;
            case 6:
                return TitanBlockSet.netherbrickperch;
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (source == DamageSource.IN_WALL || source == DamageSource.DROWN || source == DamageSource.ON_FIRE || source == DamageSource.IN_FIRE) {
            return false;
        }

        else if ((source.getTrueSource() != null) && (source.getTrueSource() instanceof EntityGargoyleTitan)) {
            return false;
        }

        else {
            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        boolean flag = arg70652n1.attackEntityFrom(DamageSource.causeMobDamage(this), this.fetchKindGargoyle() == 6 ? 14F : this.fetchKindGargoyle() == 5 ? 26F : this.fetchKindGargoyle() == 4 ? 14F : this.fetchKindGargoyle() == 3 ? 20F : this.fetchKindGargoyle() == 2 ? 18F : this.fetchKindGargoyle() == 1 ? 4F : 8F);
        this.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
        if (flag) {
            switch (this.fetchKindGargoyle()) {
                case 0:
                default:
                    arg70652n1.motionY += 0.3D;
                    break;
                case 1:
                    arg70652n1.motionY += 0.15D;
                    break;
                case 2:
                    arg70652n1.motionY += 0.5D;
                    break;
                case 3:
                    arg70652n1.motionY += 0.6D;
                    // TODO no vanilla "mob.zombie.remedy" SoundEvent; closest villager cure sound
                    this.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F);
                    break;
                case 4:
                    arg70652n1.motionY += 0.4D;
                    break;
                case 5:
                    arg70652n1.motionY += 0.5D;
                    break;
                case 6: {
                        arg70652n1.motionY += 0.3D;
                        arg70652n1.setFire(10);
                    }
            }
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte arg70103n1) {
        if (arg70103n1 == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        }

        else {
            super.handleStatusUpdate(arg70103n1);
        }
    }

    protected SoundEvent getAmbientSound() {
        return this.fetchCubeNature(this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - 0.5D), MathHelper.floor(this.posZ))).getBlock()) ? null : com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:gargoyleLiving");
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:gargoyleGrunt");
    }

    protected SoundEvent getDeathSound() {
        return com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:gargoyleDeath");
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
    }

    public boolean funcCn175474() {
        return this.dataManager.get(KEY_VICTIM) != 0;
    }

    public EntityLivingBase getTargetedEntity() {
        if (!this.funcCn175474()) {
            return null;
        }

        else if (this.world.isRemote) {
            if (this.fld175478bn != null) {
                return this.fld175478bn;
            }

            else {
                Entity entity = this.world.getEntityByID(this.dataManager.get(KEY_VICTIM).intValue());
                if (entity instanceof EntityLivingBase) {
                    this.fld175478bn = (EntityLivingBase)entity;
                    return this.fld175478bn;
                }

                else {
                    return null;
                }
            }
        }

        else {
            return this.getAttackTarget();
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        int j = this.rand.nextInt(5);
        int k;
        switch (this.fetchKindGargoyle()) {
            case 0:
            default:
                for (k = 0; k < j; ++k) {
                    this.dropItem(Item.getItemFromBlock(Blocks.STONE), 1);
                }

                break;
            case 1:
                for (k = 0; k < j; ++k) {
                    this.dropItem(Item.getItemFromBlock(Blocks.SANDSTONE), 1);
                }

                break;
            case 2:
                for (k = 0; k < j; ++k) {
                    this.dropItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 1);
                }

                break;
            case 3:
                for (k = 0; k < j; ++k) {
                    this.dropItem(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 1);
                }

                break;
            case 4:
                for (k = 0; k < j; ++k) {
                    this.dropItem(Item.getItemFromBlock(Blocks.IRON_BLOCK), 1);
                }

                break;
            case 5:
                for (k = 0; k < j; ++k) {
                    this.dropItem(Item.getItemFromBlock(Blocks.END_STONE), 1);
                }

                break;
            case 6:
                for (k = 0; k < j; ++k) {
                    this.dropItem(Item.getItemFromBlock(Blocks.NETHER_BRICK), 1);
                }
        }
    }

    class AIBeamAttack extends EntityAIBase {
        private EntityGargoyle fld179456a = EntityGargoyle.this;
        private int fld179455b;
        public AIBeamAttack() {
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.fld179456a.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && this.fld179456a.fetchKindGargoyle() == 3;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && this.fld179456a.getAttackTarget() != null && this.fld179456a.fetchKindGargoyle() == 3;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.fld179455b = -10;
            this.fld179456a.getNavigator().clearPath();
            this.fld179456a.getLookHelper().setLookPositionWithEntity(this.fld179456a.getAttackTarget(), 90.0F, 90.0F);
            this.fld179456a.isAirBorne = true;
        }

        /**
         * Resets the task
         */
        public void resetTask() {
            this.fld179456a.setAttackTarget((EntityLivingBase)null);
        }

        /**
         * Updates the task
         */
        public void updateTask() {
            EntityLivingBase entitylivingbase = this.fld179456a.getAttackTarget();
            this.fld179456a.getNavigator().clearPath();
            this.fld179456a.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
            if (!this.fld179456a.canEntityBeSeen(entitylivingbase)) {
                this.fld179456a.setAttackTarget((EntityLivingBase)null);
            }

            else {
                ++this.fld179455b;
                fld179456a.renderYawOffset = fld179456a.rotationYaw = fld179456a.rotationYawHead;
                if (this.fld179455b > 0) {
                    entitylivingbase.attackEntityFrom(DamageSource.MAGIC, 1F);
                    entitylivingbase.setFire(1 + fld179455b);
                }

                if (this.fld179455b == 0) {
                    this.fld179456a.world.setEntityState(this.fld179456a, (byte)21);
                }

                else if (this.fld179455b >= 60) {
                    float f = 12.0F;
                    if (this.fld179456a.world.getDifficulty() == EnumDifficulty.HARD) {
                        f += 4.0F;
                    }

                    fld179456a.attackEntityAsMob(entitylivingbase);
                    entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.fld179456a, this.fld179456a), f);
                    this.fld179456a.setAttackTarget((EntityLivingBase)null);
                }

                else if (this.fld179455b >= 60 && this.fld179455b % 20 == 0) {
                    ;
                }
            }
        }
    }

    class AIPerch extends EntityAIBase {
        public AIPerch() {
            setMutexBits(7);
        }

        public boolean shouldExecute() {
            Block blockmain = EntityGargoyle.this.world.getBlockState(new BlockPos((int)EntityGargoyle.this.xWaypoint, (int)EntityGargoyle.this.yWaypoint, (int)EntityGargoyle.this.zWaypoint)).getBlock();
            return EntityGargoyle.this.fetchCubeNature(blockmain) && EntityGargoyle.this.getAttackTarget() == null;
        }
    }
}
