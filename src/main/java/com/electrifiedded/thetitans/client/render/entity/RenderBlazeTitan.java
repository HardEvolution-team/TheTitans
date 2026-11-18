package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelBlazeTitan;
import com.electrifiedded.thetitans.client.render.layer.LayerBlazeTitanCharge;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBlazeTitan extends RenderLiving<EntityBlazeTitan> {
    private static final ResourceLocation texturesBlaze = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/blaze_titan/blaze_titan.png"));
    private static final ResourceLocation texturesBlaze2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/blaze_titan/frost_titan.png"));
    private static final ResourceLocation texturesBlaze3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/blaze_titan/acid_titan.png"));
    private static final ResourceLocation texturesBlaze4 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/blaze_titan/dirt_titan.png"));
    private static final ResourceLocation texturesBlaze5 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/blaze_titan/void_titan.png"));

    public RenderBlazeTitan(RenderManager rm) {
        super(rm, new ModelBlazeTitan(), 0.5F);
        this.addLayer(new LayerBlazeTitanCharge(this));
    }

    protected ResourceLocation getEntityTexture(EntityBlazeTitan entity) {
        switch (entity.fetchVariantGiant()) {
            case 1:
                return texturesBlaze2;
            case 2:
                return texturesBlaze3;
            case 3:
                return texturesBlaze4;
            case 4:
                return texturesBlaze5;
            default:
                return texturesBlaze;
        }
    }

    protected void preRenderCallback(EntityBlazeTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
        GL11.glTranslatef(0.0F, 1.26F, 0.0F);
    }

    public void doRender(EntityBlazeTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
