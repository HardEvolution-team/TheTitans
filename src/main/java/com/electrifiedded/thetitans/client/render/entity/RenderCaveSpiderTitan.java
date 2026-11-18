package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelSpiderTitan;
import com.electrifiedded.thetitans.client.render.layer.LayerCaveSpiderTitanCharge;
import com.electrifiedded.thetitans.client.render.layer.LayerCaveSpiderTitanEyes;
import com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCaveSpiderTitan extends RenderLiving<EntityCaveSpiderTitan> {
    // 1.7.10 render-pass overlays restored as LayerRenderers (charge aura pass 1 + glowing-eyes pass 0).
    private static final ResourceLocation irisesTexturesSpider = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/spider_titan_eyes.png"));
    private static final ResourceLocation spiderTexturesCave = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/cave_spider_titan/cave_spider_titan.png"));
    private static final ResourceLocation spider2TexturesCave = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/cave_spider_titan/jesus_spider_titan.png"));
    private static final ResourceLocation spider3TexturesCave = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/cave_spider_titan/ore_spider_titan.png"));
    private static final ResourceLocation spider4TexturesCave = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/cave_spider_titan/venomous_spider_titan.png"));
    private static final ResourceLocation spider4IrisesTexturesCave = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/cave_spider_titan/venomous_spider_titan_eyes.png"));
    private static final ResourceLocation spider5TexturesCave = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/cave_spider_titan/ice_spider_titan.png"));
    private static final ResourceLocation spider5IrisesTexturesCave = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/cave_spider_titan/ice_spider_titan_eyes.png"));
    private static final ResourceLocation irisesTexturesSpider2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/void_spider_titan_eyes.png"));
    private static final ResourceLocation texturesSpider2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/void_spider_titan.png"));

    public RenderCaveSpiderTitan(RenderManager rm) {
        super(rm, new ModelSpiderTitan(), 0.7F);
        this.addLayer(new LayerCaveSpiderTitanCharge(this));
        this.addLayer(new LayerCaveSpiderTitanEyes(this));
    }

    protected float getDeathMaxRotation(EntityCaveSpiderTitan entity) {
        return 180.0F;
    }

    protected ResourceLocation getEntityTexture(EntityCaveSpiderTitan entity) {
        switch (entity.fetchVariantGiant()) {
            case 1:
                return spider2TexturesCave;
            case 2:
                return spider3TexturesCave;
            case 3:
                return spider4TexturesCave;
            case 4:
                return spider5TexturesCave;
            case 5:
                return texturesSpider2;
            default:
                return spiderTexturesCave;
        }
    }

    protected void preRenderCallback(EntityCaveSpiderTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
    }

    public void doRender(EntityCaveSpiderTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
