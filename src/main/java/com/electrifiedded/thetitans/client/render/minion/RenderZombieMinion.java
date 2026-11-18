package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieMinion extends RenderBiped<EntityZombieMinion> {
    private static final ResourceLocation texturesZombie = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation villagerTexturesZombie = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
    private final ModelBiped meshZombie;
    private final ModelZombieVillager villagerMeshZombie;

    public RenderZombieMinion(RenderManager renderManager) {
        super(renderManager, new ModelZombie(), 0.5F);
        this.meshZombie = (ModelBiped)this.mainModel;
        this.villagerMeshZombie = new ModelZombieVillager();
        this.addLayer(new LayerMinionRobes<EntityZombieMinion>(this,
                new ModelZombieMinion(0.15F, true), new ModelZombieMinion(0.15F, true), new ModelZombieMinion(0.151F, true), new ModelZombieMinion(0.01F, true),
                "minions/overlay/biped_overlay.png", "minions/overlay/biped_overlay_shoes.png", "minions/overlay/zombie_overlay_herald.png", "minions/glow/zombie.png"));
        // original RenderBiped armor passes; 1.12.2 RenderBiped no longer adds the armor layer itself
        this.addLayer(new net.minecraft.client.renderer.entity.layers.LayerBipedArmor(this));
    }

    public void doRender(EntityZombieMinion entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.mainModel = entity.villagerIs() ? this.villagerMeshZombie : this.meshZombie;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityZombieMinion entity) {
        return entity.villagerIs() ? villagerTexturesZombie : texturesZombie;
    }

    protected void applyRotations(EntityZombieMinion entity, float arg77043n2, float arg77043n3, float partialTicks) {
        super.applyRotations(entity, arg77043n2, arg77043n3, partialTicks);
    }

    protected void preRenderCallback(EntityZombieMinion entity, float partialTicks) {
        if (entity.fetchKindMinion() == EnumMinionType.LORD) {
            GlStateManager.scale(1.05F, 1.05F, 1.05F);
        }
    }
}
