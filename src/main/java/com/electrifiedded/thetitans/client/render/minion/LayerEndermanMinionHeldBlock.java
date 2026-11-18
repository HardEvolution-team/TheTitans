package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Carried block for enderman minions (donor renderEquippedItems); mirrors vanilla LayerHeldBlock. */
@SideOnly(Side.CLIENT)
public class LayerEndermanMinionHeldBlock implements LayerRenderer<EntityEndermanMinion> {
    private final RenderLiving<EntityEndermanMinion> renderer;

    public LayerEndermanMinionHeldBlock(RenderLiving<EntityEndermanMinion> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityEndermanMinion entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        IBlockState state = entity.getHeldBlockState();
        if (state == null)
            return;
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.6875F, -0.75F);
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.25F, 0.1875F, 0.25F);
        GlStateManager.scale(-0.5F, -0.5F, 0.5F);
        int light = entity.getBrightnessForRender();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(light % 65536), (float)(light / 65536));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, 1.0F);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
