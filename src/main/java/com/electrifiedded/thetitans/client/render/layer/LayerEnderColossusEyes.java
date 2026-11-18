package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerEnderColossusEyes implements LayerRenderer<EntityEnderColossus> {
    private static final ResourceLocation irisesSkinEnderman = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/ender_colossus_eyes.png"));
    private static final ResourceLocation irisesSlainSkinEnderman = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/ender_colossus_eyes_dead.png"));
    private static final ResourceLocation irisesSkinEnderman2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/holy_ender_colossus_eyes.png"));
    private static final ResourceLocation irisesSlainSkinEnderman2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/holy_ender_colossus_eyes_dead.png"));
    private static final ResourceLocation irisesSkinEnderman3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/icey_ender_colossus_eyes.png"));
    private static final ResourceLocation irisesSlainSkinEnderman3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/icey_ender_colossus_eyes_dead.png"));
    private final RenderLivingBase<EntityEnderColossus> renderer;

    public LayerEnderColossusEyes(RenderLivingBase<EntityEnderColossus> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityEnderColossus entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        // Original: eyes pass fires only while the eye laser is idle and the death animation has not reached its late frames.
        ResourceLocation tex;
        if (entity.fetchLaserTicksEye() < 0 && (entity.getAnimID() != 10 || entity.deathTicks <= 160)) {
            switch (entity.fetchVariantGiant()) {
                case 1: tex = irisesSkinEnderman2; break;
                case 2: tex = irisesSkinEnderman3; break;
                default: tex = irisesSkinEnderman;
            }
        } else {
            switch (entity.fetchVariantGiant()) {
                case 1: tex = irisesSlainSkinEnderman2; break;
                case 2: tex = irisesSlainSkinEnderman3; break;
                default: tex = irisesSlainSkinEnderman;
            }
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(1, 1);
        GL11.glDisable(2896);
        GL11.glDepthMask(!entity.isInvisible());
        int c0 = 15728880;
        int j = c0 % 65536;
        int k = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glEnable(2896);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
