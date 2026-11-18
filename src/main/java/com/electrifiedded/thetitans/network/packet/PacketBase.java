package com.electrifiedded.thetitans.network.packet;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

public class PacketBase implements IMessage, IMessageHandler<PacketBase, IMessage> {
    protected int test;

    public PacketBase() {
    }

    public PacketBase(int value) {
        test = value;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        test = buffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(test);
    }

    @Override
    public PacketBase onMessage(PacketBase message, MessageContext ctx) {
        if (ctx.side.isClient())
            return whenMessageClient(message, ctx);
        else
            return whenMessageServer(message, ctx);
    }

    @SideOnly(Side.CLIENT)
    protected PacketBase whenMessageClient(PacketBase message, MessageContext ctx) {
        return null;
    }

    protected PacketBase whenMessageServer(PacketBase message, MessageContext ctx) {
        return null;
    }
}
