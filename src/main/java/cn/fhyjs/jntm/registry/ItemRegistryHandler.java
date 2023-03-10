package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.Cxkimage;
import cn.fhyjs.jntm.item.*;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.IOException;
import java.util.Objects;

import static cn.fhyjs.jntm.Jntm.proxy;
import static cn.fhyjs.jntm.registry.SoundEventRegistryHandler.arr_2;
import static cn.fhyjs.jntm.registry.SoundEventRegistryHandler.prefix;

@Mod.EventBusSubscriber
public class ItemRegistryHandler {
    public static Fsms fsms =new Fsms("bosscxk");
    public static ggxdd ggxdd = new ggxdd();
    public static JGPDTEX jgpdtex =new JGPDTEX();
    public static rawkr rawkr = new rawkr(3,3,true);
    public static final ItemBlock cookedcxk_item = new ItemBlock(BlockRegistryHandler.BLOCK_cookedcxk);
    public static final ItemBlock ITEM_CxkTNT = new ItemBlock(BlockRegistryHandler.BLOCK_CxkTnt);
    public static music_xjj music_xjj,music_kdj;
    public static Jntm_help_item jntmHelpItem = new Jntm_help_item();
    public static Jiguangpao JGP = new Jiguangpao();
    public  static final Ji_Games JI_GAMES = new Ji_Games();
    public static final ItemBlock CXKIMAGE = new ItemBlock(BlockRegistryHandler.CXKIMAGE);
    public static final InsJvav INS_JVAV=new InsJvav();
    public static final Danmaku_Gun DANMAKU_GUN = new Danmaku_Gun();

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Item> event){

        IForgeRegistry<Item> registry = event.getRegistry();
        cookedcxk_item.setRegistryName(Objects.requireNonNull(cookedcxk_item.getBlock().getRegistryName()));
        ITEM_CxkTNT.setRegistryName(Objects.requireNonNull(ITEM_CxkTNT.getBlock().getRegistryName()));
        CXKIMAGE.setRegistryName(Objects.requireNonNull(CXKIMAGE.getBlock().getRegistryName()));
        registry.register(cookedcxk_item);
        registry.register(ggxdd);
        SoundEventRegistryHandler.music_xjj = (SoundEvent)new SoundEvent(new ResourceLocation(prefix, arr_2[2])).setRegistryName(arr_2[2]);
        music_xjj = new music_xjj("jntm_xjj",SoundEventRegistryHandler.music_xjj);
        registry.register(music_xjj);
        registry.register(rawkr);
        SoundEventRegistryHandler.music_kdj = (SoundEvent)new SoundEvent(new ResourceLocation(prefix, arr_2[3])).setRegistryName(arr_2[3]);
        music_kdj = new music_xjj("jntm_kdj",SoundEventRegistryHandler.music_kdj);
        registry.register(music_kdj);
        registry.register(jntmHelpItem);
        registry.register(JGP);
        registry.register(jgpdtex);
        //registry.register(JI_GAMES);
        registry.register(ITEM_CxkTNT);
        registry.register(CXKIMAGE);
        registry.register(INS_JVAV);
        registry.register(fsms);
        registry.register(DANMAKU_GUN);
        proxy.regitem_end();
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) throws IOException {
        registryModel(ggxdd);
        registryModel(cookedcxk_item);
        registryModel(music_xjj);
        registryModel(music_kdj);
        registryModel(rawkr);
        registryModel(jntmHelpItem);
        registryModel(JGP);
        registryModel(jgpdtex);
        //registryModel(JI_GAMES);
        registryModel(ITEM_CxkTNT);
        registryModel(INS_JVAV);
        registryModel(fsms);
        registryModel(DANMAKU_GUN);
        ModelBakery.registerItemVariants(CXKIMAGE, new ResourceLocation(Jntm.MODID, "danmakugan"));
        
        //IItemRendererHandler.register(CXKIMAGE, new IItemRenderer);
        //ClientProxy.modelsToReg.add(new ClientProxy.ModelRegistryObj(CXKIMAGE, 0, new ModelResourceLocation("jntm:dynamic/cxkimage_1")));
        //ClientProxy.modelsToBake.add(new ClientProxy.ModelBakeObj(CXKIMAGE,new ModelResourceLocation("jntm:/dynamic/cxkimage_1"), CXKIMAGE.getMeshDefinition()));
        //ModelLoader.setCustomModelResourceLocation(CXKIMAGE,0,);
        //proxy.registerItems(event);
    }
    @SideOnly(Side.CLIENT)
    private static void registryModel(Item item){
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),"inventory");
        ModelLoader.setCustomModelResourceLocation(item,0,modelResourceLocation);
    }
}
