package com.electrifiedded.thetitans.client.render.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntityThinUndeadOther;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderThinUndeadOther extends RenderBiped<EntityThinUndeadOther> {
    public static final ResourceLocation[] SKIN_POOL_UNDEAD = new ResourceLocation[] {
        new ResourceLocation("textures/entity/skeleton/skeleton.png"),new ResourceLocation("textures/entity/skeleton/wither_skeleton.png"),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/ash_skeleton.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/frail.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/frozen_skeleton.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/mailed_skeleton.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton1.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton2.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton3.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton4.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton5.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton6.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton7.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton8.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton9.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/skeleton10.png"))
    };

    public RenderThinUndeadOther(RenderManager rm) {
        super(rm, new ModelSkeleton(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityThinUndeadOther mob) {
        int t = mob.fetchKindMob();
        if (t < 0 || t >= SKIN_POOL_UNDEAD.length)
            t = 0;
        return SKIN_POOL_UNDEAD[t];
    }
}
