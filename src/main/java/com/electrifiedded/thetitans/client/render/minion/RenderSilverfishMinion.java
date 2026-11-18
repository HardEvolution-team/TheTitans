package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelSilverfishMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySilverfishMinion;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSilverfishMinion extends RenderLiving<EntitySilverfishMinion> {
    private static final ResourceLocation texturesSilverfish = new ResourceLocation("textures/entity/silverfish.png");

    public RenderSilverfishMinion(RenderManager renderManager) {
        super(renderManager, new ModelSilverfish(), 0.3F);
        this.addLayer(new LayerMinionRobes<EntitySilverfishMinion>(this,
                new ModelSilverfishMinion(0.15F), null, new ModelSilverfishMinion(0.151F), new ModelSilverfishMinion(0.01F),
                "minions/overlay/silverfish_overlay.png", null, "minions/overlay/silverfish_overlay_herald.png", "minions/glow/silverfish.png"));
    }

    protected float getDeathMaxRotation(EntitySilverfishMinion arg77037n1) {
        return 180.0F;
    }

    protected ResourceLocation getEntityTexture(EntitySilverfishMinion mob) {
        return texturesSilverfish;
    }
}
