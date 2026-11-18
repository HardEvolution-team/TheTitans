package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerSlimeTitanInner implements LayerRenderer<EntitySlimeTitan> {
    private final RenderLivingBase<EntitySlimeTitan> renderer;
    private final ModelBase scaleAmount;

    public LayerSlimeTitanInner(RenderLivingBase<EntitySlimeTitan> renderer, ModelBase scaleAmount) {
        this.renderer = renderer;
        this.scaleAmount = scaleAmount;
    }

    @Override
    public void doRenderLayer(EntitySlimeTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity.isInvisible())
            return;
        GL11.glEnable(2977);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.renderer.getMainModel().setModelAttributes(this.scaleAmount);
        this.scaleAmount.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
