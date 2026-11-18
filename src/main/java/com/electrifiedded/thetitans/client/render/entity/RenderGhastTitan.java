package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGhastTitan extends RenderLiving<EntityGhastTitan> {
    private static final ResourceLocation texturesGhast = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ghast_titan/ghast_titan.png"));
    private static final ResourceLocation shootingTexturesGhast = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ghast_titan/ghast_titan_shooting.png"));
    private static final ResourceLocation texturesGhast2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ghast_titan/lightning_ghast_titan.png"));
    private static final ResourceLocation shootingTexturesGhast2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ghast_titan/lightning_ghast_titan_shooting.png"));
    private static final ResourceLocation texturesGhast3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ghast_titan/obsidian_ghast_titan.png"));
    private static final ResourceLocation shootingTexturesGhast3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ghast_titan/obsidian_ghast_titan_shooting.png"));

    public RenderGhastTitan(RenderManager rm) {
        super(rm, new ModelGhastTitan(), 4.0F);
    }

    protected ResourceLocation getEntityTexture(EntityGhastTitan entity) {
        switch (entity.fetchVariantGiant()) {
            case 1:
                return entity.isAttacking() ? shootingTexturesGhast2 : texturesGhast2;
            case 2:
                return entity.isAttacking() ? shootingTexturesGhast3 : texturesGhast3;
            default:
                return entity.isAttacking() ? shootingTexturesGhast : texturesGhast;
        }
    }

    protected void preRenderCallback(EntityGhastTitan entity, float partialTicks) {
        float f1 = (entity.strikeCounterPrev + (entity.counterStrike - entity.strikeCounterPrev) * partialTicks) / 20.0F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        f1 = 1.0F / (f1 * f1 * f1 * f1 * f1 * 2.0F + 1.0F);
        float f2 = (8.0F + f1) / 2.0F;
        float f3 = (8.0F + 1.0F / f1) / 2.0F;
        GL11.glScalef(f3, f2, f3);
        float f11 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f11, f11, f11);
        if (entity.hurtTime > 0) {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
    }

    public void doRender(EntityGhastTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
