package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thetitans.entity.mob.EntityEvilGolem;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelEvilGolem extends ModelBase {
    public ModelRenderer ironGolemHead;
    public ModelRenderer golemHead2Iron;
    public ModelRenderer ironGolemBody;
    public ModelRenderer ironGolemRightArm;
    public ModelRenderer ironGolemLeftArm;
    public ModelRenderer ironGolemLeftLeg;
    public ModelRenderer ironGolemRightLeg;
    public boolean smallerSkullHas;

    public ModelEvilGolem(float p_i1162_1_, float p_i1162_2_) {
        short short1 = 128;
        short short2 = 128;
        this.ironGolemHead = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.ironGolemHead.setRotationPoint(0.0F, 0.0F + p_i1162_2_, -2.0F);
        this.ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, p_i1162_1_);
        this.ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, p_i1162_1_);
        this.golemHead2Iron = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.golemHead2Iron.setRotationPoint(0.0F, 0.0F + p_i1162_2_, -2.0F);
        this.golemHead2Iron.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -5.5F, 8, 8, 8, p_i1162_1_);
        this.ironGolemBody = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.ironGolemBody.setRotationPoint(0.0F, 0.0F + p_i1162_2_, 0.0F);
        this.ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, p_i1162_1_);
        this.ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, p_i1162_1_ + 0.5F);
        this.ironGolemRightArm = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, p_i1162_1_);
        this.ironGolemLeftArm = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, p_i1162_1_);
        this.ironGolemLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(short1, short2);
        this.ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + p_i1162_2_, 0.0F);
        this.ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i1162_1_);
        this.ironGolemRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(short1, short2);
        this.ironGolemRightLeg.mirror = true;
        this.ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + p_i1162_2_, 0.0F);
        this.ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i1162_1_);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        this.setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        this.ironGolemHead.render(arg78088n7);
        this.golemHead2Iron.render(arg78088n7);
        this.ironGolemBody.render(arg78088n7);
        this.ironGolemLeftLeg.render(arg78088n7);
        this.ironGolemRightLeg.render(arg78088n7);
        this.ironGolemRightArm.render(arg78088n7);
        this.ironGolemLeftArm.render(arg78088n7);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        this.ironGolemHead.rotateAngleY = arg78087n4 / (180F / (float)Math.PI);
        this.ironGolemHead.rotateAngleX = arg78087n5 / (180F / (float)Math.PI);
        this.golemHead2Iron.rotateAngleY = arg78087n4 / (180F / (float)Math.PI);
        this.golemHead2Iron.rotateAngleX = arg78087n5 / (180F / (float)Math.PI);
        this.ironGolemLeftLeg.rotateAngleX = -1.5F * this.triangleWave(arg78087n1, 13.0F) * arg78087n2;
        this.ironGolemRightLeg.rotateAngleX = 1.5F * this.triangleWave(arg78087n1, 13.0F) * arg78087n2;
        this.ironGolemLeftLeg.rotateAngleY = 0.0F;
        this.ironGolemRightLeg.rotateAngleY = 0.0F;
        if (smallerSkullHas) {
            this.ironGolemHead.isHidden = true;
            this.golemHead2Iron.isHidden = false;
        }

        else {
            this.golemHead2Iron.isHidden = true;
            this.ironGolemHead.isHidden = false;
        }
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase arg78086n1, float arg78086n2, float arg78086n3, float arg78086n4) {
        EntityEvilGolem entityirongolem = (EntityEvilGolem)arg78086n1;
        int i = entityirongolem.getAttackTimer();
        if (i > 0) {
            this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float)i - arg78086n4, 10.0F);
            this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float)i - arg78086n4, 10.0F);
        }

        else {
            this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(arg78086n2, 13.0F)) * arg78086n3;
            this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(arg78086n2, 13.0F)) * arg78086n3;
        }
    }

    private float triangleWave(float arg78172n1, float arg78172n2) {
        return (Math.abs(arg78172n1 % arg78172n2 - arg78172n2 * 0.5F) - arg78172n2 * 0.25F) / (arg78172n2 * 0.25F);
    }
}
