package com.electrifiedded.thetitans.entity.titan.mutant;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.compat.mutant.MutantTitans;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGammaLightning;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Mutant Creeper Titan: the Creeper Titan on the four-legged Mutant Creeper rig (with dorsal vents, toe claws
 * and a tail) plus the mutant's pounce-and-detonate, the lightning charge-up and creeper-minion broods.
 */
@SuppressWarnings("deprecation")
public class EntityMutantCreeperTitan extends EntityCreeperTitan implements AIMutantTitanAnim.Host {
    /** Mutant Creeper 1.98x2.8 vs Creeper 0.6x1.7. */
    public static final float WIDTH_SCALE = 1.98F / 0.6F, HEIGHT_SCALE = 2.8F / 1.7F;

    public static final int ANIM_POUNCE = 20, ANIM_CHARGE = 21, ANIM_BROOD = 22;
    public static final int POUNCE_LEAP = 18;

    private boolean pounceAirborne;
    private int pounceLandTick = -1;

    public EntityMutantCreeperTitan(World worldIn) {
        super(worldIn);
        experienceValue = 40000;
    }

    public static double hpRatio() {
        return MutantTitans.ratio("mutantCreeperMaxHealth", 150.0D, 20.0D);
    }

    /** Explosion strength 8 (12 charged) vs the creeper's 3 (6). */
    public static double damageRatio() {
        return MutantTitans.ratio("mutantCreeperDeathStrength", 8.0D, 3.0D);
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * hpRatio();
    }

    @Override
    public double fetchAmountStrike(double baseDamage) {
        return super.fetchAmountStrike(baseDamage) * damageRatio();
    }

    /** Radius of the death blast: a 10000 x 10000 crater down to bedrock. */
    public static double MEGA_BLAST_RADIUS = 5000.0D;

    @Override
    protected double blastRadius() {
        return MEGA_BLAST_RADIUS;
    }

    /** The wave kills what it passes: every living thing in the carved volume takes the blast (loot bombs survive). */
    @Override
    protected com.electrifiedded.archaiclib.world.ChunkBlast startBlast(double radius) {
        com.electrifiedded.archaiclib.world.ChunkBlast blast = super.startBlast(radius);
        if (blast != null) {
            final float dmg = (float)fetchAmountStrike(500.0D);
            final net.minecraft.util.DamageSource src = com.electrifiedded.thetitans.effect.DamageSourceExtra.causeGiantExplosionHarmCreeper(this);
            blast.onEntity = e -> {
                if (e instanceof EntityLivingBase && e != this && e.isEntityAlive()) {
                    e.hurtResistantTime = 0;
                    if (e instanceof com.electrifiedded.thetitans.entity.titan.EntityTitan)
                        ((com.electrifiedded.thetitans.entity.titan.EntityTitan)e).hurt(dmg);
                    else
                        e.attackEntityFrom(src, dmg);
                }
            };
            MutantTitans.play(this, "entity.mutant_creeper.death", 10000.0F, 0.4F);
        }
        return blast;
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
        String base = I18n.translateToLocal("entity.thetitans.MutantCreeperTitan.name");
        return getPowered() ? I18n.translateToLocal("entity.thetitans.MutantTitan.charged") + " " + base : base;
    }

    @Override
    protected String fetchHurtAudioGiant() {
        return "mutantbeasts:entity.mutant_creeper.hurt";
    }

    @Override
    protected String fetchDeathAudioGiant() {
        return "mutantbeasts:entity.mutant_creeper.death";
    }

    @Override
    protected void triggerAudioGiant(String sound, float volume, float pitch) {
        if (!MutantTitans.playIfMutant(this, sound, volume, pitch * 0.75F))
            super.triggerAudioGiant(sound, volume, pitch);
    }

    @Override
    public void playLivingSound() {
        MutantTitans.play(this, "entity.mutant_creeper.ambient", getSoundVolume(), 0.6F + rand.nextFloat() * 0.2F);
    }

    // ------------------------------------------------------------------ AI

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_POUNCE, 90, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_CHARGE, 140, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_BROOD, 60, true));
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
                if (d > fetchReachMelee() * 0.8D && d < fetchReachMelee() * 5.0D && rand.nextInt(60) == 0)
                    startAnim(ANIM_POUNCE);
                else if (!getPowered() && getHealth() < getMaxHealth() * 0.5F && rand.nextInt(400) == 0)
                    startAnim(ANIM_CHARGE);
                else if (rand.nextInt(250) == 0)
                    startAnim(ANIM_BROOD);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public void mutantAnimTick(int id, int tick) {
        EntityLivingBase target = getAttackTarget();
        switch (id) {
            case ANIM_POUNCE:
                tickPounce(tick, target);
                break;
            case ANIM_CHARGE:
                motionX = motionZ = 0.0D;
                if (tick == 10)
                    MutantTitans.play(this, "entity.mutant_creeper.charge", 40.0F, 0.6F);
                if (tick >= 20 && tick <= 110 && tick % 15 == 5) {
                    double a = rand.nextDouble() * Math.PI * 2.0D, r = width * (0.6D + rand.nextDouble());
                    double lx = posX + Math.cos(a) * r, lz = posZ + Math.sin(a) * r;
                    world.addWeatherEffect(new EntityGammaLightning(world, lx, posY, lz, 0.4F, 0.8F, 1.0F));
                    MutantTitans.strikeAround(this, lx, posY, lz, fetchScaleMultiplierGiant() * 0.3D, 60.0D, 2, 0.8D);
                }
                if (tick == 120 && !getPowered()) {
                    setPowered(true);
                    world.addWeatherEffect(new EntityGammaLightning(world, posX, posY, posZ, 0.4F, 0.8F, 1.0F));
                    shakeUserCamerasNearby(10.0D);
                }
                break;
            case ANIM_BROOD:
                motionX = motionZ = 0.0D;
                if (tick == 30 && !world.isRemote) {
                    MutantTitans.play(this, "entity.creeper_minion.primed", 20.0F, 0.5F);
                    int n = 3 + world.getDifficulty().getId() + (getPowered() ? 3 : 0);
                    for (int i = 0; i < n; i++) {
                        EntityCreeperMinion m = new EntityCreeperMinion(world);
                        double a = Math.PI * 2.0D * i / n;
                        m.setLocationAndAngles(posX, posY + height * 0.6D, posZ, (float)(a * 180.0D / Math.PI), 0.0F);
                        m.onInitialSpawn(world.getDifficultyForLocation(m.getPosition()), null);
                        m.motionX = Math.cos(a) * 1.6D;
                        m.motionY = 1.0D;
                        m.motionZ = Math.sin(a) * 1.6D;
                        world.spawnEntity(m); // a charged titan electrifies its brood itself (EntityCreeperTitan)
                    }
                }
                break;
        }
    }

    /** Crouch, leap at the target, detonate on landing (radius &amp; power follow the charged state). */
    private void tickPounce(int tick, EntityLivingBase target) {
        if (tick < POUNCE_LEAP) {
            motionX = motionZ = 0.0D;
            if (target != null)
                getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
            pounceAirborne = false;
            pounceLandTick = -1;
        }
        if (tick == POUNCE_LEAP) {
            MutantTitans.play(this, "entity.mutant_creeper.charge", 20.0F, 0.8F);
            double dx, dz;
            if (target != null) {
                dx = target.posX - posX;
                dz = target.posZ - posZ;
            } else {
                dx = -MathHelper.sin(renderYawOffset * 0.017453292F);
                dz = MathHelper.cos(renderYawOffset * 0.017453292F);
            }
            double len = Math.max(0.001D, Math.sqrt(dx * dx + dz * dz));
            double reach = Math.min(len, fetchReachMelee() * 4.0D);
            // ballistic hop: ~24 ticks of air time
            motionX = dx / len * reach / 24.0D;
            motionZ = dz / len * reach / 24.0D;
            motionY = MutantTitans.config("mutantCreeperJumpingStrength", 4.0D) * 0.45D;
            pounceAirborne = true;
            isAirBorne = true;
        }
        if (pounceAirborne && tick > POUNCE_LEAP + 4 && (onGround || tick >= POUNCE_LEAP + 40)) {
            pounceAirborne = false;
            pounceLandTick = tick;
            boolean charged = getPowered();
            double r = fetchScaleMultiplierGiant() * (charged ? 1.2D : 0.8D);
            MutantTitans.strikeAround(this, posX, posY, posZ, r, charged ? 300.0D : 180.0D, 6, 2.0D);
            if (world instanceof WorldServer)
                ((WorldServer)world).spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY + 2.0D, posZ, 30, r * 0.5D, 2.0D, r * 0.5D, 0.0D);
            triggerAudioGiant("thetitans:titanland", 50.0F, 1.0F);
            world.playSound(null, posX, posY, posZ, net.minecraft.init.SoundEvents.ENTITY_GENERIC_EXPLODE, net.minecraft.util.SoundCategory.HOSTILE, 30.0F, 0.5F);
            shakeUserCamerasNearby(12.0D);
            motionX = motionZ = 0.0D;
        }
        if (pounceLandTick > 0)
            motionX = motionZ = 0.0D;
    }
}
