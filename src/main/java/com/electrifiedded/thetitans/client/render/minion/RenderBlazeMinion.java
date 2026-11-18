package com.electrifiedded.thetitans.client.render.minion;

import com.electrifiedded.thetitans.client.model.ModelBlazeMinion;
import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion;

import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlazeMinion extends RenderLiving<EntityBlazeMinion> {
    private static final ResourceLocation texturesBlaze = new ResourceLocation("textures/entity/blaze.png");

    public RenderBlazeMinion(RenderManager renderManager) {
        super(renderManager, new ModelBlaze(), 0.5F);
        this.addLayer(new LayerMinionRobes<EntityBlazeMinion>(this,
                new ModelBlazeMinion(0.1F), null, new ModelBlazeMinion(0.11F), new ModelBlazeMinion(0.01F),
                "minions/overlay/blaze_overlay.png", null, "minions/overlay/blaze_overlay_herald.png", "minions/glow/blaze.png"));
    }

    protected ResourceLocation getEntityTexture(EntityBlazeMinion entity) {
        return texturesBlaze;
    }
}
