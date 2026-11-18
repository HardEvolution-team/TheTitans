package com.electrifiedded.thetitans.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderWebShot extends Render<EntityFireball> {
    private float scale;

    public RenderWebShot(RenderManager rm, float scale) {
        super(rm);
        this.scale = scale;
    }

    @Override
    public void doRender(EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 1.5F, (float)z);
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef((entity.ticksExisted + partialTicks) * (MathHelper.cos(partialTicks * 0.05F) * 10.0F), 0.0F, 1.0F, 0.0F);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItem(new ItemStack(Blocks.WEB), ItemCameraTransforms.TransformType.GROUND);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFireball entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
