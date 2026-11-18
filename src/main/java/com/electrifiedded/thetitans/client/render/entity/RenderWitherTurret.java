package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.client.model.ModelWitherTurret;
import com.electrifiedded.thetitans.client.render.layer.LayerWitherTurretEnchant;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurret;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWitherTurret extends RenderLiving<EntityWitherTurret> {
    private static final ResourceLocation texturesWither = new ResourceLocation("textures/entity/wither/wither.png");

    public RenderWitherTurret(RenderManager rm) {
        super(rm, new ModelWitherTurret(0.0F), 1.0F);
        this.addLayer(new LayerWitherTurretEnchant(this));
    }

    protected void preRenderCallback(EntityWitherTurret entity, float partialTicks) {
        GL11.glScalef(2.0F, 2.0F, 2.0F);
    }

    protected ResourceLocation getEntityTexture(EntityWitherTurret entity) {
        return texturesWither;
    }
}
