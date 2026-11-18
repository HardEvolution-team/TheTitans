package com.electrifiedded.thetitans.network.packet;

import com.electrifiedded.thetitans.handler.EventStore;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

public class PacketWorldData implements IMessage, IMessageHandler<PacketWorldData, IMessage> {
    protected String name;
    protected boolean truthV;

    public PacketWorldData() {}

    public PacketWorldData(String name, boolean truthV) {
        this.name = name;
        this.truthV = truthV;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        name = ByteBufUtils.readUTF8String(buffer);
        truthV = buffer.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, name);
        buffer.writeBoolean(truthV);
    }

    @Override
    public PacketWorldData onMessage(PacketWorldData message, MessageContext ctx) {
        if (ctx.side.isClient())
            return whenMessageClient(message, ctx);
        else
            return whenMessageServer(message, ctx);
    }

    @SideOnly(Side.CLIENT)
    protected PacketWorldData whenMessageClient(final PacketWorldData message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            public void run() {
                if (Minecraft.getMinecraft().world != null)
                    EventStore.setBool(Minecraft.getMinecraft().world, message.name, message.truthV);
            }
        });
        return null;
    }

    protected PacketWorldData whenMessageServer(PacketWorldData message, MessageContext ctx) {
        return null;
    }
}
