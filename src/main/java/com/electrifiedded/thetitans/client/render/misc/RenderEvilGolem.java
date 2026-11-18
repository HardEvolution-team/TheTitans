package com.electrifiedded.thetitans.client.render.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelEvilGolem;
import com.electrifiedded.thetitans.entity.mob.EntityEvilGolem;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEvilGolem extends RenderLiving<EntityEvilGolem> {
    public static final ResourceLocation[] GOLEM_SKIN_POOL_EVIL = new ResourceLocation[] {
        new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/big_zombie.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/wither_golem.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/gold_golem.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/big_enderman.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/spider_golem.png"))
    };
    private final ModelEvilGolem golemMeshIron;

    public RenderEvilGolem(RenderManager renderManager) {
        super(renderManager, new ModelEvilGolem(0.0F, -7.0F), 0.75F);
        this.golemMeshIron = (ModelEvilGolem)this.mainModel;
    }

    protected ResourceLocation getEntityTexture(EntityEvilGolem mob) {
        return GOLEM_SKIN_POOL_EVIL[mob.fetchKindMob()];
    }

    protected void applyRotations(EntityEvilGolem entity, float arg77043n2, float arg77043n3, float partialTicks) {
        super.applyRotations(entity, arg77043n2, arg77043n3, partialTicks);
        golemMeshIron.smallerSkullHas = entity.fetchKindMob() == 0 || entity.fetchKindMob() == 3 || entity.fetchKindMob() == 4;
        if ((double)entity.limbSwingAmount >= 0.01D) {
            float f3 = 13.0F;
            float f4 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
            GlStateManager.rotate(6.5F * f5, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.scale(1.2F, 1.2F, 1.2F);
    }
}
