package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPigZombieMinion extends RenderBiped<EntityPigZombieMinion> {
    private static final ResourceLocation pigmanTexturesZombie = new ResourceLocation("textures/entity/zombie_pigman.png");

    public RenderPigZombieMinion(RenderManager renderManager) {
        super(renderManager, new ModelZombie(), 0.5F);
        this.addLayer(new LayerMinionRobes<EntityPigZombieMinion>(this,
                new ModelZombieMinion(0.15F, true), new ModelZombieMinion(0.15F, true), new ModelZombieMinion(0.151F, true), new ModelZombieMinion(0.01F, false),
                "minions/overlay/biped_overlay.png", "minions/overlay/biped_overlay_shoes.png", "minions/overlay/zombie_pigman_overlay_herald.png", "minions/glow/pigzombie.png"));
        // original RenderBiped armor passes; 1.12.2 RenderBiped no longer adds the armor layer itself
        this.addLayer(new net.minecraft.client.renderer.entity.layers.LayerBipedArmor(this));
    }

    protected ResourceLocation getEntityTexture(EntityPigZombieMinion entity) {
        return pigmanTexturesZombie;
    }
}
