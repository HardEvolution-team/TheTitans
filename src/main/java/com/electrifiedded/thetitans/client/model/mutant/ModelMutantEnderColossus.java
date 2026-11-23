package com.electrifiedded.thetitans.client.model.mutant;

import static com.electrifiedded.archaiclib.client.model.ModelAnim.PI;

import com.electrifiedded.archaiclib.client.model.ModelAnim;
import com.electrifiedded.archaiclib.client.model.ScalableModelRenderer;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantEnderColossus;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The four-armed Mutant Enderman rig (with fingers) grown for a titan, plus two-bone horns, mandible tusks,
 * back spines and knee spikes (texture 128x64 extended to 128x128).
 */
@SideOnly(Side.CLIENT)
public class ModelMutantEnderColossus extends ModelMutantTitan {

    static final class Arm {
        final ScalableModelRenderer arm;
        final ModelRenderer forearm, hand, thumb;
        final ModelRenderer[] finger = new ModelRenderer[3], foreFinger = new ModelRenderer[3];
        final boolean right;

        Arm(ModelBase m, ModelRenderer connect, boolean right) {
            this.right = right;
            arm = new ScalableModelRenderer(m, 92, 0);
            arm.mirror = !right;
            arm.addBox(-1.5F, 0.0F, -1.5F, 3, 22, 3, 0.1F);
            arm.setRotationPoint(right ? -4.0F : 4.0F, -14.0F, 0.0F);
            connect.addChild(arm);
            forearm = box(m, 104, 0, -1.5F, 0.0F, -1.5F, 3, 18, 3, 0.0F, !right);
            forearm.setRotationPoint(0.0F, 21.0F, 1.0F);
            arm.addChild(forearm);
            hand = new ModelRenderer(m);
            hand.setRotationPoint(0.0F, 17.5F, 0.0F);
            forearm.addChild(hand);
            for (int i = 0; i < 3; i++) {
                finger[i] = box(m, 76, 0, -0.5F, 0.0F, -0.5F, 1, i == 1 ? 6 : 5, 1, 0.6F, !right);
                finger[i].setRotationPoint(right ? -0.5F : 0.5F, 0.0F, i - 1.0F);
                foreFinger[i] = box(m, 76, 0, -0.5F, 0.0F, -0.5F, 1, i == 1 ? 6 : 5, 1, 0.59F, !right);
                foreFinger[i].setRotationPoint(0.0F, 0.5F + (i == 1 ? 6 : 5), 0.0F);
                hand.addChild(finger[i]);
                finger[i].addChild(foreFinger[i]);
            }
            thumb = box(m, 76, 0, -0.5F, 0.0F, -0.5F, 1, 5, 1, 0.6F, right);
            thumb.setRotationPoint(right ? 0.5F : -0.5F, 0.0F, -0.5F);
            hand.addChild(thumb);
        }

        void setAngles() {
            ModelAnim.reset(arm, forearm, hand, thumb);
            ModelAnim.reset(finger);
            ModelAnim.reset(foreFinger);
            float s = right ? 1.0F : -1.0F;
            arm.rotateAngleX = -PI / 6;
            arm.rotateAngleZ = s * PI / 6;
            forearm.rotateAngleX = -PI / 5;
            hand.rotateAngleY = -s * PI / 8;
            finger[0].rotateAngleX = -PI / 12;
            finger[1].rotateAngleZ = s * 0.17453294F;
            finger[2].rotateAngleX = PI / 12;
            for (int i = 0; i < 3; i++)
                foreFinger[i].rotateAngleZ = -s * (i == 1 ? PI / 8 : PI / 12);
            thumb.rotateAngleX = -PI / 5;
            thumb.rotateAngleZ = -s * PI / 8;
            arm.setScale(1.0F);
        }

        /** Raise / swing the arm by w * (x, y, z) (y and z mirrored for the left side). */
        void move(float w, float x, float y, float z) {
            float s = right ? 1.0F : -1.0F;
            ModelAnim.add(arm, w, x, s * y, s * z);
        }

        /** Curl (positive) or splay (negative) the fingers. */
        void grip(float w) {
            float s = right ? 1.0F : -1.0F;
            for (int i = 0; i < 3; i++) {
                finger[i].rotateAngleZ += s * w * 0.6F;
                foreFinger[i].rotateAngleZ += s * w * 0.9F;
            }
            thumb.rotateAngleZ -= s * w * 0.6F;
        }
    }

    private final ModelRenderer pelvis, abdomen, chest, neck, head, mouth, legjoint1, legjoint2, leg1, leg2, foreleg1, foreleg2;
    private final Arm rightArm, leftArm, lowerRightArm, lowerLeftArm;
    private final Arm[] arms;
    // titan additions
    private final ModelRenderer horn1, horn2, hornTip1, hornTip2, tusk1, tusk2, knee1, knee2;
    private final ModelRenderer[] spines = new ModelRenderer[3];

    public ModelMutantEnderColossus() {
        textureWidth = 128;
        textureHeight = 128;
        pelvis = new ModelRenderer(this);
        pelvis.setRotationPoint(0.0F, -15.5F, 8.0F);
        abdomen = box(this, 32, 0, -4.0F, -10.0F, -2.0F, 8, 10, 4, 0.0F, false);
        pelvis.addChild(abdomen);
        chest = box(this, 50, 8, -5.0F, -16.0F, -3.0F, 10, 16, 6, 0.0F, false);
        chest.setRotationPoint(0.0F, -8.0F, 0.0F);
        abdomen.addChild(chest);
        neck = box(this, 32, 14, -1.5F, -4.0F, -1.5F, 3, 4, 3, 0.0F, false);
        neck.setRotationPoint(0.0F, -15.0F, 0.0F);
        chest.addChild(neck);
        head = new ModelRenderer(this);
        head.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8, 6, 8, 0.5F);
        head.setTextureOffset(0, 14).addBox(-4.0F, 3.0F, -8.0F, 8, 2, 8, 0.5F);
        head.setRotationPoint(0.0F, -5.0F, 3.0F);
        neck.addChild(head);
        mouth = box(this, 0, 24, -4.0F, 3.0F, -8.0F, 8, 2, 8, 0.0F, false);
        head.addChild(mouth);
        rightArm = new Arm(this, chest, true);
        leftArm = new Arm(this, chest, false);
        lowerRightArm = new Arm(this, chest, true);
        lowerRightArm.arm.rotationPointY += 6.0F;
        lowerLeftArm = new Arm(this, chest, false);
        lowerLeftArm.arm.rotationPointY += 6.0F;
        arms = new Arm[] { rightArm, leftArm, lowerRightArm, lowerLeftArm };
        legjoint1 = new ModelRenderer(this);
        legjoint1.setRotationPoint(-1.5F, 0.0F, 0.75F);
        abdomen.addChild(legjoint1);
        legjoint2 = new ModelRenderer(this);
        legjoint2.setRotationPoint(1.5F, 0.0F, 0.75F);
        abdomen.addChild(legjoint2);
        leg1 = box(this, 0, 34, -1.5F, 0.0F, -1.5F, 3, 24, 3, 0.5F, false);
        leg1.setRotationPoint(0.0F, -2.0F, 0.0F);
        legjoint1.addChild(leg1);
        leg2 = box(this, 0, 34, -1.5F, 0.0F, -1.5F, 3, 24, 3, 0.5F, true);
        leg2.setRotationPoint(0.0F, -2.0F, 0.0F);
        legjoint2.addChild(leg2);
        foreleg1 = box(this, 12, 34, -1.5F, 0.0F, -1.5F, 3, 24, 3, 0.5F, false);
        foreleg1.setRotationPoint(0.0F, 23.0F, 0.0F);
        leg1.addChild(foreleg1);
        foreleg2 = box(this, 12, 34, -1.5F, 0.0F, -1.5F, 3, 24, 3, 0.5F, true);
        foreleg2.setRotationPoint(0.0F, 23.0F, 0.0F);
        leg2.addChild(foreleg2);

        // ---- titan additions
        horn1 = box(this, 0, 64, -1.0F, -5.0F, -1.0F, 2, 5, 2, 0.0F, false);
        horn1.setRotationPoint(-3.0F, -4.0F, -2.0F);
        head.addChild(horn1);
        horn2 = box(this, 0, 64, -1.0F, -5.0F, -1.0F, 2, 5, 2, 0.0F, true);
        horn2.setRotationPoint(3.0F, -4.0F, -2.0F);
        head.addChild(horn2);
        hornTip1 = box(this, 8, 64, -0.5F, -5.0F, -0.5F, 1, 5, 1, 0.0F, false);
        hornTip1.setRotationPoint(0.0F, -4.5F, 0.0F);
        horn1.addChild(hornTip1);
        hornTip2 = box(this, 8, 64, -0.5F, -5.0F, -0.5F, 1, 5, 1, 0.0F, true);
        hornTip2.setRotationPoint(0.0F, -4.5F, 0.0F);
        horn2.addChild(hornTip2);
        tusk1 = box(this, 12, 64, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.1F, false);
        tusk1.setRotationPoint(-3.5F, 4.5F, -7.5F);
        mouth.addChild(tusk1);
        tusk2 = box(this, 12, 64, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.1F, true);
        tusk2.setRotationPoint(3.5F, 4.5F, -7.5F);
        mouth.addChild(tusk2);
        for (int i = 0; i < spines.length; i++) {
            spines[i] = box(this, 16, 64, -0.5F, -6.0F, -0.5F, 1, 6, 1, 0.15F, false);
            spines[i].setRotationPoint(0.0F, -13.0F + i * 5.0F, 2.5F);
            chest.addChild(spines[i]);
        }
        knee1 = box(this, 20, 64, -0.5F, -4.0F, -0.5F, 1, 4, 1, 0.2F, false);
        knee1.setRotationPoint(0.0F, 22.5F, -1.0F);
        leg1.addChild(knee1);
        knee2 = box(this, 20, 64, -0.5F, -4.0F, -0.5F, 1, 4, 1, 0.2F, true);
        knee2.setRotationPoint(0.0F, 22.5F, -1.0F);
        leg2.addChild(knee2);

        style(1, PUNCH, 12, 30);
        style(2, CAST, 50, 100);
        style(3, CAST, 60, 110);
        style(4, THROW, 55, 110);
        style(5, ROAR, 80, 200);
        style(6, SWIPE, 32, 70);
        style(7, STOMP, 60, 150);
        style(8, STUN, 0, 400);
        style(9, SMASH, 26, 60);
        style(10, DEATH, 0, 2000);
        style(11, THROW, 55, 110);
        style(13, CAST, 40, 80);
        style(EntityMutantEnderColossus.ANIM_BARRAGE, SPECIAL1, EntityMutantEnderColossus.BARRAGE_TICKS[0], 90);
        style(EntityMutantEnderColossus.ANIM_TELESMASH, SPECIAL2, EntityMutantEnderColossus.TELE_SMASH, 70);
        style(EntityMutantEnderColossus.ANIM_SCREAM, ROAR, 18, 120);
        style(EntityMutantEnderColossus.ANIM_CLONES, SPECIAL3, 10, 40);
    }

    @Override
    protected void renderRoot(float scale) {
        pelvis.render(scale);
    }

    @Override
    protected void restPose() {
        pelvis.rotationPointY = -15.5F;
        abdomen.rotateAngleX = PI / 10;
        ModelAnim.rot(chest, PI / 8, 0.0F, 0.0F);
        ModelAnim.rot(neck, PI / 16, 0.0F, 0.0F);
        ModelAnim.rot(head, -PI / 4, 0.0F, 0.0F);
        mouth.rotateAngleX = 0.0F;
        for (Arm a : arms)
            a.setAngles();
        lowerRightArm.arm.rotateAngleX += 0.1F;
        lowerRightArm.arm.rotateAngleZ -= 0.2F;
        lowerLeftArm.arm.rotateAngleX += 0.1F;
        lowerLeftArm.arm.rotateAngleZ += 0.2F;
        legjoint1.rotateAngleX = 0.0F;
        legjoint2.rotateAngleX = 0.0F;
        ModelAnim.rot(leg1, -PI * 2.0F / 7.0F, 0.0F, PI / 12);
        ModelAnim.rot(leg2, -PI * 2.0F / 7.0F, 0.0F, -PI / 12);
        ModelAnim.rot(foreleg1, PI / 4, 0.0F, -0.1308997F);
        ModelAnim.rot(foreleg2, PI / 4, 0.0F, 0.1308997F);
        ModelAnim.rot(horn1, -0.6F, 0.0F, -0.5F);
        ModelAnim.rot(horn2, -0.6F, 0.0F, 0.5F);
        ModelAnim.rot(hornTip1, -0.5F, 0.0F, 0.3F);
        ModelAnim.rot(hornTip2, -0.5F, 0.0F, -0.3F);
        ModelAnim.rot(tusk1, -0.3F, 0.0F, 0.2F);
        ModelAnim.rot(tusk2, -0.3F, 0.0F, -0.2F);
        for (ModelRenderer s : spines)
            ModelAnim.rot(s, -0.8F, 0.0F, 0.0F);
        ModelAnim.rot(knee1, -0.5F, 0.0F, 0.0F);
        ModelAnim.rot(knee2, -0.5F, 0.0F, 0.0F);
    }

    @Override
    protected void idle(EntityTitan titan, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        float walkSpeed = 0.3F;
        float walkAnim1 = (sin((limbSwing - 0.8F) * walkSpeed) + 0.8F) * limbSwingAmount;
        float walkAnim2 = -(sin((limbSwing + 0.8F) * walkSpeed) - 0.8F) * limbSwingAmount;
        float walkAnim3 = (sin((limbSwing + 0.8F) * walkSpeed) - 0.8F) * limbSwingAmount;
        float walkAnim4 = -(sin((limbSwing - 0.8F) * walkSpeed) + 0.8F) * limbSwingAmount;
        float walk = sin(limbSwing * walkSpeed) * limbSwingAmount;
        float breatheAnim = titan.getHealth() <= 0.0F ? 0.0F : sin(ageInTicks * 0.15F);
        float faceYaw = yaw * PI / 180.0F;
        float facePitch = pitch * PI / 180.0F;
        head.rotateAngleX += facePitch * 0.5F;
        head.rotateAngleY += faceYaw * 0.7F;
        head.rotateAngleZ -= faceYaw * 0.7F;
        neck.rotateAngleX += facePitch * 0.3F;
        chest.rotateAngleX += facePitch * 0.2F;
        mouth.rotateAngleX += breatheAnim * 0.02F + 0.02F;
        neck.rotateAngleX -= breatheAnim * 0.02F;
        rightArm.arm.rotateAngleZ += breatheAnim * 0.004F;
        leftArm.arm.rotateAngleZ -= breatheAnim * 0.004F;
        for (Arm a : arms)
            a.grip(breatheAnim * (a == rightArm || a == leftArm ? 0.08F : 0.035F));
        pelvis.rotationPointY -= Math.abs(walk);
        chest.rotateAngleY -= walk * 0.06F;
        rightArm.arm.rotateAngleX -= walk * 0.6F;
        leftArm.arm.rotateAngleX += walk * 0.6F;
        rightArm.forearm.rotateAngleX -= walk * 0.2F;
        leftArm.forearm.rotateAngleX += walk * 0.2F;
        lowerRightArm.arm.rotateAngleX -= walk * 0.3F;
        lowerLeftArm.arm.rotateAngleX += walk * 0.3F;
        lowerRightArm.forearm.rotateAngleX -= walk * 0.1F;
        lowerLeftArm.forearm.rotateAngleX += walk * 0.1F;
        legjoint1.rotateAngleX += walkAnim1 * 0.6F;
        legjoint2.rotateAngleX += walkAnim2 * 0.6F;
        foreleg1.rotateAngleX += walkAnim3 * 0.3F;
        foreleg2.rotateAngleX += walkAnim4 * 0.3F;
        for (int i = 0; i < spines.length; i++)
            spines[i].rotateAngleX += sin(ageInTicks * 0.15F - i) * 0.05F;
    }

    @Override
    protected void animate(EntityTitan titan, int style, float t, int hit, int duration) {
        float[] w = ModelAnim.attack(t, hit, duration);
        switch (style) {
            case PUNCH:
            case SMASH: {
                boolean both = style == SMASH;
                rightArm.move(w[0], -2.4F, 0.3F, 0.4F);
                rightArm.move(w[1], -0.8F, -0.3F, -0.2F);
                rightArm.grip(w[0] + w[1]);
                if (both) {
                    leftArm.move(w[0], -2.4F, 0.3F, 0.4F);
                    leftArm.move(w[1], -0.8F, -0.3F, -0.2F);
                    leftArm.grip(w[0] + w[1]);
                }
                ModelAnim.add(chest, w[0], -0.2F, both ? 0.0F : 0.3F, 0.0F);
                ModelAnim.add(chest, w[1], 0.35F, both ? 0.0F : -0.4F, 0.0F);
                mouth.rotateAngleX += 0.4F * w[1];
                break;
            }
            case SWIPE:
                for (Arm a : new Arm[] { rightArm, lowerRightArm }) {
                    a.move(w[0], -1.3F, 1.2F, 0.8F);
                    a.move(w[1], -1.4F, -1.4F, 0.3F);
                    a.grip(-w[0] + w[1]);
                }
                ModelAnim.add(chest, w[0] - w[1], 0.0F, 0.4F, 0.0F);
                break;
            case STOMP:
                ModelAnim.add(legjoint1, w[0], -1.0F, 0.0F, 0.0F);
                ModelAnim.add(foreleg1, w[0], 0.5F, 0.0F, 0.0F);
                ModelAnim.add(legjoint1, w[1], 0.6F, 0.0F, 0.0F);
                for (Arm a : arms)
                    a.move(w[0], -0.6F, 0.0F, 0.6F);
                ModelAnim.add(chest, w[1], 0.25F, 0.0F, 0.0F);
                break;
            case ROAR: { // the mutant scream: everything flung wide, mouth agape, head thrashing
                float hold = ModelAnim.pulse(t, hit * 0.5F, hit, duration - 25.0F, duration);
                float wind = ModelAnim.pulse(t, 0.0F, hit * 0.5F, hit * 0.6F, hit);
                ModelAnim.add(chest, wind, 0.6F, 0.0F, 0.0F);
                for (Arm a : arms)
                    a.move(wind, 0.4F, 0.0F, -0.5F);
                ModelAnim.add(chest, hold, -0.45F, 0.0F, 0.0F);
                ModelAnim.add(head, hold, -0.6F, sin(t * 0.9F) * 0.25F, sin(t * 1.3F) * 0.15F);
                mouth.rotateAngleX += 0.9F * hold;
                for (Arm a : arms) {
                    a.move(hold, -0.6F, 0.0F, 1.1F);
                    a.grip(-hold);
                }
                ModelAnim.add(tusk1, hold, 0.0F, 0.0F, 0.4F);
                ModelAnim.add(tusk2, hold, 0.0F, 0.0F, -0.4F);
                break;
            }
            case CAST: {
                float hold = ModelAnim.pulse(t, 0.0F, hit, duration - 20.0F, duration);
                rightArm.move(hold, -2.8F, 0.0F, 0.5F);
                leftArm.move(hold, -2.8F, 0.0F, 0.5F);
                lowerRightArm.move(hold, -1.2F, 0.0F, 0.9F);
                lowerLeftArm.move(hold, -1.2F, 0.0F, 0.9F);
                for (Arm a : arms)
                    a.grip(-hold * (0.8F + 0.2F * sin(t * 0.7F)));
                ModelAnim.add(head, hold, -0.5F, 0.0F, 0.0F);
                mouth.rotateAngleX += 0.5F * hold;
                break;
            }
            case THROW:
                throwWith(rightArm, t, hit, 1.0F);
                throwWith(leftArm, t, hit, 1.0F);
                break;
            case STUN: {
                float in = ModelAnim.ease(t / 20.0F) * (1.0F - ModelAnim.ease(ModelAnim.window(t, duration - 20.0F, duration)));
                pelvis.rotationPointY += 14.0F * in;
                ModelAnim.addMirrored(leg1, leg2, in, -0.6F, 0.0F, 0.0F);
                ModelAnim.addMirrored(foreleg1, foreleg2, in, 1.2F, 0.0F, 0.0F);
                ModelAnim.add(chest, in, 0.5F, 0.0F, sin(t * 0.06F) * 0.2F);
                ModelAnim.add(head, in, 0.7F, 0.0F, 0.0F);
                for (Arm a : arms) {
                    a.move(in, 0.5F, 0.0F, -0.4F);
                    a.grip(-in * 0.5F);
                }
                mouth.rotateAngleX += 0.4F * in;
                break;
            }
            case DEATH:
                fall(ModelAnim.ease(t / 60.0F));
                break;
            case RISE:
                fall(1.0F - ModelAnim.ease(ModelAnim.window(t, duration * 0.4F, duration * 0.9F)));
                break;
            case SPECIAL1: { // four-armed block barrage
                int[] at = EntityMutantEnderColossus.BARRAGE_TICKS;
                Arm[] order = { rightArm, leftArm, lowerRightArm, lowerLeftArm };
                for (int i = 0; i < 4; i++)
                    throwWith(order[i], t, at[i], 1.0F);
                ModelAnim.add(chest, ModelAnim.pulse(t, 0.0F, 20.0F, duration - 20.0F, duration), -0.15F, 0.0F, 0.0F);
                break;
            }
            case SPECIAL2: { // tele-smash: hands raised, lifting the prey with telekinesis, then hurled down
                int lift = EntityMutantEnderColossus.TELE_LIFT;
                float raise = ModelAnim.pulse(t, 0.0F, lift, hit - 4.0F, hit);
                float slam = ModelAnim.pulse(t, hit - 4.0F, hit, hit + 6.0F, duration);
                for (Arm a : arms) {
                    a.move(raise, -2.6F, 0.0F, 0.3F);
                    a.grip(-raise + slam * 1.2F);
                    a.move(slam, -0.6F, 0.0F, -0.2F);
                }
                ModelAnim.add(head, raise, -0.6F, 0.0F, 0.0F);
                ModelAnim.add(chest, slam, 0.5F, 0.0F, 0.0F);
                mouth.rotateAngleX += 0.6F * slam;
                break;
            }
            case SPECIAL3: { // clone swarm: arms crossed, body flickers/shrinks for the blink
                float fold = ModelAnim.pulse(t, 0.0F, hit, hit + 15.0F, duration);
                for (Arm a : arms) {
                    a.move(fold, -0.9F, -0.8F, -0.6F);
                    a.grip(fold);
                }
                ModelAnim.add(chest, fold, 0.3F, 0.0F, 0.0F);
                ModelAnim.add(head, fold, 0.4F, 0.0F, 0.0F);
                break;
            }
            default:
                break;
        }
    }

    /** Pick a block from the ground (wind-up), hold it, hurl it at {@code at}. */
    private void throwWith(Arm a, float t, float at, float strength) {
        float reach = ModelAnim.pulse(t, at - 30.0F, at - 20.0F, at - 16.0F, at - 8.0F);
        float back = ModelAnim.pulse(t, at - 16.0F, at - 8.0F, at - 2.0F, at);
        float hurl = ModelAnim.pulse(t, at - 2.0F, at + 2.0F, at + 6.0F, at + 16.0F);
        a.move(reach * strength, 0.6F, 0.0F, 0.3F);
        a.grip(reach * 1.2F + back * 1.2F);
        a.move(back * strength, -3.0F, 0.2F, 0.2F);
        ModelAnim.add(a.forearm, back, -0.6F, 0.0F, 0.0F);
        a.move(hurl * strength, -1.1F, -0.2F, 0.0F);
        a.grip(-hurl);
    }

    private void fall(float f) {
        pelvis.rotationPointY += 30.0F * f;
        ModelAnim.addMirrored(leg1, leg2, f, -1.2F, 0.0F, 0.3F);
        ModelAnim.addMirrored(foreleg1, foreleg2, f, 2.0F, 0.0F, 0.0F);
        ModelAnim.add(abdomen, f, 0.4F, 0.0F, 0.0F);
        ModelAnim.add(chest, f, 0.6F, 0.0F, 0.2F);
        ModelAnim.add(head, f, 0.8F, 0.4F, 0.0F);
        for (Arm a : arms) {
            a.move(f, 0.6F, 0.0F, -0.6F);
            a.grip(-f * 0.6F);
        }
        mouth.rotateAngleX += 0.6F * f;
    }

    // ------------------------------------------------------------------ ground choreography

    @Override
    protected void downPose(EntityTitan titan, int kind, float t, float d, float age) {
        if (kind != DOWN_DEATH) {
            // (the colossus never sleeps; kneeling meditation, rising = unfolding)
            float k = kind == DOWN_DORMANT ? 1.0F : 1.0F - ph(t / d, 0.3F, 0.8F);
            fall(0.6F * k);
            for (Arm a : arms) {
                a.move(k, -0.7F, -0.6F, -0.5F);
                a.grip(k * 0.5F);
            }
            ModelAnim.add(head, k, 0.4F, 0.0F, 0.0F);
            return;
        }
        // agonised scream, four arms flung wide -> knees give way, every hand clawing at its skull ->
        // topples backwards -> fingers keep twitching
        float scream = hold(t, 0.0F, 6.0F, 22.0F, 30.0F);
        float kneel = ph(t, 24.0F, 46.0F);
        float topple = ph(t, 62.0F, 84.0F);
        topple *= topple;
        float wKneel = kneel * (1.0F - topple);
        root[1] -= 8.0F * scream;
        ModelAnim.add(chest, scream, -0.6F, 0.0F, 0.0F);
        ModelAnim.add(head, scream, -0.9F, sin(age * 1.5F) * 0.25F, 0.0F);
        mouth.rotateAngleX += 1.1F * scream + 0.7F * wKneel + 0.9F * topple;
        for (Arm a : arms) {
            a.move(scream, -1.0F, 0.0F, 1.1F);
            a.grip(-scream);
        }
        // kneeling, clutching the head
        fall(0.55F * kneel + 0.45F * topple);
        float writhe = sin(age * 0.9F) * 0.12F * wKneel;
        rightArm.move(wKneel, -2.7F, 0.3F, -0.1F + writhe);
        leftArm.move(wKneel, -2.7F, 0.3F, -0.1F - writhe);
        lowerRightArm.move(wKneel, -1.6F, 0.6F, -0.4F);
        lowerLeftArm.move(wKneel, -1.6F, 0.6F, -0.4F);
        for (Arm a : arms)
            a.grip(wKneel * 1.2F);
        ModelAnim.add(head, wKneel, 0.5F, 0.0F, writhe * 2.0F);
        // flat on its back, arms splayed
        root[1] -= 82.0F * topple;
        root[0] -= 5.0F * topple;
        for (Arm a : arms) {
            a.move(topple, -0.4F, 0.0F, 0.9F);
            a.grip(-topple * 0.5F);
        }
        ModelAnim.add(head, topple, -0.5F, 0.6F, 0.0F);
        float twitch = topple * (float)Math.exp(-Math.max(0.0F, t - 84.0F) / 60.0F);
        for (int i = 0; i < arms.length; i++)
            arms[i].grip(sin(age * (1.7F + i * 0.4F)) * 0.6F * twitch);
        for (int i = 0; i < spines.length; i++)
            spines[i].rotateAngleX += sin(age * 1.3F - i) * 0.2F * twitch;
    }

    /** Sets the lower arms' scale (they grow out when the titan is hurt badly). */
    public void lowerArmScale(float s) {
        lowerRightArm.arm.setScale(s);
        lowerLeftArm.arm.setScale(s);
    }
}
