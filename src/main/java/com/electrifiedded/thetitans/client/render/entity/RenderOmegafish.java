package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelOmegafish;
import com.electrifiedded.thetitans.client.render.layer.LayerOmegafishCharge;
import com.electrifiedded.thetitans.client.render.layer.LayerOmegafishEyes;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderOmegafish extends RenderLiving<EntitySilverfishTitan> {
    // 1.7.10 render-pass overlays restored as LayerRenderers (charge aura pass 1 + magma/void eyes pass 0).
    private static final ResourceLocation texturesSilverfish = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/omegafish.png"));
    private static final ResourceLocation texturesSilverfish2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/omegaworm.png"));
    private static final ResourceLocation texturesSilverfish3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/frostfish.png"));
    private static final ResourceLocation texturesSilverfish4 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/seafish.png"));
    private static final ResourceLocation texturesSilverfish5 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/poisonfish.png"));
    private static final ResourceLocation texturesSilverfish6 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/magmafish.png"));
    private static final ResourceLocation texturesSilverfish7 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/omegafish/voidfish.png"));

    public RenderOmegafish(RenderManager rm) {
        super(rm, new ModelOmegafish(), 0.3F);
        this.addLayer(new LayerOmegafishCharge(this));
        this.addLayer(new LayerOmegafishEyes(this));
    }

    protected float getDeathMaxRotation(EntitySilverfishTitan entity) {
        return 180.0F;
    }

    protected ResourceLocation getEntityTexture(EntitySilverfishTitan entity) {
        switch (entity.fetchVariantGiant()) {
            case 1:
                return texturesSilverfish2;
            case 2:
                return texturesSilverfish3;
            case 3:
                return texturesSilverfish4;
            case 4:
                return texturesSilverfish5;
            case 5:
                return texturesSilverfish6;
            case 6:
                return texturesSilverfish7;
            default:
                return texturesSilverfish;
        }
    }

    protected void preRenderCallback(EntitySilverfishTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        if (entity.burrowingIs)
            GL11.glTranslatef(0.0F, 8F, 0.0F);
        GL11.glScalef(f1, f1, f1);
        GL11.glTranslatef(0.0F, 0.01F, 0.0F);
    }

    public void doRender(EntitySilverfishTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
