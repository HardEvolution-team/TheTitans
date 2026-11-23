package com.electrifiedded.thetitans.client.render.mutant;

import com.electrifiedded.thetitans.entity.titan.mutant.EntityTitanBonePart;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Draws one bone of the Mutant Skeleton rig (the mutant's body-part model) at the fallen titan's scale. */
@SideOnly(Side.CLIENT)
public class RenderTitanBonePart extends Render<EntityTitanBonePart> {
    private static final class Bones extends ModelBase {
        final ModelRenderer pelvis, rib, head, arm, forearm, leg, foreleg, shoulder;

        Bones() {
            textureWidth = 128;
            textureHeight = 192; // the titan's extended skeleton texture
            pelvis = new ModelRenderer(this, 0, 16);
            pelvis.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 6);
            rib = new ModelRenderer(this, 32, 12);
            rib.addBox(-3.0F, -1.0F, -1.0F, 6, 2, 2, 0.25F);
            head = new ModelRenderer(this, 0, 0);
            head.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.4F);
            ModelRenderer jaw = new ModelRenderer(this, 72, 0);
            jaw.addBox(-4.0F, -3.0F, -8.0F, 8, 3, 8, 0.7F);
            jaw.setRotationPoint(0.0F, 3.8F, 3.7F);
            jaw.rotateAngleX = 0.09817477F;
            head.addChild(jaw);
            arm = new ModelRenderer(this, 0, 28);
            arm.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
            forearm = new ModelRenderer(this, 16, 28);
            forearm.addBox(-2.0F, -7.0F, -2.0F, 4, 14, 4, -0.01F);
            leg = new ModelRenderer(this, 0, 28);
            leg.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
            foreleg = new ModelRenderer(this, 32, 28);
            foreleg.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
            shoulder = new ModelRenderer(this, 28, 16);
            shoulder.addBox(-4.0F, -1.5F, -3.0F, 8, 3, 6);
        }

        ModelRenderer of(int part) {
            if (part == 0)
                return pelvis;
            if (part >= 1 && part < 19)
                return rib;
            if (part == 19)
                return head;
            if (part == 21 || part == 22)
                return arm;
            if (part == 23 || part == 24)
                return forearm;
            if (part == 25 || part == 26)
                return leg;
            if (part == 27 || part == 28)
                return foreleg;
            return shoulder;
        }
    }

    private final Bones model = new Bones();

    public RenderTitanBonePart(RenderManager rm) {
        super(rm);
        shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityTitanBonePart entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        float s = entity.getScale() * 1.2F;
        GlStateManager.translate((float)x, (float)y + entity.height * 0.5F, (float)z);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.rotate(yaw, 0.2F, 0.9F, -0.1F);
        GlStateManager.rotate(pitch, 0.9F, 0.1F, 0.2F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(s, -s, -s);
        bindEntityTexture(entity);
        model.of(entity.getPart()).render(0.0625F);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTitanBonePart entity) {
        return RenderMutantSkeletonTitan.texture(entity.getVariant());
    }
}
