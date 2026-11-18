package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelSkeletonLoyalist;
import com.electrifiedded.thetitans.client.model.ModelSkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSkeletonMinion extends RenderBiped<EntitySkeletonMinion> {
    private static final ResourceLocation texturesSkeleton = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    private static final ResourceLocation skeletonTexturesWither = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public RenderSkeletonMinion(RenderManager renderManager) {
        super(renderManager, new ModelSkeletonLoyalist(), 0.5F);
        // original: skeleton robe uses the shared biped overlay; herald/glow swap between wither and normal skeleton.
        this.addLayer(new LayerMinionRobes<EntitySkeletonMinion>(this,
                new ModelSkeletonMinion(0.15F, true), new ModelSkeletonMinion(0.15F, false), new ModelSkeletonMinion(0.151F, false), new ModelSkeletonMinion(0.01F, false),
                "minions/overlay/biped_overlay.png", "minions/overlay/skeleton_overlay_shoes.png", "minions/overlay/skeleton_overlay_herald.png", "minions/glow/skeleton.png") {
            protected String heraldLabelOverlay(EntitySkeletonMinion entity) {
                return entity.fetchKindSkeleton() == 1 ? "minions/overlay/wither_skeleton_overlay_herald.png" : "minions/overlay/skeleton_overlay_herald.png";
            }
            protected String glowLabelSkin(EntitySkeletonMinion entity) {
                return entity.fetchKindSkeleton() == 1 ? "minions/glow/wither_skeleton.png" : "minions/glow/skeleton.png";
            }
        });
        // original RenderBiped armor passes; 1.12.2 RenderBiped no longer adds the armor layer itself
        this.addLayer(new net.minecraft.client.renderer.entity.layers.LayerBipedArmor(this));
    }

    protected void preRenderCallback(EntitySkeletonMinion entity, float partialTicks) {
        if (entity.fetchKindSkeleton() == 1) {
            GlStateManager.scale(1.2F, 1.2F, 1.2F);
        }
    }

    protected ResourceLocation getEntityTexture(EntitySkeletonMinion entity) {
        return entity.fetchKindSkeleton() == 1 ? skeletonTexturesWither : texturesSkeleton;
    }
}
