package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class ModelGhastGuard extends ModelBase {
    ModelRenderer body;
    ModelRenderer[] tentacles = new ModelRenderer[9];

    public ModelGhastGuard() {
        byte b0 = -16;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
        this.body.rotationPointY += 24 + b0;
        Random random = new Random(1660L);
        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i] = new ModelRenderer(this, 0, 0);
            float f = ((i % 3 - i / 3 % 2 * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float f1 = (i / 3 / 2.0F * 2.0F - 1.0F) * 5.0F;
            int j = random.nextInt(7) + 8;
            this.tentacles[i].addBox(-1.0F, 0.0F, -1.0F, 2, j, 2);
            this.tentacles[i].rotationPointX = f;
            this.tentacles[i].rotationPointZ = f1;
            this.body.addChild(this.tentacles[i]);
        }
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i].rotateAngleX = (0.2F * MathHelper.sin(arg78087n3 * 0.3F + i) + 0.4F);
            this.tentacles[i].rotationPointY = 7.0F;
        }

        this.body.rotateAngleY = (arg78087n4 / 57.295776F);
        this.body.rotateAngleX = (arg78087n5 / 57.295776F);
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        this.body.render(arg78088n7);
    }
}
