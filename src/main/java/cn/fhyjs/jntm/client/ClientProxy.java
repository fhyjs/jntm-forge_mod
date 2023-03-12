package cn.fhyjs.jntm.client;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import cn.fhyjs.jntm.registry.RenderRegistryHandler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.Display;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class ClientProxy extends CommonProxy {
    @SideOnly(Side.CLIENT)
    @Override
    public void onModelRegistry(ModelRegistryEvent event){
        super.onModelRegistry(event);
    }
    public static List<ModelRegistryObj> modelsToReg = new ArrayList<ModelRegistryObj>();
    public static List<ModelBakeObj> modelsToBake = new ArrayList<ModelBakeObj>();
    public List<StateMapObj> statesToMap = new ArrayList<StateMapObj>();
    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
        Display.setTitle(I18n.translateToLocal("window.jntmtitle.name")+Display.getTitle());

    }
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
    @Override
    @SideOnly(Side.CLIENT)
    public void regitem_end(){
        super.regitem_end();
    }
}