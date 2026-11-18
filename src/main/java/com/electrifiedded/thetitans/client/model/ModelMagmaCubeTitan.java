package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMagmaCubeTitan extends ModelBase {
    ModelRenderer[] segments = new ModelRenderer[8];
    ModelRenderer core;

    public ModelMagmaCubeTitan() {
        for (int i = 0; i < this.segments.length; i++) {
            byte b0 = 0;
            int j = i;
            if (i == 2) {
                b0 = 24;
                j = 10;
            }

            else if (i == 3) {
                b0 = 24;
                j = 19;
            }

            this.segments[i] = new ModelRenderer(this, b0, j);
            this.segments[i].addBox(-4.0F, 16 + i, -4.0F, 8, 1, 8);
        }

        this.core = new ModelRenderer(this, 0, 16);
        this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
    }

    public void setLivingAnimations(EntityLivingBase arg78086n1, float arg78086n2, float arg78086n3, float arg78086n4) {
        EntityMagmaCubeTitan entitymagmacube = (EntityMagmaCubeTitan)arg78086n1;
        float f3 = entitymagmacube.prevSquishFactor + (entitymagmacube.squishFactor - entitymagmacube.prevSquishFactor) * arg78086n4;
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i].rotationPointY = (-(4 - i) * f3 * 1.7F);
        }
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        this.core.render(arg78088n7);
        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i].render(arg78088n7);
        }
    }
}
