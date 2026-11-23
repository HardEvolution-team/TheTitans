package com.electrifiedded.thetitans.entity.titan.mutant;

import java.util.List;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.compat.mutant.MutantTitans;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.util.EnumParticleTypes;

/**
 * Mutant Skeleton Titan: the Skeleton Titan on the Mutant Skeleton rig (with an extra spine segment, a tail,
 * horns and a rib-bone crossbow) plus the mutant's rib constriction, leaping multi-shot and charged piercing bolt.
 */
@SuppressWarnings("deprecation")
public class EntityMutantSkeletonTitan extends EntitySkeletonTitan implements AIMutantTitanAnim.Host {
    /** Mutant Skeleton 1.2x3.6 vs Skeleton 0.6x1.99. */
    public static final float WIDTH_SCALE = 1.2F / 0.6F, HEIGHT_SCALE = 3.6F / 1.99F;

    public static final int ANIM_CONSTRICT = 20, ANIM_MULTISHOT = 21, ANIM_PIERCE = 22, ANIM_VOLLEY = 23;
    /** Giga volley: first shot, interval, shots (each one a fan of 3 giant arrows), duration. */
    public static final int VOLLEY_START = 26, VOLLEY_RATE = 8, VOLLEY_SHOTS = 7, VOLLEY_DURATION = 26 + 8 * 7 + 20;
    public static final int CONSTRICT_GRAB = 18, CONSTRICT_RELEASE = 64;
    /** Death: the body cracks open for this many ticks, then falls apart into its bones. */
    public static final int DEATH_COLLAPSE = 24;

    private int caughtId = -1;

    public EntityMutantSkeletonTitan(World worldIn) {
        super(worldIn);
        experienceValue = 40000;
    }

    public static double hpRatio() {
        return MutantTitans.ratio("mutantSkeletonMaxHealth", 150.0D, 20.0D);
    }

    public static double damageRatio() {
        // melee 4 vs 2, arrows 12 vs ~4: the mutant hits roughly 2-3x harder
        return (MutantTitans.ratio("mutantSkeletonAttackDamage", 4.0D, 2.0D) + MutantTitans.ratio("mutantSkeletonArrowDamage", 12.0D, 4.0D)) * 0.5D;
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * hpRatio();
    }

    @Override
    public double fetchAmountStrike(double baseDamage) {
        return super.fetchAmountStrike(baseDamage) * damageRatio();
    }

    @Override
    protected void setSize(float width, float height) {
        super.setSize(width * WIDTH_SCALE, height * HEIGHT_SCALE);
    }

    @Override
    public boolean allowStrikeGiant(Class<?> clazz) {
        return clazz != getClass() && super.allowStrikeGiant(clazz);
    }

    @Override
    public String getName() {
        if (hasCustomName())
            return getCustomNameTag();
        return I18n.translateToLocal(fetchKindSkeleton() == 1 ? "entity.thetitans.MutantWitherSkeletonTitan.name" : "entity.thetitans.MutantSkeletonTitan.name");
    }

    @Override
    protected String fetchHurtAudioGiant() {
        return "mutantbeasts:entity.mutant_skeleton.hurt";
    }

    @Override
    protected String fetchDeathAudioGiant() {
        return "mutantbeasts:entity.mutant_skeleton.death";
    }

    @Override
    protected void triggerAudioGiant(String sound, float volume, float pitch) {
        if (!MutantTitans.playIfMutant(this, sound, volume, pitch * 0.75F))
            super.triggerAudioGiant(sound, volume, pitch);
    }

    @Override
    public void playLivingSound() {
        MutantTitans.play(this, "entity.mutant_skeleton.ambient", getSoundVolume(), 0.6F + rand.nextFloat() * 0.2F);
    }

    // ------------------------------------------------------------------ death: falls apart like the mutant

    /**
     * Instead of the skeleton titan's long lie-down, the mutant titan collapses at once into its 18 giant bones
     * (the mutant's own body parts) which fly apart, crush what they hit and can be taken as Titanic bones.
     */
    @Override
    protected void whenDeathUpdateGiant() {
        if (getHealth() > 0.0F) {
            super.whenDeathUpdateGiant();
            return;
        }
        ++deathTicks;
        setHealth(0.0F);
        motionX = motionZ = 0.0D;
        setAttackTarget(null);
        if (!world.isRemote && deathTicks == 1) {
            MutantTitans.play(this, "entity.mutant_skeleton.hurt", 30.0F, 0.4F);
            shakeUserCamerasNearby(4.0D);
        }
        if (!world.isRemote && deathTicks == DEATH_COLLAPSE)
            collapse();
        if (deathTicks >= DEATH_COLLAPSE + 40)
            setDead();
    }

    private void collapse() {
        float s = fetchScaleMultiplierGiant();
        MutantTitans.play(this, "entity.mutant_skeleton.death", 30.0F, 0.5F);
        triggerAudioGiant("thetitans:titanFall", 20.0F, 1.0F);
        shakeUserCamerasNearby(12.0D);
        double r = width * 0.8D;
        for (net.minecraft.entity.Entity e : world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(r, height * 0.2D, r)))
            if (e instanceof EntityLivingBase && !(e instanceof com.electrifiedded.thetitans.entity.titan.EntityTitanPart) && allowStrikeGiant(e.getClass()))
                strikeMobChoosen((EntityLivingBase)e, (float)fetchAmountStrike(40.0D), 4);
        float dmg = (float)fetchAmountStrike(30.0D);
        double v = s * 0.35D;
        for (int part : EntityTitanBonePart.PARTS) {
            EntityTitanBonePart bone = new EntityTitanBonePart(world, this, part, s, dmg);
            bone.motionX = (rand.nextFloat() * 1.6F - 0.8F) * v;
            bone.motionY = (rand.nextFloat() * 0.25F + 0.1F) * v;
            bone.motionZ = (rand.nextFloat() * 1.6F - 0.8F) * v;
            if (isBurning())
                bone.setFire(10);
            world.spawnEntity(bone);
        }
        if (world instanceof WorldServer)
            ((WorldServer)world).spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY + height * 0.4D, posZ, 12, width * 0.5D, height * 0.3D, width * 0.5D, 0.0D);
    }

    // ------------------------------------------------------------------ AI

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_CONSTRICT, 80, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_MULTISHOT, 80, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_PIERCE, 70, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_VOLLEY, VOLLEY_DURATION, true));
    }

    private void startAnim(int id) {
        AnimationAPI.sendAnimPacket(this, id);
        setAnimID(id);
        setAnimTick(0);
    }

    @Override
    public void onLivingUpdate() {
        if (!world.isRemote) {
            EntityLivingBase target = getAttackTarget();
            if (target != null && !stunnedIs && getAnimID() == 0 && ticksExisted > 5 && deathTicks <= 0) {
                double d = getDistance(target);
                if (d < fetchReachMelee() && rand.nextInt(7) == 0)
                    startAnim(ANIM_CONSTRICT);
                else if (d >= fetchReachMelee() && rand.nextInt(80) == 0) {
                    int r = rand.nextInt(3);
                    startAnim(r == 0 ? ANIM_MULTISHOT : r == 1 ? ANIM_PIERCE : ANIM_VOLLEY);
                }
            }
            if (caughtId >= 0 && getAnimID() != ANIM_CONSTRICT)
                caughtId = -1;
        }
        super.onLivingUpdate();
    }

    @Override
    public void mutantAnimTick(int id, int tick) {
        EntityLivingBase target = getAttackTarget();
        switch (id) {
            case ANIM_CONSTRICT:
                tickConstrict(tick, target);
                break;
            case ANIM_MULTISHOT:
                motionX *= 0.5D;
                motionZ *= 0.5D;
                if (target != null)
                    getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
                if (tick == 14) {
                    MutantTitans.play(this, "entity.mutant_skeleton.jump", 20.0F, 0.6F);
                    motionY = 1.2D;
                }
                if (tick == 24)
                    MutantTitans.play(this, "entity.mutant_skeleton.bow_draw", 20.0F, 0.6F);
                if (target != null && tick >= 34 && tick <= 58 && tick % 6 == 4) {
                    MutantTitans.play(this, "entity.mutant_skeleton.bow_shoot", 20.0F, 0.7F);
                    // the mutant's crossbow: three bolts at once
                    for (int i = 0; i < 3; i++)
                        attackEntityWithRangedAttack(target, 1.0F);
                }
                break;
            case ANIM_VOLLEY:
                motionX = motionZ = 0.0D;
                if (target != null)
                    getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
                if (tick == 6)
                    MutantTitans.play(this, "entity.mutant_skeleton.bow_draw", 30.0F, 0.45F);
                if (target != null && tick >= VOLLEY_START && (tick - VOLLEY_START) % VOLLEY_RATE == 0 && (tick - VOLLEY_START) / VOLLEY_RATE < VOLLEY_SHOTS) {
                    MutantTitans.play(this, "entity.mutant_skeleton.bow_shoot", 30.0F, 0.45F + rand.nextFloat() * 0.1F);
                    for (int i = -1; i <= 1; i++)
                        fireGiantArrow(target, i * 9.0F);
                    shakeUserCamerasNearby(2.0D);
                }
                break;
            case ANIM_PIERCE:
                motionX = motionZ = 0.0D;
                if (target != null)
                    getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
                if (tick == 8)
                    MutantTitans.play(this, "entity.mutant_skeleton.bow_draw", 20.0F, 0.5F);
                if (tick == 40 && target != null) {
                    MutantTitans.play(this, "entity.mutant_skeleton.bow_shoot", 30.0F, 0.5F);
                    strikeWithEnlargedRangedStrikeMob(target, 1.0F);
                    for (int i = 0; i < 4; i++)
                        attackEntityWithRangedAttack(target, 0.3F);
                    shakeUserCamerasNearby(4.0D);
                }
                break;
        }
    }

    @Override
    public void mutantAnimEnd(int id) {
        caughtId = -1;
    }

    /** One giant crossbow bolt at the target, turned {@code yawOffset} degrees off the line of fire. */
    private void fireGiantArrow(EntityLivingBase target, float yawOffset) {
        renderYawOffset = rotationYaw = rotationYawHead;
        float s = fetchScaleMultiplierGiant();
        float dis = 0.625F * s;
        float xfac = MathHelper.sin(renderYawOffset * 0.017453292F), zfac = MathHelper.cos(renderYawOffset * 0.017453292F);
        double sx = posX - xfac * dis, sy = posY + height * 0.62D, sz = posZ + zfac * dis;
        double dx = target.posX - sx, dy = target.posY + target.height * 0.5D - sy, dz = target.posZ - sz;
        float c = MathHelper.cos(yawOffset * 0.017453292F), sn = MathHelper.sin(yawOffset * 0.017453292F);
        double rx = dx * c - dz * sn, rz = dx * sn + dz * c;
        com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitanGiantArrow arrow = new com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitanGiantArrow(world, this, rx, dy, rz);
        arrow.posX = sx;
        arrow.posY = sy;
        arrow.posZ = sz;
        arrow.assignKindArrow(fetchVariantGiant() == 4 ? 2 : 1);
        world.spawnEntity(arrow);
    }

    /** The mutant's rib cage opens, snaps shut around the victim and crushes it before spitting it out. */
    private void tickConstrict(int tick, EntityLivingBase target) {
        motionX = motionZ = 0.0D;
        if (tick < CONSTRICT_GRAB && target != null)
            getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        float yaw = renderYawOffset * 0.017453292F;
        double fx = -MathHelper.sin(yaw), fz = MathHelper.cos(yaw);
        double cx = posX + fx * width * 0.35D, cy = posY + height * 0.55D, cz = posZ + fz * width * 0.35D;
        if (tick == CONSTRICT_GRAB) {
            Entity caught = null;
            if (target != null && getDistance(target) < fetchReachMelee() * 1.2D && !(target instanceof EntityTitan))
                caught = target;
            if (caught == null) {
                List<Entity> near = world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(cx, posY, cz, cx, posY + height, cz).grow(width * 0.6D, 0.0D, width * 0.6D));
                for (Entity e : near)
                    if (e instanceof EntityLivingBase && !(e instanceof EntityTitanPart) && !(e instanceof EntityTitan) && allowStrikeGiant(e.getClass())) {
                        caught = e;
                        break;
                    }
            }
            caughtId = caught == null ? -1 : caught.getEntityId();
            MutantTitans.play(this, "entity.mutant_skeleton.bite", 20.0F, 0.5F);
            if (caught == null && target instanceof EntityTitan && getDistance(target) < fetchReachMelee() * 1.2D)
                strikeMobChoosen(target, (float)fetchAmountStrike(300.0D), 6); // too big to swallow: bitten instead
        }
        Entity caught = caughtId >= 0 ? world.getEntityByID(caughtId) : null;
        if (caught != null && !caught.isEntityAlive())
            caught = null;
        if (caught != null && tick > CONSTRICT_GRAB && tick < CONSTRICT_RELEASE) {
            caught.setPositionAndUpdate(cx, cy - caught.height * 0.5D, cz);
            caught.motionX = caught.motionY = caught.motionZ = 0.0D;
            caught.fallDistance = 0.0F;
            if (tick % 8 == 0) {
                strikeMobChoosen(caught, (float)fetchAmountStrike(45.0D), 0);
                if (caught instanceof EntityLivingBase)
                    ((EntityLivingBase)caught).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 4));
                MutantTitans.play(this, "entity.mutant_skeleton.punch", 10.0F, 0.6F);
            }
        }
        if (tick == CONSTRICT_RELEASE && caught != null) {
            caught.motionX = fx * 3.0D;
            caught.motionY = 1.6D;
            caught.motionZ = fz * 3.0D;
            caught.velocityChanged = true;
            strikeMobChoosen(caught, (float)fetchAmountStrike(60.0D), 6);
            caughtId = -1;
        }
    }
}
