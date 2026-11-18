package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelBeam;
import com.electrifiedded.thetitans.client.model.ModelGargoyle;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerGargoyleBeam implements LayerRenderer<EntityGargoyle> {
    private static final ResourceLocation rayTexturesGargoyle = new ResourceLocation(TheTitans.getTextures("textures/entities", "gargoyle_beam.png"));
    private final RenderLivingBase<EntityGargoyle> renderer;
    private final ModelBeam rayMesh = new ModelBeam();

    public LayerGargoyleBeam(RenderLivingBase<EntityGargoyle> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityGargoyle entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity.fetchKindGargoyle() != 3)
            return;
        GL11.glDepthMask(!entity.isInvisible());
        int x = MathHelper.floor(entity.posX);
        int y = MathHelper.floor(entity.getEntityBoundingBox().minY - 0.5D);
        int z = MathHelper.floor(entity.posZ);
        boolean flag = entity.world.getBlockState(new BlockPos(x, y, z)).getBlock() == entity.fetchCubeToPerchFavorite();
        ModelGargoyle model = (ModelGargoyle)this.renderer.getMainModel();
        model.torsoV.postRender(0.020833334F);
        model.torso.postRender(0.020833334F);
        model.skullZ.postRender(0.020833334F);
        float f1 = (float)entity.ticksExisted + partialTicks;
        Minecraft.getMinecraft().getTextureManager().bindTexture(rayTexturesGargoyle);
        GL11.glTranslatef(0.0F, -0.5F, 0.0F);
        if (!entity.onGround)
            GL11.glTranslatef(0.0F, 0.25F, 0.0F);
        if (flag)
            GL11.glTranslatef(0.0F, 0.3F, -0.65F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(0.75F, 10.0F, 0.4F);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, f1 * 0.01F, 0.0F);
        GL11.glMatrixMode(5888);
        GL11.glEnable(3042);
        float f4 = 0.5F;
        GL11.glColor4f(f4, f4, f4, f4);
        GL11.glDisable(2896);
        GL11.glBlendFunc(1, 1);
        GL11.glDisable(3008);
        int i = 61680;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(i % 65536), (float)(i / 65536));
        rayMesh.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
