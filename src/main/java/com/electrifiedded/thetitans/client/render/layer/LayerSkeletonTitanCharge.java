package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelSkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerSkeletonTitanCharge implements LayerRenderer<EntitySkeletonTitan> {
    private static final ResourceLocation skeleton2OverlayTexturesWither = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/skeleton_titan/wither_skeleton_titan/solar_skeleton_titan_eyes.png"));
    private final RenderLivingBase<EntitySkeletonTitan> renderer;
    private final ModelSkeletonTitan meshOverlaying = new ModelSkeletonTitan(0.1F);

    public LayerSkeletonTitanCharge(RenderLivingBase<EntitySkeletonTitan> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntitySkeletonTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        float c01 = (float)entity.ticksExisted + partialTicks;

        // Pass A: armored charge glow (original pass==1)
        if (entity.isArmored() && entity.isEntityAlive()) {
            GL11.glDepthMask(!entity.isInvisible());
            Minecraft.getMinecraft().getTextureManager().bindTexture(TheTitans.giantWhiteTexture64x64Generic);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            float j1 = MathHelper.cos(c01 * 0.02F) * 5.0F;
            float k1 = c01 * 0.01F;
            GL11.glTranslatef(MathHelper.cos(c01 * 0.2F), k1, j1);
            this.renderer.getMainModel().setModelAttributes(this.meshOverlaying);
            this.meshOverlaying.stackHeld.isHidden = true;
            this.meshOverlaying.item2Held.isHidden = true;
            GL11.glMatrixMode(5888);
            GL11.glEnable(3042);
            if (entity.fetchKindSkeleton() == 1) {
                switch (entity.fetchVariantGiant()) {
                    case 1: GL11.glColor4f(0.9F + MathHelper.cos(c01 * 0.05F) * 0.2F, 0.3F, 0.2F, 1.0F); break;
                    case 2: GL11.glColor4f(0.2F, 0.2F, 0.3F, 1.0F); break;
                    default: GL11.glColor4f(0.1F, 0.1F, 0.1F, 1.0F);
                }
            } else {
                switch (entity.fetchVariantGiant()) {
                    case 1: GL11.glColor4f(0.76F + MathHelper.cos(c01 * 0.05F) * 0.1F, 0.69F, 0.5F, 1.0F); break;
                    case 2: GL11.glColor4f(0.7F + MathHelper.cos(c01 * 0.5F) * 0.3F, 0.0F, 0.7F + MathHelper.cos(c01 * 0.5F) * 0.3F, 1.0F); break;
                    case 3: GL11.glColor4f(0.1F, 0.5F, 0.6F + MathHelper.cos(c01 * 0.05F) * 0.1F, 1.0F); break;
                    case 4: GL11.glColor4f(0.4F, 0.0F, 0.4F, 1.0F); break;
                    default: GL11.glColor4f(0.5F, 0.6F + MathHelper.cos(c01 * 0.05F) * 0.1F, 0.6F + MathHelper.cos(c01 * 0.05F) * 0.1F, 1.0F);
                }
            }
            GL11.glDisable(2896);
            GL11.glBlendFunc(1, 1);
            this.meshOverlaying.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            // restore (original pass==2)
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glEnable(2896);
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDepthMask(true);
        }

        // Pass B: solar glowing-eyes overlay (original pass==0, type==1 && variant==1)
        if (entity.fetchVariantGiant() == 1 && entity.fetchKindSkeleton() == 1) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(skeleton2OverlayTexturesWither);
            GL11.glEnable(3042);
            GL11.glDisable(3008);
            GL11.glBlendFunc(1, 1);
            GL11.glDepthMask(!entity.isInvisible());
            int c0 = 15728880;
            int j = c0 % 65536;
            int k = c0 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (entity.getAnimID() == 10 && entity.deathTicks > 200) {
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
            }
            this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GL11.glDisable(3042);
            GL11.glEnable(3008);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDepthMask(true);
            Minecraft.getMinecraft().entityRenderer.enableLightmap();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
