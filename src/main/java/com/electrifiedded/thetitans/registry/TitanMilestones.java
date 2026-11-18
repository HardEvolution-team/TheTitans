package com.electrifiedded.thetitans.registry;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

/**
 * 1.12.2 replacement for 1.7.10 {@code player.triggerAchievement(TitanTrophies.X)}.
 *
 * The original's achievementV page is ported as JSON advancements under
 * {@code assets/thetitans/advancements/} whose criteria are {@code minecraft:impossible}; this grants them from code
 * exactly where the original fired the achievementV. Ids with no JSON (the ~40 original fields that were declared but never
 * instantiated, i.e. null in 1.7.10) silently no-op, which matches the original where triggerAchievement(null) did nothing.
 */
public final class TitanMilestones {
    private TitanMilestones() {}

    public static void runGrant(EntityPlayer player, String id) {
        if (id == null || !(player instanceof EntityPlayerMP))
            return;
        EntityPlayerMP mp = (EntityPlayerMP) player;
        MinecraftServer server = mp.getServer();
        if (server == null)
            return;
        Advancement advancement = server.getAdvancementManager().getAdvancement(id.indexOf(':') >= 0 ? new ResourceLocation(id) : new ResourceLocation(TheTitans.MODID_V, id));
        if (advancement == null)
            return;
        AdvancementProgress progress = mp.getAdvancements().getProgress(advancement);
        if (progress.isDone())
            return;
        for (String criterion : progress.getRemaningCriteria())
            mp.getAdvancements().grantCriterion(advancement, criterion);
    }
}
