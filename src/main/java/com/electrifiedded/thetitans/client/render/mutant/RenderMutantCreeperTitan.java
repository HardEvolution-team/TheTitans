package com.electrifiedded.thetitans.client.render.mutant;

import com.electrifiedded.thetitans.client.model.mutant.ModelMutantCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantCreeperTitan;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMutantCreeperTitan extends RenderLiving<EntityMutantCreeperTitan> {
    /** MutantCreeperRenderer draws the model at 1.2x. */
    private static final float MUTANT_SCALE = 1.2F;
    private static final ResourceLocation CHARGE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    public RenderMutantCreeperTitan(RenderManager rm) {
        super(rm, new ModelMutantCreeperTitan(), 0.75F);
        addLayer(new ChargeLayer(this));
    }

    @Override
    protected void preRenderCallback(EntityMutantCreeperTitan entity, float partialTicks) {
        float f1 = entity.getCreeperFlashIntensity(partialTicks);
        float f2 = 1.0F + MathHelper.sin(f1 * 100.0F) * f1 * 0.01F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 *= f1;
        f1 *= f1;
        float s = entity.fetchScaleMultiplierGiant() * MUTANT_SCALE;
        GlStateManager.scale((1.0F + f1 * 0.4F) * f2 * s, (1.0F + f1 * 0.1F) / f2 * s, (1.0F + f1 * 0.4F) * f2 * s);
    }

    @Override
    protected int getColorMultiplier(EntityMutantCreeperTitan entity, float lightBrightness, float partialTickTime) {
        float f = entity.getCreeperFlashIntensity(partialTickTime);
        if ((int)(f * 10.0F) % 2 == 0)
            return 0;
        int i = MathHelper.clamp((int)(f * 0.2F * 255.0F), 0, 255);
        return i << 24 | 0xFFFFFF;
    }

    @Override
    protected float getDeathMaxRotation(EntityMutantCreeperTitan entity) {
        return 0.0F;
    }

    public static ResourceLocation texture(int variant) {
        final int v = Math.max(0, Math.min(4, variant));
        return MutantTitanTextures.get("mutant_creeper_titan_" + v, MutantTitanTextures.mutant("textures/entity/mutant_creeper.png"), 128, 64, b -> {
            b.canvas(128, 128)
                    .box(0, 64, 2, 6, 2, 36, 7, 9, 16, 0xFFFFFFFF, 0.6F, 0.6F) // dorsal vents: dark body skin
                    .box(8, 64, 1, 2, 3, 96, 8, 7, 20, 0xFFFFFFFF, 0.1F, 0.55F) // toe claws: dark grey
                    .box(16, 64, 4, 4, 8, 0, 8, 10, 14, 0xFFFFFFFF) // tail: pelvis skin
                    .box(40, 64, 3, 3, 7, 0, 8, 10, 14, 0xFFFFFFFF, 1.0F, 0.9F) // tail tip
                    .box(60, 64, 1, 6, 4, 10, 32, 10, 12, 0xFFFFFFFF, 0.8F, 0.85F); // gill fins: head skin
            // creeper titan variants: normal, hell, ghost, ender, void
            int[] tint = { 0xFFFFFFFF, 0xFFFF8050, 0xFFE8F4FF, 0xFFB080FF, 0xFF9A70E0 };
            float[] bright = { 1.0F, 1.0F, 1.25F, 0.8F, 0.7F };
            if (v != 0)
                b.recolor(0, 0, 128, 128, tint[v], bright[v]);
        });
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMutantCreeperTitan entity) {
        return texture(entity.fetchVariantGiant());
    }

    /** Charged creeper aura on the mutant rig. */
    static class ChargeLayer implements LayerRenderer<EntityMutantCreeperTitan> {
        private final RenderMutantCreeperTitan renderer;
        private final ModelMutantCreeperTitan shell = new ModelMutantCreeperTitan(2.0F);

        ChargeLayer(RenderMutantCreeperTitan renderer) {
            this.renderer = renderer;
        }

        @Override
        public void doRenderLayer(EntityMutantCreeperTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (!entity.getPowered())
                return;
            boolean invisible = entity.isInvisible();
            GlStateManager.depthMask(!invisible);
            renderer.bindTexture(CHARGE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = entity.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            shell.setModelAttributes(renderer.getMainModel());
            shell.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            shell.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
}
