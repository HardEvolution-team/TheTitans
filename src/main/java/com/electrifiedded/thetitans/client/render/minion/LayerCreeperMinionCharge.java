package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelCreeperMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Charged-creeper swirl for creeper minions (original RenderCreeperMinion.shouldRenderPass pass 1). Same as vanilla
 * LayerCreeperCharge, except the original tints it brighter per minion rank: 0.5 + rank * 0.05.
 */
@SideOnly(Side.CLIENT)
public class LayerCreeperMinionCharge implements LayerRenderer<EntityCreeperMinion> {
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderLiving<EntityCreeperMinion> renderer;
    private final ModelCreeperMinion model = new ModelCreeperMinion(2.0F);

    public LayerCreeperMinionCharge(RenderLiving<EntityCreeperMinion> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityCreeperMinion entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.getPowered())
            return;
        boolean invisible = entity.isInvisible();
        GlStateManager.depthMask(!invisible);
        this.renderer.bindTexture(LIGHTNING_TEXTURE);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = (float)entity.ticksExisted + partialTicks;
        GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        float tint = 0.5F + (float)entity.fetchKindIntMinion() * 0.05F;
        GlStateManager.color(tint, tint, tint, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        this.model.setModelAttributes(this.renderer.getMainModel());
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
