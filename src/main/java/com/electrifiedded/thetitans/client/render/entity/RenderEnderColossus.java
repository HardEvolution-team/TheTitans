package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelEnderColossus;
import com.electrifiedded.thetitans.client.render.layer.LayerEnderColossusEyes;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEnderColossus extends RenderLiving<EntityEnderColossus> {
    private static final ResourceLocation irisRaySkinEnderman = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/ender_colossus_beam.png"));
    private static final ResourceLocation texturesEnderman = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/ender_colossus.png"));
    private static final ResourceLocation slainTexturesEnderman = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/ender_colossus_dead.png"));
    private static final ResourceLocation texturesEnderman2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/holy_ender_colossus.png"));
    private static final ResourceLocation slainTexturesEnderman2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/holy_ender_colossus_dead.png"));
    private static final ResourceLocation texturesEnderman3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/icey_ender_colossus.png"));
    private static final ResourceLocation slainTexturesEnderman3 = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/ender_colossus/icey_ender_colossus_dead.png"));
    private ModelEnderColossus meshEnderman;

    public RenderEnderColossus(RenderManager rm) {
        super(rm, new ModelEnderColossus(), 0.5F);
        this.meshEnderman = ((ModelEnderColossus)this.mainModel);
        this.addLayer(new LayerEnderColossusEyes(this));
    }

    protected void preRenderCallback(EntityEnderColossus entity, float partialTicks) {
        float f1 = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(f1, f1, f1);
        GL11.glTranslatef(0.0F, 0.015F, 0.0F);
        if (entity.getRidingEntity() != null) {
            GL11.glTranslatef(0.0F, 0.1F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }
    }

    public void doRender(EntityEnderColossus entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.meshEnderman.isAttacking = entity.isScreaming();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.fetchLaserTicksEye() >= 0 && entity.getHealth() > 0.0F) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            bindTexture(irisRaySkinEnderman);
            GL11.glTexParameterf(3553, 10242, 10497.0F);
            GL11.glTexParameterf(3553, 10243, 10497.0F);
            GL11.glDisable(2896);
            GL11.glDisable(2884);
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 15728880 % 65536 / 1.0F, 15728880 / 65536 / 1.0F);
            OpenGlHelper.glBlendFunc(770, 1, 1, 0);
            float f4 = (float)entity.world.getTotalWorldTime() + partialTicks;
            float f5 = f4 * 0.5F % 1.0F;
            float f6 = entity.getEyeHeight();
            GL11.glPushMatrix();
            float f = entity.rotationYawHead * 3.1415927F / 180.0F;
            MathHelper.sin(f);
            MathHelper.cos(f);
            GL11.glTranslatef((float)x, (float)y + f6 + ((0.02F + 0.02F * MathHelper.cos(partialTicks * 0.05F)) * 3.1415927F), (float)z);
            GL11.glScalef(entity.fetchScaleMultiplierGiant(), entity.fetchScaleMultiplierGiant(), entity.fetchScaleMultiplierGiant());
            Vec3d vec3 = fetchEndBeam(entity, f6, partialTicks);
            Vec3d vec31 = fetchStartBeam(entity, f6, partialTicks);
            Vec3d vec32 = vec3.subtract(vec31);
            double d3 = vec32.length() + 0.1D;
            vec32 = vec32.normalize();
            float f7 = (float)Math.acos(vec32.y);
            float f8 = (float)Math.atan2(vec32.z, vec32.x);
            GL11.glRotatef((1.5707964F + -f8) * 57.295776F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(f7 * 57.295776F, 1.0F, 0.0F, 0.0F);
            double d4 = f4 * 0.005D * -1.5D;
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            double d15 = Math.cos(d4 * 0.0D + 3.141592653589793D) * 0.2D;
            double d16 = Math.sin(d4 * 0.0D + 3.141592653589793D) * 0.2D;
            double d17 = Math.cos(d4 * 0.0D + 0.0D) * 0.2D;
            double d18 = Math.sin(d4 * 0.0D + 0.0D) * 0.2D;
            double d25 = -1.0F + f5;
            double d26 = d3 * 2.5D + d25;
            buffer.pos(d15, -d3, d16).tex(0.5D, d26).color(255, 0, 255, 255).endVertex();
            buffer.pos(d15, 0.0D, d16).tex(0.5D, d25).color(255, 0, 255, 255).endVertex();
            buffer.pos(d17, 0.0D, d18).tex(0.0D, d25).color(255, 0, 255, 255).endVertex();
            buffer.pos(d17, -d3, d18).tex(0.0D, d26).color(255, 0, 255, 255).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
        }
    }

    private Vec3d fetchStartBeam(EntityLivingBase entity, double eyeHeight, float partialTicks) {
        double d1 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double d2 = eyeHeight + entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        return new Vec3d(d1, d2, d3);
    }

    private Vec3d fetchEndBeam(EntityLivingBase entity, double eyeHeight, float partialTicks) {
        Vec3d vec3 = entity.getLookVec();
        double dx = vec3.x * 300D;
        double dy = vec3.y * 300D;
        double dz = vec3.z * 300D;
        double d1 = entity.lastTickPosX + (entity.posX + dx - entity.lastTickPosX) * partialTicks;
        double d2 = eyeHeight + entity.lastTickPosY + (entity.posY + dy - entity.lastTickPosY) * partialTicks;
        double d3 = entity.lastTickPosZ + (entity.posZ + dz - entity.lastTickPosZ) * partialTicks;
        return new Vec3d(d1, d2, d3);
    }

    protected ResourceLocation getEntityTexture(EntityEnderColossus entity) {
        if (entity.getAnimID() == 10 && entity.deathTicks > 200) {
            switch (entity.fetchVariantGiant()) {
                case 1:
                    return slainTexturesEnderman2;
                case 2:
                    return slainTexturesEnderman3;
                default:
                    return slainTexturesEnderman;
            }
        }

        else {
            switch (entity.fetchVariantGiant()) {
                case 1:
                    return texturesEnderman2;
                case 2:
                    return texturesEnderman3;
                default:
                    return texturesEnderman;
            }
        }
    }
}
