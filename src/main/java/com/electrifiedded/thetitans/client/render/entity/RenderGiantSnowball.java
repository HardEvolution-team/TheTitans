package com.electrifiedded.thetitans.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderGiantSnowball extends Render<Entity> {
    private Item item;
    private int meta;
    private float scale;

    public RenderGiantSnowball(RenderManager rm, Item item, int meta, float scale) {
        super(rm);
        this.item = item;
        this.meta = meta;
        this.scale = scale;
    }

    public RenderGiantSnowball(RenderManager rm, Item item) {
        this(rm, item, 0, 4F);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (this.item != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f1 = this.scale;
            GL11.glScalef(f1 / 1.0F, f1 / 1.0F, f1 / 1.0F);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            renderItem.renderItem(new ItemStack(this.item, 1, this.meta), ItemCameraTransforms.TransformType.GROUND);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
