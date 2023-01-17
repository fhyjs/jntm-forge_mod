package cn.fhyjs.jntm.common;
import cn.fhyjs.jntm.Jntm;

import cn.fhyjs.jntm.gui.Ji_games_GUI;
import cn.fhyjs.jntm.network.JntmMessage;
import cn.fhyjs.jntm.network.JntmMessageHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.example.BrowserScreen;

import java.io.IOException;
import java.util.function.Predicate;


public class CommonProxy implements ISelectiveResourceReloadListener, IDisplayHandler, IJSQueryHandler {

    public static SimpleNetworkWrapper INSTANCE = null;
    public Object hudBrowser;

    public void OpenWB() throws IOException {}
    public void preInit(FMLPreInitializationEvent event){
        registerMessage();
    }
    public void init(FMLInitializationEvent event){
    }
    public void postInit(FMLPostInitializationEvent event){}
    public void openhelpGui(GuiScreen e){}
    public void openurl(String s){}
    private void registerMessage(){
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Jntm.MODID);
        INSTANCE.registerMessage(JntmMessageHandler.class, JntmMessage.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(JntmMessageHandler.class, JntmMessage.class, 0, Side.SERVER);
    }
    public void OpenFE(String e) throws IOException {}

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {

    }

    @Override
    public void onAddressChange(IBrowser iBrowser, String s) {

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
    public boolean handleQuery(IBrowser iBrowser, long l, String s, boolean b, IJSQueryCallback ijsQueryCallback) {
        return false;
    }

    @Override
    public void cancelQuery(IBrowser iBrowser, long l) {

    }

    public API getAPI() {
        return null;
    }

    public boolean hasBackup() {
        return false;
    }

    public void setBackup(Object bu) {
    }
}
