package com.electrifiedded.thetitans.client.render.mutant;

import com.electrifiedded.thetitans.client.model.mutant.ModelMutantEnderColossus;
import com.electrifiedded.thetitans.client.render.entity.RenderEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Keeps the Ender Colossus renderer (eye laser) but draws the four-armed mutant rig and its glowing eyes. */
@SideOnly(Side.CLIENT)
public class RenderMutantEnderColossus extends RenderEnderColossus {
    private final ModelMutantEnderColossus rig = new ModelMutantEnderColossus();

    public RenderMutantEnderColossus(RenderManager rm) {
        super(rm);
        mainModel = rig;
        layerRenderers.clear();
        addLayer(new EyesLayer(this));
    }

    @Override
    protected void preRenderCallback(EntityEnderColossus entity, float partialTicks) {
        float s = entity.fetchScaleMultiplierGiant();
        GlStateManager.scale(s, s, s);
        // below 1/4 health the lower arms grow out like the mutant's
        float hp = entity.getMaxHealth() > 0 ? entity.getHealth() / entity.getMaxHealth() : 1.0F;
        rig.lowerArmScale(hp < 0.25F ? 1.25F : 1.0F);
    }

    @Override
    protected float getDeathMaxRotation(EntityEnderColossus entity) {
        return 0.0F;
    }

    public static ResourceLocation texture(int variant) {
        final int v = Math.max(0, Math.min(2, variant));
        return MutantTitanTextures.get("mutant_ender_colossus_" + v, MutantTitanTextures.mutant("textures/entity/mutant_enderman/mutant_enderman.png"), 128, 64, b -> {
            b.canvas(128, 128)
                    .box(0, 64, 2, 5, 2, 50, 8, 32, 22, 0xFFFFFFFF, 0.3F, 2.2F) // horns: dark grey from the chest
                    .box(8, 64, 1, 5, 1, 50, 8, 32, 22, 0xFFFFFFFF, 0.3F, 2.8F) // horn tips: lighter
                    .box(12, 64, 1, 4, 1, 50, 8, 32, 22, 0xFFFFFFFF, 0.2F, 3.4F) // tusks: pale
                    .box(16, 64, 1, 6, 1, 92, 0, 12, 25, 0xFFFFFFFF, 0.5F, 1.6F) // back spines: arm skin
                    .box(20, 64, 1, 4, 1, 92, 0, 12, 25, 0xFFFFFFFF, 0.5F, 1.6F); // knee spikes
            // colossus variants: normal, holy, icy
            if (v == 1)
                b.recolor(0, 0, 128, 128, 0xFFFFF0B0, 1.0F);
            else if (v == 2)
                b.recolor(0, 0, 128, 128, 0xFFB0E0FF, 1.0F);
        });
    }

    static ResourceLocation eyes() {
        return MutantTitanTextures.get("mutant_ender_colossus_eyes", MutantTitanTextures.mutant("textures/entity/mutant_enderman/eyes.png"), 128, 64, b -> b.canvas(128, 128));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEnderColossus entity) {
        return texture(entity.fetchVariantGiant());
    }

    static class EyesLayer implements LayerRenderer<EntityEnderColossus> {
        private final RenderMutantEnderColossus renderer;

        EyesLayer(RenderMutantEnderColossus renderer) {
            this.renderer = renderer;
        }

        @Override
        public void doRenderLayer(EntityEnderColossus entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entity.getHealth() <= 0.0F && entity.deathTicks > 160)
                return;
            renderer.bindTexture(eyes());
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(!entity.isInvisible());
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            int i = entity.getBrightnessForRender();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i % 65536, i / 65536);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
