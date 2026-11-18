package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelSpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCaveSpiderMinion;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCaveSpiderMinion extends RenderLiving<EntityCaveSpiderMinion> {
    private static final ResourceLocation spiderTexturesCave = new ResourceLocation("textures/entity/spider/cave_spider.png");

    public RenderCaveSpiderMinion(RenderManager renderManager) {
        super(renderManager, new ModelSpider(), 0.7F);
        this.addLayer(new LayerMinionRobes<EntityCaveSpiderMinion>(this,
                new ModelSpiderMinion(0.15F), null, new ModelSpiderMinion(0.151F), new ModelSpiderMinion(0.01F),
                "minions/overlay/spider_overlay.png", null, "minions/overlay/cave_spider_overlay_herald.png", "minions/glow/spider.png"));
    }

    protected void preRenderCallback(EntityCaveSpiderMinion entity, float partialTicks) {
        GlStateManager.scale(0.7F, 0.7F, 0.7F);
    }

    protected float getDeathMaxRotation(EntityCaveSpiderMinion entity) {
        return 180.0F;
    }

    protected ResourceLocation getEntityTexture(EntityCaveSpiderMinion entity) {
        return spiderTexturesCave;
    }
}
