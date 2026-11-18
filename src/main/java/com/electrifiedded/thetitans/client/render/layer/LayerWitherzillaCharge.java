package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelWitherzilla;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerWitherzillaCharge implements LayerRenderer<EntityWitherzilla> {
    private static final ResourceLocation sheildWitherzilla = new ResourceLocation(TheTitans.getTextures("textures/entities", "wither_aura.png"));
    private final RenderLivingBase<EntityWitherzilla> renderer;
    private final ModelWitherzilla meshOverlay = new ModelWitherzilla();

    public LayerWitherzillaCharge(RenderLivingBase<EntityWitherzilla> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityWitherzilla entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.isArmored() && !entity.isOmegaFormIn())
            return;
        GL11.glDepthMask(!entity.isInvisible());
        float f1 = (float)entity.ticksExisted + partialTicks;
        Minecraft.getMinecraft().getTextureManager().bindTexture(sheildWitherzilla);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        float f2 = f1 * 0.015F;
        float f3 = f1 * 0.01F;
        GL11.glTranslatef(f2, f3, 0.0F);
        GL11.glMatrixMode(5888);
        GL11.glEnable(3042);
        float f4 = 0.5F;
        GL11.glColor4f(f4, f4, f4, 1.0F);
        GL11.glDisable(2896);
        GL11.glBlendFunc(1, 1);
        this.renderer.getMainModel().setModelAttributes(this.meshOverlay);
        this.meshOverlay.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
