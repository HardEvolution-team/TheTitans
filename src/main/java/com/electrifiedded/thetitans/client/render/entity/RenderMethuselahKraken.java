package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntityMethuselahKraken;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMethuselahKraken extends RenderLiving<EntityMethuselahKraken> {
    private static final ModelBase MESH_PLACEHOLDER = new ModelZombie();
    private final float scale = 2.0F;
    private static final ResourceLocation texture = new ResourceLocation(TheTitans.getTextures("textures/entities", "methuselahkraken.png"));

    public RenderMethuselahKraken(RenderManager rm) {
        super(rm, MESH_PLACEHOLDER, 2.0F);
    }

    protected void preRenderCallback(EntityMethuselahKraken entity, float partialTicks) {
        GL11.glScalef(this.scale, this.scale, this.scale);
    }

    protected ResourceLocation getEntityTexture(EntityMethuselahKraken entity) {
        return texture;
    }

    public void doRender(EntityMethuselahKraken entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
