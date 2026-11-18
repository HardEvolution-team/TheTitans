package com.electrifiedded.thetitans.command;

import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.core.TheCore;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;

import java.util.List;
import javax.annotation.Nullable;

public class CommandNewDifficulty extends CommandBase {
    public String getName() {
        return "difficulty";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender sender) {
        return "commands.difficulty.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            EnumDifficulty enumdifficulty = getDifficulty(sender, args);
            server.setDifficultyForAllWorlds(enumdifficulty);
            notifyCommandListener(sender, this, "commands.difficulty.success", new Object[] {
                new TextComponentTranslation(enumdifficulty.getTranslationKey(), new Object[0])
            }
            );
        }

        else {
            throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
        }
    }

    protected EnumDifficulty getDifficulty(ICommandSender sender, String[] arguments) throws CommandException {
        if (TitanConfig.coreDifficultyUse)
            switch(String.join(" ", arguments).toLowerCase()) {
            case "p": return EnumDifficulty.PEACEFUL;
            case "peaceful": return EnumDifficulty.PEACEFUL;
            case "e": return EnumDifficulty.EASY;
            case "easy": return EnumDifficulty.EASY;
            case "n": return EnumDifficulty.NORMAL;
            case "normal": return EnumDifficulty.NORMAL;
            case "h": return EnumDifficulty.HARD;
            case "hard": return EnumDifficulty.HARD;
            case "v": return EnumDifficulty.HARD;
                // TODO mixin — TheCore.VERYHARD_V/EXPERT_V/NIGHTMARE_V/IMPOSSIBLE are coremod-added EnumDifficulty values
            case "veryhard": return TheCore.VERYHARD_V;
            case "expert": return TheCore.EXPERT_V;
            case "nightmare": return TheCore.NIGHTMARE_V;
            case "i": return TheCore.IMPOSSIBLE;
            case "impossible": return TheCore.IMPOSSIBLE;
            default: {
                    return EnumDifficulty.values()[parseInt(String.join(" ", arguments).toLowerCase(), 0, EnumDifficulty.values().length - 1)];
                }
        }

        else
            switch(String.join(" ", arguments).toLowerCase()) {
            case "p": return EnumDifficulty.EASY;
            case "peaceful": return EnumDifficulty.EASY;
            case "e": return EnumDifficulty.EASY;
            case "easy": return EnumDifficulty.EASY;
            case "n": return EnumDifficulty.NORMAL;
            case "normal": return EnumDifficulty.NORMAL;
            case "h": return EnumDifficulty.HARD;
            case "hard": return EnumDifficulty.HARD;
            default: return EnumDifficulty.byId(parseInt(String.join(" ", arguments).toLowerCase(), 0, EnumDifficulty.values().length - 1));
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {
            "peaceful", "easy", "normal", "hard", "nightmare"
        }
        ): null;
    }
}
