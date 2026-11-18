package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelOptimaAxe - Undefined
 * Created using Tabula 4.1.1
 */
public class ModelOptimaAxe extends ModelBase {
    public ModelRenderer gripV;
    public ModelRenderer handleV;
    public ModelRenderer blade1V;
    public ModelRenderer blade2V;

    public ModelOptimaAxe() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.gripV = new ModelRenderer(this, 0, 0);
        this.gripV.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.gripV.addBox(-8.0F, -48.0F, -8.0F, 16, 48, 16, 0.0F);
        this.handleV = new ModelRenderer(this, 96, 0);
        this.handleV.setRotationPoint(0.0F, -48.0F, 0.0F);
        this.handleV.addBox(-4.0F, -120.0F, -4.0F, 8, 120, 8, 0.0F);
        this.blade2V = new ModelRenderer(this, 0, 16);
        this.blade2V.mirror = true;
        this.blade2V.setRotationPoint(0.0F, -116.0F, 0.0F);
        this.blade2V.addBox(0.0F, -32.0F, 4.0F, 0, 64, 48, 0.0F);
        this.assignRotationRotate(blade2V, 0.0F, 3.141592653589793F, 0.0F);
        this.blade1V = new ModelRenderer(this, 0, 16);
        this.blade1V.setRotationPoint(0.0F, -116.0F, 0.0F);
        this.blade1V.addBox(0.0F, -32.0F, 4.0F, 0, 64, 48, 0.0F);
        this.gripV.addChild(this.handleV);
        this.handleV.addChild(this.blade2V);
        this.handleV.addChild(this.blade1V);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.gripV.render(f5);
    }

    public void render(float f5) {
        this.gripV.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
