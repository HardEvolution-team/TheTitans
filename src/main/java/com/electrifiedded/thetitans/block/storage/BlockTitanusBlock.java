package com.electrifiedded.thetitans.block.storage;

import com.electrifiedded.thetitans.entity.mob.EntityVoidGolem;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
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

public class BlockTitanusBlock extends BlockNormalCompressed {
    public BlockTitanusBlock(String name) {
        super(MapColor.GRASS, name);
        setHarvestLevel("pickaxe", 1000);
        setHardness(2400.0F);
        setResistance(18000000.0F);
        setSoundType(SoundType.STONE);
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
