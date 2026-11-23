package com.electrifiedded.thetitans.client.render.mutant;

import com.electrifiedded.thetitans.client.model.mutant.ModelMutantSkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantSkeletonTitan;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMutantSkeletonTitan extends RenderLiving<EntityMutantSkeletonTitan> {
    /** skeleton titan variants: normal, sand, guitar, sleepy, void; index 5 = wither. */
    private static final int[] TINT = { 0xFFFFFFFF, 0xFFF2DCA8, 0xFFE0E0FF, 0xFFD8E8FF, 0xFF9A70E0, 0xFF5A5A5A };
    private static final float[] BRIGHT = { 1.0F, 1.0F, 1.0F, 0.95F, 0.75F, 0.55F };

    public RenderMutantSkeletonTitan(RenderManager rm) {
        super(rm, new ModelMutantSkeletonTitan(), 0.5F);
    }

    /** Once it has cracked open the titan has fallen apart into EntityTitanBonePart bones: nothing left to draw. */
    @Override
    public void doRender(EntityMutantSkeletonTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.getHealth() <= 0.0F && entity.deathTicks >= com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantSkeletonTitan.DEATH_COLLAPSE)
            return;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntityMutantSkeletonTitan entity, float partialTicks) {
        float s = entity.fetchScaleMultiplierGiant();
        GlStateManager.scale(s, s, s);
    }

    @Override
    protected float getDeathMaxRotation(EntityMutantSkeletonTitan entity) {
        return 0.0F;
    }

    public static ResourceLocation texture(int index) {
        final int v = Math.max(0, Math.min(5, index));
        return MutantTitanTextures.get("mutant_skeleton_titan_" + v, MutantTitanTextures.mutant("textures/entity/mutant_skeleton.png"), 128, 128, b -> {
            b.canvas(128, 192)
                    .box(0, 128, 2, 7, 2, 50, 0, 18, 8, 0xFFFFFFFF, 0.6F, 0.95F) // horns: vertebra bone, a bit darker
                    .box(10, 128, 3, 3, 6, 50, 0, 18, 8, 0xFFFFFFFF) // tail vertebrae
                    .box(30, 128, 2, 2, 6, 32, 12, 16, 4, 0xFFFFFFFF) // tail tip: rib bone
                    .box(48, 128, 1, 5, 1, 32, 12, 16, 4, 0xFFFFFFFF) // shoulder spikes
                    .box(54, 128, 1, 3, 1, 32, 12, 16, 4, 0xFFFFFFFF, 0.3F, 1.15F); // fangs
            if (v != 0)
                b.recolor(0, 0, 128, 192, TINT[v], BRIGHT[v]);
        });
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMutantSkeletonTitan entity) {
        return texture(entity.fetchKindSkeleton() == 1 ? 5 : entity.fetchVariantGiant());
    }
}
