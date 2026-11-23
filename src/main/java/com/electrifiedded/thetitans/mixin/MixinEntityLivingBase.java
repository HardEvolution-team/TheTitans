package com.electrifiedded.thetitans.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla bug made fatal by titan armour: equipment attribute modifiers are stripped before "Attributes" is saved and
 * only re-applied on the first tick, so readEntityFromNBT clamps "Health" to the bonus-less max (20). Every relog /
 * chunk reload reset titan-armour players AND mobs to default HP. Keep the saved value and restore it once the armour
 * (or any other late) max-health bonus is back.
 */
@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {
    @Unique private float thetitans$hpSaved = -1.0F;
    @Unique private float thetitans$hpAtLoad;
    @Unique private int thetitans$hpWait;

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void thetitans$keepHealth(NBTTagCompound compound, CallbackInfo ci) {
        EntityLivingBase self = (EntityLivingBase)(Object)this;
        float saved = compound.getFloat("Health");
        thetitans$hpSaved = -1.0F;
        if (saved > self.getHealth() && !Float.isNaN(saved) && !Float.isInfinite(saved) && self.getHealth() > 0.0F) {
            thetitans$hpSaved = saved;
            thetitans$hpAtLoad = self.getHealth();
            thetitans$hpWait = 0;
        }
    }

    @Inject(method = "onUpdate", at = @At("TAIL"))
    private void thetitans$restoreHealth(CallbackInfo ci) {
        if (thetitans$hpSaved <= 0.0F)
            return;
        EntityLivingBase self = (EntityLivingBase)(Object)this;
        if (self.world.isRemote || self.getHealth() <= 0.0F || self.isDead) {
            if (!self.world.isRemote)
                thetitans$hpSaved = -1.0F;
            return;
        }
        float max = self.getMaxHealth();
        // damage taken while waiting still counts
        float lost = Math.max(0.0F, thetitans$hpAtLoad - self.getHealth());
        float target = Math.min(thetitans$hpSaved, max) - lost;
        if (target > self.getHealth())
            self.setHealth(target);
        thetitans$hpAtLoad = self.getHealth();
        // XP-level health bonus (RealmHandler) is applied every 100 ticks, so wait a bit longer than that.
        if (max >= thetitans$hpSaved || ++thetitans$hpWait > 120)
            thetitans$hpSaved = -1.0F;
    }
}
