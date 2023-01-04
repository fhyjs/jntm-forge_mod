package cn.fhyjs.jntm.common;
import cn.fhyjs.jntm.Jntm;

import cn.fhyjs.jntm.network.JntmMessage;
import cn.fhyjs.jntm.network.JntmMessageHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

import static com.sun.jna.platform.unix.X11.ClientMessage;

public class CommonProxy {
    public static SimpleNetworkWrapper INSTANCE = null;

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
