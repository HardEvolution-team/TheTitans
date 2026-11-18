package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCreeperMinion extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;

    public ModelCreeperMinion() {
        this(0.0F);
    }

    public ModelCreeperMinion(float p_i1147_1_) {
        byte b0 = 6;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1147_1_ + (p_i1147_1_ > 0.01F ? 0.5F : 0.0F));
        this.head.setRotationPoint(0.0F, (float)b0, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i1147_1_);
        this.body.setRotationPoint(0.0F, (float)b0, 0.0F);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1147_1_);
        this.leg1.setRotationPoint(-2.0F, (float)(12 + b0), 4.0F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1147_1_);
        this.leg2.setRotationPoint(2.0F, (float)(12 + b0), 4.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1147_1_);
        this.leg3.setRotationPoint(-2.0F, (float)(12 + b0), -4.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1147_1_);
        this.leg4.setRotationPoint(2.0F, (float)(12 + b0), -4.0F);
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        this.setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        this.head.render(arg78088n7);
        this.body.render(arg78088n7);
        this.leg1.render(arg78088n7);
        this.leg2.render(arg78088n7);
        this.leg3.render(arg78088n7);
        this.leg4.render(arg78088n7);
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        this.head.rotateAngleY = arg78087n4 / 57.295776F;
        this.head.rotateAngleX = arg78087n5 / 57.295776F;
        this.leg1.rotateAngleX = MathHelper.cos(arg78087n1 * 0.6662F) * 1.4F * arg78087n2;
        this.leg2.rotateAngleX = MathHelper.cos(arg78087n1 * 0.6662F + 3.1415927F) * 1.4F * arg78087n2;
        this.leg3.rotateAngleX = MathHelper.cos(arg78087n1 * 0.6662F + 3.1415927F) * 1.4F * arg78087n2;
        this.leg4.rotateAngleX = MathHelper.cos(arg78087n1 * 0.6662F) * 1.4F * arg78087n2;
    }
}
