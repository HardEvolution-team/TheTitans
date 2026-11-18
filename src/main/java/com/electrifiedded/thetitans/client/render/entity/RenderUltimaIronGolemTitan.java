package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelIronGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderUltimaIronGolemTitan extends RenderLiving<EntityIronGolemTitan> {
    // TODO 1.7.10 render-pass overlay/eyes/equipped-items dropped; reimplement as LayerRenderer via addLayer() in ctor.
    private static final ResourceLocation golemTexturesIron = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/golem_titans/iron_golem_titan.png"));
    private ModelIronGolemTitan meshGolem;

    public RenderUltimaIronGolemTitan(RenderManager rm) {
        super(rm, new ModelIronGolemTitan(), 1.0F);
        this.meshGolem = ((ModelIronGolemTitan)this.mainModel);
    }

    protected ResourceLocation getEntityTexture(EntityIronGolemTitan entity) {
        return golemTexturesIron;
    }

    protected void applyRotations(EntityIronGolemTitan entity, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
    }

    protected void preRenderCallback(EntityIronGolemTitan entity, float partialTicks) {
        float fl = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(fl, fl, fl);
        GL11.glTranslatef(0.0F, 0.0275F, 0.0F);
    }

    public void doRender(EntityIronGolemTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
