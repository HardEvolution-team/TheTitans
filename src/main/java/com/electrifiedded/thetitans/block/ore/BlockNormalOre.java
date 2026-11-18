package com.electrifiedded.thetitans.block.ore;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.api.IReloadable;
import com.electrifiedded.thetitans.handler.EventStore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockNormalOre extends BlockOre implements IReloadable {
    private boolean oreGreater;
    protected String materialName;
    public Block cubeBase;
    private Item item;
    private int amount = 1;
    private int experience[] = new int[2];
    public boolean renderItem;
    private Random rand = new Random();

    public BlockNormalOre(String name, int harvestLevel, float hardness, float resistance) {
        this(null, name, harvestLevel, hardness, resistance);
    }

    public BlockNormalOre(Block base, String name, int harvestLevel, float hardness, float resistance) {
        super();
        String unlocalizedName = name + "_ore";
        if (base != null && !base.equals(Blocks.STONE)) {
            cubeBase = base;
            String baseSuffix = cubeBase.getTranslationKey().substring(5).toLowerCase(java.util.Locale.ROOT);
            unlocalizedName += "_" + baseSuffix;
            setHarvestLevel("pickaxe", Math.max(harvestLevel, cubeBase.getHarvestLevel(cubeBase.getDefaultState())));
            setHardness(Math.max(hardness, (float)(Float)TheTitans.reflect.get(Block.class, cubeBase, "blockHardness", "field_149782_v")));
            setResistance(Math.max(resistance, (float)(Float)TheTitans.reflect.get(Block.class, cubeBase, "blockResistance", "field_149781_w")));
        }

        else {
            cubeBase = Blocks.STONE;
            setHarvestLevel("pickaxe", Math.max(harvestLevel, cubeBase.getHarvestLevel(cubeBase.getDefaultState())));
            setHardness(Math.max(hardness, (float)(Float)TheTitans.reflect.get(Block.class, cubeBase, "blockHardness", "field_149782_v")));
            setResistance(Math.max(resistance, (float)(Float)TheTitans.reflect.get(Block.class, cubeBase, "blockResistance", "field_149781_w")));
        }

        materialName = name;
        setCreativeTab(TheTitans.blocksPageGiant);
        setTranslationKey(unlocalizedName);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName));
        setSoundType(SoundType.STONE);
        this.oreGreater = harvestLevel >= 8;
    }


    // ores render host base + transparent <matV>_ore overlay (1.7.10 multi-pass equivalent); CUTOUT layer honours the overlay alpha.
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public net.minecraft.util.BlockRenderLayer getRenderLayer() {
        return net.minecraft.util.BlockRenderLayer.CUTOUT_MIPPED;
    }

    public int quantityDropped(Random arg149745n1) {
        return amount;
    }

    public BlockNormalOre assignDropAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return experience[0] + experience[1] == 0 ? 0 : MathHelper.getInt(rand, experience[0], experience[1]);
    }

    public BlockNormalOre assignDropExp(int experience) {
        assignDropExp(0, experience);
        return this;
    }

    public void assignDropExp(int expMin, int expMax) {
        experience[0] = expMin;
        experience[1] = expMax;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.common.FMLCommonHandler.instance().getMinecraftServerInstance();
        if (this.oreGreater && server != null && !EventStore.getBool(server.getEntityWorld(), "PostWither") && !EventStore.getBool(server.getEntityWorld(), "PostDragon")) {
            return null;
        }

        return item == null ? super.getItemDropped(state, rand, fortune) : item;
    }

    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        if (this.oreGreater && !EventStore.getBool(player.world, "PostWither") && !EventStore.getBool(player.world, "PostDragon")) {
            return false;
        }

        return super.canHarvestBlock(world, pos, player);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        EntityPlayer player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10.0D, false);
        if (this.oreGreater && !EventStore.getBool(worldIn, "PostWither") && !EventStore.getBool(worldIn, "PostDragon")) {
            if (player != null && player.capabilities.isCreativeMode) {
                super.breakBlock(worldIn, pos, state);
            }

            else if (player != null) {
                worldIn.setBlockState(pos, state);
                EventStore.sendMessage(worldIn, TheTitans.translate("event", "pretitan.mineorefail"), player);
            }
        }

        else {
            super.breakBlock(worldIn, pos, state);
        }
    }

    public BlockNormalOre assignDroppedStack(Item item) {
        this.item = item;
        return this;
    }

    public void refreshTextures() {
    }
}
