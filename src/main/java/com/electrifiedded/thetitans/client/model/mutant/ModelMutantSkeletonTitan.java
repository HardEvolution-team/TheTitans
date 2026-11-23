package com.electrifiedded.thetitans.client.model.mutant;

import static com.electrifiedded.archaiclib.client.model.ModelAnim.PI;

import com.electrifiedded.archaiclib.client.model.ModelAnim;
import com.electrifiedded.archaiclib.client.model.ScalableModelRenderer;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantSkeletonTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The Mutant Skeleton rig grown for a titan: pelvis, waist, a FOUR-segment rib spine (the mutant has three) with
 * opening rib pairs, neck, head + jaw, shoulders, jointed limbs and the rib crossbow; titan additions are horns,
 * jaw fangs, shoulder spikes and a three-bone tail (texture 128x128 extended to 128x192).
 */
@SideOnly(Side.CLIENT)
public class ModelMutantSkeletonTitan extends ModelMutantTitan {

    /** Joint = pivot bone (outer) + box bone (inner), like the mutant's JointModelRenderer. */
    static final class Joint {
        final ModelRenderer outer, inner;

        Joint(ModelBase m, int u, int v, float x, float y, float z, int w, int h, int d, float grow, boolean mirror) {
            outer = new ModelRenderer(m);
            inner = box(m, u, v, x, y, z, w, h, d, grow, mirror);
            outer.addChild(inner);
        }
    }

    /** One rib segment: vertebra + three bones per side. */
    static final class Spine {
        final ModelRenderer middle;
        final ScalableModelRenderer[] side1 = new ScalableModelRenderer[3], side2 = new ScalableModelRenderer[3];

        Spine(ModelBase m) {
            middle = box(m, 50, 0, -2.5F, -4.0F, -2.0F, 5, 4, 4, 0.5F, false);
            side1[0] = rib(m, -6.0F, 0.25F, false);
            side1[0].setRotationPoint(-3.0F, -1.0F, 1.75F);
            middle.addChild(side1[0]);
            side2[0] = rib(m, 0.0F, 0.25F, true);
            side2[0].setRotationPoint(3.0F, -1.0F, 1.75F);
            middle.addChild(side2[0]);
            side1[1] = rib(m, -6.0F, 0.2F, true);
            side1[1].setRotationPoint(-6.5F, 0.0F, 0.0F);
            side1[0].addChild(side1[1]);
            side2[1] = rib(m, 0.0F, 0.2F, false);
            side2[1].setRotationPoint(6.5F, 0.0F, 0.0F);
            side2[0].addChild(side2[1]);
            side1[2] = rib(m, -6.0F, 0.15F, false);
            side1[2].setRotationPoint(-6.4F, 0.0F, 0.0F);
            side1[1].addChild(side1[2]);
            side2[2] = rib(m, 0.0F, 0.15F, true);
            side2[2].setRotationPoint(6.4F, 0.0F, 0.0F);
            side2[1].addChild(side2[2]);
        }

        private static ScalableModelRenderer rib(ModelBase m, float x, float grow, boolean mirror) {
            ScalableModelRenderer r = new ScalableModelRenderer(m, 32, 12);
            r.mirror = mirror;
            r.addBox(x, -2.0F, -2.0F, 6, 2, 2, grow);
            return r;
        }

        void setAngles(boolean middleSpine) {
            ModelAnim.reset(middle);
            ModelAnim.reset(side1);
            ModelAnim.reset(side2);
            middle.rotateAngleX = PI / 18.0F;
            side1[0].rotateAngleY = -PI / 4.5F;
            side2[0].rotateAngleY = PI / 4.5F;
            side1[1].rotateAngleY = -PI / 3.0F;
            side2[1].rotateAngleY = PI / 3.0F;
            side1[2].rotateAngleY = -PI / 3.5F;
            side2[2].rotateAngleY = PI / 3.5F;
            if (middleSpine)
                for (int i = 0; i < 3; i++) {
                    side1[i].rotateAngleY *= 0.98F;
                    side2[i].rotateAngleY *= 0.98F;
                }
            side1[0].setScale(1.0F);
            side2[0].setScale(1.0F);
        }

        /** Ribs spread wide open (1) / clamp shut (-1). */
        void open(float w) {
            side1[0].rotateAngleY -= w * 0.55F;
            side2[0].rotateAngleY += w * 0.55F;
            side1[1].rotateAngleY -= w * 0.35F;
            side2[1].rotateAngleY += w * 0.35F;
            side1[2].rotateAngleY -= w * 0.25F;
            side2[2].rotateAngleY += w * 0.25F;
            float s = 1.0F + Math.max(0.0F, w) * 0.6F;
            side1[0].setScale(s);
            side2[0].setScale(s);
        }
    }

    private final ModelRenderer skeleBase, pelvis, waist, neck, jaw, shoulder1, shoulder2;
    private final Joint head, arm1, arm2, forearm1, forearm2, leg1, leg2, foreleg1, foreleg2;
    private final Spine[] spine = new Spine[4];
    // crossbow (from the mutant rig)
    private final ModelRenderer armwear, bowMiddle, middle1, middle2, side1, side2, side3, side4, rope1, rope2;
    // titan additions
    private final ModelRenderer horn1, horn2, fang1, fang2, spike1, spike2;
    private final ModelRenderer[] tail = new ModelRenderer[3];

    public ModelMutantSkeletonTitan() {
        textureWidth = 128;
        textureHeight = 192;
        skeleBase = new ModelRenderer(this);
        skeleBase.setRotationPoint(0.0F, 3.0F, 0.0F);
        pelvis = box(this, 0, 16, -4.0F, -6.0F, -3.0F, 8, 6, 6, 0.0F, false);
        skeleBase.addChild(pelvis);
        waist = box(this, 32, 0, -2.5F, -8.0F, -2.0F, 5, 8, 4, 0.0F, false);
        waist.setRotationPoint(0.0F, -5.0F, 0.0F);
        pelvis.addChild(waist);
        for (int i = 0; i < spine.length; i++) {
            spine[i] = new Spine(this);
            spine[i].middle.setRotationPoint(0.0F, i == 0 ? -7.0F : -5.0F, 0.0F);
            (i == 0 ? waist : spine[i - 1].middle).addChild(spine[i].middle);
        }
        ModelRenderer top = spine[spine.length - 1].middle;
        neck = box(this, 64, 0, -1.5F, -4.0F, -1.5F, 3, 4, 3, 0.0F, false);
        neck.setRotationPoint(0.0F, -4.0F, 0.0F);
        top.addChild(neck);
        head = new Joint(this, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.4F, false);
        head.outer.setRotationPoint(0.0F, -4.0F, -1.0F);
        neck.addChild(head.outer);
        jaw = box(this, 72, 0, -4.0F, -3.0F, -8.0F, 8, 3, 8, 0.7F, false);
        jaw.setRotationPoint(0.0F, -0.2F, 3.5F);
        head.inner.addChild(jaw);
        shoulder1 = box(this, 28, 16, -4.0F, -3.0F, -3.0F, 8, 3, 6, 0.0F, false);
        shoulder1.setRotationPoint(-7.0F, -3.0F, -1.0F);
        top.addChild(shoulder1);
        shoulder2 = box(this, 28, 16, -4.0F, -3.0F, -3.0F, 8, 3, 6, 0.0F, true);
        shoulder2.setRotationPoint(7.0F, -3.0F, -1.0F);
        top.addChild(shoulder2);
        arm1 = new Joint(this, 0, 28, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false);
        arm1.outer.setRotationPoint(-1.0F, -1.0F, 0.0F);
        shoulder1.addChild(arm1.outer);
        arm2 = new Joint(this, 0, 28, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, true);
        arm2.outer.setRotationPoint(1.0F, -1.0F, 0.0F);
        shoulder2.addChild(arm2.outer);
        forearm1 = new Joint(this, 16, 28, -2.0F, 0.0F, -2.0F, 4, 14, 4, -0.01F, false);
        forearm1.outer.setRotationPoint(0.0F, 11.0F, 0.0F);
        arm1.inner.addChild(forearm1.outer);
        forearm2 = new Joint(this, 16, 28, -2.0F, 0.0F, -2.0F, 4, 14, 4, -0.01F, true);
        forearm2.outer.setRotationPoint(0.0F, 11.0F, 0.0F);
        arm2.inner.addChild(forearm2.outer);
        leg1 = new Joint(this, 0, 28, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false);
        leg1.outer.setRotationPoint(-2.5F, -2.5F, 0.0F);
        pelvis.addChild(leg1.outer);
        leg2 = new Joint(this, 0, 28, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, true);
        leg2.outer.setRotationPoint(2.5F, -2.5F, 0.0F);
        pelvis.addChild(leg2.outer);
        foreleg1 = new Joint(this, 32, 28, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false);
        foreleg1.outer.setRotationPoint(0.0F, 12.0F, 0.0F);
        leg1.inner.addChild(foreleg1.outer);
        foreleg2 = new Joint(this, 32, 28, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, true);
        foreleg2.outer.setRotationPoint(0.0F, 12.0F, 0.0F);
        leg2.inner.addChild(foreleg2.outer);

        armwear = box(this, 0, 64, -2.0F, -3.0F, -2.0F, 4, 6, 4, 0.3F, false);
        armwear.setRotationPoint(0.0F, 8.0F, 0.0F);
        forearm1.inner.addChild(armwear);
        bowMiddle = box(this, 16, 64, -2.0F, -2.0F, -3.0F, 4, 4, 6, 0.0F, false);
        bowMiddle.setRotationPoint(-3.5F, 0.0F, 0.0F);
        armwear.addChild(bowMiddle);
        middle1 = box(this, 36, 64, -1.5F, -1.5F, -3.0F, 3, 3, 6, 0.0F, false);
        middle1.setRotationPoint(0.0F, 0.6F, -4.0F);
        bowMiddle.addChild(middle1);
        middle2 = box(this, 36, 64, -1.5F, -1.5F, -3.0F, 3, 3, 6, 0.0F, false);
        middle2.setRotationPoint(0.0F, 0.6F, 4.0F);
        bowMiddle.addChild(middle2);
        side1 = box(this, 0, 74, -1.0F, -1.0F, -8.0F, 2, 2, 8, 0.0F, false);
        side1.setRotationPoint(0.0F, 0.0F, -2.0F);
        middle1.addChild(side1);
        side2 = box(this, 0, 74, -1.0F, -1.0F, 0.0F, 2, 2, 8, 0.0F, false);
        side2.setRotationPoint(0.0F, 0.0F, 2.0F);
        middle2.addChild(side2);
        side3 = box(this, 20, 74, -0.5F, -0.5F, -8.0F, 1, 1, 8, 0.0F, false);
        side3.setRotationPoint(0.0F, 0.0F, -5.0F);
        side1.addChild(side3);
        side4 = box(this, 20, 74, -0.5F, -0.5F, 0.0F, 1, 1, 8, 0.0F, false);
        side4.setRotationPoint(0.0F, 0.0F, 5.0F);
        side2.addChild(side4);
        rope1 = box(this, 0, 84, -0.5F, -0.5F, 0.0F, 1, 1, 15, -0.4F, false);
        rope1.setRotationPoint(0.0F, 0.0F, -6.0F);
        side3.addChild(rope1);
        rope2 = box(this, 0, 84, -0.5F, -0.5F, -15.0F, 1, 1, 15, -0.4F, false);
        rope2.setRotationPoint(0.0F, 0.0F, 6.0F);
        side4.addChild(rope2);

        // ---- titan additions
        horn1 = box(this, 0, 128, -1.0F, -7.0F, -1.0F, 2, 7, 2, 0.0F, false);
        horn1.setRotationPoint(-3.0F, -7.5F, 0.0F);
        head.inner.addChild(horn1);
        horn2 = box(this, 0, 128, -1.0F, -7.0F, -1.0F, 2, 7, 2, 0.0F, true);
        horn2.setRotationPoint(3.0F, -7.5F, 0.0F);
        head.inner.addChild(horn2);
        fang1 = box(this, 54, 128, -0.5F, -3.0F, -0.5F, 1, 3, 1, 0.1F, false);
        fang1.setRotationPoint(-2.8F, -2.6F, -7.2F);
        jaw.addChild(fang1);
        fang2 = box(this, 54, 128, -0.5F, -3.0F, -0.5F, 1, 3, 1, 0.1F, true);
        fang2.setRotationPoint(2.8F, -2.6F, -7.2F);
        jaw.addChild(fang2);
        spike1 = box(this, 48, 128, -0.5F, -5.0F, -0.5F, 1, 5, 1, 0.2F, false);
        spike1.setRotationPoint(-1.5F, -2.5F, 0.0F);
        shoulder1.addChild(spike1);
        spike2 = box(this, 48, 128, -0.5F, -5.0F, -0.5F, 1, 5, 1, 0.2F, true);
        spike2.setRotationPoint(1.5F, -2.5F, 0.0F);
        shoulder2.addChild(spike2);
        tail[0] = box(this, 10, 128, -1.5F, -1.5F, 0.0F, 3, 3, 6, 0.0F, false);
        tail[0].setRotationPoint(0.0F, -2.0F, 2.5F);
        pelvis.addChild(tail[0]);
        tail[1] = box(this, 10, 128, -1.5F, -1.5F, 0.0F, 3, 3, 6, -0.3F, false);
        tail[1].setRotationPoint(0.0F, 0.0F, 5.5F);
        tail[0].addChild(tail[1]);
        tail[2] = box(this, 30, 128, -1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F, false);
        tail[2].setRotationPoint(0.0F, 0.0F, 5.0F);
        tail[1].addChild(tail[2]);

        style(1, PUNCH, 12, 30);
        style(2, PUNCH, 24, 60);
        style(3, SMASH, 58, 100);
        style(4, SWIPE, 24, 60);
        style(5, SHOOT, 40, 400);
        style(6, STOMP, 60, 150);
        style(7, SMASH, 120, 260);
        style(8, STUN, 0, 540);
        style(9, CAST, 60, 130);
        style(10, DEATH, 0, 2000);
        style(11, SHOOT, 78, 120);
        style(12, CAST, 35, 70);
        style(13, RISE, 0, 540);
        style(EntityMutantSkeletonTitan.ANIM_CONSTRICT, SPECIAL1, EntityMutantSkeletonTitan.CONSTRICT_GRAB, 80);
        style(EntityMutantSkeletonTitan.ANIM_MULTISHOT, SPECIAL2, 34, 80);
        style(EntityMutantSkeletonTitan.ANIM_PIERCE, SHOOT, 40, 70);
        style(EntityMutantSkeletonTitan.ANIM_VOLLEY, SPECIAL3, EntityMutantSkeletonTitan.VOLLEY_START, EntityMutantSkeletonTitan.VOLLEY_DURATION);
    }

    @Override
    protected void renderRoot(float scale) {
        skeleBase.render(scale);
    }

    @Override
    protected void restPose() {
        skeleBase.rotationPointY = 3.0F;
        ModelAnim.reset(skeleBase, pelvis, waist, neck, jaw, shoulder1, shoulder2, head.outer, head.inner, arm1.outer, arm1.inner, arm2.outer, arm2.inner,
                forearm1.outer, forearm1.inner, forearm2.outer, forearm2.inner, leg1.outer, leg1.inner, leg2.outer, leg2.inner,
                foreleg1.outer, foreleg1.inner, foreleg2.outer, foreleg2.inner, armwear, bowMiddle);
        pelvis.rotateAngleX = -PI / 10;
        waist.rotateAngleX = PI / 14;
        for (int i = 0; i < spine.length; i++)
            spine[i].setAngles(i == 1 || i == 2);
        neck.rotateAngleX = -0.1308997F;
        head.outer.rotateAngleX = -0.1308997F;
        jaw.rotateAngleX = 0.09817477F;
        shoulder1.rotateAngleX = -PI / 4;
        shoulder2.rotateAngleX = -PI / 4;
        arm1.inner.rotateAngleX = PI / 6;
        arm1.inner.rotateAngleZ = PI / 10;
        arm2.inner.rotateAngleX = PI / 6;
        arm2.inner.rotateAngleZ = -PI / 10;
        forearm1.inner.rotateAngleX = -PI / 6;
        forearm2.inner.rotateAngleX = -PI / 6;
        leg1.outer.rotateAngleX = -PI / 12 - pelvis.rotateAngleX;
        leg1.outer.rotateAngleZ = PI / 16;
        leg2.outer.rotateAngleX = -PI / 12 - pelvis.rotateAngleX;
        leg2.outer.rotateAngleZ = -PI / 16;
        foreleg1.outer.rotateAngleZ = -0.1308997F;
        foreleg1.inner.rotateAngleX = PI / 10;
        foreleg2.outer.rotateAngleZ = 0.1308997F;
        foreleg2.inner.rotateAngleX = PI / 10;
        middle1.rotateAngleX = PI / 8.0F;
        middle2.rotateAngleX = -PI / 8.0F;
        side1.rotateAngleX = -PI / 5.0F;
        side2.rotateAngleX = PI / 5.0F;
        side3.rotateAngleX = -PI / 4.0F;
        side4.rotateAngleX = PI / 4.0F;
        ModelAnim.rot(horn1, -0.5F, 0.0F, -0.45F);
        ModelAnim.rot(horn2, -0.5F, 0.0F, 0.45F);
        ModelAnim.rot(fang1, 0.15F, 0.0F, 0.0F);
        ModelAnim.rot(fang2, 0.15F, 0.0F, 0.0F);
        ModelAnim.rot(spike1, 0.3F, 0.0F, -0.5F);
        ModelAnim.rot(spike2, 0.3F, 0.0F, 0.5F);
        ModelAnim.rot(tail[0], -0.7F, 0.0F, 0.0F);
        ModelAnim.rot(tail[1], 0.35F, 0.0F, 0.0F);
        ModelAnim.rot(tail[2], 0.35F, 0.0F, 0.0F);
    }

    private void rotateRope() {
        rope1.rotateAngleX = -(middle1.rotateAngleX + side1.rotateAngleX + side3.rotateAngleX);
        rope2.rotateAngleX = -(middle2.rotateAngleX + side2.rotateAngleX + side4.rotateAngleX);
    }

    /** Bends the crossbow (0 = rest, 1 = fully drawn). */
    private void draw(float d) {
        middle1.rotateAngleX += d * 0.25F;
        middle2.rotateAngleX -= d * 0.25F;
        side1.rotateAngleX += d * 0.3F;
        side2.rotateAngleX -= d * 0.3F;
        side3.rotateAngleX += d * 0.3F;
        side4.rotateAngleX -= d * 0.3F;
    }

    @Override
    protected void idle(EntityTitan titan, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        float walkAnim1 = sin(limbSwing * 0.5F);
        float walkAnim2 = sin(limbSwing * 0.5F - 1.1F);
        float breatheAnim = titan.getHealth() <= 0.0F ? 0.0F : sin(ageInTicks * 0.1F);
        float faceYaw = yaw * PI / 180.0F;
        float facePitch = pitch * PI / 180.0F;
        skeleBase.rotationPointY -= (-0.5F + Math.abs(walkAnim1)) * limbSwingAmount;
        spine[0].middle.rotateAngleY -= walkAnim1 * 0.06F * limbSwingAmount;
        arm1.outer.rotateAngleX -= walkAnim1 * 0.9F * limbSwingAmount;
        arm2.outer.rotateAngleX += walkAnim1 * 0.9F * limbSwingAmount;
        leg1.outer.rotateAngleX += (0.2F + walkAnim1) * limbSwingAmount;
        leg2.outer.rotateAngleX -= (-0.2F + walkAnim1) * limbSwingAmount;
        foreleg1.inner.rotateAngleX += (0.6F + walkAnim2) * 0.6F * limbSwingAmount;
        foreleg2.inner.rotateAngleX -= (-0.6F + walkAnim2) * 0.6F * limbSwingAmount;
        for (Spine s : spine) {
            s.side1[1].rotateAngleY += breatheAnim * 0.02F;
            s.side2[1].rotateAngleY -= breatheAnim * 0.02F;
        }
        head.outer.rotateAngleX -= breatheAnim * 0.02F;
        jaw.rotateAngleX += breatheAnim * 0.04F + 0.04F;
        arm1.outer.rotateAngleZ += breatheAnim * 0.025F;
        arm2.outer.rotateAngleZ -= breatheAnim * 0.025F;
        head.inner.rotateAngleX += facePitch;
        head.inner.rotateAngleY += faceYaw;
        tail[0].rotateAngleY += sin(ageInTicks * 0.05F) * 0.15F + walkAnim1 * 0.2F * limbSwingAmount;
        tail[1].rotateAngleY += sin(ageInTicks * 0.05F - 0.8F) * 0.2F;
        tail[2].rotateAngleY += sin(ageInTicks * 0.05F - 1.6F) * 0.25F;
        rotateRope();
    }

    @Override
    protected void animate(EntityTitan titan, int style, float t, int hit, int duration) {
        float[] w = ModelAnim.attack(t, hit, duration);
        switch (style) {
            case PUNCH:
                melee(remap(t, hit, duration, 5.0F, 14.0F));
                jaw.rotateAngleX += 0.3F * w[1];
                break;
            case SMASH:
                for (Spine s : spine)
                    s.middle.rotateAngleX += -0.12F * w[0] + 0.18F * w[1];
                ModelAnim.addMirrored(arm1.outer, arm2.outer, w[0], -2.8F, 0.0F, 0.3F);
                ModelAnim.addMirrored(arm1.outer, arm2.outer, w[1], -0.6F, 0.0F, -0.1F);
                ModelAnim.addMirrored(forearm1.inner, forearm2.inner, w[0], -0.6F, 0.0F, 0.0F);
                ModelAnim.add(waist, w[1], 0.5F, 0.0F, 0.0F);
                ModelAnim.addMirrored(leg1.outer, leg2.outer, w[1], -0.5F, 0.0F, 0.0F);
                ModelAnim.addMirrored(foreleg1.inner, foreleg2.inner, w[1], 0.7F, 0.0F, 0.0F);
                skeleBase.rotationPointY += 2.0F * w[1];
                jaw.rotateAngleX += 0.6F * (w[0] + w[1]);
                break;
            case SWIPE:
                for (Spine s : spine)
                    s.middle.rotateAngleY += 0.2F * w[0] - 0.25F * w[1];
                ModelAnim.add(arm2.outer, w[0], -1.3F, -0.9F, -0.8F);
                ModelAnim.add(arm2.outer, w[1], -1.4F, 1.6F, -0.2F);
                jaw.rotateAngleX += 0.3F * w[1];
                break;
            case STOMP:
                ModelAnim.add(leg1.outer, w[0], -1.3F, 0.0F, 0.1F);
                ModelAnim.add(foreleg1.inner, w[0], 1.2F, 0.0F, 0.0F);
                ModelAnim.add(waist, w[0], -0.3F, 0.0F, 0.0F);
                ModelAnim.addMirrored(arm1.outer, arm2.outer, w[0], -0.8F, 0.0F, 0.6F);
                ModelAnim.add(waist, w[1], 0.35F, 0.0F, 0.0F);
                ModelAnim.add(leg1.outer, w[1], 0.2F, 0.0F, 0.0F);
                jaw.rotateAngleX += 0.5F * (w[0] + w[1]);
                break;
            case SHOOT: {
                float aim = ModelAnim.pulse(t, 0.0F, 10.0F, duration - 15.0F, duration);
                float drawn = ModelAnim.ease(ModelAnim.window(t, 6.0F, hit)) * (t < hit + 2 ? 1.0F : 0.0F);
                if (duration > 200) // long volley: re-draws every 40 ticks
                    drawn = ModelAnim.ease((t % 40.0F) / 30.0F) * aim;
                ModelAnim.add(arm1.outer, aim, -1.45F, 0.0F, 0.0F);
                ModelAnim.add(arm1.inner, aim, -PI / 6, 0.2F, -PI / 10);
                ModelAnim.add(forearm1.inner, aim, PI / 6, 0.0F, 0.0F);
                ModelAnim.add(arm2.outer, aim, -1.3F, 0.6F, 0.0F);
                ModelAnim.add(forearm2.inner, aim, -0.9F * drawn, 0.0F, 0.0F);
                for (Spine s : spine)
                    s.middle.rotateAngleY += 0.12F * aim;
                draw(drawn);
                break;
            }
            case CAST: {
                float hold = ModelAnim.pulse(t, 0.0F, hit, duration - 20.0F, duration);
                ModelAnim.addMirrored(arm1.outer, arm2.outer, hold, -2.6F, 0.0F, 0.5F);
                ModelAnim.add(head.outer, hold, -0.6F, 0.0F, 0.0F);
                jaw.rotateAngleX += 0.7F * hold;
                for (Spine s : spine)
                    s.open(hold * 0.6F * (0.8F + 0.2F * sin(t * 0.6F)));
                break;
            }
            case STUN: {
                float in = ModelAnim.ease(t / 20.0F) * (1.0F - ModelAnim.ease(ModelAnim.window(t, duration - 20.0F, duration)));
                skeleBase.rotationPointY += 7.0F * in;
                ModelAnim.add(waist, in, 0.6F, 0.0F, 0.0F);
                for (Spine s : spine)
                    s.middle.rotateAngleX += 0.15F * in;
                ModelAnim.add(head.outer, in, 0.6F, 0.0F, sin(t * 0.08F) * 0.3F);
                ModelAnim.addMirrored(leg1.outer, leg2.outer, in, -0.9F, 0.0F, 0.0F);
                ModelAnim.addMirrored(foreleg1.inner, foreleg2.inner, in, 1.3F, 0.0F, 0.0F);
                jaw.rotateAngleX += 0.5F * in;
                break;
            }
            case DEATH:
                collapse(ModelAnim.ease(t / 50.0F));
                break;
            case RISE:
                collapse(1.0F - ModelAnim.ease(ModelAnim.window(t, duration * 0.4F, duration * 0.9F)));
                break;
            case SPECIAL1: // rib constriction
                constrict(t, hit, duration);
                break;
            case SPECIAL2: { // leaping multishot
                float crouch = ModelAnim.pulse(t, 0.0F, 12.0F, 14.0F, 18.0F);
                float air = ModelAnim.pulse(t, 14.0F, 22.0F, duration - 15.0F, duration);
                skeleBase.rotationPointY += 4.0F * crouch;
                ModelAnim.addMirrored(leg1.outer, leg2.outer, crouch, -0.8F, 0.0F, 0.0F);
                ModelAnim.addMirrored(foreleg1.inner, foreleg2.inner, crouch, 1.2F, 0.0F, 0.0F);
                ModelAnim.addMirrored(leg1.outer, leg2.outer, air, 0.3F, 0.0F, 0.2F);
                ModelAnim.add(arm1.outer, air, -1.45F, 0.0F, 0.0F);
                ModelAnim.add(arm1.inner, air, -PI / 6, 0.2F, -PI / 10);
                ModelAnim.add(forearm1.inner, air, PI / 6, 0.0F, 0.0F);
                ModelAnim.add(arm2.outer, air, -1.3F, 0.6F, 0.0F);
                float pull = t >= hit ? ModelAnim.ease(((t - hit) % 6.0F) / 5.0F) : 0.0F;
                ModelAnim.add(forearm2.inner, air, -0.9F * pull, 0.0F, 0.0F);
                draw(pull * air);
                break;
            }
            case SPECIAL3: { // giga volley: braced wide, crossbow levelled, each fan of bolts kicks the whole body back
                float aim = ModelAnim.pulse(t, 0.0F, 14.0F, duration - 16.0F, duration);
                float brace = ModelAnim.pulse(t, 0.0F, 18.0F, duration - 12.0F, duration);
                int rate = EntityMutantSkeletonTitan.VOLLEY_RATE;
                float since = t - hit;
                int shot = MathHelper.floor(since / rate);
                boolean firing = since >= 0.0F && shot < EntityMutantSkeletonTitan.VOLLEY_SHOTS;
                float ph = firing ? (since - shot * rate) / rate : 0.0F;
                float kick = firing ? (float)Math.exp(-ph * 5.0F) : 0.0F;      // recoil right after each shot
                float drawn = since < 0.0F ? ModelAnim.ease(ModelAnim.window(t, 6.0F, hit)) : firing ? ModelAnim.ease(Math.max(0.0F, ph - 0.25F) / 0.75F) : 0.0F;
                skeleBase.rotationPointY += 3.0F * brace;
                ModelAnim.addMirrored(leg1.outer, leg2.outer, brace, -0.5F, 0.0F, 0.35F);
                ModelAnim.addMirrored(foreleg1.inner, foreleg2.inner, brace, 0.8F, 0.0F, 0.0F);
                ModelAnim.add(waist, brace, 0.2F, 0.0F, 0.0F);
                ModelAnim.add(arm1.outer, aim, -1.5F - 0.45F * kick, 0.0F, 0.0F);
                ModelAnim.add(arm1.inner, aim, -PI / 6, 0.2F, -PI / 10);
                ModelAnim.add(forearm1.inner, aim, PI / 6 - 0.3F * kick, 0.0F, 0.0F);
                ModelAnim.add(arm2.outer, aim, -1.3F, 0.6F, 0.0F);
                ModelAnim.add(forearm2.inner, aim, -0.9F * drawn, 0.0F, 0.0F);
                for (Spine s : spine) {
                    s.middle.rotateAngleY += 0.12F * aim;
                    s.middle.rotateAngleX -= 0.08F * kick;
                }
                ModelAnim.add(head.outer, kick, -0.25F, 0.0F, 0.0F);
                jaw.rotateAngleX += 0.4F * kick;
                for (int i = 0; i < tail.length; i++)
                    tail[i].rotateAngleX -= 0.3F * kick;
                draw(drawn);
                break;
            }
            default:
                break;
        }
        rotateRope();
    }

    /** Mutant Skeleton melee: a backhand sweep of the bow arm (0..14, impact 5). */
    private void melee(float mt) {
        int fullTick = MathHelper.floor(mt);
        float pt = mt - fullTick;
        if (fullTick < 3) {
            float f = sin((fullTick + pt) / 3.0F * PI / 2.0F);
            for (Spine s : spine)
                s.middle.rotateAngleY += f * PI / 16.0F;
            arm1.outer.rotateAngleY += f * PI / 10.0F;
            arm1.outer.rotateAngleZ += f * PI / 4.0F;
            arm2.outer.rotateAngleZ += f * -PI / 16.0F;
        } else if (fullTick < 5) {
            float f = cos((fullTick - 3 + pt) / 2.0F * PI / 2.0F);
            for (Spine s : spine)
                s.middle.rotateAngleY += f * 0.5890486F - PI / 8;
            arm1.outer.rotateAngleY += f * 2.7307692F - 2.41661F;
            arm1.outer.rotateAngleZ += f * (PI * 3.0F / 8.0F) - PI / 8;
            arm2.outer.rotateAngleZ += -PI / 16;
        } else if (fullTick < 8) {
            for (Spine s : spine)
                s.middle.rotateAngleY += -PI / 8;
            arm1.outer.rotateAngleY += -2.41661F;
            arm1.outer.rotateAngleZ += -PI / 8;
            arm2.outer.rotateAngleZ += -PI / 16;
        } else if (fullTick < 14) {
            float f = cos((fullTick - 8 + pt) / 6.0F * PI / 2.0F);
            for (Spine s : spine)
                s.middle.rotateAngleY += f * -PI / 8.0F;
            arm1.outer.rotateAngleY += f * -PI / 1.3F;
            arm1.outer.rotateAngleZ += f * -PI / 8.0F;
            arm2.outer.rotateAngleZ += f * -PI / 16.0F;
        }
    }

    /** Ribs fan open while it lunges, snap shut on the victim at {@code grab}, crush, then release. */
    private void constrict(float t, int grab, int duration) {
        int release = EntityMutantSkeletonTitan.CONSTRICT_RELEASE;
        float opening = ModelAnim.ease(ModelAnim.window(t, 0.0F, grab - 4.0F)) * (1.0F - ModelAnim.ease(ModelAnim.window(t, grab - 4.0F, grab)));
        float closed = ModelAnim.ease(ModelAnim.window(t, grab - 4.0F, grab)) * (1.0F - ModelAnim.ease(ModelAnim.window(t, release, release + 8.0F)));
        float lunge = ModelAnim.pulse(t, 0.0F, grab, release, duration);
        float squeeze = closed * (0.85F + 0.15F * sin(t * 0.8F));
        for (int i = 0; i < spine.length; i++)
            spine[i].open(opening * 1.0F - squeeze * 0.35F);
        ModelAnim.add(waist, lunge, 0.13F + 0.2F * closed, 0.0F, 0.0F);
        ModelAnim.add(spine[0].middle, closed, PI / 12, 0.0F, 0.0F);
        ModelAnim.add(neck, closed, PI / 16, 0.0F, 0.0F);
        ModelAnim.addMirrored(arm1.outer, arm2.outer, lunge, 0.0F, 0.0F, PI * 2.0F / 7.0F);
        ModelAnim.addMirrored(arm1.outer, arm2.outer, closed, -0.9F, 0.4F, -0.6F);
        skeleBase.rotationPointY += lunge;
        ModelAnim.addMirrored(leg1.outer, leg2.outer, lunge, -PI / 7, 0.0F, 0.0F);
        ModelAnim.addMirrored(foreleg1.inner, foreleg2.inner, lunge, PI / 6, 0.0F, 0.0F);
        jaw.rotateAngleX += 0.8F * opening + 0.3F * squeeze;
    }

    // ------------------------------------------------------------------ ground choreography

    @Override
    protected void downPose(EntityTitan titan, int kind, float t, float d, float age) {
        if (kind == DOWN_DEATH) {
            // the death blow cracks it open: ribs burst outward, skull thrown back, bones rattle, knees buckle
            // (at DEATH_COLLAPSE it falls apart into giant bone entities and stops being drawn)
            float burst = ph(t, 0.0F, 8.0F);
            float buckle = ph(t, 8.0F, EntityMutantSkeletonTitan.DEATH_COLLAPSE);
            float shake = (0.3F + buckle) * 0.12F;
            for (int i = 0; i < spine.length; i++) {
                spine[i].open(burst * 1.2F);
                spine[i].middle.rotateAngleZ += sin(age * 2.7F + i * 1.7F) * shake;
                spine[i].middle.rotateAngleX -= 0.15F * burst;
            }
            ModelAnim.add(head.outer, burst, -0.9F, 0.0F, 0.3F);
            head.outer.rotateAngleY += sin(age * 3.1F) * shake;
            jaw.rotateAngleX += 1.0F * burst;
            ModelAnim.addMirrored(arm1.outer, arm2.outer, burst, -0.6F, 0.0F, 1.0F);
            arm1.outer.rotateAngleX += sin(age * 2.2F) * shake;
            arm2.outer.rotateAngleX += sin(age * 2.5F + 1.0F) * shake;
            collapse(buckle * 0.6F);
            root[1] -= 8.0F * burst * (1.0F - buckle);
            return;
        }
        float r = kind == DOWN_DORMANT ? 0.0F : t / d;
        float dt = r * d;
        float rattle = hold(r, 0.0F, 0.03F, 0.1F, 0.14F) + 0.5F * hold(r, 0.3F, 0.32F, 0.5F, 0.52F);
        float legs = ph(r, 0.12F, 0.30F);           // legs fold under, the pelvis lifts off the ground
        float stand = ph(r, 0.70F, 0.86F);          // straightens up
        float a1 = snap(dt, d * 0.50F, d * 0.07F);  // right arm clicks into the shoulder
        float a2 = snap(dt, d * 0.56F, d * 0.07F);  // left arm
        float sk = snap(dt, d * 0.64F, d * 0.08F);  // the skull snaps onto the neck
        collapse(1.0F - 0.45F * legs - 0.55F * stand);
        skeleBase.rotationPointY += 5.0F * (1.0F - legs);
        // the scattered heap: every bone splayed out
        float legScatter = 1.0F - legs;
        ModelAnim.addMirrored(leg1.outer, leg2.outer, legScatter, 0.4F, 0.0F, 1.0F);
        ModelAnim.addMirrored(foreleg1.inner, foreleg2.inner, legScatter, -1.2F, 0.0F, 0.0F);
        for (int i = 0; i < spine.length; i++) {
            float up = ph(r, 0.32F + i * 0.06F, 0.40F + i * 0.06F); // vertebra by vertebra
            float sc = 1.0F - up;
            spine[i].middle.rotateAngleZ += (i % 2 == 0 ? 0.55F : -0.6F) * sc + sin(age * 2.9F + i * 2.0F) * 0.12F * rattle;
            spine[i].middle.rotateAngleX += 0.25F * sc;
            spine[i].open(0.7F * sc);
        }
        ModelAnim.add(arm1.outer, 1.0F - a1, -0.4F, 0.3F, 1.3F);
        ModelAnim.add(arm2.outer, 1.0F - a2, -0.6F, -0.2F, -1.2F);
        arm1.outer.rotateAngleX += sin(age * 2.4F) * 0.15F * rattle;
        arm2.outer.rotateAngleX += sin(age * 2.0F + 2.0F) * 0.15F * rattle;
        ModelAnim.add(head.outer, 1.0F - sk, 0.6F, 0.5F, 1.1F);
        head.outer.rotateAngleZ += sin(age * 3.3F) * 0.2F * rattle;
        float clack = hold(r, 0.64F, 0.66F, 0.76F, 0.8F);
        jaw.rotateAngleX += 0.8F * (1.0F - sk) + Math.abs(sin(age * 1.4F)) * 0.6F * clack;
        ModelAnim.add(tail[0], 1.0F - legs, 0.5F, 0.0F, 0.6F);
        ModelAnim.add(tail[1], 1.0F - legs, 0.4F, 0.6F, 0.0F);
        // neck crack + tail whip once standing
        float crack = hold(r, 0.86F, 0.87F, 0.9F, 0.91F);
        head.inner.rotateAngleZ += 0.5F * crack;
        head.inner.rotateAngleY += sin(age * 1.6F) * 0.25F * crack;
        for (int i = 0; i < tail.length; i++)
            tail[i].rotateAngleY += sin(age * 0.8F - i) * 0.6F * crack;
        // the threat: crossbow up and drawn
        float aim = hold(r, 0.90F, 0.93F, 0.97F, 1.0F);
        ModelAnim.add(arm1.outer, aim, -1.45F, 0.0F, 0.0F);
        ModelAnim.add(arm1.inner, aim, -PI / 6, 0.2F, -PI / 10);
        ModelAnim.add(arm2.outer, aim, -1.3F, 0.6F, 0.0F);
        ModelAnim.add(forearm2.inner, aim, -0.9F, 0.0F, 0.0F);
        draw(aim);
        rotateRope();
    }

    /** Bones give way: knees buckle, the spine folds forward and the skeleton slumps into a heap. */
    private void collapse(float f) {
        skeleBase.rotationPointY += 18.0F * f;
        pelvis.rotateAngleX += 0.3F * f;
        ModelAnim.addMirrored(leg1.outer, leg2.outer, f, -1.5F, 0.0F, 0.35F);
        ModelAnim.addMirrored(foreleg1.inner, foreleg2.inner, f, 2.0F, 0.0F, 0.0F);
        for (int i = 0; i < spine.length; i++) {
            spine[i].middle.rotateAngleX += 0.3F * f;
            spine[i].middle.rotateAngleZ += (i % 2 == 0 ? 0.08F : -0.08F) * f;
        }
        ModelAnim.add(head.outer, f, 0.7F, 0.3F, 0.2F);
        ModelAnim.addMirrored(arm1.outer, arm2.outer, f, 0.5F, 0.0F, -0.4F);
        jaw.rotateAngleX += 0.7F * f;
        ModelAnim.add(tail[0], f, 0.6F, 0.0F, 0.0F);
    }
}
