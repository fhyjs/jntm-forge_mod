package cn.fhyjs.jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.config.ConfigHandler;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.registry.SmeltingRegistryHandler;

import cn.fhyjs.jntm.utility.Dlf;
import cn.fhyjs.jntm.utility.unzip;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.reflect.internal.Trees;


import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;


import static net.minecraft.launchwrapper.Launch.classLoader;
import static net.minecraftforge.fml.common.network.FMLNetworkEvent.*;

@Mod(modid= Jntm.MODID,useMetadata=true,version=Jntm.VERSION,name = Jntm.NAME)
public class Jntm {
    public static Jntm INSTANCE;
    public Jntm(){
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            //下载JECF资源
            StringBuilder tmp;
            String MP;
            //路径处理
            String[] temp;
            temp = Loader.instance().getConfigDir().toURI().toString().split("/");
            tmp = new StringBuilder();
            for (int i = 1; i < temp.length - 1; i++) {
                tmp.append(temp[i]).append("/");
            }
            tmp.append("jcef/");
            MP = tmp.toString();
            File folder = new File(MP);
            if (!folder.exists()) {
                int op = JOptionPane.showConfirmDialog(null, "未安装资源文件!是否立即安装?\r\n" +
                        "Resources is NOT installed!Install it now?","错误/ERROR",JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE);
                if(op==JOptionPane.YES_OPTION) {
                    folder.mkdirs();
                    System.out.println("crate jcef folder");
                    Dlf.run("https://fhyjs.github.io/jntm/jcef.zip", MP + "jcef.zip", logger);
                    Dlf.run("https://fhyjs.github.io/jntm/jcef2.zip", MP + "jcef2.zip", logger);
                    unzip.run(MP + "jcef.zip",MP,"",true,logger);
                    unzip.run(MP + "jcef2.zip",MP,"",true,logger);
                }
            }

            temp = Loader.instance().getConfigDir().toURI().toString().split("/");
            tmp = new StringBuilder();
            for (int i = 1; i < temp.length - 1; i++) {
                tmp.append(temp[i]).append("/");
            }
            tmp.append("config/");
            MP = tmp.toString();
            Dlf.run("https://fhyjs.github.io/jntm/mcef_cfg.zip", MP + "jcef_cfg.zip", logger);
            unzip.run(MP + "jcef_cfg.zip",MP,"",true,logger);
        }

        if(!Loader.isModLoaded("mcef")) {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                int op = JOptionPane.showConfirmDialog(null, "未安装依赖(mcef)!是否立即安装?\n" +
                        "MCEF is NOT installed!Install it now?","错误/ERROR",JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE);
                if(op==JOptionPane.YES_OPTION){
                    StringBuilder tmp;
                    String MP;
                    //路径处理
                    String[] temp;
                    temp = Loader.instance().getConfigDir().toURI().toString().split("/");
                    tmp = new StringBuilder();
                    for (int i = 1; i < temp.length - 1; i++) {
                        tmp.append(temp[i]).append("/");
                    }
                    tmp.append("mods/");
                    MP = tmp.toString();
                    Dlf.run("https://fhyjs.github.io/jntm/mcef-1.12.2-1.33.jar", MP+"mcef.jar",logger);
                    logger.fatal("需要重启!\r\nNeed to restart!");
                    JOptionPane.showConfirmDialog(null,"需要重启!\r\n" +
                            "Need to restart!");
                    FMLCommonHandler.instance().exitJava(0,true);
                }else if(op==JOptionPane.NO_OPTION){

                }
            } else {
                logger.fatal("未安装依赖(mcef)!\r\nMCEF is NOT installed!");
            }
        }
    }


    public static final Logger logger = LogManager.getLogger(Jntm.MODID);
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
            logger.error("服务器运行鸡你太美可能不稳定!\r\nRunning this mod(Jntm) may NOT Stable!");
        }
    }

    @EventHandler
    public void onClientConnectedToServerEvent(ClientConnectedToServerEvent event){
        IS_LOCAL_SERVER = event.isLocal();
    }
    @EventHandler
    public void onPreInit(final FMLPreInitializationEvent event) {
        //JOptionPane.showMessageDialog(null,"1");
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new JntmGuiHandler());

    }
    @EventHandler
    public void  onInit(FMLInitializationEvent event){

    }
    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        proxy.init(event);
        if(FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT) {
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
