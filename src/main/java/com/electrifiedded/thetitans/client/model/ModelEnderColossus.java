package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thehippomaster.AnimationAPI.client.Animator;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelEnderColossus extends ModelBase {
    public boolean isAttacking;
    public ModelRenderer bottomTorso;
    public ModelRenderer thighLeft;
    public ModelRenderer thighRight;
    public ModelRenderer middleTorso;
    public ModelRenderer topTorso;
    public ModelRenderer shoulderLeft;
    public ModelRenderer shoulderRight;
    public ModelRenderer maw;
    public ModelRenderer foreLimbLeft;
    public ModelRenderer foreLimbRight;
    public ModelRenderer skullZ;
    public ModelRenderer horn1V;
    public ModelRenderer horn2V;
    public ModelRenderer horn3V;
    public ModelRenderer horn4V;
    public ModelRenderer femurLeft;
    public ModelRenderer femurRight;
    private Animator animatorV;

    public ModelEnderColossus() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shoulderRight = new ModelRenderer(this, 32, 0);
        this.shoulderRight.setRotationPoint(-5.0F, -2.0F, 0.0F);
        this.shoulderRight.addBox(-1.0F, -2.0F, -1.0F, 2, 15, 2, 0.0F);
        this.shoulderLeft = new ModelRenderer(this, 32, 0);
        this.shoulderLeft.mirror = true;
        this.shoulderLeft.setRotationPoint(5.0F, -2.0F, 0.0F);
        this.shoulderLeft.addBox(-1.0F, -2.0F, -1.0F, 2, 15, 2, 0.0F);
        this.horn1V = new ModelRenderer(this, 24, 38);
        this.horn1V.setRotationPoint(4.0F, -4.0F, 0.0F);
        this.horn1V.addBox(0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.maw = new ModelRenderer(this, 0, 16);
        this.maw.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.maw.addBox(-4.0F, -7.5F, -4.0F, 8, 8, 8, -0.25F);
        this.middleTorso = new ModelRenderer(this, 0, 40);
        this.middleTorso.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.middleTorso.addBox(-4.0F, -4.0F, -2.0F, 8, 4, 4, 0.0F);
        this.foreLimbLeft = new ModelRenderer(this, 32, 17);
        this.foreLimbLeft.mirror = true;
        this.foreLimbLeft.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.foreLimbLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 15, 2, 0.0F);
        this.foreLimbRight = new ModelRenderer(this, 32, 17);
        this.foreLimbRight.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.foreLimbRight.addBox(-1.0F, 0.0F, -1.0F, 2, 15, 2, 0.0F);
        this.topTorso = new ModelRenderer(this, 0, 32);
        this.topTorso.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.topTorso.addBox(-4.0F, -4.0F, -2.0F, 8, 4, 4, 0.0F);
        this.thighLeft = new ModelRenderer(this, 32, 0);
        this.thighLeft.mirror = true;
        this.thighLeft.setRotationPoint(2.0F, -6.0F, 0.0F);
        this.thighLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 15, 2, 0.0F);
        this.bottomTorso = new ModelRenderer(this, 0, 48);
        this.bottomTorso.setRotationPoint(0.0F, -6.0F, -0.0F);
        this.bottomTorso.addBox(-4.0F, -4.0F, -2.0F, 8, 4, 4, 0.0F);
        this.thighRight = new ModelRenderer(this, 32, 0);
        this.thighRight.setRotationPoint(-2.0F, -6.0F, 0.0F);
        this.thighRight.addBox(-1.0F, 0.0F, -1.0F, 2, 15, 2, 0.0F);
        this.femurLeft = new ModelRenderer(this, 32, 17);
        this.femurLeft.mirror = true;
        this.femurLeft.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.femurLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 15, 2, 0.0F);
        this.skullZ = new ModelRenderer(this, 0, 0);
        this.skullZ.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skullZ.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.25F);
        this.horn2V = new ModelRenderer(this, 24, 38);
        this.horn2V.setRotationPoint(-4.0F, -4.0F, 0.0F);
        this.horn2V.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.horn3V = new ModelRenderer(this, 36, 36);
        this.horn3V.setRotationPoint(3.0F, -1.0F, 0.0F);
        this.horn3V.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2, 0.0F);
        this.femurRight = new ModelRenderer(this, 32, 17);
        this.femurRight.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.femurRight.addBox(-1.0F, 0.0F, -1.0F, 2, 15, 2, 0.0F);
        this.horn4V = new ModelRenderer(this, 36, 36);
        this.horn4V.setRotationPoint(-3.0F, -1.0F, 0.0F);
        this.horn4V.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2, 0.0F);
        this.topTorso.addChild(this.shoulderRight);
        this.topTorso.addChild(this.shoulderLeft);
        this.skullZ.addChild(this.horn1V);
        this.topTorso.addChild(this.maw);
        this.bottomTorso.addChild(this.middleTorso);
        this.shoulderLeft.addChild(this.foreLimbLeft);
        this.shoulderRight.addChild(this.foreLimbRight);
        this.middleTorso.addChild(this.topTorso);
        this.thighLeft.addChild(this.femurLeft);
        this.maw.addChild(this.skullZ);
        this.skullZ.addChild(this.horn2V);
        this.horn1V.addChild(this.horn3V);
        this.thighRight.addChild(this.femurRight);
        this.horn2V.addChild(this.horn4V);
        this.animatorV = new Animator(this);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        playX((IAnimatedEntity)entity, f, f1, f2, f3, f4, f5);
        this.thighLeft.render(f5);
        this.bottomTorso.render(f5);
        this.thighRight.render(f5);
    }

    public void playX(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.animatorV.update(entity);
        assignAngles();
        EntityEnderColossus entitytitan = (EntityEnderColossus)entity;
        if (this.isAttacking)
            this.skullZ.setRotationPoint(0.0F, -7.0F, 0.0F);
        else
            this.skullZ.setRotationPoint(0.0F, 0.0F, 0.0F);
        float f6 = MathHelper.cos(f2 * 0.05F);
        this.thighRight.rotateAngleX = -0.09F;
        this.thighLeft.rotateAngleX = -0.09F;
        this.femurRight.rotateAngleX = 0.18F;
        this.femurLeft.rotateAngleX = 0.18F;
        if (entitytitan.deathTicks <= 0) {
            if (!this.isRiding) {
                this.thighLeft.rotateAngleX = -0.09F + MathHelper.cos(f * 0.33F + 2.6415927F) * 0.75F * f1;
                this.thighRight.rotateAngleX = -0.09F + MathHelper.cos(f * 0.33F - 0.5F) * 0.75F * f1;
                this.femurLeft.rotateAngleX = 0.18F + MathHelper.cos(f * 0.33F) * 0.75F * f1;
                this.femurRight.rotateAngleX = 0.18F + MathHelper.cos(f * 0.33F + 3.1415927F) * 0.75F * f1;
                if (this.femurRight.rotateAngleX < 0.0F) {
                    this.femurRight.rotateAngleX = 0.0F;
                }

                if (this.femurLeft.rotateAngleX < 0.0F) {
                    this.femurLeft.rotateAngleX = 0.0F;
                }
            }

            float faceYaw = f3 * 3.1415927F / 180.0F;
            float facePitch = f4 * 3.1415927F / 180.0F;
            if (entitytitan.getAnimID() == 0) {
                this.middleTorso.rotateAngleX = ((0.0F + -0.01F * f6) * 3.1415927F);
                this.topTorso.rotateAngleX = ((0.0F + -0.01F * f6) * 3.1415927F);
                this.skullZ.rotateAngleX = ((-0.01F + -0.01F * f6) * 3.1415927F);
                this.maw.rotateAngleX = ((0.01F + 0.01F * f6) * 3.1415927F);
                this.bottomTorso.rotateAngleZ = (MathHelper.cos(f * 0.33F) * 0.125F * f1);
                this.middleTorso.rotateAngleZ = (MathHelper.cos(f * 0.33F) * 0.125F * f1);
                this.topTorso.rotateAngleZ = (MathHelper.cos(f * 0.33F) * 0.125F * f1);
                this.maw.rotateAngleZ = (MathHelper.cos(f * 0.33F + 3.1415927F) * 0.375F * f1);
                this.foreLimbRight.rotateAngleX = MathHelper.cos(f * 0.33F + 3.1415927F) * 0.75F * f1 - 0.3F;
                this.foreLimbLeft.rotateAngleX = MathHelper.cos(f * 0.33F) * 0.75F * f1 - 0.3F;
            }

            this.shoulderRight.rotateAngleX = 0.09F + MathHelper.cos(f * 0.33F + 3.1415927F) * 0.75F * f1;
            this.shoulderLeft.rotateAngleX = 0.09F + MathHelper.cos(f * 0.33F) * 0.75F * f1;
            this.shoulderRight.rotateAngleY = 0.08F;
            this.shoulderLeft.rotateAngleY = -0.08F;
            this.shoulderRight.rotateAngleZ = 0.10471976F - (0.005F + 0.005F * f6) * 3.1415927F;
            this.shoulderLeft.rotateAngleZ = -0.10471976F + (-0.005F + -0.005F * f6) * 3.1415927F;
            if (entitytitan.getAnimID() == 0 && entitytitan.fetchLaserTicksEye() < 0) {
                this.maw.rotateAngleX += facePitch * 0.3F;
                this.maw.rotateAngleY += faceYaw * 0.3F;
                this.topTorso.rotateAngleX += facePitch * 0.3F;
                this.topTorso.rotateAngleY += faceYaw * 0.3F;
                this.middleTorso.rotateAngleX += facePitch * 0.3F;
                this.middleTorso.rotateAngleY += faceYaw * 0.3F;
                this.shoulderRight.rotateAngleX -= facePitch * 0.6F;
                this.shoulderLeft.rotateAngleX -= facePitch * 0.6F;
            }

            else {
                this.maw.rotateAngleX += facePitch * 0.9F;
                this.maw.rotateAngleY += faceYaw * 0.9F;
            }

            if (this.foreLimbRight.rotateAngleX > -0.3F)
                this.foreLimbRight.rotateAngleX = -0.3F;
            if (this.foreLimbLeft.rotateAngleX > -0.3F)
                this.foreLimbLeft.rotateAngleX = -0.3F;
            if (entitytitan.isFlying && !this.isRiding) {
                this.topTorso.rotateAngleZ = 0F;
                this.middleTorso.rotateAngleZ = 0F;
                this.bottomTorso.rotateAngleZ = 0F;
                this.maw.rotateAngleZ = 0F;
                this.bottomTorso.rotateAngleZ = 0F;
                this.middleTorso.rotateAngleZ = 0F;
                this.topTorso.rotateAngleZ = 0F;
                this.skullZ.rotateAngleZ = 0F;
                this.shoulderRight.rotateAngleX = 0.09F;
                this.shoulderLeft.rotateAngleX = 0.09F;
                this.foreLimbRight.rotateAngleX = 0.09F;
                this.foreLimbLeft.rotateAngleX = 0.09F;
                this.maw.rotateAngleX -= (float)(entitytitan.limbSwingAmount);
                this.bottomTorso.rotateAngleX += (float)(entitytitan.limbSwingAmount);
                this.thighRight.rotateAngleX = ((MathHelper.cos(f2 * 0.1F - 0.5F)) * 0.25F) - (float)(entitytitan.motionY / 5) + entitytitan.limbSwingAmount;
                this.thighLeft.rotateAngleX = ((MathHelper.cos(f2 * 0.1F - 3.6415927F)) * 0.25F) - (float)(entitytitan.motionY / 5) + entitytitan.limbSwingAmount;
                this.femurRight.rotateAngleX = 0.5F - ((MathHelper.cos(f2 * 0.1F)) * 0.5F);
                this.femurLeft.rotateAngleX = 0.5F - ((MathHelper.cos(f2 * 0.1F - 3.1415927F)) * 0.5F);
            }

            if (entitytitan.getAnimID() == 8 && (entitytitan.getAnimTick() > 20 && entitytitan.getAnimTick() < 60)) {
                this.topTorso.rotateAngleY = ((MathHelper.cos(f2)) * 0.5F);
                this.maw.rotateAngleY = ((MathHelper.cos(f2)) * 0.25F);
                this.topTorso.rotateAngleX = ((MathHelper.cos(f2 * 0.25F - 2F)) * 0.25F);
                this.middleTorso.rotateAngleX = ((MathHelper.cos(f2 * 0.25F - 1F)) * 0.25F);
                this.bottomTorso.rotateAngleX = ((MathHelper.cos(f2 * 0.25F)) * 0.25F);
            }

            if (entitytitan.getAnimID() == 8 && entitytitan.getAnimTick() > 100 && entitytitan.getAnimTick() < 340) {
                this.maw.rotateAngleY = MathHelper.cos(f2 * 0.05F) * 0.2F;
            }

            if (entitytitan.getAnimID() == 1) {
                switch (entitytitan.fetchATAAID()) {
                    case 0:
                        this.playGiantAttack1Anti(entitytitan);
                        break;
                    case 1:
                        this.playGiantAttack2Anti();
                        break;
                    case 2:
                        this.playGiantAttack3Anti();
                        break;
                    case 3:
                        this.playGiantAttack4Anti();
                }
            }

            playStomp();
            playSwat();
            playSlam();
            playMeteor();
            playLightningChain();
            playLightning();
            playBallLightning();
            playBallDragon();
            playScream();
            playStunned();
            if (this.skullZ.rotationPointY < -7F)
                this.skullZ.rotationPointY = -7.0F;
            if (entitytitan.getAnimID() == 3 && (entitytitan.getAnimTick() > 30 && entitytitan.getAnimTick() < 50)) {
                this.foreLimbRight.rotateAngleX += ((0.1F * MathHelper.cos(f2)) * 3.1415927F);
                this.foreLimbLeft.rotateAngleX -= ((0.1F * MathHelper.cos(f2)) * 3.1415927F);
            }
        }

        else {
            playDeath();
        }
    }

    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void assignAngles() {
        this.shoulderRight.setRotationPoint(-5.0F, -2.0F, 0.0F);
        this.shoulderLeft.setRotationPoint(5.0F, -2.0F, 0.0F);
        this.maw.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.bottomTorso.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.thighLeft.setRotationPoint(2.0F, -6.0F, 0.0F);
        this.thighRight.setRotationPoint(-2.0F, -6.0F, 0.0F);
        this.horn1V.setRotationPoint(4.25F, -4.5F, 0.0F);
        this.horn2V.setRotationPoint(-4.25F, -4.5F, 0.0F);
    }

    private void playGiantAttack1Anti(EntityEnderColossus entitytitan) {
        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanEnderColossusChomp", entitytitan.world, entitytitan, 100F, 1F);
        this.animatorV.setAnim(1);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 1.0F, -1.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, 1.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.thighRight, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 2.0F, -12.0F);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -1.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, -1.0F, -1.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(10);
    }

    private void playGiantAttack2Anti() {
        this.animatorV.setAnim(1);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 5.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.5F, -0.5F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.topTorso, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 1.6F, 0.0F, 2.0F);
        this.animatorV.rotate(this.foreLimbRight, -0.9F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.thighRight, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -0.5F, 0.5F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.25F, -0.25F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.25F, -0.25F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, 0.0F, 0.0F, -0.75F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(0);
    }

    private void playGiantAttack3Anti() {
        this.animatorV.setAnim(1);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 5.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, -0.8F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.8F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -4F, 0.0F, -0.25F);
        this.animatorV.rotate(this.shoulderLeft, -4F, 0.0F, 0.25F);
        this.animatorV.rotate(this.foreLimbRight, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.thighRight, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 1F, 0.0F, -0.25F);
        this.animatorV.rotate(this.shoulderLeft, 1F, 0.0F, 0.25F);
        this.animatorV.rotate(this.foreLimbRight, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(0);
    }

    private void playGiantAttack4Anti() {
        this.animatorV.setAnim(1);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.thighRight, 2F, 0.2F, 1.5F);
        this.animatorV.rotate(this.femurRight, 1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.0F, -1.0F, -0.2F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, -0.2F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, -0.2F);
        this.animatorV.rotate(this.bottomTorso, -1.0F, 1.0F, 0.6F);
        this.animatorV.rotate(this.shoulderRight, -1.5F, 0.0F, 0.75F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.shoulderLeft, -1.5F, 0.0F, -0.75F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.5F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 2.0F, -12.0F);
        this.animatorV.move(this.thighRight, 0.0F, 2.0F, -13.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 2.0F, -11.0F);
        this.animatorV.rotate(this.thighRight, -3F, 0.2F, 1.5F);
        this.animatorV.rotate(this.thighLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.0F, 0.0F, -0.25F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.5F, -0.25F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.5F, -0.25F);
        this.animatorV.rotate(this.bottomTorso, -1.0F, -1.0F, 0.75F);
        this.animatorV.rotate(this.shoulderRight, -1.5F, 0.0F, 0.75F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.shoulderLeft, -1.5F, 0.0F, -0.75F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.5F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(0);
    }

    private void playMeteor() {
        this.animatorV.setAnim(2);
        this.animatorV.startPhase(30);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 5.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.topTorso, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -1.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.shoulderLeft, -1.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.5F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -0.5F, 0.5F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.25F, -0.25F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.25F, -0.25F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(40);
    }

    private void playBallLightning() {
        this.animatorV.setAnim(4);
        this.animatorV.startPhase(30);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 5.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.topTorso, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -1.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.shoulderLeft, -1.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.5F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -0.5F, 0.5F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.25F, -0.25F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.25F, -0.25F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(40);
    }

    private void playLightningChain() {
        this.animatorV.setAnim(3);
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.maw, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -1.5F, 0.0F, 0.75F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, -0.25F);
        this.animatorV.rotate(this.shoulderLeft, -1.5F, 0.0F, -0.75F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.25F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(30);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.maw, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 0.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, 0.0F, 0.0F, -0.5F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playBallDragon() {
        this.animatorV.setAnim(11);
        this.animatorV.startPhase(30);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 5.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.topTorso, -0.25F, 0.25F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -1.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.shoulderLeft, -1.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.5F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, -8.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -0.5F, 0.5F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.25F, -0.25F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.25F, -0.25F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(40);
    }

    private void playLightning() {
        this.animatorV.setAnim(13);
        this.animatorV.startPhase(20);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.bottomTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, -1.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -1.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.maw, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(20);
    }

    private void playScream() {
        this.animatorV.setAnim(5);
        this.animatorV.startPhase(25);
        this.animatorV.move(this.bottomTorso, 0.0F, 2.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 2.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 2.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(25);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 5.0F);
        this.animatorV.rotate(this.skullZ, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 1.0F, -1.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, 1.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -1.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.bottomTorso, 0.5F, 0.0F, 1.5F);
        this.animatorV.rotate(this.shoulderRight, 0.0F, 0.0F, 1.0F);
        this.animatorV.rotate(this.shoulderLeft, 0.0F, 0.0F, -1.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, -0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(100);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -1.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.bottomTorso, 0.5F, 0.0F, -1.5F);
        this.animatorV.rotate(this.shoulderRight, 0.0F, 0.0F, 1.0F);
        this.animatorV.rotate(this.shoulderLeft, 0.0F, 0.0F, -1.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, -0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(20);
    }

    private void playSlam() {
        this.animatorV.setAnim(6);
        this.animatorV.startPhase(15);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 0.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(15);
        this.animatorV.move(this.bottomTorso, 0.0F, -1F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, -1F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, -1F, 0.0F);
        this.animatorV.rotate(this.thighRight, -2F, 0.2F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.1F, 0.2F, 0.0F);
        this.animatorV.rotate(this.femurRight, 2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, -0.8F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.8F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -4F, 0.0F, -0.25F);
        this.animatorV.rotate(this.shoulderLeft, -4F, 0.0F, 0.25F);
        this.animatorV.rotate(this.foreLimbRight, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 16F, 1.0F);
        this.animatorV.move(this.thighRight, 0.0F, 16F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 16F, 0.0F);
        this.animatorV.rotate(this.thighRight, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -2.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, -3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -0.75F, 0.0F, -0.25F);
        this.animatorV.rotate(this.foreLimbLeft, -0.75F, 0.0F, 0.25F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playStomp() {
        this.animatorV.setAnim(7);
        this.animatorV.startPhase(25);
        this.animatorV.move(this.bottomTorso, 0.0F, 2.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 2.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 2.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(25);
        this.animatorV.move(this.bottomTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, -2.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -2.0F, 0.2F, 0.75F);
        this.animatorV.rotate(this.thighLeft, 0.0F, 0.2F, 0.0F);
        this.animatorV.rotate(this.femurRight, 1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.femurLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 1.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.topTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.0F, 0.0F, 0.25F);
        this.animatorV.rotate(this.shoulderRight, -0.75F, 0.0F, 0.75F);
        this.animatorV.rotate(this.shoulderLeft, -0.75F, 0.0F, -0.75F);
        this.animatorV.rotate(this.foreLimbRight, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -0.3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.bottomTorso, 0.0F, 4.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 4.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 2.0F, 0.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.5F, 0.5F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -1.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.0F, 0.0F, -0.4F);
        this.animatorV.rotate(this.shoulderRight, 0.5F, 0.0F, 0.25F);
        this.animatorV.rotate(this.shoulderLeft, 0.5F, 0.0F, -0.25F);
        this.animatorV.rotate(this.foreLimbRight, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -0.3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(5);
        this.animatorV.startPhase(20);
        this.animatorV.move(this.bottomTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, -2.0F, 0.0F);
        this.animatorV.move(this.skullZ, 0.0F, -2.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -2.0F, 0.0F, -0.75F);
        this.animatorV.rotate(this.femurRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 1.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.maw, 1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.topTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.0F, 0.0F, -0.25F);
        this.animatorV.rotate(this.shoulderRight, 0.8F, 0.0F, 0.75F);
        this.animatorV.rotate(this.shoulderLeft, 0.8F, 0.0F, -0.75F);
        this.animatorV.rotate(this.foreLimbRight, -0.8F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -0.8F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.bottomTorso, 0.0F, 4.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 2.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 4.0F, 0.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.5F, -0.5F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, -1.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.0F, 0.0F, 0.4F);
        this.animatorV.rotate(this.shoulderRight, 0.75F, 0.0F, 0.25F);
        this.animatorV.rotate(this.shoulderLeft, 0.75F, 0.0F, -0.25F);
        this.animatorV.rotate(this.foreLimbRight, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -0.75F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.resetPhase(20);
    }

    private void playStunned() {
        this.animatorV.setAnim(8);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 0.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.move(this.bottomTorso, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighRight, 0.0F, 1.0F, 5.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 1.0F, 5.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -2.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.foreLimbRight, -1.5F, 0.0F, -1.0F);
        this.animatorV.rotate(this.shoulderLeft, -2.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.foreLimbLeft, -1.5F, 0.0F, 1.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(40);
        this.animatorV.startPhase(40);
        this.animatorV.move(this.bottomTorso, 0.0F, 19.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 19.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 19.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.9F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 2.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -2.0F, 0.5F, -0.25F);
        this.animatorV.rotate(this.foreLimbRight, -0.75F, -0.5F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, -2.0F, -0.5F, 0.25F);
        this.animatorV.rotate(this.foreLimbLeft, -0.75F, 0.5F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(240);
        this.animatorV.startPhase(20);
        this.animatorV.move(this.bottomTorso, 0.0F, 14.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 14.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 14.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 1.8F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.8F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, -2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(40);
    }

    private void playSwat() {
        this.animatorV.setAnim(9);
        this.animatorV.startPhase(15);
        this.animatorV.move(this.bottomTorso, 0.0F, 4.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 4.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 4.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.125F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.125F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.125F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.375F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 1F, 1F, 1.5F);
        this.animatorV.rotate(this.shoulderLeft, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(25);
        this.animatorV.move(this.bottomTorso, 0.0F, 16.0F, 0.0F);
        this.animatorV.move(this.thighRight, 0.0F, 16.0F, 0.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 16.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -1.0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -1.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.femurRight, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -2.0F, 0F, -0.5F);
        this.animatorV.rotate(this.shoulderLeft, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -0.25F, 0.0F, -1.0F);
        this.animatorV.rotate(this.foreLimbLeft, -0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(20);
    }

    private void playDeath() {
        this.animatorV.setAnim(10);
        this.animatorV.startPhase(40);
        this.animatorV.move(this.bottomTorso, 0.0F, 4.0F, -6.0F);
        this.animatorV.move(this.thighRight, 0.0F, 4.0F, -6.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 4.0F, -6.0F);
        this.animatorV.move(this.skullZ, 0.0F, -2.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -0.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.foreLimbLeft, -0.5F, 0.0F, -0.3F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(40);
        this.animatorV.move(this.bottomTorso, 0.0F, 4.0F, -16.0F);
        this.animatorV.move(this.thighRight, 0.0F, 4.0F, -16.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 4.0F, -16.0F);
        this.animatorV.move(this.skullZ, 0.0F, -5.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, 0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.topTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.middleTorso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderLeft, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.foreLimbRight, -0.5F, 0.0F, -0.75F);
        this.animatorV.rotate(this.foreLimbLeft, -0.5F, 0.0F, -0.3F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(80);
        this.animatorV.move(this.bottomTorso, 0.0F, 28.0F, 16.0F);
        this.animatorV.move(this.thighRight, 0.0F, 28.0F, 16.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 28.0F, 16.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -3.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -3.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurRight, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, -1.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, -1.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.foreLimbRight, 0.0F, 0.0F, -0.25F);
        this.animatorV.rotate(this.foreLimbLeft, 0.0F, 0.0F, 0.25F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(80);
        this.animatorV.move(this.bottomTorso, 0.0F, 26.0F, 16.0F);
        this.animatorV.move(this.thighRight, 0.0F, 28.0F, 16.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 28.0F, 16.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -2.0F, 0.25F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -2.0F, -0.25F, 0.0F);
        this.animatorV.rotate(this.femurRight, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, -1.55F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 0.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, 0.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.foreLimbRight, 0.0F, 0.0F, -0.25F);
        this.animatorV.rotate(this.foreLimbLeft, 0.0F, 0.0F, 0.25F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(200);
        this.animatorV.move(this.bottomTorso, 0.0F, 26.0F, 16.0F);
        this.animatorV.move(this.thighRight, 0.0F, 28.0F, 16.0F);
        this.animatorV.move(this.thighLeft, 0.0F, 28.0F, 16.0F);
        this.animatorV.move(this.skullZ, 0.0F, -7.0F, 0.0F);
        this.animatorV.rotate(this.thighRight, -1.55F, 0.25F, 0.0F);
        this.animatorV.rotate(this.thighLeft, -1.55F, -0.25F, 0.0F);
        this.animatorV.rotate(this.femurRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.femurLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.maw, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.bottomTorso, -1.55F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shoulderRight, 0.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.shoulderLeft, 0.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.foreLimbRight, 0.0F, 0.0F, -0.3F);
        this.animatorV.rotate(this.foreLimbLeft, 0.0F, 0.0F, 0.3F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(1520);
        this.animatorV.resetPhase(0);
    }
}
