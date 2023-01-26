package cn.fhyjs.jntm.client;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.registry.RenderRegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.example.ScreenCfg;
import net.montoyo.mcef.utilities.Log;
import org.lwjgl.opengl.Display;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.function.Predicate;


public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
        Display.setTitle(I18n.translateToLocal("window.jntmtitle.name")+Display.getTitle());
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void init(FMLInitializationEvent event){
        super.init(event);
        RenderRegistryHandler.register();

    }

    @Override
    public void postInit(FMLPostInitializationEvent event){
        super.postInit(event);

    }
    @SideOnly(Side.CLIENT)
    @Override
    public void openhelpGui(GuiScreen e)
    {
        Minecraft.getMinecraft().displayGuiScreen(e);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void openurl(String s){
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(URI.create(s));
                }
            }
        } catch (IOException | InternalError e) {
            e.printStackTrace();
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void OpenFE(String e) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        File dirToOpen = null;
        dirToOpen = new File(e);
        desktop.open(dirToOpen);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void OpenWB() throws IOException { }

}