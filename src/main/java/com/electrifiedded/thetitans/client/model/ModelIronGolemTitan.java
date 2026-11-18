package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thehippomaster.AnimationAPI.client.Animator;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelIronGolemTitan - Either Mojang or a mod author
 * Created using Tabula 5.1.0
 */
public class ModelIronGolemTitan extends ModelBase {
    public ModelRenderer leg1Left;
    public ModelRenderer leg1Right;
    public ModelRenderer torsoV;
    public ModelRenderer leg2Left;
    public ModelRenderer leg2Right;
    public ModelRenderer torso;
    public ModelRenderer skullZ;
    public ModelRenderer arm1Left;
    public ModelRenderer arm1Right;
    public ModelRenderer noseV;
    public ModelRenderer arm2Left;
    public ModelRenderer arm3Left;
    public ModelRenderer arm4Left;
    public ModelRenderer arm2Right;
    public ModelRenderer arm3Right;
    public ModelRenderer arm4Right;
    private Animator animatorV;

    public ModelIronGolemTitan() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.arm4Left = new ModelRenderer(this, 58, 29);
        this.arm4Left.mirror = true;
        this.arm4Left.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.arm4Left.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
        this.torsoV = new ModelRenderer(this, 0, 41);
        this.torsoV.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.torsoV.addBox(-4.5F, -5.0F, -3.0F, 9, 5, 6, 0.5F);
        this.leg2Right = new ModelRenderer(this, 98, 0);
        this.leg2Right.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.leg2Right.addBox(-3.0F, 0.0F, -2.5F, 6, 8, 5, 0.0F);
        this.arm1Right = new ModelRenderer(this, 58, 13);
        this.arm1Right.setRotationPoint(-9.0F, -9.0F, 0.0F);
        this.arm1Right.addBox(-4.0F, -3.0F, -3.0F, 4, 6, 6, 0.0F);
        this.arm2Right = new ModelRenderer(this, 78, 13);
        this.arm2Right.setRotationPoint(-2.0F, 2.0F, 0.0F);
        this.arm2Right.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
        this.arm3Right = new ModelRenderer(this, 98, 13);
        this.arm3Right.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.arm3Right.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
        this.arm3Left = new ModelRenderer(this, 98, 13);
        this.arm3Left.mirror = true;
        this.arm3Left.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.arm3Left.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
        this.skullZ = new ModelRenderer(this, 0, 0);
        this.skullZ.setRotationPoint(0.0F, -12.0F, -3.0F);
        this.skullZ.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.torso = new ModelRenderer(this, 0, 18);
        this.torso.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.torso.addBox(-9.0F, -12.0F, -6.0F, 18, 12, 11, 0.0F);
        this.leg1Right = new ModelRenderer(this, 76, 0);
        this.leg1Right.setRotationPoint(-4.0F, 8.0F, 0.0F);
        this.leg1Right.addBox(-3.0F, 0.0F, -2.5F, 6, 8, 5, 0.0F);
        this.arm2Left = new ModelRenderer(this, 78, 13);
        this.arm2Left.mirror = true;
        this.arm2Left.setRotationPoint(2.0F, 2.0F, 0.0F);
        this.arm2Left.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
        this.arm1Left = new ModelRenderer(this, 58, 13);
        this.arm1Left.mirror = true;
        this.arm1Left.setRotationPoint(9.0F, -9.0F, 0.0F);
        this.arm1Left.addBox(0.0F, -3.0F, -3.0F, 4, 6, 6, 0.0F);
        this.noseV = new ModelRenderer(this, 0, 0);
        this.noseV.setRotationPoint(0.0F, 0.0F, -5.0F);
        this.noseV.addBox(-1.0F, -3.0F, -1.0F, 2, 4, 2, 0.0F);
        this.arm4Right = new ModelRenderer(this, 58, 29);
        this.arm4Right.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.arm4Right.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
        this.leg1Left = new ModelRenderer(this, 32, 0);
        this.leg1Left.mirror = true;
        this.leg1Left.setRotationPoint(4.0F, 8.0F, 0.0F);
        this.leg1Left.addBox(-3.0F, 0.0F, -2.5F, 6, 8, 5, 0.0F);
        this.leg2Left = new ModelRenderer(this, 54, 0);
        this.leg2Left.mirror = true;
        this.leg2Left.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.leg2Left.addBox(-3.0F, 0.0F, -2.5F, 6, 8, 5, 0.0F);
        this.arm3Left.addChild(this.arm4Left);
        this.leg1Right.addChild(this.leg2Right);
        this.torso.addChild(this.arm1Right);
        this.arm1Right.addChild(this.arm2Right);
        this.arm2Right.addChild(this.arm3Right);
        this.arm2Left.addChild(this.arm3Left);
        this.torso.addChild(this.skullZ);
        this.torsoV.addChild(this.torso);
        this.arm1Left.addChild(this.arm2Left);
        this.torso.addChild(this.arm1Left);
        this.skullZ.addChild(this.noseV);
        this.arm3Right.addChild(this.arm4Right);
        this.leg1Left.addChild(this.leg2Left);
        this.animatorV = new Animator(this);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.playX((IAnimatedEntity)entity, f, f1, f2, f3, f4, f5);
        this.torsoV.render(f5);
        this.leg1Right.render(f5);
        this.leg1Left.render(f5);
    }

    public void assignAngles() {
        this.assignRotationRotate(torso, 0F, 0F, 0F);
        this.assignRotationRotate(torsoV, 0F, 0F, 0F);
        this.assignRotationRotate(leg1Left, 0F, 0F, 0F);
        this.assignRotationRotate(leg1Right, 0F, 0F, 0F);
        this.assignRotationRotate(leg2Left, 0F, 0F, 0F);
        this.assignRotationRotate(leg2Right, 0F, 0F, 0F);
        this.assignRotationRotate(arm1Left, 0F, 0F, 0F);
        this.assignRotationRotate(arm1Right, 0F, 0F, 0F);
        this.assignRotationRotate(arm2Left, 0F, 0F, 0F);
        this.assignRotationRotate(arm2Right, 0F, 0F, 0F);
        this.assignRotationRotate(arm3Left, 0F, 0F, 0F);
        this.assignRotationRotate(arm3Right, 0F, 0F, 0F);
        this.assignRotationRotate(arm4Left, 0F, 0F, 0F);
        this.assignRotationRotate(arm4Right, 0F, 0F, 0F);
        this.arm1Right.setRotationPoint(-9.0F, -9.0F, 0.0F);
        this.arm2Right.setRotationPoint(-2.0F, 1.0F, 0.0F);
        this.arm3Right.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.arm4Right.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.arm1Left.setRotationPoint(9.0F, -9.0F, 0.0F);
        this.arm2Left.setRotationPoint(2.0F, 1.0F, 0.0F);
        this.arm3Left.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.arm4Left.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.noseV.setRotationPoint(0.0F, 0.0F, -5.0F);
        this.skullZ.setRotationPoint(0.0F, -12.0F, -3.0F);
        this.torso.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.torsoV.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.leg1Right.setRotationPoint(-4.0F, 8.0F, 0.0F);
        this.leg2Right.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.leg1Left.setRotationPoint(4.0F, 8.0F, 0.0F);
        this.leg2Left.setRotationPoint(0.0F, 8.0F, 0.0F);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void playX(IAnimatedEntity entity, float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6) {
        EntityIronGolemTitan entitytitan = (EntityIronGolemTitan)entity;
        this.animatorV.update(entitytitan);
        this.assignAngles();
        float fo = 0.6662F / 3;
        if (com.electrifiedded.thetitans.TheTitans.isApril1st(entitytitan.world)) {
            this.arm1Right.rotateAngleZ = 1.5707964F;
            this.arm1Left.rotateAngleZ = -1.5707964F;
        } else if (entitytitan.deathTicks <= 0) {
            this.skullZ.rotateAngleY = (arg78087n4 / 57.295776F);
            this.skullZ.rotateAngleX = (arg78087n5 / 57.295776F);
            this.torsoV.rotateAngleZ = (MathHelper.cos(arg78087n1 * fo) * 0.2F * arg78087n2);
            this.torso.rotateAngleZ = (MathHelper.cos(arg78087n1 * fo - 1F) * 0.2F * arg78087n2);
            if (entitytitan.getAnimID() == 0) {
                this.leg1Right.rotateAngleX = (MathHelper.cos(arg78087n1 * fo - 0.5F) * 0.75F * arg78087n2);
                this.leg1Left.rotateAngleX = (MathHelper.cos(arg78087n1 * fo + 2.6415927F) * 0.75F * arg78087n2);
                this.leg2Right.rotateAngleX = (MathHelper.cos(arg78087n1 * fo + 3.1415927F) * 0.75F * arg78087n2);
                this.leg2Left.rotateAngleX = (MathHelper.cos(arg78087n1 * fo) * 0.75F * arg78087n2);
                this.arm1Right.rotateAngleX = (MathHelper.cos(arg78087n1 * fo + 3.1415927F) * 0.5F * arg78087n2);
                this.arm2Right.rotateAngleX = (MathHelper.cos(arg78087n1 * fo + 2.1415927F) * 0.5F * arg78087n2);
                this.arm3Right.rotateAngleX = (MathHelper.cos(arg78087n1 * fo + 1.6415927F) * 0.5F * arg78087n2);
                this.arm4Right.rotateAngleX = (MathHelper.cos(arg78087n1 * fo + 1.1415927F) * 0.5F * arg78087n2);
                this.arm1Left.rotateAngleX = (MathHelper.cos(arg78087n1 * fo) * 0.5F * arg78087n2);
                this.arm2Left.rotateAngleX = (MathHelper.cos(arg78087n1 * fo - 1.0F) * 0.5F * arg78087n2);
                this.arm3Left.rotateAngleX = (MathHelper.cos(arg78087n1 * fo - 1.5F) * 0.5F * arg78087n2);
                this.arm4Left.rotateAngleX = (MathHelper.cos(arg78087n1 * fo - 2.0F) * 0.5F * arg78087n2);
            }

            if (this.leg2Right.rotateAngleX < 0.0F)
                this.leg2Right.rotateAngleX = 0.0F;
            if (this.leg2Left.rotateAngleX < 0.0F)
                this.leg2Left.rotateAngleX = 0.0F;
            if (this.arm2Right.rotateAngleX > 0.0F) {
                this.arm2Right.rotateAngleX = 0.0F;
            }

            if (this.arm2Left.rotateAngleX > 0.0F) {
                this.arm2Left.rotateAngleX = 0.0F;
            }

            if (this.arm3Right.rotateAngleX > 0.0F) {
                this.arm3Right.rotateAngleX = 0.0F;
            }

            if (this.arm3Left.rotateAngleX > 0.0F) {
                this.arm3Left.rotateAngleX = 0.0F;
            }

            if (this.arm4Right.rotateAngleX > 0.0F) {
                this.arm4Right.rotateAngleX = 0.0F;
            }

            if (this.arm4Left.rotateAngleX > 0.0F) {
                this.arm4Left.rotateAngleX = 0.0F;
            }

            this.playGiantStrikeAnti();
            this.playThrow();
            this.playSlam();
            this.playStomp();
            this.playSwat();
            this.playPunch();
            this.leg1Right.setRotationPoint(-4.0F, torsoV.rotationPointY, 0.0F);
            this.leg1Left.setRotationPoint(4.0F, torsoV.rotationPointY, 0.0F);
        }

        else {
            this.playDeath();
        }
    }

    private void playGiantStrikeAnti() {
        this.animatorV.setAnim(1);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, 2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Left, 2F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, 0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Left, -3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(10);
    }

    private void playThrow() {
        this.animatorV.setAnim(5);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.arm1Right, -2F, 0.0F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -0.5F, 0.0F, -0.5F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playSlam() {
        this.animatorV.setAnim(6);
        this.animatorV.startPhase(15);
        this.animatorV.move(this.torsoV, 0.0F, 1F, 0.0F);
        this.animatorV.rotate(this.leg1Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg1Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.arm1Left, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(15);
        this.animatorV.move(this.torsoV, 0.0F, -1F, 0.0F);
        this.animatorV.rotate(this.leg1Right, -1.6F, 0.2F, 0.0F);
        this.animatorV.rotate(this.leg1Left, 0.0F, 0.2F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.9F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Left, -3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Right, -2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.move(this.torsoV, 0.0F, 8.0F, -1.0F);
        this.animatorV.rotate(this.leg1Right, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg1Left, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoV, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -2F, 0.0F, 0.5F);
        this.animatorV.rotate(this.arm1Left, -2F, 0.0F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.4F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.4F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playStomp() {
        this.animatorV.setAnim(7);
        this.animatorV.startPhase(25);
        this.animatorV.rotate(this.leg1Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg1Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.arm1Left, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(25);
        this.animatorV.rotate(this.leg1Right, -2.0F, 0.2F, 0.75F);
        this.animatorV.rotate(this.leg1Left, 0.0F, 0.2F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 1.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.leg2Left, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, -1.0F, 0.0F, 0.25F);
        this.animatorV.rotate(this.arm1Right, -0.75F, 0.0F, 0.5F);
        this.animatorV.rotate(this.arm1Left, -0.75F, 0.0F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.torsoV, 0.0F, 2.0F, 0.0F);
        this.animatorV.rotate(this.leg1Right, -0.5F, 0.5F, 0.0F);
        this.animatorV.rotate(this.leg1Left, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, -0.4F);
        this.animatorV.rotate(this.arm1Right, 0.9F, 0.0F, 0.5F);
        this.animatorV.rotate(this.arm1Left, 0.9F, 0.0F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(5);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.leg1Right, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg1Left, -2.0F, 0.0F, -0.75F);
        this.animatorV.rotate(this.leg2Right, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 1.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.torso, -1.0F, 0.0F, -0.25F);
        this.animatorV.rotate(this.arm1Right, 0.9F, 0.0F, 0.5F);
        this.animatorV.rotate(this.arm1Left, 0.9F, 0.0F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.torsoV, 0.0F, 2.0F, 0.0F);
        this.animatorV.rotate(this.leg1Right, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg1Left, -0.5F, -0.5F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.4F);
        this.animatorV.rotate(this.arm1Right, 0.9F, 0.0F, 0.5F);
        this.animatorV.rotate(this.arm1Left, 0.9F, 0.0F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.2F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.resetPhase(20);
    }

    private void playSwat() {
        this.animatorV.setAnim(8);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.arm1Left, 2.5F, 0.0F, -1.5F);
        this.animatorV.rotate(this.torso, 1.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.arm1Left, -1.5F, 0.0F, -1.5F);
        this.animatorV.rotate(this.arm2Left, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1.5F, 1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(20);
    }

    private void playPunch() {
        this.animatorV.setAnim(9);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.torso, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Right, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.torso, 1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoV, 1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -1.25F, 0.0F, 0.5F);
        this.animatorV.rotate(this.arm2Right, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.75F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.resetPhase(20);
    }

    private void playDeath() {
        this.animatorV.setAnim(10);
        this.animatorV.startPhase(40);
        this.animatorV.move(this.torsoV, 0.0F, 2.0F, 4.0F);
        this.animatorV.move(this.leg1Right, 0.0F, 2.0F, 4.0F);
        this.animatorV.move(this.leg1Left, 0.0F, 2.0F, 4.0F);
        this.animatorV.rotate(this.leg1Right, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg1Left, -0.65F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.65F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoV, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -0.5F, 0.5F, 0.5F);
        this.animatorV.rotate(this.arm1Left, -0.5F, -0.5F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(40);
        this.animatorV.move(this.torsoV, 0.0F, 2.0F, 8.0F);
        this.animatorV.move(this.leg1Right, 0.0F, 2.0F, 8.0F);
        this.animatorV.move(this.leg1Left, 0.0F, 2.0F, 8.0F);
        this.animatorV.rotate(this.leg1Right, -0.65F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg1Left, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.65F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoV, 0.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -0.5F, 0.5F, 0.5F);
        this.animatorV.rotate(this.arm1Left, -0.5F, -0.5F, -0.5F);
        this.animatorV.rotate(this.arm2Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(40);
        this.animatorV.move(this.torsoV, 0.0F, 2.0F, 9.0F);
        this.animatorV.move(this.leg1Right, 0.0F, 2.0F, 9.0F);
        this.animatorV.move(this.leg1Left, 0.0F, 2.0F, 9.0F);
        this.animatorV.rotate(this.leg1Right, -0.65F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg1Left, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.65F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoV, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -1.25F, 0.4F, 0.0F);
        this.animatorV.rotate(this.arm1Left, -1.25F, -0.4F, 0.0F);
        this.animatorV.rotate(this.arm2Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(80);
        this.animatorV.move(this.torsoV, 0.0F, 10.0F, 1.0F);
        this.animatorV.move(this.leg1Right, 0.0F, 10.0F, 0.0F);
        this.animatorV.move(this.leg1Left, 0.0F, 10.0F, 0.0F);
        this.animatorV.rotate(this.leg1Right, 1.4F, -0.5F, 0.0F);
        this.animatorV.rotate(this.leg1Left, 1.4F, 0.5F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.9F, 0.0F);
        this.animatorV.rotate(this.torso, -0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoV, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -2.8F, 0.0F, -0.5F);
        this.animatorV.rotate(this.arm1Left, -2.8F, 0.0F, 0.5F);
        this.animatorV.rotate(this.arm2Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm2Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm3Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm4Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(100);
        this.animatorV.move(this.torsoV, 0.0F, 10.0F, 1.0F);
        this.animatorV.move(this.leg1Right, 0.0F, 10.0F, 0.0F);
        this.animatorV.move(this.leg1Left, 0.0F, 10.0F, 0.0F);
        this.animatorV.rotate(this.leg1Right, 1.4F, -0.5F, 0.0F);
        this.animatorV.rotate(this.leg1Left, 1.4F, 0.5F, 0.0F);
        this.animatorV.rotate(this.leg2Right, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.leg2Left, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.0F, 0.9F, 0.0F);
        this.animatorV.rotate(this.torso, -0.2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoV, 1.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.arm1Right, -3.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.arm1Left, -3.0F, 0.0F, 0.5F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(1700);
        this.animatorV.resetPhase(0);
    }
}
