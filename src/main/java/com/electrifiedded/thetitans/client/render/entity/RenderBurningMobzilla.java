package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntityBurningMobzilla;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBurningMobzilla extends RenderLiving<EntityBurningMobzilla> {
    private static final ModelBase MESH_PLACEHOLDER = new ModelZombie();
    private final float scale = 2.05F;
    private static final ResourceLocation mobzillaMeltdownSkinBurning = new ResourceLocation(TheTitans.getTextures("textures/entities", "Godzillaburningtexturemelt.png"));
    private static final ResourceLocation mobzillaSkinBurning = new ResourceLocation(TheTitans.getTextures("textures/entities", "Godzillaburningtexture.png"));

    public RenderBurningMobzilla(RenderManager rm) {
        super(rm, MESH_PLACEHOLDER, 4.0F);
    }

    protected void preRenderCallback(EntityBurningMobzilla entity, float partialTicks) {
        GL11.glScalef(this.scale, this.scale, this.scale);
    }

    protected ResourceLocation getEntityTexture(EntityBurningMobzilla entity) {
        return entity.getHealth() > 1000.0F ? mobzillaSkinBurning : mobzillaMeltdownSkinBurning;
    }

    public void doRender(EntityBurningMobzilla entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
