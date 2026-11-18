package com.electrifiedded.thetitans.sound;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SoundMoving extends MovingSound {
    private Entity entity;
    protected int lengthAudio;
    public int ticksExisted;

    public SoundMoving(Entity entity, String sound, float volume, float pitch, int lengthAudio) {
        // resolve the registered (lowercased) SoundEvent so looping titan sounds bind to sounds.json.
        super(resolve(sound), SoundCategory.HOSTILE);
        this.entity = entity;
        this.volume = volume;
        this.lengthAudio = lengthAudio;
        this.repeat = true;
        this.pitch = pitch;
        this.xPosF = (float) entity.posX;
        this.yPosF = (float) entity.posY;
        this.zPosF = (float) entity.posZ;
    }

    private static SoundEvent resolve(String sound) {
        SoundEvent event = TitanAudio.get(sound);
        if (event != null)
            return event;
        String s = sound.toLowerCase(java.util.Locale.ROOT);
        return new SoundEvent(s.indexOf(':') >= 0 ? new ResourceLocation(s) : new ResourceLocation(TheTitans.MODID_V, s));
    }

    public void update() {
        if (lengthAudio < ticksExisted)
            this.donePlaying = true;
        this.xPosF = (float) entity.posX;
        this.yPosF = (float) entity.posY;
        this.zPosF = (float) entity.posZ;
        ticksExisted ++;
    }
}
