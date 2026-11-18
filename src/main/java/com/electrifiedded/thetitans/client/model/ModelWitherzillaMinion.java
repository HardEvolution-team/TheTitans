package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWitherzillaMinion extends ModelBase {
    private ModelRenderer[] spine;
    private ModelRenderer[] heads;

    public ModelWitherzillaMinion() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.spine = new ModelRenderer[3];
        this.spine[0] = new ModelRenderer(this, 0, 16);
        this.spine[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3);
        this.spine[1] = new ModelRenderer(this).setTextureSize(this.textureWidth, this.textureHeight);
        this.spine[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
        this.spine[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3);
        this.spine[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2);
        this.spine[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2);
        this.spine[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2);
        this.spine[2] = new ModelRenderer(this, 12, 22);
        this.spine[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3);
        this.heads = new ModelRenderer[3];
        this.heads[0] = new ModelRenderer(this, 0, 0);
        this.heads[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        this.heads[1] = new ModelRenderer(this, 32, 0);
        this.heads[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6);
        this.heads[1].rotationPointX = -8.0F;
        this.heads[1].rotationPointY = 4.0F;
        this.heads[2] = new ModelRenderer(this, 32, 0);
        this.heads[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6);
        this.heads[2].rotationPointX = 10.0F;
        this.heads[2].rotationPointY = 4.0F;
    }

    public int fetchTicksInvulnerable() {
        return 32;
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        ModelRenderer[] amodelrenderer = this.heads;
        int i = amodelrenderer.length;
        for (int j = 0; j < i; j++) {
            ModelRenderer modelrenderer = amodelrenderer[j];
            modelrenderer.render(arg78088n7);
        }

        amodelrenderer = this.spine;
        i = amodelrenderer.length;
        for (int j = 0; j < i; j++) {
            ModelRenderer modelrenderer = amodelrenderer[j];
            modelrenderer.render(arg78088n7);
        }
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        float f6 = MathHelper.cos(arg78087n3 * 0.1F);
        this.spine[1].rotateAngleX = ((0.065F + 0.05F * f6) * 3.1415927F);
        this.spine[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(this.spine[1].rotateAngleX) * 10.0F, -0.5F + MathHelper.sin(this.spine[1].rotateAngleX) * 10.0F);
        this.spine[2].rotateAngleX = ((0.265F + 0.1F * f6) * 3.1415927F);
        this.heads[0].rotateAngleY = (arg78087n4 / 57.295776F);
        this.heads[0].rotateAngleX = (arg78087n5 / 57.295776F);
    }

    public void setLivingAnimations(EntityLivingBase arg78086n1, float arg78086n2, float arg78086n3, float arg78086n4) {
        EntityWitherzillaMinion entitywither = (EntityWitherzillaMinion)arg78086n1;
        for (int i = 1; i < 3; i++) {
            this.heads[i].rotateAngleY = ((entitywither.funcA82207(i - 1) - arg78086n1.renderYawOffset) / 57.295776F);
            this.heads[i].rotateAngleX = (entitywither.funcR82210(i - 1) / 57.295776F);
        }
    }
}
