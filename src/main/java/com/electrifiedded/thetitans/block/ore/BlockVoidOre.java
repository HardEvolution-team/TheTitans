package com.electrifiedded.thetitans.block.ore;

import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockVoidOre extends BlockNormalOre {
    public BlockVoidOre(String name) {
        this(null, name);
    }

    public BlockVoidOre(Block base, String name) {
        super(base, name, 1000, 800.0F, 6000000.0F);
        assignDropExp(12000, 48000);
        assignDroppedStack(TitanGear.stackVoid);
        setSoundType(SoundType.STONE);
    }


    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0F);
        entityIn.motionX *= 0.2D;
        entityIn.motionZ *= 0.2D;
        if ((entityIn instanceof EntityPlayer)) {
            ((EntityPlayer)entityIn).addExhaustion(0.2F);
        }

        if (((entityIn instanceof EntityLivingBase)) && (((EntityLivingBase)entityIn).getRNG().nextInt(10) == 0)) {
            ((EntityLivingBase)entityIn).setFire(40);
        }

        if (((entityIn instanceof EntityLivingBase)) && (((EntityLivingBase)entityIn).getRNG().nextInt(60) == 0)) {
            ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 160, 1));
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, net.minecraft.tileentity.TileEntity te, ItemStack stack) {
        net.minecraft.stats.StatBase s = net.minecraft.stats.StatList.getBlockStats(this);
        if (s != null) player.addStat(s);
        player.addExhaustion(10.0F);
        if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            ItemStack is = this.getSilkTouchDrop(state);
            if (is != null) {
                spawnAsEntity(worldIn, pos, is);
            }
        }

        else {
            int i1 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            super.harvestBlock(worldIn, player, pos, state, te, stack);
            player.world.newExplosion(null, player.posX, player.posY, player.posZ, 1.0F, false, player.world.getGameRules().getBoolean("doTileDrops"));
        }
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public int tickRate(World worldIn) {
        return 30;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        randomDisplayTick(state, worldIn, pos, rand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        emitParticleDust(worldIn, pos);
    }

    private void emitParticleDust(World world, BlockPos pos) {
        Random random = world.rand;
        double d0 = 0.0625D;
        for (int l = 0; l < 6; l++) {
            double d1 = pos.getX() + random.nextFloat();
            double d2 = pos.getY() + random.nextFloat();
            double d3 = pos.getZ() + random.nextFloat();
            if ((l == 0) && (!world.getBlockState(pos.up()).isOpaqueCube())) {
                d2 = pos.getY() + 1 + d0;
            }

            if ((l == 1) && (!world.getBlockState(pos.down()).isOpaqueCube())) {
                d2 = pos.getY() + 0 - d0;
            }

            if ((l == 2) && (!world.getBlockState(pos.south()).isOpaqueCube())) {
                d3 = pos.getZ() + 1 + d0;
            }

            if ((l == 3) && (!world.getBlockState(pos.north()).isOpaqueCube())) {
                d3 = pos.getZ() + 0 - d0;
            }

            if ((l == 4) && (!world.getBlockState(pos.east()).isOpaqueCube())) {
                d1 = pos.getX() + 1 + d0;
            }

            if ((l == 5) && (!world.getBlockState(pos.west()).isOpaqueCube())) {
                d1 = pos.getX() + 0 - d0;
            }

            if ((d1 < pos.getX()) || (d1 > pos.getX() + 1) || (d2 < 0.0D) || (d2 > pos.getY() + 1) || (d3 < pos.getZ()) || (d3 > pos.getZ() + 1)) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d1, d2, d3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, d1, d2, d3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, d1, d2, d3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, d1, d2, d3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
