package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHarcadiumArrow extends RenderArrow<EntityHarcadiumArrow> {
    private static final ResourceLocation texturesArrow = new ResourceLocation(TheTitans.getTextures("textures/entities", "harcadium_arrow_entity.png"));
    private static final ResourceLocation texturesArrow2 = new ResourceLocation(TheTitans.getTextures("textures/entities", "void_arrow_entity.png"));
    private static final ResourceLocation texturesArrow3 = new ResourceLocation("textures/entity/arrow.png");

    public RenderHarcadiumArrow(RenderManager rm) {
        super(rm);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHarcadiumArrow entity) {
        switch (entity.fetchKindArrow()) {
            case 1:
                return texturesArrow2;
            case 2:
                return texturesArrow3;
            default:
                return texturesArrow;
        }
    }
}
