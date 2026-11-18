package com.electrifiedded.thetitans.entity.titan.classic;

import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityOgPigZombieTitan extends EntityOgZombieTitan {
    public EntityOgPigZombieTitan(World worldIn) {
        super(worldIn);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(130.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7000.0D);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.assignVillager(false);
        if (this.ticksExisted % 20 == 0) {
            this.heal(50.0F);
        }

        if (this.rand.nextInt(40) == 0 && !this.world.isRemote) {
            double d1 = this.posX + (this.rand.nextDouble() * 2.0D - 1.0D) * 30.0D;
            double d3 = this.posZ + (this.rand.nextDouble() * 2.0D - 1.0D) * 30.0D;
            double d2 = (double)this.world.getHeight((int)d1, (int)d3) - 1.5D;
            EntityPigZombie summon = new EntityPigZombie(this.world);
            summon.copyLocationAndAnglesFrom(this);
            summon.setPosition(d1, d2, d3);
            this.world.spawnEntity(summon);
            summon.playSound(TitanAudio.get("titansummonminion"), 2.0F, 1.0F);
            summon.addVelocity(0.0D, 0.9D, 0.0D);
            summon.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(summon)), (IEntityLivingData)null);
        }
    }

    protected SoundEvent getAmbientSound() {
        return TitanAudio.get("titanPigZombieLiving");
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return TitanAudio.get("titanPigZombieGrunt");
    }

    protected SoundEvent getDeathSound() {
        return TitanAudio.get("titanPigZombieDeath");
    }

    protected Item getDropItem() {
        return Items.ROTTEN_FLESH;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        Item item = this.getDropItem();
        this.dropItem(item, 16);
        int j;
        int l;
        if (item != null) {
            j = this.rand.nextInt(63);
            for (l = 0; l < j; ++l) {
                this.dropItem(item, 1 + lootingModifier);
            }

            j = this.rand.nextInt(63);
            for (l = 0; l < j; ++l) {
                this.dropItem(Items.GOLD_NUGGET, 1 + lootingModifier);
            }
        }

        j = 2 + this.rand.nextInt(8 + lootingModifier);
        for (l = 0; l < j; ++l) {
            this.dropItem(Items.EMERALD, 1);
        }

        j = 4 + this.rand.nextInt(16 + lootingModifier);
        for (l = 0; l < j; ++l) {
            this.dropItem(Items.DIAMOND, 1);
        }

        j = 1 + this.rand.nextInt(6 + lootingModifier);
        for (l = 0; l < j; ++l) {
            this.dropItem(TitanGear.ruby, 1);
        }

        j = 2 + this.rand.nextInt(8 + lootingModifier);
        for (l = 0; l < j; ++l) {
            this.dropItem(TitanGear.onyx, 1);
        }
    }

    protected void shedDropRare(int chance) {
        this.dropItem(Items.GOLD_INGOT, 64);
    }
}
