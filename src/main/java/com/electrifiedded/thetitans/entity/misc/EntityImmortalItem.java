package com.electrifiedded.thetitans.entity.misc;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityImmortalItem extends EntityItem {
    public EntityImmortalItem(World world, Entity original, ItemStack stack) {
        this(world, original.posX, original.posY, original.posZ, stack);
        this.setPickupDelay(20);
        this.motionX = original.motionX;
        this.motionY = original.motionY;
        this.motionZ = original.motionZ;
        setItem(stack);
        this.ignoreFrustumCheck = true;
    }

    public EntityImmortalItem(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z);
        setItem(stack);
    }

    public EntityImmortalItem(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.isImmuneToFire = true;
    }

    public EntityImmortalItem(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    protected void dealFireDamage(int damage) {
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float damage) {
        EntityPlayer player = this.world.getClosestPlayerToEntity(this, -1D);
        if (player != null) {
            this.setPickupDelay(0);
            this.copyLocationAndAnglesFrom(player);
        }

        return false;
    }

    public void onUpdate() {
        ItemStack stack = getItem();
        if (!stack.isEmpty() && stack.getItem() != null) {
            if (stack.getItem().onEntityItemUpdate(this)) {
                return;
            }
        }

        if (getItem().isEmpty()) {
            setDead();
        }

        else {
            // BUGFIX (items not pickable) — this used to hand-roll physics and call
            // Entity.onEntityUpdate(), SKIPPING EntityItem.onUpdate entirely — so the vanilla
            // player-collision pickup (and item merging) never ran and dropped mod items were
            // uncollectable. Run the full vanilla update instead; immortality is preserved by
            // a max lifespan (never despawns) plus the attackEntityFrom/dealFireDamage overrides.
            this.lifespan = Integer.MAX_VALUE;
            super.onUpdate();
        }
    }
}
