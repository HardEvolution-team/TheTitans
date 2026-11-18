package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelSkeletonTitan;
import com.electrifiedded.thetitans.client.render.layer.LayerSkeletonTitanCharge;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSkeletonTitan extends RenderLiving<EntitySkeletonTitan> {
    // TODO 1.7.10 render-pass overlay (armor glow / eyes) dropped; reimplement as LayerRenderer added via addLayer() in ctor.
    private static final ResourceLocation giantTexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/skeleton_titan.png"));
    private static final ResourceLocation giantPull1TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/skeleton_titan_pulling_0.png"));
    private static final ResourceLocation giantPull2TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/skeleton_titan_pulling_1.png"));
    private static final ResourceLocation giantPull3TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/skeleton_titan_pulling_2.png"));
    private static final ResourceLocation titan2TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/sand_skeleton_titan.png"));
    private static final ResourceLocation titan2Pull1TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/sand_skeleton_titan_pulling_0.png"));
    private static final ResourceLocation titan2Pull2TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/sand_skeleton_titan_pulling_1.png"));
    private static final ResourceLocation titan2Pull3TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/sand_skeleton_titan_pulling_2.png"));
    private static final ResourceLocation titan3TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/guitar_skeleton_titan.png"));
    private static final ResourceLocation titan4TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/sleepy_skeleton_titan.png"));
    private static final ResourceLocation titan4Pull1TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/sleepy_skeleton_titan_pulling_0.png"));
    private static final ResourceLocation titan4Pull2TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/sleepy_skeleton_titan_pulling_1.png"));
    private static final ResourceLocation titan4Pull3TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/sleepy_skeleton_titan_pulling_2.png"));
    private static final ResourceLocation titan5TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/void_skeleton_titan.png"));
    private static final ResourceLocation titan5Pull1TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/void_skeleton_titan_pulling_0.png"));
    private static final ResourceLocation titan5Pull2TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/void_skeleton_titan_pulling_1.png"));
    private static final ResourceLocation titan5Pull3TexturesSkeleton = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/void_skeleton_titan_pulling_2.png"));
    private static final ResourceLocation skeletonTexturesWither = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/wither_skeleton_titan/wither_skeleton_titan.png"));
    private static final ResourceLocation skeleton2TexturesWither = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/wither_skeleton_titan/solar_skeleton_titan.png"));
    private static final ResourceLocation skeleton2OverlayTexturesWither = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/wither_skeleton_titan/solar_skeleton_titan_eyes.png"));
    private static final ResourceLocation skeleton3TexturesWither = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/wither_skeleton_titan/reaper_skeleton_titan.png"));
    private ModelSkeletonTitan meshSkeleton;

    public RenderSkeletonTitan(RenderManager rm) {
        super(rm, new ModelSkeletonTitan(), 0.5F);
        this.meshSkeleton = ((ModelSkeletonTitan)this.mainModel);
        this.addLayer(new LayerSkeletonTitanCharge(this));
    }

    public void doRender(EntitySkeletonTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.fetchKindSkeleton() == 1) {
            this.meshSkeleton.witherIs = true;
        }

        else {
            this.meshSkeleton.witherIs = false;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void preRenderCallback(EntitySkeletonTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
        GL11.glTranslatef(0F, 0.0075F, 0F);
    }

    protected ResourceLocation getEntityTexture(EntitySkeletonTitan entity) {
        if (entity.fetchKindSkeleton() == 1) {
            switch (entity.fetchVariantGiant()) {
                case 1:
                    return skeleton2TexturesWither;
                case 2:
                    return skeleton3TexturesWither;
                default:
                    return skeletonTexturesWither;
            }
        }

        else {
            switch (entity.fetchVariantGiant()) {
                case 1:
                    return (entity.attackTimer >= 30 ? titan2Pull3TexturesSkeleton : (entity.attackTimer < 30 && entity.attackTimer >= 20 ? titan2Pull2TexturesSkeleton : (entity.attackTimer < 20 && entity.attackTimer >= 10 ? titan2Pull1TexturesSkeleton : titan2TexturesSkeleton)));
                case 2:
                    return titan3TexturesSkeleton;
                case 3:
                    return (entity.attackTimer >= 30 ? titan4Pull3TexturesSkeleton : (entity.attackTimer < 30 && entity.attackTimer >= 20 ? titan4Pull2TexturesSkeleton : (entity.attackTimer < 20 && entity.attackTimer >= 10 ? titan4Pull1TexturesSkeleton : titan4TexturesSkeleton)));
                case 4:
                    return (entity.attackTimer >= 30 ? titan5Pull3TexturesSkeleton : (entity.attackTimer < 30 && entity.attackTimer >= 20 ? titan5Pull2TexturesSkeleton : (entity.attackTimer < 20 && entity.attackTimer >= 10 ? titan5Pull1TexturesSkeleton : titan5TexturesSkeleton)));
                default:
                    return (entity.attackTimer >= 30 ? giantPull3TexturesSkeleton : (entity.attackTimer < 30 && entity.attackTimer >= 20 ? giantPull2TexturesSkeleton : (entity.attackTimer < 20 && entity.attackTimer >= 10 ? giantPull1TexturesSkeleton : giantTexturesSkeleton)));
            }
        }
    }
}
