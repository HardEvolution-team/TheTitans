package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitanFireball;

import net.minecraft.block.Block;
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
public class RenderGargoyleTitanFireball extends Render<EntityGargoyleTitanFireball> {
    public RenderGargoyleTitanFireball(RenderManager rm) {
        super(rm);
        this.shadowSize = 0.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGargoyleTitanFireball entity) {
        return null;
    }

    @Override
    public void doRender(EntityGargoyleTitanFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 1.5F, (float)z);
        GL11.glScalef(3F, 3F, 3F);
        GL11.glRotatef((entity.ticksExisted + partialTicks) * 10F, 1.0F, 1.0F, 1.0F);
        int i = 15728880;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        switch (entity.fetchVarientMesh()) {
            case 0:
                this.renderBlock(entity);
                this.renderBlock(entity);
                GL11.glTranslatef(0.5F, -0.25F, -0.5F);
                GL11.glRotatef(30F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.5F, 0.0F, 0.25F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.25F, 0.5F, 0.0F);
                GL11.glRotatef(-40F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(0.0F, -0.25F, 0.5F);
                GL11.glRotatef(-50F, 1.0F, 1.0F, 1.0F);
                break;
            case 1:
                this.renderBlock(entity);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.5F, 0.25F, -0.5F);
                GL11.glRotatef(60F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.5F, 1.0F, 0.25F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.25F, -0.5F, 0.0F);
                GL11.glRotatef(-40F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(0.25F, -0.25F, -0.5F);
                GL11.glRotatef(-50F, 1.0F, 1.0F, 1.0F);
                break;
            case 2:
                this.renderBlock(entity);
                this.renderBlock(entity);
                GL11.glTranslatef(0.5F, 0.5F, -0.25F);
                GL11.glRotatef(15F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(0.5F, -1.0F, -1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.5F, 0.5F, 0.5F);
                GL11.glRotatef(30F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(0.0F, -0.25F, -1.5F);
                GL11.glRotatef(60F, 1.0F, 1.0F, 1.0F);
                break;
            case 3:
                this.renderBlock(entity);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.5F, 0.25F, 0.5F);
                GL11.glRotatef(-30F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(0.5F, 0.0F, -0.25F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.25F, 0.5F, 0.0F);
                GL11.glRotatef(40F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(0.0F, 0.25F, -0.5F);
                GL11.glRotatef(50F, 1.0F, 1.0F, 1.0F);
                break;
            case 4:
                this.renderBlock(entity);
                this.renderBlock(entity);
                GL11.glTranslatef(1.5F, -0.25F, -0.5F);
                GL11.glRotatef(30F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(-1.5F, 0.5F, 0.25F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.25F, 0.5F, 0.0F);
                GL11.glRotatef(80F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(0.0F, -0.25F, 0.5F);
                GL11.glRotatef(30F, 1.0F, 1.0F, 1.0F);
                break;
            case 5:
                this.renderBlock(entity);
                this.renderBlock(entity);
                GL11.glTranslatef(0.5F, -0.75F, 0.5F);
                GL11.glRotatef(30F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.5F, 0.0F, -0.25F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.25F, -0.5F, 0.0F);
                GL11.glRotatef(-40F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.25F, -0.25F, 0.5F);
                GL11.glRotatef(-50F, 1.0F, 1.0F, 1.0F);
                break;
            case 6:
                this.renderBlock(entity);
                this.renderBlock(entity);
                GL11.glTranslatef(0.5F, -0.25F, -0.5F);
                GL11.glRotatef(-60F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.5F, 0.0F, 0.25F);
                this.renderBlock(entity);
                GL11.glTranslatef(-0.25F, 0.5F, 0.0F);
                GL11.glRotatef(90F, 1.0F, 1.0F, 1.0F);
                this.renderBlock(entity);
                GL11.glTranslatef(0.0F, -0.25F, 0.5F);
                GL11.glRotatef(-30F, 1.0F, 1.0F, 1.0F);
        }

        GL11.glPopMatrix();
    }

    private void renderBlock(EntityGargoyleTitanFireball entity) {
        Block block;
        switch (entity.getBlockType()) {
            case 0:
                block = Blocks.STONE;
                break;
            case 1:
                block = Blocks.COAL_ORE;
                break;
            case 2:
                block = Blocks.IRON_ORE;
                break;
            case 3:
                block = Blocks.REDSTONE_ORE;
                break;
            case 4:
                block = Blocks.GOLD_ORE;
                break;
            case 5:
                block = Blocks.DIAMOND_ORE;
                break;
            case 6:
                block = Blocks.EMERALD_ORE;
                break;
            default:
                block = Blocks.STONE;
        }
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItem(new ItemStack(block), ItemCameraTransforms.TransformType.GROUND);
    }
}
