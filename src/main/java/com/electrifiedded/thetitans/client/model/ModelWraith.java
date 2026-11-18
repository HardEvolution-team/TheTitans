package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thetitans.entity.mob.EntityWraith;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelWraith extends ModelZombie {
    public ModelRenderer lowerTorsoBiped;

    public ModelWraith() {
        this(0.0F);
    }

    public ModelWraith(float p_i1148_1_) {
        this(p_i1148_1_, 0.0F, 64, 32);
    }

    public ModelWraith(float p_i1149_1_, float p_i1149_2_, int p_i1149_3_, int p_i1149_4_) {
        super(p_i1149_1_, false);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.lowerTorsoBiped = new ModelRenderer(this, 16, 32);
        this.lowerTorsoBiped.addBox(-4F, 0F, -2F, 8, 12, 4);
        this.lowerTorsoBiped.setRotationPoint(0F, 10F, 0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        this.setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.25F, 0.0F);
        this.bipedHead.render(arg78088n7);
        this.bipedBody.render(arg78088n7);
        this.lowerTorsoBiped.render(arg78088n7);
        this.bipedRightArm.render(arg78088n7);
        this.bipedLeftArm.render(arg78088n7);
        this.bipedHeadwear.render(arg78088n7);
        GlStateManager.popMatrix();
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        super.setRotationAngles(arg78087n1, arg78087n2, arg78087n3, arg78087n4, arg78087n5, arg78087n6, arg78087n7);
        float f6 = MathHelper.cos(arg78087n3 * 0.1F);
        this.bipedBody.rotateAngleX = (0.0165F + 0.05F * f6) * (float)Math.PI;
        this.lowerTorsoBiped.setRotationPoint(0.0F, 0.0F + MathHelper.cos(this.bipedBody.rotateAngleX) * 11.0F, 0.0F + MathHelper.sin(this.bipedBody.rotateAngleX) * 11.0F);
        this.lowerTorsoBiped.rotateAngleX = (0.065F + 0.1F * f6) * (float)Math.PI;
        this.lowerTorsoBiped.rotateAngleY = 0.0F;
        this.lowerTorsoBiped.rotateAngleZ = 0.0F;
        if (arg78087n7 instanceof EntityWraith) {
            EntityWraith zombie = (EntityWraith)arg78087n7;
            this.isChild = zombie.isChild();
            float f1 = zombie.limbSwingAmount;
            this.lowerTorsoBiped.rotateAngleX += f1;
        }
    }
}
