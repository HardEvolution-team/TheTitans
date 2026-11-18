package com.electrifiedded.thetitans.client.render.misc;

import com.electrifiedded.thetitans.entity.titan.classic.EntityOgPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.classic.EntityOgZombieTitan;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderOGZombieTitan extends RenderBiped<EntityOgZombieTitan> {
    private static final ResourceLocation pigmanTexturesZombie = new ResourceLocation("textures/entity/zombie_pigman.png");
    private static final ResourceLocation texturesZombie = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation villagerTexturesZombie = new ResourceLocation("textures/entity/zombie/zombie_villager.png");

    public RenderOGZombieTitan(RenderManager rm) {
        super(rm, new ModelZombie(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityOgZombieTitan entity) {
        return entity instanceof EntityOgPigZombieTitan ? pigmanTexturesZombie : (entity.villagerIs() ? villagerTexturesZombie : texturesZombie);
    }

    protected void applyRotations(EntityOgZombieTitan entity, float p_rotate_2_, float p_rotate_3_, float partialTicks) {
        super.applyRotations(entity, p_rotate_2_, p_rotate_3_, partialTicks);
        if ((double) entity.limbSwingAmount >= 0.01D) {
            float f3 = 16.0F;
            float f4 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
            GL11.glRotatef(6.5F * f5, 0.0F, 0.0F, 1.0F);
        }

        GL11.glScalef(16.0F, 16.0F, 16.0F);
    }

    public void doRender(EntityOgZombieTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
