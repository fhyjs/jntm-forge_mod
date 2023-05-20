package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.config.ConfigCore;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JntmGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }
    @Override
    public boolean hasConfigGui() {
        return true;
    }
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new  ConfigGuiScreen(parentScreen);
    }
    private static final Set<RuntimeOptionCategoryElement> fmlCategories = ImmutableSet.of(new RuntimeOptionCategoryElement("HELP", "FML"));
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return fmlCategories;
    }
    public static class ConfigGuiScreen extends GuiConfig
    {

        public ConfigGuiScreen(GuiScreen parent)
        {
            super(parent, (new ConfigElement(ConfigCore.cfg.getCategory(ConfigCore.root))).getChildElements(), Jntm.MODID, false, false, Jntm.MODID);
        }
    }

}
