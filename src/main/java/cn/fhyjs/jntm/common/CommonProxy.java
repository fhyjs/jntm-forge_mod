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


public class CommonProxy  {

    public static SimpleNetworkWrapper INSTANCE = null;


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


}
