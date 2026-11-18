package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityLavaSpit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderLavaSpit extends Render<EntityLavaSpit> {
    public RenderLavaSpit(RenderManager rm) {
        super(rm);
        this.shadowSize = 0.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLavaSpit entity) {
        return null;
    }

    @Override
    public void doRender(EntityLavaSpit entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 1.5F, (float)z);
        GL11.glScalef(3F, 3F, 3F);
        int i = 15728880;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItem(new ItemStack(Blocks.NETHERRACK), ItemCameraTransforms.TransformType.GROUND);
        GL11.glPopMatrix();
    }
}
