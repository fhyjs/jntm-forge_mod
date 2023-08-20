package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.ItemGroup.jntmGroup;
import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.compat.tlm.ItemRopeEmptier;
import cn.fhyjs.jntm.item.*;
import cn.fhyjs.jntm.item.LandminePlugins.CamouflageUpgrade;
import cn.fhyjs.jntm.item.LandminePlugins.ExplosionUpgrade;
import cn.fhyjs.jntm.item.LandminePlugins.WatcherUpgrade;
import cn.fhyjs.jntm.item.cards.CustomCard;
import cn.fhyjs.jntm.item.weapon.Gohei;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.DanmakuTemplate;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.subentity.SubEntityType;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibSubEntities;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
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
    public static Fsms xiguaegg =new Fsms("xigua");
    public static Fsms fsms =new Fsms("bosscxk");
    public final static Fsms heliEgg =new Fsms("heli");
    public final static Fsms dlwEgg =new Fsms("dlw");
    public static ggxdd ggxdd = new ggxdd();
    public static Tickstop tickstop = new Tickstop();
    public static JGPDTEX jgpdtex =new JGPDTEX();
    public static rawkr rawkr = new rawkr(3,3,true);
    public static final ItemBlock cookedcxk_item = new ItemBlock(BlockRegistryHandler.BLOCK_cookedcxk);
    public static final ItemBlock ITEM_CxkTNT = new ItemBlock(BlockRegistryHandler.BLOCK_CxkTnt);
    public static final ItemBlock JimplayerBlock = new ItemBlock(BlockRegistryHandler.JIM_PLAYER_BLOCK);
    public static final ItemBlock ItemLandmine = new ItemBlock(BlockRegistryHandler.blockLandmine);
    public static final ItemBlock ItemLandmineConf = new ItemBlock(BlockRegistryHandler.blockLandmineConfigurator);
    public static music_xjj music_xjj,music_kdj;
    public static Jntm_help_item jntmHelpItem = new Jntm_help_item();
    public static Jiguangpao JGP = new Jiguangpao();
    public  static final Ji_Games JI_GAMES = new Ji_Games();
    public static final ItemBlock CXKIMAGE = new ItemBlock(BlockRegistryHandler.CXKIMAGE);
    public static final ItemBlock PmxModelBlockItem = new ItemBlock(BlockRegistryHandler.blockPmxModel);
    public static final ItemBlock IJCT = new ItemBlock(BlockRegistryHandler.JI_CRAFTING_TABLE);
    public static final InsJvav INS_JVAV=new InsJvav();
    public static final Danmaku_Gun DANMAKU_GUN = new Danmaku_Gun();
    public  static final Ji_Armor JI_ARMOR_1 = new Ji_Armor(EntityEquipmentSlot.HEAD);
    public  static final Ji_Armor JI_ARMOR_2 = new Ji_Armor(EntityEquipmentSlot.CHEST);
    public  static final Ji_Armor JI_ARMOR_3 = new Ji_Armor(EntityEquipmentSlot.LEGS);
    public  static final Ji_Armor JI_ARMOR_4 = new Ji_Armor(EntityEquipmentSlot.FEET);
    public  static final WatcherUpgrade watcherUpgrade = new WatcherUpgrade();
    public  static final ExplosionUpgrade explosionUpgrade = new ExplosionUpgrade();
    public  static final CamouflageUpgrade camouflageUpgrade = new CamouflageUpgrade();
    public  static final ItemRope itemRope = new ItemRope();
    public  static final ItemRopeEmptier itemRopeEmptier = new ItemRopeEmptier();
    public  static Item Gouhei;
    public  static Item EXPLOSION_CARD;
    public  static Item Fire_CARD;

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Item> event){
        JimplayerBlock.setRegistryName(Objects.requireNonNull(JimplayerBlock.getBlock().getRegistryName()));
        PmxModelBlockItem.setRegistryName(Objects.requireNonNull(PmxModelBlockItem.getBlock().getRegistryName()));
        ItemLandmine.setRegistryName(Objects.requireNonNull(ItemLandmine.getBlock().getRegistryName()));
        ItemLandmineConf.setRegistryName(Objects.requireNonNull(ItemLandmineConf.getBlock().getRegistryName()));
        IJCT.setRegistryName(Objects.requireNonNull(IJCT.getBlock().getRegistryName()));
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
        registry.register(IJCT);
        registry.register(ITEM_CxkTNT);
        registry.register(CXKIMAGE);
        registry.register(INS_JVAV);
        registry.register(fsms);
        registry.register(DANMAKU_GUN);
        registry.register(JI_ARMOR_1);
        registry.register(JI_ARMOR_2);
        registry.register(JI_ARMOR_3);
        registry.register(JI_ARMOR_4);
        registry.register(xiguaegg);
        registry.register(JI_GAMES);
        registry.register(JimplayerBlock);
        if (Jntm.IS_DC_Load){
            Gouhei=new Gohei("go_hei", jntmGroup.jntm_Group);
            EXPLOSION_CARD = new CustomCard("explosion_card", jntmGroup.jntm_Group) {
                @Override
                public SubEntityType getSubentity() {
                    return LibSubEntities.EXPLOSION;
                }

                @Override
                public DanmakuTemplate.Builder reShape(DanmakuTemplate.Builder builder) {
                    return builder.setShot(builder.shot().addSubEntityProperty("explosion_strength", 1d));
                }
            };
            Fire_CARD = new CustomCard("fire_card", jntmGroup.jntm_Group) {
                @Override
                public SubEntityType getSubentity() {
                    return LibSubEntities.FIRE;
                }

                @Override
                public DanmakuTemplate.Builder reShape(DanmakuTemplate.Builder builder) {
                    return builder.setShot(builder.shot().addSubEntityProperty("explosion_strength", 1d));
                }
            };
            registry.register(Gouhei);
            registry.register(EXPLOSION_CARD);
            //registry.register(Fire_CARD);
        }
        registry.register(tickstop);
        registry.register(ItemLandmine);
        registry.register(ItemLandmineConf);
        registry.register(watcherUpgrade);
        registry.register(explosionUpgrade);
        registry.register(camouflageUpgrade);
        registry.register(PmxModelBlockItem);
        registry.register(itemRope);
        if (Loader.isModLoaded("touhou_little_maid")){
            registry.register(itemRopeEmptier);
        }
        registry.register(heliEgg);
        if (Loader.isModLoaded("geckolib3")){
            registry.register(dlwEgg);
        }
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
        registryModel(IJCT);
        registryModel(ITEM_CxkTNT);
        registryModel(INS_JVAV);
        registryModel(fsms);
        registryModel(DANMAKU_GUN);
        registryModel(xiguaegg);
        ModelLoader.setCustomModelResourceLocation(CXKIMAGE,0,new ModelResourceLocation("jntm:cii","inventory"));
        registryModel(JI_GAMES);
        registryModel(JimplayerBlock);
        if (Jntm.IS_DC_Load){
            registryModel(Gouhei);
            registryModel(EXPLOSION_CARD);
            //registryModel(Fire_CARD);
        }
        registryModel(tickstop);
        registryModel(ItemLandmine);
        registryModel(ItemLandmineConf);
        registryModel(watcherUpgrade);
        registryModel(explosionUpgrade);
        registryModel(camouflageUpgrade);
        registryModel(PmxModelBlockItem);
        registryModel(itemRope);
        if (Loader.isModLoaded("touhou_little_maid")){
           registryModel(itemRopeEmptier);
        }
        registryModel(heliEgg);
        if (Loader.isModLoaded("geckolib3")){
            registryModel(dlwEgg);
        }
    }
    @SideOnly(Side.CLIENT)
    private static void registryModel(Item item){
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),"inventory");
        ModelLoader.setCustomModelResourceLocation(item,0,modelResourceLocation);
    }
}
