package com.electrifiedded.thetitans.command;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.network.NetworkHandler;
import com.electrifiedded.thetitans.network.packet.PacketPrintDebug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class CommandDebug extends CommandBase {
    public String getName() {
        return "debugger";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender sender) {
        return "command." + TheTitans.MODID_V + ".debugger.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(sender);
        if (entityplayermp != null) {
            if (args.length > 0) {
                switch(args[0].toLowerCase()) {
                    case "print": {
                            NetworkHandler.dispatchPacketClient(new PacketPrintDebug(), entityplayermp);
                            break;
                        }

                    default: TheTitans.reflect.view(args);
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "print") : Collections.emptyList();
    }
}
