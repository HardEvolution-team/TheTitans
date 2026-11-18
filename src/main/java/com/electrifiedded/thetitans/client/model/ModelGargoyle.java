package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thetitans.entity.titan.EntityGargoyle;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

/**
 * ModelGargoyle - Either Mojang or a mod author
 * Created using Tabula 5.1.0
 */
public class ModelGargoyle extends ModelBase {
    public ModelRenderer torsoV;
    public ModelRenderer torso;
    public ModelRenderer shankLeft;
    public ModelRenderer shankRight;
    public ModelRenderer skullZ;
    public ModelRenderer limbRight;
    public ModelRenderer limbLeft;
    public ModelRenderer wing1Right;
    public ModelRenderer wing1Left;
    public ModelRenderer hornRight;
    public ModelRenderer hornLeft;
    public ModelRenderer noseV;
    public ModelRenderer limbShoulderRight;
    public ModelRenderer limb1Left;
    public ModelRenderer wing2Right;
    public ModelRenderer flapSkin1Right;
    public ModelRenderer flapSkin2Right;
    public ModelRenderer wing2Left;
    public ModelRenderer flapSkin1Left;
    public ModelRenderer flapSkin2Left;

    public ModelGargoyle() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.limbRight = new ModelRenderer(this, 68, 0);
        this.limbRight.setRotationPoint(-9.0F, -7.0F, 0.0F);
        this.limbRight.addBox(-5.0F, -2.0F, -3.0F, 6, 20, 6, 0.0F);
        this.limbLeft = new ModelRenderer(this, 68, 0);
        this.limbLeft.mirror = true;
        this.limbLeft.setRotationPoint(9.0F, -7.0F, 0.0F);
        this.limbLeft.addBox(-1.0F, -2.0F, -3.0F, 6, 20, 6, 0.0F);
        this.wing1Right = new ModelRenderer(this, 0, 47);
        this.wing1Right.setRotationPoint(-6.0F, -8.0F, 3.0F);
        this.wing1Right.addBox(-12.0F, -1.5F, -1.5F, 12, 3, 3, 0.0F);
        this.assignRotationRotate(wing1Right, 0.3490658503988659F, 0.3490658503988659F, 0.3490658503988659F);
        this.limbShoulderRight = new ModelRenderer(this, 68, 0);
        this.limbShoulderRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.limbShoulderRight.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.assignRotationRotate(limbShoulderRight, -0.0F, 1.0471975511965976F, 1.0471975511965976F);
        this.limb1Left = new ModelRenderer(this, 68, 0);
        this.limb1Left.mirror = true;
        this.limb1Left.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.limb1Left.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.assignRotationRotate(limb1Left, 0.0F, -1.0471975511965976F, -1.0471975511965976F);
        this.flapSkin2Left = new ModelRenderer(this, 0, 61);
        this.flapSkin2Left.mirror = true;
        this.flapSkin2Left.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flapSkin2Left.addBox(-12.0F, 0.0F, 0.0F, 12, 8, 0, 0.0F);
        this.torsoV = new ModelRenderer(this, 0, 36);
        this.torsoV.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.torsoV.addBox(-5.0F, -5.0F, -3.0F, 10, 5, 6, 0.5F);
        this.flapSkin2Right = new ModelRenderer(this, 0, 61);
        this.flapSkin2Right.mirror = true;
        this.flapSkin2Right.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flapSkin2Right.addBox(-12.0F, 0.0F, 0.0F, 12, 8, 0, 0.0F);
        this.shankLeft = new ModelRenderer(this, 46, 0);
        this.shankLeft.mirror = true;
        this.shankLeft.setRotationPoint(5.0F, 0.0F, 0.0F);
        this.shankLeft.addBox(-3.0F, 0.0F, -3.0F, 6, 14, 5, 0.0F);
        this.wing2Right = new ModelRenderer(this, 0, 47);
        this.wing2Right.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.wing2Right.addBox(-12.0F, -1.5F, -1.5F, 12, 3, 3, 0.0F);
        this.assignRotationRotate(wing2Right, 0.0F, 0.0F, -0.17453292519943295F);
        this.hornLeft = new ModelRenderer(this, 0, 0);
        this.hornLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hornLeft.addBox(1.0F, -12.0F, -3.0F, 2, 4, 2, 0.0F);
        this.flapSkin1Right = new ModelRenderer(this, 0, 53);
        this.flapSkin1Right.mirror = true;
        this.flapSkin1Right.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flapSkin1Right.addBox(-12.0F, 0.0F, 0.0F, 12, 8, 0, 0.0F);
        this.shankRight = new ModelRenderer(this, 46, 0);
        this.shankRight.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.shankRight.addBox(-3.0F, 0.0F, -3.0F, 6, 14, 5, 0.0F);
        this.noseV = new ModelRenderer(this, 0, 0);
        this.noseV.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.noseV.addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, 0.0F);
        this.flapSkin1Left = new ModelRenderer(this, 0, 53);
        this.flapSkin1Left.mirror = true;
        this.flapSkin1Left.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flapSkin1Left.addBox(-12.0F, 0.0F, 0.0F, 12, 8, 0, 0.0F);
        this.torso = new ModelRenderer(this, 0, 16);
        this.torso.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.torso.addBox(-9.0F, -10.0F, -5.0F, 18, 10, 10, 0.0F);
        this.hornRight = new ModelRenderer(this, 0, 0);
        this.hornRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hornRight.addBox(-3.0F, -12.0F, -3.0F, 2, 4, 2, 0.0F);
        this.wing1Left = new ModelRenderer(this, 0, 47);
        this.wing1Left.mirror = true;
        this.wing1Left.setRotationPoint(6.0F, -8.0F, 3.0F);
        this.wing1Left.addBox(-12.0F, -1.5F, -1.5F, 12, 3, 3, 0.0F);
        this.assignRotationRotate(wing1Left, -0.3490658503988659F, 2.792526803190927F, -0.3490658503988659F);
        this.skullZ = new ModelRenderer(this, 0, 0);
        this.skullZ.setRotationPoint(0.0F, -10.0F, -2.0F);
        this.skullZ.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.wing2Left = new ModelRenderer(this, 0, 47);
        this.wing2Left.mirror = true;
        this.wing2Left.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.wing2Left.addBox(-12.0F, -1.5F, -1.5F, 12, 3, 3, 0.0F);
        this.assignRotationRotate(wing2Left, 0.0F, 0.0F, -0.17453292519943295F);
        this.torso.addChild(this.limbRight);
        this.torso.addChild(this.limbLeft);
        this.torso.addChild(this.wing1Right);
        this.limbRight.addChild(this.limbShoulderRight);
        this.limbLeft.addChild(this.limb1Left);
        this.wing2Left.addChild(this.flapSkin2Left);
        this.wing2Right.addChild(this.flapSkin2Right);
        this.torsoV.addChild(this.shankLeft);
        this.wing1Right.addChild(this.wing2Right);
        this.skullZ.addChild(this.hornLeft);
        this.wing1Right.addChild(this.flapSkin1Right);
        this.torsoV.addChild(this.shankRight);
        this.skullZ.addChild(this.noseV);
        this.wing1Left.addChild(this.flapSkin1Left);
        this.torsoV.addChild(this.torso);
        this.skullZ.addChild(this.hornRight);
        this.torso.addChild(this.wing1Left);
        this.torso.addChild(this.skullZ);
        this.wing1Left.addChild(this.wing2Left);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.torsoV.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase arg78086n1, float arg78086n2, float arg78086n3, float arg78086n4) {
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        this.torsoV.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.assignRotationRotate(wing1Right, 0.3490658503988659F, 0.3490658503988659F, 0.3490658503988659F);
        this.assignRotationRotate(wing1Left, -0.3490658503988659F, 2.792526803190927F, -0.3490658503988659F);
        this.assignRotationRotate(wing2Right, 0.0F, 0.0F, -0.17453292519943295F);
        this.assignRotationRotate(wing2Left, 0.0F, 0.0F, -0.17453292519943295F);
        float f6 = MathHelper.sin(this.swingProgress * 3.1415927F);
        float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
        this.skullZ.rotateAngleY = arg78087n4 / (180F / (float)Math.PI);
        this.skullZ.rotateAngleX = arg78087n5 / (180F / (float)Math.PI);
        this.limbRight.rotateAngleZ = 0.0F;
        this.limbLeft.rotateAngleZ = 0.0F;
        this.limbRight.rotateAngleX = 0.0F;
        this.limbLeft.rotateAngleX = 0.0F;
        this.shankRight.rotateAngleX = -1.5F * this.triangleWave(arg78087n1, 13.0F) * arg78087n2;
        this.shankLeft.rotateAngleX = 1.5F * this.triangleWave(arg78087n1, 13.0F) * arg78087n2;
        EntityGargoyle entityirongolem = (EntityGargoyle)arg78087n7;
        int i = entityirongolem.getAttackTimer();
        boolean flag = entityirongolem.world.getBlockState(new net.minecraft.util.math.BlockPos(MathHelper.floor(entityirongolem.posX), MathHelper.floor(entityirongolem.getEntityBoundingBox().minY - 0.5D), MathHelper.floor(entityirongolem.posZ))).getBlock() == entityirongolem.fetchCubeToPerchFavorite();
        if (i > 0) {
            this.limbRight.rotateAngleX = -2.0F + 1.5F * this.triangleWave(i, 10.0F);
            this.limbLeft.rotateAngleX = -2.0F + 1.5F * this.triangleWave(i, 10.0F);
        }

        else if (entityirongolem.onGround && !entityirongolem.fetchAggressive()) {
            this.limbRight.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(arg78087n1, 13.0F)) * arg78087n2;
            this.limbLeft.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(arg78087n1, 13.0F)) * arg78087n2;
        }

        this.shankRight.rotateAngleY = 0.0F;
        this.shankLeft.rotateAngleY = 0.0F;
        if (!entityirongolem.onGround || entityirongolem.fetchAggressive()) {
            wing1Right.rotateAngleY += MathHelper.cos(arg78087n3 * 0.5F) * 0.5F;
            wing1Left.rotateAngleY -= MathHelper.cos(arg78087n3 * 0.5F) * 0.5F;
            wing2Right.rotateAngleY += MathHelper.cos(arg78087n3 * 0.5F - 1.5F);
            wing2Left.rotateAngleY -= MathHelper.cos(arg78087n3 * 0.5F - 1.5F);
            this.skullZ.rotateAngleX -= 1.0F;
            this.limbRight.rotateAngleX -= 2.5F - (f6 * 1.2F - f7 * 0.4F);
            this.limbLeft.rotateAngleX -= 2.5F - (f6 * 1.2F - f7 * 0.4F);
            this.shankRight.rotateAngleX = 0F;
            this.shankLeft.rotateAngleX = 0F;
        }

        if (flag) {
            wing1Right.rotateAngleY += 0.5F;
            wing1Left.rotateAngleY -= 0.5F;
            wing2Right.rotateAngleY += -1.5F;
            wing2Left.rotateAngleY -= -1.5F;
            this.limbRight.rotateAngleX -= 0.75F;
            this.limbLeft.rotateAngleX -= 0.75F;
            this.limbRight.rotateAngleZ -= 0.5F;
            this.limbLeft.rotateAngleZ += 0.5F;
            this.shankRight.rotateAngleX = -0.75F;
            this.shankLeft.rotateAngleX = -0.75F;
            this.torsoV.rotationPointY = 18F;
            this.torsoV.rotateAngleX = 1.25F;
            this.skullZ.rotateAngleX -= 1.0F;
        }

        else
            this.torsoV.rotateAngleX = 0F;
    }

    private float triangleWave(float arg78172n1, float arg78172n2) {
        return (Math.abs(arg78172n1 % arg78172n2 - arg78172n2 * 0.5F) - arg78172n2 * 0.25F) / (arg78172n2 * 0.25F);
    }
}
