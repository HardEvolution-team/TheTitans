package com.electrifiedded.thetitans.item.spawn;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.item.misc.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.HashMap;
import java.util.Iterator;

public class ItemSpawnEggRegular extends ItemBase {
    public ItemSpawnEggRegular() {
        super("spawn_egg");
        setHasSubtypes(true);
        setCreativeTab(TheTitans.birthEggsPageGiant);
    }

    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(ItemStack stack) {
        ModContainer container = FMLCommonHandler.instance().findContainerFor(TheTitans.modInstance);
        EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(container, stack.getItemDamage());
        return "Spawn " + ((registration == null) ? "Unknown" : I18n.translateToLocal("entity.thetitans." + registration.getEntityName() + ".name"));
    }

    // egg color now applied via IItemColor (registered in ClientProxy). Layer 0 = base, layer 1 = spots.
    public int getColor(ItemStack stack, int tintIndex) {
        EggColors c = colorsTableOvum.get(Integer.valueOf(stack.getMetadata()));
        if (c == null)
            return 0xFFFFFF;
        return tintIndex == 0 ? c.primaryColor : c.secondaryColor;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
            return EnumActionResult.SUCCESS;
        BlockPos spawnPos = pos.offset(facing);
        double addY = 0.0D;
        if (facing == EnumFacing.UP && !world.isAirBlock(pos) && world.getBlockState(pos).isFullCube()) {
            addY = 0.5D;
        }

        EntityLiving living = spawnMobCreature(world, stack.getItemDamage(), spawnPos.getX() + 0.5D, spawnPos.getY() + addY, spawnPos.getZ() + 0.5D);
        if (living != null) {
            if (stack.hasDisplayName()) {
                living.setCustomNameTag(stack.getDisplayName());
            }

            if (player.isSneaking() && !player.onGround) {
                if (living instanceof EntitySkeletonMinion)
                    ((EntitySkeletonMinion)living).setSkeletonType(1);
                living.setHealth(living.getMaxHealth());
            }

            if (!player.capabilities.isCreativeMode)stack.shrink(1);
            if (player.isSneaking()) {
                for (int i = 0; i <= 9; i++) {
                    EntityLiving living1 = spawnMobCreature(world, stack.getItemDamage(), spawnPos.getX() + 0.5D, spawnPos.getY() + addY, spawnPos.getZ() + 0.5D);
                    if (living1 != null) {
                        if (stack.hasDisplayName()) {
                            living1.setCustomNameTag(stack.getDisplayName());
                        }

                        if (player.isSneaking() && !player.onGround) {
                            if (living1 instanceof EntitySkeletonMinion)
                                ((EntitySkeletonMinion)living1).setSkeletonType(1);
                            living1.setHealth(living1.getMaxHealth());
                        }
                    }
                }
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public static EntityLiving spawnMobCreature(World world, int id, double x, double y, double z) {
        if (world.isRemote) {
            return null;
        }

        ModContainer container = FMLCommonHandler.instance().findContainerFor(TheTitans.modInstance);
        EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(container, id);
        Entity entity = createEntity(registration.getEntityClass(), world);
        if ((entity != null) && ((entity instanceof EntityLiving))) {
            EntityLiving living = (EntityLiving)entity;
            living.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            living.rotationYawHead = living.rotationYaw;
            living.renderYawOffset = living.rotationYaw;
            living.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(living)), null);
            world.spawnEntity(living);
            living.playLivingSound();
            return living;
        }

        return null;
    }

    public static Entity createEntity(Class<? extends Entity> clazz, World world) {
        if (clazz == null) {
            return null;
        }

        Entity entity = null;
        try {
            entity = clazz.getConstructor(new Class[] {
                World.class
            }
            ).newInstance(new Object[] {
                world
            }
            );
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab))
            return;
        Iterator<Integer> iterator = colorsTableOvum.keySet().iterator();
        while (iterator.hasNext()) {
            items.add(new ItemStack(this, 1, iterator.next().intValue()));
        }
    }

    @SuppressWarnings("unlikely-arg-type")
    public static void appendEgg(Class<? extends Entity> clazz, int color1, int color2) {
        if (colorsTableOvum.containsKey(clazz)) {
            throw new IllegalArgumentException("Mapping for that entity exists already!");
        }

        EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(clazz, false);
        colorsTableOvum.put(Integer.valueOf(registration.getModEntityId()), new EggColors(color1, color2));
    }

    public static HashMap<Integer, EggColors> colorsTableOvum = new HashMap<Integer, EggColors>();
    public static class EggColors {
        public int primaryColor;
        public int secondaryColor;
        public EggColors(int color1, int color2) {
            this.primaryColor = color1;
            this.secondaryColor = color2;
        }
    }
}
