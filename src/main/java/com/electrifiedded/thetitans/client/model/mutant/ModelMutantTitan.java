package com.electrifiedded.thetitans.client.model.mutant;

import java.util.HashMap;
import java.util.Map;

import com.electrifiedded.archaiclib.client.model.ModelAnim;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Base of the mutant titan rigs: every animation id the titan can play (inherited titan attacks and the new
 * mutant ones) is mapped to an animation "style" with its impact tick and duration, so the mutant body acts out
 * every move of the titan kit.
 */
@SideOnly(Side.CLIENT)
public abstract class ModelMutantTitan extends ModelBase {
    public static final int SMASH = 1, SWIPE = 2, STOMP = 3, ROAR = 4, CAST = 5, THROW = 6, STUN = 7, DEATH = 8,
            RISE = 9, SHOOT = 10, PUNCH = 11, SPECIAL1 = 12, SPECIAL2 = 13, SPECIAL3 = 14, SPECIAL4 = 15, REVIVE = 16;

    /** anim id -> {style, impact tick, duration} */
    private final Map<Integer, int[]> styles = new HashMap<>();
    protected float partialTick;

    protected void style(int animId, int style, int hit, int duration) {
        styles.put(animId, new int[] { style, hit, duration });
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.partialTick = partialTick;
    }

    @Override
    public final void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        EntityTitan titan = (EntityTitan)entity;
        restPose();
        int[] st = styles.get(titan.getAnimID());
        float calm = 1.0F;
        if (titan.getHealth() <= 0.0F && st == null)
            st = styles.get(10);
        Smooth sm = smooth.computeIfAbsent(titan.getEntityId(), k -> new Smooth());
        float age = titan.ticksExisted + partialTick;
        float dt = sm.lastAge < 0 ? 0.0F : Math.max(0.0F, Math.min(5.0F, age - sm.lastAge));
        sm.lastAge = age;
        // Ground choreography: sleeping, dying and rising are acted out by each mutant rig (bones + a root tilt
        // around the feet), so every mutant collapses and gets back up its own way.
        int down = -1;
        float downT = 0.0F, downD = 1.0F;
        int[] forced = POSE_TEST ? forcedPose(titan) : null;
        if (forced != null) {
            int[] fs = styles.get(forced[0]);
            if (forced[0] == 0)
                down = DOWN_DORMANT;
            else if (fs != null && (fs[0] == DEATH || fs[0] == RISE)) {
                down = fs[0] == DEATH ? DOWN_DEATH : DOWN_RISE;
                downT = forced[1];
                downD = fs[2];
            } else if (fs != null) {
                restPose();
                animate(titan, fs[0], forced[1], fs[1], fs[2]);
                idle(titan, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
                java.util.Arrays.fill(root, 0.0F);
                root[4] = 1.0F;
                renderTilted(scale);
                return;
            }
        } else if (titan.fetchWaiting() && titan.getAnimID() == 0 && titan.getHealth() > 0.0F)
            down = DOWN_DORMANT;
        else if (titan.getHealth() <= 0.0F || (st != null && st[0] == DEATH)) {
            down = DOWN_DEATH;
            // (a living titan frozen in the death anim = screenshot test: pose from the anim tick)
            downT = titan.getHealth() > 0.0F ? titan.getAnimTick() + partialTick : titan.deathTicks + partialTick;
            downD = st != null && st[0] == DEATH ? st[2] : 2000.0F;
        } else if (st != null && st[0] == RISE) {
            down = DOWN_RISE;
            downT = clock(sm, titan, dt);
            downD = st[2];
        }
        java.util.Arrays.fill(root, 0.0F);
        root[4] = 1.0F;
        if (down >= 0) {
            float age2 = titan.ticksExisted + partialTick;
            downPose(titan, down, downT, downD, age2);
            if (Boolean.getBoolean("thetitans.mutantdebug") && titan.ticksExisted % 40 == 0)
                System.out.println("[MutantModel] down=" + down + " t=" + downT);
            blend(sm, dt);
            renderTilted(scale);
            if (smooth.size() > 256)
                smooth.clear();
            return;
        }
        if (st != null) {
            float t = clock(sm, titan, dt);
            if (titan.getAnimTick() <= 0 && titan.getAnimID() == 0)
                t = 0.0F;
            calm = 1.0F - ModelAnim.ease(t / 8.0F);
            if (st[0] == DEATH || st[0] == STUN || st[0] == RISE || st[0] == REVIVE)
                calm = 0.0F;
            animate(titan, st[0], t, st[1], st[2]);
        }
        if (Boolean.getBoolean("thetitans.mutantdebug") && titan.ticksExisted % 40 == 0)
            System.out.println("[MutantModel] " + titan.getClass().getSimpleName() + " id=" + titan.getEntityId() + " anim=" + titan.getAnimID() + " tick=" + titan.getAnimTick() + " hp=" + titan.getHealth() + " dead=" + titan.isDead + " style=" + (st == null ? -1 : st[0]) + " pos=" + (int)titan.posX + "," + (int)titan.posY + "," + (int)titan.posZ);
        if (st == null)
            sm.animId = -1;
        idle(titan, limbSwing, limbSwingAmount * calm, ageInTicks, netHeadYaw * Math.max(calm, 0.25F), headPitch * calm);
        blend(sm, dt);
        renderTilted(scale);
        if (smooth.size() > 256)
            smooth.clear();
    }

    // ---------------------------------------------------------------- smoothing

    /** Per-entity client state: local animation clock and last rendered bone pose. */
    private static final class Smooth {
        int animId = -1;
        float clock;
        float lastAge = -1.0F;
        float[] pose;
    }

    /** Dev screenshot mode (-Dthetitans.posetest=true): a titan named "pose:<animId>:<tick>" is frozen in that pose. */
    private static final boolean POSE_TEST = Boolean.getBoolean("thetitans.posetest");

    private static int[] forcedPose(EntityTitan titan) {
        String n = titan.hasCustomName() ? titan.getCustomNameTag() : "";
        if (!n.startsWith("pose:"))
            return null;
        try {
            String[] p = n.split(":");
            return new int[] { Integer.parseInt(p[1]), Integer.parseInt(p[2]) };
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static final int DOWN_DORMANT = 0, DOWN_DEATH = 1, DOWN_RISE = 2;

    /**
     * Root transform filled by {@link #downPose}: {drop (px, + = down), pitch (deg, + = falls on its face),
     * roll (deg), pivot z (px, + = behind), scale}. Smoothed together with the bones.
     */
    protected final float[] root = new float[5];

    /**
     * Acts out a ground pose: {@link #DOWN_DORMANT} (sleeping, t = 0), {@link #DOWN_DEATH} (t = death ticks) or
     * {@link #DOWN_RISE} (t over duration d; must start from the dormant pose). Called after {@link #restPose()}.
     */
    protected abstract void downPose(EntityTitan titan, int kind, float t, float d, float age);

    private void renderTilted(float scale) {
        if (Math.abs(root[0]) + Math.abs(root[1]) + Math.abs(root[2]) < 0.001F && Math.abs(root[4] - 1.0F) < 0.001F) {
            renderRoot(scale);
            return;
        }
        net.minecraft.client.renderer.GlStateManager.pushMatrix();
        net.minecraft.client.renderer.GlStateManager.translate(0.0F, root[0] / 16.0F, 0.0F);
        net.minecraft.client.renderer.GlStateManager.translate(0.0F, 1.5F, root[3] / 16.0F);
        net.minecraft.client.renderer.GlStateManager.rotate(root[1], 1.0F, 0.0F, 0.0F);
        net.minecraft.client.renderer.GlStateManager.rotate(root[2], 0.0F, 0.0F, 1.0F);
        net.minecraft.client.renderer.GlStateManager.scale(root[4], root[4], root[4]);
        net.minecraft.client.renderer.GlStateManager.translate(0.0F, -1.5F, -root[3] / 16.0F);
        renderRoot(scale);
        net.minecraft.client.renderer.GlStateManager.popMatrix();
    }

    // ---------------------------------------------------------------- choreography helpers

    /** Eased 0..1 progress of t through [a, b]. */
    protected static float ph(float t, float a, float b) {
        return ModelAnim.ease(ModelAnim.window(t, a, b));
    }

    /** Fades in over [a, b] and out over [c, d]. */
    protected static float hold(float t, float a, float b, float c, float d) {
        return ModelAnim.pulse(t, a, b, c, d);
    }

    /** Linear blend. */
    protected static float mix(float a, float b, float f) {
        return a + (b - a) * f;
    }

    /** Overshoot settle: 0 -> 1 with a damped wobble (snap into place). */
    protected static float snap(float t, float a, float len) {
        if (t <= a)
            return 0.0F;
        float x = Math.min(1.0F, (t - a) / len);
        return 1.0F - (float)Math.exp(-6.0F * x) * MathHelper.cos(x * 9.0F);
    }

    private final Map<Integer, Smooth> smooth = new HashMap<>();

    /**
     * The anim tick arrives from the server through the data manager in irregular bursts, so tick+partial jumps
     * back and forth. Run a local clock at render rate and only nudge it toward the server value.
     */
    private float clock(Smooth sm, EntityTitan titan, float dt) {
        int id = titan.getAnimID();
        float server = titan.getAnimTick();
        if (sm.animId != id || Math.abs(server - sm.clock) > 4.0F) {
            sm.animId = id;
            sm.clock = server + partialTick;
            return sm.clock;
        }
        if (!titan.fetchWaiting())
            sm.clock += dt * (titan.isChild() ? 2.0F : 1.0F);
        sm.clock += (server + partialTick - sm.clock) * Math.min(1.0F, dt * 0.15F);
        if (sm.clock > server + 1.5F)
            sm.clock = server + 1.5F;
        return sm.clock;
    }

    /** Eases every bone toward its new pose so starts/ends of moves and style switches don't snap. */
    private void blend(Smooth sm, float dt) {
        int n = boxList.size() * 6 + root.length;
        float[] target = new float[n];
        System.arraycopy(root, 0, target, boxList.size() * 6, root.length);
        for (int i = 0; i < boxList.size(); i++) {
            ModelRenderer r = boxList.get(i);
            target[i * 6] = r.rotateAngleX;
            target[i * 6 + 1] = r.rotateAngleY;
            target[i * 6 + 2] = r.rotateAngleZ;
            target[i * 6 + 3] = r.rotationPointX;
            target[i * 6 + 4] = r.rotationPointY;
            target[i * 6 + 5] = r.rotationPointZ;
        }
        if (sm.pose == null || sm.pose.length != n) {
            sm.pose = target;
            return;
        }
        float a = 1.0F - (float)Math.exp(-dt / 1.6F);
        for (int i = 0; i < n; i++) {
            float d = target[i] - sm.pose[i];
            if (i < boxList.size() * 6 && i % 6 < 3)
                d = MathHelper.wrapDegrees(d * 57.29578F) / 57.29578F;
            sm.pose[i] += d * a;
        }
        for (int i = 0; i < boxList.size(); i++) {
            ModelRenderer r = boxList.get(i);
            r.rotateAngleX = sm.pose[i * 6];
            r.rotateAngleY = sm.pose[i * 6 + 1];
            r.rotateAngleZ = sm.pose[i * 6 + 2];
            r.rotationPointX = sm.pose[i * 6 + 3];
            r.rotationPointY = sm.pose[i * 6 + 4];
            r.rotationPointZ = sm.pose[i * 6 + 5];
        }
        System.arraycopy(sm.pose, boxList.size() * 6, root, 0, root.length);
    }

    /** Resets all bones to the mutant's rest pose. */
    protected abstract void restPose();

    /** Breathing, walking and head look (already damped while attacking). */
    protected abstract void idle(EntityTitan titan, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch);

    /** Acts out one animation style. {@code t} = tick + partial, {@code hit} = impact tick. */
    protected abstract void animate(EntityTitan titan, int style, float t, int hit, int duration);

    protected abstract void renderRoot(float scale);

    // ---------------------------------------------------------------- helpers

    /**
     * Remaps a titan timeline [0, hit, duration] onto an original mutant animation timeline
     * [0, mHit, mEnd] (piecewise linear), so the mutant's own animation curves can be replayed at titan pace.
     */
    protected static float remap(float t, float hit, float duration, float mHit, float mEnd) {
        if (t <= hit)
            return hit <= 0 ? mHit : t / hit * mHit;
        return mHit + Math.min(1.0F, (t - hit) / Math.max(1.0F, duration - hit)) * (mEnd - mHit);
    }

    protected static float sin(float f) {
        return MathHelper.sin(f);
    }

    protected static float cos(float f) {
        return MathHelper.cos(f);
    }

    protected static ModelRenderer box(ModelBase m, int u, int v, float x, float y, float z, int w, int h, int d, float grow, boolean mirror) {
        ModelRenderer r = new ModelRenderer(m, u, v);
        r.mirror = mirror;
        r.addBox(x, y, z, w, h, d, grow);
        return r;
    }
}
