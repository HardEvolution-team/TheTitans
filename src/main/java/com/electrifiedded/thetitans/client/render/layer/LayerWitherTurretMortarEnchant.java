package com.electrifiedded.thetitans.client.render.layer;

import com.electrifiedded.thetitans.client.model.ModelWitherMortar;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretMortar;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerWitherTurretMortarEnchant implements LayerRenderer<EntityWitherTurretMortar> {
    private final RenderLivingBase<EntityWitherTurretMortar> renderer;
    private final ModelWitherMortar model = new ModelWitherMortar();

    public LayerWitherTurretMortarEnchant(RenderLivingBase<EntityWitherTurretMortar> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityWitherTurretMortar entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.enchantedIs())
            return;
        this.renderer.getMainModel().setModelAttributes(this.model);
        LayerArmorBase.renderEnchantedGlint(this.renderer, entity, this.model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
