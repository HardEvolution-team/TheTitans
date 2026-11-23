package com.electrifiedded.thetitans.client.model.mutant;

import static com.electrifiedded.archaiclib.client.model.ModelAnim.PI;

import com.electrifiedded.archaiclib.client.model.ModelAnim;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantCreeperTitan;

import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The four-legged Mutant Creeper rig grown for a titan: the original 12 bones plus four dorsal vents, ten toe
 * claws, gill fins and a two-bone tail (texture 128x64 extended to 128x128).
 */
@SideOnly(Side.CLIENT)
public class ModelMutantCreeperTitan extends ModelMutantTitan {
    private final ModelRenderer pelvis, body, neck, head, frleg, flleg, frforeleg, flforeleg, brleg, blleg, brforeleg, blforeleg;
    private final ModelRenderer[] vents = new ModelRenderer[4], claws = new ModelRenderer[10], tail = new ModelRenderer[2];
    private final ModelRenderer fin1, fin2;
    private final float grow;

    public ModelMutantCreeperTitan() {
        this(0.0F);
    }

    /** {@code scale} &gt; 0 = the charged-aura shell. */
    public ModelMutantCreeperTitan(float scale) {
        grow = scale;
        textureWidth = 128;
        textureHeight = scale > 0.0F ? 64 : 128;
        pelvis = box(this, 0, 0, -5.0F, -14.0F, -4.0F, 10, 14, 8, scale, false);
        pelvis.setRotationPoint(0.0F, 14.0F, -3.0F);
        body = box(this, 36, 0, -4.5F, -14.0F, -3.5F, 9, 16, 7, scale, false);
        body.setRotationPoint(0.0F, -12.0F, 0.0F);
        pelvis.addChild(body);
        neck = box(this, 68, 0, -4.0F, -14.0F, -3.0F, 8, 14, 6, scale, false);
        neck.setRotationPoint(0.0F, -11.0F, 1.0F);
        body.addChild(neck);
        head = box(this, 0, 22, -5.0F, -12.0F, -5.0F, 10, 12, 10, scale, false);
        head.setRotationPoint(0.0F, -12.0F, 1.0F);
        neck.addChild(head);
        frleg = box(this, 40, 24, -3.0F, -4.0F, -14.0F, 6, 4, 14, scale, false);
        frleg.setRotationPoint(3.0F, 0.0F, 0.0F);
        pelvis.addChild(frleg);
        flleg = box(this, 40, 24, -3.0F, -4.0F, -14.0F, 6, 4, 14, scale, true);
        flleg.setRotationPoint(-3.0F, 0.0F, 0.0F);
        pelvis.addChild(flleg);
        frforeleg = box(this, 96, 0, -3.5F, 0.0F, -4.0F, 7, 20, 8, scale, false);
        frforeleg.setRotationPoint(0.0F, -4.0F, -14.0F);
        frleg.addChild(frforeleg);
        flforeleg = box(this, 96, 0, -3.5F, 0.0F, -4.0F, 7, 20, 8, scale, true);
        flforeleg.setRotationPoint(0.0F, -4.0F, -14.0F);
        flleg.addChild(flforeleg);
        brleg = box(this, 0, 44, -2.0F, -4.0F, 0.0F, 4, 4, 14, scale, false);
        brleg.setRotationPoint(2.0F, -2.0F, 4.0F);
        pelvis.addChild(brleg);
        blleg = box(this, 0, 44, -2.0F, -4.0F, 0.0F, 4, 4, 14, scale, true);
        blleg.setRotationPoint(-2.0F, -2.0F, 4.0F);
        pelvis.addChild(blleg);
        brforeleg = box(this, 80, 28, -3.0F, 0.0F, -3.0F, 6, 18, 6, scale, false);
        brforeleg.setRotationPoint(0.0F, -4.0F, 14.0F);
        brleg.addChild(brforeleg);
        blforeleg = box(this, 80, 28, -3.0F, 0.0F, -3.0F, 6, 18, 6, scale, true);
        blforeleg.setRotationPoint(0.0F, -4.0F, 14.0F);
        blleg.addChild(blforeleg);

        // ---- titan additions
        for (int i = 0; i < 4; i++) {
            vents[i] = box(this, 0, 64, -1.0F, -6.0F, -1.0F, 2, 6, 2, scale, i % 2 == 1);
            vents[i].setRotationPoint(i % 2 == 0 ? -1.5F : 1.5F, -12.0F + i * 3.5F, 3.0F);
            body.addChild(vents[i]);
        }
        ModelRenderer[] feet = { frforeleg, frforeleg, frforeleg, flforeleg, flforeleg, flforeleg, brforeleg, brforeleg, blforeleg, blforeleg };
        float[] cx = { -2.5F, 0.0F, 2.5F, -2.5F, 0.0F, 2.5F, -1.8F, 1.8F, -1.8F, 1.8F };
        for (int i = 0; i < claws.length; i++) {
            claws[i] = box(this, 8, 64, -0.5F, -1.0F, -3.0F, 1, 2, 3, scale, i >= 3 && i < 6 || i >= 8);
            claws[i].setRotationPoint(cx[i], feet[i] == brforeleg || feet[i] == blforeleg ? 17.0F : 19.0F, feet[i] == brforeleg || feet[i] == blforeleg ? -2.5F : -3.5F);
            feet[i].addChild(claws[i]);
        }
        tail[0] = box(this, 16, 64, -2.0F, -2.0F, 0.0F, 4, 4, 8, scale, false);
        tail[0].setRotationPoint(0.0F, -2.0F, 3.5F);
        pelvis.addChild(tail[0]);
        tail[1] = box(this, 40, 64, -1.5F, -1.5F, 0.0F, 3, 3, 7, scale, false);
        tail[1].setRotationPoint(0.0F, 0.0F, 7.5F);
        tail[0].addChild(tail[1]);
        fin1 = box(this, 60, 64, -1.0F, -6.0F, 0.0F, 1, 6, 4, scale, false);
        fin1.setRotationPoint(-5.0F, -3.0F, 0.0F);
        head.addChild(fin1);
        fin2 = box(this, 60, 64, 0.0F, -6.0F, 0.0F, 1, 6, 4, scale, true);
        fin2.setRotationPoint(5.0F, -3.0F, 0.0F);
        head.addChild(fin2);

        style(1, PUNCH, 24, 60);
        style(2, STOMP, 60, 150);
        style(3, SMASH, 90, 170);
        style(4, PUNCH, 32, 70);
        style(5, SWIPE, 30, 60);
        style(6, ROAR, 30, 60);
        style(7, CAST, 100, 230);
        style(8, STUN, 0, 520);
        style(10, DEATH, 0, 2000);
        style(13, RISE, 0, 480);
        style(EntityMutantCreeperTitan.ANIM_POUNCE, SPECIAL1, EntityMutantCreeperTitan.POUNCE_LEAP, 90);
        style(EntityMutantCreeperTitan.ANIM_CHARGE, SPECIAL2, 20, 140);
        style(EntityMutantCreeperTitan.ANIM_BROOD, SPECIAL3, 30, 60);
    }

    @Override
    protected void renderRoot(float scale) {
        pelvis.render(scale);
    }

    @Override
    protected void restPose() {
        pelvis.rotationPointY = 14.0F;
        ModelAnim.rot(pelvis, -PI / 4, 0.0F, 0.0F);
        ModelAnim.rot(body, PI * 3.0F / 10.0F, 0.0F, 0.0F);
        ModelAnim.rot(neck, PI / 3, 0.0F, 0.0F);
        ModelAnim.rot(head, PI / 6, 0.0F, 0.0F);
        ModelAnim.rot(frleg, PI / 10, -PI / 4, 0.0F);
        ModelAnim.rot(flleg, PI / 10, PI / 4, 0.0F);
        ModelAnim.rot(frforeleg, -PI / 15, PI / 8, 0.0F);
        ModelAnim.rot(flforeleg, -PI / 15, -PI / 8, 0.0F);
        ModelAnim.rot(brleg, 0.9F, PI / 5, 0.0F);
        ModelAnim.rot(blleg, 0.9F, -PI / 5, 0.0F);
        ModelAnim.rot(brforeleg, 0.48332196F, 0.0F, 0.0F);
        ModelAnim.rot(blforeleg, 0.48332196F, 0.0F, 0.0F);
        for (int i = 0; i < vents.length; i++)
            ModelAnim.rot(vents[i], -0.9F, 0.0F, i % 2 == 0 ? -0.25F : 0.25F);
        for (ModelRenderer c : claws)
            ModelAnim.rot(c, 0.15F, 0.0F, 0.0F);
        ModelAnim.rot(tail[0], -0.4F, 0.0F, 0.0F);
        ModelAnim.rot(tail[1], 0.3F, 0.0F, 0.0F);
        ModelAnim.rot(fin1, -0.3F, 0.0F, -0.35F);
        ModelAnim.rot(fin2, -0.3F, 0.0F, 0.35F);
    }

    @Override
    protected void idle(EntityTitan titan, float f, float f1, float f2, float f3, float f4) {
        float breatheAnim = titan.getHealth() <= 0.0F ? 0.0F : sin(f2 * 0.1F);
        float walkAnim1 = Math.max(0.0F, (sin(f * PI / 4.0F) + 0.4F) * f1);
        float walkAnim2 = Math.max(0.0F, (sin(f * PI / 4.0F + PI) + 0.4F) * f1);
        float walkAnim3 = sin(f * PI / 8.0F) * f1;
        float walkAnim4 = Math.max(0.0F, (sin(f * PI / 4.0F + PI / 2) + 0.4F) * f1);
        float walkAnim5 = Math.max(0.0F, (sin(f * PI / 4.0F + PI * 3.0F / 2.0F) + 0.4F) * f1);
        float walkAnim6 = sin(f * PI / 8.0F + PI / 2) * f1;
        float f6 = f3 * PI / 180.0F / 3.0F;
        float f7 = f4 * PI / 180.0F / 3.0F;
        pelvis.rotationPointY += sin(f * PI / 4.0F) * f1 * 0.5F;
        body.rotateAngleX += breatheAnim * 0.02F + f7;
        body.rotateAngleY += f6;
        neck.rotateAngleX += breatheAnim * 0.02F + f7;
        neck.rotateAngleY = f6;
        head.rotateAngleX += breatheAnim * 0.02F + f7;
        head.rotateAngleY = f6;
        frleg.rotateAngleX -= walkAnim1 * 0.3F;
        frleg.rotateAngleY += walkAnim3 * 0.2F;
        frleg.rotateAngleZ += walkAnim3 * 0.2F;
        flleg.rotateAngleX -= walkAnim2 * 0.3F;
        flleg.rotateAngleY -= walkAnim3 * 0.2F;
        flleg.rotateAngleZ -= walkAnim3 * 0.2F;
        brleg.rotateAngleX += walkAnim5 * 0.3F;
        brleg.rotateAngleY -= walkAnim6 * 0.2F;
        brleg.rotateAngleZ -= walkAnim6 * 0.2F;
        blleg.rotateAngleX += walkAnim4 * 0.3F;
        blleg.rotateAngleY += walkAnim6 * 0.2F;
        blleg.rotateAngleZ += walkAnim6 * 0.2F;
        for (int i = 0; i < vents.length; i++)
            vents[i].rotateAngleX += breatheAnim * 0.06F * (i % 2 == 0 ? 1 : -1);
        fin1.rotateAngleZ -= breatheAnim * 0.08F;
        fin2.rotateAngleZ += breatheAnim * 0.08F;
        tail[0].rotateAngleY += sin(f2 * 0.06F) * 0.2F + walkAnim3 * 0.3F;
        tail[1].rotateAngleY += sin(f2 * 0.06F - 0.9F) * 0.3F;
    }

    private void frontLegs(float w, float x, float y, float z) {
        ModelAnim.addMirrored(frleg, flleg, w, x, y, z);
    }

    @Override
    protected void animate(EntityTitan titan, int style, float t, int hit, int duration) {
        float[] w = ModelAnim.attack(t, hit, duration);
        switch (style) {
            case PUNCH: // head-butt
                ModelAnim.add(neck, w[0], -0.6F, 0.0F, 0.0F);
                ModelAnim.add(head, w[0], -0.4F, 0.0F, 0.0F);
                ModelAnim.add(body, w[0], -0.25F, 0.0F, 0.0F);
                ModelAnim.add(neck, w[1], 0.5F, 0.0F, 0.0F);
                ModelAnim.add(head, w[1], 0.5F, 0.0F, 0.0F);
                ModelAnim.add(body, w[1], 0.45F, 0.0F, 0.0F);
                frontLegs(w[1], 0.25F, 0.0F, 0.0F);
                break;
            case SMASH: // rear up on the hind legs, both fore legs come crashing down
                ModelAnim.add(pelvis, w[0], -0.55F, 0.0F, 0.0F);
                pelvis.rotationPointY -= 3.0F * w[0];
                frontLegs(w[0], -1.1F, 0.0F, 0.0F);
                ModelAnim.addMirrored(frforeleg, flforeleg, w[0], 0.7F, 0.0F, 0.0F);
                ModelAnim.add(neck, w[0], -0.3F, 0.0F, 0.0F);
                ModelAnim.add(pelvis, w[1], 0.25F, 0.0F, 0.0F);
                frontLegs(w[1], 0.35F, 0.0F, 0.0F);
                ModelAnim.add(neck, w[1], 0.4F, 0.0F, 0.0F);
                ModelAnim.add(head, w[1], 0.3F, 0.0F, 0.0F);
                break;
            case STOMP:
                ModelAnim.add(frleg, w[0], -1.0F, 0.0F, 0.0F);
                ModelAnim.add(frforeleg, w[0], 0.6F, 0.0F, 0.0F);
                ModelAnim.add(body, w[0], -0.2F, 0.0F, 0.0F);
                ModelAnim.add(frleg, w[1], 0.4F, 0.0F, 0.0F);
                ModelAnim.add(body, w[1], 0.25F, 0.0F, 0.0F);
                pelvis.rotationPointY += 1.5F * w[1];
                break;
            case SWIPE: // fore-leg backhand
                ModelAnim.add(flleg, w[0], -0.8F, 0.9F, 0.0F);
                ModelAnim.add(flleg, w[1], -0.6F, -1.4F, 0.0F);
                ModelAnim.add(body, w[0] - w[1], 0.0F, 0.3F, 0.0F);
                ModelAnim.add(neck, w[1], 0.0F, -0.4F, 0.0F);
                break;
            case ROAR: { // spit / hiss: neck thrust forward, fins and vents flare
                float hold = ModelAnim.pulse(t, 0.0F, hit, duration - 10.0F, duration);
                ModelAnim.add(neck, hold, -0.5F, 0.0F, 0.0F);
                ModelAnim.add(head, hold, -0.6F, sin(t * 1.4F) * 0.1F, 0.0F);
                flare(hold);
                break;
            }
            case CAST: { // thunder clap: body rises, head to the sky, charge crackles through the vents
                float hold = ModelAnim.pulse(t, 0.0F, hit, duration - 30.0F, duration);
                ModelAnim.add(pelvis, hold, -0.35F, 0.0F, 0.0F);
                ModelAnim.add(neck, hold, -0.5F, 0.0F, 0.0F);
                ModelAnim.add(head, hold, -0.7F, 0.0F, 0.0F);
                frontLegs(hold, -0.3F, 0.0F, 0.0F);
                flare(hold * (0.8F + 0.2F * sin(t * 2.0F)));
                break;
            }
            case STUN: {
                float in = ModelAnim.ease(t / 20.0F) * (1.0F - ModelAnim.ease(ModelAnim.window(t, duration - 20.0F, duration)));
                pelvis.rotationPointY += 6.0F * in;
                ModelAnim.add(neck, in, 0.5F, 0.0F, sin(t * 0.07F) * 0.25F);
                ModelAnim.add(head, in, 0.5F, 0.0F, 0.0F);
                ModelAnim.addMirrored(frleg, flleg, in, 0.2F, 0.0F, 0.4F);
                ModelAnim.addMirrored(brleg, blleg, in, 0.2F, 0.0F, -0.4F);
                break;
            }
            case DEATH:
                sprawl(ModelAnim.ease(t / 40.0F));
                break;
            case RISE:
                sprawl(1.0F - ModelAnim.ease(ModelAnim.window(t, duration * 0.4F, duration * 0.9F)));
                break;
            case SPECIAL1: { // pounce
                float crouch = ModelAnim.pulse(t, 0.0F, hit - 2.0F, hit, hit + 4.0F);
                float air = ModelAnim.pulse(t, hit, hit + 6.0F, hit + 22.0F, hit + 28.0F);
                float land = ModelAnim.pulse(t, hit + 22.0F, hit + 26.0F, duration - 20.0F, duration);
                pelvis.rotationPointY += 5.0F * crouch - 2.0F * land;
                ModelAnim.addMirrored(frleg, flleg, crouch, 0.3F, 0.0F, 0.35F);
                ModelAnim.addMirrored(brleg, blleg, crouch, -0.3F, 0.0F, -0.3F);
                ModelAnim.add(neck, crouch, 0.3F, 0.0F, 0.0F);
                frontLegs(air, -0.9F, 0.0F, 0.0F);
                ModelAnim.addMirrored(frforeleg, flforeleg, air, -0.4F, 0.0F, 0.0F);
                ModelAnim.addMirrored(brleg, blleg, air, 0.6F, 0.0F, 0.0F);
                ModelAnim.add(neck, air, -0.4F, 0.0F, 0.0F);
                ModelAnim.addMirrored(frleg, flleg, land, 0.2F, 0.0F, 0.5F);
                ModelAnim.addMirrored(brleg, blleg, land, 0.1F, 0.0F, -0.45F);
                ModelAnim.add(neck, land, 0.4F, 0.0F, 0.0F);
                flare(land);
                break;
            }
            case SPECIAL2: { // charge-up: hunkered down, everything trembles, vents wide open
                float hold = ModelAnim.pulse(t, 0.0F, hit, duration - 15.0F, duration);
                float tremble = sin(t * 2.7F) * 0.06F * hold;
                pelvis.rotationPointY += 3.0F * hold;
                ModelAnim.addMirrored(frleg, flleg, hold, 0.2F, 0.0F, 0.3F + tremble);
                ModelAnim.addMirrored(brleg, blleg, hold, 0.0F, 0.0F, -0.3F - tremble);
                ModelAnim.add(neck, hold, -0.3F, tremble, 0.0F);
                ModelAnim.add(head, hold, -0.4F, 0.0F, tremble);
                flare(hold * 1.3F);
                break;
            }
            case SPECIAL3: { // brood: back arches and the minions are flung out of the vents
                float arch = ModelAnim.pulse(t, 0.0F, hit, hit + 6.0F, duration);
                ModelAnim.add(body, arch, -0.45F, 0.0F, 0.0F);
                ModelAnim.add(neck, arch, 0.35F, 0.0F, 0.0F);
                flare(arch * 1.5F);
                pelvis.rotationPointY += 2.0F * arch;
                break;
            }
            default:
                break;
        }
    }

    private void flare(float w) {
        for (int i = 0; i < vents.length; i++)
            ModelAnim.add(vents[i], w, 0.6F, 0.0F, i % 2 == 0 ? -0.4F : 0.4F);
        ModelAnim.add(fin1, w, 0.0F, 0.0F, -0.7F);
        ModelAnim.add(fin2, w, 0.0F, 0.0F, 0.7F);
    }

    private void sprawl(float f) {
        pelvis.rotationPointY += 9.0F * f;
        ModelAnim.addMirrored(frleg, flleg, f, 0.4F, 0.0F, 0.9F);
        ModelAnim.addMirrored(brleg, blleg, f, -0.3F, 0.0F, -0.8F);
        ModelAnim.add(neck, f, 0.6F, 0.0F, 0.3F);
        ModelAnim.add(head, f, 0.6F, 0.4F, 0.0F);
        ModelAnim.add(tail[0], f, 0.5F, 0.0F, 0.0F);
    }

    // ------------------------------------------------------------------ ground choreography

    @Override
    protected void downPose(EntityTitan titan, int kind, float t, float d, float age) {
        if (kind == DOWN_DEATH) {
            // death throes -> slumps onto its side, vents sputtering -> (at 300) staggers up and swells to burst
            float throes = hold(t, 0.0F, 6.0F, 22.0F, 32.0F);
            float slump = ph(t, 26.0F, 50.0F);
            float up = ph(t, 290.0F, 310.0F);
            float swell = ph(t, 300.0F, 360.0F);
            float down = slump * (1.0F - up);
            pelvis.rotationPointY -= 4.0F * throes;
            ModelAnim.add(neck, throes, -0.9F, sin(age * 1.1F) * 0.4F, 0.0F);
            ModelAnim.add(head, throes, -0.6F, 0.0F, sin(age * 1.4F) * 0.3F);
            ModelAnim.addMirrored(frleg, flleg, throes, -0.6F + sin(age * 1.7F) * 0.3F, 0.0F, 0.2F);
            flare(throes);
            sprawl(down);
            root[2] += 75.0F * down;
            root[0] -= 6.0F * down;
            float twitch = down * (float)Math.exp(-Math.max(0.0F, t - 50.0F) / 80.0F);
            ModelAnim.addMirrored(brleg, blleg, twitch, sin(age * 1.9F) * 0.3F, 0.0F, 0.0F);
            frforeleg.rotateAngleX += sin(age * 2.3F) * 0.4F * twitch;
            flare(down * 0.5F * (0.5F + 0.5F * sin(age * 0.7F)));
            // the fuse: legs braced, everything trembles faster, the body bloats
            float trem = swell * (0.04F + 0.1F * swell);
            float fq = 1.5F + 3.0F * swell;
            root[4] *= 1.0F + 0.35F * swell + 0.04F * swell * sin(age * fq);
            ModelAnim.addMirrored(frleg, flleg, up, 0.1F, 0.0F, 0.35F + sin(age * fq) * trem);
            ModelAnim.addMirrored(brleg, blleg, up, 0.0F, 0.0F, -0.35F - sin(age * fq) * trem);
            ModelAnim.add(neck, up, -0.6F, sin(age * fq) * trem, 0.0F);
            ModelAnim.add(head, up, -0.5F, 0.0F, sin(age * fq * 1.3F) * trem);
            flare(swell * 1.6F);
            return;
        }
        float r = kind == DOWN_DORMANT ? 0.0F : t / d;
        float breathe = hold(r, 0.0F, 0.04F, 0.2F, 0.26F);
        float headUp = ph(r, 0.18F, 0.34F);
        float sniff = hold(r, 0.3F, 0.34F, 0.44F, 0.48F);
        float front = ph(r, 0.46F, 0.60F);
        float back = ph(r, 0.60F, 0.74F);
        float shake = hold(r, 0.76F, 0.79F, 0.88F, 0.92F);
        float hiss = hold(r, 0.91F, 0.94F, 0.97F, 1.0F);
        // curled up asleep: belly down, legs tucked, head on the ground, tail wrapped round
        float sleep = 1.0F - front;
        sprawlFront(1.0F - front);
        sprawlBack(1.0F - back);
        pelvis.rotationPointY += 9.0F * (1.0F - back) * 0.5F + 9.0F * (1.0F - front) * 0.5F;
        ModelAnim.add(neck, 1.0F - headUp, 0.4F, 0.0F, 0.0F);
        ModelAnim.add(head, 1.0F - headUp, 0.3F, 0.5F, 0.0F);
        ModelAnim.add(tail[0], sleep, 0.2F, 1.1F, 0.0F);
        ModelAnim.add(tail[1], sleep, 0.0F, 0.9F, 0.0F);
        for (int i = 0; i < vents.length; i++)
            ModelAnim.add(vents[i], sleep, -0.3F, 0.0F, 0.0F);
        flare(breathe * (0.4F + 0.4F * sin(age * 0.25F)));
        head.rotateAngleY += sin(age * 0.2F) * 0.5F * sniff;
        neck.rotateAngleX -= Math.abs(sin(age * 0.6F)) * 0.15F * sniff;
        // the front half rises first: chest up, rear still down
        root[1] -= 12.0F * front * (1.0F - back);
        // wet-dog shake from head to tail
        root[2] += sin(age * 1.9F) * 14.0F * shake;
        neck.rotateAngleZ += sin(age * 1.9F - 0.6F) * 0.35F * shake;
        head.rotateAngleZ += sin(age * 1.9F - 1.2F) * 0.45F * shake;
        tail[0].rotateAngleY += sin(age * 1.9F + 0.8F) * 0.6F * shake;
        flare(shake * 0.8F);
        // hiss
        ModelAnim.add(neck, hiss, -0.45F, 0.0F, 0.0F);
        ModelAnim.add(head, hiss, -0.35F, 0.0F, 0.0F);
        flare(hiss * 1.4F);
    }

    private void sprawlFront(float f) {
        ModelAnim.addMirrored(frleg, flleg, f, 0.4F, 0.0F, 0.9F);
        ModelAnim.add(neck, f, 0.6F, 0.0F, 0.3F);
        ModelAnim.add(head, f, 0.6F, 0.4F, 0.0F);
    }

    private void sprawlBack(float f) {
        ModelAnim.addMirrored(brleg, blleg, f, -0.3F, 0.0F, -0.8F);
        ModelAnim.add(tail[0], f, 0.5F, 0.0F, 0.0F);
    }

    /** Shell grow factor (the charged layer uses its own instance). */
    public float shellGrow() {
        return grow;
    }
}
