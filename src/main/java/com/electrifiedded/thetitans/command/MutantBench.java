package com.electrifiedded.thetitans.command;

import java.util.List;
import java.util.function.Consumer;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.AIMutantTitanAnim;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantZombieTitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.world.World;

/** "/titanbench mutant": spawns the Mutant Titans with a punching bag each and forces every mutant move once. */
final class MutantBench {
    private MutantBench() {
    }

    private static final int[] MOVES = { 20, 21, 22, 23, 2 };
    private static final java.util.Map<Entity, java.util.Map<Integer, Integer>> PLAYED = new java.util.WeakHashMap<>();
    private static final java.util.Map<Entity, Integer> LAST = new java.util.WeakHashMap<>();
    private static final java.util.Map<Entity, Integer> NEXT = new java.util.WeakHashMap<>();

    static boolean FAR, KILL;
    private static final java.util.Map<Entity, Integer> KILLS = new java.util.WeakHashMap<>();

    static Entity target(World world, EntityTitan titan) {
        EntityIronGolem golem = new EntityIronGolem(world);
        golem.setLocationAndAngles(titan.posX + titan.width * (FAR ? 2.5D : 0.4D), titan.posY, titan.posZ, 0.0F, 0.0F);
        golem.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0E9D);
        golem.setHealth(1.0E9F);
        world.spawnEntity(golem);
        titan.setAttackTarget(golem);
        return golem;
    }

    static void tick(World world, List<Entity> spawned, int step, Consumer<String> log) {
        for (int i = 0; i + 1 < spawned.size(); i += 2) {
            Entity e = spawned.get(i);
            if (!(e instanceof EntityTitan) || !(e instanceof AIMutantTitanAnim.Host))
                continue;
            EntityTitan t = (EntityTitan)e;
            Entity golem = spawned.get(i + 1);
            if (golem instanceof EntityLivingBase && golem.isEntityAlive())
                t.setAttackTarget((EntityLivingBase)golem);
            if (KILL) {
                if (t.getHealth() > 0.0F && step > 20 && step % 5 == 0 && t.getAnimID() != 23) {
                    t.hurt(t.getMaxHealth() * 2.0D);
                    if (t.getHealth() <= 0.0F)
                        KILLS.merge(t, 1, Integer::sum);
                }
                if (step % 50 == 0)
                    log.accept(t.getClass().getSimpleName() + " step " + step + " hp=" + t.getHealth() + " deathTicks=" + t.deathTicks + " dead=" + t.isDead + " kills=" + KILLS.getOrDefault(t, 0)
                            + " bones=" + world.getEntities(com.electrifiedded.thetitans.entity.titan.mutant.EntityTitanBonePart.class, x -> true).size());
                continue;
            }
            if (step % 20 == 0 && t.getAnimID() != 0)
                log.accept(t.getClass().getSimpleName() + " step " + step + " anim=" + t.getAnimID() + " tick=" + t.getAnimTick()
                        + " zombieTitans=" + world.getEntities(com.electrifiedded.thetitans.entity.titan.EntityZombieTitan.class, x -> x != t).size()
                        + " thrownBlocks=" + world.getEntities(com.electrifiedded.thetitans.entity.titan.mutant.EntityTitanThrownBlock.class, x -> true).size()
                        + " giantArrows=" + world.getEntities(com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitanGiantArrow.class, x -> true).size());
            int cur = t.getAnimID();
            if (LAST.getOrDefault(t, 0) != cur && cur != 0)
                PLAYED.computeIfAbsent(t, k -> new java.util.TreeMap<>()).merge(cur, 1, Integer::sum);
            LAST.put(t, cur);
            if (t.getAnimID() == 0 && t.isEntityAlive() && step > 20) {
                int idx = NEXT.getOrDefault(t, 0);
                if (idx >= MOVES.length)
                    continue;
                NEXT.put(t, idx + 1);
                int move = MOVES[idx];
                if (move == 23 && t instanceof EntityMutantZombieTitan)
                    t.hurt(t.getHealth() + 1.0D); // burn a life -> revive animation
                else if (move == 2 && !(t instanceof EntityMutantZombieTitan))
                    continue;
                else if (move == 23 && t instanceof com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantCreeperTitan)
                    continue;
                else {
                    AnimationAPI.sendAnimPacket((com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity)t, move);
                    t.setAnimID(move);
                    t.setAnimTick(0);
                }
                log.accept(t.getClass().getSimpleName() + " forced move " + move + " anim=" + t.getAnimID() + " hp=" + String.format("%.3e", t.getHealth()));
            }
        }
    }

    static void report(List<Entity> spawned, Consumer<String> log) {
        for (int i = 0; i + 1 < spawned.size(); i += 2) {
            Entity e = spawned.get(i), g = spawned.get(i + 1);
            if (!(e instanceof EntityTitan))
                continue;
            EntityTitan t = (EntityTitan)e;
            double baseHp = -1.0D;
            float bw = 0, bh = 0;
            try {
                EntityTitan base = (EntityTitan)t.getClass().getSuperclass().getConstructor(World.class).newInstance(t.world);
                baseHp = base.fetchAmountHp();
                bw = base.width;
                bh = base.height;
            } catch (Throwable ignored) {
            }
            log.accept(String.format("%s: parent titan hp=%.3e (%.1fx%.1f) -> mutant x%.2f hp, x%.2f width, x%.2f height",
                    t.getClass().getSimpleName(), baseHp, bw, bh, t.fetchAmountHp() / baseHp, t.width / Math.max(0.01F, bw), t.height / Math.max(0.01F, bh)));
            String extra = t instanceof EntityMutantZombieTitan ? " lives=" + ((EntityMutantZombieTitan)t).getLives() : "";
            log.accept(t.getClass().getSimpleName() + " animations started (id=count): " + PLAYED.get(t));
            log.accept(String.format("%s alive=%s name='%s' maxHp=%.3e hp=%.3e size=%.1fx%.1f scale=%.1f anim=%d%s | target hp lost=%.3e",
                    t.getClass().getSimpleName(), t.isEntityAlive(), t.getName(), (double)t.getMaxHealth(), (double)t.getHealth(), t.width, t.height,
                    t.fetchScaleMultiplierGiant(), t.getAnimID(), extra,
                    g instanceof EntityLivingBase ? 1.0E9D - ((EntityLivingBase)g).getHealth() : -1.0D));
        }
    }
}
