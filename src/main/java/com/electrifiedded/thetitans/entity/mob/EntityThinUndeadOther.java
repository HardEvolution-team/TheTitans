package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.entity.misc.IVariedMob;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.biome.Biome;

public class EntityThinUndeadOther extends EntitySkeleton implements IVariedMob {
    private static final DataParameter<Integer> KIND_MOB = EntityDataManager.createKey(EntityThinUndeadOther.class, DataSerializers.VARINT);

    public EntityThinUndeadOther(World p_i1745_1_) {
        super(p_i1745_1_);
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityGolem>(this, EntityGolem.class, false));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_MOB, Integer.valueOf(0));
    }

    public int fetchKindMob() {
        return this.dataManager.get(KIND_MOB).intValue();
    }

    public void assignKindMob(int miniontype) {
        this.dataManager.set(KIND_MOB, Integer.valueOf(miniontype));
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.fetchHpMob());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.fetchStrikeMob());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.fetchPaceMob());
        this.setHealth(this.getMaxHealth());
        // type 1 = wither-skeleton variant: fire immune, melee stone sword; all others use a bow.
        this.isImmuneToFire = miniontype == 1;
        ItemStack itemstack = this.getHeldItemMainhand();
        if (miniontype == 1) {
            if (itemstack.isEmpty() || itemstack.getItem() == Items.BOW)
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }

        else if (itemstack.isEmpty() || itemstack.getItem() == Items.STONE_SWORD) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }

        this.setCombatTask();
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.assignKindMob(5 + this.rand.nextInt(10));
        if (this.world.provider instanceof WorldProviderHell) {
            this.assignKindMob(2);
        }

        Biome biome = this.world.getBiome(new BlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ)));
        if (biome.getTemperature(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ))) < 0.15F) {
            this.assignKindMob(3 + this.rand.nextInt(2));
        }

        return livingdata;
    }

    public int getTotalArmorValue() {
        // original: type-5 (SkeletonMail) gets +12 armor.
        switch (this.fetchKindMob()) {
            case 5:
                return super.getTotalArmorValue() + 12;
            default:
                return super.getTotalArmorValue();
        }
    }

    public String getName() {
        return I18n.translateToLocal("entity." + this.fetchNamesVarient() + ".name");
    }

    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        EntityTippedArrow entityarrow = new EntityTippedArrow(this.world, this);
        double d10 = target.posX - this.posX;
        double d20 = target.getEntityBoundingBox().minY + target.height / 3.0F - entityarrow.posY;
        double d30 = target.posZ - this.posZ;
        double d40 = MathHelper.sqrt(d10 * d10 + d30 * d30);
        entityarrow.shoot(d10, d20 + d40 * 0.2D, d30, 1.6F, 14 - this.world.getDifficulty().getId() * 4);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, this.getHeldItemMainhand());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, this.getHeldItemMainhand());
        entityarrow.setDamage(distanceFactor * this.fetchStrikeMob() + this.rand.nextGaussian() * 0.25D + this.world.getDifficulty().getId() * 0.11F);
        if (i > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
        }

        if (j > 0) {
            entityarrow.setKnockbackStrength(j);
        }

        if ((EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, this.getHeldItemMainhand()) > 0) || this.fetchKindMob() == 1) {
            entityarrow.setFire(100);
        }

        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow);
    }

    public String fetchNamesVarient() {
        switch (this.fetchKindMob()) {
            case 1:  return "SkeletonWitherSmall";
            case 2:  return "SkeletonAsh";
            case 3:  return "SkeletonFrail";
            case 4:  return "SkeletonFrozen";
            case 5:  return "SkeletonMail";
            case 6:  return "SkeletonBrokenHead";
            case 7:  return "SkeletonOvergrown";
            case 8:  return "Skeleton4Face";
            case 9:  return "SkeletonFang";
            case 10: return "SkeletonHoly";
            case 11: return "SkeletonDoctor";
            case 12: return "SkeletonSad";
            case 13: return "SkeletonMud";
            case 14: return "SkeletonCreepster";
            case 15: return "SkeletonWerid";
            default: return "Skeleton";
        }
    }

    public double fetchHpMob() {
        switch (this.fetchKindMob()) {
            case 1:  return 20.0D;
            case 2:  return 25.0D;
            case 3:  return 15.0D;
            case 4:  return 30.0D;
            case 5:  return 60.0D;
            case 6:  return 10.0D;
            case 7:  return 22.0D;
            case 8:  return 80.0D;
            case 9:  return 16.0D;
            case 10: return 30.0D;
            case 11: return 40.0D;
            case 12: return 20.0D;
            case 13: return 40.0D;
            case 14: return 30.0D;
            case 15: return 50.0D;
            default: return 20.0D;
        }
    }

    public double fetchStrikeMob() {
        switch (this.fetchKindMob()) {
            case 1:  return 4.0D;
            case 2:  return 3.0D;
            case 3:  return 5.0D;
            case 4:  return 2.0D;
            case 5:  return 3.0D;
            case 6:  return 3.0D;
            case 7:  return 3.0D;
            case 8:  return 4.0D;
            case 9:  return 5.0D;
            case 10: return 2.0D;
            case 11: return 10.0D;
            case 12: return 2.0D;
            case 13: return 6.0D;
            case 14: return 3.0D;
            case 15: return 2.0D;
            default: return 2.0D;
        }
    }

    public double fetchPaceMob() {
        switch (this.fetchKindMob()) {
            case 1:  return 0.5D;
            case 3:  return 0.3D;
            case 6:  return 0.275D;
            default: return 0.25D;
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Type", this.fetchKindMob());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.assignKindMob(tagCompund.getInteger("Type"));
    }
}
