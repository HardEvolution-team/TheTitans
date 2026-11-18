package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemTitanSword extends ItemNormalSword {
    protected int beatsUse;
    protected int beatsServer;
    @SideOnly(Side.CLIENT)
    protected int beatsClient;
    private int uses;
    protected ToolMaterial material;

    public ItemTitanSword(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
        this.material = material;
        uses = 0;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getItem() instanceof ItemTitanSword) {
            tooltip.add(TextFormatting.BLUE + "+" + String.format("%.0f Titan Damage (vs non Titans)", getDamage() * TheTitans.getBonusDamageVSGiantMobs(false)));
            tooltip.add(TextFormatting.BLUE + "+" + String.format("%.0f Titan Damage (vs Titans)", getDamage() * TheTitans.getBonusDamageVSGiantMobs(true)));
        }
    }

    public EnumAction getItemUseAction(ItemStack arg77661n1) {
        return EnumAction.BOW;
    }

    // dash-launch ("slingshot flight") needs a real use duration + a right-click that starts "using"
    // the item — vanilla Item defaults getMaxItemUseDuration to 0, so in 1.12.2 the bow-draw never engaged and
    // onPlayerStoppedUsing (the launch) never fired. This is why flight appeared missing.
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, net.minecraft.util.EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new net.minecraft.util.ActionResult<ItemStack>(net.minecraft.util.EnumActionResult.SUCCESS, stack);
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote)
            beatsClient++;
        else
            beatsServer++;
        if (entityIn != null) {
            if (uses > 0) {
                com.electrifiedded.thetitans.util.Maths.Vec3 vector = new com.electrifiedded.thetitans.util.Maths.Vec3(entityIn.motionX, entityIn.motionY, entityIn.motionZ);
                if (worldIn.isRemote && beatsClient % 2 == 0)
                    for (int i = 0; i < 3; i++)
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, entityIn.posX + Maths.random(-0.05D, 0.05D), entityIn.posY + Maths.random(-0.05D, 0.05D), entityIn.posZ + Maths.random(-0.05D, 0.05D), -(vector.xVec * 0.25), -(vector.yVec * 0.25), -(vector.zVec * 0.25));
                if (entityIn.isInWater() || entityIn.onGround)
                    uses = 0;
                if (entityIn instanceof EntityLivingBase && (beatsServer % 3 == 0 || uses == 0))
                    slayRight((EntityLivingBase)entityIn, stack);
            }
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (player == null || player.isDead)
            return;
        beatsUse ++;
    }

    private void kill(EntityLivingBase entityLiving, ItemStack stack , double multiplier) {
        if (!entityLiving.world.isRemote) {
            Vec3d vec3 = entityLiving.getLook(1.0F);
            double dx = vec3.x * getRange() * 0.95;
            double dy = entityLiving.getEyeHeight() + vec3.y * getRange() * 0.95;
            double dz = vec3.z * getRange() * 0.95;
            List<?> found1 = entityLiving.world.getEntitiesWithinAABBExcludingEntity(entityLiving, entityLiving.getEntityBoundingBox().grow(getRange() * multiplier, getRange() * 0.5D * multiplier, getRange() * multiplier).offset(dx, dy, dz));
            if ((found1 != null) && (!found1.isEmpty())) {
                stack.damageItem(1, entityLiving);
                for (Object mobRef1Obj : found1) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (((mobRef1 instanceof EntityTNTPrimed)) && (!mobRef1.isEntityInvulnerable(DamageSource.GENERIC))) {
                        mobRef1.world.newExplosion(entityLiving, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 4.0F, false, false);
                        mobRef1.setDead();
                    }

                    else if (((mobRef1 instanceof EntityFireball)) && (!mobRef1.isEntityInvulnerable(DamageSource.GENERIC))) {
                        mobRef1.world.newExplosion(entityLiving, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 0.0F, false, false);
                        mobRef1.setDead();
                    }

                    else if (mobRef1 instanceof EntityLivingBase && mobRef1.isEntityAlive() && mobRef1 != entityLiving && (entityLiving instanceof EntityPlayer && !TheTitans.checkFriendlyFire((EntityPlayer)entityLiving, mobRef1, true)))
                        hitEntity(stack, (EntityLivingBase) mobRef1, entityLiving);
                }
            }
        }
    }

    private void slayRight(EntityLivingBase entityLiving, ItemStack stack) {
        if (!entityLiving.world.isRemote) {
            com.electrifiedded.thetitans.util.Maths.Vec3 vector = new com.electrifiedded.thetitans.util.Maths.Vec3(entityLiving.motionX, entityLiving.motionY, entityLiving.motionZ);
            if (vector.speed() / this.fetchPaceDash() > 0.35D)
                kill(entityLiving, stack, 0.35D);
            beatsServer = 0;
        }

        else
            beatsClient = 0;
    }

    private void slayLeft(EntityLivingBase entityLiving, ItemStack stack) {
        if (!entityLiving.world.isRemote) {
            kill(entityLiving, stack, 1.0D);
            beatsServer = 0;
        }

        else
            beatsClient = 0;
        beatsUse = 0;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (uses < getMaxUses() && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            Vec3d vector = player.getLook(1.0F);
            float usePerc = (1.0F - (0.75F * MathHelper.clamp((float)(timeLeft - (getMaxItemUseDuration(stack) - fetchTicksUse())), 0.0F, 1.0F)));
            uses ++;
            if (player.isSneaking()) {
                player.motionX = -fetchPaceDash() * vector.x * usePerc * 0.75D;
                player.motionY = -fetchPaceDash() * vector.y * usePerc * 0.75D;
                player.motionZ = -fetchPaceDash() * vector.z * usePerc * 0.75D;
            }

            else {
                player.motionX = fetchPaceDash() * vector.x * usePerc;
                player.motionY = fetchPaceDash() * vector.y * usePerc;
                player.motionZ = fetchPaceDash() * vector.z * usePerc;
            }

            if (worldIn.isRemote)
                player.playSound(TitanAudio.get("thetitans:titanSwing"), 10.0F, 0.75F + (0.25F * Math.min(usePerc, 1.0F)));
        }
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (target != null && !EntityTitan.quackosIs(target)) {
            com.electrifiedded.thetitans.util.Maths.Vec3 vector = new com.electrifiedded.thetitans.util.Maths.Vec3(attacker.motionX, attacker.motionY, attacker.motionZ);
            float perc = MathHelper.clamp((float)(vector.speed()) / (float)fetchPaceDash(), 1.0F, 2.0F);
            if (target.hurtResistantTime > fetchTicksHurt() * 2)
                target.hurtResistantTime = fetchTicksHurt() * 2;
            if (attacker.motionY < -0.25D)
                ++perc;
            float percentage = 1.0F;
            if (!(target instanceof EntityTitan) && target.getEyeHeight() < 6.0F) {
                if (target instanceof com.electrifiedded.thetitans.entity.minion.IMinion && TheTitans.isHeraldLevel(((com.electrifiedded.thetitans.entity.minion.IMinion)target).fetchKindMinion())) {
                    switch (((com.electrifiedded.thetitans.entity.minion.IMinion)target).fetchKindMinion()) {
                        case TEMPLAR:
                            percentage = TheTitans.getBonusDamageVSGiantMobs(true) * 0.1F;
                            break;
                        case ARBITRATOR:
                            percentage = TheTitans.getBonusDamageVSGiantMobs(true) * 0.01F;
                            break;
                        case GRANDMASTER:
                            percentage = TheTitans.getBonusDamageVSGiantMobs(true) * 0.001F;
                            break;
                        default:
                            percentage = TheTitans.getBonusDamageVSGiantMobs(true) * 1.0E-4F;
                    }

                    target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), getDamage() * percentage * perc);
                    target.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 1.1F);
                    target.hurtResistantTime = 0;
                }

                else
                    target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), getDamage() * perc);
            }

            else {
                target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), getDamage() * TheTitans.getBonusDamageVSGiantMobs(target instanceof EntityTitan) * perc);
                target.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 1.0F);
                target.motionY += 0.5D;
            }
        }

        return true;
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving.world.isRemote)
            entityLiving.playSound(TitanAudio.get("thetitans:titanSwing"), 10.0F, 0.75F + (0.25F * Math.min((float)beatsClient / (float)fetchTicksUse(), 1.0F)));
        slayLeft(entityLiving, stack);
        return false;
    }

    protected double getRange() {
        return 7.0D;
    }

    protected double fetchPaceDash() {
        return 4.0D;
    }

    protected int fetchTicksUse() {
        return 20;
    }

    protected int fetchTicksHurt() {
        return 20;
    }

    public float getDamage() {
        return material.getAttackDamage() + 4.0F;
    }

    protected int getMaxUses() {
        return 4;
    }
}
