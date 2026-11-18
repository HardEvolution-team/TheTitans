package com.electrifiedded.thetitans.sound;

import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class SoundHandler {
    public static List<SoundMoving> sounds = new ArrayList<SoundMoving>();
    @SideOnly(Side.CLIENT)
    public static void triggerAttatchedAudio(Entity entity, String name, float volume, float pitch, int lengthAudio) {
        Minecraft mc = Minecraft.getMinecraft();
        SoundMoving sound = new SoundMoving(entity, name, volume, pitch, lengthAudio);
        mc.getSoundHandler().playSound(sound);
        sounds.add(sound);
    }

    public static void whenPlayMob(String name, World world, Entity entityName, float volume, float pitch) {
        // resolve custom (thetitans) sound, else translate 1.7.10 vanilla names to 1.12.2; skip if unknown.
        SoundEvent event = TitanAudio.get(name);
        if (event == null)
            event = legacyVanilla(name);
        if (event == null)
            return;
        world.playSound(null, entityName.posX, entityName.posY, entityName.posZ,
                event, SoundCategory.HOSTILE, volume, pitch);
    }

    // maps the handful of 1.7.10 vanilla sound strings the mod still passes to their 1.12.2 SoundEvents.
    private static SoundEvent legacyVanilla(String name) {
        switch (name) {
            case "random.explode": return net.minecraft.init.SoundEvents.ENTITY_GENERIC_EXPLODE;
            case "random.fizz": return net.minecraft.init.SoundEvents.BLOCK_FIRE_EXTINGUISH;
            case "random.levelup": return net.minecraft.init.SoundEvents.ENTITY_PLAYER_LEVELUP;
            case "random.orb": return net.minecraft.init.SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
            case "mob.enderdragon.growl": return net.minecraft.init.SoundEvents.ENTITY_ENDERDRAGON_GROWL;
            case "mob.irongolem.throw": return net.minecraft.init.SoundEvents.ENTITY_IRONGOLEM_ATTACK;
            default:
                // full "domain:path" vanilla ids resolve straight from the registry
                if (name.indexOf(':') >= 0)
                    return net.minecraftforge.fml.common.registry.ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(name));
                return null;
        }
    }
}
