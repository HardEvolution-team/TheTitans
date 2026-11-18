package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelExecutorDragon;
import com.electrifiedded.thetitans.client.render.layer.LayerExecutorDragonEyes;
import com.electrifiedded.thetitans.entity.titan.EntityExecutorDragon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderExecutorDragon extends RenderLiving<EntityExecutorDragon> {
    private static final ResourceLocation dragonTexturesExecutor = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/god_titans/executor_dragon.png"));

    public RenderExecutorDragon(RenderManager rm) {
        super(rm, new ModelExecutorDragon(), 1.0F);
        this.addLayer(new LayerExecutorDragonEyes(this));
    }

    protected ResourceLocation getEntityTexture(EntityExecutorDragon entity) {
        int i = entity.getInvulTime();
        return ((i > 0) && ((i > 300) || (i / 10 % 2 != 1))) ? TheTitans.giantWhiteTexture64x64Generic : dragonTexturesExecutor;
    }

    protected void preRenderCallback(EntityExecutorDragon entity, float partialTicks) {
        // the model stands ~52px tall (feet at y~34, back spikes at y~-18) while the hitbox is 1.6*size
        // blocks tall, so 0.5*size makes model and hitbox the same height. The lift puts the feet on the ground.
        float f1 = entity.fetchScaleMultiplierGiant() * 0.5F;
        GlStateManager.scale(f1, f1, f1);
        GlStateManager.translate(0.0F, -0.625F, 0.0F);
    }
}
