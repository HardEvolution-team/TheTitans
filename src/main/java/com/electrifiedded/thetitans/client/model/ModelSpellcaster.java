package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thetitans.entity.mob.EntityMagicUser;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSpellcaster extends ModelBiped {
    public boolean spellCasting;

    public ModelSpellcaster() {
        super(0.0F);
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        EntityMagicUser entity = (EntityMagicUser)arg78087n7;
        spellCasting = entity.isSpellCasting();
        if (entity.fetchKindMob() == 1) {
            rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
        }

        super.setRotationAngles(arg78087n1, arg78087n2, arg78087n3, arg78087n4, arg78087n5, arg78087n6, arg78087n7);
        if (entity.fetchKindMob() == 0) {
            float f6 = MathHelper.sin(this.swingProgress * (float)Math.PI);
            float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
            this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
            this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F);
            this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F);
            this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(arg78087n3 * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(arg78087n3 * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(arg78087n3 * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(arg78087n3 * 0.067F) * 0.05F;
        }

        if (spellCasting) {
            MathHelper.cos(arg78087n3 * 0.1F);
            this.bipedRightArm.rotateAngleX = MathHelper.cos(arg78087n3 * 0.6662F) * 0.25F;
            this.bipedLeftArm.rotateAngleX = MathHelper.cos(arg78087n3 * 0.6662F) * 0.25F;
            this.bipedRightArm.rotateAngleZ = 2.3561945F;
            this.bipedLeftArm.rotateAngleZ = -2.3561945F;
            this.bipedRightArm.rotateAngleY = 0.0F;
            this.bipedLeftArm.rotateAngleY = 0.0F;
        }
    }
}
