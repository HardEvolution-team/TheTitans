package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.minion.IMinion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerMinionRobes<T extends EntityLivingBase & IMinion> implements LayerRenderer<T> {
    private final RenderLivingBase<T> renderer;
    private final ModelBase meshOverlay;
    private final ModelBase shoesMeshOverlay; // null = no shoes layer (non-biped minions)
    private final ModelBase heraldMeshOverlay;
    private final ModelBase meshIrises;
    private final String overlayRobe;    // e.g. "minions/overlay/blaze_overlay.png"
    private final String overlayShoesZ;   // may be null
    private final String overlayHeraldZ;  // e.g. "minions/overlay/blaze_overlay_herald.png"
    private final String skinGlow;    // e.g. "minions/glow/blaze.png", null disables the eyes pass

    public LayerMinionRobes(RenderLivingBase<T> renderer, ModelBase overlay, ModelBase overlayShoes, ModelBase overlayHerald, ModelBase eyes, String overlayRobe, String overlayShoesZ, String overlayHeraldZ, String skinGlow) {
        this.renderer = renderer;
        this.meshOverlay = overlay;
        this.shoesMeshOverlay = overlayShoes;
        this.heraldMeshOverlay = overlayHerald;
        this.meshIrises = eyes;
        this.overlayRobe = overlayRobe;
        this.overlayShoesZ = overlayShoesZ;
        this.overlayHeraldZ = overlayHeraldZ;
        this.skinGlow = skinGlow;
    }

    // per-rank robe tint (original TheTitans.applyMinionColoringRobes)
    private static void robesColor(int type) {
        switch (type) {
            case 1: GlStateManager.color(0.654902F, 0.34509805F, 0.7019608F, 1.0F); break;
            case 2: GlStateManager.color(0.007843138F, 0.85490197F, 0.85490197F, 1.0F); break;
            case 3: GlStateManager.color(0.007843138F, 0.85882354F, 0.007843138F, 1.0F); break;
            case 4: GlStateManager.color(1.0F, 1.0F, 0.007843138F, 1.0F); break;
            case 5: GlStateManager.color(1.0F, 0.007843138F, 0.007843138F, 1.0F); break;
            case 6: GlStateManager.color(0.007843138F, 0.007843138F, 1.0F, 1.0F); break;
            case 7: GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); break;
            case 8: GlStateManager.color(0.21568628F, 0.21568628F, 0.21568628F, 1.0F); break;
            default: GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    // per-rank shoe tint (original TheTitans.applyMinionColoringShoes)
    private static void shoesColor(int type) {
        switch (type) {
            case 5: GlStateManager.color(0.0F, 0.047058824F, 1.0F, 1.0F); break;
            case 6: GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F); break;
            case 7: GlStateManager.color(0.63529414F, 0.63529414F, 0.63529414F, 1.0F); break;
            case 8: GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F); break;
            default: GlStateManager.color(0.41960785F, 0.41960785F, 0.41960785F, 1.0F);
        }
    }

    private void bind(String path) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TheTitans.getTextureLocation("textures/entities", path));
    }

    private void inflictPose(ModelBase model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        model.setModelAttributes(this.renderer.getMainModel());
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        int type = entity.fetchKindIntMinion();
        // original renders robes/shoes/herald only when the mob is visible; the glow eyes render regardless.
        if (!entity.isInvisible()) {
            if (type > 0) {
                GlStateManager.pushMatrix();
                robesColor(type);
                bind(overlayRobe);
                inflictPose(meshOverlay, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.popMatrix();

                if (shoesMeshOverlay != null && overlayShoesZ != null) {
                    GlStateManager.pushMatrix();
                    shoesColor(type);
                    bind(overlayShoesZ);
                    inflictPose(shoesMeshOverlay, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                }
            }

            if (type > 4) {
                GlStateManager.pushMatrix();
                bind(heraldLabelOverlay(entity));
                inflictPose(heraldMeshOverlay, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.popMatrix();
            }
        }

        if (skinGlow != null && type > 4 && needsGlow(entity)) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            float f = entity.ticksExisted + partialTicks;
            float pulse = 1.0F - MathHelper.cos(f * 0.1F) * 0.2F;
            GlStateManager.color(type == 8 ? pulse : 0.0F, type == 8 ? pulse : 0.5F + MathHelper.cos(f * 0.1F) * 0.2F, pulse, 1.0F);
            float prevX = OpenGlHelper.lastBrightnessX;
            float prevY = OpenGlHelper.lastBrightnessY;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            bind(glowLabelSkin(entity));
            inflictPose(meshIrises, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    // fixed herald file for most minions; skeleton/pigzombie override to swap wither/villager variants.
    protected String heraldLabelOverlay(T entity) {
        return overlayHeraldZ;
    }

    // gate on shooting state etc.; ghast overrides. Default: always glow at herald rank.
    protected boolean needsGlow(T entity) {
        return true;
    }

    // fixed glow file for most minions; skeleton overrides to swap the wither variant.
    protected String glowLabelSkin(T entity) {
        return skinGlow;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
