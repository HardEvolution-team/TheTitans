package com.electrifiedded.thetitans.client.render.mutant;

import com.electrifiedded.thetitans.entity.titan.mutant.EntityTitanThrownBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Draws the giant thrown block: the block model scaled up, tumbling end over end. */
@SideOnly(Side.CLIENT)
public class RenderTitanThrownBlock extends Render<EntityTitanThrownBlock> {
    public RenderTitanThrownBlock(RenderManager rm) {
        super(rm);
        shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityTitanThrownBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
        IBlockState state = entity.getBlock();
        float s = entity.getBlockSize();
        float spin = (entity.getAge() + partialTicks) * 9.0F;
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + s * 0.5D, z);
        GlStateManager.rotate(spin, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(spin * 0.7F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(s, s, s);
        GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, 1.0F);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTitanThrownBlock entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
