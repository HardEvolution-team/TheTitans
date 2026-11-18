package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSilverfishMinion extends ModelBase {
    private ModelRenderer[] silverfishBodyParts;
    private ModelRenderer[] silverfishWings;
    private float[] fld78170c;
    private static final int[][] boxLengthSilverfish = new int[][]{{3, 2, 2}, {4, 3, 2}, {6, 4, 3}, {3, 3, 3}, {2, 2, 3}, {2, 1, 2}, {1, 1, 2}};
    private static final int[][] skinPositionsSilverfish = new int[][]{{0, 0}, {0, 4}, {0, 9}, {0, 16}, {0, 22}, {11, 0}, {13, 4}};

    public ModelSilverfishMinion() {
        this(0.0F);
    }

    public ModelSilverfishMinion(float fl) {
        this.silverfishBodyParts = new ModelRenderer[7];
        this.fld78170c = new float[7];
        float f = -3.5F;

        for(int i = 0; i < this.silverfishBodyParts.length; ++i) {
            this.silverfishBodyParts[i] = new ModelRenderer(this, skinPositionsSilverfish[i][0], skinPositionsSilverfish[i][1]);
            this.silverfishBodyParts[i].addBox((float)boxLengthSilverfish[i][0] * -0.5F, 0.0F, (float)boxLengthSilverfish[i][2] * -0.5F, boxLengthSilverfish[i][0], boxLengthSilverfish[i][1], boxLengthSilverfish[i][2], fl);
            this.silverfishBodyParts[i].setRotationPoint(0.0F, (float)(24 - boxLengthSilverfish[i][1]), f);
            this.fld78170c[i] = f;
            if(i < this.silverfishBodyParts.length - 1) {
                f += (float)(boxLengthSilverfish[i][2] + boxLengthSilverfish[i + 1][2]) * 0.5F;
            }
        }

        this.silverfishWings = new ModelRenderer[3];
        this.silverfishWings[0] = new ModelRenderer(this, 20, 0);
        this.silverfishWings[0].addBox(-5.0F, 0.0F, (float)boxLengthSilverfish[2][2] * -0.5F, 10, 8, boxLengthSilverfish[2][2], fl);
        this.silverfishWings[0].setRotationPoint(0.0F, 16.0F, this.fld78170c[2]);
        this.silverfishWings[1] = new ModelRenderer(this, 20, 11);
        this.silverfishWings[1].addBox(-3.0F, 0.0F, (float)boxLengthSilverfish[4][2] * -0.5F, 6, 4, boxLengthSilverfish[4][2], fl);
        this.silverfishWings[1].setRotationPoint(0.0F, 20.0F, this.fld78170c[4]);
        this.silverfishWings[2] = new ModelRenderer(this, 20, 18);
        this.silverfishWings[2].addBox(-3.0F, 0.0F, (float)boxLengthSilverfish[4][2] * -0.5F, 6, 5, boxLengthSilverfish[1][2], fl);
        this.silverfishWings[2].setRotationPoint(0.0F, 19.0F, this.fld78170c[1]);
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        this.setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);

        int i;
        for(i = 0; i < this.silverfishBodyParts.length; ++i) {
            this.silverfishBodyParts[i].render(arg78088n7);
        }

        for(i = 0; i < this.silverfishWings.length; ++i) {
            this.silverfishWings[i].render(arg78088n7);
        }
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        float f1 = 0.0F;
        if(arg78087n7 instanceof EntityCreature) {
            EntityCreature i = (EntityCreature)arg78087n7;
            f1 = i.limbSwingAmount;
            if(f1 >= 0.9F) {
                f1 = 0.9F;
            }

            if(i.onGround) {
                arg78087n3 *= f1;
            }

            if(f1 <= 0.0F) {
                arg78087n3 = 3.5F;
            }
        }

        for(int var10 = 0; var10 < this.silverfishBodyParts.length; ++var10) {
            this.silverfishBodyParts[var10].rotateAngleY = MathHelper.cos(arg78087n3 * 0.9F + (float)var10 * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(var10 - 2));
            this.silverfishBodyParts[var10].rotationPointX = MathHelper.sin(arg78087n3 * 0.9F + (float)var10 * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(var10 - 2);
        }

        this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
        this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
        this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
        this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
        this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
    }
}
