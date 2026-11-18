package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelWitherzilla;
import com.electrifiedded.thetitans.client.render.layer.LayerWitherzillaCharge;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWitherzilla extends RenderLiving<EntityWitherzilla> {
    // TODO 1.7.10 render-pass overlay/eyes/equipped-items dropped; reimplement as LayerRenderer via addLayer() in ctor.
    private static final ResourceLocation truthAboutTheUniverseLaysIn1sAnd0sThe = new ResourceLocation(TheTitans.getTextures("textures/entities", "binary.png"));
    private static final ResourceLocation sheildWitherzilla = new ResourceLocation(TheTitans.getTextures("textures/entities", "wither_aura.png"));
    private static final ResourceLocation omegaTexturesWitherzilla = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/god_titans/witherzilla_omega.png"));
    private static final ResourceLocation texturesWitherzilla = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/god_titans/witherzilla.png"));
    private int armourModePrev;

    public RenderWitherzilla(RenderManager rm) {
        super(rm, new ModelWitherzilla(), 1.0F);
        this.armourModePrev = ((ModelWitherzilla)this.mainModel).fetchTicksInvulnerable();
        this.addLayer(new LayerWitherzillaCharge(this));
    }

    protected void renderModel(EntityWitherzilla entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.bindEntityTexture(entity);
        if (!entity.isInvisible()) {
            this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        else if (!entity.isInvisibleToPlayer(Minecraft.getMinecraft().player)) {
            if (entity.deathTicks > 0) {
                GL11.glDepthFunc(514);
                GL11.glDisable(3553);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                bindTexture(TheTitans.giantWhiteTexture64x64Generic);
                this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glEnable(3553);
                GL11.glDisable(3042);
                GL11.glDepthFunc(515);
            }

            if (entity.hurtTime > 0) {
                GL11.glDepthFunc(514);
                GL11.glDisable(3553);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
                this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glEnable(3553);
                GL11.glDisable(3042);
                GL11.glDepthFunc(515);
            }
        }

        else {
            this.mainModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        }
    }

    public void doRender(EntityWitherzilla entity, double x, double y, double z, float entityYaw, float partialTicks) {
        int i = ((ModelWitherzilla)this.mainModel).fetchTicksInvulnerable();
        if (i != this.armourModePrev) {
            this.armourModePrev = i;
            this.mainModel = new ModelWitherzilla();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityWitherzilla entity) {
        int i = entity.getInvulTime();
        return (entity.isArmored() ? truthAboutTheUniverseLaysIn1sAnd0sThe : ((i > 0) && ((i > 300) || (i / 10 % 2 != 1))) ? TheTitans.giantWhiteTexture64x64Generic : ((entity.isEntityInvulnerable(net.minecraft.util.DamageSource.GENERIC) || entity.isOmegaFormIn()) ? omegaTexturesWitherzilla : texturesWitherzilla));
    }

    protected void preRenderCallback(EntityWitherzilla entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant() * 2.0F;
        GL11.glScalef(f1, f1, f1);
    }
}
