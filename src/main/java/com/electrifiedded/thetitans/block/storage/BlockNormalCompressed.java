package com.electrifiedded.thetitans.block.storage;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.api.IBurnable;
import com.electrifiedded.thetitans.api.IReloadable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockNormalCompressed extends Block implements IReloadable, IBurnable {
    private int burnTime;

    public BlockNormalCompressed(MapColor color, String name) {
        super(Material.IRON, color);
        setHardness(3.0F);
        setResistance(5.0F);
        setSoundType(SoundType.METAL);
        setCreativeTab(TheTitans.blocksPageGiant);
        setTranslationKey(name);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, name));
    }

    @Override
    public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon) {
        return true;
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
