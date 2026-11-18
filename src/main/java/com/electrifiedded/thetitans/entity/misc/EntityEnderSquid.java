package com.electrifiedded.thetitans.entity.misc;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityEnderSquid extends EntitySquid {
    public EntityEnderSquid(World worldIn) {
        super(worldIn);
        this.experienceValue = 5;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
    }

    public boolean getCanSpawnHere() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox()) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        int j = this.rand.nextInt(3 + lootingModifier) + 1;
        int k;
        for (k = 0; k < j; ++k) {
            this.entityDropItem(new ItemStack(Items.DYE, 1, 0), 0.0F);
        }

        j = this.rand.nextInt(2 + lootingModifier) + 1;
        for (k = 0; k < j; ++k) {
            this.dropItem(Items.ENDER_PEARL, 1);
        }

        if (wasRecentlyHit && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + lootingModifier) > 0)) {
            this.dropItem(Items.ENDER_EYE, 1);
        }
    }

    protected boolean teleportRandomly() {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 16.0D;
        double d1 = this.posY + (this.rand.nextDouble() - 0.5D) * 16.0D;
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 16.0D;
        return this.teleportTo(d0, d1, d2);
    }

    protected boolean teleportTo(double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        double d3 = this.posX;
        double d4 = this.posY;
        double d5 = this.posZ;
        this.posX = event.getTargetX();
        this.posY = event.getTargetY();
        this.posZ = event.getTargetZ();
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY);
        int k = MathHelper.floor(this.posZ);
        if (this.world.getBlockState(new BlockPos(i, j, k)).getMaterial().blocksMovement()) {
            this.setPosition(this.posX, (double)this.world.getHeight(i, k), this.posZ);
        } else {
            this.setPosition(this.posX, this.posY, this.posZ);
        }

        this.world.playSound(null, d3, d4, d5, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.HOSTILE, 1.0F, 1.0F);
        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        return true;
    }

    public boolean isWet() {
        return this.world.isMaterialInBB(this.getEntityBoundingBox().grow(0.0D, -0.6000000238418579D, 0.0D), net.minecraft.block.material.Material.WATER) || this.world.isRainingAt(new BlockPos(this)) || this.world.isRainingAt(new BlockPos(this.posX, this.posY + (double)this.height, this.posZ));
    }

    public boolean isInWater() {
        return true;
    }

    public void onLivingUpdate() {
        this.inWater = this.isInWater();
        this.fallDistance = 0.0F;
        this.idleTime = 0;
        super.onLivingUpdate();
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        if (this.isWet() || this.isBurning() || this.isInsideOfMaterial(net.minecraft.block.material.Material.LAVA)) {
            this.teleportRandomly();
        }

        if (!this.world.isRemote && this.world.getClosestPlayerToEntity(this, 4.0D) != null) {
            this.teleportTo(this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D, this.posY + 12.0D, this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D);
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.getIsInvulnerable()) {
            return false;
        }

        if (source instanceof EntityDamageSourceIndirect) {
            for (int i = 0; i < 64; ++i) {
                if (this.teleportRandomly()) {
                    return false;
                }
            }
        }

        if (!super.attackEntityFrom(source, amount)) {
            return false;
        }

        if (this.rand.nextInt(10) == 0 || amount >= 5.0F) {
            this.teleportRandomly();
        }

        return true;
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return this.experienceValue > 0 ? this.experienceValue + this.rand.nextInt(this.experienceValue) : this.experienceValue;
    }
}
