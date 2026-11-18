package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.client.model.ModelBeam;
import com.electrifiedded.thetitans.client.model.ModelGargoyleTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerGargoyleTitanBeam implements LayerRenderer<EntityGargoyleTitan> {
    private static final ResourceLocation skinWater = new ResourceLocation("textures/blocks/water_still.png");
    private final RenderLivingBase<EntityGargoyleTitan> renderer;
    private final ModelBeam modelmatterV = new ModelBeam();

    public LayerGargoyleTitanBeam(RenderLivingBase<EntityGargoyleTitan> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityGargoyleTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!(entity.getAnimID() == 5 && entity.getAnimTick() > 24 && entity.getAnimTick() < 36))
            return;
        GL11.glDepthMask(!entity.isInvisible());
        ModelGargoyleTitan model = (ModelGargoyleTitan)this.renderer.getMainModel();
        GL11.glPushMatrix();
        model.torsoV.postRender(0.020833334F);
        model.torso.postRender(0.020833334F);
        model.skullZ.postRender(0.020833334F);
        float f1 = (float)entity.ticksExisted + partialTicks;
        Minecraft.getMinecraft().getTextureManager().bindTexture(skinWater);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(0.5F, 10.0F, 0.5F);
        GL11.glTranslatef(0.0F, -0.035F, 0.35F);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        float f3 = f1 * 0.01F;
        GL11.glTranslatef(0.0F, f3, 0.0F);
        GL11.glMatrixMode(5888);
        GL11.glEnable(3042);
        float f4 = 0.9F;
        GL11.glColor4f(f4, f4, f4, f4);
        GL11.glDisable(2896);
        GL11.glBlendFunc(1, 1);
        this.modelmatterV.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
