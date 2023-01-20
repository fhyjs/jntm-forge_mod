package cn.fhyjs.jntm.client;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.registry.RenderRegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.common.MinecraftForge;
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

public class ClientProxy extends CommonProxy implements ISelectiveResourceReloadListener, IDisplayHandler, IJSQueryHandler {
    //---browser---
    private Minecraft mc;
    private net.montoyo.mcef.api.API mcef;

    public ScreenCfg hudBrowser = null;
    @Override
    public void setBackup(Object bu) {
        this.backup = bu;
    }
    @Override
    public boolean hasBackup() {
        return this.backup != null;
    }
    private Object backup = null;
    public net.montoyo.mcef.api.API getAPI() {
        return mcef;
    }
    //---browser---
    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
        Display.setTitle(I18n.translateToLocal("window.jntmtitle.name")+Display.getTitle());
        //---browser---
        mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(this);

        mcef = MCEFApi.getAPI();
        if(mcef != null)
            mcef.registerScheme("jntm", WDScheme.class, true, false, false, true, true, false, false);
        //---browser---
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
        //---browser---
        if(mcef == null)
            throw new RuntimeException("MCEF is missing");

        mcef.registerDisplayHandler(this);
        mcef.registerJSQueryHandler(this);
        //---browser---
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
    //---browser---
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        Log.info("Resource manager reload: clearing GUI cache...");
    }

    @Override
    public void onAddressChange(IBrowser browser, String url) {
        if(browser != null) {

        }
    }

    @Override
    public void onTitleChange(IBrowser iBrowser, String s) {

    }

    @Override
    public void onTooltip(IBrowser iBrowser, String s) {

    }

    @Override
    public void onStatusMessage(IBrowser iBrowser, String s) {

    }

    @Override
    public boolean handleQuery(IBrowser browser, long queryId, String query, boolean persistent, IJSQueryCallback cb) {
        if(browser != null && persistent && query != null && cb != null) {
            query = query.toLowerCase();

            if(query.startsWith("webdisplays_")) {
                query = query.substring(12);

                String args;
                int parenthesis = query.indexOf('(');
                if(parenthesis < 0)
                    args = null;
                else {
                    if(query.indexOf(')') != query.length() - 1) {
                        cb.failure(400, "Malformed request");
                        return true;
                    }

                    args = query.substring(parenthesis + 1, query.length() - 1);
                    query = query.substring(0, parenthesis);
                }
/*
                if(jsDispatcher.canHandleQuery(query))
                    jsDispatcher.enqueueQuery(browser, query, args, cb);
                else
                    cb.failure(404, "Unknown WebDisplays query");
*/
                return true;
            }
        }

        return false;
    }

    @Override
    public void cancelQuery(IBrowser iBrowser, long l) {

    }
    //---browser---
}