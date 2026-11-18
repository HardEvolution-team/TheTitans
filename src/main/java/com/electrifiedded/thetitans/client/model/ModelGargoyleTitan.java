package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.client.Animator;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelGargoyleTitan - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
public class ModelGargoyleTitan extends ModelBase {
    public ModelRenderer torsoV;
    public ModelRenderer torso;
    public ModelRenderer shankLeft;
    public ModelRenderer shankRight;
    public ModelRenderer skullZ;
    public ModelRenderer limbRight;
    public ModelRenderer limbLeft;
    public ModelRenderer wing1Right;
    public ModelRenderer wing1Left;
    public ModelRenderer horn1Right;
    public ModelRenderer noseV;
    public ModelRenderer horn1Left;
    public ModelRenderer horn2Right;
    public ModelRenderer horn3Right;
    public ModelRenderer horn2Left;
    public ModelRenderer horn3Left;
    public ModelRenderer limbShoulderRight;
    public ModelRenderer forearmRight;
    public ModelRenderer limbShoulderLeft;
    public ModelRenderer forearmLeft;
    public ModelRenderer wing2Right;
    public ModelRenderer flapSkin1Right;
    public ModelRenderer flapSkin2Right;
    public ModelRenderer wing2Left;
    public ModelRenderer flapSkin1Left;
    public ModelRenderer flapSkin2Left;
    public ModelRenderer footLeft;
    public ModelRenderer footRight;
    private Animator animatorV;

    public ModelGargoyleTitan() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.forearmLeft = new ModelRenderer(this, 68, 16);
        this.forearmLeft.mirror = true;
        this.forearmLeft.setRotationPoint(2.0F, 8.0F, 0.0F);
        this.forearmLeft.addBox(-3.0F, 0.0F, -3.0F, 6, 10, 6, 0.0F);
        this.horn3Right = new ModelRenderer(this, 0, 0);
        this.horn3Right.setRotationPoint(0.0F, -3.0F, -0.5F);
        this.horn3Right.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 2, 0.0F);
        this.assignRotationRotate(horn3Right, -0.8651597102135892F, 0.0F, -0.091106186954104F);
        this.shankLeft = new ModelRenderer(this, 46, 0);
        this.shankLeft.mirror = true;
        this.shankLeft.setRotationPoint(5.0F, 0.0F, 0.0F);
        this.shankLeft.addBox(-3.0F, 0.0F, -3.0F, 6, 7, 5, 0.0F);
        this.skullZ = new ModelRenderer(this, 0, 0);
        this.skullZ.setRotationPoint(0.0F, -10.0F, -2.0F);
        this.skullZ.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.limbShoulderLeft = new ModelRenderer(this, 68, 0);
        this.limbShoulderLeft.mirror = true;
        this.limbShoulderLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.limbShoulderLeft.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.assignRotationRotate(limbShoulderLeft, 0.0F, -1.0471975511965976F, -1.0471975511965976F);
        this.torsoV = new ModelRenderer(this, 0, 36);
        this.torsoV.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.torsoV.addBox(-5.0F, -5.0F, -3.0F, 10, 5, 6, 0.5F);
        this.limbRight = new ModelRenderer(this, 68, 0);
        this.limbRight.setRotationPoint(-9.0F, -7.0F, 0.0F);
        this.limbRight.addBox(-5.0F, -2.0F, -3.0F, 6, 10, 6, 0.0F);
        this.noseV = new ModelRenderer(this, 0, 0);
        this.noseV.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.noseV.addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, 0.0F);
        this.torso = new ModelRenderer(this, 0, 16);
        this.torso.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.torso.addBox(-9.0F, -10.0F, -5.0F, 18, 10, 10, 0.0F);
        this.wing2Left = new ModelRenderer(this, 0, 47);
        this.wing2Left.mirror = true;
        this.wing2Left.setRotationPoint(-18.0F, 0.0F, 0.0F);
        this.wing2Left.addBox(-18.0F, -1.5F, -1.5F, 18, 3, 3, 0.0F);
        this.assignRotationRotate(wing2Left, 0.0F, 0.0F, -0.17453292519943295F);
        this.footLeft = new ModelRenderer(this, 46, 12);
        this.footLeft.mirror = true;
        this.footLeft.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.footLeft.addBox(-3.0F, 0.0F, -3.0F, 6, 7, 5, 0.0F);
        this.horn1Right = new ModelRenderer(this, 0, 0);
        this.horn1Right.setRotationPoint(-2.0F, -7.5F, -1.5F);
        this.horn1Right.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 2, 0.0F);
        this.assignRotationRotate(horn1Right, 0.22759093446006054F, 0.0F, -0.27314402793711257F);
        this.flapSkin1Right = new ModelRenderer(this, 0, 53);
        this.flapSkin1Right.mirror = true;
        this.flapSkin1Right.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flapSkin1Right.addBox(-18.0F, 0.0F, 0.0F, 18, 12, 0, 0.0F);
        this.flapSkin2Left = new ModelRenderer(this, 0, 65);
        this.flapSkin2Left.mirror = true;
        this.flapSkin2Left.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flapSkin2Left.addBox(-18.0F, 0.0F, 0.0F, 18, 12, 0, 0.0F);
        this.forearmRight = new ModelRenderer(this, 68, 16);
        this.forearmRight.setRotationPoint(-2.0F, 8.0F, 0.0F);
        this.forearmRight.addBox(-3.0F, 0.0F, -3.0F, 6, 10, 6, 0.0F);
        this.horn1Left = new ModelRenderer(this, 0, 0);
        this.horn1Left.mirror = true;
        this.horn1Left.setRotationPoint(2.0F, -7.5F, -1.5F);
        this.horn1Left.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 2, 0.0F);
        this.assignRotationRotate(horn1Left, 0.22759093446006054F, 0.0F, 0.27314402793711257F);
        this.wing2Right = new ModelRenderer(this, 0, 47);
        this.wing2Right.setRotationPoint(-18.0F, 0.0F, 0.0F);
        this.wing2Right.addBox(-18.0F, -1.5F, -1.5F, 18, 3, 3, 0.0F);
        this.assignRotationRotate(wing2Right, 0.0F, 0.0F, -0.17453292519943295F);
        this.flapSkin1Left = new ModelRenderer(this, 0, 53);
        this.flapSkin1Left.mirror = true;
        this.flapSkin1Left.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flapSkin1Left.addBox(-18.0F, 0.0F, 0.0F, 18, 12, 0, 0.0F);
        this.shankRight = new ModelRenderer(this, 46, 0);
        this.shankRight.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.shankRight.addBox(-3.0F, 0.0F, -3.0F, 6, 7, 5, 0.0F);
        this.wing1Left = new ModelRenderer(this, 0, 47);
        this.wing1Left.mirror = true;
        this.wing1Left.setRotationPoint(6.0F, -8.0F, 3.0F);
        this.wing1Left.addBox(-18.0F, -1.5F, -1.5F, 18, 3, 3, 0.0F);
        this.assignRotationRotate(wing1Left, -0.3490658503988659F, 2.792526803190927F, -0.3490658503988659F);
        this.footRight = new ModelRenderer(this, 46, 12);
        this.footRight.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.footRight.addBox(-3.0F, 0.0F, -3.0F, 6, 7, 5, 0.0F);
        this.limbLeft = new ModelRenderer(this, 68, 0);
        this.limbLeft.mirror = true;
        this.limbLeft.setRotationPoint(9.0F, -7.0F, 0.0F);
        this.limbLeft.addBox(-1.0F, -2.0F, -3.0F, 6, 10, 6, 0.0F);
        this.wing1Right = new ModelRenderer(this, 0, 47);
        this.wing1Right.setRotationPoint(-6.0F, -8.0F, 3.0F);
        this.wing1Right.addBox(-18.0F, -1.5F, -1.5F, 18, 3, 3, 0.0F);
        this.assignRotationRotate(wing1Right, 0.3490658503988659F, 0.3490658503988659F, 0.3490658503988659F);
        this.horn2Right = new ModelRenderer(this, 0, 0);
        this.horn2Right.setRotationPoint(0.0F, -3.0F, -0.5F);
        this.horn2Right.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 2, 0.0F);
        this.assignRotationRotate(horn2Right, -0.6829473363053812F, 0.0F, -0.091106186954104F);
        this.limbShoulderRight = new ModelRenderer(this, 68, 0);
        this.limbShoulderRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.limbShoulderRight.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.assignRotationRotate(limbShoulderRight, -0.0F, 1.0471975511965976F, 1.0471975511965976F);
        this.horn2Left = new ModelRenderer(this, 0, 0);
        this.horn2Left.mirror = true;
        this.horn2Left.setRotationPoint(0.0F, -3.0F, -0.5F);
        this.horn2Left.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 2, 0.0F);
        this.assignRotationRotate(horn2Left, -0.6829473363053812F, 0.0F, 0.091106186954104F);
        this.horn3Left = new ModelRenderer(this, 0, 0);
        this.horn3Left.mirror = true;
        this.horn3Left.setRotationPoint(0.0F, -3.0F, -0.5F);
        this.horn3Left.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 2, 0.0F);
        this.assignRotationRotate(horn3Left, -0.8651597102135892F, 0.0F, 0.091106186954104F);
        this.flapSkin2Right = new ModelRenderer(this, 0, 65);
        this.flapSkin2Right.mirror = true;
        this.flapSkin2Right.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flapSkin2Right.addBox(-18.0F, 0.0F, 0.0F, 18, 12, 0, 0.0F);
        this.limbLeft.addChild(this.forearmLeft);
        this.horn2Right.addChild(this.horn3Right);
        this.torsoV.addChild(this.shankLeft);
        this.torso.addChild(this.skullZ);
        this.limbLeft.addChild(this.limbShoulderLeft);
        this.torso.addChild(this.limbRight);
        this.skullZ.addChild(this.noseV);
        this.torsoV.addChild(this.torso);
        this.wing1Left.addChild(this.wing2Left);
        this.shankLeft.addChild(this.footLeft);
        this.skullZ.addChild(this.horn1Right);
        this.wing1Right.addChild(this.flapSkin1Right);
        this.wing2Left.addChild(this.flapSkin2Left);
        this.limbRight.addChild(this.forearmRight);
        this.skullZ.addChild(this.horn1Left);
        this.wing1Right.addChild(this.wing2Right);
        this.wing1Left.addChild(this.flapSkin1Left);
        this.torsoV.addChild(this.shankRight);
        this.torso.addChild(this.wing1Left);
        this.shankRight.addChild(this.footRight);
        this.torso.addChild(this.limbLeft);
        this.torso.addChild(this.wing1Right);
        this.horn1Right.addChild(this.horn2Right);
        this.limbRight.addChild(this.limbShoulderRight);
        this.horn1Left.addChild(this.horn2Left);
        this.horn2Left.addChild(this.horn3Left);
        this.wing2Right.addChild(this.flapSkin2Right);
        this.animatorV = new Animator(this);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.torsoV.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void assignAngles() {
        this.torsoV.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.shankRight.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.shankLeft.setRotationPoint(5.0F, 0.0F, 0.0F);
        this.assignRotationRotate(limbShoulderLeft, 0.0F, -1.0471975511965976F, -1.0471975511965976F);
        this.assignRotationRotate(limbShoulderRight, -0.0F, 1.0471975511965976F, 1.0471975511965976F);
        this.assignRotationRotate(wing1Right, 0.3490658503988659F, 0.3490658503988659F, 0.3490658503988659F);
        this.assignRotationRotate(wing1Left, -0.3490658503988659F, 2.792526803190927F, -0.3490658503988659F);
        this.assignRotationRotate(wing2Right, 0.0F, 0.0F, -0.17453292519943295F);
        this.assignRotationRotate(wing2Left, 0.0F, 0.0F, -0.17453292519943295F);
        this.assignRotationRotate(horn1Right, 0.22759093446006054F, 0.0F, -0.27314402793711257F);
        this.assignRotationRotate(horn2Right, -0.6829473363053812F, 0.0F, -0.091106186954104F);
        this.assignRotationRotate(horn3Right, -0.8651597102135892F, 0.0F, -0.091106186954104F);
        this.assignRotationRotate(horn1Left, 0.22759093446006054F, 0.0F, 0.27314402793711257F);
        this.assignRotationRotate(horn2Left, -0.6829473363053812F, 0.0F, 0.091106186954104F);
        this.assignRotationRotate(horn3Left, -0.8651597102135892F, 0.0F, 0.091106186954104F);
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        EntityGargoyleTitan entitytitan = (EntityGargoyleTitan)arg78087n7;
        this.animatorV.update(entitytitan);
        this.assignAngles();
        if (entitytitan.deathTicks <= 0) {
            this.skullZ.rotateAngleY = (arg78087n4 / 57.295776F);
            this.skullZ.rotateAngleX = (arg78087n5 / 57.295776F);
            if (entitytitan.getAnimID() == 0) {
                wing1Right.rotateAngleY += MathHelper.cos(arg78087n3 * 0.05F) * 0.125F;
                wing1Left.rotateAngleY -= MathHelper.cos(arg78087n3 * 0.05F) * 0.125F;
                wing2Right.rotateAngleY += MathHelper.cos(arg78087n3 * 0.05F - 0.2F) * 0.25F;
                wing2Left.rotateAngleY -= MathHelper.cos(arg78087n3 * 0.05F - 0.2F) * 0.25F;
                wing1Right.rotateAngleY += MathHelper.cos(arg78087n3 * 0.3331F) * 0.2F * arg78087n2;
                wing1Left.rotateAngleY -= MathHelper.cos(arg78087n3 * 0.3331F) * 0.2F * arg78087n2;
                wing2Right.rotateAngleY += MathHelper.cos(arg78087n3 * 0.3331F - 0.2F) * 0.2F * arg78087n2;
                wing2Left.rotateAngleY -= MathHelper.cos(arg78087n3 * 0.3331F - 0.2F) * 0.2F * arg78087n2;
                this.shankRight.rotateAngleX = (MathHelper.cos(arg78087n1 * 0.3331F - 0.5F) * 1.25F * arg78087n2);
                this.shankLeft.rotateAngleX = (MathHelper.cos(arg78087n1 * 0.3331F + 2.6415927F) * 1.25F * arg78087n2);
                this.footRight.rotateAngleX = (MathHelper.cos(arg78087n1 * 0.3331F + 3.1415927F) * 1.25F * arg78087n2);
                this.footLeft.rotateAngleX = (MathHelper.cos(arg78087n1 * 0.3331F) * 1.25F * arg78087n2);
                this.limbRight.rotateAngleX = (MathHelper.cos(arg78087n1 * 0.25F + 3.1415927F) * 1.0F * arg78087n2);
                this.limbLeft.rotateAngleX = (MathHelper.cos(arg78087n1 * 0.25F) * 1.0F * arg78087n2);
                this.forearmRight.rotateAngleX = (MathHelper.cos(arg78087n1 * 0.25F + 2.1415927F) * 1.0F * arg78087n2);
                this.forearmLeft.rotateAngleX = (MathHelper.cos(arg78087n1 * 0.25F - 1.0F) * 1.0F * arg78087n2);
            }

            if (this.footRight.rotateAngleX < 0.0F)
                this.footRight.rotateAngleX = 0.0F;
            if (this.footLeft.rotateAngleX < 0.0F)
                this.footLeft.rotateAngleX = 0.0F;
            if (this.forearmRight.rotateAngleX > 0.0F) {
                this.forearmRight.rotateAngleX = 0.0F;
            }

            if (this.forearmLeft.rotateAngleX > 0.0F) {
                this.forearmLeft.rotateAngleX = 0.0F;
            }

            if (!entitytitan.onGround) {
                wing1Right.rotateAngleY += MathHelper.cos(arg78087n3 * 0.2F);
                wing1Left.rotateAngleY -= MathHelper.cos(arg78087n3 * 0.2F);
                wing2Right.rotateAngleY += MathHelper.cos(arg78087n3 * 0.2F - 2F);
                wing2Left.rotateAngleY -= MathHelper.cos(arg78087n3 * 0.2F - 2F);
                this.torsoV.rotateAngleX += 1F;
                this.skullZ.rotateAngleX -= 1F;
                this.limbRight.rotateAngleX = -0.5F;
                this.limbLeft.rotateAngleX = -0.5F;
                this.forearmRight.rotateAngleX = -0.5F;
                this.forearmLeft.rotateAngleX = -0.5F;
                this.shankRight.rotateAngleX = 0.5F;
                this.shankLeft.rotateAngleX = 0.5F;
                this.footRight.rotateAngleX = 0.5F;
                this.footLeft.rotateAngleX = 0.5F;
            }

            this.playGiantStrikeAnti();
            this.playBuffetWing();
            this.playSlam();
            this.playMeteor();
            this.playSpoutWater();
            this.playSpitLava();
            this.playStomp();
            this.playSwat();
            this.playPunch();
        }

        else {
        }
    }

    private void playGiantStrikeAnti() {
        this.animatorV.setAnim(1);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.wing1Right, 0F, 1F, 0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, -1F, -0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, 1F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, -1F, 0F);
        this.animatorV.rotate(this.skullZ, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbLeft, 2F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.wing1Right, 0F, -1F, -1F);
        this.animatorV.rotate(this.wing1Left, 0F, 1F, 1F);
        this.animatorV.rotate(this.wing2Right, 0F, -1F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 1F,0F);
        this.animatorV.rotate(this.skullZ, 0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, -2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbLeft, -2F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(10);
    }

    private void playBuffetWing() {
        this.animatorV.setAnim(2);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.wing1Right, 0F, 1F, 0F);
        this.animatorV.rotate(this.wing1Left, 0F, -1F, 0F);
        this.animatorV.rotate(this.wing2Right, 0F, 1F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, -1F, 0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, -1F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, -1F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.wing1Right, -0.25F, -1F, 0.5F);
        this.animatorV.rotate(this.wing1Left, 0.25F, 1F, -0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -1F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 1F, 0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, -1F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, -1F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.wing1Right, 0F, -0.75F, -0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, 0.75F, 0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -0.75F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 0.75F,0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.wing1Right, 0F, 0.5F, 0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, -0.5F, -0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, 0.5F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, -0.5F,0F);
        this.animatorV.rotate(this.skullZ, 0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.torso, 0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.limbRight, -2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmRight, 0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(60);
        this.animatorV.resetPhase(20);
    }

    private void playSlam() {
        this.animatorV.setAnim(3);
        this.animatorV.startPhase(10);
        this.animatorV.move(this.torsoV, 0.0F, 1.0F, 0.0F);
        this.animatorV.rotate(this.wing1Right, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.wing1Left, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shankRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shankLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(15);
        this.animatorV.move(this.torsoV, 0.0F, -1F, 0.0F);
        this.animatorV.rotate(this.wing1Right, 0F, -0.5F, 0.25F);
        this.animatorV.rotate(this.wing1Left, 0F, 0.5F, -0.25F);
        this.animatorV.rotate(this.wing2Right, 0F, -0.5F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 0.5F,0F);
        this.animatorV.rotate(this.shankRight, -1.6F, 0.2F, 0.0F);
        this.animatorV.rotate(this.shankLeft, 0.0F, 0.2F, 0.0F);
        this.animatorV.rotate(this.footRight, 0.9F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, -2.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbLeft, -2.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmRight, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(15);
        this.animatorV.move(this.torsoV, 0.0F, 4.0F, -1.0F);
        this.animatorV.rotate(this.wing1Right, 0F, 0.5F, -0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, -0.5F, 0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, 0.5F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, -0.5F,0F);
        this.animatorV.rotate(this.shankRight, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shankLeft, -1.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torsoV, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbLeft, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmRight, -0.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.forearmLeft, -0.5F, 0.0F, 0.5F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(30);
    }

    private void playMeteor() {
        this.animatorV.setAnim(4);
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.wing1Right, 0F, -0.75F, -0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, 0.75F, 0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -0.75F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 0.75F,0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(10);
        this.animatorV.rotate(this.wing1Right, 0F, 0.5F, 0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, -0.5F, -0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, 0.5F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, -0.5F,0F);
        this.animatorV.rotate(this.skullZ, 0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.torso, 0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.limbRight, -2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmRight, 0F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(60);
        this.animatorV.resetPhase(20);
    }

    private void playSpoutWater() {
        this.animatorV.setAnim(5);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.wing1Right, 0F, -0.75F, -0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, 0.75F, 0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -0.75F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 0.75F,0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.wing1Right, 0F, 0.5F, 0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, -0.5F, -0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, 0.5F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, -0.5F,0F);
        this.animatorV.rotate(this.noseV, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.5F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.5F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.resetPhase(20);
    }

    private void playSpitLava() {
        this.animatorV.setAnim(6);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.wing1Right, 0F, -0.75F, -0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, 0.75F, 0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -0.75F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 0.75F,0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.wing1Right, 0F, -0.75F, -0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, 0.75F, 0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -0.75F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 0.75F,0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(5);
        this.animatorV.rotate(this.wing1Right, 0F, -0.75F, -0.5F);
        this.animatorV.rotate(this.wing1Left, 0F, 0.75F, 0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -0.75F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 0.75F,0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(10);
        this.animatorV.resetPhase(20);
    }

    private void playStomp() {
        this.animatorV.setAnim(7);
        this.animatorV.startPhase(25);
        this.animatorV.rotate(this.shankRight, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shankLeft, -0.25F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.5F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, -0.5F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -1.6F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -1.6F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(25);
        this.animatorV.rotate(this.shankRight, -2.0F, 0.2F, 0.75F);
        this.animatorV.rotate(this.shankLeft, 0.0F, 0.2F, 0.0F);
        this.animatorV.rotate(this.footRight, 1.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.footLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.torso, -1.0F, 0.0F, 0.25F);
        this.animatorV.rotate(this.limbRight, -0.75F, 0.0F, 0.5F);
        this.animatorV.rotate(this.limbLeft, -0.75F, 0.0F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -0.3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.wing1Right, -0.25F, -1F, 0.5F);
        this.animatorV.rotate(this.wing1Left, 0.25F, 1F, -0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -1F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 1F, 0F);
        this.animatorV.rotate(this.shankRight, -0.5F, 0.5F, 0.0F);
        this.animatorV.rotate(this.shankLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footRight, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 0.1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, -0.4F);
        this.animatorV.rotate(this.limbRight, -0.75F, 0.0F, 0.5F);
        this.animatorV.rotate(this.limbLeft, -0.75F, 0.0F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -0.3F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -0.3F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(5);
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.shankRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shankLeft, -2.0F, 0.0F, -0.75F);
        this.animatorV.rotate(this.footRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 1.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.skullZ, 1.0F, 0.0F, -0.5F);
        this.animatorV.rotate(this.torso, -1.0F, 0.0F, -0.25F);
        this.animatorV.rotate(this.limbRight, 0.8F, 0.0F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.8F, 0.0F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -0.8F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -0.8F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.rotate(this.wing1Right, -0.25F, -1F, 0.5F);
        this.animatorV.rotate(this.wing1Left, 0.25F, 1F, -0.5F);
        this.animatorV.rotate(this.wing2Right, 0F, -1F, 0F);
        this.animatorV.rotate(this.wing2Left, 0F, 1F, 0F);
        this.animatorV.rotate(this.shankRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.shankLeft, -0.5F, -0.5F, 0.0F);
        this.animatorV.rotate(this.footRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, -0.5F);
        this.animatorV.rotate(this.torso, 0.5F, 0.0F, 0.4F);
        this.animatorV.rotate(this.limbRight, 0.75F, 0.0F, 0.5F);
        this.animatorV.rotate(this.limbLeft, 0.75F, 0.0F, -0.5F);
        this.animatorV.rotate(this.forearmRight, -0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -0.75F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.resetPhase(20);
    }

    private void playSwat() {
        this.animatorV.setAnim(8);
        this.animatorV.startPhase(20);
        this.animatorV.move(this.torsoV, 0.0F, 2.0F, 0.0F);
        this.animatorV.rotate(this.shankRight, -0.5F, 0.25F, 0.0F);
        this.animatorV.rotate(this.shankLeft, -0.5F, -0.25F, 0.0F);
        this.animatorV.rotate(this.footRight, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -1.5F, 1.0F, 0.0F);
        this.animatorV.rotate(this.limbLeft, -2F, 0.75F, 1.5F);
        this.animatorV.rotate(this.forearmLeft, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 1.5F, -1.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.torsoV, 0.0F, 6.0F, 0.0F);
        this.animatorV.rotate(this.shankRight, -1.0F, 0.5F, 0.0F);
        this.animatorV.rotate(this.shankLeft, -1.0F, -0.5F, 0.0F);
        this.animatorV.rotate(this.footRight, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbLeft, -2F, -1.25F, -1.5F);
        this.animatorV.rotate(this.forearmLeft, -1.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 2.25F, 1.0F, 0.0F);
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
        this.animatorV.rotate(this.torso, 0.0F, 0.0F, 0.5F);
        this.animatorV.rotate(this.skullZ, 0.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbLeft, -2.5F, 0.0F, 0.5F);
        this.animatorV.rotate(this.forearmLeft, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, 0.75F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmRight, -1.5F, 0.0F, 0.0F);
        this.animatorV.endPhase();
        this.animatorV.startPhase(20);
        this.animatorV.move(this.torsoV, 0.0F, 4.0F, 0.0F);
        this.animatorV.rotate(this.shankRight, -1F, 0.5F, 0.0F);
        this.animatorV.rotate(this.shankLeft, -1F, -0.5F, 0.0F);
        this.animatorV.rotate(this.footRight, 2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.footLeft, 2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.skullZ, -1F, 0.0F, 0.0F);
        this.animatorV.rotate(this.torso, 2.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbRight, -2F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmRight, 0.0F, 0.0F, 0.0F);
        this.animatorV.rotate(this.limbLeft, -1.5F, 0.0F, 0.0F);
        this.animatorV.rotate(this.forearmLeft, -0.5F, 0.0F, 0.5F);
        this.animatorV.endPhase();
        this.animatorV.setStationaryPhase(20);
        this.animatorV.resetPhase(20);
    }
}
