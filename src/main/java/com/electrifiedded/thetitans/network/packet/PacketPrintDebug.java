package com.electrifiedded.thetitans.network.packet;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

public class PacketPrintDebug implements IMessage, IMessageHandler<PacketPrintDebug, IMessage> {
    public PacketPrintDebug() {
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
    }

    @Override
    public void toBytes(ByteBuf buffer) {
    }

    @Override
    public PacketPrintDebug onMessage(PacketPrintDebug message, MessageContext ctx) {
        if (ctx.side.isClient())
            return whenMessageClient(message, ctx);
        else
            return whenMessageServer(message, ctx);
    }

    @SideOnly(Side.CLIENT)
    protected PacketPrintDebug whenMessageClient(PacketPrintDebug message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            public void run() {
                TheTitans.logger.errorsPrint();
            }
        });
        return null;
    }

    protected PacketPrintDebug whenMessageServer(PacketPrintDebug message, MessageContext ctx) {
        return null;
    }
}
