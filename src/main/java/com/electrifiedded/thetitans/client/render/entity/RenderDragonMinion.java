package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.client.model.ModelDragonMinion;
import com.electrifiedded.thetitans.client.render.layer.LayerDragonMinionEyes;
import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;

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
public class RenderDragonMinion extends RenderLiving<EntityDragonMinion> {
    // TODO 1.7.10 render-pass overlay/eyes/equipped-items dropped; reimplement as LayerRenderer via addLayer() in ctor.
    private static final ResourceLocation dragonExplodingTexturesEnder = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
    private static final ResourceLocation dragonCrystalRayTexturesEnder = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
    private static final ResourceLocation dragonTexturesEnder = new ResourceLocation("textures/entity/enderdragon/dragon.png");
    /** An instance of the dragon model in RenderDragon */
    protected ModelDragonMinion dragonMesh;

    public RenderDragonMinion(RenderManager rm) {
        super(rm, new ModelDragonMinion(0.0F), 8.0F);
        this.dragonMesh = (ModelDragonMinion)this.mainModel;
        this.addLayer(new LayerDragonMinionEyes(this));
    }

    protected void corpseRotate(EntityDragonMinion entity, float ageInTicks, float rotationYaw, float partialTicks) {
        float f3 = (float)entity.getMovementOffsets(7, partialTicks)[0];
        float f4 = (float)(entity.getMovementOffsets(5, partialTicks)[1] - entity.getMovementOffsets(10, partialTicks)[1]);
        GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(f4 * 10.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, 1.0F);
        if (entity.deathTime > 0) {
            float f5 = ((float)entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f5 = MathHelper.sqrt(f5);
            if (f5 > 1.0F) {
                f5 = 1.0F;
            }

            GL11.glRotatef(f5 * this.getDeathMaxRotation(entity), 0.0F, 0.0F, 1.0F);
        }
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityDragonMinion entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity.deathTicks > 0) {
            float f6 = (float)entity.deathTicks / 200.0F;
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glAlphaFunc(GL11.GL_GREATER, f6);
            this.bindTexture(dragonExplodingTexturesEnder);
            this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glDepthFunc(GL11.GL_EQUAL);
        }

        this.bindEntityTexture(entity);
        this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        if (entity.hurtTime > 0) {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
            this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
    }

    public void doRender(EntityDragonMinion entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.healingEnderCrystal != null) {
            float f2 = (float)entity.healingEnderCrystal.innerRotation + partialTicks;
            float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
            f3 = (f3 * f3 + f3) * 0.2F;
            float f4 = (float)(entity.healingEnderCrystal.posX - entity.posX - (entity.prevPosX - entity.posX) * (double)(1.0F - partialTicks));
            float f5 = (float)((double)f3 + entity.healingEnderCrystal.posY - 1.0D - entity.posY - (entity.prevPosY - entity.posY) * (double)(1.0F - partialTicks));
            float f6 = (float)(entity.healingEnderCrystal.posZ - entity.posZ - (entity.prevPosZ - entity.posZ) * (double)(1.0F - partialTicks));
            float f7 = MathHelper.sqrt(f4 * f4 + f6 * f6);
            float f8 = MathHelper.sqrt(f4 * f4 + f5 * f5 + f6 * f6);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + 2.0F, (float)z);
            GL11.glRotatef((float)(-Math.atan2((double)f6, (double)f4)) * 180.0F / (float)Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef((float)(-Math.atan2((double)f7, (double)f5)) * 180.0F / (float)Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_CULL_FACE);
            this.bindTexture(dragonCrystalRayTexturesEnder);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            float f9 = 0.0F - ((float)entity.ticksExisted + partialTicks) * 0.01F;
            float f10 = MathHelper.sqrt(f4 * f4 + f5 * f5 + f6 * f6) / 32.0F - ((float)entity.ticksExisted + partialTicks) * 0.01F;
            buffer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
            byte b0 = 8;
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

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityDragonMinion entity) {
        return dragonTexturesEnder;
    }
}
