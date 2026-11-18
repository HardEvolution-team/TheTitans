package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGargoyleTitanFireball extends EntityFireball {
    private static final DataParameter<Byte> KIND_CUBE = EntityDataManager.<Byte>createKey(EntityGargoyleTitanFireball.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> VARIENT_MESH = EntityDataManager.<Byte>createKey(EntityGargoyleTitanFireball.class, DataSerializers.BYTE);

    public EntityGargoyleTitanFireball(World worldIn) {
        super(worldIn);
        this.setSize(3F, 3F);
        this.assignVarientMesh(this.rand.nextInt(6));
        int i = this.rand.nextInt(100);
        if (i >= 40)
            this.assignKindCube(0);
        else if (i < 40 && i >= 20)
            this.assignKindCube(1);
        else if (i < 20 && i >= 10)
            this.assignKindCube(2);
        else if (i < 10 && i >= 5)
            this.assignKindCube(3);
        else if (i < 5 && i >= 2)
            this.assignKindCube(4);
        else if (i == 1)
            this.assignKindCube(5);
        else if (i == 0)
            this.assignKindCube(6);
    }

    public EntityGargoyleTitanFireball(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
        this.setSize(3F, 3F);
        this.assignVarientMesh(this.rand.nextInt(6));
        int i = this.rand.nextInt(100);
        if (i >= 40)
            this.assignKindCube(0);
        else if (i < 40 && i >= 20)
            this.assignKindCube(1);
        else if (i < 20 && i >= 10)
            this.assignKindCube(2);
        else if (i < 10 && i >= 5)
            this.assignKindCube(3);
        else if (i < 5 && i >= 2)
            this.assignKindCube(4);
        else if (i == 1)
            this.assignKindCube(5);
        else if (i == 0)
            this.assignKindCube(6);
    }

    public EntityGargoyleTitanFireball(World worldIn, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(worldIn, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
        this.setSize(3F, 3F);
        this.assignVarientMesh(this.rand.nextInt(6));
        int i = this.rand.nextInt(100);
        if (i >= 40)
            this.assignKindCube(0);
        else if (i < 40 && i >= 20)
            this.assignKindCube(1);
        else if (i < 20 && i >= 10)
            this.assignKindCube(2);
        else if (i < 10 && i >= 5)
            this.assignKindCube(3);
        else if (i < 5 && i >= 2)
            this.assignKindCube(4);
        else if (i == 1)
            this.assignKindCube(5);
        else if (i == 0)
            this.assignKindCube(6);
    }

    public boolean isBurning() {
        return false;
    }

    protected void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            float f = 3000.0F;
            if ((movingObject.entityHit != null) && !(movingObject.entityHit instanceof EntityGargoyleTitan)) {
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityGargoyleTitan)
                    ((EntityGargoyleTitan)this.shootingEntity).strikeMobChoosen(movingObject.entityHit, (float)((EntityGargoyleTitan)this.shootingEntity).fetchAmountStrike(1.0D), 0);
                this.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity != null ? this.shootingEntity : this), f);
            }

            if (movingObject.entityHit == null) {
                int i = MathHelper.floor(this.posY);
                int i1 = MathHelper.floor(this.posX);
                int j1 = MathHelper.floor(this.posZ);
                for (int l1 = -1; l1 <= 1; l1++) {
                    for (int i2 = -1; i2 <= 1; i2++) {
                        for (int j = 0; j <= 2; j++) {
                            int j2 = i1 + l1;
                            int k = i + j;
                            int l = j1 + i2;
                            BlockPos pos = new BlockPos(j2, k, l);
                            if (!this.world.getBlockState(pos).isOpaqueCube()) {
                                switch (this.getBlockType()) {
                                    case 0:
                                        this.world.setBlockState(pos, Blocks.STONE.getStateFromMeta(0), 3);
                                        break;
                                    case 1:
                                        this.world.setBlockState(pos, Blocks.COAL_ORE.getStateFromMeta(0), 3);
                                        break;
                                    case 2:
                                        this.world.setBlockState(pos, Blocks.IRON_ORE.getStateFromMeta(0), 3);
                                        break;
                                    case 3:
                                        this.world.setBlockState(pos, Blocks.REDSTONE_ORE.getStateFromMeta(0), 3);
                                        break;
                                    case 4:
                                        this.world.setBlockState(pos, Blocks.GOLD_ORE.getStateFromMeta(0), 3);
                                        break;
                                    case 5:
                                        this.world.setBlockState(pos, Blocks.DIAMOND_ORE.getStateFromMeta(0), 3);
                                        break;
                                    case 6:
                                        this.world.setBlockState(pos, Blocks.EMERALD_ORE.getStateFromMeta(0), 3);
                                }
                            }
                        }
                    }
                }

                this.world.newExplosion(this.shootingEntity != null ? this.shootingEntity : this, this.posX, this.posY + 3D, this.posZ, 2F, true, false);
            }

            setDead();
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_CUBE, Byte.valueOf((byte)0));
        this.dataManager.register(VARIENT_MESH, Byte.valueOf((byte)0));
    }

    public int getBlockType() {
        return this.dataManager.get(KIND_CUBE);
    }

    public void assignKindCube(int arg82201n1) {
        this.dataManager.set(KIND_CUBE, Byte.valueOf((byte)arg82201n1));
    }

    public int fetchVarientMesh() {
        return this.dataManager.get(VARIENT_MESH);
    }

    public void assignVarientMesh(int arg82201n1) {
        this.dataManager.set(VARIENT_MESH, Byte.valueOf((byte)arg82201n1));
    }
}
