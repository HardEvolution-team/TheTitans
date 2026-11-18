package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSkeletonMinion extends ModelBiped {
    public ModelSkeletonMinion() {
        this(0.0F, false);
    }

    public ModelSkeletonMinion(float p_i46303_1_, boolean p_i46303_2_) {
        super(p_i46303_1_, 0.0F, 64, 32);
        if(!p_i46303_2_) {
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, p_i46303_1_);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
            this.bipedLeftArm = new ModelRenderer(this, 40, 16);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, p_i46303_1_);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedRightLeg = new ModelRenderer(this, 0, 16);
            this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, p_i46303_1_);
            this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
            this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, p_i46303_1_);
            this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        }
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        super.setRotationAngles(arg78087n1, arg78087n2, arg78087n3, arg78087n4, arg78087n5, arg78087n6, arg78087n7);
        float f6 = MathHelper.sin(this.swingProgress * 3.1415927F);
        float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
        if(arg78087n7 instanceof EntityCreature) {
            EntityCreature skeleton = (EntityCreature)arg78087n7;
            this.isChild = skeleton.isChild();
            float f1 = skeleton.limbSwingAmount * 3.0F;
            this.bipedRightArm.rotateAngleX = -(f1 >= 1.6F ? 1.6F : f1) - (f6 * 1.2F - f7 * 0.4F);
            this.bipedLeftArm.rotateAngleX = -(f1 >= 1.6F ? 1.6F : f1) - (f6 * 1.2F - f7 * 0.4F);
        }
    }
}
