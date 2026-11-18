package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretGround;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerWitherTurretGroundEnchant implements LayerRenderer<EntityWitherTurretGround> {
    private final RenderLivingBase<EntityWitherTurretGround> renderer;

    public LayerWitherTurretGroundEnchant(RenderLivingBase<EntityWitherTurretGround> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityWitherTurretGround entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.enchantedIs())
            return;
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
