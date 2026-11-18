package com.electrifiedded.thehippomaster.AnimationAPI.packet;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;

public class PacketAnim implements IMessage {
    private byte keyMotion;
    private int entityID;

    public PacketAnim() {
    }

    public PacketAnim(byte anim, int entity) {
        keyMotion = anim;
        entityID = entity;
    }

    @Override
    public void toBytes( ByteBuf buffer) {
        buffer.writeByte(keyMotion);
        buffer.writeInt(entityID);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        keyMotion = buffer.readByte();
        entityID = buffer.readInt();
    }

    public static class Handler implements IMessageHandler<PacketAnim, IMessage> {
        @Override
        public IMessage onMessage(final PacketAnim packet, MessageContext ctx) {
            // Client-bound packet; hop to the client thread before touching the world.
            net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    World world = AnimationAPI.proxy.getWorldClient();
                    if(world == null) return;
                    Object obj = world.getEntityByID(packet.entityID);
                    if(obj instanceof IAnimatedEntity && packet.keyMotion != -1) {
                        IAnimatedEntity entity = (IAnimatedEntity)obj;
                        entity.setAnimID(packet.keyMotion);
                        if(packet.keyMotion == 0) entity.setAnimTick(0);
                    }
                }
            });
            return null;
        }
    }
}
