package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelZombieTitan;
import com.electrifiedded.thetitans.client.render.layer.LayerZombieTitanArmor;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderZombieTitan extends RenderLiving<EntityZombieTitan> {
    private static final ResourceLocation giantTexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/zombie_titan.png"));
    private static final ResourceLocation titan2TexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/jungle_zombie_titan.png"));
    private static final ResourceLocation titan3TexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/blood_zombie_titan.png"));
    private static final ResourceLocation titan4TexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/radiation_zombie_titan.png"));
    private static final ResourceLocation titan5TexturesZombie = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/zombie_titan/void_zombie_titan.png"));

    public RenderZombieTitan(RenderManager rm) {
        super(rm, new ModelZombieTitan(), 0.5F);
        this.addLayer(new LayerZombieTitanArmor(this));
    }

    protected void preRenderCallback(EntityZombieTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
        GL11.glTranslatef(0.0F, 0.01F, 0.0F);
    }

    protected ResourceLocation getEntityTexture(EntityZombieTitan entity) {
        switch (entity.fetchVariantGiant()) {
            case 1:
                return titan2TexturesZombie;
            case 2:
                return titan3TexturesZombie;
            case 3:
                return titan4TexturesZombie;
            case 4:
                return titan5TexturesZombie;
            default:
                return giantTexturesZombie;
        }
    }

    public void doRender(EntityZombieTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
