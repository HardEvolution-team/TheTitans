package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelCreeperTitan;
import com.electrifiedded.thetitans.client.render.layer.LayerCreeperTitanCharge;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCreeperTitan extends RenderLiving<EntityCreeperTitan> {
    private static final ResourceLocation texturesCreeper = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/creeper_titan.png"));
    private static final ResourceLocation texturesCreeper2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/hell_creeper_titan.png"));
    private static final ResourceLocation texturesCreeper3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/ghost_creeper_titan.png"));
    private static final ResourceLocation texturesCreeper4 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/ender_creeper_titan.png"));
    private static final ResourceLocation texturesCreeper5 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/void_creeper_titan.png"));

    public RenderCreeperTitan(RenderManager rm) {
        super(rm, new ModelCreeperTitan(), 0.75F);
        this.addLayer(new LayerCreeperTitanCharge(this));
    }

    protected void preRenderCallback(EntityCreeperTitan entity, float partialTicks) {
        float f1 = entity.getCreeperFlashIntensity(partialTicks);
        float f2 = 1.0F + MathHelper.sin(f1 * 100.0F) * f1 * 0.01F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        f1 *= f1;
        f1 *= f1;
        float f3 = (1.0F + f1 * 0.5F) * f2;
        float f4 = (1.0F + f1 * 0.15F) / f2;
        GL11.glScalef(f3, f4, f3);
        float fl = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(fl, fl, fl);
    }

    protected int getColorMultiplier(EntityCreeperTitan entity, float p2, float p3) {
        float f2 = entity.getCreeperFlashIntensity(p3);
        if ((int)(f2 * 10.0F) % 2 == 0) {
            return 0;
        }

        int i = (int)(f2 * 0.2F * 255.0F);
        if (i < 0) {
            i = 0;
        }

        if (i > 255) {
            i = 255;
        }

        short short1 = 255;
        short short2 = 255;
        short short3 = 255;
        return i << 24 | short1 << 16 | short2 << 8 | short3;
    }

    protected ResourceLocation getEntityTexture(EntityCreeperTitan entity) {
        switch (entity.fetchVariantGiant()) {
            case 1:
                return texturesCreeper2;
            case 2:
                return texturesCreeper3;
            case 3:
                return texturesCreeper4;
            case 4:
                return texturesCreeper5;
            default:
                return texturesCreeper;
        }
    }

    public void doRender(EntityCreeperTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
