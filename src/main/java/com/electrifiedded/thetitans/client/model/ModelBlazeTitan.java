package com.electrifiedded.thetitans.client.model;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.client.Animator;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBlazeTitan extends ModelBase {
    private ModelRenderer[] blazeSticks = new ModelRenderer[12];
    private ModelRenderer blazeHead;
    @SuppressWarnings("unused")
    private Animator animatorV;

    public ModelBlazeTitan() {
        this(0.0F);
    }

    public ModelBlazeTitan(float p_i1147_1_) {
        for (int i = 0; i < this.blazeSticks.length; i++) {
            this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
            this.blazeSticks[i].addBox(-1.0F, -4.0F, -1.0F, 2, 8, 2, p_i1147_1_);
        }

        this.blazeHead = new ModelRenderer(this, 0, 0);
        this.blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, p_i1147_1_);
        this.animatorV = new Animator(this);
    }

    public void render(Entity arg78088n1, float arg78088n2, float arg78088n3, float arg78088n4, float arg78088n5, float arg78088n6, float arg78088n7) {
        setRotationAngles(arg78088n2, arg78088n3, arg78088n4, arg78088n5, arg78088n6, arg78088n7, arg78088n1);
        this.blazeHead.render(arg78088n7);
        for (int i = 0; i < this.blazeSticks.length; i++) {
            this.blazeSticks[i].render(arg78088n7);
        }
    }

    public void setRotationAngles(float arg78087n1, float arg78087n2, float arg78087n3, float arg78087n4, float arg78087n5, float arg78087n6, Entity arg78087n7) {
        EntityBlazeTitan entitytitan = (EntityBlazeTitan)arg78087n7;
        float f6 = arg78087n3 * 3.1415927F * -0.008F;
        int i;
        for (i = 0; i < 4; i++) {
            this.blazeSticks[i].rotationPointY = (4.0F + MathHelper.cos((i * 2 + arg78087n3) * 0.03F));
            this.blazeSticks[i].rotationPointX = (MathHelper.cos(f6) * 10.0F);
            this.blazeSticks[i].rotationPointZ = (MathHelper.sin(f6) * 10.0F);
            if (entitytitan.isChargedBlaze()) {
                this.blazeSticks[i].rotateAngleX = MathHelper.cos(arg78087n3 * 0.1F) * (float)Math.PI;
            }

            else {
                this.blazeSticks[i].rotateAngleX = 0F;
            }

            f6 += 1.0F;
        }

        f6 = 0.7853982F + arg78087n3 * 3.1415927F * 0.005F;
        for (i = 4; i < 8; i++) {
            this.blazeSticks[i].rotationPointY = (10.0F + MathHelper.cos((i * 3 + arg78087n3) * 0.05F));
            this.blazeSticks[i].rotationPointX = (MathHelper.cos(f6) * 7.0F);
            this.blazeSticks[i].rotationPointZ = (MathHelper.sin(f6) * 7.0F);
            f6 += 1.0F;
        }

        f6 = 0.47123894F + arg78087n3 * 3.1415927F * -0.003F;
        for (i = 8; i < 12; i++) {
            this.blazeSticks[i].rotationPointY = (17.0F + MathHelper.cos((i * 1.5F + arg78087n3) * 0.02F));
            this.blazeSticks[i].rotationPointX = (MathHelper.cos(f6) * 4.0F);
            this.blazeSticks[i].rotationPointZ = (MathHelper.sin(f6) * 4.0F);
            f6 += 1.0F;
        }

        this.blazeHead.rotateAngleY = (arg78087n4 / 57.295776F);
        this.blazeHead.rotateAngleX = (arg78087n5 / 57.295776F);
        this.blazeHead.rotationPointY = 0F;
    }
}
