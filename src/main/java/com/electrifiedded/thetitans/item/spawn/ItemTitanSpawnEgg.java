package com.electrifiedded.thetitans.item.spawn;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityExecutorDragon;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTitanSpawnEgg extends Item {
    public static final String[] eggsubsOmegafish = new String[] {
        "", ".desert", ".frost", ".sea", ".poison", ".magma", ".void"
    };
    public static final String[] eggsubsCavespidertitan = new String[] {
        "", ".jesus", ".ore", ".venom", ".ice", ".void"
    };
    public static final String[] eggsubsSpidertitan = new String[] {
        "", ".test", ".mycelium", ".lunar", ".void"
    };
    public static final String[] eggsubsZombietitan = new String[] {
        "", ".jungle", ".blood", ".radioactive", ".void"
    };
    public static final String[] eggsubsSkeletontitan = new String[] {
        "", ".sand", ".guitar", ".sleep", ".void"
    };
    public static final String[] eggsubsCreepertitan = new String[] {
        "", ".hell", ".ghost", ".ender", ".void"
    };
    public static final String[] eggsubsZombiepigmantitan = new String[] {
        "", ".king", ".wither", ".count", ".void"
    };
    public static final String[] eggsubsBlazetitan = new String[] {
        "", ".blizzard", ".acid", ".dirt", ".void"
    };
    public static final String[] eggsubsWitherskeletontitan = new String[] {
        "", ".solar", ".reaper"
    };
    public static final String[] eggsubsGhasttitan = new String[] {
        "", ".lightning", ".obsidian"
    };
    public static final String[] eggsubsEndercolossus = new String[] {
        "", ".holy", ".icey"
    };

    public ItemTitanSpawnEgg(String unlocalizedName) {
        setTranslationKey(unlocalizedName);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName));
        setCreativeTab(TheTitans.birthEggsPageGiant);
        this.setHasSubtypes(true);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack arg77667n1) {
        int numb = 1;
        if (this == TitanGear.enderColossusOvum)
            numb = 3;
        if (this == TitanGear.ghastGiantOvum)
            numb = 3;
        if (this == TitanGear.witherSkeletonGiantOvum)
            numb = 3;
        if (this == TitanGear.blazeGiantOvum)
            numb = 5;
        if (this == TitanGear.zombiePigmanGiantOvum)
            numb = 5;
        if (this == TitanGear.creeperGiantOvum || this == TitanGear.chargedCreeperGiantOvum)
            numb = 5;
        if (this == TitanGear.skeletonGiantOvum)
            numb = 5;
        if (this == TitanGear.zombieGiantOvum)
            numb = 5;
        if (this == TitanGear.spiderGiantOvum)
            numb = 5;
        if (this == TitanGear.caveSpiderGiantOvum)
            numb = 6;
        if (this == TitanGear.omegafishOvum)
            numb = 7;
        int i = MathHelper.clamp(arg77667n1.getItemDamage(), 0, numb);
        String sub = "";
        if (this == TitanGear.enderColossusOvum)
            sub = eggsubsEndercolossus[i];
        if (this == TitanGear.ghastGiantOvum)
            sub = eggsubsGhasttitan[i];
        if (this == TitanGear.witherSkeletonGiantOvum)
            sub = eggsubsWitherskeletontitan[i];
        if (this == TitanGear.blazeGiantOvum)
            sub = eggsubsBlazetitan[i];
        if (this == TitanGear.zombiePigmanGiantOvum)
            sub = eggsubsZombiepigmantitan[i];
        if (this == TitanGear.creeperGiantOvum || this == TitanGear.chargedCreeperGiantOvum)
            sub = eggsubsCreepertitan[i];
        if (this == TitanGear.skeletonGiantOvum)
            sub = eggsubsSkeletontitan[i];
        if (this == TitanGear.zombieGiantOvum)
            sub = eggsubsZombietitan[i];
        if (this == TitanGear.spiderGiantOvum)
            sub = eggsubsSpidertitan[i];
        if (this == TitanGear.caveSpiderGiantOvum)
            sub = eggsubsCavespidertitan[i];
        if (this == TitanGear.omegafishOvum)
            sub = eggsubsOmegafish[i];
        return super.getTranslationKey() + sub;
    }

    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        EntityTitan titan = fetchToSpawnGiant(worldIn);
        if (titan == null) {
            return EnumActionResult.PASS;
        }
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        BlockPos spawnPos = pos.offset(facing);
        double d0 = 0.0D;
        if ((facing == EnumFacing.UP) && (state.getRenderType() == net.minecraft.util.EnumBlockRenderType.MODEL))
            d0 = 0.5D;
        // 1.7.10 checked render type == 11 (special model with reduced height, e.g. snow layer); MODEL is the closest equivalent
        titan.world = worldIn;
        titan.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(titan)), (IEntityLivingData)null);
        titan.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY() + d0, spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
        if (!worldIn.isRemote)
            worldIn.spawnEntity(titan);
        {
            if (this == TitanGear.witherSkeletonGiantOvum || this == TitanGear.witherJockeyGiantOvum) {
                ((EntitySkeletonTitan)titan).setSkeletonType(1);
            }

            if (this == TitanGear.chargedCreeperGiantOvum) {
                ((EntityCreeperTitan)titan).setPowered(true);
            }

            if (this == TitanGear.spiderJockeyGiantOvum || this == TitanGear.witherJockeyGiantOvum) {
                EntitySpiderTitan entity = new EntitySpiderTitan(worldIn);
                entity.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
                entity.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY() + d0, spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
                worldIn.spawnEntity(entity);
                titan.startRiding(entity);
            }

            titan.playLivingSound();
            titan.assignVariantGiant(this.getDamage(stack));
            // TODO 1.7.10 "random.break"; BLOCK_STONE_BREAK is the closest registered SoundEvent
            titan.playSound(SoundEvents.BLOCK_STONE_BREAK, 10000.0F, 0.5F);
            if (!playerIn.capabilities.isCreativeMode)
                stack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }

    public EnumRarity getRarity(ItemStack stack) {
        return TheTitans.titan;
    }

    public EntityTitan fetchToSpawnGiant(World world) {
        if (this == TitanGear.witherzillaOvum)
            return new EntityWitherzilla(world);
        else if (this == TitanGear.executorDragonOvum)
            return new EntityExecutorDragon(world);
        else if (this == TitanGear.ultimaIronGolemGiantOvum)
            return new EntityIronGolemTitan(world);
        else if (this == TitanGear.enderColossusOvum)
            return new EntityEnderColossus(world);
        else if (this == TitanGear.ghastGiantOvum)
            return new EntityGhastTitan(world);
        else if (this == TitanGear.gargoyleKingOvum)
            return new EntityGargoyleTitan(world);
        else if (this == TitanGear.witherSkeletonGiantOvum || this == TitanGear.witherJockeyGiantOvum || this == TitanGear.skeletonGiantOvum || this == TitanGear.spiderJockeyGiantOvum)
            return new EntitySkeletonTitan(world);
        else if (this == TitanGear.creeperGiantOvum || this == TitanGear.chargedCreeperGiantOvum)
            return new EntityCreeperTitan(world);
        else if (this == TitanGear.zombieGiantOvum)
            return new EntityZombieTitan(world);
        else if (this == TitanGear.spiderGiantOvum)
            return new EntitySpiderTitan(world);
        else if (this == TitanGear.caveSpiderGiantOvum)
            return new EntityCaveSpiderTitan(world);
        else if (this == TitanGear.blazeGiantOvum)
            return new EntityBlazeTitan(world);
        else if (this == TitanGear.zombiePigmanGiantOvum)
            return new EntityPigZombieTitan(world);
        else if (this == TitanGear.omegafishOvum)
            return new EntitySilverfishTitan(world);
        else if (this == TitanGear.magmaCubeGiantOvum)
            return new EntityMagmaCubeTitan(world);
        else if (this == TitanGear.slimeGiantOvum)
            return new EntitySlimeTitan(world);
        else if (this == TitanGear.snowGolemGiantOvum)
            return new EntitySnowGolemTitan(world);
        else
            return null;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab))
            return;
        int numb = 1;
        if (this == TitanGear.enderColossusOvum)
            numb = 3;
        if (this == TitanGear.ghastGiantOvum)
            numb = 3;
        if (this == TitanGear.witherSkeletonGiantOvum)
            numb = 3;
        if (this == TitanGear.skeletonGiantOvum)
            numb = 5;
        if (this == TitanGear.creeperGiantOvum || this == TitanGear.chargedCreeperGiantOvum)
            numb = 5;
        if (this == TitanGear.zombieGiantOvum)
            numb = 5;
        if (this == TitanGear.spiderGiantOvum)
            numb = 5;
        if (this == TitanGear.caveSpiderGiantOvum)
            numb = 6;
        if (this == TitanGear.blazeGiantOvum)
            numb = 5;
        if (this == TitanGear.zombiePigmanGiantOvum)
            numb = 5;
        if (this == TitanGear.omegafishOvum)
            numb = 7;
        for (int i = 0; i < numb; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }
}
