package com.electrifiedded.thetitans.client.render.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.misc.EntityEnderSquid;

import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEnderSquid extends RenderLiving<EntityEnderSquid> {
    private static final ResourceLocation texture = new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/end_squid.png"));

    public RenderEnderSquid(RenderManager rm) {
        super(rm, new ModelSquid(), 0.7F);
    }

    protected ResourceLocation getEntityTexture(EntityEnderSquid entity) {
        return texture;
    }

    protected void applyRotations(EntityEnderSquid entity, float ageInTicks, float rotationYaw, float partialTicks) {
        float f3 = entity.prevSquidPitch + (entity.squidPitch - entity.prevSquidPitch) * partialTicks;
        float f4 = entity.prevSquidYaw + (entity.squidYaw - entity.prevSquidYaw) * partialTicks;
        GlStateManager.translate(0.0F, 0.5F, 0.0F);
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f4, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -1.2F, 0.0F);
    }

    protected float handleRotationFloat(EntityEnderSquid entity, float partialTicks) {
        return entity.prevSquidRotation + (entity.squidRotation - entity.prevSquidRotation) * partialTicks;
    }

    public void doRender(EntityEnderSquid entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
