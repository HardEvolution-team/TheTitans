package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelCreeperMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCreeperMinion extends RenderLiving<EntityCreeperMinion> {
    private static final ResourceLocation texturesCreeper = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RenderCreeperMinion(RenderManager renderManager) {
        super(renderManager, new ModelCreeper(), 0.5F);
        this.addLayer(new LayerMinionRobes<EntityCreeperMinion>(this,
                new ModelCreeperMinion(0.15F), new ModelCreeperMinion(0.15F), new ModelCreeperMinion(0.151F), new ModelCreeperMinion(0.01F),
                "minions/overlay/creeper_overlay.png", "minions/overlay/creeper_overlay_shoes.png", "minions/overlay/creeper_overlay_herald.png", "minions/glow/creeper.png"));
        this.addLayer(new LayerCreeperMinionCharge(this));
    }

    protected void preRenderCallback(EntityCreeperMinion entity, float partialTicks) {
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
        float f3 = (1.0F + f1 * 0.4F) * f2;
        float f4 = (1.0F + f1 * 0.1F) / f2;
        GlStateManager.scale(f3, f4, f3);
        GlStateManager.translate(0.0F, 0.125F, 0.0F);
    }

    protected int getColorMultiplier(EntityCreeperMinion entity, float arg77030n2, float partialTicks) {
        float f2 = entity.getCreeperFlashIntensity(partialTicks);
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

    protected ResourceLocation getEntityTexture(EntityCreeperMinion entity) {
        return texturesCreeper;
    }
}
