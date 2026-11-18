package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.item.armor.ItemTitanArmor;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PulseHandler {
    // Maps the legacy 1.7.10 armor slot indices (1..4 = feet, legs, chest, head) to 1.12.2 EntityEquipmentSlot.
    private static final EntityEquipmentSlot[] SLOTS_ARMOUR = {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};
    private static Minecraft mc;
    private int beatsClient;
    private int beatsServer;

    public PulseHandler(Minecraft minecraft) {
        mc = minecraft;
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {
        for (int i = 0; i < EventEntry.instances.size(); i++)
            EventEntry.instances.get(i).onUpdate(event.side);
        if (EventStore.deleteWorld != null)
            EventStore.queueRealmDelete("thetitans.retry.b");
        net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.common.FMLCommonHandler.instance().getMinecraftServerInstance();
        if (event.phase == TickEvent.Phase.END && server != null && server.getTickCounter() % 100 == 0)
            TitanAscension.tickServer(server);
        beatsServer++;
    }

    @SubscribeEvent
    public void whenChangedEventConfiguration(OnConfigChangedEvent event)

            {
        if(event.getModID().equalsIgnoreCase(TheTitans.MODID_V)) {
            TheTitans.config.save();
            TitanConfig.load();
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(ClientTickEvent event) {
        for (int i = 0; i < EventEntry.instances.size(); i++)
            EventEntry.instances.get(i).onUpdate(event.side);
        if (mc.world != null && mc.player != null && !mc.isGamePaused())
            for (EntityLivingBase entity : mc.world.getEntitiesWithinAABB(EntityLivingBase.class, mc.player.getEntityBoundingBox().grow(32.0D))) {
            for (int i = 1; i < 5; i++) {
                ItemStack stack = entity.getItemStackFromSlot(SLOTS_ARMOUR[i - 1]);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemTitanArmor) {
                    ((ItemTitanArmor)stack.getItem()).whenTickAudio(mc.world, entity, stack);
                    break;
                }
            }
        }

        if (!mc.isGamePaused())
            for (int i = 0; i < SoundHandler.sounds.size(); i++) {
            SoundHandler.sounds.get(i).update();
            if (SoundHandler.sounds.get(i).isDonePlaying()) {
                SoundHandler.sounds.remove(i);
                i--;
            }
        }

        if (!mc.isIntegratedServerRunning() && EventStore.deleteWorld != null)
            MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(EventStore.deleteWorld));
        beatsClient++;
    }
}
