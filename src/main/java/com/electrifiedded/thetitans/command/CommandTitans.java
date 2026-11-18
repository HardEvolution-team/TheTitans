package com.electrifiedded.thetitans.command;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.handler.EventStore;
import com.electrifiedded.thetitans.network.NetworkHandler;
import com.electrifiedded.thetitans.network.packet.PacketWorldData;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class CommandTitans extends CommandBase {
    private static final String[] SUBCOMMANDS_V = {"progress"};
    /** Progression flags (EventStore bool keys) that gate titan spawning/waking — see RealmHandler/EventStore.
     *  `/titans progress PreTitanComplete true` wakes the dormant statue titans for testing. */
    private static final String[] KEYS_PROGRESS = {
        "PreTitanComplete", "TitanComplete", "PostTitanComplete",
                "PostFirstTitan", "FirstLesserTitanDefeated", "FirstAverageTitanDefeated", "FirstGreaterTitanDefeated",
                "PostWither", "PostWitherzilla", "PostWitherzillaOmega",
                "PostDragon", "PostExecutorDragon", "PostExecutorDragonOmega",
                "PostLord", "ArbitratorKilled", "GrandmasterKilled", "TemplarKilled"
    };

    public String getName() {
        return "titans";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender sender) {
        return "commands." + TheTitans.MODID_V + ".titans.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        if (player == null)
            return;

        World world = player.world;

        if (args.length > 0)
            switch(args[0].toLowerCase()) {
            case "progress": {
                    try {
                        if (args.length > 2)
                            progress(player, args[1], Boolean.parseBoolean(args[2]));
                        else
                            player.sendMessage(new TextComponentString(TheTitans.translate("commands", "titans.usage")));

                        break;
                    } catch (Exception e) {
                        player.sendMessage(new TextComponentString(TheTitans.translate("commands", "titans.failed", args[1])));
                    }
                }

            default:
                player.sendMessage(new TextComponentString(TheTitans.translate("commands", "titans.usage")));
        } else
                player.sendMessage(new TextComponentString(TheTitans.translate("commands", "titans.usage")));
    }

    private void progress(EntityPlayerMP player, String name, boolean flag) {
        EventStore.setBool(player.world, name, flag);
        NetworkHandler.dispatchPacketClient(new PacketWorldData(name, flag));
        player.sendMessage(new TextComponentString(TheTitans.translate("commands", "titans.success", name, flag)));
    }

    /** Tab-completion: arg1 = subcommand, arg2 = progression key, arg3 = true|false. */
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        switch (args.length) {
            case 1:
                return getListOfStringsMatchingLastWord(args, SUBCOMMANDS_V);
            case 2:
                if (args[0].equalsIgnoreCase("progress"))
                    return getListOfStringsMatchingLastWord(args, KEYS_PROGRESS);
                break;
            case 3:
                if (args[0].equalsIgnoreCase("progress"))
                    return getListOfStringsMatchingLastWord(args, "true", "false");
                break;
        }

        return Collections.emptyList();
    }
}
