package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelBlazeMinion extends ModelBase {
    private ModelRenderer[] blazeSticks;
    private ModelRenderer blazeHead;

    public ModelBlazeMinion() {
        this(0.0F);
    }

    public ModelBlazeMinion(float overlay) {
        this.blazeSticks = new ModelRenderer[12];

        for(int i = 0; i < this.blazeSticks.length; ++i) {
            this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
            this.blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2, overlay);
        }

        this.blazeHead = new ModelRenderer(this, 0, 0);
        this.blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, overlay + (overlay > 0.01F ? 0.5F : 0.0F));
    }

    public int fetchTicksInvulnerable() {
        return 8;
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        this.setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        this.blazeHead.render(arg78088n7);

        for(int i = 0; i < this.blazeSticks.length; ++i) {
            this.blazeSticks[i].render(arg78088n7);
        }
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        float f6 = arg78087n3 * 3.1415927F * -0.1F;

        int i;
        for(i = 0; i < 4; ++i) {
            this.blazeSticks[i].rotationPointY = -2.0F + MathHelper.cos(((float)(i * 2) + arg78087n3) * 0.25F);
            this.blazeSticks[i].rotationPointX = MathHelper.cos(f6) * 9.0F;
            this.blazeSticks[i].rotationPointZ = MathHelper.sin(f6) * 9.0F;
            ++f6;
        }

        f6 = 0.7853982F + arg78087n3 * 3.1415927F * 0.03F;

        for(i = 4; i < 8; ++i) {
            this.blazeSticks[i].rotationPointY = 2.0F + MathHelper.cos(((float)(i * 2) + arg78087n3) * 0.25F);
            this.blazeSticks[i].rotationPointX = MathHelper.cos(f6) * 7.0F;
            this.blazeSticks[i].rotationPointZ = MathHelper.sin(f6) * 7.0F;
            ++f6;
        }

        f6 = 0.47123894F + arg78087n3 * 3.1415927F * -0.05F;

        for(i = 8; i < 12; ++i) {
            this.blazeSticks[i].rotationPointY = 11.0F + MathHelper.cos(((float)i * 1.5F + arg78087n3) * 0.5F);
            this.blazeSticks[i].rotationPointX = MathHelper.cos(f6) * 5.0F;
            this.blazeSticks[i].rotationPointZ = MathHelper.sin(f6) * 5.0F;
            ++f6;
        }

        this.blazeHead.rotateAngleY = arg78087n4 / 57.295776F;
        this.blazeHead.rotateAngleX = arg78087n5 / 57.295776F;
    }
}
