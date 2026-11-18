package com.electrifiedded.thetitans.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderLightningBall extends Render<EntityFireball> {
    private float scale;

    public RenderLightningBall(RenderManager rm, float scale) {
        super(rm);
        this.scale = scale;
    }

    @Override
    public void doRender(EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f2 = this.scale;
        GL11.glScalef(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItem(new ItemStack(Items.SNOWBALL), ItemCameraTransforms.TransformType.GROUND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFireball entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
