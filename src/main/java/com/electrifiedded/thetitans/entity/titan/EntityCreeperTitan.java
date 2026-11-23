package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAICreeperTitanSwell;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanLookIdle;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWander;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWatchClosest;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanAntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanAttack3;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanAttack4;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanCreation;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanDeath;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanSpit;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanStunned;
import com.electrifiedded.thetitans.entity.titan.anim.creepertitan.AnimationCreeperTitanThunderClap;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class EntityCreeperTitan extends EntityTitan implements IAnimatedEntity, ITitanHitbox {
    private static final DataParameter<Byte> MODE_CREEPER = EntityDataManager.<Byte>createKey(EntityCreeperTitan.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> POWERED = EntityDataManager.<Byte>createKey(EntityCreeperTitan.class, DataSerializers.BYTE);
    public int toLegsHarm;
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime = 200;
    public EntityTitanPart head;
    public EntityTitanPart body;
    public EntityTitanPart leg1;
    public EntityTitanPart leg2;
    public EntityTitanPart leg3;
    public EntityTitanPart leg4;

    public EntityCreeperTitan(World worldIn) {
        super(worldIn);
        head = new EntityTitanPart(worldIn, this, "head", 8.0F, 8.0F);
        body = new EntityTitanPart(worldIn, this, "body", 7.0F, 12.0F);
        leg1 = new EntityTitanPart(worldIn, this, "leg1", 4.5F, 8.0F);
        leg2 = new EntityTitanPart(worldIn, this, "leg2", 4.5F, 8.0F);
        leg3 = new EntityTitanPart(worldIn, this, "leg3", 4.5F, 8.0F);
        leg4 = new EntityTitanPart(worldIn, this, "leg4", 4.5F, 8.0F);
        setPiece.add(head);
        setPiece.add(body);
        setPiece.add(leg1);
        setPiece.add(leg2);
        setPiece.add(leg3);
        setPiece.add(leg4);
        setSize(8.0F, 26.0F);
        experienceValue = 50000;
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(MODE_CREEPER, Byte.valueOf((byte)-1));
        this.dataManager.register(POWERED, Byte.valueOf((byte)0));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.dataManager.get(POWERED).byteValue() == 1) {
            tagCompound.setBoolean("Charged", true);
        }

        tagCompound.setInteger("DamageToLegs", toLegsHarm);
        tagCompound.setShort("Fuse", (short)fuseTime);
        tagCompound.setBoolean("Stunned", stunnedIs);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.dataManager.set(POWERED, Byte.valueOf((byte)(tagCompund.getBoolean("Charged") ? 1 : 0)));
        if (tagCompund.hasKey("DamageToLegs", 99)) {
            toLegsHarm = tagCompund.getInteger("DamageToLegs");
        }

        stunnedIs = tagCompund.getBoolean("Stunned");
        if (tagCompund.hasKey("Fuse", 99)) {
            fuseTime = tagCompund.getShort("Fuse");
        }
    }

    protected void applyEntityAI() {
        tasks.addTask(0, new EntityAICreeperTitanSwell(this));
        tasks.addTask(1, new AnimationCreeperTitanCreation(this));
        tasks.addTask(1, new AnimationCreeperTitanDeath(this));
        tasks.addTask(1, new AnimationCreeperTitanStunned(this));
        tasks.addTask(1, new AnimationCreeperTitanThunderClap(this));
        tasks.addTask(1, new AnimationCreeperTitanSpit(this));
        tasks.addTask(1, new AnimationCreeperTitanAttack4(this));
        tasks.addTask(1, new AnimationCreeperTitanAntiTitanAttack(this));
        tasks.addTask(1, new AnimationCreeperTitanAttack2(this));
        tasks.addTask(1, new AnimationCreeperTitanAttack1(this));
        tasks.addTask(1, new AnimationCreeperTitanAttack3(this));
        tasks.addTask(6, new EntityAITitanWander(this, 300));
        tasks.addTask(7, new EntityAITitanWatchClosest(this, EntityTitanSpirit.class, 128.0F));
        tasks.addTask(8, new EntityAITitanWatchClosest(this, EntityTitan.class, 128.0F));
        tasks.addTask(9, new EntityAITitanWatchClosest(this, EntityPlayer.class, 64.0F));
        tasks.addTask(10, new EntityAITitanLookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterCreeper));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    public double fetchAmountHp() {
        switch (fetchVariantGiant()) {
            case 1:
                return super.fetchAmountHp() * 25.0D;
            case 2:
                return super.fetchAmountHp() * 15.0D;
            case 3:
                return super.fetchAmountHp() * 30.0D;
            case 4:
                return super.fetchAmountHp() * 40.0D;
            default:
                return super.fetchAmountHp() * 20.0D;
        }
    }

    @Override
    public String getName() {
        return I18n.translateToLocal("entity.thetitans.CreeperTitan." + fetchVariantGiant() + ".name");
    }

    public int fetchTintLabel() {
        int ran = rand.nextInt(3);
        if (getPowered()) {
            switch (fetchVariantGiant()) {
                case 1:
                    return ran == 2 ? 16729149 : (ran == 1 ? 14560808 : 11736850);
                case 2:
                    return ran == 2 ? 13631487 : (ran == 1 ? 14942207 : 16777215);
                case 3:
                    return ran == 2 ? 8716438 : (ran == 1 ? 10944701 : 12976352);
                case 4:
                    return ran == 2 ? 2757422 : (ran == 1 ? 3414328 : 3612219);
                default:
                    return ran == 2 ? 681970 : (ran == 1 ? 695794 : 712944);
            }
        }

        else {
            switch (fetchVariantGiant()) {
                case 1:
                    return 5988449;
                case 2:
                    return 16777215;
                case 3:
                    return 15760127;
                case 4:
                    return 3612219;
                default:
                    return 6946631;
            }
        }
    }

    public void onLivingUpdate() {
        giantMelee = true;
        if (motionY > 3.0D)motionY *= 0.8D;
        if (!isRiding() && !fetchWaiting() && !stunnedIs && getAnimID() == 0) {
            flyTitan(0.625F * fetchScaleMultiplierGiant(), 0.125F * fetchScaleMultiplierGiant(), 0.25D * fetchScaleMultiplierGiant());
        }

        if (fetchWaiting()) {
            AnimationAPI.sendAnimPacket(this, 13);
            if (allowAwaken()) {
                awaken();
            }
        }

        else {
            if (getAnimID() == 13) {
                motionX = 0D;
                motionZ = 0D;
                if (motionY > 0D)
                    motionY = 0D;
            }

            if (getAnimID() == 13 && getAnimTick() == 1)
                triggerAudioGiant("thetitans:titanBirth", 1000.0F, 1.0F);
            if (getAnimID() == 13 && getAnimTick() == 10)
                triggerAudioGiant("thetitans:titanCreeperAwaken", getSoundVolume(), 1F);
            if (getAnimID() == 13 && getAnimTick() == 130)
                triggerAudioGiant("thetitans:titanRumble", getSoundVolume(), 1F);
            if (getAnimID() == 13 && getAnimTick() == 160)
                triggerAudioGiant("thetitans:titanCreeperBeginMove", getSoundVolume(), 1F);
            if (getAnimID() == 13 && (getAnimTick() == 260 || getAnimTick() == 261 || getAnimTick() == 390 || getAnimTick() == 410)) {
                performStepGiant();
                triggerAudioGiant("thetitans:titanPress", getSoundVolume(), 1F);
            }

            if (getAnimID() != 13 && (getPowered()) && (!stunnedIs)) {
                WorldInfo worldinfo = fetchDetailsOverworld();
                if (worldinfo != null) {
                    worldinfo.setRainTime(9999999);
                    worldinfo.setThunderTime(1000000);
                    worldinfo.setRaining(true);
                    worldinfo.setThundering(true);
                }
                if (rand.nextInt(20) == 0) {
                    EntityGammaLightning entitylightning = new EntityGammaLightning(world, posX, head.posY + 4D, posZ, 0F, 0.5F, 1.0F);
                    world.addWeatherEffect(entitylightning);
                }

                if (rand.nextInt(40) == 0) {
                    for (int l = 0; l < 50; l++) {
                        int i = MathHelper.floor(posX);
                        int j = MathHelper.floor(posY);
                        int k = MathHelper.floor(posZ);
                        int i1 = i + MathHelper.getInt(rand, 10, 100) * MathHelper.getInt(rand, -1, 1);
                        int j1 = j + MathHelper.getInt(rand, 10, 100) * MathHelper.getInt(rand, -1, 1);
                        int k1 = k + MathHelper.getInt(rand, 10, 100) * MathHelper.getInt(rand, -1, 1);
                        EntityGammaLightning entitylightning = new EntityGammaLightning(world, i1, j1, k1, 0F, 0.5F, 1.0F);
                        if ((world.isSideSolid(new net.minecraft.util.math.BlockPos(i1, j1 - 1, k1), net.minecraft.util.EnumFacing.UP)) && (world.checkNoEntityCollision(entitylightning.getEntityBoundingBox())) && (world.getCollisionBoxes(entitylightning, entitylightning.getEntityBoundingBox()).isEmpty()) && (!world.containsAnyLiquid(entitylightning.getEntityBoundingBox()))) {
                            world.addWeatherEffect(new EntityGammaLightning(world, i1, j1, k1, 0F, 0.5F, 1.0F));
                        }
                    }
                }
            }
        }

        if (stunnedIs) {
            setAttackTarget(null);
            AnimationAPI.sendAnimPacket(this, 8);
        }

        if (getAnimID() == 10) {
            if ((getAnimTick() == 50) || (getAnimTick() == 70) || (getAnimTick() == 100)) {
                performStepGiant();
            }

            if (getAnimTick() == 120) {
                shakeUserCamerasNearby(10D);
                triggerAudioGiant("thetitans:titanFall", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:groundSmash", 20.0F, 1.0F);
            }

            if (getAnimTick() == 160) {
                shakeUserCamerasNearby(10D);
                triggerAudioGiant("thetitans:titanFall", 20.0F, 1.0F);
            }
        }

        if (getAnimID() == 8) {
            if (getAnimTick() == 120) {
                shakeUserCamerasNearby(10D);
                triggerAudioGiant("thetitans:groundSmash", 8.0F, 0.9F);
                triggerAudioGiant("thetitans:titanFall", 10.0F, 1.0F);
            }

            if (getAnimTick() == 20) {
                triggerAudioGiant("thetitans:titanCreeperStun", getSoundVolume(), getSoundPitch());
            }

            if (getAnimTick() > 500)
                stunnedIs = false;
            else
                stunnedIs = true;
            setAttackTarget(null);
        }

        if (stunnedIs || deathTicks > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (!stunnedIs) && (getAnimID() == 0)) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                switch (rand.nextInt(4)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 3);
                        setAnimID(3);
                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 2);
                        setAnimID(2);
                        break;
                    case 2:AnimationAPI.sendAnimPacket(this, 5);
                        setAnimID(5);
                        break;
                    case 3:AnimationAPI.sendAnimPacket(this, 4);
                        setAnimID(4);
                        break;
                    case 4:if (getAttackTarget().height >= height - 12F || (posY + getEyeHeight() < getAttackTarget().posY + getAttackTarget().getEyeHeight())) {
                            AnimationAPI.sendAnimPacket(this, 1);
                            setAnimID(1);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 3);
                            setAnimID(3);
                        }

                        break;
                }
            }

            else if (getAnimID() == 0 && (getRNG().nextInt(100) == 0)) {
                switch (rand.nextInt(3)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 7);
                        setAnimID(7);
                        break;
                    default:AnimationAPI.sendAnimPacket(this, 6);
                        setAnimID(6);
                }
            }
        }

        if (getAnimID() == 6) {
            if ((getAnimTick() <= 50) && (getAnimTick() >= 20) && (getAttackTarget() != null)) {
                int it = getPowered() ? (5 + rand.nextInt(5)) : (2 + rand.nextInt(2));
                for (int i = 0; i < it; i++) {
                    switch (rand.nextInt(3)) {
                        case 0:Vec3d vec3 = getLook(1F);
                            double d5 = getAttackTarget().posX + ((getRNG().nextFloat() * 2.0F - 1.0F) * 4.0F) - head.posX + vec3.x * (0.5D * fetchScaleMultiplierGiant());
                            double d6 = getAttackTarget().posY - 16 + ((getRNG().nextFloat() * 2.0F - 1.0F) * 4.0F) - head.posY;
                            double d7 = getAttackTarget().posZ + ((getRNG().nextFloat() * 2.0F - 1.0F) * 4.0F) - head.posZ + vec3.z * (0.5D * fetchScaleMultiplierGiant());
                            EntityTitanFireball entitylargefireball = new EntityTitanFireball(world, this, d5, d6, d7, 1);
                            entitylargefireball.posX = (head.posX + vec3.x * (0.5D * fetchScaleMultiplierGiant()));
                            entitylargefireball.posY = (head.posY);
                            entitylargefireball.posZ = (head.posZ + vec3.z * (0.5D * fetchScaleMultiplierGiant()));
                            world.spawnEntity(entitylargefireball);
                            entitylargefireball.assignIDFireball(1);
                            triggerAudioGiant("thetitans:titanGhastFireball", 100F, 1.25F);
                            break;
                        case 1:if (!world.isRemote) {
                                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, getAttackTarget().posX + 0.5D + (getRNG().nextFloat() * 2.0F - 1.0F) * 32.0F, getAttackTarget().posY + 32.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 32.0F, getAttackTarget().posZ + 0.5D + (getRNG().nextFloat() * 2.0F - 1.0F) * 32.0F, this);
                                world.spawnEntity(entitytntprimed);
                                triggerAudioGiant("game.tnt.primed", 1.0F, 1.0F);
                                entitytntprimed.setFuse(100 + rand.nextInt(60));
                            }

                            break;
                    }
                }
            }
        }

        List<?> list = (ticksExisted % 20 == 0 && getPowered()) ? world.getEntitiesWithinAABB(EntityCreeperMinion.class, getEntityBoundingBox().grow(100.0D, 100.0D, 100.0D)) : java.util.Collections.emptyList();
        if (!list.isEmpty()) {
            for (int i1 = 0; i1 < list.size(); i1++) {
                Entity entity = (Entity)list.get(i1);
                if ((entity != null) && (getPowered()) && ((entity instanceof EntityCreeperMinion))) {
                    if (!((EntityCreeperMinion)entity).getPowered()) {
                        world.addWeatherEffect(new EntityLightningBolt(world, entity.posX, entity.posY + entity.height, entity.posZ, false));
                    }

                    else {
                        ((EntityCreeperMinion)entity).heal(5.0F);
                    }
                }
            }
        }

        if ((getAttackTarget() != null) && ((getAttackTarget() instanceof EntityPlayer)) && (((EntityPlayer)getAttackTarget()).getName().equals("Boom337317"))) {
            setAttackTarget(null);
        }

        if ((getAttackTarget() != null) && (onGround) && (!stunnedIs) && getAnimID() == 0) {
            int in;
            if (getPowered())
                in = 50;
            else in = 600;
            if (rand.nextInt(in) == 0) {
                if (rand.nextInt(4) == 0) {
                    jump();
                    double d01 = getAttackTarget().posX - posX;
                    double d11 = getAttackTarget().posZ - posZ;
                    float f21 = MathHelper.sqrt(d01 * d01 + d11 * d11);
                    double hor = f21 / 16.0F;
                    double ver = 2.0D;
                    motionX = (d01 / f21 * hor * hor + motionX * hor);
                    motionZ = (d11 / f21 * hor * hor + motionZ * hor);
                    motionY = ver;
                }

                else
                    jumpMobAt(getAttackTarget());
            }
        }

        if (getAttackTarget() != null && !(getAttackTarget() instanceof EntityTitan) && (((rand.nextInt(30) == 0 && getPowered()) || (rand.nextInt(150) == 0 && !getPowered())) && (!stunnedIs))) {
            performStrikeToMobLightning(this);
            performStrikeToMobLightning(getAttackTarget());
        }

        super.onLivingUpdate();
    }

    /** Returns the overworld's WorldInfo (rain/thunder controls) or null server-side isn't available. */
    private WorldInfo fetchDetailsOverworld() {
        net.minecraft.server.MinecraftServer server = world.getMinecraftServer();
        if (server == null)
            return null;
        net.minecraft.world.WorldServer overworld = server.getWorld(0);
        return overworld != null ? overworld.getWorldInfo() : null;
    }

    @SuppressWarnings("unchecked")
    protected void whenDeathUpdateGiant() {
        if (timeSinceIgnited >= fuseTime) {
            deathTicks = 200;
            setDead();
        }

        else {
            if (getHealth() <= 0D) {
                ++deathTicks;
                AnimationAPI.sendAnimPacket(this, 10);
                setAnimID(10);
                setHealth(0.0F);
            }

            else {
                attackEntityFrom(DamageSource.OUT_OF_WORLD, 25F);
                deathTicks = 0;
                if (getAnimID() == 10)
                    setAnimID(0);
            }

            motionX *= 0.0D;
            motionZ *= 0.0D;
            setAttackTarget(null);
            if ((deathTicks == 1) && (!world.isRemote)) {
                triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
                ArrayList<?> listp = Lists.newArrayList(world.playerEntities);
                if ((listp != null) && (!listp.isEmpty())) {
                    for (int i1 = 0; i1 < listp.size(); i1++) {
                        Entity entity = (Entity)listp.get(i1);
                        if ((entity != null) && ((entity instanceof EntityPlayer)) && fetchAchievement() != null) {
                            { /* advancement runGrant removed (1.12.2) */ }
                        }
                    }
                }
            }

            if (deathTicks == 1) {
                setAnimTick(1);
            }

            if (deathTicks < 300)
                timeSinceIgnited = 0;
            if (deathTicks >= 300) {
                if (timeSinceIgnited == 1)
                    triggerAudioGiant("thetitans:titanCreeperWarning", Float.MAX_VALUE, 1.0F);
                ++timeSinceIgnited;
                setCreeperState(1);
                float f = (rand.nextFloat() - 0.5F) * 16.0F;
                float f1 = (rand.nextFloat() - 0.5F) * 12.0F;
                float f2 = (rand.nextFloat() - 0.5F) * 16.0F;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
            }

            if (timeSinceIgnited >= fuseTime) {
                deathTicks = 200;
                setDead();
            }
        }
    }

    protected void whenUpdateBounds() {
        if (ticksExisted > 5) {
            setSize(0.5F * fetchScaleMultiplierGiant(), 1.625F * fetchScaleMultiplierGiant());
            head.height = head.width = 0.5F * fetchScaleMultiplierGiant();
            body.height = 0.75F * fetchScaleMultiplierGiant();
            body.width = 0.5F * fetchScaleMultiplierGiant();
            leg1.height = leg2.height = leg3.height = leg4.height = 0.5F * fetchScaleMultiplierGiant();
            leg1.width = leg2.width = leg3.width = leg4.width = 0.28125F * fetchScaleMultiplierGiant();
            float f = renderYawOffset * 3.1415927F / 180.0F;
            float f1 = MathHelper.sin(f);
            float f2 = MathHelper.cos(f);
            float dis = (0.34375F * fetchScaleMultiplierGiant());
            float offset = ((getAnimID() == 3 && getAnimTick() > 30 && getAnimTick() < 70) || (getAnimID() == 7 && getAnimTick() > 30 && getAnimTick() < 130)) ? (0.375F * fetchScaleMultiplierGiant()) : 0F;
            head.setLocationAndAngles(posX, posY + (getAnimID() == 8 ? (0.75F * fetchScaleMultiplierGiant()) : (1.125F * fetchScaleMultiplierGiant())), posZ, 0.0F, 0.0F);
            body.setLocationAndAngles(posX, posY + (getAnimID() == 8 ? 0.0D : (0.375F * fetchScaleMultiplierGiant())), posZ, 0.0F, 0.0F);
            leg1.setLocationAndAngles(posX - (f1 * dis) + (f2 * dis), posY + offset, posZ + (f2 * dis) + (f1 * dis), 0.0F, 0.0F);
            leg2.setLocationAndAngles(posX - (f1 * dis) - (f2 * dis), posY + offset, posZ + (f2 * dis) - (f1 * dis), 0.0F, 0.0F);
            leg3.setLocationAndAngles(posX + (f1 * dis) + (f2 * dis), posY, posZ - (f2 * dis) + (f1 * dis), 0.0F, 0.0F);
            leg4.setLocationAndAngles(posX + (f1 * dis) - (f2 * dis), posY, posZ - (f2 * dis) - (f1 * dis), 0.0F, 0.0F);
        }

        super.whenUpdateBounds();
    }

    protected void whenUpdateAnimation() {}

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != head.getClass()) && (arg70686n1 != body.getClass()) && (arg70686n1 != leg1.getClass()) && (arg70686n1 != leg2.getClass()) && (arg70686n1 != leg3.getClass()) && (arg70686n1 != leg4.getClass()) && (arg70686n1 != EntityCreeperMinion.class) && (arg70686n1 != EntityCreeperTitan.class);
    }

    public boolean isArmored() {
        return getPowered();
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(250) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (isValidLightLevel());
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.giantServantSpawnrateCreeper;
    }

    public int getParticleCount() {
        if (getPowered()) {
            return 28;
        }

        return super.getParticleCount();
    }

    public boolean allowHurtByUserBe() {
        return (stunnedIs) && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    public float getEyeHeight() {
        return 0.9076923076923077F * height;
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.AVERAGE;
    }

    public void fall(float arg70069n1, float damageMultiplier) {
        onGround = true;
        isAirBorne = false;
        float[] fallRet = ForgeHooks.onLivingFall(this, arg70069n1, damageMultiplier);
        if (fallRet == null) return;
        arg70069n1 = fallRet[0];
        if (arg70069n1 <= 0.0F) return;
        PotionEffect potioneffect = getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f1 = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0.0F;
        int i = MathHelper.ceil(arg70069n1 - 32F - f1);
        triggerAudioGiant("thetitans:titanSlam", 1F + i, 1.0F);
        if (i > 0) {
            shakeUserCamerasNearby(i);
            triggerAudioGiant("thetitans:groundSmash", 20.0F, 1.0F);
            triggerAudioGiant("thetitans:titanland", 10000.0F, 1.0F);
            triggerAudioGiant("thetitans:titanStrike", 10.0F, 1.0F);
            wreckInAABBToplessBlocks(getEntityBoundingBox().grow(24D, 1D, 24D));
            collideWithEntities(body, world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(24D, 4D, 24D)));
            List<?> found11 = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(48D, 4D, 48D), e -> e != this);
            if ((found11 != null) && (!found11.isEmpty())) {
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (((entity instanceof EntityLivingBase)) && allowStrikeGiant(entity.getClass()) && (!(entity instanceof EntityTitan))) {
                        float smash = 50F - getDistance(entity);
                        if (smash <= 1F)
                            smash = 1F;
                        entity.attackEntityFrom(DamageSource.causeMobDamage(this), smash);
                        double d0 = getEntityBoundingBox().minX + getEntityBoundingBox().maxX;
                        double d1 = getEntityBoundingBox().minZ + getEntityBoundingBox().maxZ;
                        double d2 = entity.posX - d0;
                        double d3 = entity.posZ - d1;
                        double d4 = d2 * d2 + d3 * d3;
                        entity.addVelocity(d2 / d4 * 8.0D, 1.0D, d3 / d4 * 8.0D);
                        if ((!world.isRemote) && (isEntityAlive()) && (!stunnedIs)) {
                            collideWithEntities(body, world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(16.0D, 1.0D, 16.0D)));
                        }
                    }
                }
            }
        }

        if (getCreeperState() > 0) {
            timeSinceIgnited += 20;
        }
    }

    public double getSpeed() {
        return (getPowered() ? 0.375D : 0.25D) / 6;
    }

    public void strikeMobChoosen(Entity damagedEntity, float damage, int knockbackAmount) {
        super.strikeMobChoosen(damagedEntity, damage, knockbackAmount);
        if ((getPowered()) && ((damagedEntity instanceof EntityLivingBase)) && isEntityAlive()) {
            triggerAudioGiant("ambient.weather.thunder", 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
            triggerAudioGiant("ambient.weather.thunder", 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
            world.newExplosion(this, damagedEntity.posX, damagedEntity.posY + damagedEntity.getEyeHeight(), damagedEntity.posZ, 6.0F, false, false);
            world.newExplosion(this, posX, head.posY, posZ, 2.0F, false, false);
            damagedEntity.attackEntityFrom(DamageSourceExtra.lightningBolt, 49.0F);
            world.addWeatherEffect(new EntityGammaLightning(world, posX, posY + getEyeHeight(), posZ, 0F, 0.5F, 1.0F));
            world.addWeatherEffect(new EntityGammaLightning(world, damagedEntity.posX, damagedEntity.posY, damagedEntity.posZ, 0F, 0.5F, 1.0F));
        }
    }

    public boolean needsMove() {
        return !stunnedIs &&super.needsMove();
    }

    public void performStrikeToMobLightning(Entity arg70652n1) {
        if (getPowered()) {
            triggerAudioGiant("ambient.weather.thunder", 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
            triggerAudioGiant("ambient.weather.thunder", 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
            world.newExplosion(this, arg70652n1.posX, arg70652n1.posY + arg70652n1.getEyeHeight(), arg70652n1.posZ, 8.0F, false, false);
            world.newExplosion(this, posX, head.posY + 4D, posZ, 4.0F, false, false);
            arg70652n1.attackEntityFrom(DamageSourceExtra.lightningBolt, 49.0F);
            float f = (float)fetchAmountStrike(5.0F);
            int i = fetchAmountKnockback();
            if (arg70652n1 != this)
                strikeMobChoosen(arg70652n1, f, i);
            world.addWeatherEffect(new EntityGammaLightning(world, posX, head.posY + 4D, posZ, 0F, 0.5F, 1.0F));
            world.addWeatherEffect(new EntityGammaLightning(world, arg70652n1.posX, arg70652n1.posY, arg70652n1.posZ, 0F, 0.5F, 1.0F));
        }

        else if (!getPowered()) {
            world.newExplosion(this, arg70652n1.posX, arg70652n1.posY + arg70652n1.getEyeHeight(), arg70652n1.posZ, 4.0F, false, false);
            world.newExplosion(this, posX, head.posY + 4D, posZ, 2.0F, false, false);
            arg70652n1.attackEntityFrom(DamageSourceExtra.lightningBolt, 49.0F);
            float f = (float)fetchAmountStrike(2.0F);
            int i = fetchAmountKnockback();
            if (arg70652n1 != this)
                strikeMobChoosen(arg70652n1, f, i);
            world.addWeatherEffect(new EntityGammaLightning(world, posX, head.posY + 4D, posZ, 0F, 0.5F, 0.25F));
            world.addWeatherEffect(new EntityGammaLightning(world, arg70652n1.posX, arg70652n1.posY, arg70652n1.posZ, 0F, 0.5F, 0.25F));
        }
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        return false;
    }

    public double fetchReachMelee() {
        return width + (1.25D * fetchScaleMultiplierGiant());
    }

    protected String fetchLivingAudioGiant() {
        return stunnedIs || fetchWaiting() || getAnimID() == 13 ? null : "thetitans:titanCreeperLiving";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:titanCreeperGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:titanCreeperDeath";
    }

    /** Titan footstep: destroy blocks under legs, shake cameras, play step sounds. */
    protected void performStepGiant() {
        triggerAudioGiant("thetitans:titanStep", 10.0F, 1.1F);
        triggerAudioGiant("thetitans:titanStep", 10.0F, 1.1F);
        shakeUserCamerasNearby(4D);
        shakeUserCamerasNearby(4D);
        if (!fetchWaiting() && getAnimID() != 13) {
            if (keyFoot == 0) {
                destroyBlocksInAABB(leg1.getEntityBoundingBox().offset(0, -1, 0));
                destroyBlocksInAABB(leg4.getEntityBoundingBox().offset(0, -1, 0));
                ++keyFoot;
            }

            else {
                destroyBlocksInAABB(leg2.getEntityBoundingBox().offset(0, -1, 0));
                destroyBlocksInAABB(leg3.getEntityBoundingBox().offset(0, -1, 0));
                keyFoot = 0;
            }
        }
    }

    protected void playStepSound(net.minecraft.util.math.BlockPos pos, Block blockIn) {
        performStepGiant();
    }

    public String fetchAchievement() {
        return null;
    }

    public void setPowered(boolean powered) {
        this.dataManager.set(POWERED, Byte.valueOf(powered ? (byte)1 : (byte)0));
    }

    public boolean getPowered() {
        return this.dataManager.get(POWERED).byteValue() == 1;
    }

    @SideOnly(Side.CLIENT)
    public float getCreeperFlashIntensity(float arg70831n1) {
        return (lastActiveTime + (timeSinceIgnited - lastActiveTime) * arg70831n1) / (fuseTime - 2);
    }

    protected Item getDropItem() {
        return Items.GUNPOWDER;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < (getPowered() ? 60 : 16); x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(16000);
                world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GUNPOWDER));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.TNT));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.COAL));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.EMERALD));
                itembomb.assignTotalStack(8 + rand.nextInt(8 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.DIAMOND));
                itembomb.assignTotalStack(8 + rand.nextInt(8 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.IRON_INGOT));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanGear.harcadium));
                itembomb.assignTotalStack(4 + rand.nextInt(4 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            if (rand.nextInt(10) == 0)
                for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.BEDROCK));
                itembomb.assignTotalStack(1);
                world.spawnEntity(itembomb);
            }
        }
    }

    public int getCreeperState() {
        return this.dataManager.get(MODE_CREEPER).byteValue();
    }

    public void setCreeperState(int arg70829n1) {
        this.dataManager.set(MODE_CREEPER, Byte.valueOf((byte)arg70829n1));
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        IEntityLivingData p_180482_2_1 = super.onInitialSpawn(difficulty, arg180482n2);
        assignWaiting(true);
        if (rand.nextInt(100) == 0) {
            setPowered(true);
        }

        if (world.provider instanceof net.minecraft.world.WorldProviderEnd)
            assignVariantGiant(3);
        if (world.provider instanceof com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere || world.provider instanceof com.electrifiedded.thetitans.world.dimension.WorldProviderVoid)
            assignVariantGiant(4);
        return p_180482_2_1;
    }

    /** Block-wrecking radius of the death blast (the original wrecked a cube of this half-size in one tick). */
    protected double blastRadius() {
        return getPowered() ? (16D * fetchScaleMultiplierGiant()) : (8D * fetchScaleMultiplierGiant());
    }

    /**
     * PERF — the death blast used to wreck the whole cube (up to 1536^2 x 256 cells) in a single tick: a server
     * freeze of minutes. Now ArchaicLib's ChunkBlast carves the sphere as a shock wave under a per-tick budget.
     */
    protected com.electrifiedded.archaiclib.world.ChunkBlast startBlast(double radius) {
        if (!(world instanceof net.minecraft.world.WorldServer))
            return null;
        com.electrifiedded.archaiclib.world.ChunkBlast blast = com.electrifiedded.archaiclib.world.ChunkBlast.of((net.minecraft.world.WorldServer)world, posX, posY + height * 0.25D, posZ, radius);
        final long t0 = System.currentTimeMillis();
        blast.onDone = () -> TheTitans.info(getName() + " blast r=" + (int)radius + ": " + blast.chunksCarved() + " chunks carved in " + (System.currentTimeMillis() - t0) / 1000.0D + " s");
        return blast.start();
    }

    protected void explode() {
        for (int i = 0; i < 1000; i++) {
            float f = (rand.nextFloat() - 0.5F) * 16F;
            float f1 = (rand.nextFloat() - 0.5F) * 48F;
            float f2 = (rand.nextFloat() - 0.5F) * 16F;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + f1, posZ + f2, (rand.nextDouble() - 0.5D), 0.0D, (rand.nextDouble() - 0.5D));
        }

        triggerAudioGiant("thetitans:titanCreeperExplosion", Float.MAX_VALUE, 1.0F);
        triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
        world.getGameRules().getBoolean("mobGriefing");
        double d1 = Math.min(blastRadius(), getPowered() ? (16D * fetchScaleMultiplierGiant()) : (8D * fetchScaleMultiplierGiant()));
        double d2 = getPowered() ? (64D * fetchScaleMultiplierGiant()) : (32D * fetchScaleMultiplierGiant());
        startBlast(blastRadius());
        triggerAudioGiant("thetitans:titanland", 10000.0F, 1.0F);
        for (int da = 0; da <= 10; da++) {
            List<?> list = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(d1, d1, d1), e -> e != this);
            if ((list != null) && (!list.isEmpty()))
                for (int i = 0; i < list.size(); i++) {
                Entity entity = (Entity)list.get(i);
                if (entity != null && (entity instanceof EntityLivingBase)) {
                    if (entity instanceof EntityZombieTitan)
                        if (((EntityZombieTitan)entity).fetchVariantGiant() == 0)
                        ((EntityZombieTitan)entity).assignVariantGiant(3);
                    strikeMobChoosen(entity, (float)fetchAmountStrike(50), 2);
                    entity.attackEntityFrom(DamageSourceExtra.causeGiantExplosionHarmCreeper(this), (float)fetchAmountStrike(1));
                    entity.attackEntityFrom(new DamageSource("blindness").setDamageBypassesArmor().setDamageIsAbsolute(), (float)fetchAmountStrike(1));
                    if (entity instanceof EntityCreeperTitan) {
                        ((EntityCreeperTitan)entity).heal(getPowered() ? 1000F : 500F);
                        ((EntityCreeperTitan)entity).setPowered(true);
                    }
                }
            }
        }

        List<?> found111 = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(d2, d2, d2), e -> e != this);
        if ((found111 != null) && (!found111.isEmpty())) {
            for (int i = 0; i < found111.size(); i++) {
                Entity entity = (Entity)found111.get(i);
                if (world.isRemote && (entity instanceof EntityLivingBase)) {
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ClientProxy.giantRadiationCreeper, 30000, 1));
                    if (getPowered())
                        ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, 30000, 3));
                }
            }
        }
    }

    public void funcCo175493() {
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof EntityPlayer && !world.isRemote) {
            setAttackTarget((EntityLivingBase) source.getTrueSource());
            setRevengeTarget((EntityLivingBase) source.getTrueSource());
        }

        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (source.getDamageType() == "lightningBolt" || source.getDamageType() == "explosion" || ((source.getTrueSource() instanceof EntityCreeperMinion)) || ((source.getTrueSource() instanceof EntityCreeperTitan)) || ((source.isExplosion()) && (!(source.getTrueSource() instanceof EntityWitherTurret)))) {
            heal(amount);
            return false;
        }

        else {
            recentlyHit = 200;
            Entity entity = source.getTrueSource();
            if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source)) && (amount > 25.0F)) {
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntityCreeperTitan)) {
                        EntityCreeperTitan entitypigzombie = (EntityCreeperTitan)mobRef1;
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

    public boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource source, float amount) {
        funcE82195(source, amount);
        if ((source == DamageSourceExtra.lightningBolt) || ((source.getTrueSource() instanceof EntityCreeperMinion)) || ((source.getTrueSource() instanceof EntityCreeperTitan)) || ((source.isExplosion()) && (!(source.getTrueSource() instanceof EntityWitherTurret)))) {
            heal(amount);
            return false;
        }

        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayer && source.canHarmInCreative() && (toLegsHarm < 32) && (!stunnedIs) && ((arg70965n1 == leg1) || (arg70965n1 == leg2) || (arg70965n1 == leg3) || (arg70965n1 == leg4))) {
            toLegsHarm += 1;
            setAttackTarget((EntityLivingBase) source.getTrueSource());
            setRevengeTarget((EntityLivingBase) source.getTrueSource());
            if (toLegsHarm >= 32) {
                triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
                stunnedIs = true;
                toLegsHarm = 0;
            }
        }

        return true;
    }

    protected boolean funcE82195(DamageSource arg82195n1, float arg82195n2) {
        return attackEntityFrom(arg82195n1, arg82195n2);
    }

    public World funcD82194() {
        return world;
    }

    protected void inactActionDeath() {
        if (!world.isRemote) {
            explode();
            triggerAudioGiant("mob.creeper.death", getSoundVolume(), getSoundPitch());
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                if (getPowered())
                    dropFewItems(true, 0);
                dropEquipment(true, 0);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(false);
            entitytitan.assignKindSpirit(7);
        }
    }

    protected EntityLiving fetchMinion() {
        return new EntityCreeperMinion(world);
    }

    protected double cap() {
        return super.cap();
    }

    protected double fetchRegen() {
        return getPowered() ? super.fetchRegen() * 2 : super.fetchRegen();
    }
}
