package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelWitherMinion;
import com.electrifiedded.thetitans.client.render.layer.LayerWitherMinionCharge;
import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWitherMinion extends RenderLiving<EntityWitherMinion> {
    private static final ResourceLocation empowermentRayTexturesWither = new ResourceLocation(TheTitans.getTextures("textures/entities", "wither_empowerment_beam.png"));
    private static final ResourceLocation witherTexturesInvulnerable = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation texturesWither = new ResourceLocation("textures/entity/wither/wither.png");
    private int armourModePrev;

    public RenderWitherMinion(RenderManager rm) {
        super(rm, new ModelWitherMinion(), 1.0F);
        this.armourModePrev = ((ModelWitherMinion)this.mainModel).fetchTicksInvulnerable();
        this.addLayer(new LayerWitherMinionCharge(this));
    }

    public void doRender(EntityWitherMinion entity, double x, double y, double z, float entityYaw, float partialTicks) {
        int e = ((ModelWitherMinion)this.mainModel).fetchTicksInvulnerable();
        if (e != this.armourModePrev) {
            this.armourModePrev = e;
            this.mainModel = new ModelWitherMinion();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.master != null && entity.isEntityAlive()) {
            float f1 = (entity.ticksExisted + partialTicks);
            float f4 = (float)(entity.master.posX - entity.posX - (entity.prevPosX - entity.posX) * (1.0F - partialTicks));
            float f5 = (float)((entity.master.posY - entity.getEyeHeight() + entity.master.getEyeHeight()) - entity.posY - (entity.prevPosY - entity.posY) * (1.0F - partialTicks));
            float f6 = (float)(entity.master.posZ - entity.posZ - (entity.prevPosZ - entity.posZ) * (1.0F - partialTicks));
            float f7 = MathHelper.sqrt(f4 * f4 + f6 * f6);
            float f8 = MathHelper.sqrt(f4 * f4 + f5 * f5 + f6 * f6);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + entity.getEyeHeight(), (float)z);
            GL11.glRotatef((float)-Math.atan2(f6, f4) * 180.0F / 3.1415927F - 90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef((float)-Math.atan2(f7, f5) * 180.0F / 3.1415927F - 90.0F, 1.0F, 0.0F, 0.0F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_CULL_FACE);
            bindTexture(empowermentRayTexturesWither);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            float f9 = f1 * 0.005F;
            float f10 = MathHelper.sqrt(f4 * f4 + f5 * f5 + f6 * f6) / 32.0F + f9;
            byte b0 = 64;
            int c0 = 15728880;
            int j = c0 % 65536;
            int k = c0 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
            GL11.glEnable(2896);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 2F);
            buffer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
            for (int i = 0; i <= b0; ++i) {
                float f11 = MathHelper.sin((float)(i % b0) * (float)Math.PI * 2.0F / (float)b0) * 0.75F;
                float f12 = MathHelper.cos((float)(i % b0) * (float)Math.PI * 2.0F / (float)b0) * 0.75F;
                float f13 = (float)(i % b0) * 1.0F / (float)b0;
                buffer.pos((double)(f11 * 0.2F), (double)(f12 * 0.2F), 0.0D).tex((double)f13, (double)f10).color(0, 0, 0, 255).endVertex();
                buffer.pos((double)f11, (double)f12, (double)f8).tex((double)f13, (double)f9).color(255, 255, 255, 255).endVertex();
            }

            tessellator.draw();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glShadeModel(GL11.GL_FLAT);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(EntityWitherMinion entity) {
        int i = entity.getInvulTime();
        return (i > 0) && ((i > 80) || (i / 5 % 2 != 1)) ? witherTexturesInvulnerable : texturesWither;
    }

    protected void preRenderCallback(EntityWitherMinion entity, float partialTicks) {
        float f1 = 2.0F;
        int i = entity.getInvulTime();
        if (i > 0) {
            f1 -= (i - partialTicks) / 220.0F * 0.5F;
        }

        GL11.glScalef(f1, f1, f1);
    }
}
