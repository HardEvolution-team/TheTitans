package com.electrifiedded.thetitans.client.model.mutant;

import static com.electrifiedded.archaiclib.client.model.ModelAnim.PI;

import com.electrifiedded.archaiclib.client.model.ModelAnim;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantZombieTitan;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The Mutant Zombie rig (Mutant Creatures) grown for a titan: the original 12 bones and animation curves
 * (hammer-fist, roar, grab-throw, collapse &amp; revive) plus a lower jaw, finger claws, shoulder spurs, back
 * spikes and a spine ridge (texture: base 128x128 extended to 128x256, see RenderMutantZombieTitan).
 */
@SideOnly(Side.CLIENT)
public class ModelMutantZombieTitan extends ModelMutantTitan {
    private final ModelRenderer pelvis, waist, chest, head, arm1, arm2, forearm1, forearm2, leg1, leg2, foreleg1, foreleg2;
    // titan bones
    private final ModelRenderer jaw, ridge, spur1, spur2;
    private final ModelRenderer[] claws1 = new ModelRenderer[3], claws2 = new ModelRenderer[3], spikes = new ModelRenderer[3];
    private float pt;

    public ModelMutantZombieTitan() {
        textureWidth = 128;
        textureHeight = 256;
        pelvis = new ModelRenderer(this);
        pelvis.setRotationPoint(0.0F, 10.0F, 6.0F);
        waist = box(this, 0, 44, -7.0F, -16.0F, -6.0F, 14, 16, 12, 0.0F, false);
        pelvis.addChild(waist);
        chest = box(this, 0, 16, -12.0F, -12.0F, -8.0F, 24, 12, 16, 0.0F, false);
        chest.setRotationPoint(0.0F, -12.0F, 0.0F);
        waist.addChild(chest);
        head = box(this, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false);
        head.setRotationPoint(0.0F, -11.0F, -4.0F);
        chest.addChild(head);
        arm1 = box(this, 104, 0, -3.0F, 0.0F, -3.0F, 6, 16, 6, 0.0F, false);
        arm1.setRotationPoint(-11.0F, -8.0F, 2.0F);
        chest.addChild(arm1);
        arm2 = box(this, 104, 0, -3.0F, 0.0F, -3.0F, 6, 16, 6, 0.0F, true);
        arm2.setRotationPoint(11.0F, -8.0F, 2.0F);
        chest.addChild(arm2);
        forearm1 = box(this, 104, 22, -3.0F, 0.0F, -3.0F, 6, 16, 6, 0.1F, false);
        forearm1.setRotationPoint(0.0F, 14.0F, 0.0F);
        arm1.addChild(forearm1);
        forearm2 = box(this, 104, 22, -3.0F, 0.0F, -3.0F, 6, 16, 6, 0.1F, true);
        forearm2.setRotationPoint(0.0F, 14.0F, 0.0F);
        arm2.addChild(forearm2);
        leg1 = box(this, 80, 0, -3.0F, 0.0F, -3.0F, 6, 11, 6, 0.0F, false);
        leg1.setRotationPoint(-5.0F, -2.0F, 0.0F);
        pelvis.addChild(leg1);
        leg2 = box(this, 80, 0, -3.0F, 0.0F, -3.0F, 6, 11, 6, 0.0F, true);
        leg2.setRotationPoint(5.0F, -2.0F, 0.0F);
        pelvis.addChild(leg2);
        foreleg1 = box(this, 80, 17, -3.0F, 0.0F, -3.0F, 6, 8, 6, 0.1F, false);
        foreleg1.setRotationPoint(0.0F, 9.5F, 0.0F);
        leg1.addChild(foreleg1);
        foreleg2 = box(this, 80, 17, -3.0F, 0.0F, -3.0F, 6, 8, 6, 0.1F, true);
        foreleg2.setRotationPoint(0.0F, 9.5F, 0.0F);
        leg2.addChild(foreleg2);

        // ---- titan additions (UVs in the painted lower half of the canvas)
        jaw = box(this, 0, 128, -3.5F, 0.0F, -7.0F, 7, 3, 7, 0.0F, false);
        jaw.setRotationPoint(0.0F, -0.5F, 0.0F);
        head.addChild(jaw);
        for (int i = 0; i < 3; i++) {
            claws1[i] = box(this, 32, 128, -0.5F, 0.0F, -0.5F, 1, 5, 1, 0.15F, false);
            claws1[i].setRotationPoint(-2.0F + i * 2.0F, 15.5F, -2.2F);
            forearm1.addChild(claws1[i]);
            claws2[i] = box(this, 32, 128, -0.5F, 0.0F, -0.5F, 1, 5, 1, 0.15F, true);
            claws2[i].setRotationPoint(-2.0F + i * 2.0F, 15.5F, -2.2F);
            forearm2.addChild(claws2[i]);
            spikes[i] = box(this, 40, 128, -1.0F, -8.0F, -1.0F, 2, 8, 2, 0.0F, i == 2);
            spikes[i].setRotationPoint(-6.0F + i * 6.0F, -8.0F, 7.0F);
            chest.addChild(spikes[i]);
        }
        spur1 = box(this, 52, 128, -1.0F, -6.0F, -1.0F, 2, 6, 2, 0.0F, false);
        spur1.setRotationPoint(-1.0F, 1.0F, 0.0F);
        arm1.addChild(spur1);
        spur2 = box(this, 52, 128, -1.0F, -6.0F, -1.0F, 2, 6, 2, 0.0F, true);
        spur2.setRotationPoint(1.0F, 1.0F, 0.0F);
        arm2.addChild(spur2);
        ridge = box(this, 0, 140, -2.0F, -7.0F, 0.0F, 4, 14, 3, 0.0F, false);
        ridge.setRotationPoint(0.0F, -8.0F, 5.0F);
        waist.addChild(ridge);

        // the titan kit, acted out by the mutant body: {style, impact, duration}
        style(1, PUNCH, 12, 30);
        style(2, THROW, EntityMutantZombieTitan.THROW_GRAB_TICK, 70);
        style(3, SMASH, 30, 70);
        style(4, STOMP, 30, 60);
        style(5, CAST, 34, 110);
        style(6, STOMP, 60, 150);
        style(7, SMASH, 120, 230);
        style(8, STUN, 0, 180);
        style(9, SWIPE, 95, 190);
        style(10, DEATH, 0, 2000);
        style(11, ROAR, 20, 200);
        style(12, THROW, 30, 110);
        style(13, RISE, 0, 560);
        style(EntityMutantZombieTitan.ANIM_SMASH, SMASH, 24, 50);
        style(EntityMutantZombieTitan.ANIM_THROW, THROW, EntityMutantZombieTitan.THROW_GRAB_TICK, 70);
        style(EntityMutantZombieTitan.ANIM_ROAR, ROAR, 30, 200);
        style(EntityMutantZombieTitan.ANIM_REVIVE, REVIVE, 0, 140);
    }

    @Override
    protected void renderRoot(float scale) {
        pelvis.render(scale);
    }

    @Override
    protected void restPose() {
        pelvis.rotationPointY = 10.0F;
        waist.rotateAngleX = PI / 16;
        chest.rotateAngleX = PI / 6;
        chest.rotateAngleY = 0.0F;
        head.rotateAngleX = -0.71994835F;
        head.rotateAngleY = 0.0F;
        head.rotateAngleZ = 0.0F;
        ModelAnim.rot(arm1, -0.32724923F, 0.0F, PI / 8);
        ModelAnim.rot(arm2, -0.32724923F, 0.0F, -PI / 8);
        forearm1.rotateAngleX = -PI / 3;
        forearm2.rotateAngleX = -PI / 3;
        ModelAnim.rot(leg1, -PI / 4, 0.0F, 0.0F);
        ModelAnim.rot(leg2, -PI / 4, 0.0F, 0.0F);
        foreleg1.rotateAngleX = PI / 4;
        foreleg2.rotateAngleX = PI / 4;
        jaw.rotateAngleX = 0.12F;
        for (int i = 0; i < 3; i++) {
            ModelAnim.rot(claws1[i], -0.35F, 0.0F, (i - 1) * 0.15F);
            ModelAnim.rot(claws2[i], -0.35F, 0.0F, (i - 1) * 0.15F);
            ModelAnim.rot(spikes[i], -0.65F, 0.0F, (i - 1) * 0.35F);
        }
        ModelAnim.rot(spur1, 0.0F, 0.0F, -0.45F);
        ModelAnim.rot(spur2, 0.0F, 0.0F, 0.45F);
        ModelAnim.rot(ridge, -0.1F, 0.0F, 0.0F);
    }

    @Override
    protected void idle(EntityTitan titan, float f, float f1, float f2, float f3, float f4) {
        float walkAnim1 = (sin((f - 0.7F) * 0.4F) + 0.7F) * f1;
        float walkAnim2 = -(sin((f + 0.7F) * 0.4F) - 0.7F) * f1;
        float walkAnim = sin(f * 0.4F) * f1;
        float breatheAnim = sin(f2 * 0.1F);
        float faceYaw = f3 * PI / 180.0F;
        float facePitch = f4 * PI / 180.0F;
        if (titan.getHealth() <= 0.0F)
            breatheAnim = 0.0F;
        chest.rotateAngleX += breatheAnim * 0.02F;
        arm1.rotateAngleZ -= breatheAnim * 0.05F;
        arm2.rotateAngleZ += breatheAnim * 0.05F;
        jaw.rotateAngleX += breatheAnim * 0.03F + 0.03F;
        head.rotateAngleX += facePitch * 0.6F;
        head.rotateAngleY += faceYaw * 0.8F;
        head.rotateAngleZ -= faceYaw * 0.2F;
        chest.rotateAngleX += facePitch * 0.4F;
        chest.rotateAngleY += faceYaw * 0.2F;
        pelvis.rotationPointY += sin(f * 0.8F) * f1 * 0.5F;
        chest.rotateAngleY -= walkAnim * 0.1F;
        arm1.rotateAngleX -= walkAnim * 0.6F;
        arm2.rotateAngleX += walkAnim * 0.6F;
        leg1.rotateAngleX += walkAnim1 * 0.9F;
        leg2.rotateAngleX += walkAnim2 * 0.9F;
        for (int i = 0; i < 3; i++)
            spikes[i].rotateAngleX += breatheAnim * 0.03F;
    }

    @Override
    protected void animate(EntityTitan titan, int style, float t, int hit, int duration) {
        float[] w = ModelAnim.attack(t, hit, duration);
        switch (style) {
            case SMASH:
            case PUNCH: {
                float mt = remap(t, hit, duration, 12.0F, 25.0F);
                melee(mt);
                jaw.rotateAngleX += 0.5F * w[1] + 0.2F * w[0];
                break;
            }
            case ROAR: {
                float mt = remap(t, hit, duration, 10.0F, 120.0F);
                roar(mt);
                jaw.rotateAngleX += 0.9F * ModelAnim.pulse(mt, 5.0F, 12.0F, 70.0F, 80.0F);
                break;
            }
            case THROW: {
                int release = hit + 24;
                throwAnim(t, hit, release, duration);
                break;
            }
            case DEATH:
                death(Math.min(t * 0.5F, 100.0F));
                jaw.rotateAngleX += 0.6F * ModelAnim.ease(t / 30.0F);
                break;
            case REVIVE:
                death(t);
                break;
            case RISE:
                death(100.0F + ModelAnim.window(t, duration * 0.45F, duration * 0.9F) * 40.0F);
                break;
            case STOMP: {
                // rear up, then both fists and the right foot come down together
                ModelAnim.add(waist, w[0], -0.35F, 0.0F, 0.0F);
                ModelAnim.add(chest, w[0], -0.4F, 0.0F, 0.0F);
                ModelAnim.addMirrored(arm1, arm2, w[0], -2.6F, 0.0F, 0.2F);
                ModelAnim.add(leg1, w[0], -0.9F, 0.0F, 0.15F);
                ModelAnim.add(foreleg1, w[0], 0.6F, 0.0F, 0.0F);
                ModelAnim.add(waist, w[1], 0.5F, 0.0F, 0.0F);
                ModelAnim.add(chest, w[1], 0.5F, 0.0F, 0.0F);
                ModelAnim.addMirrored(arm1, arm2, w[1], -1.2F, 0.0F, -0.2F);
                ModelAnim.add(leg1, w[1], 0.3F, 0.0F, 0.0F);
                jaw.rotateAngleX += 0.5F * (w[0] + w[1]);
                break;
            }
            case SWIPE: {
                ModelAnim.add(chest, w[0], 0.0F, 0.7F, 0.0F);
                ModelAnim.add(arm1, w[0], -1.4F, 0.9F, 0.9F);
                ModelAnim.add(chest, w[1], 0.15F, -0.8F, 0.0F);
                ModelAnim.add(arm1, w[1], -1.5F, -1.4F, 0.4F);
                ModelAnim.add(forearm1, w[1], 0.5F, 0.0F, 0.0F);
                ModelAnim.add(arm2, w[0] + w[1], 0.4F, 0.0F, -0.3F);
                jaw.rotateAngleX += 0.4F * w[1];
                break;
            }
            case CAST: {
                float hold = ModelAnim.pulse(t, 0.0F, hit, duration - 25.0F, duration);
                ModelAnim.add(waist, hold, -0.3F, 0.0F, 0.0F);
                ModelAnim.add(chest, hold, -0.55F, 0.0F, 0.0F);
                ModelAnim.add(head, hold, -0.5F, 0.0F, 0.0F);
                ModelAnim.addMirrored(arm1, arm2, hold, -2.9F, 0.0F, 0.6F);
                ModelAnim.addMirrored(forearm1, forearm2, hold, 0.7F, 0.0F, 0.0F);
                float shake = sin(t * 1.3F) * 0.08F * hold;
                chest.rotateAngleZ += shake;
                jaw.rotateAngleX += 0.8F * hold;
                break;
            }
            case STUN: {
                float in = ModelAnim.ease(t / 20.0F) * (1.0F - ModelAnim.ease(ModelAnim.window(t, duration - 20.0F, duration)));
                pelvis.rotationPointY += 6.0F * in;
                ModelAnim.add(waist, in, 0.45F, 0.0F, 0.0F);
                ModelAnim.add(chest, in, 0.35F, sin(t * 0.08F) * 0.15F, 0.0F);
                ModelAnim.add(head, in, 0.6F, 0.0F, sin(t * 0.1F) * 0.2F);
                ModelAnim.addMirrored(arm1, arm2, in, 0.6F, 0.0F, -0.3F);
                ModelAnim.addMirrored(leg1, leg2, in, -0.4F, 0.0F, 0.0F);
                ModelAnim.addMirrored(foreleg1, foreleg2, in, 0.6F, 0.0F, 0.0F);
                jaw.rotateAngleX += 0.5F * in;
                break;
            }
            default:
                break;
        }
    }

    // ------------------------------------------------------------------ the Mutant Zombie's own curves

    private void at(float mt) {
        pt = mt - MathHelper.floor(mt);
    }

    private void melee(float mt) {
        at(mt);
        int fullTick = MathHelper.floor(mt);
        arm1.rotateAngleZ = 0.0F;
        arm2.rotateAngleZ = 0.0F;
        if (fullTick < 8) {
            float tick = (fullTick + pt) / 8.0F;
            float f = -sin(tick * PI / 2.0F);
            float f1 = cos(tick * PI / 2.0F);
            waist.rotateAngleX += f * 0.2F;
            chest.rotateAngleX += f * 0.2F;
            arm1.rotateAngleX += f * 2.3F;
            arm1.rotateAngleZ += f1 * PI / 8.0F;
            arm2.rotateAngleX += f * 2.3F;
            arm2.rotateAngleZ -= f1 * PI / 8.0F;
            forearm1.rotateAngleX += f * 0.8F;
            forearm2.rotateAngleX += f * 0.8F;
        } else if (fullTick < 12) {
            float tick = (fullTick - 8 + pt) / 4.0F;
            float f = -cos(tick * PI / 2.0F);
            float f1 = sin(tick * PI / 2.0F);
            waist.rotateAngleX += f * 0.9F + 0.7F;
            chest.rotateAngleX += f * 0.9F + 0.7F;
            arm1.rotateAngleX += f * 0.2F - 2.1F;
            arm1.rotateAngleZ += f1 * 0.3F;
            arm2.rotateAngleX += f * 0.2F - 2.1F;
            arm2.rotateAngleZ -= f1 * 0.3F;
            forearm1.rotateAngleX += f * 1.0F + 0.2F;
            forearm2.rotateAngleX += f * 1.0F + 0.2F;
        } else if (fullTick < 16) {
            waist.rotateAngleX += 0.7F;
            chest.rotateAngleX += 0.7F;
            arm1.rotateAngleX -= 2.1F;
            arm1.rotateAngleZ += 0.3F;
            arm2.rotateAngleX -= 2.1F;
            arm2.rotateAngleZ -= 0.3F;
            forearm1.rotateAngleX += 0.2F;
            forearm2.rotateAngleX += 0.2F;
        } else if (fullTick < 24) {
            float tick = (fullTick - 16 + pt) / 8.0F;
            float f = cos(tick * PI / 2.0F);
            waist.rotateAngleX += f * 0.7F;
            chest.rotateAngleX += f * 0.7F;
            arm1.rotateAngleX -= f * 2.1F;
            arm1.rotateAngleZ += f * -0.09269908F + PI / 8;
            arm2.rotateAngleX -= f * 2.1F;
            arm2.rotateAngleZ -= f * -0.09269908F + PI / 8;
            forearm1.rotateAngleX += f * 0.2F;
            forearm2.rotateAngleX += f * 0.2F;
        } else {
            arm1.rotateAngleZ += PI / 8;
            arm2.rotateAngleZ += -PI / 8;
        }
    }

    private void roar(float mt) {
        at(mt);
        int fullTick = MathHelper.floor(mt);
        if (fullTick < 10) {
            float tick = (fullTick + pt) / 10.0F;
            float f = sin(tick * PI / 2.0F);
            float f1 = sin(tick * PI * PI / 8.0F);
            waist.rotateAngleX += f * 0.2F;
            chest.rotateAngleX += f * 0.4F;
            chest.rotateAngleY += f1 * 0.06F;
            head.rotateAngleX += f * 0.8F;
            arm1.rotateAngleX -= f * 1.2F;
            arm1.rotateAngleZ += f * 0.6F;
            arm2.rotateAngleX -= f * 1.2F;
            arm2.rotateAngleZ -= f * 0.6F;
            forearm1.rotateAngleX -= f * 0.8F;
            forearm2.rotateAngleX -= f * 0.8F;
        } else if (fullTick < 15) {
            float tick = (fullTick - 10 + pt) / 5.0F;
            float f = cos(tick * PI / 2.0F);
            float f1 = sin(tick * PI / 2.0F);
            waist.rotateAngleX += f * 0.39634955F - PI / 16;
            chest.rotateAngleX += f * 0.6F - 0.2F;
            head.rotateAngleX += f * 1.0F - 0.2F;
            arm1.rotateAngleX -= f * 2.2F - 1.0F;
            arm1.rotateAngleY += f1 * 0.4F;
            arm1.rotateAngleZ += 0.6F;
            arm2.rotateAngleX -= f * 2.2F - 1.0F;
            arm2.rotateAngleY -= f1 * 0.4F;
            arm2.rotateAngleZ -= 0.6F;
            forearm1.rotateAngleX -= f * 1.0F - 0.2F;
            forearm2.rotateAngleX -= f * 1.0F - 0.2F;
            leg1.rotateAngleY += f1 * 0.3F;
            leg2.rotateAngleY -= f1 * 0.3F;
        } else if (fullTick < 75) {
            waist.rotateAngleX -= PI / 16;
            chest.rotateAngleX -= 0.2F;
            head.rotateAngleX -= 0.2F;
            ModelAnim.add(arm1, 1.0F, 1.0F, 0.4F, 0.6F);
            ModelAnim.add(arm2, 1.0F, 1.0F, -0.4F, -0.6F);
            forearm1.rotateAngleX += 0.2F;
            forearm2.rotateAngleX += 0.2F;
            leg1.rotateAngleY += 0.3F;
            leg2.rotateAngleY -= 0.3F;
        } else if (fullTick < 90) {
            float tick = (fullTick - 75 + pt) / 15.0F;
            float f = cos(tick * PI / 2.0F);
            waist.rotateAngleX -= f * 0.69634956F - 0.5F;
            chest.rotateAngleX -= f * 0.7F - 0.5F;
            head.rotateAngleX -= f * 0.6F - 0.4F;
            ModelAnim.add(arm1, 1.0F, f * 2.6F - 1.6F, f * 0.4F, f * 0.99269915F - PI / 8);
            ModelAnim.add(arm2, 1.0F, f * 2.6F - 1.6F, -f * 0.4F, -f * 0.99269915F + PI / 8);
            forearm1.rotateAngleX += f * -0.6F + 0.8F;
            forearm2.rotateAngleX += f * -0.6F + 0.8F;
            leg1.rotateAngleY += f * 0.3F;
            leg2.rotateAngleY -= f * 0.3F;
        } else if (fullTick < 110) {
            waist.rotateAngleX += 0.5F;
            chest.rotateAngleX += 0.5F;
            head.rotateAngleX += 0.4F;
            ModelAnim.add(arm1, 1.0F, -1.6F, 0.0F, -PI / 8);
            ModelAnim.add(arm2, 1.0F, -1.6F, 0.0F, PI / 8);
            forearm1.rotateAngleX += 0.8F;
            forearm2.rotateAngleX += 0.8F;
        } else {
            float tick = Math.min(1.0F, (fullTick - 110 + pt) / 10.0F);
            float f = cos(tick * PI / 2.0F);
            waist.rotateAngleX += f * 0.5F;
            chest.rotateAngleX += f * 0.5F;
            head.rotateAngleX += f * 0.4F;
            ModelAnim.add(arm1, 1.0F, f * -1.6F, 0.0F, f * -PI / 8.0F);
            ModelAnim.add(arm2, 1.0F, f * -1.6F, 0.0F, f * PI / 8.0F);
            forearm1.rotateAngleX += f * 0.8F;
            forearm2.rotateAngleX += f * 0.8F;
        }
        if (fullTick >= 10 && fullTick < 75) {
            float tick = (fullTick - 10 + pt) / 65.0F;
            float f = sin(tick * PI * 8.0F);
            float f1 = sin(tick * PI * 8.0F + PI / 4);
            head.rotateAngleY += f * 0.5F - f1 * 0.2F;
            head.rotateAngleZ -= f * 0.5F;
            chest.rotateAngleY += f1 * 0.06F;
        }
    }

    /** Mutant grab-throw: wind-up (attack tick 0..15), hold overhead, hurl (hit tick), recover (finish tick). */
    private void throwAnim(float t, int grab, int release, int duration) {
        float attackTick = grab <= 0 ? 15.0F : Math.min(15.0F, t / grab * 15.0F);
        float hitTick = t < release ? -1.0F : (t - release) * 5.0F / 8.0F;
        float finishTick = hitTick < 5.0F ? -1.0F : (t - release - 8.0F) * 10.0F / Math.max(1.0F, duration - release - 8.0F);
        at(attackTick);
        if (attackTick < 3) {
            float f = sin(attackTick / 3.0F * PI / 2.0F);
            chest.rotateAngleX -= f * 0.4F;
            arm1.rotateAngleX -= f * 1.8F;
            arm1.rotateAngleZ -= f * PI / 8.0F;
            arm2.rotateAngleX -= f * 1.8F;
            arm2.rotateAngleZ += f * PI / 8.0F;
        } else if (attackTick < 5) {
            chest.rotateAngleX -= 0.4F;
            arm1.rotateAngleX--;
            arm1.rotateAngleZ = 0.0F;
            arm2.rotateAngleX--;
            arm2.rotateAngleZ = 0.0F;
        } else if (attackTick < 8) {
            float tick = (attackTick - 5.0F) / 3.0F;
            float f = cos(tick * PI / 2.0F);
            float f1 = sin(tick * PI / 2.0F);
            waist.rotateAngleX += f1 * 0.2F;
            chest.rotateAngleX -= f * 0.6F - 0.2F;
            arm1.rotateAngleX -= f * 2.2F - 0.4F;
            arm1.rotateAngleZ -= f * PI / 8.0F;
            arm2.rotateAngleX -= f * 2.2F - 0.4F;
            arm2.rotateAngleZ += f * PI / 8.0F;
            forearm1.rotateAngleX -= f1 * 0.4F;
            forearm2.rotateAngleX -= f1 * 0.4F;
        } else if (attackTick < 10) {
            waist.rotateAngleX += 0.2F;
            chest.rotateAngleX += 0.2F;
            arm1.rotateAngleX += 0.4F;
            arm2.rotateAngleX += 0.4F;
            forearm1.rotateAngleX -= 0.4F;
            forearm2.rotateAngleX -= 0.4F;
        } else if (attackTick < 15) {
            float tick = (attackTick - 10.0F) / 5.0F;
            float f = cos(tick * PI / 2.0F);
            float f1 = sin(tick * PI / 2.0F);
            waist.rotateAngleX += f * 0.39634955F - PI / 16;
            chest.rotateAngleX += f * 0.8F - 0.6F;
            arm1.rotateAngleX += f * 3.0F - 2.6F;
            arm2.rotateAngleX += f * 3.0F - 2.6F;
            forearm1.rotateAngleX -= f * 0.4F;
            forearm2.rotateAngleX -= f * 0.4F;
            leg1.rotateAngleX += f1 * 0.6F;
            leg2.rotateAngleX += f1 * 0.6F;
        } else if (hitTick < 0) {
            waist.rotateAngleX -= PI / 16;
            chest.rotateAngleX -= 0.6F;
            arm1.rotateAngleX -= 2.6F;
            arm2.rotateAngleX -= 2.6F;
            leg1.rotateAngleX += 0.6F;
            leg2.rotateAngleX += 0.6F;
            jaw.rotateAngleX += 0.4F;
        } else if (hitTick < 5) {
            float tick = Math.min(1.0F, hitTick / 3.0F);
            float f = cos(tick * PI / 2.0F);
            float f1 = sin(tick * PI / 2.0F);
            waist.rotateAngleX -= f * 0.39634955F - 0.2F;
            chest.rotateAngleX -= f * 0.8F - 0.2F;
            ModelAnim.add(arm1, 1.0F, -(f * 2.2F + 0.4F), -f1 * PI / 8.0F, f1 * 0.4F);
            ModelAnim.add(arm2, 1.0F, -(f * 2.2F + 0.4F), f1 * PI / 8.0F, -f1 * 0.4F);
            forearm1.rotateAngleX += f1 * 0.2F;
            forearm2.rotateAngleX += f1 * 0.2F;
            leg1.rotateAngleX += f * 0.8F - 0.2F;
            leg2.rotateAngleX += f * 0.8F - 0.2F;
            jaw.rotateAngleX += 0.7F * f1;
        } else if (finishTick < 10) {
            float f = cos(Math.max(0.0F, finishTick) / 10.0F * PI / 2.0F);
            waist.rotateAngleX += f * 0.2F;
            chest.rotateAngleX += f * 0.2F;
            ModelAnim.add(arm1, 1.0F, -f * 0.4F, -f * PI / 8.0F, f * 0.4F);
            ModelAnim.add(arm2, 1.0F, -f * 0.4F, f * PI / 8.0F, -f * 0.4F);
            forearm1.rotateAngleX += f * 0.2F;
            forearm2.rotateAngleX += f * 0.2F;
            leg1.rotateAngleX -= f * 0.2F;
            leg2.rotateAngleX -= f * 0.2F;
        }
    }

    // ------------------------------------------------------------------ ground choreography

    @Override
    protected void downPose(EntityTitan titan, int kind, float t, float d, float age) {
        if (kind == DOWN_DEATH) {
            // stagger back roaring -> knees give -> keels over on its face -> twitches
            float stagger = hold(t, 0.0F, 6.0F, 12.0F, 22.0F);
            float knees = ph(t, 12.0F, 34.0F);
            float fall = ph(t, 42.0F, 60.0F);
            fall *= fall;
            float wStag = stagger * (1.0F - knees), wKneel = knees * (1.0F - fall);
            staggerPose(wStag);
            kneelPose(wKneel, true);
            ModelAnim.add(head, wKneel, -0.9F, 0.0F, 0.0F); // last howl at the sky
            jaw.rotateAngleX += 0.9F * wKneel + 1.0F * wStag;
            ModelAnim.addMirrored(arm1, arm2, wKneel, 0.5F, 0.0F, -0.2F);
            liePose(fall, false);
            float bounce = fall * (float)Math.exp(-Math.max(0.0F, t - 60.0F) / 6.0F) * MathHelper.sin(Math.max(0.0F, t - 60.0F) * 0.9F);
            root[0] -= 3.0F * Math.abs(bounce);
            float twitch = fall * (float)Math.exp(-Math.max(0.0F, t - 62.0F) / 45.0F) * (t > 62.0F ? 1.0F : 0.0F);
            forearm1.rotateAngleX += sin(age * 1.9F) * 0.25F * twitch;
            arm2.rotateAngleZ += sin(age * 1.3F + 1.0F) * 0.15F * twitch;
            for (int i = 0; i < 3; i++) {
                claws1[i].rotateAngleX += sin(age * 2.3F + i) * 0.5F * twitch;
                claws2[i].rotateAngleX += sin(age * 2.1F + i * 2) * 0.5F * twitch;
            }
            jaw.rotateAngleX += 0.5F * fall;
            return;
        }
        float r = kind == DOWN_DORMANT ? 0.0F : t / d;
        float push = ph(r, 0.24F, 0.42F);   // fists plant, the chest heaves off the ground
        float kneel = ph(r, 0.42F, 0.58F);  // drags the knees under itself
        float stand = ph(r, 0.62F, 0.80F);  // stands up
        float roar = hold(r, 0.82F, 0.86F, 0.95F, 0.99F); // chest-pounding roar
        // stirring: claws scrape, the head jerks up and drops again
        float stir = hold(r, 0.04F, 0.08F, 0.22F, 0.26F);
        liePose(1.0F - push, true);
        for (int i = 0; i < 3; i++) {
            claws1[i].rotateAngleX += sin(age * 0.7F + i) * 0.4F * stir;
            claws2[i].rotateAngleX += sin(age * 0.6F + i) * 0.4F * stir;
        }
        head.rotateAngleX -= Math.max(0.0F, sin(age * 0.12F)) * 0.6F * stir;
        forearm1.rotateAngleX -= Math.max(0.0F, sin(age * 0.09F)) * 0.5F * stir;
        // push-up
        float wPush = push * (1.0F - kneel);
        root[1] += 50.0F * wPush;
        root[0] -= 4.0F * wPush;
        ModelAnim.addMirrored(leg1, leg2, wPush, 0.9F, 0.0F, 0.0F);
        ModelAnim.addMirrored(foreleg1, foreleg2, wPush, -0.75F, 0.0F, 0.0F);
        ModelAnim.addMirrored(arm1, arm2, wPush, -1.1F, 0.0F, 0.15F);
        ModelAnim.addMirrored(forearm1, forearm2, wPush, 0.9F, 0.0F, 0.0F);
        ModelAnim.add(chest, wPush, -0.55F, 0.0F, sin(age * 0.25F) * 0.06F);
        ModelAnim.add(head, wPush, -0.5F, 0.0F, 0.0F);
        jaw.rotateAngleX += 0.4F * wPush;
        // one knee down, the other foot planted, a fist on the ground
        float wKneel = kneel * (1.0F - stand);
        kneelPose(wKneel, false);
        // standing up: strain, then straighten
        float strain = hold(r, 0.6F, 0.66F, 0.74F, 0.8F);
        ModelAnim.add(chest, strain, 0.3F, 0.0F, 0.0F);
        ModelAnim.add(head, strain, 0.4F, 0.0F, 0.0F);
        pelvis.rotationPointY += 3.0F * strain;
        // roar: lean back, arms flung wide, then the fists beat the chest
        float beat = Math.abs(sin(age * 0.55F));
        root[1] -= 6.0F * roar;
        ModelAnim.add(chest, roar, -0.5F, 0.0F, 0.0F);
        ModelAnim.add(head, roar, -0.9F, 0.0F, 0.0F);
        ModelAnim.add(arm1, roar, -1.6F - 0.6F * beat, 0.0F, 0.6F * (1.0F - beat));
        ModelAnim.add(arm2, roar, -1.6F - 0.6F * (1.0F - beat), 0.0F, -0.6F * beat);
        ModelAnim.addMirrored(forearm1, forearm2, roar, -0.6F, 0.0F, 0.0F);
        jaw.rotateAngleX += 1.1F * roar;
        for (int i = 0; i < 3; i++)
            spikes[i].rotateAngleX += 0.4F * roar;
    }

    /** Face down on the ground: legs stretched out, head turned aside, arms reaching ahead or flung aside. */
    private void liePose(float w, boolean reaching) {
        if (w <= 0.0F)
            return;
        root[1] += 84.0F * w;
        root[0] -= 7.0F * w;
        pelvis.rotationPointY -= 2.0F * w;
        ModelAnim.add(waist, w, -PI / 16, 0.0F, 0.0F);
        ModelAnim.add(chest, w, -0.4F, 0.0F, 0.0F);
        ModelAnim.add(head, w, 0.45F, 1.0F, 0.2F);
        ModelAnim.addMirrored(leg1, leg2, w, 0.95F, 0.0F, 0.12F);
        ModelAnim.addMirrored(foreleg1, foreleg2, w, -0.7F, 0.0F, 0.0F);
        if (reaching) {
            ModelAnim.add(arm1, w, -2.6F, 0.0F, 0.2F);
            ModelAnim.add(arm2, w, -2.2F, 0.0F, -0.45F);
            ModelAnim.addMirrored(forearm1, forearm2, w, 0.9F, 0.0F, 0.0F);
        } else {
            ModelAnim.addMirrored(arm1, arm2, w, -0.5F, 0.0F, 0.6F);
            ModelAnim.addMirrored(forearm1, forearm2, w, 0.7F, 0.0F, 0.0F);
        }
        jaw.rotateAngleX += 0.25F * w;
    }

    /** Kneeling: both knees down (dying) or one knee and a planted foot with a fist on the ground (rising). */
    private void kneelPose(float w, boolean both) {
        if (w <= 0.0F)
            return;
        pelvis.rotationPointY += 8.0F * w;
        root[1] += 12.0F * w;
        ModelAnim.add(leg1, w, 0.75F, 0.0F, 0.1F);
        ModelAnim.add(foreleg1, w, 0.85F, 0.0F, 0.0F);
        if (both) {
            ModelAnim.add(leg2, w, 0.75F, 0.0F, -0.1F);
            ModelAnim.add(foreleg2, w, 0.85F, 0.0F, 0.0F);
            ModelAnim.add(head, w, 0.3F, 0.0F, 0.0F);
        } else {
            ModelAnim.add(leg2, w, -0.75F, 0.0F, -0.1F);
            ModelAnim.add(foreleg2, w, 0.9F, 0.0F, 0.0F);
            ModelAnim.add(arm1, w, -0.5F, 0.0F, 0.0F);
            ModelAnim.add(forearm1, w, 0.8F, 0.0F, 0.0F);
            ModelAnim.add(arm2, w, 0.2F, 0.0F, -0.3F);
            ModelAnim.add(chest, w, 0.25F, -0.2F, 0.0F);
            ModelAnim.add(head, w, -0.3F, 0.0F, 0.0F);
        }
    }

    /** Recoils from the killing blow: rears back, arms thrown wide. */
    private void staggerPose(float w) {
        if (w <= 0.0F)
            return;
        root[1] -= 10.0F * w;
        ModelAnim.add(chest, w, -0.6F, 0.2F, 0.0F);
        ModelAnim.add(head, w, -0.7F, 0.0F, 0.2F);
        ModelAnim.addMirrored(arm1, arm2, w, -0.8F, 0.0F, 1.2F);
        ModelAnim.add(leg2, w, -0.4F, 0.0F, 0.0F);
    }

    /** Mutant collapse (0..20), lying (..100), getting back up (..140). */
    private void death(float deathTime) {
        float f;
        if (deathTime <= 20.0F)
            f = sin(Math.max(0.0F, deathTime - 1.0F) / 20.0F * PI / 2.0F);
        else if (deathTime <= 100.0F)
            f = 1.0F;
        else
            f = cos(Math.min(1.0F, (deathTime - 100.0F) / 40.0F) * PI / 2.0F);
        pelvis.rotationPointY += f * 28.0F;
        head.rotateAngleX -= f * PI / 10.0F;
        head.rotateAngleY += f * PI / 5.0F;
        chest.rotateAngleX -= f * PI / 12.0F;
        waist.rotateAngleX -= f * PI / 10.0F;
        arm1.rotateAngleX -= f * PI / 2.0F;
        arm1.rotateAngleY += f * PI / 2.8F;
        arm2.rotateAngleX -= f * PI / 2.0F;
        arm2.rotateAngleY -= f * PI / 2.8F;
        leg1.rotateAngleX += f * PI / 6.0F;
        leg1.rotateAngleZ += f * PI / 12.0F;
        leg2.rotateAngleX += f * PI / 6.0F;
        leg2.rotateAngleZ -= f * PI / 12.0F;
    }
}
