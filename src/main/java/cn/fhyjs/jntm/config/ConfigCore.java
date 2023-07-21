package cn.fhyjs.jntm.config;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

import java.util.List;

public class ConfigCore {
    public static final String root = "root";
    public static final String general = root+".general";
    public static final String client = root+".client";
    public static Configuration cfg;
    public static boolean isenabledTrayIcon = true;
    public static boolean isenabledRP = true;
    private static boolean orp;
    public static boolean isenabledUP=false;
    public static boolean isenabledTelnet=true;

    public static void loadConfig(FMLPreInitializationEvent event) {
        // net.minecraftforge.common.config.Configurationのインスタンスを生成する。
        cfg = new Configuration(event.getSuggestedConfigurationFile(), Jntm.VERSION, true);
        // 初期化する。
        initConfig();
        // コンフィグファイルの内容を変数と同期させる。
        syncConfig();
    }


    /** コンフィグを初期化する。 */
    private static void initConfig() {
        // カテゴリのコメントなどを設定する。
        // General
        cfg.addCustomCategoryComment(root, "鸡你太美mod配置.");
        cfg.setCategoryLanguageKey(root, "config.jntm.category.root");
        cfg.addCustomCategoryComment(client, "鸡你太美mod客户端配置.");
        cfg.setCategoryLanguageKey(client, "config.jntm.category.client");
        cfg.addCustomCategoryComment(general, "鸡你太美mod通用设置.");
        cfg.setCategoryLanguageKey(general, "config.jntm.category.general");
        orp = isenabledRP;
        // Difficulty
        //cfg.addCustomCategoryComment(DIFFICULTY, "The settings of difficulty.");
        //cfg.setCategoryLanguageKey(DIFFICULTY, "config.aluminium.category.difficulty");
        //cfg.setCategoryRequiresMcRestart(DIFFICULTY, true);
    }


    /** コンフィグを同期する。 */
    public static void syncConfig() {
        Jntm.logger.log(Level.INFO,"Syncing config");
        // 各項目の設定値を反映させる。
        isenabledTrayIcon = cfg.getBoolean("enabledTrayIcon", client, isenabledTrayIcon, "任务栏图标会显示当该值为true时.", "config.jntm.prop.enabledGenerator");
        isenabledRP = cfg.getBoolean("isenabledRP", client, isenabledRP, "资源包开关.", "config.jntm.prop.isenabledRP");
        isenabledUP = cfg.getBoolean("isenabledUP", general, isenabledUP, "允许客户端上传文件开关.", "config.jntm.prop.isenabledUP");
        isenabledTelnet = cfg.getBoolean("isenabledTelnet", general, isenabledTelnet, "启动远程访问mod.(重启生效)", "config.jntm.prop.isenabledTelnet");
        // Difficulty
        //amountSmelting = (byte) cfg.getInt("amountSmelting", DIFFICULTY, amountSmelting, 1, Byte.MAX_VALUE, "Smelting amount of Aluminium Ingot from Aluminium Ore.", "config.jntm.prop.amountSmelting");
        // 設定内容をコンフィグファイルに保存する。
        cfg.save();
        if (FMLCommonHandler.instance().getSide()== Side.CLIENT &&orp!=isenabledRP){
            orp=isenabledRP;
            if (orp){
                List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao");
                defaultResourcePacks.add(ClientProxy.JRP);
                ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), defaultResourcePacks, "field_110449_ao");
                FMLClientHandler.instance().refreshResources();
            }else {
                List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao");
                defaultResourcePacks.remove(ClientProxy.JRP);
                ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), defaultResourcePacks, "field_110449_ao");
                FMLClientHandler.instance().refreshResources();
            }
            FMLClientHandler.instance().refreshResources();
        }
    }
}
