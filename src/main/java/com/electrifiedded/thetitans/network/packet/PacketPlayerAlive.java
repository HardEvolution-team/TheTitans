package com.electrifiedded.thetitans.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

public class PacketPlayerAlive implements IMessage, IMessageHandler<PacketPlayerAlive, IMessage> {
    protected String username;

    public PacketPlayerAlive() {
    }

    public PacketPlayerAlive(String username) {
        this.username = username;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        username = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, username);
    }

    @Override
    public PacketPlayerAlive onMessage(PacketPlayerAlive message, MessageContext ctx) {
        if (ctx.side.isClient())
            return whenMessageClient(message, ctx);
        else
            return whenMessageServer(message, ctx);
    }

    @SideOnly(Side.CLIENT)
    protected PacketPlayerAlive whenMessageClient(final PacketPlayerAlive message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            public void run() {
                if (message.username.equals(Minecraft.getMinecraft().player.getName())) {
                    Minecraft.getMinecraft().player.isDead = false;
                    Minecraft.getMinecraft().player.deathTime = -1;
                    // BUGFIX "alive but dead" — reviving via the titan-armor HP pool cleared the death
                    // flags but left the GuiGameOver death screen open, so the player could move yet stayed stuck
                    // on the death GUI. Dismiss it here when the local player is revived.
                    if (Minecraft.getMinecraft().currentScreen instanceof net.minecraft.client.gui.GuiGameOver)
                        Minecraft.getMinecraft().displayGuiScreen(null);
                    return;
                }

                for (Object object : Minecraft.getMinecraft().world.playerEntities)
                    if (object != null && object instanceof EntityPlayer)
                    if (((EntityPlayer)object).getName().equals(message.username)) {
                    ((EntityPlayer)object).isDead = false;
                    ((EntityPlayer)object).deathTime = -1;
                    break;
                }
            }
        });
        return null;
    }

    protected PacketPlayerAlive whenMessageServer(PacketPlayerAlive message, MessageContext ctx) {
        return null;
    }
}
