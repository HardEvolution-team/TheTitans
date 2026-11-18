package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.minion.EntityCaveSpiderMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCaveSpiderTitan extends EntitySpiderTitan {
    public boolean subduedIs;

    public EntityCaveSpiderTitan(World worldIn) {
        super(worldIn);
        experienceValue = (9000 + fetchPowerExtra() * 350);
    }

    public float fetchScaleMultiplierGiant() {
        return super.fetchScaleMultiplierGiant() * 0.7F;
    }

    protected void applyEntityAI() {
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.spiderGiantSorterCave));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp();
    }

    public void updatePassenger(Entity passenger) {
        passenger.setPosition(posX, posY + (8.8D + fetchPowerExtra() * 0.1D), posZ);
    }

    protected boolean isMovementBlocked() {
        return (subduedIs) && (getPassengers().isEmpty()) ? true : super.isMovementBlocked();
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        subduedIs = tagCompund.getBoolean("Subdued");
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Subdued", subduedIs);
    }

    public boolean canBeCollidedWith() {
        return stunnedIs || subduedIs;
    }

    public double getSpeed() {
        return (fetchIDBonus() == 1 ? 0.65D : 0.6D) + fetchPowerExtra() * 0.001D;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return arg70686n1 != head.getClass() && arg70686n1 != thorax.getClass() && arg70686n1 != abdomen.getClass() && arg70686n1 != rightlegsV.getClass() && arg70686n1 != leftlegsV.getClass() && arg70686n1 != EntityWebShot.class && (arg70686n1 != EntityCaveSpiderMinion.class) && (arg70686n1 != EntityCaveSpiderTitan.class);
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(50) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (isValidLightLevel());
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.spiderGiantServantSpawnrateCave;
    }

    public void strikeMobChoosen(Entity damagedEntity, float damage, int knockbackAmount) {
        super.strikeMobChoosen(damagedEntity, damage, knockbackAmount);
        if (((damagedEntity instanceof EntityLivingBase))) {
            ((EntityLivingBase)damagedEntity).addPotionEffect(new PotionEffect(MobEffects.POISON, 800, 3));
        }
    }

    public boolean processInteract(EntityPlayer arg70085n1, EnumHand hand) {
        ItemStack itemstack = arg70085n1.inventory.getCurrentItem();
        arg70085n1.swingArm(EnumHand.MAIN_HAND);
        if ((stunnedIs) && (!subduedIs)) {
            if ((!itemstack.isEmpty()) && (itemstack.getItem() == Items.GOLDEN_APPLE)) {
                subduedIs = true;
                SoundHandler.whenPlayMob("random.levelup", world, this, 10.0F, 1.0F);
                arg70085n1.sendMessage(new TextComponentString(getCustomNameTag() + " has been subdued by " + arg70085n1.getName()));
                return super.processInteract(arg70085n1, hand);
            }
        }

        else if (subduedIs) {
            if ((itemstack.isEmpty()) && (arg70085n1.getRidingEntity() == null)) {
                arg70085n1.startRiding(this);
            }

            else if (!itemstack.isEmpty()) {
                if (itemstack.getItem() == Items.COOKED_CHICKEN) {
                    AnimationAPI.sendAnimPacket(this, 3);
                    setAnimID(3);
                }

                if (itemstack.getItem() == Items.BONE) {
                    AnimationAPI.sendAnimPacket(this, 9);
                    setAnimID(9);
                }
            }
        }

        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        else {
            return super.attackEntityFrom(source, amount);
        }
    }

    public String getName() {
        switch (fetchVariantGiant()) {
            case 1:
                return I18n.translateToLocal("entity.CaveSpiderTitanJesus.name");
            case 2:
                return I18n.translateToLocal("entity.CaveSpiderTitanOre.name");
            case 3:
                return I18n.translateToLocal("entity.CaveSpiderTitanVenomous.name");
            case 4:
                return I18n.translateToLocal("entity.CaveSpiderTitanIce.name");
            case 5:
                return I18n.translateToLocal("entity.CaveSpiderTitanVoid.name");
            default:
                return I18n.translateToLocal("entity.CaveSpiderTitan.name");
        }
    }

    public void travel(float arg70612n1, float arg70612n2, float arg70612n3) {
        Entity rider = getPassengers().isEmpty() ? null : getPassengers().get(0);
        if ((rider != null) && ((rider instanceof EntityLivingBase)) && (subduedIs)) {
            setAttackTarget(null);
            rotationPitch = rider.rotationPitch;
            rotationYawHead = ((EntityLivingBase)rider).rotationYawHead;
            setRotation(rotationYaw, rotationPitch);
            arg70612n1 = ((EntityLivingBase)rider).moveStrafing;
            arg70612n3 = ((EntityLivingBase)rider).moveForward;
            if (((EntityLivingBase)rider).moveForward > 0.0F) {
                addVelocity(-MathHelper.sin(((EntityLivingBase)rider).rotationYawHead * 3.1415927F / 180.0F) * getSpeed(), 0.0D, MathHelper.cos(((EntityLivingBase)rider).rotationYawHead * 3.1415927F / 180.0F) * getSpeed());
            }

            if (((EntityLivingBase)rider).moveForward < 0.0F) {
                addVelocity(-MathHelper.sin(((EntityLivingBase)rider).rotationYawHead * 3.1415927F / 180.0F) * -getSpeed(), 0.0D, MathHelper.cos(((EntityLivingBase)rider).rotationYawHead * 3.1415927F / 180.0F) * -getSpeed());
            }

            if ((onGround) && (((EntityLivingBase)rider).rotationPitch < -80.0F)) {
                jump();
            }

            if (!world.isRemote) {
                super.travel(arg70612n1, arg70612n2, arg70612n3);
            }

            prevLimbSwingAmount = limbSwingAmount;
            double d1 = posX - prevPosX;
            double d0 = posZ - prevPosZ;
            float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
            limbSwing += limbSwingAmount;
        }

        else {
            super.travel(arg70612n1, arg70612n2, arg70612n3);
        }
    }

    protected EntityLiving fetchMinion() {
        return new EntityCaveSpiderMinion(world);
    }

    protected double cap() {
        return super.cap();
    }
}
