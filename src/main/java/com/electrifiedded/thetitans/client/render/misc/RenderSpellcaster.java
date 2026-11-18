package com.electrifiedded.thetitans.client.render.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelSpellcaster;
import com.electrifiedded.thetitans.entity.mob.EntityMagicUser;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpellcaster extends RenderBiped<EntityMagicUser> {
    private ModelSpellcaster meshSpellcaster;
    public static final ResourceLocation[] SKIN_POOL_SPELLCASTER = new ResourceLocation[] {
        new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_novice.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_novice.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_novice.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_fire.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_ice.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_earth.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_lightning.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_void.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_sniper.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_healer.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_barrage.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_aether.png")),new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/magic_archmage.png"))
    };

    public RenderSpellcaster(RenderManager renderManager) {
        super(renderManager, new ModelSpellcaster(), 0.5F);
        this.meshSpellcaster = (ModelSpellcaster)super.mainModel;
    }

    protected ResourceLocation getEntityTexture(EntityMagicUser mob) {
        return SKIN_POOL_SPELLCASTER[mob.fetchKindMob()];
    }

    public void doRender(EntityMagicUser entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.meshSpellcaster.spellCasting = entity.isSpellCasting();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
