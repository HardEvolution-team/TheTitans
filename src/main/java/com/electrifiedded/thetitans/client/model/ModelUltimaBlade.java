package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelUltimaBlade extends ModelBase {
    public ModelRenderer handleV;
    public ModelRenderer guardV;
    public ModelRenderer lowerBlade;
    public ModelRenderer tipBlade;

    public ModelUltimaBlade() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.tipBlade = new ModelRenderer(this, 16, 48);
        this.tipBlade.setRotationPoint(0.0F, -72.0F, 0.0F);
        this.tipBlade.addBox(0.0F, -72.0F, -4.0F, 0, 72, 8, 0.0F);
        assignRotationRotate(this.tipBlade, -0.017453292F, 0.0F, 0.0F);
        this.guardV = new ModelRenderer(this, 48, 0);
        this.guardV.setRotationPoint(0.0F, -48.0F, 0.0F);
        this.guardV.addBox(-10.0F, -2.0F, -10.0F, 20, 2, 20, 0.0F);
        this.handleV = new ModelRenderer(this, 0, 0);
        this.handleV.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.handleV.addBox(-4.0F, -48.0F, -4.0F, 8, 48, 8, 0.0F);
        this.lowerBlade = new ModelRenderer(this, 0, 48);
        this.lowerBlade.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerBlade.addBox(0.0F, -72.0F, -4.0F, 0, 72, 8, 0.0F);
        this.lowerBlade.addChild(this.tipBlade);
        this.handleV.addChild(this.guardV);
        this.guardV.addChild(this.lowerBlade);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.handleV.render(f5);
    }

    public void render(float f5) {
        this.handleV.render(f5);
    }

    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
