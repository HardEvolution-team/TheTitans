package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSkeletonLoyalist extends ModelBiped {
    public boolean armsRaise;

    public ModelSkeletonLoyalist() {
        this(0.0F, false);
    }

    public ModelSkeletonLoyalist(float p_i46303_1_, boolean p_i46303_2_) {
        super(p_i46303_1_, 0.0F, 64, 32);
        if (!p_i46303_2_) {
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

    public void setLivingAnimations(EntityLivingBase arg78086n1, float arg78086n2, float arg78086n3, float arg78086n4) {
        EntitySkeletonMinion entityskeleton = (EntitySkeletonMinion)arg78086n1;
        if (entityskeleton.ticksExisted % 1 == 0) {
            ItemStack itemstack = entityskeleton.getHeldItemMainhand();
            this.rightArmPose = ((itemstack != null) && (itemstack.getItem() == Items.BOW)) ? ModelBiped.ArmPose.BOW_AND_ARROW : ModelBiped.ArmPose.EMPTY;
            this.armsRaise = ((entityskeleton.getAttackTarget() != null) && (entityskeleton.getDistanceSq(entityskeleton.getAttackTarget()) < 36.0D) && (((itemstack != null) && (itemstack.getItem() != Items.BOW)) || (itemstack == null)));
        }

        super.setLivingAnimations(entityskeleton, arg78086n2, arg78086n3, arg78086n4);
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        super.setRotationAngles(arg78087n1, arg78087n2, arg78087n3, arg78087n4, arg78087n5, arg78087n6, arg78087n7);
        if (this.armsRaise) {
            float f6 = MathHelper.sin(this.swingProgress * 3.1415927F);
            float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = (-(0.1F - f6 * 0.6F));
            this.bipedLeftArm.rotateAngleY = (0.1F - f6 * 0.6F);
            this.bipedRightArm.rotateAngleX = -1.5707964F;
            this.bipedLeftArm.rotateAngleX = -1.5707964F;
            this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(arg78087n3 * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(arg78087n3 * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(arg78087n3 * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(arg78087n3 * 0.067F) * 0.05F;
        }
    }
}
