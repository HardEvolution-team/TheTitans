package com.electrifiedded.thetitans.entity.titan.mutant;

import java.util.ArrayList;
import java.util.List;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.compat.mutant.MutantTitans;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityFallingBlockTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Mutant Ender Colossus: the Ender Colossus on the four-armed Mutant Enderman rig (plus horns, mandibles and a
 * spined back) with the mutant's block barrage from all four hands, tele-smash, scream and clone swarm.
 */
@SuppressWarnings("deprecation")
public class EntityMutantEnderColossus extends EntityEnderColossus implements AIMutantTitanAnim.Host {
    /** Mutant Enderman 1.2x4.2 vs Enderman 0.6x2.9. */
    public static final float WIDTH_SCALE = 1.2F / 0.6F, HEIGHT_SCALE = 4.2F / 2.9F;

    public static final int ANIM_BARRAGE = 20, ANIM_TELESMASH = 21, ANIM_SCREAM = 22, ANIM_CLONES = 23;
    /** Barrage: the arm that throws at each of these ticks (1 = upper right, 2 = upper left, 3/4 = lower). */
    public static final int[] BARRAGE_TICKS = { 30, 40, 50, 60 };
    public static final int TELE_LIFT = 14, TELE_SMASH = 44;

    private static final Block[] THROWN = { Blocks.END_STONE, Blocks.OBSIDIAN, Blocks.STONE, Blocks.GRASS, Blocks.END_STONE };

    /** {x, y, z, ticksLeft} of in-flight block impacts. */
    private final List<double[]> impacts = new ArrayList<>();
    private int liftedId = -1;

    public EntityMutantEnderColossus(World worldIn) {
        super(worldIn);
        experienceValue = 80000;
    }

    public static double hpRatio() {
        return MutantTitans.ratio("mutantEndermanMaxHealth", 200.0D, 40.0D);
    }

    /** Same 7 melee as the enderman, but with four arms and 8-damage thrown blocks. */
    public static double damageRatio() {
        return (MutantTitans.ratio("mutantEndermanAttackDamage", 7.0D, 7.0D) + MutantTitans.ratio("mutantEndermanBlockDamage", 8.0D, 7.0D)) * 0.75D;
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
        return I18n.translateToLocal("entity.thetitans.MutantEnderColossus.name");
    }

    @Override
    protected String fetchHurtAudioGiant() {
        return "mutantbeasts:entity.mutant_enderman.hurt";
    }

    @Override
    protected String fetchDeathAudioGiant() {
        return "mutantbeasts:entity.mutant_enderman.death";
    }

    @Override
    protected void triggerAudioGiant(String sound, float volume, float pitch) {
        if (!MutantTitans.playIfMutant(this, sound, volume, pitch * 0.7F))
            super.triggerAudioGiant(sound, volume, pitch);
    }

    @Override
    public void playLivingSound() {
        MutantTitans.play(this, "entity.mutant_enderman.ambient", getSoundVolume(), 0.5F + rand.nextFloat() * 0.2F);
    }

    // ------------------------------------------------------------------ AI

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_BARRAGE, 90, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_TELESMASH, 70, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_SCREAM, 120, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_CLONES, 40, true));
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
                int r = rand.nextInt(d < fetchReachMelee() ? 40 : 70);
                if (r == 0)
                    startAnim(ANIM_BARRAGE);
                else if (r == 1)
                    startAnim(ANIM_TELESMASH);
                else if (r == 2 && d < fetchReachMelee() * 2.0D)
                    startAnim(ANIM_SCREAM);
                else if (r == 3 && d > fetchReachMelee() * 2.0D)
                    startAnim(ANIM_CLONES);
            }
            tickImpacts();
            if (liftedId >= 0 && getAnimID() != ANIM_TELESMASH)
                liftedId = -1;
        }
        super.onLivingUpdate();
    }

    private void tickImpacts() {
        for (int i = impacts.size() - 1; i >= 0; i--) {
            double[] im = impacts.get(i);
            if (--im[3] > 0)
                continue;
            impacts.remove(i);
            double r = fetchScaleMultiplierGiant() * 0.18D;
            MutantTitans.strikeAround(this, im[0], im[1], im[2], r, 90.0D, 3, 0.8D);
            MutantTitans.groundBurst(world, im[0], im[1], im[2], 40);
        }
    }

    @Override
    public void mutantAnimTick(int id, int tick) {
        EntityLivingBase target = getAttackTarget();
        switch (id) {
            case ANIM_BARRAGE:
                motionX *= 0.3D;
                motionZ *= 0.3D;
                if (target != null)
                    getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
                for (int arm = 0; arm < BARRAGE_TICKS.length; arm++) {
                    if (tick == BARRAGE_TICKS[arm] - 20)
                        MutantTitans.play(this, "entity.mutant_enderman.morph", 10.0F, 0.6F);
                    if (tick == BARRAGE_TICKS[arm] && target != null)
                        throwBlock(target, arm);
                }
                break;
            case ANIM_TELESMASH:
                tickTeleSmash(tick, target);
                break;
            case ANIM_SCREAM:
                motionX = motionZ = 0.0D;
                if (tick == 18)
                    MutantTitans.play(this, "entity.mutant_enderman.scream", 40.0F, 0.6F);
                if (tick >= 20 && tick <= 100 && tick % 10 == 0) {
                    double r = width * 2.0D;
                    shakeUserCamerasNearby(10.0D);
                    for (Entity e : world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(r, r * 0.4D, r))) {
                        if (!(e instanceof EntityLivingBase) || e instanceof EntityTitanPart || !allowStrikeGiant(e.getClass()))
                            continue;
                        EntityLivingBase l = (EntityLivingBase)e;
                        if (!(l instanceof EntityTitan)) {
                            l.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
                            l.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 80, 0));
                            l.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1));
                            double dx = l.posX - posX, dz = l.posZ - posZ, len = Math.max(0.01D, Math.sqrt(dx * dx + dz * dz));
                            l.motionX += dx / len * 0.8D;
                            l.motionZ += dz / len * 0.8D;
                        }
                        strikeMobChoosen(l, (float)fetchAmountStrike(25.0D), 1);
                    }
                }
                break;
            case ANIM_CLONES:
                motionX = motionZ = 0.0D;
                if (tick == 10 && target != null) {
                    MutantTitans.play(this, "entity.mutant_enderman.teleport", 30.0F, 0.6F);
                    int n = 2 + world.getDifficulty().getId();
                    for (int i = 0; i < n; i++) {
                        EntityEndermanMinion clone = new EntityEndermanMinion(world);
                        double a = Math.PI * 2.0D * i / n;
                        clone.setLocationAndAngles(target.posX + Math.cos(a) * 6.0D, target.posY, target.posZ + Math.sin(a) * 6.0D, rand.nextFloat() * 360.0F, 0.0F);
                        clone.onInitialSpawn(world.getDifficultyForLocation(clone.getPosition()), null);
                        clone.setAttackTarget(target);
                        world.spawnEntity(clone);
                        portalBurst(clone.posX, clone.posY + 1.0D, clone.posZ, 20);
                    }
                }
                if (tick == 25 && target != null && getDistance(target) > fetchReachMelee() * 1.5D) {
                    // the colossus itself blinks next to its prey
                    double a = rand.nextDouble() * Math.PI * 2.0D, r = fetchReachMelee() * 0.8D;
                    portalBurst(posX, posY + height * 0.5D, posZ, 80);
                    warpToMob(this, target.posX + Math.cos(a) * r, target.posY, target.posZ + Math.sin(a) * r);
                    portalBurst(posX, posY + height * 0.5D, posZ, 80);
                }
                break;
        }
    }

    /**
     * The mutant enderman's thrown block, at colossus scale: a giant spinning cube of terrain on a ballistic arc
     * ({@link EntityTitanThrownBlock}). The last arm of the barrage hurls a double-size one.
     */
    private void throwBlock(EntityLivingBase target, int arm) {
        float yaw = renderYawOffset * 0.017453292F;
        double side = (arm % 2 == 0 ? -1.0D : 1.0D) * width * 0.45D;
        double sx = posX + MathHelper.cos(yaw) * side, sy = posY + height * (arm < 2 ? 0.85D : 0.65D), sz = posZ + MathHelper.sin(yaw) * side;
        double spread = arm == BARRAGE_TICKS.length - 1 ? 0.0D : 6.0D;
        double tx = target.posX + (rand.nextDouble() - 0.5D) * spread, ty = target.posY, tz = target.posZ + (rand.nextDouble() - 0.5D) * spread;
        float size = Math.min(14.0F, Math.max(2.0F, height * 0.11F)) * (arm == BARRAGE_TICKS.length - 1 ? 2.0F : 1.0F);
        double dist = Math.sqrt((tx - sx) * (tx - sx) + (tz - sz) * (tz - sz));
        int flight = MathHelper.clamp((int)(dist / 4.0D), 14, 40);
        Block b = THROWN[rand.nextInt(THROWN.length)];
        float dmg = (float)fetchAmountStrike(MutantTitans.config("mutantEndermanBlockDamage", 8.0D) * 12.0D) * (size > 14.0F ? 2.0F : 1.0F);
        EntityTitanThrownBlock block = new EntityTitanThrownBlock(world, this, b.getDefaultState(), size, dmg);
        block.setPosition(sx, sy - size * 0.5D, sz);
        block.aimAt(tx, ty, tz, flight);
        world.spawnEntity(block);
        portalBurst(sx, sy, sz, 40);
        MutantTitans.play(this, "entity.mutant_enderman.teleport", 12.0F, 0.8F);
    }

    /** Teleports the target high above, holds it, then hurls it down. */
    private void tickTeleSmash(int tick, EntityLivingBase target) {
        motionX = motionZ = 0.0D;
        if (tick == TELE_LIFT && target != null) {
            MutantTitans.play(this, "entity.mutant_enderman.stare", 20.0F, 0.6F);
            if (target instanceof EntityTitan)
                liftedId = -1;
            else {
                liftedId = target.getEntityId();
                portalBurst(target.posX, target.posY + 1.0D, target.posZ, 40);
                target.setPositionAndUpdate(target.posX, target.posY + 24.0D, target.posZ);
                portalBurst(target.posX, target.posY + 1.0D, target.posZ, 40);
            }
        }
        Entity lifted = liftedId >= 0 ? world.getEntityByID(liftedId) : null;
        if (lifted != null && tick > TELE_LIFT && tick < TELE_SMASH) {
            lifted.motionX = lifted.motionZ = 0.0D;
            lifted.motionY = 0.02D;
            lifted.fallDistance = 0.0F;
        }
        if (tick == TELE_SMASH) {
            if (lifted != null) {
                lifted.motionY = -5.0D;
                lifted.velocityChanged = true;
                strikeMobChoosen(lifted, (float)fetchAmountStrike(MutantTitans.config("mutantEndermanTelesmashDamage", 6.0D) * 12.0D), 0);
            } else if (target != null && getDistance(target) < fetchReachMelee() * 2.0D) {
                strikeMobChoosen(target, (float)fetchAmountStrike(250.0D), 8);
                shakeUserCamerasNearby(6.0D);
            }
            MutantTitans.play(this, "entity.mutant_enderman.teleport", 20.0F, 0.5F);
            liftedId = -1;
        }
    }

    private void portalBurst(double x, double y, double z, int n) {
        if (world instanceof WorldServer)
            ((WorldServer)world).spawnParticle(EnumParticleTypes.PORTAL, x, y, z, n, 2.0D, 2.0D, 2.0D, 0.5D);
    }

    @Override
    public void mutantAnimEnd(int id) {
        liftedId = -1;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
    }
}
