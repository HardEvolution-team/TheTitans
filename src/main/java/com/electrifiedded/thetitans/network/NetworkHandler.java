package com.electrifiedded.thetitans.network;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.network.packet.PacketPlayerAlive;
import com.electrifiedded.thetitans.network.packet.PacketPrintDebug;
import com.electrifiedded.thetitans.network.packet.PacketRemoveBar;
import com.electrifiedded.thetitans.network.packet.PacketWorldData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetworkHandler {
    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(TheTitans.MODID_V);
    private static int ID = -1;

    public static void preInit() {
        register(PacketRemoveBar.class);
        register(PacketPrintDebug.class);
        register(PacketPlayerAlive.class);
        register(PacketWorldData.class, Side.CLIENT);
    }

    public static boolean dispatchPacketClient(IMessage message, Object... targets) {
        if (message == null)
            return false;
        if (targets.length > 0 && targets[0] != null) {
            if (targets[0] instanceof Integer)
                instance.sendToDimension(message, (int)targets[0]);
            else if (targets[0] instanceof EntityPlayerMP)
                instance.sendTo(message, (EntityPlayerMP)targets[0]);
            else
                return false;
        }

        else
            instance.sendToAll(message);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static boolean dispatchPacketServer(IMessage message) {
        if (message == null)
            return false;
        instance.sendToServer(message);
        return true;
    }

    private static <T extends IMessage & IMessageHandler<T, IMessage>> void register(Class<T> packetXHandler) {
        register(packetXHandler, packetXHandler, (Side)null);
    }

    private static <T extends IMessage & IMessageHandler<T, IMessage>> void register(Class<T> packetXHandler, Side... side) {
        register(packetXHandler, packetXHandler, side);
    }

    private static <T extends IMessage> void register(Class<? extends IMessageHandler<T, IMessage>> handler, Class<T> packet, Side... side) {
        if (side[0] == null) {
            instance.registerMessage(handler, packet, ID++, Side.CLIENT);
            instance.registerMessage(handler, packet, ID++, Side.SERVER);
            return;
        }

        instance.registerMessage(handler, packet, ID++, side[0]);
    }
}
