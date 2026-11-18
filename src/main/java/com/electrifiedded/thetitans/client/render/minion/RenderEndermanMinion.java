package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelEndermanMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;

import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderEndermanMinion extends RenderLiving<EntityEndermanMinion> {
    private static final ResourceLocation texturesEnderman = new ResourceLocation("textures/entity/enderman/enderman.png");
    private ModelEnderman meshEnderman;
    private Random rnd = new Random();

    public RenderEndermanMinion(RenderManager renderManager) {
        super(renderManager, new ModelEnderman(0.0F), 0.5F);
        this.meshEnderman = (ModelEnderman)this.mainModel;
        this.addLayer(new LayerMinionRobes<EntityEndermanMinion>(this,
                new ModelEndermanMinion(0.15F), null, new ModelEndermanMinion(0.151F), new ModelEndermanMinion(0.01F),
                "minions/overlay/enderman_overlay.png", null, "minions/overlay/enderman_overlay_herald.png", "minions/glow/enderman.png"));
        this.addLayer(new LayerEndermanMinionHeldBlock(this));
    }

    public void doRender(EntityEndermanMinion entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.meshEnderman.isCarrying = entity.getHeldBlockState() != null;
        this.meshEnderman.isAttacking = entity.isScreaming();
        double d3 = 0.01D;
        if (entity.isScreaming()) {
            x += this.rnd.nextGaussian() * d3;
            z += this.rnd.nextGaussian() * d3;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityEndermanMinion entity) {
        return texturesEnderman;
    }
}
