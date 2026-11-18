package com.electrifiedded.thetitans.client.render.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Reproduces the exact GL space a 1.7.10 Forge IItemRenderer was called in, from inside a 1.12.2 TEISR.
 *
 * For every view: bridge = (1.12.2 chain up to renderByItem)^-1 * (1.7.10 chain up to IItemRenderer.renderItem),
 * so a original renderItem(type) GL sequence replayed after begin() lands exactly where it did in 1.7.10 - including
 * swing/equip animation, which both versions apply before these chains in the same order.
 *
 * 1.7.10 chains (verified against Forge 1.7.10-10.13.4.1614 ForgeHooksClient bytecode + vanilla renderers):
 *  EQUIPPED_FIRST_PERSON  ItemRenderer: T(0.56,-0.52,-0.72) Ry(45) [swing] S(0.4) + renderEquippedItem chain
 *  EQUIPPED               RenderPlayer after arm.postRender: T(-1/16,7/16,1/16) + full3D or flat chain
 *                         + renderEquippedItem chain
 *  renderEquippedItem     T(0,-0.3,0) S(1.5) Ry(50) Rz(335) T(-0.9375,-0.0625,0)
 *  ENTITY                 RenderItem: T(x, y+bob, z) (y = minY+0.125 in 1.7.10) Ry(spin) S(0.5)
 *  INVENTORY              T(x-2,y+3) S(10) T(1,0.5,1) S(1,1,-1) Rx(210) Ry(45) Ry(-90)
 * 1.12.2 chains:
 *  FIRST_PERSON           T(i*0.56,-0.52,-0.72) Ry(i*45)[swing] Ry(i*-45) T(-0.5)
 *  THIRD_PERSON           arm.postRender Rx(-90) Ry(180) T(i/16,0.125,-0.625) T(-0.5)
 *  GROUND                 T(x, minY+bob+0.25, z) Ry(spin) T(-0.5)
 *  GUI                    T(x+8,y+8) S(16,-16,16) T(-0.5)
 * Left hand = mirror image of the right-hand pose (1.7.10 had no off-hand).
 */
@SideOnly(Side.CLIENT)
public final class LegacyItemTransforms {
    /** Which donor IItemRenderer case to replay. */
    public enum View { EQUIPPED, EQUIPPED_FIRST_PERSON, ENTITY, INVENTORY }

    private LegacyItemTransforms() {}

    private static boolean leftIs(TransformType t) {
        return t == TransformType.FIRST_PERSON_LEFT_HAND || t == TransformType.THIRD_PERSON_LEFT_HAND;
    }

    /**
     * Applies the bridge for the current 1.12.2 transform and returns the original view to replay.
     * @param full3D the original Item.isFull3D() (swords/tools true, spawn eggs false) - selects RenderPlayer's chain
     */
    public static View begin(TransformType t, boolean full3D) {
        switch (t) {
            case FIRST_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
                mirrorLeftIf(t);
                GlStateManager.translate(0.5F, 0.5F, 0.5F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(0.4F, 0.4F, 0.4F);
                chainEquipped();
                return View.EQUIPPED_FIRST_PERSON;
            case THIRD_PERSON_LEFT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
                mirrorLeftIf(t);
                GlStateManager.translate(0.5F, 0.5F, 0.5F);
                GlStateManager.translate(-0.0625F, -0.125F, 0.625F);
                GlStateManager.rotate(-180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(-0.0625F, 0.4375F, 0.0625F);
                if (full3D) {
                    GlStateManager.translate(0.0F, 0.1875F, 0.0F);
                    GlStateManager.scale(0.625F, -0.625F, 0.625F);
                    GlStateManager.rotate(-100.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                } else {
                    GlStateManager.translate(0.25F, 0.1875F, -0.1875F);
                    GlStateManager.scale(0.375F, 0.375F, 0.375F);
                    GlStateManager.rotate(60.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(20.0F, 0.0F, 0.0F, 1.0F);
                }
                chainEquipped();
                return View.EQUIPPED;
            case GUI:
                GlStateManager.translate(0.5F, 0.5F, 0.5F);
                GlStateManager.scale(0.0625F, -0.0625F, 0.0625F);
                GlStateManager.translate(-10.0F, -5.0F, 0.0F);
                GlStateManager.scale(10.0F, 10.0F, 10.0F);
                GlStateManager.translate(1.0F, 0.5F, 1.0F);
                GlStateManager.scale(1.0F, 1.0F, -1.0F);
                GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                return View.INVENTORY;
            case FIXED:
                // 1.7.10 item frames drew the ENTITY pose with renderInFrame: S(1.25) T(0,0.05,0) Ry(-90) S(0.5)
                GlStateManager.translate(0.5F, 0.5F, 0.5F);
                GlStateManager.scale(1.25F, 1.25F, 1.25F);
                GlStateManager.translate(0.0F, 0.05F, 0.0F);
                GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                return View.ENTITY;
            default: // GROUND, HEAD, NONE
                GlStateManager.translate(0.5F, 0.375F, 0.5F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                return View.ENTITY;
        }
    }

    /** Must be called after the donor pose, right before the model draws (closes the left-hand mirror). */
    public static void end(TransformType t) {
        if (leftIs(t))
            GlStateManager.scale(-1.0F, 1.0F, 1.0F);
    }

    // Left hand: reflect the whole right-hand pose about the item's x=0.5 plane. The second reflection in end()
    // keeps the determinant (face culling) identical to the right hand.
    private static void mirrorLeftIf(TransformType t) {
        if (!leftIs(t))
            return;
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.scale(-1.0F, 1.0F, 1.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
    }

    // ForgeHooksClient.renderEquippedItem (no EQUIPPED_BLOCK helper)
    private static void chainEquipped() {
        GlStateManager.translate(0.0F, -0.3F, 0.0F);
        GlStateManager.scale(1.5F, 1.5F, 1.5F);
        GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(335.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-0.9375F, -0.0625F, 0.0F);
    }
}
