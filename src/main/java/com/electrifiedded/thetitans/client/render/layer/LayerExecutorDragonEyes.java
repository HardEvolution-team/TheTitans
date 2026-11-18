package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityExecutorDragon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

// glowing-eyes pass for the Executor Dragon, same GL state as LayerDragonMinionEyes but with the
// mod's own executor_dragon_eyes texture.
@SideOnly(Side.CLIENT)
public class LayerExecutorDragonEyes implements LayerRenderer<EntityExecutorDragon> {
    private static final ResourceLocation dragonIrisesTexturesExecutor = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/god_titans/executor_dragon_eyes.png"));
    private final RenderLivingBase<EntityExecutorDragon> renderer;

    public LayerExecutorDragonEyes(RenderLivingBase<EntityExecutorDragon> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityExecutorDragon entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity.getInvulTime() > 0)
            return;
        Minecraft.getMinecraft().getTextureManager().bindTexture(dragonIrisesTexturesExecutor);
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
