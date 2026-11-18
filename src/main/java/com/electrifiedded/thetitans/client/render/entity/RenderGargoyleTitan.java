package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelGargoyleTitan;
import com.electrifiedded.thetitans.client.render.layer.LayerGargoyleTitanBeam;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGargoyleTitan extends RenderLiving<EntityGargoyleTitan> {
    // TODO 1.7.10 render-pass overlay/eyes/equipped-items dropped; reimplement as LayerRenderer via addLayer() in ctor.
    private static final ResourceLocation golemTexturesIron = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/golem_titans/gargoyle_titan.png"));

    public RenderGargoyleTitan(RenderManager rm) {
        super(rm, new ModelGargoyleTitan(), 1.0F);
        this.addLayer(new LayerGargoyleTitanBeam(this));
    }

    protected ResourceLocation getEntityTexture(EntityGargoyleTitan entity) {
        return golemTexturesIron;
    }

    protected void applyRotations(EntityGargoyleTitan entity, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
        if (entity.limbSwingAmount >= 0.01D) {
            float f3 = 13.0F;
            float f4 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f5 = (Math.abs((f4/2) % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
            GL11.glRotatef(3.5F * f5, 0.0F, 0.0F, 1.0F);
        }
    }

    protected void preRenderCallback(EntityGargoyleTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
        GL11.glTranslatef(0.0F, 0.0275F, 0.0F);
    }

    public void doRender(EntityGargoyleTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
