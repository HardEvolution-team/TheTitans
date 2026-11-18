package com.electrifiedded.thehippomaster.AnimationAPI;

import com.electrifiedded.thehippomaster.AnimationAPI.packet.PacketAnim;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "titansanimationapi", name = "TitansAnimationAPI", version = "1.2.4")
public class AnimationAPI {
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("AnimAPI");
        wrapper.registerMessage(PacketAnim.Handler.class, PacketAnim.class, 0, Side.CLIENT);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.initTimer();
    }

    public static boolean isClient() {
        return FMLCommonHandler.instance().getSide().isClient();
    }

    public static boolean isEffectiveClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static void sendAnimPacket(IAnimatedEntity entity, int keyMotion) {
        if(isEffectiveClient()) return;
        // PERF — titans keep their anim id in synced DataManager entries, so re-sending the id they already
        // have is pure network spam (dormant titans did it every tick, stunned ones too). And only players tracking
        // the entity can resolve its id, so don't broadcast to the whole server.
        if(entity instanceof com.electrifiedded.thetitans.entity.titan.EntityTitan && entity.getAnimID() == keyMotion) return;
        entity.setAnimID(keyMotion);
        wrapper.sendToAllTracking(new PacketAnim((byte)keyMotion, ((Entity)entity).getEntityId()), (Entity)entity);
    }

    @Instance("AnimationAPI")
    public static AnimationAPI instance;
    @SidedProxy(clientSide="com.electrifiedded.thehippomaster.AnimationAPI.client.ClientProxy", serverSide="com.electrifiedded.thehippomaster.AnimationAPI.CommonProxy")
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper wrapper;
    public static final String[] clockF;
    static {
        clockF = new String[] {
            "field_71428_T", "S", "timer"
        };
    }
}
