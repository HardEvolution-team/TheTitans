package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.block.Block;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityItemBomb extends EntityThrowable {
    private static final DataParameter<Byte> WILDCARD = EntityDataManager.<Byte>createKey(EntityItemBomb.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> TOTAL_STACK = EntityDataManager.<Integer>createKey(EntityItemBomb.class, DataSerializers.VARINT);
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityItemBomb.class, DataSerializers.ITEM_STACK);
    public int xpColor;
    private boolean spewingStart;
    private int beatsXp;

    public EntityItemBomb(World p_i1773_1_) {
        super(p_i1773_1_);
        ticksExisted += rand.nextInt(200);
        setSize(3F, 3F);
        rotationYaw = (float)(Math.random() * 360.0D);
        motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 3F);
        motionY = (double)((float)(Math.random() * 0.2D) * 2.0F) + 1D;
        motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 3F);
    }

    public EntityItemBomb(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        this(p_i1775_1_);
        setPosition(p_i1775_2_, p_i1775_4_, p_i1775_6_);
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(WILDCARD, Byte.valueOf((byte)0));
        dataManager.register(TOTAL_STACK, Integer.valueOf(0));
        dataManager.register(ITEM, ItemStack.EMPTY);
    }

    public int fetchAmountStack() {
        return dataManager.get(TOTAL_STACK).intValue();
    }

    public void assignTotalStack(int arg82215n1) {
        dataManager.set(TOTAL_STACK, Integer.valueOf(arg82215n1));
    }

    /**
     * Returns the ItemStack corresponding to the Entity (Note: if no item exists, will log an error but still return an
     * ItemStack containing Block.stone)
     */
    public ItemStack getEntityItem() {
        ItemStack itemstack = dataManager.get(ITEM);
        return itemstack.isEmpty() ? new ItemStack(Blocks.BEDROCK) : itemstack;
    }

    /**
     * Sets the ItemStack for this entity
     */
    public void assignStackStackMob(ItemStack arg92058n1) {
        dataManager.set(ITEM, arg92058n1);
        dataManager.setDirty(ITEM);
    }

    public boolean fetchCardWild() {
        return dataManager.get(WILDCARD).byteValue() == 1;
    }

    public void assignCardWild(boolean arg82201n1) {
        assignStackStackMob(new ItemStack(Blocks.BEDROCK));
        dataManager.set(WILDCARD, Byte.valueOf((byte) (arg82201n1 ? 1 : 0)));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setShort("Value", (short)fetchAmountStack());
        tagCompound.setBoolean("WildCard", fetchCardWild());
        if (!getEntityItem().isEmpty()) {
            tagCompound.setTag("Item", getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        assignTotalStack(tagCompund.getShort("Value"));
        assignCardWild(tagCompund.getBoolean("WildCard"));
        NBTTagCompound nbttagcompound1 = tagCompund.getCompoundTag("Item");
        assignStackStackMob(new ItemStack(nbttagcompound1));
    }

    public boolean isBurning() {
        return false;
    }

    protected float getGravityVelocity() {
        return 0.05F;
    }

    protected void onImpact(net.minecraft.util.math.RayTraceResult movingObject) {
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        else {
            if (!isDead && !world.isRemote && source.getTrueSource() != null && !source.isExplosion()) {
                if (fetchCardWild())
                    spewingStart = true;
                else
                    for (int i1 = 0; i1 < fetchAmountStack(); i1++) {
                    EntityItem entityitem = new EntityItem(world, posX, posY + 1D, posZ, new ItemStack(getEntityItem().getItem(), 1, getEntityItem().getItemDamage()));
                    entityitem.setPickupDelay(40);
                    entityitem.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 3F);
                    entityitem.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F) + 0.5D;
                    entityitem.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 3F);
                    world.spawnEntity(entityitem);
                    setDead();
                }
            }

            return true;
        }
    }

    public void setDead() {
        super.setDead();
        playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 5F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        playSound(SoundEvents.ENTITY_ITEM_BREAK, 5F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        world.spawnParticle(fetchAmountStack() >= 32 ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_LARGE, posX + ((rand.nextDouble() - 0.5F) * width), posY + 3D + ((rand.nextDouble() - 0.5F) * height), posZ + ((rand.nextDouble() - 0.5F) * width), 0.0D, 0.0D, 0.0D);
    }

    public void onUpdate() {
        onEntityUpdate();
        beatsXp ++;
        if (fetchCardWild() && beatsXp % 5 == 0) {
            if (Maths.chance()) {
                Iterator<Item> list = Item.REGISTRY.iterator();
                List<Item> items = new ArrayList<Item>();
                Item i;
                while (list.hasNext()) {
                    i = (Item)list.next();
                    if (i != null)
                        if (i != TitanGear.ultimaBlade && i != TitanGear.optimaAxe)
                        items.add(i);
                }

                if (items.size() > 0) {
                    i = items.get(Maths.random(items.size() - 1));
                    if (i != null)
                        assignStackStackMob(new ItemStack(i, i.getItemStackLimit(), 0));
                }
            }

            else {
                Iterator<Block> list = Block.REGISTRY.iterator();
                List<Block> blocks = new ArrayList<Block>();
                Block i;
                while (list.hasNext()) {
                    i = (Block)list.next();
                    if (i != null)
                        if (i != Blocks.AIR)
                        blocks.add(i);
                }

                if (blocks.size() > 0) {
                    i = blocks.get(Maths.random(blocks.size() - 1));
                    if (i != null) {
                        assignStackStackMob(new ItemStack(i, 64, 0));
                    }
                }
            }

            if (spewingStart) {
                EntityItem entityitem = new EntityItem(world, posX, posY + 1D, posZ, getEntityItem());
                entityitem.setPickupDelay(40);
                entityitem.motionX = Maths.random(-1.0D, 1.0D) * 0.5D;//(double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 3F);
                entityitem.motionY = Maths.random(0.5D, 1.0D) * 0.5D;//(double)((float)(Math.random() * 0.2D) * 2.0F) + 0.5D;
                entityitem.motionZ = Maths.random(-1.0D, 1.0D) * 0.5D;//(double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 3F);
                world.spawnParticle(fetchAmountStack() >= 32 ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_LARGE, posX + ((rand.nextDouble() - 0.5F) * width), posY + 3D + ((rand.nextDouble() - 0.5F) * height), posZ + ((rand.nextDouble() - 0.5F) * width), 0.0D, 0.0D, 0.0D);
                playSound(SoundEvents.ENTITY_CHICKEN_EGG, 5F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.75F);
                world.spawnEntity(entityitem);
                assignTotalStack(fetchAmountStack() - 1);
            }
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.03999999910593033D;
        noClip = pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);
        move(MoverType.SELF, motionX, motionY, motionZ);
        float f = 0.98F;
        if (onGround) {
            BlockPos below = new BlockPos(MathHelper.floor(posX), MathHelper.floor(getEntityBoundingBox().minY) - 1, MathHelper.floor(posZ));
            f = world.getBlockState(below).getBlock().slipperiness * 0.98F;
        }
        motionX *= (double)f;
        motionY *= 0.9800000190734863D;
        motionZ *= (double)f;
        if (onGround)
            motionY *= -0.5D;
        if (getEntityItem().isEmpty())
            assignStackStackMob(new ItemStack(Blocks.BEDROCK));
        if (fetchAmountStack() < 1)
            setDead();
    }
}
