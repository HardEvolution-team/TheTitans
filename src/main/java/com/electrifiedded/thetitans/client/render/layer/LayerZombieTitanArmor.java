package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerZombieTitanArmor implements LayerRenderer<EntityZombieTitan> {
    private final RenderLivingBase<EntityZombieTitan> renderer;
    private final ModelZombieTitan meshOverlaying = new ModelZombieTitan(0.1F);

    public LayerZombieTitanArmor(RenderLivingBase<EntityZombieTitan> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityZombieTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.isArmored() || !entity.isEntityAlive())
            return;
        GL11.glDepthMask(!entity.isInvisible());
        float f1 = (float)entity.ticksExisted + partialTicks;
        Minecraft.getMinecraft().getTextureManager().bindTexture(TheTitans.giantWhiteTexture64x64Generic);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        float f2 = MathHelper.cos(f1 * 0.02F) * 5.0F;
        float f3 = f1 * 0.01F;
        GL11.glTranslatef(MathHelper.cos(f1 * 0.2F), f3, f2);
        GL11.glMatrixMode(5888);
        GL11.glEnable(3042);
        GL11.glColor4f(0.0F, 0.6F + MathHelper.cos(f1 * 0.05F) * 0.3F, 0.0F, 1.0F);
        GL11.glDisable(2896);
        GL11.glBlendFunc(1, 1);
        this.renderer.getMainModel().setModelAttributes(this.meshOverlaying);
        this.meshOverlaying.stackHeld.showModel = false;
        this.meshOverlaying.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
