package com.electrifiedded.thetitans.client.render.mutant;

import com.electrifiedded.thetitans.client.model.mutant.ModelMutantZombieTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantZombieTitan;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMutantZombieTitan extends RenderLiving<EntityMutantZombieTitan> {
    /** MutantZombieRenderer draws the model at 1.3x. */
    private static final float MUTANT_SCALE = 1.3F;

    public RenderMutantZombieTitan(RenderManager rm) {
        super(rm, new ModelMutantZombieTitan(), 0.5F);
    }

    @Override
    protected void preRenderCallback(EntityMutantZombieTitan entity, float partialTicks) {
        float s = entity.fetchScaleMultiplierGiant() * MUTANT_SCALE;
        GlStateManager.scale(s, s, s);
    }

    @Override
    protected float getDeathMaxRotation(EntityMutantZombieTitan entity) {
        return 0.0F; // the rig plays its own collapse
    }

    public static ResourceLocation texture(int variant) {
        final int v = Math.max(0, Math.min(4, variant));
        return MutantTitanTextures.get("mutant_zombie_titan_" + v, MutantTitanTextures.mutant("textures/entity/mutant_zombie.png"), 128, 128, b -> {
            b.canvas(128, 256)
                    .box(0, 128, 7, 3, 7, 8, 8, 8, 8, 0xFFFFFFFF) // jaw: head skin
                    .box(32, 128, 1, 5, 1, 16, 32, 24, 12, 0xFFFFF4E0, 0.12F, 1.7F) // claws: pale bone
                    .box(40, 128, 2, 8, 2, 16, 32, 24, 12, 0xFFFFF4E0, 0.12F, 1.6F) // back spikes
                    .box(52, 128, 2, 6, 2, 16, 32, 24, 12, 0xFFFFF4E0, 0.12F, 1.6F) // shoulder spurs
                    .box(0, 140, 4, 14, 3, 110, 6, 6, 16, 0xFFFFFFFF, 0.7F, 0.8F); // spine ridge: dark arm skin
            if (v != 0)
                b.recolor(0, 0, 128, 256, MutantTitanTextures.VARIANT_TINT[v], MutantTitanTextures.VARIANT_BRIGHT[v]);
        });
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMutantZombieTitan entity) {
        return texture(entity.fetchVariantGiant());
    }
}
