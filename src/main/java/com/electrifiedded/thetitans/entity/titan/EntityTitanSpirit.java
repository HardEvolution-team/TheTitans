package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCaveSpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGhastMinion;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySilverfishMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.handler.ChunkLoadHooks;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityTitanSpirit extends EntityFlying {
    private static final DataParameter<Byte> HUNTING_VESSEL = EntityDataManager.<Byte>createKey(EntityTitanSpirit.class, DataSerializers.BYTE);
    private static final DataParameter<Float> TONNAGE_V = EntityDataManager.<Float>createKey(EntityTitanSpirit.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> KIND_SPIRIT = EntityDataManager.<Integer>createKey(EntityTitanSpirit.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> LABEL_KEY_SPIRIT = EntityDataManager.<Integer>createKey(EntityTitanSpirit.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SUBTYPE = EntityDataManager.<Integer>createKey(EntityTitanSpirit.class, DataSerializers.VARINT);
    public double xWaypoint;
    public double yWaypoint;
    public double zWaypoint;
    private EntityLivingBase mobPrev;

    public EntityTitanSpirit(World worldIn) {
        super(worldIn);
        setSize(8.0F, 8.0F);
        noClip = true;
        isImmuneToFire = true;
        ignoreFrustumCheck = true;
        SoundEvent birth = com.electrifiedded.thetitans.registry.TitanAudio.get("titanBirth");
        if (birth != null)
            playSound(birth, 10000.0F, 2.0F);
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(HUNTING_VESSEL, Byte.valueOf((byte)0));
        dataManager.register(TONNAGE_V, Float.valueOf(1.0F));
        dataManager.register(KIND_SPIRIT, Integer.valueOf(0));
        dataManager.register(LABEL_KEY_SPIRIT, Integer.valueOf(0));
        dataManager.register(SUBTYPE, Integer.valueOf(0));
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        assignTonnage(tagCompund.getFloat("Tonnage"));
        assignKindSpirit(tagCompund.getInteger("SpiritType"));
        assignLabelIDSpirit(tagCompund.getInteger("SpiritNameID"));
        setSubType(tagCompund.getInteger("SubType"));
        assignHuntingVessel(tagCompund.getBoolean("ShouldHuntForVessels"));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setFloat("Tonnage", fetchTonnage());
        tagCompound.setInteger("SpiritType", fetchKindSpirit());
        tagCompound.setInteger("SpiritNameID", fetchLabelIDSpirit());
        tagCompound.setInteger("SubType", getSubType());
        tagCompound.setBoolean("ShouldHuntForVessels", isHuntingVessel());
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Double.MAX_VALUE);
    }

    public boolean isHuntingVessel() {
        return dataManager.get(HUNTING_VESSEL).byteValue() == 1;
    }

    public void assignHuntingVessel(boolean hunting) {
        dataManager.set(HUNTING_VESSEL, Byte.valueOf(hunting ? (byte)1 : (byte)0));
    }

    public int fetchKindSpirit() {
        return dataManager.get(KIND_SPIRIT).intValue();
    }

    public void assignKindSpirit(int type) {
        dataManager.set(KIND_SPIRIT, Integer.valueOf(type));
    }

    public int fetchLabelIDSpirit() {
        return dataManager.get(LABEL_KEY_SPIRIT).intValue();
    }

    public void assignLabelIDSpirit(int nameid) {
        dataManager.set(LABEL_KEY_SPIRIT, Integer.valueOf(nameid));
    }

    public int getSubType() {
        return dataManager.get(SUBTYPE).intValue();
    }

    public void setSubType(int subtype) {
        dataManager.set(SUBTYPE, Integer.valueOf(subtype));
    }

    public float fetchTonnage() {
        return dataManager.get(TONNAGE_V).floatValue();
    }

    public void assignTonnage(float ton) {
        dataManager.set(TONNAGE_V, Float.valueOf(ton));
    }

    public float fetchTonnageMax() {
        switch (fetchKindSpirit()) {
            case 1:
                return 8.0E15F;
            case 2:
                return 1.2E16F;
            case 3:
                return 1.6E16F;
            case 4:
                return 2.0E18F;
            case 5:
                return 2.0E20F;
            case 6:
                return 2.0E18F;
            case 7:
                return 2.0E18F;
            case 8:
                return 2.0E18F;
            case 9:
                return 2.0E18F;
            case 10:
                return 1.0E20F;
            case 11:
                return 4.0E20F;
            case 12:
                return 3.0E30F;
            case 13:
                return 2.0E30F;
            default:
                return 1.0F;
        }
    }

    public String getName() {
        switch (fetchKindSpirit()) {
            case 1:
                return I18n.translateToLocal("entity.thetitans.SilverfishTitan." + getSubType() + ".name");
            case 2:
                return I18n.translateToLocal("entity.thetitans.CaveSpiderTitan." + getSubType() + ".name");
            case 3:
                return I18n.translateToLocal("entity.thetitans.SpiderTitan." + getSubType() + ".name");
            case 4:
                return I18n.translateToLocal("entity.thetitans.SkeletonTitan." + getSubType() + ".name");
            case 5:
                return I18n.translateToLocal("entity.thetitans.WitherSkeletonTitan." + getSubType() + ".name");
            case 6:
                return I18n.translateToLocal("entity.thetitans.ZombieTitan." + getSubType() + ".name");
            case 7:
                return I18n.translateToLocal("entity.thetitans.CreeperTitan." + getSubType() + ".name");
            case 8:
                return I18n.translateToLocal("entity.thetitans.PigZombieTitan." + getSubType() + ".name");
            case 9:
                return I18n.translateToLocal("entity.thetitans.BlazeTitan." + getSubType() + ".name");
            case 10:
                return I18n.translateToLocal("entity.thetitans.EndermanTitan." + getSubType() + ".name");
            case 11:
                return I18n.translateToLocal("entity.thetitans.GhastTitan." + getSubType() + ".name");
            case 12:
                return "§kRegnator";
            default:
                return I18n.translateToLocal("entity.thetitans.TitanSpirit.name");
        }
    }

    // release the chunk ticket and drop the static map entries when the entity leaves the world (death or
    // unload); previously nothing ever called haltLoading, so every titan that ever woke stayed referenced.
    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!world.isRemote)
            com.electrifiedded.thetitans.handler.ChunkLoadHooks.haltLoading(this);
    }

    @SuppressWarnings("unchecked")
    public void setDead() {
        if (!world.isRemote) {
            switch (fetchKindSpirit()) {
                case 1:
                    world.newExplosion(this, posX, posY, posZ, 18.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntitySilverfishTitan omegafish = new EntitySilverfishTitan(world);
                    omegafish.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    omegafish.assignVariantGiant(getSubType());
                    omegafish.funcM82206();
                    world.spawnEntity(omegafish);
                    break;
                case 2:
                    world.newExplosion(this, posX, posY, posZ, 12.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntityCaveSpiderTitan cavespidertitan = new EntityCaveSpiderTitan(world);
                    cavespidertitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    cavespidertitan.assignVariantGiant(getSubType());
                    cavespidertitan.funcM82206();
                    world.spawnEntity(cavespidertitan);
                    break;
                case 3:
                    world.newExplosion(this, posX, posY, posZ, 12.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntitySpiderTitan spidertitan = new EntitySpiderTitan(world);
                    spidertitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    spidertitan.assignVariantGiant(getSubType());
                    spidertitan.funcM82206();
                    world.spawnEntity(spidertitan);
                    break;
                case 4:
                    world.newExplosion(this, posX, posY, posZ, 18.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntitySkeletonTitan skeletontitan = new EntitySkeletonTitan(world);
                    skeletontitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    skeletontitan.setSkeletonType(0);
                    skeletontitan.assignVariantGiant(getSubType());
                    skeletontitan.funcM82206();
                    world.spawnEntity(skeletontitan);
                    break;
                case 5:
                    world.newExplosion(this, posX, posY, posZ, 18.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntitySkeletonTitan witherskeletontitan = new EntitySkeletonTitan(world);
                    witherskeletontitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    witherskeletontitan.setSkeletonType(1);
                    witherskeletontitan.assignVariantGiant(getSubType());
                    witherskeletontitan.funcM82206();
                    world.spawnEntity(witherskeletontitan);
                    break;
                case 6:
                    world.newExplosion(this, posX, posY, posZ, 18.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntityZombieTitan zombietitan = new EntityZombieTitan(world);
                    zombietitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    zombietitan.onInitialSpawn(world.getDifficultyForLocation(new net.minecraft.util.math.BlockPos(zombietitan)), (IEntityLivingData)null);
                    zombietitan.assignVariantGiant(getSubType());
                    zombietitan.funcM82206();
                    world.spawnEntity(zombietitan);
                    if (mobPrev != null) {
                        zombietitan.setChild(((EntityZombieMinion)mobPrev).isChild());
                        zombietitan.assignVillager(((EntityZombieMinion)mobPrev).villagerIs());
                    }

                    break;
                case 7:
                    world.newExplosion(this, posX, posY, posZ, 18.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntityCreeperTitan creepertitan = new EntityCreeperTitan(world);
                    creepertitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    creepertitan.assignVariantGiant(getSubType());
                    creepertitan.funcM82206();
                    world.spawnEntity(creepertitan);
                    if (mobPrev != null)
                        creepertitan.setPowered(((EntityCreeperMinion)mobPrev).getPowered());
                    break;
                case 8:
                    world.newExplosion(this, posX, posY, posZ, 18.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntityPigZombieTitan pigzombietitan = new EntityPigZombieTitan(world);
                    pigzombietitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    pigzombietitan.assignVariantGiant(getSubType());
                    pigzombietitan.funcM82206();
                    world.spawnEntity(pigzombietitan);
                    if (mobPrev != null)
                        pigzombietitan.setChild(((EntityPigZombieMinion)mobPrev).isChild());
                    break;
                case 9:
                    world.newExplosion(this, posX, posY, posZ, 16.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntityBlazeTitan blazetitan = new EntityBlazeTitan(world);
                    blazetitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    blazetitan.assignVariantGiant(getSubType());
                    blazetitan.funcM82206();
                    world.spawnEntity(blazetitan);
                    break;
                case 10:
                    world.newExplosion(this, posX, posY, posZ, 45.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntityEnderColossus endercolossus = new EntityEnderColossus(world);
                    endercolossus.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    endercolossus.assignVariantGiant(getSubType());
                    endercolossus.funcM82206();
                    world.spawnEntity(endercolossus);
                    break;
                case 11:
                    world.newExplosion(this, posX, posY, posZ, 8.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                    EntityGhastTitan ghasttitan = new EntityGhastTitan(world);
                    ghasttitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    ghasttitan.assignVariantGiant(getSubType());
                    ghasttitan.funcM82206();
                    world.spawnEntity(ghasttitan);
                    break;
                case 12:
                    if ((world.provider instanceof WorldProviderVoid)) {
                        if (mobPrev != null)
                            ((EntityPlayer)mobPrev).sendMessage(new TextComponentString("In the end, you are your own undoing."));
                        // TODO 1.7.10 manual transferEntityToWorld replaced with vanilla changeDimension(0); original re-created entity in overworld
                        changeDimension(0);
                    }

                    else {
                        if (mobPrev != null) {
                            ((EntityPlayer)mobPrev).attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                            ((EntityPlayer)mobPrev).setHealth(0F);
                        }

                        world.newExplosion(this, posX, posY, posZ, 7.0F, true, world.getGameRules().getBoolean("mobGriefing"));
                        EntityWitherzilla witherzilla = new EntityWitherzilla(world);
                        witherzilla.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                        witherzilla.funcM82206();
                        world.spawnEntity(witherzilla);
                        if (mobPrev != null)
                            ((EntityPlayer)mobPrev).sendMessage(new TextComponentString("This universe is flawed, Notch. They've corrupted it. It has to go."));
                    }

                    break;
            }

            ArrayList<?> listp = Lists.newArrayList(world.playerEntities);
            if (fetchKindSpirit() != 12 && (listp != null) && (!listp.isEmpty()) && !world.isRemote) {
                for (int i1 = 0; i1 < listp.size(); i1++) {
                    Entity entity = (Entity)listp.get(i1);
                    if ((entity != null) && ((entity instanceof EntityPlayer))) {
                        playLivingSound();
                        ((EntityPlayer)entity).sendMessage(new TextComponentString(getName() + ": Now to return to where we left off, " + ((EntityPlayer)entity).getName() + "."));
                    }
                }
            }
        }

        super.setDead();
    }

    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote)
            ChunkLoadHooks.refreshLoaded(this);
        switch (fetchKindSpirit()) {
            case 1:
                setSize(8.0F, 8.0F);
                break;
            case 2:
                setSize(8.0F, 8.0F);
                break;
            case 3:
                setSize(8.0F, 8.0F);
                break;
            case 4:
                setSize(12.0F, 12.0F);
                break;
            case 6:
                setSize(12.0F, 12.0F);
                break;
            case 7:
                setSize(12.0F, 12.0F);
                break;
            case 8:
                setSize(12.0F, 12.0F);
                break;
            case 9:
                setSize(12.0F, 12.0F);
                break;
            case 5:setSize(16.0F, 16.0F);
                break;
            case 10:setSize(16.0F, 16.0F);
                break;
            case 11:
                setSize(16.0F, 16.0F);
                break;
            case 12:
                setSize(20.0F, 20.0F);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void onLivingUpdate() {
        if (fetchTonnage() >= fetchTonnageMax())
            setDead();
        if (fetchTonnage() < 0F)
            assignTonnage(0F);
        else
            assignTonnage(fetchTonnage() - 1F);
        List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(32D, 32D, 32D));
        if ((list != null) && (!list.isEmpty())) {
            for (int i = 0; i < list.size(); i++) {
                Entity entity = (Entity)list.get(i);
                if (entity != null && entity.isEntityAlive() && entity instanceof EntityLivingBase && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit) && !(entity instanceof EntityTitanPart)) {
                    if (ticksExisted % 40 == 0)
                        entity.attackEntityFrom(DamageSourceExtra.causeStealingHarmSoul(this), 2F);
                    double speed = entity.isSneaking() ? 0.2D : 0.4D;
                    double mx = posX - entity.posX;
                    double my = (posY + 4D) - (entity.posY + 1D);
                    double mz = posZ - entity.posZ;
                    float f2 = MathHelper.sqrt(mx * mx + my * my + mz * mz);
                    entity.motionX = (mx / f2 * speed * speed + entity.motionX);
                    entity.motionY = (my / f2 * speed * speed + entity.motionY);
                    entity.motionZ = (mz / f2 * speed * speed + entity.motionZ);
                    if (entity instanceof EntityLiving)
                        ((EntityLiving)entity).setAttackTarget(this);
                    short short1 = (short)(int)getDistance(entity);
                    for (int f = 0; f < short1; f++) {
                        double d9 = f / (short1 - 1.0D);
                        double d6 = posX + mx * -d9;
                        double d7 = (posY + 4D) + my * -d9;
                        double d8 = posZ + mz * -d9;
                        world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, d6, d7, d8, entity.motionX, entity.motionY, entity.motionZ);
                    }
                }

                if (entity != null && entity instanceof EntityItem) {
                    entity.hurtResistantTime = 0;
                    double mx = posX - entity.posX;
                    double my = (posY + (height * 0.5D)) - entity.posY;
                    double mz = posZ - entity.posZ;
                    float f2 = MathHelper.sqrt(mx * mx + my * my + mz * mz);
                    entity.motionX = (mx / f2 * 0.3D * 0.3D + entity.motionX);
                    entity.motionY = (my / f2 * 0.3D * 0.3D + entity.motionY);
                    entity.motionZ = (mz / f2 * 0.3D * 0.3D + entity.motionZ);
                }

                if (entity != null && entity instanceof EntityTNTPrimed) {
                    entity.hurtResistantTime = 0;
                    double mx = posX - entity.posX;
                    double my = (posY + (height * 0.5D)) - entity.posY;
                    double mz = posZ - entity.posZ;
                    float f2 = MathHelper.sqrt(mx * mx + my * my + mz * mz);
                    entity.motionX = (mx / f2 * 0.3D * 0.3D + entity.motionX);
                    entity.motionY = (my / f2 * 0.3D * 0.3D + entity.motionY);
                    entity.motionZ = (mz / f2 * 0.3D * 0.3D + entity.motionZ);
                }

                if (entity != null && entity instanceof EntityFallingBlock) {
                    entity.hurtResistantTime = 0;
                    float f6 = (entity.ticksExisted + entity.getEntityId()) * (float)Math.PI * -0.5F;
                    double mx = (posX + (MathHelper.cos(f6) * 16F)) - entity.posX;
                    double my = (posY + (height * 0.5D)) - entity.posY;
                    double mz = (posZ + (MathHelper.sin(f6) * 16F)) - entity.posZ;
                    float f2 = MathHelper.sqrt(mx * mx + my * my + mz * mz);
                    entity.motionX = (mx / f2 * 0.4D * 0.4D + entity.motionX);
                    entity.motionY = (my / f2 * 0.4D * 0.4D + entity.motionY);
                    entity.motionZ = (mz / f2 * 0.4D * 0.4D + entity.motionZ);
                    ArrayList<?> arraylist = new ArrayList<Object>(world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox()));
                    boolean flag = ((EntityFallingBlock)entity).getBlock().getBlock() == Blocks.ANVIL;
                    DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                    Iterator<?> iterator = arraylist.iterator();
                    while (iterator.hasNext()) {
                        Entity mobRef1 = (Entity)iterator.next();
                        mobRef1.attackEntityFrom(damagesource, 20F);
                    }
                }

                if (entity != null && entity instanceof EntityFireball) {
                    entity.hurtResistantTime = 0;
                    double mx = posX - entity.posX;
                    double my = (posY + (height * 0.5D)) - entity.posY;
                    double mz = posZ - entity.posZ;
                    float f2 = MathHelper.sqrt(mx * mx + my * my + mz * mz);
                    entity.motionX = (mx / f2 * 0.3D * 0.3D + entity.motionX);
                    entity.motionY = (my / f2 * 0.3D * 0.3D + entity.motionY);
                    entity.motionZ = (mz / f2 * 0.3D * 0.3D + entity.motionZ);
                }
            }
        }

        if (fetchLabelIDSpirit() <= 0)
            assignLabelIDSpirit(1);
        super.onLivingUpdate();
        refreshActionModeSpirit();
        setHealth(Float.MAX_VALUE);
        deathTime = 0;
        for (int i = 0; i < width + height + 20; i++) {
            float f = (rand.nextFloat() - 0.5F) * width;
            float f1 = (rand.nextFloat() - 0.5F) * height;
            float f2 = (rand.nextFloat() - 0.5F) * width;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + f, posY + 4.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + f, posY + 2.0D + f1, posZ + f2, motionX, motionY, motionZ);
            world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, posX + f, posY + 4.0D + f1, posZ + f2, motionX, motionY, motionZ);
        }

        List<?> theBoundingBox = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
        if ((theBoundingBox != null) && (!theBoundingBox.isEmpty())) {
            for (int i1 = 0; i1 < theBoundingBox.size(); i1++) {
                Entity entity = (Entity)theBoundingBox.get(i1);
                if (entity != null) {
                    if ((fetchKindSpirit() == 1) && ((entity instanceof EntitySilverfishMinion)) && ((EntitySilverfishMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntitySilverfishMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 2) && ((entity instanceof EntityCaveSpiderMinion)) && ((EntityCaveSpiderMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntityCaveSpiderMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 3) && ((entity instanceof EntitySpiderMinion)) && ((EntitySpiderMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntitySpiderMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 4) && ((entity instanceof EntitySkeletonMinion)) && (((EntitySkeletonMinion)entity).fetchKindSkeleton() != 1) && ((EntitySkeletonMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntitySkeletonMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 5) && ((entity instanceof EntitySkeletonMinion)) && (((EntitySkeletonMinion)entity).fetchKindSkeleton() == 1) && ((EntitySkeletonMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntitySkeletonMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 6) && ((entity instanceof EntityZombieMinion)) && ((EntityZombieMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntityZombieMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 7) && ((entity instanceof EntityCreeperMinion)) && ((EntityCreeperMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntityCreeperMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 8) && ((entity instanceof EntityPigZombieMinion)) && ((EntityPigZombieMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntityPigZombieMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 9) && ((entity instanceof EntityBlazeMinion)) && ((EntityBlazeMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntityBlazeMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 10) && ((entity instanceof EntityEndermanMinion)) && ((EntityEndermanMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntityEndermanMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 11) && ((entity instanceof EntityGhastMinion)) && ((EntityGhastMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                        mobPrev = (EntityGhastMinion) entity;
                        setDead();
                    }

                    if ((fetchKindSpirit() == 12) && ((entity instanceof EntityPlayer))) {
                        mobPrev = (EntityPlayer) entity;
                        setDead();
                    }

                    if ((entity instanceof EntityLivingBase)) {
                        ((EntityLivingBase)entity).attackEntityFrom(DamageSourceExtra.causeStealingHarmSoul(this), 100F);
                        ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 300, 3));
                    }

                    // dropped `delayBeforeCanPickup < 0` guard — no public getter in 1.12.2 EntityItem; soul-steals all non-nether-star items
                    if (((entity instanceof EntityItem)) && (((EntityItem)entity).getItem().getItem() != Items.NETHER_STAR)) {
                        ((EntityItem)entity).attackEntityFrom(DamageSourceExtra.causeStealingHarmSoul(this), 100F);
                    }

                    if ((entity instanceof EntityEnderCrystal)) {
                        ((EntityEnderCrystal)entity).attackEntityFrom(DamageSourceExtra.causeStealingHarmSoul(this), 100F);
                    }
                }
            }
        }
    }

    private void refreshActionModeSpirit() {
        if (yWaypoint <= 0D)
            yWaypoint = 0D;
        if (yWaypoint > 400D)
            yWaypoint = 400D;
        if (isHuntingVessel()) {
            EntityPlayer player = world.getClosestPlayerToEntity(this, 256D);
            if (getAttackTarget() != null) {
                xWaypoint = getAttackTarget().posX;
                yWaypoint = getAttackTarget().posY;
                zWaypoint = getAttackTarget().posZ;
                if (!getAttackTarget().isEntityAlive())
                    setAttackTarget(null);
            }

            else if (ticksExisted % 40 == 0 && player != null && posX != xWaypoint && posY != zWaypoint) {
                xWaypoint = (player.posX + (rand.nextFloat() * 2.0F - 1.0F) * 100.0F);
                yWaypoint = (200.0D + (rand.nextFloat() * 2.0F - 1.0F) * 20.0F);
                zWaypoint = (player.posZ + (rand.nextFloat() * 2.0F - 1.0F) * 100.0F);
            }

            else if (player == null && ticksExisted % 40 == 0 && rand.nextInt(5) == 0) {
                xWaypoint = (posX + (rand.nextFloat() * 2.0F - 1.0F) * 200.0F);
                yWaypoint = (200.0D + (rand.nextFloat() * 2.0F - 1.0F) * 20.0F);
                zWaypoint = (posZ + (rand.nextFloat() * 2.0F - 1.0F) * 200.0F);
            }
        }

        double d0 = xWaypoint - posX;
        double d1 = yWaypoint - posY;
        double d2 = zWaypoint - posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        d3 = MathHelper.sqrt(d3);
        if (isHuntingVessel() && getDistanceSq(xWaypoint, yWaypoint, zWaypoint) > 90000D) {
            xWaypoint = posX;
            yWaypoint = posY;
            zWaypoint = posZ;
        }

        if (getDistanceSq(xWaypoint, yWaypoint, zWaypoint) > 4D) {
            motionX += d0 / d3 * 0.15D;
            motionY += d1 / d3 * 0.15D;
            motionZ += d2 / d3 * 0.15D;
        }

        if (fetchKindSpirit() == 12) {
            if (world.provider instanceof WorldProviderVoid)
                assignHuntingVessel(true);
            if (!isHuntingVessel()) {
                xWaypoint = posX;
                yWaypoint = 200D;
                zWaypoint = posZ;
            }
        }

        else if (!isHuntingVessel()) {
            xWaypoint = (posX + (rand.nextFloat() * 2.0F - 1.0F) * 128.0F);
            yWaypoint = 400.0D;
            zWaypoint = (posZ + (rand.nextFloat() * 2.0F - 1.0F) * 128.0F);
            if (posY >= 300.0D) {
                assignHuntingVessel(true);
                if (!world.isRemote)
                    setPosition(posX + (rand.nextFloat() * 2.0F - 1.0F) * 10000.0F, 250.0D, posZ + (rand.nextFloat() * 2.0F - 1.0F) * 10000.0F);
            }
        }

        else {
            List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if (entity != null) {
                        if ((fetchKindSpirit() == 1) && ((entity instanceof EntitySilverfishMinion)) && ((EntitySilverfishMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 2) && ((entity instanceof EntityCaveSpiderMinion)) && ((EntityCaveSpiderMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 3) && ((entity instanceof EntitySpiderMinion)) && ((EntitySpiderMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 4) && ((entity instanceof EntitySkeletonMinion)) && (((EntitySkeletonMinion)entity).fetchKindSkeleton() != 1) && ((EntitySkeletonMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 5) && ((entity instanceof EntitySkeletonMinion)) && (((EntitySkeletonMinion)entity).fetchKindSkeleton() == 1) && ((EntitySkeletonMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 6) && ((entity instanceof EntityZombieMinion)) && ((EntityZombieMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 7) && ((entity instanceof EntityCreeperMinion)) && ((EntityCreeperMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 8) && ((entity instanceof EntityPigZombieMinion)) && ((EntityPigZombieMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 9) && ((entity instanceof EntityBlazeMinion)) && ((EntityBlazeMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 10) && ((entity instanceof EntityEndermanMinion)) && ((EntityEndermanMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 11) && ((entity instanceof EntityGhastMinion)) && ((EntityGhastMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR) {
                            setAttackTarget((EntityLiving) entity);
                            ((EntityLiving)entity).setAttackTarget(this);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }

                        if ((fetchKindSpirit() == 12) && ((entity instanceof EntityPlayer))) {
                            setAttackTarget((EntityLiving) entity);
                            xWaypoint = entity.posX;
                            yWaypoint = entity.posY;
                            zWaypoint = entity.posZ;
                        }
                    }
                }
            }
        }
    }

    protected void despawnEntity() {
    }

    protected void collideWithNearbyEntities() {
        List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(1D, 1D, 1D));
        if ((list != null) && (!list.isEmpty())) {
            for (int i = 0; i < list.size(); i++) {
                Entity entity = (Entity)list.get(i);
                if (entity != null && entity instanceof EntityLivingBase && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit) && !(entity instanceof EntityTitanPart)) {
                    entity.hurtResistantTime = 0;
                    double mx = posX - entity.posX;
                    double my = (posY + 4D) - entity.posY;
                    double mz = posZ - entity.posZ;
                    float f2 = MathHelper.sqrt(mx * mx + my * my + mz * mz);
                    entity.motionX = (mx / f2 * 0.9D * 0.9D + entity.motionX);
                    entity.motionY = (my / f2 * 0.9D * 0.9D + entity.motionY);
                    entity.motionZ = (mz / f2 * 0.9D * 0.9D + entity.motionZ);
                    ((EntityLivingBase)entity).setHealth(0F);
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 100, 3));
                    ((EntityLivingBase)entity).copyLocationAndAnglesFrom(this);
                    if (((EntityLivingBase)entity).deathTime > 0) {
                        world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 1.0D, 0.0D, 0.0D);
                        playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
                        SoundEvent creeperBoom = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanCreeperExplosion");
                        if (creeperBoom != null)
                            playSound(creeperBoom, 10F, getSoundPitch() + 0.5F);
                        playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 10F, getSoundPitch() + 0.75F);
                        ((EntityLivingBase)entity).setDead();
                        assignTonnage(fetchTonnage() + ((EntityLivingBase)entity).getMaxHealth());
                    }
                }

                if (isHuntingVessel() && entity != null && entity instanceof EntityItem) {
                    entity.hurtResistantTime = 0;
                    double mx = posX - entity.posX;
                    double my = (posY + 4D) - entity.posY;
                    double mz = posZ - entity.posZ;
                    float f2 = MathHelper.sqrt(mx * mx + my * my + mz * mz);
                    entity.motionX = (mx / f2 * 0.9D * 0.9D + entity.motionX);
                    entity.motionY = (my / f2 * 0.9D * 0.9D + entity.motionY);
                    entity.motionZ = (mz / f2 * 0.9D * 0.9D + entity.motionZ);
                    entity.attackEntityFrom(DamageSourceExtra.causeStealingHarmSoul(this), 100F);
                }
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBePushed() {
        return false;
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    protected SoundEvent getAmbientSound() {
        String key;
        switch (fetchKindSpirit()) {
            case 1:
                key = "titanSilverfishLiving";
                break;
            case 2:
                key = "titanSpiderLiving";
                break;
            case 3:
                key = "titanSpiderLiving";
                break;
            case 4:
                key = "titanSkeletonLiving";
                break;
            case 5:
                key = "titanWitherSkeletonLiving";
                break;
            case 6:
                key = "titanZombieLiving";
                break;
            case 7:
                key = "titanCreeperLiving";
                break;
            case 8:
                key = "titanPigZombieLiving";
                break;
            case 9:
                key = "titanBlazeBreathe";
                break;
            case 10:
                key = "titanEnderColossusRoar";
                break;
            case 11:
                key = "titanGhastLiving";
                break;
            case 12:
                key = "witherzillaLiving";
                break;
            default:
                return SoundEvents.ENTITY_WITHER_AMBIENT;
        }

        SoundEvent snd = com.electrifiedded.thetitans.registry.TitanAudio.get(key);
        return snd != null ? snd : SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected float getSoundVolume() {
        return 6.0F;
    }

    protected void damageEntity(DamageSource arg70665n1, float arg70665n2) {
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    protected void onDeathUpdate() {
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityLiving)
            setAttackTarget((EntityLiving) source.getTrueSource());
        return false;
    }
}
