package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelZombieMinion extends ModelBiped {
    public ModelZombieMinion() {
        this(0.0F);
    }

    public ModelZombieMinion(float p_i1168_1_) {
        this(p_i1168_1_, false);
    }

    public ModelZombieMinion(float p_i1168_1_, boolean p_i1168_2_) {
        super(p_i1168_1_, 0.0F, 64, p_i1168_2_ ? 32 : 64);
    }

    protected ModelZombieMinion(float p_i1167_1_, float p_i1167_2_, int p_i1167_3_, int p_i1167_4_) {
        super(p_i1167_1_, p_i1167_2_, p_i1167_3_, p_i1167_4_);
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        this.setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        if(this.isChild) {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
            GL11.glTranslatef(0.0F, 16.0F * arg78088n7, 0.0F);
            this.bipedHead.render(arg78088n7);
            this.bipedHeadwear.render(arg78088n7);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * arg78088n7, 0.0F);
            this.bipedBody.render(arg78088n7);
            this.bipedRightArm.render(arg78088n7);
            this.bipedLeftArm.render(arg78088n7);
            this.bipedRightLeg.render(arg78088n7);
            this.bipedLeftLeg.render(arg78088n7);
            GL11.glPopMatrix();
        } else {
            this.bipedHead.render(arg78088n7);
            this.bipedBody.render(arg78088n7);
            this.bipedRightArm.render(arg78088n7);
            this.bipedLeftArm.render(arg78088n7);
            this.bipedRightLeg.render(arg78088n7);
            this.bipedLeftLeg.render(arg78088n7);
            this.bipedHeadwear.render(arg78088n7);
        }
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        super.setRotationAngles(arg78087n1, arg78087n2, arg78087n3, arg78087n4, arg78087n5, arg78087n6, arg78087n7);
        this.bipedHead.rotateAngleZ = arg78087n4 / 114.59155F;
        this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
        float f6 = MathHelper.sin(this.swingProgress * 3.1415927F);
        float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
        this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
        this.bipedBody.rotateAngleY = 0.0F;
        if(arg78087n7 instanceof EntityCreature) {
            EntityCreature zombie = (EntityCreature)arg78087n7;
            this.isChild = zombie.isChild();
            float f1 = zombie.limbSwingAmount * 4.0F + f6;
            this.bipedRightArm.rotateAngleX = -(f1 >= 1.6F ? 1.6F : f1) - (f6 * 1.2F - f7 * 0.4F) + MathHelper.cos(arg78087n1 * 0.3331F + 3.1415927F) * 0.5F * arg78087n2 * 0.5F;
            this.bipedLeftArm.rotateAngleX = -(f1 >= 1.6F ? 1.6F : f1) - (f6 * 1.2F - f7 * 0.4F) + MathHelper.cos(arg78087n1 * 0.3331F) * 0.5F * arg78087n2 * 0.5F;
        }

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(arg78087n3 * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(arg78087n3 * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(arg78087n3 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(arg78087n3 * 0.067F) * 0.05F;
    }
}
