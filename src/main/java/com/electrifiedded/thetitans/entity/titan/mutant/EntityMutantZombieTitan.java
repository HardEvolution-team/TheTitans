package com.electrifiedded.thetitans.entity.titan.mutant;

import java.util.List;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.compat.mutant.MutantTitans;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan2RebuildSword;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan7DownwardSlash;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * Mutant Zombie Titan: the Zombie Titan's kit, re-animated on the Mutant Zombie rig, plus the mutant's moves —
 * seismic hammer-fists, grab &amp; hurl, the summoning roar and its three lives.
 */
@SuppressWarnings("deprecation")
public class EntityMutantZombieTitan extends EntityZombieTitan implements AIMutantTitanAnim.Host {
    /** Mutant Zombie 1.8x3.2 vs Zombie 0.6x1.95. */
    public static final float WIDTH_SCALE = 1.8F / 0.6F, HEIGHT_SCALE = 3.2F / 1.95F;

    public static final int ANIM_SMASH = 20, ANIM_THROW = 21, ANIM_ROAR = 22, ANIM_REVIVE = 23;
    public static final int THROW_GRAB_TICK = 16, THROW_RELEASE_TICK = 40;

    private final TitanSeismicWaves waves = new TitanSeismicWaves(this);
    private int lives = -1;
    private int grabbedId = -1;
    /** Titans raised by the last summoning roar, and ticks until the next one is allowed. */
    private final java.util.List<java.util.UUID> horde = new java.util.ArrayList<>();
    private int hordeCooldown;
    public static final int HORDE_TICK = 60, HORDE_COOLDOWN = 2400, HORDE_MUTANTS = 2, HORDE_ORIGINALS = 3;
    private double smashDirX, smashDirZ;

    public EntityMutantZombieTitan(World worldIn) {
        super(worldIn);
        experienceValue = 40000;
    }

    // ------------------------------------------------------------------ stats (real mutant/vanilla ratios)

    public static double hpRatio() {
        return MutantTitans.ratio("mutantZombieMaxHealth", 150.0D, 20.0D);
    }

    public static double damageRatio() {
        return MutantTitans.ratio("mutantZombieAttackDamage", 12.0D, 3.0D);
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
    public float getEyeHeight() {
        return 0.82F * height;
    }

    public int getLives() {
        if (lives < 0)
            lives = (int)MutantTitans.config("mutantZombieLives", 3.0D);
        return lives;
    }

    // ------------------------------------------------------------------ no baby / villager / sword forms

    @Override
    public boolean isChild() {
        return false;
    }

    @Override
    public boolean villagerIs() {
        return false;
    }

    @Override
    public boolean armedIs() {
        return false;
    }

    @Override
    public void assignArmed(boolean armed) {
    }

    @Override
    public boolean allowStrikeGiant(Class<?> clazz) {
        return clazz != getClass() && super.allowStrikeGiant(clazz);
    }

    @Override
    public String getName() {
        if (hasCustomName())
            return getCustomNameTag();
        String base = I18n.translateToLocal("entity.thetitans.MutantZombieTitan.name");
        switch (fetchVariantGiant()) {
            case 1: return I18n.translateToLocal("entity.thetitans.MutantTitan.jungle") + " " + base;
            case 2: return I18n.translateToLocal("entity.thetitans.MutantTitan.blood") + " " + base;
            case 3: return I18n.translateToLocal("entity.thetitans.MutantTitan.radioactive") + " " + base;
            case 4: return I18n.translateToLocal("entity.thetitans.MutantTitan.void") + " " + base;
            default: return base;
        }
    }

    // ------------------------------------------------------------------ sounds through the mutant mod

    @Override
    protected String fetchHurtAudioGiant() {
        return "mutantbeasts:entity.mutant_zombie.hurt";
    }

    @Override
    protected String fetchDeathAudioGiant() {
        return "mutantbeasts:entity.mutant_zombie.death";
    }

    @Override
    protected void triggerAudioGiant(String sound, float volume, float pitch) {
        if ("mob.zombie.death".equals(sound))
            sound = fetchDeathAudioGiant();
        if (!MutantTitans.playIfMutant(this, sound, volume, pitch * 0.8F))
            super.triggerAudioGiant(sound, volume, pitch);
    }

    @Override
    public void playLivingSound() {
        MutantTitans.play(this, "entity.mutant_zombie.ambient", getSoundVolume(), 0.6F + rand.nextFloat() * 0.2F);
    }

    // ------------------------------------------------------------------ AI

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        // the mutant has no sword: "rebuild sword" becomes the grab & hurl, the sword slash never happens
        AIMutantTitanAnim.removeTasks(tasks, AnimationZombieTitan2RebuildSword.class, AnimationZombieTitan7DownwardSlash.class);
        tasks.addTask(1, new AIMutantTitanAnim(this, 2, 70, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_SMASH, 50, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_THROW, 70, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_ROAR, 200, true));
        tasks.addTask(1, new AIMutantTitanAnim(this, ANIM_REVIVE, 140, false));
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
                if (d < fetchReachMelee() * 1.1D) {
                    int r = rand.nextInt(6);
                    if (r == 0)
                        startAnim(ANIM_SMASH);
                    else if (r == 1)
                        startAnim(ANIM_THROW);
                } else if (rand.nextInt(hordeCooldown <= 0 && !isSummonedGiant() ? 120 : 400) == 0) {
                    startAnim(ANIM_ROAR);
                } else if (rand.nextInt(120) == 0 && d < fetchReachMelee() * 3.0D) {
                    startAnim(ANIM_SMASH);
                }
            }
            waves.tick(1);
            if (hordeCooldown > 0)
                hordeCooldown--;
            if (grabbedId >= 0 && getAnimID() != ANIM_THROW && getAnimID() != 2)
                grabbedId = -1;
        }
        super.onLivingUpdate();
    }

    @Override
    public void mutantAnimTick(int id, int tick) {
        switch (id) {
            case ANIM_SMASH:
                tickSmash(tick);
                break;
            case 2:
            case ANIM_THROW:
                tickThrow(tick);
                break;
            case ANIM_ROAR:
                tickRoar(tick);
                break;
            case ANIM_REVIVE:
                motionX = motionZ = 0.0D;
                if (tick == 100)
                    MutantTitans.play(this, "entity.mutant_zombie.roar", 20.0F, 0.7F);
                break;
        }
    }

    @Override
    public void mutantAnimEnd(int id) {
        grabbedId = -1;
    }

    /** The mutant's hammer-fists: both arms come down and send a seismic wave towards the target. */
    private void tickSmash(int tick) {
        EntityLivingBase target = getAttackTarget();
        motionX *= 0.2D;
        motionZ *= 0.2D;
        if (tick == 1)
            MutantTitans.play(this, "entity.mutant_zombie.attack", 10.0F, 0.6F + rand.nextFloat() * 0.2F);
        if (tick < 16 && target != null)
            getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (tick == 16) {
            double dx, dz;
            if (target != null) {
                dx = target.posX - posX;
                dz = target.posZ - posZ;
            } else {
                dx = -MathHelper.sin(renderYawOffset * 0.017453292F);
                dz = MathHelper.cos(renderYawOffset * 0.017453292F);
            }
            double len = Math.max(Math.sqrt(dx * dx + dz * dz), 0.001D);
            smashDirX = dx / len;
            smashDirZ = dz / len;
            renderYawOffset = rotationYaw = rotationYawHead = MutantTitans.yawTo(this, posX + smashDirX, posZ + smashDirZ);
        }
        if (tick == 24) {
            float s = fetchScaleMultiplierGiant();
            // fists land in front of the titan, the wave runs ~4x the reach
            double front = width * 0.45D;
            MutantTitans.strikeAround(this, posX + smashDirX * front, posY, posZ + smashDirZ * front, width * 0.45D, 120.0D, 4, 1.5D);
            waves.line(smashDirX, smashDirZ, front, 14, s * 0.3D, s * 0.12D, 70.0D);
            shakeUserCamerasNearby(8.0D);
            MutantTitans.groundBurst(world, posX + smashDirX * front, posY, posZ + smashDirZ * front, 120);
        }
    }

    /** Grab whatever stands in front, lift it and hurl it (titans are too heavy to throw: they get crushed instead). */
    private void tickThrow(int tick) {
        EntityLivingBase target = getAttackTarget();
        motionX *= 0.2D;
        motionZ *= 0.2D;
        if (tick < THROW_GRAB_TICK && target != null)
            getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        float yaw = renderYawOffset * 0.017453292F;
        double fx = -MathHelper.sin(yaw), fz = MathHelper.cos(yaw);
        double handX = posX + fx * width * 0.55D, handY = posY + height * 0.55D, handZ = posZ + fz * width * 0.55D;
        if (tick == THROW_GRAB_TICK) {
            MutantTitans.play(this, "entity.mutant_zombie.grunt", 10.0F, 0.6F);
            Entity grabbed = null;
            if (target != null && getDistance(target) < fetchReachMelee() * 1.3D)
                grabbed = target;
            else {
                List<Entity> near = world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(handX, posY, handZ, handX, posY + height * 0.6D, handZ).grow(width * 0.5D, 0.0D, width * 0.5D));
                for (Entity e : near)
                    if (e instanceof EntityLivingBase && !(e instanceof EntityTitanPart) && allowStrikeGiant(e.getClass())) {
                        grabbed = e;
                        break;
                    }
            }
            grabbedId = grabbed == null ? -1 : grabbed.getEntityId();
            if (grabbed != null)
                strikeMobChoosen(grabbed, (float)fetchAmountStrike(40.0D), 0);
        }
        Entity grabbed = grabbedId >= 0 ? world.getEntityByID(grabbedId) : null;
        if (grabbed != null && !grabbed.isEntityAlive())
            grabbed = null;
        if (grabbed != null && tick > THROW_GRAB_TICK && tick < THROW_RELEASE_TICK && !(grabbed instanceof EntityTitan)) {
            // hold it up in both fists
            double lift = Math.min(1.0D, (tick - THROW_GRAB_TICK) / 10.0D);
            grabbed.setPositionAndUpdate(handX, handY + height * 0.45D * lift, handZ);
            grabbed.motionX = grabbed.motionY = grabbed.motionZ = 0.0D;
            grabbed.fallDistance = 0.0F;
        }
        if (tick == THROW_RELEASE_TICK) {
            MutantTitans.play(this, "entity.mutant_zombie.attack", 10.0F, 0.5F);
            if (grabbed != null) {
                if (grabbed instanceof EntityTitan) {
                    strikeMobChoosen(grabbed, (float)fetchAmountStrike(400.0D), 12);
                    shakeUserCamerasNearby(6.0D);
                } else {
                    grabbed.motionX = fx * 4.5D;
                    grabbed.motionY = 2.2D;
                    grabbed.motionZ = fz * 4.5D;
                    grabbed.velocityChanged = true;
                    strikeMobChoosen(grabbed, (float)fetchAmountStrike(80.0D), 6);
                }
            }
            grabbedId = -1;
        }
    }

    /** The mutant roar: stuns and starves everything around, raises the horde. */
    private void tickRoar(int tick) {
        motionX = motionZ = 0.0D;
        if (!world.isRemote && tick == HORDE_TICK && canCallHorde())
            callHorde();
        if (tick == 20) {
            MutantTitans.play(this, "entity.mutant_zombie.roar", 40.0F, 0.6F);
            shakeUserCamerasNearby(12.0D);
        }
        if (tick >= 25 && tick <= 140 && tick % 10 == 5) {
            double r = width * 1.4D;
            List<Entity> near = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(r, r * 0.3D, r));
            for (Entity e : near) {
                if (!(e instanceof EntityLivingBase) || e instanceof EntityTitan || e instanceof EntityTitanPart || !allowStrikeGiant(e.getClass()))
                    continue;
                EntityLivingBase l = (EntityLivingBase)e;
                double dx = l.posX - posX, dz = l.posZ - posZ, len = Math.max(0.01D, Math.sqrt(dx * dx + dz * dz));
                l.motionX += dx / len * 1.2D;
                l.motionZ += dz / len * 1.2D;
                l.motionY += 0.3D;
                l.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 200, 2));
                l.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 80, 2));
                if (tick == 25)
                    strikeMobChoosen(l, (float)fetchAmountStrike(30.0D), 3);
            }
        }
        if (!world.isRemote && (tick == 40 || tick == 80 || tick == 120)) {
            int n = 2 + world.getDifficulty().getId();
            for (int i = 0; i < n; i++) {
                EntityZombieMinion minion = new EntityZombieMinion(world);
                double a = rand.nextDouble() * Math.PI * 2.0D;
                minion.setLocationAndAngles(posX + Math.cos(a) * width * 0.8D, posY, posZ + Math.sin(a) * width * 0.8D, rand.nextFloat() * 360.0F, 0.0F);
                minion.onInitialSpawn(world.getDifficultyForLocation(minion.getPosition()), null);
                world.spawnEntity(minion);
                MutantTitans.groundBurst(world, minion.posX, minion.posY, minion.posZ, 20);
            }
        }
    }

    // ------------------------------------------------------------------ the titan horde

    private boolean canCallHorde() {
        if (isSummonedGiant() || hordeCooldown > 0 || !(world instanceof net.minecraft.world.WorldServer))
            return false;
        horde.removeIf(id -> {
            Entity e = ((net.minecraft.world.WorldServer)world).getEntityFromUuid(id);
            return e == null || !e.isEntityAlive();
        });
        return horde.isEmpty();
    }

    /**
     * The roar calls up 2 Mutant Zombie Titans and 3 original Zombie Titans: they claw their way out of the
     * ground around it (their rise animation) and join the fight. Called-up titans drop nothing and can't roar
     * for more themselves.
     */
    private void callHorde() {
        hordeCooldown = HORDE_COOLDOWN;
        EntityLivingBase target = getAttackTarget();
        int total = HORDE_MUTANTS + HORDE_ORIGINALS;
        double base = rand.nextDouble() * Math.PI * 2.0D;
        for (int i = 0; i < total; i++) {
            EntityZombieTitan titan = i < HORDE_MUTANTS ? new EntityMutantZombieTitan(world) : new EntityZombieTitan(world);
            double a = base + Math.PI * 2.0D * i / total;
            double r = width * (1.6D + rand.nextDouble() * 0.6D);
            double x = posX + Math.cos(a) * r, z = posZ + Math.sin(a) * r;
            net.minecraft.util.math.BlockPos top = world.getHeight(new net.minecraft.util.math.BlockPos(x, posY, z));
            titan.setLocationAndAngles(x, top.getY(), z, (float)Math.toDegrees(Math.atan2(posZ - z, posX - x)) - 90.0F, 0.0F);
            titan.onInitialSpawn(world.getDifficultyForLocation(top), null);
            titan.setChild(false);
            titan.assignVillager(false);
            titan.markSummonedGiant();
            if (titan instanceof EntityMutantZombieTitan)
                ((EntityMutantZombieTitan)titan).lives = 0;
            titan.assignWaiting(true);
            world.spawnEntity(titan);
            titan.awaken();
            if (target != null && !(target instanceof EntityZombieTitan))
                titan.setAttackTarget(target);
            horde.add(titan.getUniqueID());
            MutantTitans.groundBurst(world, x, top.getY(), z, 160);
        }
        MutantTitans.play(this, "entity.mutant_zombie.roar", 60.0F, 0.45F);
        shakeUserCamerasNearby(16.0D);
    }

    // ------------------------------------------------------------------ the mutant's extra lives

    @Override
    public void hurt(double value) {
        if (!world.isRemote && getHealth() > 0.0F && getHealth() - value <= 0.0D && getLives() > 0 && !isBurning() && getAnimID() != ANIM_REVIVE) {
            lives--;
            setHealth((float)Math.max(1.0D, getMaxHealth() / 3.75D));
            grabbedId = -1;
            startAnim(ANIM_REVIVE);
            MutantTitans.play(this, "entity.mutant_zombie.death", 30.0F, 0.6F);
            return;
        }
        super.hurt(value);
    }

    @Override
    public void onKillCommand() {
        lives = 0; // like the mutant: /kill skips the extra lives
        super.onKillCommand();
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return getAnimID() == ANIM_REVIVE || super.isEntityInvulnerable(source);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        // like the mutant: can't be hurt by the creature it is holding
        if (grabbedId >= 0 && source.getTrueSource() != null && source.getTrueSource().getEntityId() == grabbedId)
            return false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("MutantLives", getLives());
        tag.setInteger("HordeCooldown", hordeCooldown);
        net.minecraft.nbt.NBTTagList list = new net.minecraft.nbt.NBTTagList();
        for (java.util.UUID id : horde)
            list.appendTag(net.minecraft.nbt.NBTUtil.createUUIDTag(id));
        tag.setTag("Horde", list);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        if (tag.hasKey("MutantLives"))
            lives = tag.getInteger("MutantLives");
        hordeCooldown = tag.getInteger("HordeCooldown");
        horde.clear();
        net.minecraft.nbt.NBTTagList list = tag.getTagList("Horde", 10);
        for (int i = 0; i < list.tagCount(); i++)
            horde.add(net.minecraft.nbt.NBTUtil.getUUIDFromTag(list.getCompoundTagAt(i)));
    }

    @Override
    public void onKillEntity(EntityLivingBase victim) {
        super.onKillEntity(victim);
        if (victim instanceof EntityPlayer)
            MutantTitans.play(this, "entity.mutant_zombie.roar", 20.0F, 0.8F);
    }
}
