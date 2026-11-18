package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.client.model.ModelWitherMortar;
import com.electrifiedded.thetitans.client.render.layer.LayerWitherTurretMortarEnchant;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretMortar;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWitherTurretMortar extends RenderLiving<EntityWitherTurretMortar> {
    private static final ResourceLocation texturesWither = new ResourceLocation("textures/entity/wither/wither.png");

    public RenderWitherTurretMortar(RenderManager rm) {
        super(rm, new ModelWitherMortar(), 1.25F);
        this.addLayer(new LayerWitherTurretMortarEnchant(this));
    }

    protected void preRenderCallback(EntityWitherTurretMortar entity, float partialTicks) {
        GL11.glScalef(2.5F, 2.5F, 2.5F);
    }

    protected ResourceLocation getEntityTexture(EntityWitherTurretMortar entity) {
        return texturesWither;
    }
}
