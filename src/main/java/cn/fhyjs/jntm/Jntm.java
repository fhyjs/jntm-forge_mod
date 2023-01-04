package cn.fhyjs.jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.config.ConfigHandler;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.registry.RenderRegistryHandler;
import cn.fhyjs.jntm.registry.SmeltingRegistryHandler;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static net.minecraft.launchwrapper.Launch.classLoader;
import static net.minecraftforge.fml.common.network.FMLNetworkEvent.*;

@Mod(modid= Jntm.MODID,useMetadata=true,version=Jntm.VERSION,name = Jntm.NAME)
public class Jntm {
    public static final String MODID = "jntm";
    public static final String NAME = "jntm";
    public static final String VERSION = "11.45.14";
    public static boolean IS_LOCAL_SERVER;
    @SidedProxy(clientSide = "cn.fhyjs.jntm.client.ClientProxy",serverSide = "cn.fhyjs.jntm.common.CommonProxy")
    public static CommonProxy proxy;
    @Instance(Jntm.MODID)
    public static Jntm instance;
    @EventHandler
    public static void preInit(FMLPreInitializationEvent event){
        proxy.preInit(event);
        if(FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT) {

        }
        else {
            JOptionPane.showMessageDialog(null,"在服务器端运行鸡你太美，可能会导致一些问题，暂未修复！\r\n禁用了一些功能以稳定运行");
        }
    }

    @EventHandler
    public void ClientConnectedToServerEvent(ClientConnectedToServerEvent event){
        IS_LOCAL_SERVER = event.isLocal();
    }
    @EventHandler
    public void onPreInit(final FMLPreInitializationEvent event) {
        //JOptionPane.showMessageDialog(null,"1");
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new JntmGuiHandler());

    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        proxy.init(event);

        if(FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT) {
            RenderRegistryHandler.register();
            Display.setTitle(I18n.translateToLocal("window.jntmtitle.name")+Display.getTitle());
        }
        SmeltingRegistryHandler.register();
    }
    @EventHandler
    @SideOnly(Side.CLIENT)
    public void preInitClient(FMLPreInitializationEvent event){

    }
    @EventHandler
    public static void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
    @EventHandler
    public static void onInitialization(FMLInitializationEvent event) {
        ConfigHandler.setFile("jntm.cfg");
    }
}
