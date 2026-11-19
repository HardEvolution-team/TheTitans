package com.electrifiedded.thetitans.mixin;

import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.TitanPhysics;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import javax.annotation.Nullable;

/**
 * Titan physics fast paths (see TitanPhysics). HEAD injects instead of redirects so other mods that hook the same
 * methods keep working; non-titan entities take the untouched vanilla path.
 */
@Mixin(World.class)
public abstract class MixinWorld {
    @Shadow @Final public boolean isRemote;

    @Inject(method = "getCollisionBoxes(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private void thetitans$titanCollisionBoxes(@Nullable Entity entity, AxisAlignedBB aabb, CallbackInfoReturnable<List<AxisAlignedBB>> cir) {
        if (entity instanceof EntityTitan) {
            List<AxisAlignedBB> list = TitanPhysics.getCollisionBoxes((World)(Object)this, entity, aabb);
            if (list != null)
                cir.setReturnValue(list);
        }
    }

    @Inject(method = "isFlammableWithin", at = @At("HEAD"), cancellable = true)
    private void thetitans$skipTitanFireScan(AxisAlignedBB bb, CallbackInfoReturnable<Boolean> cir) {
        if (TitanPhysics.isTitanMoving(isRemote))
            cir.setReturnValue(false);
    }

    /**
     * PERF — Explosion exposure samples a ray grid of (2w+1)(2h+1)(2d+1) points over the entity box. A titan box
     * (24x96x24 and up) means ~460k block raytraces for ONE titan per explosion, which froze the server for a minute
     * in the bench. Boxes needing more than 512 rays are sampled on a capped 8x8x8 grid (same estimator, fewer
     * samples); normal-sized entities keep the exact vanilla path.
     */
    @Inject(method = "getBlockDensity", at = @At("HEAD"), cancellable = true)
    private void thetitans$cappedBlockDensity(net.minecraft.util.math.Vec3d vec, AxisAlignedBB bb, CallbackInfoReturnable<Float> cir) {
        Float f = TitanPhysics.cappedBlockDensity((World)(Object)this, vec, bb);
        if (f != null)
            cir.setReturnValue(f);
    }

    // Test harness (TitanProfiler / titanbench): per-class cost of every entity tick, any mod. Off = one boolean read.
    @org.spongepowered.asm.mixin.Unique private long thetitans$entT0;
    @org.spongepowered.asm.mixin.Unique private int thetitans$entDepth;

    @Inject(method = "updateEntityWithOptionalForce", at = @At("HEAD"))
    private void thetitans$profEntityHead(Entity entity, boolean force, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        if (com.electrifiedded.thetitans.util.perf.TitanProfiler.enabled && !isRemote && thetitans$entDepth++ == 0)
            thetitans$entT0 = System.nanoTime();
    }

    @Inject(method = "updateEntityWithOptionalForce", at = @At("RETURN"))
    private void thetitans$profEntityTail(Entity entity, boolean force, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        if (com.electrifiedded.thetitans.util.perf.TitanProfiler.enabled && !isRemote && thetitans$entDepth > 0 && --thetitans$entDepth == 0)
            com.electrifiedded.thetitans.util.perf.TitanProfiler.stop(entity.getClass(), com.electrifiedded.thetitans.util.perf.TitanProfiler.Sec.ENTITY, thetitans$entT0);
    }
}
