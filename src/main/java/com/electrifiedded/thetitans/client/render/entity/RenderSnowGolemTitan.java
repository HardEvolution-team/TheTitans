package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSnowGolemTitan extends RenderLiving<EntitySnowGolemTitan> {
    // TODO 1.7.10 render-pass overlay (armor glow / eyes) dropped; reimplement as LayerRenderer added via addLayer() in ctor.
    private static final ResourceLocation manTexturesSnow = new ResourceLocation("textures/entity/snowman.png");
    private ModelSnowMan meshSnowman;

    public RenderSnowGolemTitan(RenderManager rm) {
        super(rm, new ModelSnowMan(), 0.5F);
        this.meshSnowman = ((ModelSnowMan)this.mainModel);
    }

    protected void applyRotations(EntitySnowGolemTitan entity, float ageInTicks, float rotationYaw, float partialTicks) {
        GL11.glRotatef(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        if (entity.deathTime > 0) {
            float f3 = ((float)entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f3 = MathHelper.sqrt(f3);
            if (f3 > 1.0F) {
                f3 = 1.0F;
            }

            GL11.glScalef(1.0F + (f3 * 1.05F), 1.0F - (f3 * 0.5F), 1.0F + (f3 * 1.05F));
        }
    }

    protected ResourceLocation getEntityTexture(EntitySnowGolemTitan entity) {
        return manTexturesSnow;
    }

    protected void preRenderCallback(EntitySnowGolemTitan entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
        GL11.glTranslatef(0.0F, 0.039F, 0.0F);
    }

    public void doRender(EntitySnowGolemTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
