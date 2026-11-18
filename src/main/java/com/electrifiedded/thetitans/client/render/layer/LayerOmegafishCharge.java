package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerOmegafishCharge implements LayerRenderer<EntitySilverfishTitan> {
    private final RenderLivingBase<EntitySilverfishTitan> renderer;

    public LayerOmegafishCharge(RenderLivingBase<EntitySilverfishTitan> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntitySilverfishTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.isArmored() || !entity.isEntityAlive())
            return;
        GL11.glDepthMask(!entity.isInvisible());
        float c01 = (float)entity.ticksExisted + partialTicks;
        Minecraft.getMinecraft().getTextureManager().bindTexture(TheTitans.giantWhiteTexture32x64Generic);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        float j1 = MathHelper.cos(c01 * 0.05F) * 3.0F;
        float k1 = c01 * 0.01F;
        GL11.glTranslatef(j1, k1, 0.0F);
        GL11.glMatrixMode(5888);
        GL11.glEnable(3042);
        GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
        GL11.glDisable(2896);
        GL11.glBlendFunc(1, 1);
        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
