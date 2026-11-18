package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerCreeperTitanCharge implements LayerRenderer<EntityCreeperTitan> {
    private static final ResourceLocation creeperTexturesArmored = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/creeper_titan_charge.png"));
    private static final ResourceLocation creeper2TexturesArmored = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/hell_creeper_titan_charge.png"));
    private static final ResourceLocation creeper3TexturesArmored = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/ghost_creeper_titan_charge.png"));
    private static final ResourceLocation creeper4TexturesArmored = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/ender_creeper_titan_charge.png"));
    private static final ResourceLocation creeper5TexturesArmored = new ResourceLocation(TheTitans.getTextures("textures/entities", "titans/creeper_titan/void_creeper_titan_charge.png"));
    private final RenderLivingBase<EntityCreeperTitan> renderer;
    private final ModelCreeperTitan creeperModel = new ModelCreeperTitan(1.6F);

    public LayerCreeperTitanCharge(RenderLivingBase<EntityCreeperTitan> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityCreeperTitan entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.getPowered())
            return;
        GL11.glDepthMask(!entity.isInvisible());
        float f1 = (float)entity.ticksExisted + partialTicks;
        ResourceLocation tex;
        switch (entity.fetchVariantGiant()) {
            case 1: tex = creeper2TexturesArmored; break;
            case 2: tex = creeper3TexturesArmored; break;
            case 3: tex = creeper4TexturesArmored; break;
            case 4: tex = creeper5TexturesArmored; break;
            default: tex = creeperTexturesArmored;
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        float f3 = f1 * 0.01F;
        GL11.glTranslatef(0.0F, f3, f3);
        GL11.glMatrixMode(5888);
        GL11.glEnable(3042);
        float f4 = 0.75F;
        GL11.glColor4f(f4, f4, f4, 1.0F);
        GL11.glDisable(2896);
        GL11.glBlendFunc(1, 1);
        this.renderer.getMainModel().setModelAttributes(this.creeperModel);
        this.creeperModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
