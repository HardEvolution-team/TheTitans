package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelGhastGuard;
import com.electrifiedded.thetitans.client.model.ModelGhastMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGhastMinion;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGhastMinion extends RenderLiving<EntityGhastMinion> {
    private static final ResourceLocation texturesGhast = new ResourceLocation("textures/entity/ghast/ghast.png");
    private static final ResourceLocation shootingTexturesGhast = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    public RenderGhastMinion(RenderManager renderManager) {
        super(renderManager, new ModelGhastGuard(), 3.0F);
        // original: ghast glow only shows while shooting.
        this.addLayer(new LayerMinionRobes<EntityGhastMinion>(this,
                new ModelGhastMinion(0.15F), null, new ModelGhastMinion(0.151F), new ModelGhastMinion(0.01F),
                "minions/overlay/ghast_overlay.png", null, "minions/overlay/ghast_overlay_herald.png", "minions/glow/ghast.png") {
            protected boolean needsGlow(EntityGhastMinion entity) {
                return entity.funcBF110182();
            }
        });
    }

    protected ResourceLocation getEntityTexture(EntityGhastMinion entity) {
        return entity.funcBF110182() ? shootingTexturesGhast : texturesGhast;
    }

    protected void preRenderCallback(EntityGhastMinion entity, float partialTicks) {
        float f1 = (entity.strikeCounterPrev + (entity.counterStrike - entity.strikeCounterPrev) * partialTicks) / 20.0F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        f1 = 1.0F / (f1 * f1 * f1 * f1 * f1 * 2.0F + 1.0F);
        float f2 = (8.0F + f1) / 2.0F;
        float f3 = (8.0F + 1.0F / f1) / 2.0F;
        GlStateManager.scale(f3, f2, f3);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(0.0F, 0.5F, 0.0F);
    }
}
