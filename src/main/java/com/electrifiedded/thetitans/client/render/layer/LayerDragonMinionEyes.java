package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerDragonMinionEyes implements LayerRenderer<EntityDragonMinion> {
    private static final ResourceLocation dragonIrisesTexturesEnder = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
    private final RenderLivingBase<EntityDragonMinion> renderer;

    public LayerDragonMinionEyes(RenderLivingBase<EntityDragonMinion> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityDragonMinion entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(dragonIrisesTexturesEnder);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(1, 1);
        GL11.glDisable(2896);
        GL11.glDepthFunc(514);
        int c0 = 0xf0f0;
        int j = c0 % 65536;
        int k = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glEnable(2896);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glDepthFunc(515);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
