package com.electrifiedded.thetitans.network.packet;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

public class PacketRemoveBar implements IMessage, IMessageHandler<PacketRemoveBar, IMessage> {
    private String uuid;

    public PacketRemoveBar() {
    }

    public PacketRemoveBar(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        uuid = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, uuid);
    }

    @Override
    public PacketRemoveBar onMessage(PacketRemoveBar message, MessageContext ctx) {
        if (ctx.side.isClient())
            return whenMessageClient(message, ctx);
        else
            return whenMessageServer(message, ctx);
    }

    @SideOnly(Side.CLIENT)
    protected PacketRemoveBar whenMessageClient(final PacketRemoveBar message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            public void run() {
                TheTitans.debug("Removing bar: " + message.uuid);
                ClientProxy.guiIngameZ.removeQueue(message.uuid);
            }
        });
        return null;
    }

    protected PacketRemoveBar whenMessageServer(PacketRemoveBar message, MessageContext ctx) {
        return null;
    }
}
