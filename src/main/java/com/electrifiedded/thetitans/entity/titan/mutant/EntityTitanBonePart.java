package com.electrifiedded.thetitans.entity.titan.mutant;

import com.electrifiedded.thetitans.compat.mutant.MutantTitanItems;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * One giant bone of a fallen Mutant Skeleton Titan - the titan form of the mutant's body parts. Thrown out of the
 * collapsing skeleton it tumbles, crushes what it hits, then lies on the ground until a player takes it (right
 * click) as a Titanic bone item (pelvis / rib / skull / limb / shoulder pad) for the Titanic Mutant Skeleton armour.
 * Part ids are the mutant's: 0 pelvis, 1-18 ribs, 19 skull, 21-24 arms, 25-28 legs, 29-30 shoulder pads.
 */
@SuppressWarnings("deprecation")
public class EntityTitanBonePart extends Entity implements IEntityAdditionalSpawnData {
    /** Exactly the 18 pieces the mutant drops: one titan gives one full armour set. */
    public static final int[] PARTS = { 0, 1, 2, 7, 8, 13, 14, 19, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };

    private int part;
    /** Model scale (the titan's render scale, 1.0 = mutant sized). */
    private float scale = 1.0F;
    private int variant;
    private boolean yawPositive, pitchPositive;
    private int groundTicks;
    private float damage = 20.0F;

    public EntityTitanBonePart(World world) {
        super(world);
        prevRotationYaw = rotationYaw = rand.nextFloat() * 360.0F;
        prevRotationPitch = rotationPitch = rand.nextFloat() * 360.0F;
        yawPositive = rand.nextBoolean();
        pitchPositive = rand.nextBoolean();
        setSize(0.7F, 0.7F);
        ignoreFrustumCheck = true;
    }

    public EntityTitanBonePart(World world, EntityTitan owner, int part, float scale, float damage) {
        this(world);
        this.part = part;
        this.variant = owner.fetchVariantGiant();
        this.damage = damage;
        setScale(scale);
        setPosition(owner.posX, owner.posY + owner.height * (0.25F + rand.nextFloat() * 0.5F), owner.posZ);
    }

    private void setScale(float s) {
        scale = Math.max(1.0F, s);
        float w = Math.min(0.7F * scale, 10.0F); // collision box capped: water/lava/collision scans grow with its volume
        setSize(w, w);
    }

    public int getPart() {
        return part;
    }

    public float getScale() {
        return scale;
    }

    public int getVariant() {
        return variant;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d = 64.0D * scale;
        return distance < d * d;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        Item item = MutantTitanItems.boneItem(part);
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    @Override
    public void onUpdate() {
        // PERF — a resting bone only re-checks the ground (and water/fire) once a second.
        boolean resting = onGround && Math.abs(motionX) + Math.abs(motionZ) < 0.01D && Math.abs(motionY) < 0.1D;
        if (resting && (ticksExisted + getEntityId()) % 20 != 0) {
            ticksExisted++;
            motionX = motionY = motionZ = 0.0D;
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            prevRotationYaw = rotationYaw;
            prevRotationPitch = rotationPitch;
            if (!world.isRemote && ++groundTicks > 36000)
                setDead();
            return;
        }
        super.onUpdate();
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (!hasNoGravity())
            motionY -= 0.045D;
        move(MoverType.SELF, motionX, motionY, motionZ);
        motionX *= 0.96D;
        motionY *= 0.96D;
        motionZ *= 0.96D;
        if (onGround) {
            motionX *= 0.7D;
            motionY *= 0.7D;
            motionZ *= 0.7D;
        }
        if (!onGround && !inWater) {
            rotationYaw += 10.0F * (yawPositive ? 1 : -1);
            rotationPitch += 15.0F * (pitchPositive ? 1 : -1);
            if (!world.isRemote && ticksExisted % 2 == 0)
                crush();
        } else if (!world.isRemote) {
            if (groundTicks == 0 && world instanceof WorldServer) // landing thud
                ((WorldServer)world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY, posZ, 3, width * 0.4D, 0.2D, width * 0.4D, 0.0D);
            groundTicks++;
        }
        // lies around for 30 minutes, then crumbles
        if (!world.isRemote && groundTicks > 36000)
            setDead();
    }

    private void crush() {
        AxisAlignedBB box = getEntityBoundingBox();
        for (Entity e : world.getEntitiesWithinAABBExcludingEntity(this, box)) {
            if (!(e instanceof EntityLivingBase) || !e.canBeCollidedWith() || e instanceof EntityMutantSkeletonTitan)
                continue;
            if (isBurning())
                e.setFire(5);
            if (e instanceof EntityTitan)
                ((EntityTitan)e).hurt(damage);
            else
                e.attackEntityFrom(DamageSource.causeThrownDamage(this, this), damage);
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        Item item = MutantTitanItems.boneItem(part);
        if (!world.isRemote && item != null) {
            if (world.getGameRules().getBoolean("doEntityDrops")) {
                EntityItem drop = new EntityItem(world, posX, posY + height * 0.5D, posZ, new ItemStack(item));
                drop.setNoPickupDelay();
                world.spawnEntity(drop);
            }
            if (world instanceof WorldServer)
                ((WorldServer)world).spawnParticle(EnumParticleTypes.CLOUD, posX, posY + height * 0.5D, posZ, 20, width * 0.4D, height * 0.4D, width * 0.4D, 0.02D);
            playSound(net.minecraft.init.SoundEvents.ENTITY_SKELETON_HURT, 2.0F, 0.5F);
            setDead();
        }
        player.swingArm(hand);
        return true;
    }

    @Override
    public String getName() {
        if (hasCustomName())
            return getCustomNameTag();
        Item item = MutantTitanItems.boneItem(part);
        return item == null ? super.getName() : I18n.translateToLocal(item.getTranslationKey() + ".name");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setByte("Part", (byte)part);
        tag.setFloat("Scale", scale);
        tag.setByte("Variant", (byte)variant);
        tag.setInteger("GroundTicks", groundTicks);
        tag.setFloat("Damage", damage);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        part = tag.getByte("Part");
        setScale(tag.getFloat("Scale"));
        variant = tag.getByte("Variant");
        groundTicks = tag.getInteger("GroundTicks");
        damage = tag.getFloat("Damage");
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeByte(part);
        buf.writeFloat(scale);
        buf.writeByte(variant);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        part = buf.readByte();
        setScale(buf.readFloat());
        variant = buf.readByte();
    }
}
