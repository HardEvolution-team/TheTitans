package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityTitanFireball;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderTitanFireball extends Render<EntityTitanFireball> {
    public RenderTitanFireball(RenderManager rm) {
        super(rm);
    }

    @Override
    public void doRender(EntityTitanFireball fireball, double x, double y, double z, float entityYaw, float partialTicks) {
        Item item;
        switch (fireball.fetchIDFireball()) {
            case 1:item = Items.GUNPOWDER;
                break;
            case 2:item = Items.BLAZE_POWDER;
                break;
            case 3:item = Items.ROTTEN_FLESH;
                break;
            case 4:item = Items.ENDER_PEARL;
                break;
            case 5:item = Items.IRON_INGOT;
                break;
            case 6:item = Items.SNOWBALL;
                break;
            default:item = Items.FIRE_CHARGE;
        }

        if (item != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f1 = fireball.width * 2F;
            GL11.glScalef(f1, f1, f1);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            renderItem.renderItem(new ItemStack(item), ItemCameraTransforms.TransformType.GROUND);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTitanFireball entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
