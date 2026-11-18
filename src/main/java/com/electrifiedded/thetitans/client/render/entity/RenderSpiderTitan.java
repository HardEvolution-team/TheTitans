package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelSpiderTitan;
import com.electrifiedded.thetitans.client.render.layer.LayerSpiderTitanCharge;
import com.electrifiedded.thetitans.client.render.layer.LayerSpiderTitanEyes;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSpiderTitan extends RenderLiving<EntitySpiderTitan> {
    // TODO 1.7.10 render-pass overlay (armor glow / eyes) dropped; reimplement as LayerRenderer added via addLayer() in ctor.
    private static final ResourceLocation irisesTexturesSpider = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/spider_titan_eyes.png"));
    private static final ResourceLocation texturesSpider = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/spider_titan.png"));
    private static final ResourceLocation irisesTexturesSpider2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/test_spider_titan_eyes.png"));
    private static final ResourceLocation texturesSpider2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/test_spider_titan.png"));
    private static final ResourceLocation irisesTexturesSpider3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/mycelium_spider_titan_eyes.png"));
    private static final ResourceLocation texturesSpider3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/mycelium_spider_titan.png"));
    private static final ResourceLocation irisesTexturesSpider4 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/lunar_spider_titan_eyes.png"));
    private static final ResourceLocation texturesSpider4 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/lunar_spider_titan.png"));
    private static final ResourceLocation irisesTexturesSpider5 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/void_spider_titan_eyes.png"));
    private static final ResourceLocation texturesSpider5 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/spider_titan/void_spider_titan.png"));

    public RenderSpiderTitan(RenderManager rm) {
        super(rm, new ModelSpiderTitan(), 1.0F);
        this.addLayer(new LayerSpiderTitanEyes(this));
        this.addLayer(new LayerSpiderTitanCharge(this));
    }

    protected float getDeathMaxRotation(EntitySpiderTitan entity) {
        return 180.0F;
    }

    protected ResourceLocation getEntityTexture(EntitySpiderTitan entity) {
        switch (entity.fetchVariantGiant()) {
            case 1:
                return texturesSpider2;
            case 2:
                return texturesSpider3;
            case 3:
                return texturesSpider4;
            case 4:
                return texturesSpider5;
            default:
                return texturesSpider;
        }
    }

    protected void preRenderCallback(EntitySpiderTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
    }

    public void doRender(EntitySpiderTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
