package com.electrifiedded.thetitans.mixin;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/**
 * 1.12.2 hard-caps generic.maxHealth at 1024 (and attackDamage / followRange at 2048). Titans need up to ~1e28 HP,
 * titan armor pushes players far past 1024, and 1.7.10 had no such cap. Lifting the cap at construction lets every
 * entity keep its real HP in plain vanilla health (getHealth/getMaxHealth) - no side pools, no reflection hacks.
 */
@Mixin(RangedAttribute.class)
public abstract class MixinRangedAttribute {
    @Shadow @Final @Mutable private double maximumValue;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void thetitans$liftCap(@Nullable IAttribute parent, String name, double defaultValue, double minimumValue, double maximumValue, CallbackInfo ci) {
        if ("generic.maxHealth".equals(name) || "generic.attackDamage".equals(name) || "generic.followRange".equals(name))
            this.maximumValue = Double.MAX_VALUE;
    }
}
