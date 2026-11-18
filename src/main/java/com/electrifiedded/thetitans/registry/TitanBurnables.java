package com.electrifiedded.thetitans.registry;

import com.electrifiedded.thetitans.api.IBurnable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

import java.util.ArrayList;
import java.util.List;

public class TitanBurnables implements IFuelHandler {
    private final List<IBurnable> fuels = new ArrayList<IBurnable>();

    public TitanBurnables() {
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        for (IBurnable burnable : fuels)
            if (burnable instanceof Item && fuel.getItem().equals((Item) burnable))
            return burnable.getBurnTime();
        else if (burnable instanceof Block && fuel.getItem().equals(Item.getItemFromBlock((Block) burnable)))
            return burnable.getBurnTime();
        return 0;
    }

    public void add(Item... items) {
        for(Item item : items)
            if (item instanceof IBurnable)
            fuels.add((IBurnable) item);
    }

    public void add(Block... blocks) {
        for(Block block : blocks)
            if (block instanceof IBurnable)
            fuels.add((IBurnable) block);
    }
}
