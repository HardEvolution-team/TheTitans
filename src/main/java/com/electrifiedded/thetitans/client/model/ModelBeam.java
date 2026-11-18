package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBeam extends ModelBase {
    public ModelRenderer shape1V;

    public ModelBeam() {
        this.textureWidth = 32;
        this.textureHeight = 256;
        this.shape1V = new ModelRenderer(this, 0, -8);
        this.shape1V.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape1V.addBox(-4.0F, -256.0F, -4.0F, 8, 256, 8, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.shape1V.render(f5);
    }

    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
