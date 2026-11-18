package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thehippomaster.AnimationAPI.client.Animator;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelOmegafish extends ModelBase {
    public ModelRenderer centerTorso;
    public ModelRenderer tail1V;
    public ModelRenderer fuzz1V;
    public ModelRenderer torsoFront;
    public ModelRenderer tail2V;
    public ModelRenderer tail3V;
    public ModelRenderer fuzz3V;
    public ModelRenderer tipRear;
    public ModelRenderer skullZ;
    public ModelRenderer fuzz2V;
    private Animator animatorV;

    public ModelOmegafish() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.tipRear = new ModelRenderer(this, 13, 4);
        this.tipRear.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.tipRear.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.fuzz1V = new ModelRenderer(this, 20, 0);
        this.fuzz1V.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.fuzz1V.addBox(-5.0F, -8.0F, -1.5F, 10, 8, 3, 0.0F);
        this.tail3V = new ModelRenderer(this, 11, 0);
        this.tail3V.setRotationPoint(0.0F, 0.5F, 3.0F);
        this.tail3V.addBox(-1.0F, -0.5F, 0.0F, 2, 1, 2, 0.0F);
        this.fuzz3V = new ModelRenderer(this, 20, 11);
        this.fuzz3V.setRotationPoint(0.0F, 1.0F, 1.5F);
        this.fuzz3V.addBox(-3.0F, -4.0F, -1.5F, 6, 4, 3, 0.0F);
        this.skullZ = new ModelRenderer(this, 0, 0);
        this.skullZ.setRotationPoint(0.0F, 0.5F, -2.0F);
        this.skullZ.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, 0.0F);
        this.centerTorso = new ModelRenderer(this, 0, 9);
        this.centerTorso.setRotationPoint(0.0F, 22.0F, 1.0F);
        this.centerTorso.addBox(-3.0F, -2.0F, -1.5F, 6, 4, 3, 0.0F);
        this.torsoFront = new ModelRenderer(this, 0, 4);
        this.torsoFront.setRotationPoint(0.0F, 0.5F, -1.5F);
        this.torsoFront.addBox(-2.0F, -1.5F, -2.0F, 4, 3, 2, 0.0F);
        this.tail2V = new ModelRenderer(this, 0, 22);
        this.tail2V.setRotationPoint(0.0F, 0.5F, 3.0F);
        this.tail2V.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3, 0.0F);
        this.tail1V = new ModelRenderer(this, 0, 16);
        this.tail1V.setRotationPoint(0.0F, 0.5F, 1.5F);
        this.tail1V.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 3, 0.0F);
        this.fuzz2V = new ModelRenderer(this, 20, 18);
        this.fuzz2V.setRotationPoint(0.0F, 1.5F, -0.5F);
        this.fuzz2V.addBox(-3.0F, -5.0F, -1.5F, 6, 5, 2, 0.0F);
        this.tail3V.addChild(this.tipRear);
        this.centerTorso.addChild(this.fuzz1V);
        this.tail2V.addChild(this.tail3V);
        this.tail2V.addChild(this.fuzz3V);
        this.torsoFront.addChild(this.skullZ);
        this.centerTorso.addChild(this.torsoFront);
        this.tail1V.addChild(this.tail2V);
        this.centerTorso.addChild(this.tail1V);
        this.torsoFront.addChild(this.fuzz2V);
        this.animatorV = new Animator(this);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        playX((IAnimatedEntity)entity, f, f1, f2, f3, f4, f5);
        this.centerTorso.render(f5);
    }

    public void assignAngles() {
        this.centerTorso.rotationPointY = 22.0F;
        this.centerTorso.rotationPointX = 0.0F;
        this.centerTorso.rotationPointZ = 1.0F;
        this.torsoFront.rotationPointY = 0.5F;
        this.torsoFront.rotationPointX = 0.0F;
        this.torsoFront.rotationPointZ = -1.5F;
    }

    public void playX(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.animatorV.update(entity);
        assignAngles();
        EntitySilverfishTitan entitytitan = (EntitySilverfishTitan)entity;
        if (entitytitan.deathTicks <= 0) {
            if (entitytitan.getAnimID() == 0) {
                this.torsoFront.rotateAngleX = ((-0.01F + 0.01F * MathHelper.cos(f2 * 0.1F)) * 3.1415927F);
                this.skullZ.rotateAngleX = ((0.01F + -0.01F * MathHelper.cos(f2 * 0.1F)) * 3.1415927F);
                this.skullZ.rotateAngleY = -(MathHelper.cos(f * 0.5F) * 0.25F * f1);
                this.torsoFront.rotateAngleY = (MathHelper.cos(f * 0.5F - 0.5F) * 0.25F * f1);
                this.centerTorso.rotateAngleY = (MathHelper.cos(f * 0.5F - 1F) * 0.25F * f1);
                this.tail1V.rotateAngleY = -(f3 * 3.1415927F / 180.0F / 4) + (0.01F * MathHelper.cos(f2 * 0.1F - 1F) * 3.1415927F) + (MathHelper.cos(f * 0.5F - 1.5F) * 0.25F * f1);
                this.tail2V.rotateAngleY = -(f3 * 3.1415927F / 180.0F / 4) + (0.01F * MathHelper.cos(f2 * 0.1F - 1.5F) * 3.1415927F) + (MathHelper.cos(f * 0.5F - 2F) * 0.25F * f1);
                this.tail3V.rotateAngleY = -(f3 * 3.1415927F / 180.0F / 4) + (0.01F * MathHelper.cos(f2 * 0.1F - 2F) * 3.1415927F) + (MathHelper.cos(f * 0.5F - 2.5F) * 0.25F * f1);
                this.tipRear.rotateAngleY = -(f3 * 3.1415927F / 180.0F / 4) + (0.01F * MathHelper.cos(f2 * 0.1F - 2.5F) * 3.1415927F) + (MathHelper.cos(f * 0.5F - 3F) * 0.25F * f1);
            }

            if (entitytitan.isFlying && !this.isRiding && entitytitan.getAnimID() != 2) {
                this.skullZ.rotateAngleY = -(MathHelper.cos(f * 0.35F) * 0.5F * f1);
                this.torsoFront.rotateAngleY = (MathHelper.cos(f * 0.35F - 0.5F) * 0.25F * f1);
                this.centerTorso.rotateAngleY = (MathHelper.cos(f * 0.35F - 1F) * 0.25F * f1);
                this.tail1V.rotateAngleY = (MathHelper.cos(f * 0.35F - 1.5F) * 0.5F * f1);
                this.tail2V.rotateAngleY = (MathHelper.cos(f * 0.35F - 2F) * 0.5F * f1);
                this.tail3V.rotateAngleY = (MathHelper.cos(f * 0.35F - 2.5F) * 0.5F * f1);
                this.tipRear.rotateAngleY = (MathHelper.cos(f * 0.35F - 3F) * 0.5F * f1);
                this.tail1V.rotateAngleX += (MathHelper.cos(f * 0.35F - 1.5F) * 0.1F * f1) - 0.25F + (entitytitan.limbSwingAmount / 4);
                this.tail2V.rotateAngleX += (MathHelper.cos(f * 0.35F - 2F) * 0.1F * f1) - 0.25F + (entitytitan.limbSwingAmount / 4);
                this.tail3V.rotateAngleX += (MathHelper.cos(f * 0.35F - 2.5F) * 0.1F * f1) - 0.25F + (entitytitan.limbSwingAmount / 4);
                this.tipRear.rotateAngleX += (MathHelper.cos(f * 0.35F - 3F) * 0.1F * f1) - 0.25F + (entitytitan.limbSwingAmount / 4);
            }

            float faceYaw = f3 * 3.1415927F / 180.0F;
            float facePitch = f4 * 3.1415927F / 180.0F;
            this.skullZ.rotateAngleX += facePitch * 0.45F;
            this.skullZ.rotateAngleY += faceYaw * 0.45F;
            this.torsoFront.rotateAngleX += facePitch * 0.45F;
            this.torsoFront.rotateAngleY += faceYaw * 0.45F;
            if (entitytitan.getAnimID() == 11) {
                switch (entitytitan.fetchATAAID()) {
                    case 0:
                        this.playGiantAttack1Anti();
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

            if (entitytitan.fetchWaiting()) {
                this.animatorV.rotate(this.skullZ, 1.5F, 0.0F, 0.0F);
                this.animatorV.rotate(this.torsoFront, 1.25F, 0.0F, 0.0F);
                this.animatorV.rotate(this.tail1V, -1.25F, 0.0F, 0.0F);
                this.animatorV.rotate(this.tail2V, -1.25F, 0.0F, 0.0F);
                this.animatorV.rotate(this.tail3V, -1.25F, 0.0F, 0.0F);
                this.animatorV.rotate(this.tipRear, -1.25F, 0.0F, 0.0F);
                this.animatorV.rotate(this.centerTorso, -2.0F, 0.0F, -1.5F);
                this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
            }

            playSlamBody();
            playIncapacitated();
            playShotLightning();
            playSwipeLTail();
            playSwipeRTail();
            playButtHead();
            playSmashTail();
            playUnburrow();
            playBurrow();
            playBirth();
        }

        else {
            playDeath();
        }
    }

    private void playGiantAttack1Anti() {
        this.animatorV.setAnim(11);
        this.animatorV.startPhase(20);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, 6F);
        this.animatorV.rotate(this.tail1V, -0.05F, -0.6F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.05F, -0.6F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.05F, -0.6F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.05F, -0.6F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, -0.75F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.15F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, -12F);
        this.animatorV.rotate(this.tail1V, 1.0F, 0.6F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.6F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.6F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.6F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 4.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, -0.15F, -0.2F);
        this.animatorV.rotate(this.torsoFront, 0.0F, -0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.centerTorso, 0.0F, 6.3F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(0);
    }

    private void playGiantAttack2Anti() {
        this.animatorV.setAnim(11);
        this.animatorV.startPhase(20);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, 6F);
        this.animatorV.rotate(this.tail1V, -0.05F, 0.6F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.05F, 0.6F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.05F, 0.6F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.05F, 0.6F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.75F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, -0.15F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, -0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, -12F);
        this.animatorV.rotate(this.tail1V, 1.0F, -0.6F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.6F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, -0.6F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, -0.6F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, -4.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.15F, 0.2F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.centerTorso, 0.0F, -6.3F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(0);
    }

    private void playGiantAttack3Anti() {
        this.animatorV.setAnim(11);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, -0.3F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.tail1V, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 2.0F, 0.0F, -1.0F);
        this.animatorV.move(this.centerTorso, 0.0F, -8.0F, -16F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(20);
    }

    private void playGiantAttack4Anti() {
        this.animatorV.setAnim(11);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.tail1V, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.8F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.centerTorso, 0.0F, -8.0F, -16F);
        this.animatorV.rotate(this.tail1V, -0.5F, 0.2F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.5F, 0.2F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.5F, 0.2F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.5F, 0.2F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.5F, -4.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.15F, 0.2F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.centerTorso, 6.8F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(0);
    }

    private void playBurrow() {
        this.animatorV.setAnim(1);
        this.animatorV.startPhase(0);
        this.animatorV.move(this.centerTorso, 0.0F, -8.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -2.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, -8.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.3F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -0.4F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, -28.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.6F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, -6.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.6F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, -2.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.3F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.6F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 8.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.6F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 16.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.3F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.6F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 24.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(0);
    }

    private void playUnburrow() {
        this.animatorV.setAnim(2);
        this.animatorV.startPhase(0);
        this.animatorV.rotate(this.skullZ, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 8.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.3F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(10);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -0.4F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, -24.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(20);
    }

    private void playSmashTail() {
        this.animatorV.setAnim(3);
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.tail1V, -0.05F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.05F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.05F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.05F, -0.5F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, -0.75F, 0.0F);
        this.animatorV.rotate(this.skullZ, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.25F, 0.75F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.tail1V, 0.9F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.7F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.6F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.6F, -0.5F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, -0.8F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -0.4F, 0.8F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.tail1V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, -0.75F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.8F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(20);
    }

    private void playButtHead() {
        this.animatorV.setAnim(4);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -2.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, 2.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.3F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, -4.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, 0.75F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.6F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.5F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(15);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(0);
    }

    private void playSwipeRTail() {
        this.animatorV.setAnim(5);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.tail1V, -0.05F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.05F, -0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.05F, -0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.05F, -0.3F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.15F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.tail1V, 0.0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.3F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 3.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, -0.15F, -0.2F);
        this.animatorV.rotate(this.torsoFront, 0.0F, -0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playSwipeLTail() {
        this.animatorV.setAnim(6);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.tail1V, -0.05F, 0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.05F, 0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.05F, 0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.05F, 0.3F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, -0.15F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, -0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.tail1V, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.4F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, -0.3F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, -3.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.15F, -0.2F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.15F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playShotLightning() {
        this.animatorV.setAnim(7);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.tail1V, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.tail1V, 0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.tail1V, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(25);
    }

    private void playIncapacitated() {
        this.animatorV.setAnim(8);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -0.7F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.7F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, -1.25F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.5F, -0.3F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, -0.3F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, -1.25F);
        this.animatorV.rotate(this.tail1V, 0.1F, -0.1F, 0.1F);
        this.animatorV.rotate(this.tail2V, 0.0F, -0.2F, -0.2F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.1F, 0.2F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.2F, 0.3F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(300);
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 3.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.skullZ, 0.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playSlamBody() {
        this.animatorV.setAnim(9);
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.tail1V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.tail1V, 0.5F, -0.25F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, -0.25F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, -0.25F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, -0.25F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.tail1V, 0.5F, 0.25F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, 0.25F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, 0.25F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, 0.25F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.tail1V, 0.5F, -0.25F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, -0.25F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, -0.25F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, -0.25F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.tail1V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.tail1V, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.8F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(60);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, -0.3F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.tail1V, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -0.8F, 0.0F, -1.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(140);
        this.animatorV.rotate(this.skullZ, 1.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.torsoFront, 1.5F, 0.0F, -0.75F);
        this.animatorV.rotate(this.tail1V, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -2.0F, 0.0F, -1.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(40);
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, -0.3F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, -0.75F);
        this.animatorV.rotate(this.tail1V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.0F, 0.0F, -1.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, -0.3F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, -0.75F);
        this.animatorV.rotate(this.tail1V, -0.75F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.0F, 0.0F, -1.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(40);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playDeath() {
        this.animatorV.setAnim(10);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -0.7F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -3.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, -12.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(30);
        this.animatorV.rotate(this.skullZ, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -3.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.2F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -3.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.startPhase(40);
        this.animatorV.rotate(this.skullZ, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -3.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.4F);
        this.animatorV.rotate(this.torsoFront, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -3.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.4F);
        this.animatorV.rotate(this.torsoFront, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -3.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.4F);
        this.animatorV.rotate(this.torsoFront, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -3.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, -0.6F, 0.0F, 0.5F);
        this.animatorV.rotate(this.torsoFront, -0.5F, 0.0F, 0.3F);
        this.animatorV.rotate(this.centerTorso, -3.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, 0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(1760);
        this.animatorV.resetPhase(0);
    }

    private void playBirth() {
        this.animatorV.setAnim(13);
        this.animatorV.startPhase(0);
        this.animatorV.rotate(this.skullZ, 1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail1V, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -2.0F, 0.0F, -1.5F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(60);
        this.animatorV.startPhase(40);
        this.animatorV.rotate(this.skullZ, 1.25F, 0.0F, -0.5F);
        this.animatorV.rotate(this.torsoFront, 1.25F, 0.0F, -0.5F);
        this.animatorV.rotate(this.tail1V, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, -2.0F, 0.0F, -1.5F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(40);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.3F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.75F);
        this.animatorV.rotate(this.tail1V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.0F, 0.0F, -1.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.3F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.75F);
        this.animatorV.rotate(this.tail1V, -0.75F, 1.0F, 0.0F);
        this.animatorV.rotate(this.tail2V, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tail3V, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.tipRear, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 1.0F, 0.0F, -1.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(40);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoFront, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.move(this.centerTorso, 0.0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(40);
    }
}
