package com.electrifiedded.thetitans.client.render.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntityUndeadOther;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUndeadOther extends RenderBiped<EntityUndeadOther> {
    public static final ResourceLocation[] SKIN_POOL_UNDEAD = new ResourceLocation[] {
        new ResourceLocation("textures/entity/zombie/zombie.png"),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/golden_guard.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie1.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie2.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie3.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie4.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie5.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie6.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie7.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie8.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie9.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/zombie10.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/ghoul_zombie.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/wight_zombie.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/ender_zombie.png"))
    };

    public RenderUndeadOther(RenderManager renderManager) {
        super(renderManager, new ModelZombie(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityUndeadOther mob) {
        return SKIN_POOL_UNDEAD[mob.fetchKindMob()];
    }
}
