package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.client.model.ModelWitherzillaMinion;
import com.electrifiedded.thetitans.client.render.layer.LayerWitherzillaMinionCharge;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWitherzillaMinion extends RenderLiving<EntityWitherzillaMinion> {
    private static final ResourceLocation witherTexturesInvulnerable = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation texturesWither = new ResourceLocation("textures/entity/wither/wither.png");
    private int armourModePrev;

    public RenderWitherzillaMinion(RenderManager rm) {
        super(rm, new ModelWitherzillaMinion(), 1.0F);
        this.armourModePrev = ((ModelWitherzillaMinion)this.mainModel).fetchTicksInvulnerable();
        this.addLayer(new LayerWitherzillaMinionCharge(this));
    }

    public void doRender(EntityWitherzillaMinion entity, double x, double y, double z, float entityYaw, float partialTicks) {
        int i = ((ModelWitherzillaMinion)this.mainModel).fetchTicksInvulnerable();
        if (i != this.armourModePrev) {
            this.armourModePrev = i;
            this.mainModel = new ModelWitherzillaMinion();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityWitherzillaMinion entity) {
        int i = entity.getInvulTime();
        return (i > 0) && ((i > 80) || (i / 5 % 2 != 1)) ? witherTexturesInvulnerable : texturesWither;
    }

    protected void preRenderCallback(EntityWitherzillaMinion entity, float partialTicks) {
        int i = entity.getInvulTime();
        if (i > 0) {
            float f1 = 2.0F - (i - partialTicks) / 220.0F * 0.5F;
            GL11.glScalef(f1, f1, f1);
        }

        else {
            GL11.glScalef(2.0F, 2.0F, 2.0F);
        }
    }
}
