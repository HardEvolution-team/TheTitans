package com.electrifiedded.thetitans.item.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.api.IBurnable;
import com.electrifiedded.thetitans.api.IReloadable;
import com.electrifiedded.thetitans.registry.TitanMilestones;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemBase extends Item implements IReloadable, IBurnable {
    private List<String> information = new ArrayList<String>();
    protected String achievementV;
    public boolean beCraftedMust;
    private int burnTime;
    /** Plain "element" material that is not craftable (harcadium, void ...). */
    public static ItemBase element(String name, String achievementV, String info) {
        ItemBase item = new ItemBase(name, achievementV, info);
        item.beCraftedMust = false;
        return item;
    }

    /** Plain arrow item in the weapons tab. */
    public static ItemBase arrow(String material, String achievementV) {
        ItemBase item = new ItemBase(material + "_arrow");
        item.setCreativeTab(TheTitans.weaponsPageGiant);
        item.assignAchievement(achievementV);
        return item;
    }

    public ItemBase(String unlocalizedName, String achievementV, String... information) {
        setTranslationKey(unlocalizedName);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName));
        setCreativeTab(TheTitans.materialsPageGiant);
        appendDetails(information);
        assignAchievement(achievementV);
        beCraftedMust = true;
        burnTime = 0;
    }

    public ItemBase(String unlocalizedName, String achievementV) {
        this(unlocalizedName, achievementV, (String)null);
    }

    public ItemBase(String unlocalizedName) {
        this(unlocalizedName, null, (String)null);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.addAll(information);
    }

    public ItemBase appendDetails(String... information) {
        if (information.length > 0)
            for (int i = 0; i < information.length; i++)
            if (information[i] != null)
            this.information.add(information[i]);
        return this;
    }

    public ItemBase assignAchievement(String achievementV) {
        this.achievementV = achievementV;
        return this;
    }

    public String fetchAchievement() {
        return achievementV;
    }

    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (fetchAchievement() != null && beCraftedMust)
            TitanMilestones.runGrant(player, fetchAchievement());
        super.onCreated(stack, world, player);
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (fetchAchievement() != null && !beCraftedMust && !world.isRemote && entity instanceof EntityPlayer)
            TitanMilestones.runGrant((EntityPlayer) entity, fetchAchievement());
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }

    @Override
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
