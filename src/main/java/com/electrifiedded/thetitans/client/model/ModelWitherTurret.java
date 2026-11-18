package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWitherTurret extends ModelBase {
    private ModelRenderer pole1V;
    private ModelRenderer pole2V;
    private ModelRenderer skullZ;

    public ModelWitherTurret(float f) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.pole1V = new ModelRenderer(this, 0, 22);
        this.pole1V.addBox(-1.5F, -10.0F, -1.5F, 3, 10, 3, f);
        this.pole1V.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.pole2V = new ModelRenderer(this, 0, 22);
        this.pole2V.addBox(-1.5F, -10.0F, -1.5F, 3, 10, 3, f);
        this.pole2V.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.skullZ = new ModelRenderer(this, 0, 0);
        this.skullZ.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, f);
        this.skullZ.setRotationPoint(0.0F, 2.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.pole1V.render(f5);
        this.pole2V.render(f5);
        this.skullZ.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.skullZ.rotateAngleY = (f3 / 57.295776F);
        this.skullZ.rotateAngleX = (f4 / 57.295776F);
    }
}
