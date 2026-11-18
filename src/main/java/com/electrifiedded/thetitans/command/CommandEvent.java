package com.electrifiedded.thetitans.command;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntityEvilGolem;
import com.electrifiedded.thetitans.entity.mob.EntityUndeadOther;
import com.electrifiedded.thetitans.entity.mob.EntityWraith;
import com.electrifiedded.thetitans.handler.EventEntry;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class CommandEvent extends CommandBase {
    private static final String[] EVENTS = {"zombiehoard", "spiderhoard"};

    public String getName() {
        return "event";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender sender) {
        return "command." + TheTitans.MODID_V + ".event.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(sender);
        if (entityplayermp != null) {
            if (args.length > 0) {
                switch(args[0].toLowerCase()) {
                    case "zombiehoard": {
                            EventEntry.create(entityplayermp, entityplayermp.world, "zombie_hoard", 5.0D, 256.0D).appendBossesMini(EntityEvilGolem.class).appendCommonX(EntityZombie.class, EntitySkeleton.class, EntityUndeadOther.class).appendRare(EntityWraith.class).assignReachTicks(false, false, true, true).start(0, 0, 50);
                            break;
                        }

                    case "spiderhoard": {
                            EventEntry.create(entityplayermp, entityplayermp.world, "spider_hoard", 5.0D, 256.0D).appendCommonX(EntitySpider.class).appendRare(EntityWraith.class).start(0, 0, 200);
                            break;
                        }
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, EVENTS) : Collections.emptyList();
    }
}
