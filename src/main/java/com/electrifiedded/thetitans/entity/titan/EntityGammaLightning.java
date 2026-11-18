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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityGammaLightning extends EntityLightningBolt {
    private static final DataParameter<Float> RED = EntityDataManager.<Float>createKey(EntityGammaLightning.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> GREEN = EntityDataManager.<Float>createKey(EntityGammaLightning.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> BLUE = EntityDataManager.<Float>createKey(EntityGammaLightning.class, DataSerializers.FLOAT);
    /** Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc. */
    private int lightningState;
    /** A random long that is used to change the vertex of the lightning rendered in RenderLightningBolt */
    public long boltVertex;
    /** Determines the time before the EntityLightningBolt is destroyed. It is a random integer decremented over time. */
    private int boltLivingTime;
    // EntityRegistry.registerModEntity requires a (World) ctor; delegate to the white/default-colour variant.
    public EntityGammaLightning(World p_i1703_1_) {
        this(p_i1703_1_, 1.0F, 1.0F, 1.0F);
    }

    public EntityGammaLightning(World p_i1703_1_, float red, float green, float blue) {
        // 1.12.2 EntityLightningBolt ctor is (World,x,y,z,effectOnly); no r/g/b color ctor
        super(p_i1703_1_, 0.0D, 0.0D, 0.0D, false);
        this.setSize(3F, 3F);
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
    }

    public EntityGammaLightning(World p_i1703_1_, double p_i1703_2_, double p_i1703_4_, double p_i1703_6_, float red, float green, float blue) {
        this(p_i1703_1_, red, green, blue);
        this.setPosition(p_i1703_2_, p_i1703_4_, p_i1703_6_);
    }

    public void setPosition(double arg70107n1, double arg70107n3, double arg70107n5) {
        super.setPosition(arg70107n1, arg70107n3, arg70107n5);
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
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 10F, 0.7F);
        }

        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                if (!this.world.isRemote && this.world.getGameRules().getBoolean("doFireTick") && this.world.isAreaLoaded(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)), 10)) {
                    int i = MathHelper.floor(this.posX);
                    int j = MathHelper.floor(this.posY);
                    int k = MathHelper.floor(this.posZ);
                    BlockPos pos = new BlockPos(i, j, k);
                    if (this.world.getBlockState(pos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(this.world, pos)) {
                        this.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    }

                    for (i = 0; i < 16; ++i) {
                        j = MathHelper.floor(this.posX) + this.rand.nextInt(6) - 3;
                        k = MathHelper.floor(this.posY) + this.rand.nextInt(6) - 3;
                        int l = MathHelper.floor(this.posZ) + this.rand.nextInt(6) - 3;
                        BlockPos pos2 = new BlockPos(j, k, l);
                        if (this.world.getBlockState(pos2).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(this.world, pos2)) {
                            this.world.setBlockState(pos2, Blocks.FIRE.getDefaultState());
                        }
                    }
                }

                this.setDead();
            }

            else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
                if (!this.world.isRemote && this.world.getGameRules().getBoolean("doFireTick") && this.world.isAreaLoaded(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)), 10)) {
                    int i = MathHelper.floor(this.posX);
                    int j = MathHelper.floor(this.posY);
                    int k = MathHelper.floor(this.posZ);
                    BlockPos pos = new BlockPos(i, j, k);
                    if (this.world.getBlockState(pos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(this.world, pos)) {
                        this.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }

        if (this.lightningState >= 0) {
            double d0 = 10D;
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0));
            for (Object entityObj : list) {
                Entity entity = (Entity) entityObj;
                if (entity != null && !(entity instanceof EntityTitanPart) && !(entity instanceof EntityTitan) && !entity.isImmuneToFire()) {
                    if (!(entity instanceof EntityPlayer) || (entity instanceof EntityPlayer && !((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(TitanGear.optimaAxe)) && !((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(TitanGear.ultimaBlade)))) {
                        entity.onStruckByLightning(null);
                        entity.setFire(1000);
                        entity.attackEntityFrom(DamageSourceExtra.lightningBolt, 200F);
                        entity.hurtResistantTime = 0;
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

                if (entity != null && entity.getClass() == EntityLightningBolt.class) {
                    entity.setDead();
                }
            }
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.onEntityUpdate();
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double arg70112n1) {
        return true;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(RED, Float.valueOf(0F));
        this.dataManager.register(GREEN, Float.valueOf(0.56F));
        this.dataManager.register(BLUE, Float.valueOf(0F));
    }

    public final float getRed() {
        return this.dataManager.get(RED).floatValue();
    }

    public void setRed(float arg70606n1) {
        this.dataManager.set(RED, Float.valueOf(MathHelper.clamp(arg70606n1, 0F, 1F)));
    }

    public final float getGreen() {
        return this.dataManager.get(GREEN).floatValue();
    }

    public void setGreen(float arg70606n1) {
        this.dataManager.set(GREEN, Float.valueOf(MathHelper.clamp(arg70606n1, 0F, 1F)));
    }

    public final float getBlue() {
        return this.dataManager.get(BLUE).floatValue();
    }

    public void setBlue(float arg70606n1) {
        this.dataManager.set(BLUE, Float.valueOf(MathHelper.clamp(arg70606n1, 0F, 1F)));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound arg70037n1) {
        this.setRed(arg70037n1.getFloat("R"));
        this.setGreen(arg70037n1.getFloat("G"));
        this.setBlue(arg70037n1.getFloat("B"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound arg70014n1) {
        arg70014n1.setFloat("R", this.getRed());
        arg70014n1.setFloat("G", this.getGreen());
        arg70014n1.setFloat("B", this.getBlue());
    }
}
