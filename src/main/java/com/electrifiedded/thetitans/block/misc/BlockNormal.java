package com.electrifiedded.thetitans.block.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.api.IBurnable;
import com.electrifiedded.thetitans.api.IReloadable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockNormal extends Block implements IReloadable, IBurnable {
    private Item item;
    private int amount = 1;
    private int experience[] = new int[2];
    private int burnTime;
    private Random rand = new Random();

    public BlockNormal(String name, int harvestLevel, float hardness, float resistance) {
        super(Material.ROCK);
        setHarvestLevel("pickaxe", harvestLevel);
        setCreativeTab(TheTitans.blocksPageGiant);
        setTranslationKey(name);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, name));
        setSoundType(SoundType.STONE);
        setHardness(hardness);
        setResistance(resistance);
    }

    public int quantityDropped(Random arg149745n1) {
        return amount;
    }

    public void assignDropAmount(int amount) {
        this.amount = amount;
    }

    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return experience[0] + experience[1] == 0 ? 0 : MathHelper.getInt(rand, experience[0], experience[1]);
    }

    public void assignDropExp(int experience) {
        assignDropExp(0, experience);
    }

    public void assignDropExp(int expMin, int expMax) {
        experience[0] = expMin;
        experience[1] = expMax;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return item == null ? super.getItemDropped(state, rand, fortune) : item;
    }

    public void assignDroppedStack(Item item) {
        this.item = item;
    }

    public void refreshTextures() {
    }

    @Override
    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime() {
        return burnTime;
    }
}
