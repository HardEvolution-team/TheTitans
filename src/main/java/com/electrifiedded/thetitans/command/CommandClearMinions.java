package com.electrifiedded.thetitans.command;

import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.entity.minion.ITemplar;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.*;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

public class CommandClearMinions extends CommandBase {
    public String getName() {
        return "clearMinions";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getUsage(ICommandSender sender) {
        return "commands.killminions.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(sender);
        sender.sendMessage(new TextComponentTranslation("commands.killminions.processing", new Object[0]));
        List<?> list = entityplayermp.world.loadedEntityList;
        if ((list != null) && (!list.isEmpty())) {
            for (int i1 = 0; i1 < list.size(); i1++) {
                Entity entity = (Entity)list.get(i1);
                if ((entity != null) && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit) && (!entity.isEntityAlive() || entity.getClass() == (Class<?>)EntityList.getClass(new ResourceLocation("MutantCreatures.SkeletonPart")) || entity.getClass() == (Class<?>)EntityList.getClass(new ResourceLocation("MutantCreatures.SpiderPig")) || entity.getClass() == (Class<?>)EntityList.getClass(new ResourceLocation("MutantCreatures.CreeperMinionEgg")) || entity instanceof IMinion || entity instanceof ITemplar || entity instanceof EntityGhast || entity instanceof EntitySlime || entity instanceof EntityFallingBlock || entity instanceof EntityWaterMob || entity instanceof EntityMob || (entity instanceof EntityAnimal && !(entity instanceof EntityWolf)) || entity instanceof EntityAmbientCreature || entity.getClass().getCanonicalName().startsWith("danger.orespawn") || entity instanceof EntityFireball || entity instanceof EntityTNTPrimed || entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof IProjectile)) {
                    entity.setDead();
                    entity.isDead = true;
                }

                if (entity instanceof EntityItem) {
                    entity.setDead();
                }

                if (entity != null && entity.isEntityAlive() && entity instanceof EntityTitanSpirit) {
                    ((EntityTitanSpirit)entity).assignHuntingVessel(false);
                }
            }
        }

        sender.sendMessage(new TextComponentTranslation("commands.killminions.success", new Object[0]));
    }
}
