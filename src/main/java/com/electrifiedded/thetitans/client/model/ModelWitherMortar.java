package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelWitherMortar extends net.minecraft.client.model.ModelBase {
    public ModelRenderer support;
    public ModelRenderer skullZ;
    public ModelRenderer tripodShankBack;
    public ModelRenderer frontTripodShankRight;
    public ModelRenderer frontTripodShankLeft;
    public ModelRenderer tripodShankTipBack;
    public ModelRenderer handle;

    public ModelWitherMortar() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.handle = new ModelRenderer(this, 24, 22);
        this.handle.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.handle.addBox(-5.5F, -1.0F, -1.0F, 11, 2, 2, 0.0F);
        this.skullZ = new ModelRenderer(this, 0, 0);
        this.skullZ.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.skullZ.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
        this.tripodShankBack = new ModelRenderer(this, 0, 22);
        this.tripodShankBack.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.tripodShankBack.addBox(-1.5F, 0.0F, -1.5F, 3, 10, 3, 0.0F);
        assignRotationRotate(this.tripodShankBack, 1.3089969F, 0.0F, 0.0F);
        this.support = new ModelRenderer(this, 0, 22);
        this.support.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.support.addBox(-1.5F, 0.0F, -1.5F, 3, 10, 3, 0.0F);
        this.tripodShankTipBack = new ModelRenderer(this, 12, 22);
        this.tripodShankTipBack.setRotationPoint(0.0F, 18.0F, 9.0F);
        this.tripodShankTipBack.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.frontTripodShankRight = new ModelRenderer(this, 24, 22);
        this.frontTripodShankRight.mirror = true;
        this.frontTripodShankRight.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.frontTripodShankRight.addBox(0.0F, -1.0F, -1.0F, 11, 2, 2, 0.0F);
        assignRotationRotate(this.frontTripodShankRight, -0.61086524F, 2.6179938F, -0.87266463F);
        this.frontTripodShankLeft = new ModelRenderer(this, 24, 22);
        this.frontTripodShankLeft.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.frontTripodShankLeft.addBox(-11.0F, -1.0F, -1.0F, 11, 2, 2, 0.0F);
        assignRotationRotate(this.frontTripodShankLeft, -0.61086524F, -2.6179938F, 0.87266463F);
    }

    public void render(net.minecraft.entity.Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.handle.render(f5);
        this.skullZ.render(f5);
        this.tripodShankBack.render(f5);
        this.support.render(f5);
        this.tripodShankTipBack.render(f5);
        this.frontTripodShankRight.render(f5);
        this.frontTripodShankLeft.render(f5);
    }

    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, net.minecraft.entity.Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.skullZ.rotateAngleY = (f3 / 57.295776F);
        this.skullZ.rotateAngleX = (f4 / 57.295776F);
    }
}
