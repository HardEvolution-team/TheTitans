package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelSpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySpiderMinion;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpiderMinion extends RenderLiving<EntitySpiderMinion> {
    private static final ResourceLocation texturesSpider = new ResourceLocation("textures/entity/spider/spider.png");

    public RenderSpiderMinion(RenderManager renderManager) {
        super(renderManager, new ModelSpider(), 1.0F);
        this.addLayer(new LayerMinionRobes<EntitySpiderMinion>(this,
                new ModelSpiderMinion(0.15F), null, new ModelSpiderMinion(0.151F), new ModelSpiderMinion(0.01F),
                "minions/overlay/spider_overlay.png", null, "minions/overlay/spider_overlay_herald.png", "minions/glow/spider.png"));
    }

    protected float getDeathMaxRotation(EntitySpiderMinion entity) {
        return 180.0F;
    }

    protected ResourceLocation getEntityTexture(EntitySpiderMinion entity) {
        return texturesSpider;
    }
}
