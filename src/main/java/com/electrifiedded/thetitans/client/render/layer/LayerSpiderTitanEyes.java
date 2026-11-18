package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerSpiderTitanEyes implements LayerRenderer<EntitySpiderTitan> {
    private static final ResourceLocation irisesTexturesSpider = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/spider_titan_eyes.png"));
    private static final ResourceLocation irisesTexturesSpider2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/test_spider_titan_eyes.png"));
    private static final ResourceLocation irisesTexturesSpider3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/mycelium_spider_titan_eyes.png"));
    private static final ResourceLocation irisesTexturesSpider4 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/lunar_spider_titan_eyes.png"));
    private static final ResourceLocation irisesTexturesSpider5 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/void_spider_titan_eyes.png"));
    private final RenderLivingBase<EntitySpiderTitan> renderer;

    public LayerSpiderTitanEyes(RenderLivingBase<EntitySpiderTitan> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntitySpiderTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ResourceLocation tex;
        switch (entity.fetchVariantGiant()) {
            case 1: tex = irisesTexturesSpider2; break;
            case 2: tex = irisesTexturesSpider3; break;
            case 3: tex = irisesTexturesSpider4; break;
            case 4: tex = irisesTexturesSpider5; break;
            default: tex = irisesTexturesSpider;
        }

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
        if (entity.getAnimID() == 10 && entity.deathTicks > 200) {
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        }

        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
