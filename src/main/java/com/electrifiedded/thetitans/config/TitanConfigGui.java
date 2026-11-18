package com.electrifiedded.thetitans.config;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TitanConfigGui implements IModGuiFactory {
    private static String id = TheTitans.MODID_V + ".config.";
    public static class TitanGui extends GuiConfig {
        public TitanGui(GuiScreen parent) {
            super(parent, getConfigElements(), TheTitans.MODID_V, false, false, I18n.format(id + "title"));
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> config = new ArrayList<IConfigElement>();
            List<ConfigCategory> categories = new ArrayList<ConfigCategory>();
            categories.add(TheTitans.config.getCategory("general"));
            categories.add(TheTitans.config.getCategory("client"));
            categories.add(TheTitans.config.getCategory("core"));
            categories.add(TheTitans.config.getCategory("blocks"));
            categories.add(TheTitans.config.getCategory("items"));
            categories.add(TheTitans.config.getCategory("mobs"));
            categories.add(TheTitans.config.getCategory("misc"));
            for (int i = 0; i < categories.size(); i++)
                if (categories.get(i).getName().equals("general"))
                config.addAll(new ConfigElement(categories.get(i)).getChildElements());
            else
                config.add(new ConfigElement(categories.get(i)));
            return config;
        }
    }

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new TitanGui(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}
