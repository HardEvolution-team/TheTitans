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
}
