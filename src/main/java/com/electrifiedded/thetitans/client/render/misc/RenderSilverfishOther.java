package com.electrifiedded.thetitans.client.render.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntitySilverfishOther;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSilverfishOther extends RenderLiving<EntitySilverfishOther> {
    public static final ResourceLocation[] SKIN_POOL_SILVERFISH = new ResourceLocation[] {
        new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/poisonfish.png")), new ResourceLocation("textures/entity/silverfish.png"), new ResourceLocation("textures/entity/silverfish.png"), new ResourceLocation("textures/entity/silverfish.png"), new ResourceLocation("textures/entity/silverfish.png"), new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/ender_silverfish.png")), new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/endermite.png")), new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/dustworm.png"))
    };

    public RenderSilverfishOther(RenderManager renderManager) {
        super(renderManager, new ModelSilverfish(), 0.3F);
    }

    protected float getDeathMaxRotation(EntitySilverfishOther arg77037n1) {
        return 180.0F;
    }

    protected ResourceLocation getEntityTexture(EntitySilverfishOther mob) {
        return SKIN_POOL_SILVERFISH[mob.fetchKindMob()];
    }
}
