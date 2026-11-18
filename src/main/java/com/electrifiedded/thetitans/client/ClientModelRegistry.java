package com.electrifiedded.thetitans.client;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.render.item.PerspectiveAwareModel;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = TheTitans.MODID_V, value = Side.CLIENT)
public class ClientModelRegistry {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        TitanGear.setup();
        TitanBlockSet.setup();
        for (Item item : TitanGear.fetchItemsRegistered())
            if (item != null && item.getRegistryName() != null)
            register(item);
        for (Block block : TitanBlockSet.fetchBlocksRegistered())
            if (block != null && block.getRegistryName() != null)
            register(Item.getItemFromBlock(block));
    }

    // swap the baked builtin/entity model for the katana/axe with a perspective-recording wrapper so
    // their TEISR can pick the right in-hand/ground/gui GL transform (1.7.10 rendered these huge in-hand).
    @SubscribeEvent
    public static void onModelBake(net.minecraftforge.client.event.ModelBakeEvent event) {
        perspectiveWrap(event, TitanGear.ultimaBlade);
        perspectiveWrap(event, TitanGear.optimaAxe);
        perspectiveWrap(event, TitanGear.omegafishOvum);
        perspectiveWrap(event, TitanGear.caveSpiderGiantOvum);
        perspectiveWrap(event, TitanGear.spiderGiantOvum);
        perspectiveWrap(event, TitanGear.spiderJockeyGiantOvum);
        perspectiveWrap(event, TitanGear.slimeGiantOvum);
        perspectiveWrap(event, TitanGear.magmaCubeGiantOvum);
        perspectiveWrap(event, TitanGear.skeletonGiantOvum);
        perspectiveWrap(event, TitanGear.zombieGiantOvum);
        perspectiveWrap(event, TitanGear.creeperGiantOvum);
        perspectiveWrap(event, TitanGear.chargedCreeperGiantOvum);
        perspectiveWrap(event, TitanGear.zombiePigmanGiantOvum);
        perspectiveWrap(event, TitanGear.blazeGiantOvum);
        perspectiveWrap(event, TitanGear.witherJockeyGiantOvum);
        perspectiveWrap(event, TitanGear.witherSkeletonGiantOvum);
        perspectiveWrap(event, TitanGear.snowGolemGiantOvum);
        perspectiveWrap(event, TitanGear.ultimaIronGolemGiantOvum);
        perspectiveWrap(event, TitanGear.gargoyleKingOvum);
        perspectiveWrap(event, TitanGear.ghastGiantOvum);
        perspectiveWrap(event, TitanGear.enderColossusOvum);
        perspectiveWrap(event, TitanGear.witherzillaOvum);
        perspectiveWrap(event, TitanGear.executorDragonOvum);
    }

    private static void perspectiveWrap(net.minecraftforge.client.event.ModelBakeEvent event, Item item) {
        if (item == null || item.getRegistryName() == null)
            return;
        ModelResourceLocation mrl = new ModelResourceLocation(item.getRegistryName(), "inventory");
        net.minecraft.client.renderer.block.model.IBakedModel base = event.getModelRegistry().getObject(mrl);
        if (base != null && !(base instanceof com.electrifiedded.thetitans.client.render.item.PerspectiveAwareModel))
            event.getModelRegistry().putObject(mrl, new com.electrifiedded.thetitans.client.render.item.PerspectiveAwareModel(base));
    }

    private static void register(Item item) {
        if (item == null || item.getRegistryName() == null)
            return;
        ResourceLocation rl = item.getRegistryName();
        final ModelResourceLocation inv = new ModelResourceLocation(rl, "inventory");
        if (item.getHasSubtypes()) {
            ModelBakery.registerItemVariants(item, inv);
            ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
                public ModelResourceLocation getModelLocation(ItemStack stack) { return inv; }
            });
            return;
        }
        ModelLoader.setCustomModelResourceLocation(item, 0, inv);
    }
}
