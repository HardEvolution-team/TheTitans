package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityFallingBlockTitan extends EntityFallingBlock {
    private IBlockState fld145811e;
    public int fld145814a;
    public int fld145812b;
    public boolean fld145813c;
    private boolean fld145808f;
    private boolean fld145809g;
    private int fld145815h;
    private float fld145816i;
    public NBTTagCompound fld145810d;

    public EntityFallingBlockTitan(World p_i1706_1_) {
        super(p_i1706_1_);
        fld145813c = true;
        fld145815h = 40;
        fld145816i = 2.0F;
    }

    public EntityFallingBlockTitan(World p_i45318_1_, double p_i45318_2_, double p_i45318_4_, double p_i45318_6_, Block p_i45318_8_) {
        this(p_i45318_1_, p_i45318_2_, p_i45318_4_, p_i45318_6_, p_i45318_8_, 0);
    }

    public EntityFallingBlockTitan(World p_i45319_1_, double p_i45319_2_, double p_i45319_4_, double p_i45319_6_, Block p_i45319_8_, int p_i45319_9_) {
        super(p_i45319_1_);
        fld145813c = true;
        fld145815h = 40;
        fld145816i = 2.0F;
        fld145814a = p_i45319_9_;
        fld145811e = p_i45319_8_.getStateFromMeta(p_i45319_9_);
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
        setPosition(p_i45319_2_, p_i45319_4_, p_i45319_6_);
        prevPosX = p_i45319_2_;
        prevPosY = p_i45319_4_;
        prevPosZ = p_i45319_6_;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        fld145809g = true;
        ignoreFrustumCheck = false;
        if (fld145811e.getMaterial() == Material.AIR) {
            fld145811e = Blocks.SAND.getDefaultState();
        }

        else {
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            ++fld145812b;
            motionY -= 0.03999999910593033D;
            move(MoverType.SELF, motionX, motionY, motionZ);
            motionX *= 0.9800000190734863D;
            motionY *= 0.9800000190734863D;
            motionZ *= 0.9800000190734863D;
            if (isServer() && fld145812b > 10) {
                int i = MathHelper.floor(posX);
                int j = MathHelper.floor(posY);
                int k = MathHelper.floor(posZ);
                BlockPos pos = new BlockPos(i, j, k);
                if (onGround) {
                    motionX *= 0.699999988079071D;
                    motionZ *= 0.699999988079071D;
                    motionY *= -0.5D;
                    if (world.getBlockState(pos).getBlock() != Blocks.PISTON_EXTENSION) {
                        if (world.getClosestPlayerToEntity(this, 32.0D) != null) {
                            net.minecraft.util.SoundEvent press = com.electrifiedded.thetitans.registry.TitanAudio.get("titanPress");
                            if (press != null) {
                                playSound(press, 2.0F, 1.0F + rand.nextFloat() * 0.25F);
                            }
                        }
                        setDead();
                        if (!fld145808f && world.mayPlace(fld145811e.getBlock(), pos, true, net.minecraft.util.EnumFacing.UP, (Entity)null) && !BlockFalling.canFallThrough(world.getBlockState(pos.down())) && world.setBlockState(pos, fld145811e, 3)) {
                            if (fld145811e.getBlock() instanceof BlockFalling) {
                                ((BlockFalling)fld145811e.getBlock()).onEndFalling(world, pos, fld145811e, world.getBlockState(pos));
                            }

                            if (fld145810d != null && fld145811e.getBlock() instanceof ITileEntityProvider) {
                                TileEntity tileentity = world.getTileEntity(pos);
                                if (tileentity != null) {
                                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                                    tileentity.writeToNBT(nbttagcompound);
                                    Iterator<?> iterator = fld145810d.getKeySet().iterator();
                                    while (iterator.hasNext()) {
                                        String s = (String)iterator.next();
                                        NBTBase nbtbase = fld145810d.getTag(s);
                                        if (!s.equals("x") && !s.equals("y") && !s.equals("z")) {
                                            nbttagcompound.setTag(s, nbtbase.copy());
                                        }
                                    }

                                    tileentity.readFromNBT(nbttagcompound);
                                    tileentity.markDirty();
                                }
                            }
                        }

                        else if (fld145813c && fld145808f) {
                            entityDropItem(new ItemStack(fld145811e.getBlock(), 1, fld145811e.getBlock().damageDropped(fld145811e)), 0.0F);
                        }
                    }
                }

                else if (isServer() && (j < 1 || j > 256) || fld145812b > 1000) {
                    if (isClient()) {
                        // TODO client particle/destroy-effect calls (renderGlobal.spawnParticle / effectRenderer.addBlockDestroyEffects) — best-effort world particle
                        world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_LARGE, i, j, k, 0D, 0D, 0D);
                        if (world.getClosestPlayerToEntity(this, 16D) != null) {
                            // wired custom sound thetitans:titanPress (key registered)
                            net.minecraft.util.SoundEvent press = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanPress");
                            if (press != null)
                                playSound(press, 10F, 1.25F + (rand.nextFloat() * 0.25F));
                        }
                    }

                    entityDropItem(new ItemStack(fld145811e.getBlock(), 1, fld145811e.getBlock().damageDropped(fld145811e)), 0.0F);
                    fall(fld145812b, 1.0F);
                    setDead();
                }
            }
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    @SuppressWarnings("unchecked")
    public void fall(float arg70069n1, float arg193627n2) {
        if (fld145809g) {
            int i = MathHelper.ceil(arg70069n1 - 1.0F);
            if (i > 0) {
                ArrayList<?> arraylist = new ArrayList<Object>(world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(1D, 1D, 1D)));
                boolean flag = fld145811e.getBlock() == Blocks.ANVIL;
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                Iterator<?> iterator = arraylist.iterator();
                while (iterator.hasNext()) {
                    Entity entity = (Entity)iterator.next();
                    entity.hurtResistantTime = 0;
                    if (!entity.noClip) {
                        entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor((float)i * fld145816i), fld145815h * 10F));
                    }
                    if (fld145811e.getBlock() == Blocks.FIRE)
                        entity.setFire(20);
                }

                if (flag && (double)rand.nextFloat() < 0.05000000074505806D + (double)i * 0.05D) {
                    int j = fld145814a >> 2;
                    int k = fld145814a & 3;
                    ++j;
                    if (j > 2) {
                        fld145808f = true;
                    }

                    else {
                        fld145814a = k | j << 2;
                    }
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound arg70014n1) {
        Block block = fld145811e.getBlock();
        arg70014n1.setByte("Tile", (byte)Block.getIdFromBlock(block));
        arg70014n1.setInteger("TileID", Block.getIdFromBlock(block));
        arg70014n1.setByte("Data", (byte)fld145814a);
        arg70014n1.setByte("Time", (byte)fld145812b);
        arg70014n1.setBoolean("DropItem", fld145813c);
        arg70014n1.setBoolean("HurtEntities", fld145809g);
        arg70014n1.setFloat("FallHurtAmount", fld145816i);
        arg70014n1.setInteger("FallHurtMax", fld145815h);
        if (fld145810d != null) {
            arg70014n1.setTag("TileEntityData", fld145810d);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound arg70037n1) {
        Block block;
        if (arg70037n1.hasKey("TileID", 99)) {
            block = Block.getBlockById(arg70037n1.getInteger("TileID"));
        }

        else {
            block = Block.getBlockById(arg70037n1.getByte("Tile") & 255);
        }

        fld145814a = arg70037n1.getByte("Data") & 255;
        fld145811e = block.getStateFromMeta(fld145814a);
        fld145812b = arg70037n1.getByte("Time") & 255;
        if (arg70037n1.hasKey("HurtEntities", 99)) {
            fld145809g = arg70037n1.getBoolean("HurtEntities");
            fld145816i = arg70037n1.getFloat("FallHurtAmount");
            fld145815h = arg70037n1.getInteger("FallHurtMax");
        }

        else if (fld145811e.getBlock() == Blocks.ANVIL) {
            fld145809g = true;
        }

        if (arg70037n1.hasKey("DropItem", 99)) {
            fld145813c = arg70037n1.getBoolean("DropItem");
        }

        if (arg70037n1.hasKey("TileEntityData", 10)) {
            fld145810d = arg70037n1.getCompoundTag("TileEntityData");
        }

        if (fld145811e.getMaterial() == Material.AIR) {
            fld145811e = Blocks.SAND.getDefaultState();
        }
    }

    public void setHurtEntities(boolean arg145806n1) {
        fld145809g = arg145806n1;
    }

    public void addEntityCrashInfo(CrashReportCategory arg85029n1) {
        super.addEntityCrashInfo(arg85029n1);
        arg85029n1.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(fld145811e.getBlock())));
        arg85029n1.addCrashSection("Immitating block data", Integer.valueOf(fld145814a));
    }

    @SideOnly(Side.CLIENT)
    public float fetchScaleShadow() {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World fetchGiantRealm() {
        return world;
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    public Block fetchCubeFalling() {
        return fld145811e.getBlock();
    }

    @Override
    public IBlockState getBlock() {
        return fld145811e;
    }

    public boolean isClient() {
        return world.isRemote;
    }

    public boolean isServer() {
        return !world.isRemote;
    }
}
