package cn.fhyjs.jntm.common;
import cn.fhyjs.jntm.Jntm;

import cn.fhyjs.jntm.block.TileEntityCxkImage;
import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.item.WeaponBase;
import cn.fhyjs.jntm.network.*;
import cn.fhyjs.jntm.registry.DispenserBehaviorRegistryHandler;
import cn.fhyjs.jntm.registry.TileEntityRegistryHandler;
import cn.fhyjs.jntm.utility.FileManager;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import software.bernie.geckolib3.GeckoLib;

import javax.annotation.Nullable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class CommonProxy  {
    public static boolean McInited = false;

    public void regitem_end(){}
    public void onModelRegistry(ModelRegistryEvent event){}
    public static SimpleNetworkWrapper INSTANCE = null;
    public void registerItems(ModelRegistryEvent event) {}
    public static FileManager fileManager;
    public void OpenWB() throws IOException {}
    @Mod.Metadata(Jntm.MODID)
    private static ModMetadata meta;
    public void preInit(FMLPreInitializationEvent event){
        fileManager=new FileManager();
        registerMessage();
        ConfigCore.loadConfig(event);
    }
    public void init(FMLInitializationEvent event){
        // 初始化GeckoLib
        //GeckoLib.initialize();
        McInited=true;
        TileEntityRegistryHandler.reg();
    }
    public void postInit(FMLPostInitializationEvent event){
        DispenserBehaviorRegistryHandler.run();

    }
    public void openhelpGui(GuiScreen e){}

    @Nullable
    public  IAnimationStateMachine loadAsm(ResourceLocation loc, ImmutableMap<String, ITimeValue> parameters){
        return null;
    };

    public void openurl(String s){}
    public static Map<BlockPos,Thread> jimplayers= new HashMap<>();
    public String getCB() throws IOException, UnsupportedFlavorException {return null;}
    private void registerMessage(){
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Jntm.MODID);
        INSTANCE.registerMessage(JntmMessageHandler.class, JntmMessage.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(JntmMessageHandler.class, JntmMessage.class, 0, Side.SERVER);
        INSTANCE.registerMessage(SCINMessageHandler.class, SCINMessage.class, 1, Side.CLIENT);
        INSTANCE.registerMessage(SCINMessageHandler.class, SCINMessage.class, 1, Side.SERVER);
        INSTANCE.registerMessage(Opt_Play_M_Handler.class, Opt_Ply_Message.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(Opt_Play_M_Handler.class, Opt_Ply_Message.class, 2, Side.SERVER);
    }
    public void OpenFE(String e) throws IOException {}


    public void registerItemRenderer(Item weaponBase, int i, String inventory) {
    }

    public void onInitialization(FMLInitializationEvent event) {

    }
}
