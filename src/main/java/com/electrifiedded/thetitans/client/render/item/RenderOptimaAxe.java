package com.electrifiedded.thetitans.client.render.item;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelOptimaAxe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

// Original 1.7.10 RenderOptimaAxe poses replayed verbatim inside LegacyItemTransforms (exact 1.7.10 GL spaces).
// Case mapping verified from the original's switch-map bytecode: 1=EQUIPPED, 2=EQUIPPED_FIRST_PERSON, 3=ENTITY, 4=INVENTORY.
public class RenderOptimaAxe extends TileEntityItemStackRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TheTitans.getTextures("textures/entities", "optima_axe.png"));
    private final ModelOptimaAxe swordmodelV = new ModelOptimaAxe();

    public void renderByItem(ItemStack stack) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        TransformType t = PerspectiveAwareModel.transformLive;
        GlStateManager.pushMatrix();
        switch (LegacyItemTransforms.begin(t, true)) {
            case EQUIPPED:
                GlStateManager.scale(1.5F, 1.5F, 1.5F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-80.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(0.0F, -1.0F, -0.3F);
                break;
            case EQUIPPED_FIRST_PERSON:
                GlStateManager.scale(1.5F, 1.5F, 1.5F);
                GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-240.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(-0.75F, -1.5F, -0.25F);
                break;
            case ENTITY:
                GlStateManager.scale(3.0F, 3.0F, 3.0F);
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, -1.5F, 0.0F);
                break;
            case INVENTORY:
                GlStateManager.scale(0.15F, 0.15F, 0.15F);
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(75.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, 5.0F, 0.0F);
                break;
        }
        LegacyItemTransforms.end(t);
        this.swordmodelV.render(0.0625F);
        GlStateManager.popMatrix();
    }
}
