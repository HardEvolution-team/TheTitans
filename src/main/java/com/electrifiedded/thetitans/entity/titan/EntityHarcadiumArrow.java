package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityHarcadiumArrow extends EntityArrow {
    private static final DataParameter<Byte> CRITICAL = EntityDataManager.<Byte>createKey(EntityHarcadiumArrow.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> KIND_ARROW = EntityDataManager.<Integer>createKey(EntityHarcadiumArrow.class, DataSerializers.VARINT);
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData;
    private boolean inGround;
    public int bePickedUpMay;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage = 50D;
    private int knockbackStrength;
    private int piercingV = 0;
    private Entity piercedV;

    public EntityHarcadiumArrow(World worldIn) {
        super(worldIn);
        setSize(0.5F, 0.5F);
    }

    public EntityHarcadiumArrow(World worldIn, double x, double y, double z) {
        super(worldIn);
        setSize(0.5F, 0.5F);
        setPosition(x, y, z);
    }

    public EntityHarcadiumArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_) {
        super(worldIn);
        shootingEntity = shooter;
        if ((shooter instanceof EntityPlayer)) {
            bePickedUpMay = 1;
        }

        posY = (shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D);
        double d0 = p_i1755_3_.posX - shooter.posX;
        double d1 = p_i1755_3_.getEntityBoundingBox().minY + p_i1755_3_.height / 3.0F - posY;
        double d2 = p_i1755_3_.posZ - shooter.posZ;
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        if (d3 >= 1.0E-7D) {
            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
            float f3 = (float)-(Math.atan2(d1, d3) * 180.0D / 3.141592653589793D);
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            setLocationAndAngles(shooter.posX + d4, posY, shooter.posZ + d5, f2, f3);
            float f4 = (float)(d3 * 0.20000000298023224D);
            assignHeadingThrowable(d0, d1 + f4, d2, p_i1755_4_, p_i1755_5_);
        }
    }

    public EntityHarcadiumArrow(World worldIn, EntityLivingBase shooter, float p_i1756_3_) {
        super(worldIn);
        shootingEntity = shooter;
        if ((shooter instanceof EntityPlayer)) {
            bePickedUpMay = 1;
        }

        setSize(0.5F, 0.5F);
        setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        posX -= MathHelper.cos(rotationYaw / 180.0F * 3.1415927F) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin(rotationYaw / 180.0F * 3.1415927F) * 0.16F;
        setPosition(posX, posY, posZ);
        motionX = (-MathHelper.sin(rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(rotationPitch / 180.0F * 3.1415927F));
        motionZ = (MathHelper.cos(rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(rotationPitch / 180.0F * 3.1415927F));
        motionY = (-MathHelper.sin(rotationPitch / 180.0F * 3.1415927F));
        assignHeadingThrowable(motionX, motionY, motionZ, p_i1756_3_ * 1.5F, 1.0F);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CRITICAL, Byte.valueOf((byte)0));
        this.dataManager.register(KIND_ARROW, Integer.valueOf(0));
    }

    public int fetchKindArrow() {
        return this.dataManager.get(KIND_ARROW).intValue();
    }

    public void assignKindArrow(int type) {
        this.noClip = (type == 2);
        this.dataManager.set(KIND_ARROW, Integer.valueOf(type));
    }

    public void assignHeadingThrowable(double x, double y, double z, float velocity, float inaccuracy) {
        float f2 = MathHelper.sqrt(x * x + y * y + z * z);
        x /= f2;
        y /= f2;
        z /= f2;
        x += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
        y += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
        z += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
        x *= velocity;
        y *= velocity;
        z *= velocity;
        motionX = x;
        motionY = y;
        motionZ = z;
        float f3 = MathHelper.sqrt(x * x + z * z);
        prevRotationYaw = (rotationYaw = (float)(Math.atan2(x, z) * 180.0D / 3.141592653589793D));
        prevRotationPitch = (rotationPitch = (float)(Math.atan2(y, f3) * 180.0D / 3.141592653589793D));
        ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double arg180426n1, double arg180426n3, double arg180426n5, float arg180426n7, float arg180426n8, int arg180426n9, boolean arg180426n10) {
        setPosition(arg180426n1, arg180426n3, arg180426n5);
        setRotation(arg180426n7, arg180426n8);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) {
        motionX = x;
        motionY = y;
        motionZ = z;
        if ((prevRotationPitch == 0.0F) && (prevRotationYaw == 0.0F)) {
            float f = MathHelper.sqrt(x * x + z * z);
            prevRotationYaw = (rotationYaw = (float)(Math.atan2(x, z) * 180.0D / 3.141592653589793D));
            prevRotationPitch = (rotationPitch = (float)(Math.atan2(y, f) * 180.0D / 3.141592653589793D));
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            ticksInGround = 0;
        }
    }

    public void onUpdate() {
        onEntityUpdate();
        com.electrifiedded.thetitans.util.Maths.Vec3 vector = new com.electrifiedded.thetitans.util.Maths.Vec3(motionX, motionY, motionZ);
        if (world.isRemote && fetchKindArrow() == 1)
            if (!inGround && vector.speed() > 0.1D)
            for (int i = 0; i < 2; i++)
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + motionX * i / 4.0D, posY + motionY * i / 4.0D, posZ + motionZ * i / 4.0D, -(vector.xVec * 0.25), -(vector.yVec * 0.25), -(vector.zVec * 0.25));
        else if (ticksExisted % 2 == 0)
            for (int i = 0; i < 2; i++)
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + Maths.random(-0.05, 0.05), posY + Maths.random(-0.05, 0.05), posZ + Maths.random(-0.05, 0.05), 0.0D, 0.0D, 0.0D);
        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float)(Math.atan2(motionY, (double)f) * 180.0D / Math.PI);
        }

        BlockPos tilePos = new BlockPos(xTile, yTile, zTile);
        IBlockState tileState = world.getBlockState(tilePos);
        Block block = tileState.getBlock();
        if (fetchKindArrow() != 2 && tileState.getMaterial() != Material.AIR) {
            AxisAlignedBB axisalignedbb = tileState.getCollisionBoundingBox(world, tilePos);
            // 1.12 collision box is block-local; offset by pos before the vec-inside test
            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(tilePos).contains(new Vec3d(posX, posY, posZ)))
                inGround = true;
        }

        if (arrowShake > 0)
            --arrowShake;
        if (inGround) {
            int j = block.getMetaFromState(tileState);
            if ((block == inTile) && (j == inData)) {
                ticksInGround += 1;
                if (shootingEntity != null && (shootingEntity instanceof EntitySkeletonTitan || (shootingEntity instanceof EntityLivingBase && !((EntityLivingBase)shootingEntity).getHeldItemMainhand().isEmpty() && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, ((EntityLivingBase)shootingEntity).getHeldItemMainhand()) > 0))) {
                    world.newExplosion(this, posX, posY, posZ, 1F + (rand.nextFloat() * 7F), false, world.getGameRules().getBoolean("mobGriefing"));
                    setDead();
                }

                if (ticksInGround >= 1200 || (ticksInGround >= 100 && !(shootingEntity instanceof EntityPlayer)))
                    setDead();
            }

            else {
                inGround = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                ticksInGround = 0;
                ticksInAir = 0;
            }
        }

        else {
            ticksInAir += 1;
            Vec3d vec31 = new Vec3d(posX, posY, posZ);
            Vec3d vec3 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            RayTraceResult movingobjectposition = world.rayTraceBlocks(vec31, vec3, false, true, false);
            vec31 = new Vec3d(posX, posY, posZ);
            vec3 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            if (movingobjectposition != null)
                vec3 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
            Entity entity = null;
            List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(motionX, motionY, motionZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            for (Object mobRef1Obj : list) {
                Entity mobRef1 = (Entity) mobRef1Obj;
                if (mobRef1 != piercedV && (mobRef1.canBeCollidedWith()) && ((mobRef1 != shootingEntity) || (ticksInAir >= 5))) {
                    float f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = mobRef1.getEntityBoundingBox().grow(f1, f1, f1);
                    RayTraceResult movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
                    if (movingobjectposition1 != null) {
                        double d1 = vec31.distanceTo(movingobjectposition1.hitVec);
                        if ((d1 < d0) || (d0 == 0.0D)) {
                            entity = mobRef1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
                movingobjectposition = new RayTraceResult(entity);
            if ((movingobjectposition != null) && (movingobjectposition.entityHit != null) && ((movingobjectposition.entityHit instanceof EntityPlayer))) {
                EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;
                if ((entityplayer.capabilities.disableDamage) || (((shootingEntity instanceof EntityPlayer)) && (!((EntityPlayer)shootingEntity).canAttackPlayer(entityplayer))))
                    movingobjectposition = null;
            }

            if (movingobjectposition != null)
                if (movingobjectposition.entityHit != null) {
                if (allowMobHit(movingobjectposition.entityHit)) {
                    float f2 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    DamageSource damagesource;
                    if (shootingEntity == null)
                        damagesource = DamageSourceExtra.causeArrowHarmHarcadium(this, this);
                    else
                        damagesource = DamageSourceExtra.causeArrowHarmHarcadium(this, shootingEntity);
                    float f1 = (float)damage;
                    if (fetchKindArrow() != 2 && (movingobjectposition.entityHit instanceof EntityTitan || movingobjectposition.entityHit.getEyeHeight() >= 6.0F)) {
                        if (shootingEntity != null)
                            damagesource = DamageSourceExtra.causeGiantHarmAnti(shootingEntity);
                        f1 *= com.electrifiedded.thetitans.TheTitans.getBonusDamageVSGiantMobs(movingobjectposition.entityHit instanceof EntityTitan);
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanpunch", world, movingobjectposition.entityHit, 10.0F, 1.0F);
                    }

                    if (fetchKindArrow() != 2 && movingobjectposition.entityHit instanceof com.electrifiedded.thetitans.entity.minion.IMinion && com.electrifiedded.thetitans.TheTitans.isHeraldLevel(((com.electrifiedded.thetitans.entity.minion.IMinion)movingobjectposition.entityHit).fetchKindMinion())) {
                        if (shootingEntity != null)
                            damagesource = DamageSourceExtra.causeGiantHarmAnti(shootingEntity);
                        switch (((com.electrifiedded.thetitans.entity.minion.IMinion)movingobjectposition.entityHit).fetchKindMinion()) {
                            case TEMPLAR:
                                f1 *= com.electrifiedded.thetitans.TheTitans.getBonusDamageVSGiantMobs(true) * 0.1F;
                                break;
                            case ARBITRATOR:
                                f1 *= com.electrifiedded.thetitans.TheTitans.getBonusDamageVSGiantMobs(true) * 0.01F;
                                break;
                            case GRANDMASTER:
                                f1 *= com.electrifiedded.thetitans.TheTitans.getBonusDamageVSGiantMobs(true) * 0.001F;
                                break;
                            default:
                                f1 *= com.electrifiedded.thetitans.TheTitans.getBonusDamageVSGiantMobs(true) * 1.0E-4F;
                        }

                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanpunch", world, movingobjectposition.entityHit, 10.0F, 1.0F);
                    }

                    if (getIsCritical())
                        f1 *= 2.0F;
                    if (fetchKindArrow() == 1)
                        if (movingobjectposition.entityHit.hurtResistantTime > 5)
                        movingobjectposition.entityHit.hurtResistantTime = 5;
                    else
                        if (movingobjectposition.entityHit.hurtResistantTime > 10)
                        movingobjectposition.entityHit.hurtResistantTime = 10;
                    if (isBurning())
                        movingobjectposition.entityHit.setFire(150);
                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, f2 * f1)) {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                            EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;
                            if (fetchKindArrow() == 2 && shootingEntity instanceof EntitySkeletonMinion && ((EntitySkeletonMinion)shootingEntity).fetchKindIntMinion() >= 3)
                                ((EntitySkeletonMinion)shootingEntity).attackEntityAsMob(entitylivingbase);
                            if (!world.isRemote)
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            if (knockbackStrength > 0) {
                                float f4 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
                                if (f4 > 0.0F)
                                    movingobjectposition.entityHit.addVelocity(motionX * knockbackStrength * 1.5D / f4, 0.75D, motionZ * knockbackStrength * 1.5D / f4);
                            }

                            if ((shootingEntity instanceof EntityLivingBase)) {
                                EnchantmentHelper.applyThornEnchantments(entitylivingbase, shootingEntity);
                                EnchantmentHelper.applyArthropodEnchantments(entitylivingbase, shootingEntity);
                            }

                            if ((shootingEntity != null) && (movingobjectposition.entityHit != shootingEntity) && ((movingobjectposition.entityHit instanceof EntityPlayer)) && ((shootingEntity instanceof EntityPlayerMP)))
                                ((EntityPlayerMP)shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                        }

                        playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                        if (movingobjectposition.entityHit.isEntityAlive())
                            piercingV ++;
                        if (!(movingobjectposition.entityHit instanceof EntityEnderman) && ((fetchKindArrow() == 2) || (fetchKindArrow() == 1 && piercingV > 9) || (piercingV > 4))) {
                            setDead();
                            if (bePickedUpMay == 1)
                                switch (fetchKindArrow()) {
                                case 1:
                                    movingobjectposition.entityHit.dropItem(TitanGear.arrowVoid, 1);
                                    break;
                                case 2:
                                    break;
                                default:
                                    movingobjectposition.entityHit.dropItem(TitanGear.arrowHarcadium, 1);
                            }
                        }
                    }
                }
            }

            else if (fetchKindArrow() != 2) {
                BlockPos hitPos = movingobjectposition.getBlockPos();
                xTile = hitPos.getX();
                yTile = hitPos.getY();
                zTile = hitPos.getZ();
                IBlockState inState = world.getBlockState(hitPos);
                inTile = inState.getBlock();
                inData = inTile.getMetaFromState(inState);
                if (inTile instanceof BlockGlass) {
                    playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F / (rand.nextFloat() * 0.2F + 0.9F));
                    world.setBlockToAir(hitPos);
                }

                else {
                    motionX = ((float)(movingobjectposition.hitVec.x - posX));
                    motionY = ((float)(movingobjectposition.hitVec.y - posY));
                    motionZ = ((float)(movingobjectposition.hitVec.z - posZ));
                    float f2 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    posX -= motionX / f2 * 0.05000000074505806D;
                    posY -= motionY / f2 * 0.05000000074505806D;
                    posZ -= motionZ / f2 * 0.05000000074505806D;
                    inGround = true;
                    arrowShake = 7;
                    playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                    setIsCritical(false);
                    if (inState.getMaterial() != Material.AIR)
                        inTile.onEntityCollision(world, hitPos, inState, this);
                }
            }

            if (fetchKindArrow() == 2) {
                if (getIsCritical())
                    for (int i = 0; i < 4; i++)
                    world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, posX + motionX * i / 4.0D, posY + motionY * i / 4.0D, posZ + motionZ * i / 4.0D, -motionX, -motionY + 0.2D, -motionZ);
                for (int i = 0; i < 4; i++)
                    world.spawnParticle(EnumParticleTypes.CRIT, posX + motionX * i / 4.0D, posY + motionY * i / 4.0D, posZ + motionZ * i / 4.0D, -motionX, -motionY + 0.2D, -motionZ);
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float f2 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
            for (rotationPitch = (float)(Math.atan2(motionY, (double)f2) * 180.0D / Math.PI); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F);
            while (rotationPitch - prevRotationPitch >= 180.0F)
                prevRotationPitch += 360.0F;
            while (rotationYaw - prevRotationYaw < -180.0F)
                prevRotationYaw -= 360.0F;
            while (rotationYaw - prevRotationYaw >= 180.0F)
                prevRotationYaw += 360.0F;
            rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
            rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
            float f3 = 0.99F;
            float f1 = 0.05F;
            if (isInWater()) {
                for (int l = 0; l < 4; ++l) {
                    float f4 = 0.25F;
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * (double)f4, posY - motionY * (double)f4, posZ - motionZ * (double)f4, motionX, motionY, motionZ);
                }

                f3 = 0.8F;
            }

            if (isWet())
                extinguish();
            motionX *= (double)f3;
            motionY *= (double)f3;
            motionZ *= (double)f3;
            motionY -= (double)f1;
            setPosition(posX, posY, posZ);
            doBlockCollisions();
        }
    }

    private boolean allowMobHit(Entity entityHit) {
        return EntityTitan.quackosIs(entityHit) ? false : (shootingEntity == null ? true : (!(shootingEntity instanceof EntitySkeletonMinion) || !(entityHit instanceof EntitySkeletonMinion)) && (!(shootingEntity instanceof EntitySkeletonTitan) || ((EntitySkeletonTitan)shootingEntity).allowStrikeGiant(entityHit.getClass())));
    }

    public void writeEntityToNBT(NBTTagCompound arg70014n1) {
        arg70014n1.setShort("xTile", (short)xTile);
        arg70014n1.setShort("yTile", (short)yTile);
        arg70014n1.setShort("zTile", (short)zTile);
        arg70014n1.setShort("life", (short)ticksInGround);
        arg70014n1.setByte("inTile", (byte)Block.getIdFromBlock(inTile));
        arg70014n1.setByte("inData", (byte)inData);
        arg70014n1.setByte("shake", (byte)arrowShake);
        arg70014n1.setByte("inGround", (byte)(inGround ? 1 : 0));
        arg70014n1.setByte("pickup", (byte)bePickedUpMay);
        arg70014n1.setDouble("damage", damage);
        arg70014n1.setInteger("ArrowType", fetchKindArrow());
    }

    public void readEntityFromNBT(NBTTagCompound arg70037n1) {
        assignKindArrow(arg70037n1.getInteger("ArrowType"));
        xTile = arg70037n1.getShort("xTile");
        yTile = arg70037n1.getShort("yTile");
        zTile = arg70037n1.getShort("zTile");
        ticksInGround = arg70037n1.getShort("life");
        inTile = Block.getBlockById(arg70037n1.getByte("inTile") & 0xFF);
        inData = (arg70037n1.getByte("inData") & 0xFF);
        arrowShake = (arg70037n1.getByte("shake") & 0xFF);
        inGround = (arg70037n1.getByte("inGround") == 1);
        if (arg70037n1.hasKey("damage", 99)) {
            damage = arg70037n1.getDouble("damage");
        }

        if (arg70037n1.hasKey("pickup", 99)) {
            bePickedUpMay = arg70037n1.getByte("pickup");
        }

        else if (arg70037n1.hasKey("player", 99)) {
            bePickedUpMay = (arg70037n1.getBoolean("player") ? 1 : 0);
        }
    }

    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if ((!world.isRemote) && (inGround) && (arrowShake <= 0)) {
            boolean flag = (bePickedUpMay == 1) || ((bePickedUpMay == 2) && (entityIn.capabilities.isCreativeMode));
            if ((bePickedUpMay == 1) && (!entityIn.inventory.addItemStackToInventory(new ItemStack(fetchKindArrow() == 1 ? TitanGear.arrowVoid : TitanGear.arrowHarcadium, 1)))) {
                flag = false;
            }

            if (flag) {
                playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityIn.onItemPickup(this, 1);
                setDead();
            }
        }
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public void setDamage(double arg70239n1) {
        damage = arg70239n1;
    }

    public double getDamage() {
        return damage;
    }

    public void setKnockbackStrength(int arg70240n1) {
        knockbackStrength = arg70240n1;
    }

    public boolean allowWithStackStrike() {
        return false;
    }

    public void setIsCritical(boolean arg70243n1) {
        byte b0 = this.dataManager.get(CRITICAL).byteValue();
        if (arg70243n1) {
            this.dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 | 0x1)));
        }

        else {
            this.dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
        }
    }

    public boolean getIsCritical() {
        byte b0 = this.dataManager.get(CRITICAL).byteValue();
        return (b0 & 0x1) != 0;
    }

    // EntityArrow is abstract in 1.12.2 and requires getArrowStack(); pick-up item mirrors arrow type
    protected ItemStack getArrowStack() {
        return new ItemStack(fetchKindArrow() == 1 ? TitanGear.arrowVoid : TitanGear.arrowHarcadium, 1);
    }
}
