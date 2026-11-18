package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerOmegafishEyes implements LayerRenderer<EntitySilverfishTitan> {
    private static final ResourceLocation overlayTexturesSilverfish6 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/magmafish_eyes.png"));
    private static final ResourceLocation overlayTexturesSilverfish7 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/voidfish_eyes.png"));
    private final RenderLivingBase<EntitySilverfishTitan> renderer;

    public LayerOmegafishEyes(RenderLivingBase<EntitySilverfishTitan> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntitySilverfishTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        int variant = entity.fetchVariantGiant();
        if (variant != 5 && variant != 6)
            return;
        ResourceLocation tex = variant == 6 ? overlayTexturesSilverfish7 : overlayTexturesSilverfish6;
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(1, 1);
        GL11.glDepthMask(!entity.isInvisible());
        int c0 = 15728880;
        int j = c0 % 65536;
        int k = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (entity.getAnimID() == 10 && entity.deathTicks > 200)
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glDepthMask(true);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
