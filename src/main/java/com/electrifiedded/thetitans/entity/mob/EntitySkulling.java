package com.electrifiedded.thetitans.entity.mob;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySkulling extends EntityMob {
    private static final DataParameter<Byte> SHIELDED_V = EntityDataManager.createKey(EntitySkulling.class, DataSerializers.BYTE);
    private int clockHide;

    public EntitySkulling(World p_i1743_1_) {
        super(p_i1743_1_);
        this.setSize(0.6F, 0.6F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 0.8D, true));
        this.tasks.addTask(6, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
    }

    @SideOnly(Side.CLIENT)
    public int fetchTimerHide() {
        return this.clockHide;
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte arg70103n1) {
        if (arg70103n1 == 4) {
            this.clockHide = 100;
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
        }

        else {
            super.handleStatusUpdate(arg70103n1);
        }
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SHIELDED_V, Byte.valueOf((byte)0));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.800000011920929D);
        // EntityMob has no default ATTACK_DAMAGE attribute; register one so the melee AI can hit.
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false.
     */
    public boolean sheildedIs() {
        return (this.dataManager.get(SHIELDED_V).byteValue() & 1) != 0;
    }

    public void assignSheilded(boolean arg70839n1) {
        byte b0 = this.dataManager.get(SHIELDED_V).byteValue();
        if (arg70839n1) {
            b0 = (byte)(b0 | 1);
        }

        else {
            b0 &= -2;
        }

        this.dataManager.set(SHIELDED_V, Byte.valueOf(b0));
    }

    public boolean attackEntityFrom(DamageSource arg70097n1, float arg70097n2) {
        if (this.isEntityInvulnerable(arg70097n1)) {
            return false;
        }

        else if (super.attackEntityFrom(arg70097n1, arg70097n2)) {
            Entity entity = arg70097n1.getTrueSource();
            if (entity != null) {
                if ((rand.nextInt(4) == 0 || arg70097n2 > 10F) && !this.sheildedIs()) {
                    this.world.setEntityState(this, (byte)4);
                    this.clockHide = 100;
                    this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
                }

                if (this.sheildedIs())
                    arg70097n2 *= 0.2F;
            }

            return true;
        }

        else {
            return false;
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.sheildedIs()) {
            this.motionX *= 0.25D;
            this.motionZ *= 0.25D;
        }

        if (this.clockHide > 0) {
            --this.clockHide;
        }

        this.assignSheilded(this.clockHide > 0);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.25F);
    }


    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.SAND);
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        super.dropFewItems(arg70628n1, arg70628n2);
        if (arg70628n1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + arg70628n2) > 0)) {
            this.dropItem(Items.SPIDER_EYE, 1);
        }
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean isPotionApplicable(PotionEffect arg70687n1) {
        return arg70687n1.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(arg70687n1);
    }
}
