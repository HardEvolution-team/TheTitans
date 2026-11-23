package com.electrifiedded.thetitans.entity.titan.mutant;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import net.minecraft.entity.ai.EntityAITasks;

/** Generic AnimationAPI task for the mutant titans: the titan itself implements the per-tick behaviour. */
public class AIMutantTitanAnim extends AIAnimation {

    /** Implemented by every mutant titan. */
    public interface Host {
        /** Called every tick while animation {@code id} plays (server side). */
        void mutantAnimTick(int id, int tick);

        /** Called once when the animation ends (normally or interrupted). */
        default void mutantAnimEnd(int id) {
        }
    }

    private final EntityTitan titan;
    private final int id, duration;
    private final boolean stunInterrupts;

    public AIMutantTitanAnim(EntityTitan titan, int id, int duration, boolean stunInterrupts) {
        super((IAnimatedEntity)titan);
        this.titan = titan;
        this.id = id;
        this.duration = duration;
        this.stunInterrupts = stunInterrupts;
    }

    @Override
    public int getAnimID() {
        return id;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (stunInterrupts && titan.stunnedIs)
            return false;
        return titan.getAnimTick() <= duration && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        ((Host)titan).mutantAnimTick(id, titan.getAnimTick());
    }

    @Override
    public void resetTask() {
        ((Host)titan).mutantAnimEnd(id);
        super.resetTask();
    }

    /** Removes the inherited animation tasks of the given classes (to replace them with mutant ones). */
    public static void removeTasks(EntityAITasks tasks, Class<?>... classes) {
        tasks.taskEntries.removeIf(e -> {
            for (Class<?> c : classes)
                if (c.isInstance(e.action))
                    return true;
            return false;
        });
    }
}
