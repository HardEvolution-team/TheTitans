package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelPigZombieTitan;
import com.electrifiedded.thetitans.client.render.layer.LayerZombiePigmanTitanCharge;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderZombiePigmanTitan extends RenderLiving<EntityPigZombieTitan> {
    // TODO 1.7.10 render-pass overlay (armor glow / eyes) dropped; reimplement as LayerRenderer added via addLayer() in ctor.
    private static final ResourceLocation pigmanGiantTexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/pigzombie_titan/zombie_pigman_titan.png"));
    private static final ResourceLocation pigman2GiantTexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/pigzombie_titan/king_pigman_titan.png"));
    private static final ResourceLocation pigman3GiantTexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/pigzombie_titan/wither_zombie_pigman_titan.png"));
    private static final ResourceLocation pigman4GiantTexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/pigzombie_titan/count_pigvampire_titan.png"));
    private static final ResourceLocation pigman5GiantTexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/pigzombie_titan/void_pigman_titan.png"));

    public RenderZombiePigmanTitan(RenderManager rm) {
        super(rm, new ModelPigZombieTitan(), 0.5F);
        this.addLayer(new LayerZombiePigmanTitanCharge(this));
    }

    protected ResourceLocation getEntityTexture(EntityPigZombieTitan entity) {
        switch (entity.fetchVariantGiant()) {
            case 1:
                return pigman2GiantTexturesZombie;
            case 2:
                return pigman3GiantTexturesZombie;
            case 3:
                return pigman4GiantTexturesZombie;
            case 4:
                return pigman5GiantTexturesZombie;
            default:
                return pigmanGiantTexturesZombie;
        }
    }

    protected void preRenderCallback(EntityPigZombieTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
        GL11.glTranslatef(0.0F, 0.01F, 0.0F);
    }

    public void doRender(EntityPigZombieTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
