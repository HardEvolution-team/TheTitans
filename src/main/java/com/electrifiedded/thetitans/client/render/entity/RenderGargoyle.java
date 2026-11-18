package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelGargoyle;
import com.electrifiedded.thetitans.client.render.layer.LayerGargoyleBeam;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyle;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGargoyle extends RenderLiving<EntityGargoyle> {
    // golden-gargoyle (type 3) perch beam + invisibility depth-mask restored via LayerGargoyleBeam.
    private static final ResourceLocation gargoyleTexturesStone = new ResourceLocation(TheTitans.getTextures("textures/entities", "gargoyle1.png"));
    private static final ResourceLocation gargoyleTexturesSandstone = new ResourceLocation(TheTitans.getTextures("textures/entities", "gargoyle2.png"));
    private static final ResourceLocation gargoyleTexturesObsidian = new ResourceLocation(TheTitans.getTextures("textures/entities", "gargoyle3.png"));
    private static final ResourceLocation gargoyleTexturesGolden = new ResourceLocation(TheTitans.getTextures("textures/entities", "gargoyle4.png"));
    private static final ResourceLocation gargoyleTexturesIron = new ResourceLocation(TheTitans.getTextures("textures/entities", "gargoyle5.png"));
    private static final ResourceLocation gargoyleTexturesEndstone = new ResourceLocation(TheTitans.getTextures("textures/entities", "gargoyle6.png"));
    private static final ResourceLocation gargoyleTexturesNethratic = new ResourceLocation(TheTitans.getTextures("textures/entities", "gargoyle7.png"));

    public RenderGargoyle(RenderManager rm) {
        super(rm, new ModelGargoyle(), 0.8F);
        addLayer(new LayerGargoyleBeam(this));
    }

    protected ResourceLocation getEntityTexture(EntityGargoyle entity) {
        switch (entity.fetchKindGargoyle()) {
            case 0:
            default:
                return gargoyleTexturesStone;
            case 1:
                return gargoyleTexturesSandstone;
            case 2:
                return gargoyleTexturesObsidian;
            case 3:
                return gargoyleTexturesGolden;
            case 4:
                return gargoyleTexturesIron;
            case 5:
                return gargoyleTexturesEndstone;
            case 6:
                return gargoyleTexturesNethratic;
        }
    }

    protected void applyRotations(EntityGargoyle entity, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
        if (!entity.onGround || entity.fetchAggressive()) {
            GL11.glRotatef(75F, -1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0F, 1.0F);
        }

        if (entity.fetchKindGargoyle() == 1) {
            GL11.glScalef(0.75F, 1F, 0.75F);
        }

        if (entity.fetchKindGargoyle() == 2) {
            GL11.glScalef(1.25F, 1F, 1.25F);
        }

        if (entity.fetchKindGargoyle() == 3) {
            GL11.glScalef(1.1F, 1F, 1.1F);
        }

        if (entity.fetchKindGargoyle() == 4) {
            GL11.glScalef(1.2F, 1F, 1.2F);
        }

        if (entity.fetchKindGargoyle() == 5) {
            GL11.glScalef(1.1F, 1.1F, 1.1F);
        }

        if (entity.fetchKindGargoyle() == 6) {
            GL11.glScalef(1.05F, 0.95F, 1.05F);
        }
    }
}
