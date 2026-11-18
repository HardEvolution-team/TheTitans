package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTitanSpawnEgg extends ModelBase {
    public ModelRenderer coreOvum;
    public ModelRenderer bottomOvum;
    public ModelRenderer side1Ovum;
    public ModelRenderer side2Ovum;
    public ModelRenderer side3Ovum;
    public ModelRenderer side4Ovum;
    public ModelRenderer topOvum;
    public ModelRenderer flameZ;
    public ModelRenderer rod1V;
    public ModelRenderer rod2V;
    public ModelRenderer rod3V;
    public ModelRenderer rod4V;
    public ModelRenderer Item;
    public ModelRenderer fuzzV;
    public ModelRenderer horn1V;
    public ModelRenderer horn11V;
    public ModelRenderer horn2V;
    public ModelRenderer horn22V;
    public ModelRenderer tipOvum;

    public ModelTitanSpawnEgg() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.bottomOvum = new ModelRenderer(this, 16, 0);
        this.bottomOvum.setRotationPoint(0.0F, 2.5F, 0.0F);
        this.bottomOvum.addBox(-1.5F, 0.0F, -1.5F, 3, 1, 3, 0.0F);
        this.side2Ovum = new ModelRenderer(this, 10, 12);
        this.side2Ovum.setRotationPoint(-2.5F, 0.0F, 0.0F);
        this.side2Ovum.addBox(-1.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F);
        this.horn2V = new ModelRenderer(this, 10, 4);
        this.horn2V.setRotationPoint(-6.0F, 0.0F, 0.0F);
        this.horn2V.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.side3Ovum = new ModelRenderer(this, 0, 0);
        this.side3Ovum.setRotationPoint(0.0F, 0.0F, 2.5F);
        this.side3Ovum.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 1, 0.0F);
        this.rod4V = new ModelRenderer(this, 0, 56);
        this.rod4V.setRotationPoint(5.0F, 0.0F, -5.0F);
        this.rod4V.addBox(-0.5F, -3.5F, -0.5F, 1, 7, 1, 0.0F);
        this.tipOvum = new ModelRenderer(this, 50, 0);
        this.tipOvum.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.tipOvum.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
        this.rod3V = new ModelRenderer(this, 0, 56);
        this.rod3V.setRotationPoint(-5.0F, 0.0F, -5.0F);
        this.rod3V.addBox(-0.5F, -3.5F, -0.5F, 1, 7, 1, 0.0F);
        this.horn22V = new ModelRenderer(this, 10, 6);
        this.horn22V.setRotationPoint(-5.5F, -0.5F, 0.0F);
        this.horn22V.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F);
        this.side1Ovum = new ModelRenderer(this, 0, 12);
        this.side1Ovum.setRotationPoint(2.5F, 0.0F, 0.0F);
        this.side1Ovum.addBox(0.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F);
        this.flameZ = new ModelRenderer(this, 19, 49);
        this.flameZ.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flameZ.addBox(-3.5F, -3.0F, -4.0F, 7, 7, 8, 0.0F);
        assignRotationRotate(this.flameZ, 0.0F, 0.7853982F, 0.0F);
        this.horn1V = new ModelRenderer(this, 10, 4);
        this.horn1V.setRotationPoint(3.5F, 0.0F, 0.0F);
        this.horn1V.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.Item = new ModelRenderer(this, 0, 16);
        this.Item.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.Item.addBox(0.0F, -4.0F, -4.0F, 0, 4, 4, 0.0F);
        assignRotationRotate(this.Item, 0.0F, 0.5235988F, 0.0F);
        this.side4Ovum = new ModelRenderer(this, 0, 6);
        this.side4Ovum.setRotationPoint(0.0F, 0.0F, -2.5F);
        this.side4Ovum.addBox(-1.5F, -1.5F, -1.0F, 3, 3, 1, 0.0F);
        this.fuzzV = new ModelRenderer(this, 44, 4);
        this.fuzzV.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.fuzzV.addBox(-5.0F, -5.5F, 0.0F, 10, 7, 0, 0.0F);
        this.rod1V = new ModelRenderer(this, 0, 56);
        this.rod1V.setRotationPoint(5.0F, 0.0F, 5.0F);
        this.rod1V.addBox(-0.5F, -3.5F, -0.5F, 1, 7, 1, 0.0F);
        this.horn11V = new ModelRenderer(this, 10, 6);
        this.horn11V.setRotationPoint(6.0F, -0.5F, 0.0F);
        this.horn11V.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F);
        this.topOvum = new ModelRenderer(this, 40, 0);
        this.topOvum.setRotationPoint(0.0F, -2.5F, 0.0F);
        this.topOvum.addBox(-1.5F, -1.0F, -1.5F, 3, 1, 3, 0.0F);
        this.coreOvum = new ModelRenderer(this, 24, 0);
        this.coreOvum.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.coreOvum.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.rod2V = new ModelRenderer(this, 0, 56);
        this.rod2V.setRotationPoint(-5.0F, 0.0F, 5.0F);
        this.rod2V.addBox(-0.5F, -3.5F, -0.5F, 1, 7, 1, 0.0F);
    }

    public void render(float f5, int eggtypeV) {
        this.coreOvum.render(f5);
        this.bottomOvum.render(f5);
        this.side1Ovum.render(f5);
        this.side2Ovum.render(f5);
        this.side3Ovum.render(f5);
        this.side4Ovum.render(f5);
        this.topOvum.render(f5);
        this.tipOvum.render(f5);
        if (eggtypeV == 1) {
            this.flameZ.rotateAngleY += f5;
        }

        if (eggtypeV == 3) {
            this.fuzzV.render(f5);
        }

        if ((eggtypeV == 1) || (eggtypeV == 2)) {
            this.flameZ.render(f5);
        }

        if (eggtypeV == 2) {
            this.rod1V.render(f5);
            this.rod2V.render(f5);
            this.rod3V.render(f5);
            this.rod4V.render(f5);
        }

        if (eggtypeV == 4) {
            this.Item.render(f5);
        }

        if (eggtypeV == 5) {
            this.horn1V.render(f5);
            this.horn2V.render(f5);
            this.horn11V.render(f5);
            this.horn22V.render(f5);
        }
    }

    public void render(int eggtypeV, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.coreOvum.render(f5);
        this.bottomOvum.render(f5);
        this.side1Ovum.render(f5);
        this.side2Ovum.render(f5);
        this.side3Ovum.render(f5);
        this.side4Ovum.render(f5);
        this.topOvum.render(f5);
        this.tipOvum.render(f5);
        if (eggtypeV == 1) {
            this.flameZ.rotateAngleY = -(f4 * 0.25F);
        }

        if (eggtypeV == 3) {
            this.fuzzV.render(f5);
        }

        if ((eggtypeV == 1) || (eggtypeV == 2)) {
            this.flameZ.render(f5);
        }

        if (eggtypeV == 2) {
            this.rod1V.render(f5);
            this.rod2V.render(f5);
            this.rod3V.render(f5);
            this.rod4V.render(f5);
        }

        if (eggtypeV == 4) {
            this.Item.render(f5);
        }

        if (eggtypeV == 5) {
            this.horn1V.render(f5);
            this.horn2V.render(f5);
            this.horn11V.render(f5);
            this.horn22V.render(f5);
        }
    }

    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
