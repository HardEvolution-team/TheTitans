package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.client.model.ModelWitherMachineGun;
import com.electrifiedded.thetitans.client.render.layer.LayerWitherTurretGroundEnchant;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretGround;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWitherTurretGround extends RenderLiving<EntityWitherTurretGround> {
    private static final ResourceLocation texturesWither = new ResourceLocation("textures/entity/wither/wither.png");

    public RenderWitherTurretGround(RenderManager rm) {
        super(rm, new ModelWitherMachineGun(), 1.0F);
        this.addLayer(new LayerWitherTurretGroundEnchant(this));
    }

    protected void preRenderCallback(EntityWitherTurretGround entity, float partialTicks) {
        GL11.glScalef(2.0F, 2.0F, 2.0F);
    }

    protected ResourceLocation getEntityTexture(EntityWitherTurretGround entity) {
        return texturesWither;
    }
}
