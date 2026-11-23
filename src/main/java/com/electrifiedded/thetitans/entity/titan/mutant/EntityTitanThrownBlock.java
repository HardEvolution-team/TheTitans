package com.electrifiedded.thetitans.entity.titan.mutant;

import java.util.List;

import com.electrifiedded.thetitans.compat.mutant.MutantTitans;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * The Mutant Ender Colossus' version of the mutant enderman's thrown block: a giant spinning cube of terrain
 * (several blocks across) that flies on a ballistic arc and bursts on whatever it hits.
 */
public class EntityTitanThrownBlock extends Entity {
    private static final DataParameter<Integer> BLOCK = EntityDataManager.createKey(EntityTitanThrownBlock.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityTitanThrownBlock.class, DataSerializers.FLOAT);

    public static final double GRAVITY = 0.06D;

    private EntityTitan thrower;
    private int throwerId = -1;
    private float damage = 100.0F;
    private int age;

    public EntityTitanThrownBlock(World world) {
        super(world);
        setSize(1.0F, 1.0F);
    }

    public EntityTitanThrownBlock(World world, EntityTitan thrower, IBlockState state, float size, float damage) {
        this(world);
        this.thrower = thrower;
        this.throwerId = thrower.getEntityId();
        this.damage = damage;
        dataManager.set(BLOCK, Block.getStateId(state));
        dataManager.set(SIZE, size);
        setSize(size, size);
    }

    @Override
    protected void entityInit() {
        dataManager.register(BLOCK, Block.getStateId(Blocks.END_STONE.getDefaultState()));
        dataManager.register(SIZE, 1.0F);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (SIZE.equals(key))
            setSize(getBlockSize(), getBlockSize());
    }

    public IBlockState getBlock() {
        return Block.getStateById(dataManager.get(BLOCK));
    }

    public float getBlockSize() {
        return dataManager.get(SIZE);
    }

    /** Spin angle for the renderer. */
    public int getAge() {
        return age;
    }

    /** Launch velocity so the block lands on (x, y, z) after {@code flight} ticks. */
    public void aimAt(double x, double y, double z, int flight) {
        motionX = (x - posX) / flight;
        motionZ = (z - posZ) / flight;
        motionY = (y - posY) / flight + GRAVITY * flight * 0.5D;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        age++;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (world.isRemote) {
            float s = getBlockSize();
            for (int i = 0; i < 4; i++)
                world.spawnParticle(EnumParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5D) * s, posY + rand.nextDouble() * s, posZ + (rand.nextDouble() - 0.5D) * s,
                        (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
            setPosition(posX + motionX, posY + motionY, posZ + motionZ);
            motionY -= GRAVITY;
            return;
        }
        if (thrower == null && throwerId >= 0) {
            Entity e = world.getEntityByID(throwerId);
            if (e instanceof EntityTitan)
                thrower = (EntityTitan)e;
        }
        if (age > 200 || posY < -64.0D) {
            setDead();
            return;
        }
        Vec3d from = new Vec3d(posX, posY + height * 0.5D, posZ);
        Vec3d to = new Vec3d(posX + motionX, posY + height * 0.5D + motionY, posZ + motionZ);
        RayTraceResult hit = world.rayTraceBlocks(from, to, false, true, false);
        // anything living inside the sweep of the cube
        AxisAlignedBB sweep = getEntityBoundingBox().expand(motionX, motionY, motionZ);
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, sweep);
        for (Entity e : list) {
            if (!(e instanceof EntityLivingBase) || e == thrower || !e.canBeCollidedWith())
                continue;
            if (e instanceof EntityTitanPart && thrower != null && ((EntityTitanPart)e).getParent() == thrower)
                continue;
            if (thrower != null && !thrower.allowStrikeGiant(e.getClass()))
                continue;
            impact(e.posX, e.posY, e.posZ);
            return;
        }
        if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK) {
            impact(hit.hitVec.x, hit.hitVec.y, hit.hitVec.z);
            return;
        }
        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
        motionY -= GRAVITY;
    }

    private void impact(double x, double y, double z) {
        float s = getBlockSize();
        if (thrower != null && thrower.isEntityAlive()) {
            MutantTitans.strikeAround(thrower, x, y, z, s * 1.4D, damage, 4, 1.2D);
            MutantTitans.groundBurst(world, x, y, z, (int)(30 + s * 12));
            MutantTitans.play(this, "entity.mutant_enderman.teleport", 12.0F, 0.5F);
        }
        boolean grief = world.getGameRules().getBoolean("mobGriefing");
        world.newExplosion(thrower != null ? thrower : this, x, y, z, Math.min(7.0F, 2.0F + s * 0.6F), false, grief);
        if (world instanceof WorldServer) {
            WorldServer ws = (WorldServer)world;
            ws.spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y + s * 0.5D, z, (int)(60 + s * 20), s * 0.5D, s * 0.5D, s * 0.5D, 0.3D, Block.getStateId(getBlock()));
            ws.spawnParticle(EnumParticleTypes.PORTAL, x, y + s * 0.5D, z, 120, s * 0.6D, s * 0.6D, s * 0.6D, 1.0D);
        }
        setDead();
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 512.0D * 512.0D;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        // short-lived projectile: not worth keeping across reloads
        setDead();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
    }
}
