package com.electrifiedded.thetitans.client.render.misc;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombie extends RenderBiped<EntityZombie> {
    private static final ResourceLocation texturesZombie = new ResourceLocation("textures/entity/zombie/zombie.png");

    public RenderZombie(RenderManager rm) {
        super(rm, new ModelZombie(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityZombie entity) {
        return texturesZombie;
    }

    public void doRender(EntityZombie entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
