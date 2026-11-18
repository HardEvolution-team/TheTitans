package com.electrifiedded.thetitans.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class CommandNewKill extends CommandBase {
    private static final String[] SELECTORS_V = {"all", "players", "mobs", "hostile", "passive", "neutral", "items"};

    public String getName() {
        return "kill";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getUsage(ICommandSender sender) {
        return "commands.kill.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(sender);
        if (args.length > 0) {
            int killed = 0;
            Entity entity;
            boolean forced = false;
            if (args.length > 1 && args[args.length - 1].equals("true")) {
                forced = true;
                String[] arg = new String[args.length - 1];
                for (int i = 0; i < args.length - 1; i++)
                    arg[i] = args[i];
                args = arg;
            }

            List<?> list = entityplayermp.world.loadedEntityList;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    entity = (Entity)list.get(i);
                    switch(String.join(" ", args)) {
                        case "all": {
                                if (entity.isEntityAlive() && !(entity instanceof EntityPlayerMP)) {
                                    kill(entity, forced); killed ++;
                                }

                                break;
                            }

                        case "players": {
                                if (entity.isEntityAlive() && entity instanceof EntityPlayerMP) {
                                    kill(entity, forced); killed ++;
                                }

                                break;
                            }

                        case "mobs": ;
                            {
                                if (entity.isEntityAlive() && !(entity instanceof EntityPlayerMP) && entity instanceof EntityLivingBase) {
                                    kill(entity, forced); killed ++;
                                }

                                break;
                            }

                        case "hostile": ;
                            {
                                if (entity.isEntityAlive() && !(entity instanceof EntityPlayerMP) && entity instanceof EntityMob) {
                                    kill(entity, forced); killed ++;
                                }

                                break;
                            }

                        case "passive": ;
                            {
                                if (entity.isEntityAlive() && !(entity instanceof EntityPlayerMP) && entity instanceof EntityAnimal) {
                                    kill(entity, forced); killed ++;
                                }

                                break;
                            }

                        case "neutral": ;
                            {
                                if (entity.isEntityAlive() && !(entity instanceof EntityPlayerMP) && (entity instanceof EntityTameable || entity instanceof EntityGolem)) {
                                    kill(entity, forced); killed ++;
                                }

                                break;
                            }

                        case "items": ;
                            {
                                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) {
                                    kill(entity, true); killed ++;
                                }

                                break;
                            }

                        default: {
                                if (entity.isEntityAlive() && entity.getName().equals(String.join(" ", args))) {
                                    kill(entity, forced); killed ++;
                                }

                                break;
                            }
                    }
                }

                if (killed < 1)
                    sender.sendMessage(new TextComponentTranslation("commands.kill.fail.b", new Object[0]));
                else
                    sender.sendMessage(new TextComponentTranslation("commands.kill.success", new Object[] {
                    killed
                }
                ));
            }

            else
                sender.sendMessage(new TextComponentTranslation("commands.kill.fail.a", new Object[0]));
        }

        else
            sender.sendMessage(new TextComponentTranslation("commands.kill.usage", new Object[0]));
    }

    private void kill(Entity entity, boolean forced) {
        if (forced)
            entity.isDead = true;
        else
            entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }

    /** Tab-completion: arg1 = selector or player name; a trailing "true" forces the kill. */
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            List<String> options = new java.util.ArrayList<String>(java.util.Arrays.asList(SELECTORS_V));
            options.addAll(java.util.Arrays.asList(server.getOnlinePlayerNames()));
            return getListOfStringsMatchingLastWord(args, options);
        }

        return args.length == 2 ? getListOfStringsMatchingLastWord(args, "true") : Collections.emptyList();
    }
}
