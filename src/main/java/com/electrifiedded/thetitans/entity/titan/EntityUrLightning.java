package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityUrLightning extends EntityLightningBolt {
    /** Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc. */
    private int lightningState;
    /** A random long that is used to change the vertex of the lightning rendered in RenderLightningBolt */
    public long boltVertex;
    /** Determines the time before the EntityLightningBolt is destroyed. It is a random integer decremented over time. */
    private int boltLivingTime;

    public EntityUrLightning(World p_i1703_1_, double p_i1703_2_, double p_i1703_4_, double p_i1703_6_) {
        super(p_i1703_1_, p_i1703_2_, p_i1703_4_, p_i1703_6_, false);
        this.setSize(6F, 6F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        if (this.lightningState == 2) {
            float volume = 0.5F + this.rand.nextFloat();
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, volume);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, volume + 0.1F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, volume + 0.2F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, volume + 0.3F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, volume - 0.1F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, volume - 0.2F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, volume - 0.3F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 10F, 0.5F);
        }

        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                if (!this.world.isRemote && this.world.getGameRules().getBoolean("mobGriefing") && this.world.isAreaLoaded(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)), 10)) {
                    int i = MathHelper.floor(this.posX);
                    int j = MathHelper.floor(this.posY);
                    int k = MathHelper.floor(this.posZ);
                    for (i = 0; i < 100; ++i) {
                        j = MathHelper.floor(this.posX) + this.rand.nextInt(10) - 5;
                        k = MathHelper.floor(this.posY) + this.rand.nextInt(10) - 5;
                        int l = MathHelper.floor(this.posZ) + this.rand.nextInt(10) - 5;
                        BlockPos pos = new BlockPos(j, k, l);
                        if (this.world.getBlockState(pos).getMaterial() != Material.AIR) {
                            this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }
                    }
                }

                this.setDead();
            }
        }

        if (this.lightningState >= 0) {
            double d0 = 20D;
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0));
            for (Object entityObj : list) {
                Entity entity = (Entity) entityObj;
                if (entity != null && !(entity instanceof EntityTitanPart) && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit)) {
                    if (!(entity instanceof EntityPlayer) || (entity instanceof EntityPlayer && !((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(TitanGear.optimaAxe)) && !((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(TitanGear.ultimaBlade)))) {
                        entity.setFire(Integer.MAX_VALUE);
                        entity.attackEntityFrom(DamageSourceExtra.lightningBolt, 2000F);
                        entity.hurtResistantTime = 0;
                        if (!(entity instanceof EntityPlayer))
                            entity.isDead = true;
                    }

                    if (entity instanceof EntityPlayer && (((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(TitanGear.optimaAxe)) || ((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(TitanGear.ultimaBlade))) && ((EntityPlayer)entity).isActiveItemStackBlocking()) {
                        ((EntityPlayer)entity).motionX = 0D;
                        ((EntityPlayer)entity).motionY = 0D;
                        ((EntityPlayer)entity).motionZ = 0D;
                        ((EntityPlayer)entity).posX = ((EntityPlayer)entity).lastTickPosX;
                        ((EntityPlayer)entity).posY = ((EntityPlayer)entity).lastTickPosY;
                        ((EntityPlayer)entity).posZ = ((EntityPlayer)entity).lastTickPosZ;
                    }
                }
            }
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        super.onUpdate();
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double arg70112n1) {
        return true;
    }

    protected void entityInit() {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound arg70037n1) {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound arg70014n1) {
    }
}
