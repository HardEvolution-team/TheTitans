package com.electrifiedded.thetitans.item.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNormalIngot extends ItemBase {
    private int color;
    private String iconnameV;
    private String matV;

    public ItemNormalIngot(String unlocalizedName) {
        this(unlocalizedName, -1);
    }

    public ItemNormalIngot(String unlocalizedName, int maincolor) {
        this(unlocalizedName, maincolor, "ingot");
    }

    public ItemNormalIngot(String unlocalizedName, int maincolor, String icon) {
        super(unlocalizedName);
        this.color = maincolor;
        this.iconnameV = icon;
        this.matV = unlocalizedName;
    }

    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal("material." + matV) + (iconnameV.equals("ingot") ? I18n.translateToLocal("dura.ingot") : "");
    }

    @SideOnly(Side.CLIENT)
    public int getColor() {
        return color;
    }
}
