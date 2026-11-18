package com.electrifiedded.thetitans.block.plant;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntityHarcadiumGolem;
import com.electrifiedded.thetitans.entity.mob.EntityVoidGolem;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyle;
import com.electrifiedded.thetitans.registry.TitanBlockSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMagicPumpkin extends BlockDirectional {
    private boolean lit;

    public BlockMagicPumpkin(boolean p_i45419_1_) {
        super(Material.GOURD);
        this.setTickRandomly(true);
        this.lit = p_i45419_1_;
        setCreativeTab(TheTitans.blocksPageGiant);
        setHardness(1.0F);
        setSoundType(SoundType.WOOD);
        setTranslationKey("magic_pumpkin");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, "magic_pumpkin"));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        EntityPlayer player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 16D, false);
        BlockPos below = pos.down();
        BlockPos below2 = pos.down(2);
        Block b1 = block(worldIn, below);
        Block b2 = block(worldIn, below2);

        if (b1 == Blocks.IRON_BLOCK && b2 == Blocks.REDSTONE_BLOCK)
            summonTry(worldIn, pos, player, 0, Blocks.STONE);
        else if (b1 == Blocks.SANDSTONE && b2 == Blocks.REDSTONE_BLOCK)
            summonTry(worldIn, pos, player, 1, Blocks.SANDSTONE);
        else if (b1 == Blocks.OBSIDIAN && b2 == Blocks.DIAMOND_BLOCK)
            summonTry(worldIn, pos, player, 2, Blocks.OBSIDIAN);
        else if (b1 == Blocks.DIAMOND_BLOCK && b2 == Blocks.REDSTONE_BLOCK)
            summonTry(worldIn, pos, player, 3, Blocks.GOLD_BLOCK);
        else if (b1 == Blocks.IRON_BLOCK && b2 == Blocks.REDSTONE_BLOCK)
            summonTry(worldIn, pos, player, 4, Blocks.IRON_BLOCK);
        else if (b1 == Blocks.OBSIDIAN && b2 == Blocks.REDSTONE_BLOCK)
            summonTry(worldIn, pos, player, 5, Blocks.END_STONE);
        else if (b1 == Blocks.GOLD_BLOCK && b2 == Blocks.REDSTONE_BLOCK)
            summonTry(worldIn, pos, player, 6, Blocks.NETHER_BRICK);
        else if (b1 == com.electrifiedded.thetitans.registry.TitanBlockSet.harcadium_block && b2 == com.electrifiedded.thetitans.registry.TitanBlockSet.harcadium_block)
            tryGolemSummon(worldIn, pos, player, com.electrifiedded.thetitans.registry.TitanBlockSet.harcadium_block, new com.electrifiedded.thetitans.entity.mob.EntityHarcadiumGolem(worldIn));
        else if (b1 == com.electrifiedded.thetitans.registry.TitanBlockSet.void_block && b2 == com.electrifiedded.thetitans.registry.TitanBlockSet.void_block)
            tryGolemSummon(worldIn, pos, player, com.electrifiedded.thetitans.registry.TitanBlockSet.void_block, new com.electrifiedded.thetitans.entity.mob.EntityVoidGolem(worldIn));
    }

    // harcadium/void golem summon — same pattern as the gargoyle helper but spawns the given golem.
    private void tryGolemSummon(World world, BlockPos pos, EntityPlayer player, Block arms, net.minecraft.entity.EntityLiving golem) {
        BlockPos below = pos.down();
        boolean flagX = block(world, below.west()) == arms && block(world, below.east()) == arms;
        boolean flagZ = block(world, below.north()) == arms && block(world, below.south()) == arms;
        if (!flagX && !flagZ)
            return;
        world.setBlockToAir(pos);
        world.setBlockToAir(below);
        world.setBlockToAir(pos.down(2));
        if (flagX) { world.setBlockToAir(below.west()); world.setBlockToAir(below.east()); }
        else { world.setBlockToAir(below.north()); world.setBlockToAir(below.south()); }
        if (golem instanceof com.electrifiedded.thetitans.entity.titan.EntityGargoyle)
            ((com.electrifiedded.thetitans.entity.titan.EntityGargoyle)golem).setPlayerCreated(true);
        golem.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY() - 1.95D, (double)pos.getZ() + 0.5D, player != null ? player.rotationYaw + 180F : 0F, 0.0F);
        golem.rotationYawHead = golem.rotationYaw;
        world.spawnEntity(golem);
        for (int l = 0; l < 120; ++l)
            world.spawnParticle(EnumParticleTypes.SNOWBALL, (double)pos.getX() + world.rand.nextDouble(), (double)(pos.getY() - 2) + world.rand.nextDouble() * 3.9D, (double)pos.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
    }

    // 1.7.10 had 7 copy-pasted branches; collapsed to one helper (same behavior).
    private void summonTry(World world, BlockPos pos, EntityPlayer player, int gargoyleType, Block arms) {
        BlockPos below = pos.down();
        boolean flagX = block(world, below.west()) == arms && block(world, below.east()) == arms;
        boolean flagZ = block(world, below.north()) == arms && block(world, below.south()) == arms;
        if (!flagX && !flagZ)
            return;

        world.setBlockToAir(pos);
        world.setBlockToAir(below);
        world.setBlockToAir(pos.down(2));
        if (flagX) {
            world.setBlockToAir(below.west());
            world.setBlockToAir(below.east());
        }

        else {
            world.setBlockToAir(below.north());
            world.setBlockToAir(below.south());
        }

        EntityGargoyle entityirongolem = new EntityGargoyle(world);
        entityirongolem.setPlayerCreated(true);
        entityirongolem.assignKindGargoyle(gargoyleType);
        entityirongolem.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY() - 1.95D, (double)pos.getZ() + 0.5D, player != null ? player.rotationYaw + 180F : 0F, 0.0F);
        entityirongolem.rotationYawHead = entityirongolem.rotationYaw;
        entityirongolem.xWaypoint = (double)pos.getX();
        entityirongolem.yWaypoint = (double)pos.getY() - 3;
        entityirongolem.zWaypoint = (double)pos.getZ();
        world.spawnEntity(entityirongolem);
        for (int l = 0; l < 120; ++l) {
            world.spawnParticle(EnumParticleTypes.SNOWBALL, (double)pos.getX() + world.rand.nextDouble(), (double)(pos.getY() - 2) + world.rand.nextDouble() * 3.9D, (double)pos.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    private static Block block(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock();
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.down()).isTopSolid();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    }
}
