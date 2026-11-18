package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntityOverlordScorpion;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderOverlordScorpion extends RenderLiving<EntityOverlordScorpion> {
    private static final ModelBase MESH_PLACEHOLDER = new ModelZombie();
    private static final ResourceLocation texture = new ResourceLocation(TheTitans.getTextures("textures/entities", "overlordscorpion.png"));

    public RenderOverlordScorpion(RenderManager rm) {
        super(rm, MESH_PLACEHOLDER, 1.75F);
    }

    protected void preRenderCallback(EntityOverlordScorpion entity, float partialTicks) {
        GL11.glScalef(3.0F, 3.0F, 3.0F);
    }

    protected ResourceLocation getEntityTexture(EntityOverlordScorpion entity) {
        return texture;
    }

    public void doRender(EntityOverlordScorpion entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
