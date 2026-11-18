package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.client.render.layer.LayerSlimeTitanInner;
import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSlimeTitan extends RenderLiving<EntitySlimeTitan> {
    private static final ResourceLocation texturesSlime = new ResourceLocation("textures/entity/slime/slime.png");

    public RenderSlimeTitan(RenderManager rm, ModelBase p_i1267_1_, ModelBase p_i1267_2_, float p_i1267_3_) {
        super(rm, p_i1267_1_, p_i1267_3_);
        this.addLayer(new LayerSlimeTitanInner(this, p_i1267_2_));
    }

    public void doRender(EntitySlimeTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.shadowSize = (0.25F * entity.getSlimeSize());
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void applyRotations(EntitySlimeTitan entity, float ageInTicks, float rotationYaw, float partialTicks) {
        GL11.glRotatef(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        if (entity.deathTime > 0) {
            float f3 = ((float)entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f3 = MathHelper.sqrt(f3);
            if (f3 > 1.0F) {
                f3 = 1.0F;
            }

            GL11.glScalef(1.0F + (f3 * 2.0F), 1.0F - (f3 * 0.99F), 1.0F + (f3 * 2.0F));
        }
    }

    protected void preRenderCallback(EntitySlimeTitan entity, float partialTicks) {
        float f1 = entity.getSlimeSize();
        float f2 = (entity.prevSquishFactor + (entity.squishFactor - entity.prevSquishFactor) * partialTicks) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        GL11.glScalef(f3 * f1, 1.0F / f3 * f1, f3 * f1);
        float fl = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(fl, fl, fl);
        float f4 = 0.025F + ((entity.getHealth() - entity.getMaxHealth()) / entity.getMaxHealth() * 0.5F);
        float f5 = 1F + (MathHelper.cos((entity.ticksExisted + entity.getEntityId() + partialTicks) * f4) * f4);
        float f6 = 1F + (MathHelper.sin((entity.ticksExisted + entity.getEntityId() + partialTicks) * f4 + 0.785F) * f4);
        if (entity.deathTime <= 0)
            GL11.glScalef(f5, f6, f5);
        GL11.glTranslatef(0F, 0.0075F, 0F);
    }

    protected ResourceLocation getEntityTexture(EntitySlimeTitan entity) {
        return texturesSlime;
    }
}
