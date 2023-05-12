package cn.fhyjs.jntm.client;
import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;

import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.network.EventHandler;
import cn.fhyjs.jntm.registry.RenderRegistryHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.Display;


import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClientProxy extends CommonProxy {
    @SideOnly(Side.CLIENT)
    @Override
    public void onModelRegistry(ModelRegistryEvent event){
        super.onModelRegistry(event);
    }
    public static List<ModelRegistryObj> modelsToReg = new ArrayList<ModelRegistryObj>();
    public static List<ModelBakeObj> modelsToBake = new ArrayList<ModelBakeObj>();
    @Override
    public String getCB() throws IOException, UnsupportedFlavorException {
        super.getCB();
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
            }
        }
        return ret;
    }
    public Image[] TIs;
    public List<StateMapObj> statesToMap = new ArrayList<StateMapObj>();
    public proadd nt=new proadd();
    public static TrayIcon TIl;
    public  SystemTray tray = SystemTray.getSystemTray();
    @Override
    public void preInit(FMLPreInitializationEvent event)  {
        super.preInit(event);
        Display.setTitle(I18n.translateToLocal("window.jntmtitle.name")+Display.getTitle());
        if(SystemTray.isSupported()&& ConfigCore.isenabledTrayIcon) {

            TIs=new Image[17];
            for (int i=0;i< TIs.length;i++){
                try {
                    Image image = null;
                    image = ImageIO.read(Objects.requireNonNull(Jntm.class.getClassLoader().getResourceAsStream("assets/jntm/textures/gui/st/"+i+".png")));
                    if (image != null) {
                        TIs[i] =image;
                    }
                } catch (IOException | NullPointerException e) {
                    Jntm.logger.error(e);
                }
            }

            TIl=new TrayIcon(TIs[0],Display.getTitle());
            TIl.setImageAutoSize(true);//设置图像自适应
            try {
                tray.add(TIl);
            } catch (AWTException e) {
                Jntm.logger.error(e);
            }
            nt.start();
        }else {
            Jntm.logger.error("SystemTray Is NOT Supported,Or it's disable in config");
        }
        try {
            if (ConfigCore.isenabledRP) {
                GameConfig gc = new GameConfig(Paths.get(getrunpath("") + "options.txt"));
                gc.addResourcePack("jntm.zip", "jntm.zip");
                gc.writeToFile();
                List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao");
                defaultResourcePacks.add(JRP);
                ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), defaultResourcePacks, "field_110449_ao");
                FMLClientHandler.instance().refreshResources();
            }
        } catch (Exception e) {
            Jntm.logger.error(new RuntimeException(e));
        }
    }
    public static Jntm_RP JRP = new Jntm_RP();
    @SideOnly(Side.CLIENT)
    @Override
    public void init(FMLInitializationEvent event){
        super.init(event);


    }
    public static class ModelRegistryObj {
        final Item item;
        final int meta;
        final ModelResourceLocation resource;

        public ModelRegistryObj(Item i, int m, ModelResourceLocation loc) {
            item = i;
            meta = m;
            resource = loc;
        }
    }
    public static class ModelBakeObj {
        final Item item;
        final ResourceLocation resource;
        final ItemMeshDefinition meshDefinition;
        public ModelBakeObj(Item i,  ModelResourceLocation location, ItemMeshDefinition itemMeshDefinition) {
            item = i;
            resource = location;
            meshDefinition = itemMeshDefinition;
        }
    }
    public static class StateMapObj {
        final Block block;
        final StateMapperBase map;
        public StateMapObj(Block b, StateMapperBase mapper) {
            block = b;
            map = mapper;
        }

    }
    @Override
    public void registerItemRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
    @Override
    public void registerItems(ModelRegistryEvent e) {
        super.registerItems(e);
        for(StateMapObj obj : statesToMap) {
            ModelLoader.setCustomStateMapper(obj.block, obj.map);
        }
        for(ModelBakeObj obj : modelsToBake) {
            ModelLoader.setCustomMeshDefinition(obj.item, obj.meshDefinition);
            if(obj.resource == null) {
                NonNullList<ItemStack> subItems = NonNullList.create();
                obj.item.getSubItems(CreativeTabs.SEARCH, subItems);
                for(ItemStack stack : subItems) {
                    ModelBakery.registerItemVariants(obj.item, obj.meshDefinition.getModelLocation(stack));
                }
            }
            else {
                ModelBakery.registerItemVariants(obj.item, obj.resource); // Ensure the custom model is loaded and prevent the default model from being loaded
            }
        }
        for(ModelRegistryObj obj : modelsToReg) {
            ModelLoader.setCustomModelResourceLocation(obj.item, obj.meta, obj.resource);
        }
    }
    @Override
    public void postInit(FMLPostInitializationEvent event){
        super.postInit(event);
        RenderRegistryHandler.register(event);
        EventHandler.postInit = true;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void openhelpGui(GuiScreen e)
    {
        Minecraft.getMinecraft().displayGuiScreen(e);
    }
    @Nullable
    @Override
    public IAnimationStateMachine loadAsm(final ResourceLocation loc, final ImmutableMap<String, ITimeValue> parameters) {
        return ModelLoaderRegistry.loadASM(loc, parameters);
    }
    public String getrunpath(String sp){
        String MP;
        StringBuilder tmp;
        String[] temp;
        temp = Loader.instance().getConfigDir().toURI().toString().split("/");
        tmp = new StringBuilder();
        for (int i = 1; i < temp.length - 1; i++) {
            tmp.append(temp[i]).append("/");
        }
        tmp.append(sp).append("/");
        MP = tmp.toString();
        return MP;
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
    @Override
    @SideOnly(Side.CLIENT)
    public void regitem_end(){
        super.regitem_end();

    }
    class proadd extends Thread{
        @Override
        public void run(){
            while (true){
                for (int i=0;i< TIs.length;i++){
                    TIl.setImage(TIs[i]);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Jntm.logger.error(e);
                    }
                }
            }
        }
    }
}