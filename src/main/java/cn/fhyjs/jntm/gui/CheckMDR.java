package cn.fhyjs.jntm.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.Loader;

public class CheckMDR extends GuiContainer {
    public CheckMDR() {
        super(new Jvav_C());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
    }
    public static boolean isInstallMMDR(){
        return Loader.isModLoaded("kaimyentity");
    }
}
