package com.electrifiedded.thetitans.client.render.item;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelTitanSpawnEgg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

// Port of 1.7.10 IItemRenderer egg renderers. All 20 per-titan egg renders were
// near-identical (same model + GL transform, differing only in eggtypeV + texture list), so the shared
// draw lives here and each subclass just supplies its texture array + eggtypeV.
// Original poses replayed verbatim inside LegacyItemTransforms (exact 1.7.10 GL spaces). Case mapping verified
// from the original switch-map bytecode: 1=EQUIPPED, 2=EQUIPPED_FIRST_PERSON, 3=ENTITY, 4=INVENTORY.
public class RenderTitanSpawnEgg extends TileEntityItemStackRenderer {
    protected final com.electrifiedded.thetitans.client.model.ModelTitanSpawnEgg eggmodelV = new com.electrifiedded.thetitans.client.model.ModelTitanSpawnEgg();
    protected final net.minecraft.util.ResourceLocation[] textures;
    protected final int eggtypeV;

    public RenderTitanSpawnEgg(int eggtypeV, String[] eggPaths) {
        this.eggtypeV = eggtypeV;
        this.textures = new net.minecraft.util.ResourceLocation[eggPaths.length];
        for (int i = 0; i < eggPaths.length; ++i)
            this.textures[i] = new net.minecraft.util.ResourceLocation(com.electrifiedded.thetitans.TheTitans.getTextures("textures/entities", "eggs/" + eggPaths[i]));
    }

    public void renderByItem(ItemStack stack) {
        int meta = MathHelper.clamp(stack.getMetadata(), 0, this.textures.length - 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.textures[meta]);
        GlStateManager.pushMatrix();
        TransformType type = PerspectiveAwareModel.transformLive;
        // spawn eggs are not isFull3D() in 1.7.10 -> RenderPlayer's flat-item chain in third person
        switch (LegacyItemTransforms.begin(type, false)) {
            case EQUIPPED:
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(0.0F, -0.1F, 0.2F);
                break;
            case EQUIPPED_FIRST_PERSON:
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(125.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(-0.45F, -0.2F, 0.5F);
                break;
            case ENTITY:
                GlStateManager.scale(3.0F, 3.0F, 3.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(0.02F, -0.15F, 0.02F);
                break;
            case INVENTORY:
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                break;
        }
        LegacyItemTransforms.end(type);
        this.eggmodelV.render(this.eggtypeV, null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
