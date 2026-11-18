package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSlimeTitan extends ModelBase {
    ModelRenderer slimeBodies;
    ModelRenderer slimeRightEye;
    ModelRenderer slimeLeftEye;
    ModelRenderer slimeMouth;

    public ModelSlimeTitan(int p_i1157_1_) {
        this.slimeBodies = new ModelRenderer(this, 0, p_i1157_1_);
        this.slimeBodies.addBox(-4.0F, 16.0F, -4.0F, 8, 8, 8);
        if (p_i1157_1_ > 0) {
            this.slimeBodies = new ModelRenderer(this, 0, p_i1157_1_);
            this.slimeBodies.addBox(-3.0F, 17.0F, -3.0F, 6, 6, 6);
            this.slimeRightEye = new ModelRenderer(this, 32, 0);
            this.slimeRightEye.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
            this.slimeRightEye.setRotationPoint(-2.25F, 19.0F, -2.5F);
            this.slimeLeftEye = new ModelRenderer(this, 32, 4);
            this.slimeLeftEye.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
            this.slimeLeftEye.setRotationPoint(2.25F, 19.0F, -2.5F);
            this.slimeMouth = new ModelRenderer(this, 32, 8);
            this.slimeMouth.addBox(0.0F, 21.0F, -3.5F, 1, 1, 1);
        }
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        this.slimeBodies.render(arg78088n7);
        if (this.slimeRightEye != null) {
            this.slimeRightEye.render(arg78088n7);
            this.slimeLeftEye.render(arg78088n7);
            this.slimeMouth.render(arg78088n7);
        }
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        if (this.slimeRightEye != null) {
            this.slimeRightEye.rotateAngleY = (arg78087n4 / 57.295776F);
            this.slimeRightEye.rotateAngleX = (arg78087n5 / 57.295776F);
        }

        if (this.slimeLeftEye != null) {
            this.slimeLeftEye.rotateAngleY = (arg78087n4 / 57.295776F);
            this.slimeLeftEye.rotateAngleX = (arg78087n5 / 57.295776F);
        }
    }
}
