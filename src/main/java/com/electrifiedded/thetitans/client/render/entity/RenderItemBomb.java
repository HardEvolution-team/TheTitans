package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityItemBomb;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderItemBomb extends Render<EntityItemBomb> {
    public RenderItemBomb(RenderManager rm) {
        super(rm);
    }

    @Override
    public void doRender(EntityItemBomb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        ItemStack stack = entity.fetchCardWild() ? new ItemStack(Items.FILLED_MAP) : entity.getEntityItem();
        if (stack != null && !stack.isEmpty()) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            // Original: scale = width * 2 (6 for a 3x3 bomb) on a 1x1 icon / full block, items another x1.25.
            // 1.12's GROUND transform shrinks items x0.5 and blocks x0.25, so render untransformed (NONE: a centred
            // unit cube) with the original scale instead — the drops were 4-8x too small.
            float f9 = entity.width * 2.0F;
            GL11.glScalef(f9, f9, f9);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GL11.glTranslatef(0, MathHelper.sin(((float)entity.ticksExisted + partialTicks) / 20.0F) * 0.1F + 0.1F, 0);
            GL11.glRotatef((float) ((((float)entity.ticksExisted + y) / 40.0F) * (180F / (float)Math.PI)), 0.0F, 1.0F, 0.0F);
            if (entity.fetchCardWild() || !(entity.getEntityItem().getItem() instanceof ItemBlock))
                GL11.glScalef(1.25F, 1.25F, 1.25F);
            GL11.glTranslatef(0, 0.5F, 0);
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            renderItem.renderItem(stack, ItemCameraTransforms.TransformType.NONE);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityItemBomb entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
