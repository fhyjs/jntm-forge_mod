package cn.fhyjs.jntm.config;

import cn.fhyjs.jntm.Jntm;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

public class ConfigCore {
    public static final String GENERAL = "General";
    private static final String DIFFICULTY = GENERAL + ".Difficulty";
    public static Configuration cfg;
    public static boolean isenabledTrayIcon = true;
    public static byte amountSmelting = 1;


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
        cfg.addCustomCategoryComment(GENERAL, "鸡你太美mod配置文档.");
        cfg.setCategoryLanguageKey(GENERAL, "config.jntm.category.general");
        cfg.setCategoryRequiresMcRestart(GENERAL, true);
        // Difficulty
        //cfg.addCustomCategoryComment(DIFFICULTY, "The settings of difficulty.");
        //cfg.setCategoryLanguageKey(DIFFICULTY, "config.aluminium.category.difficulty");
        //cfg.setCategoryRequiresMcRestart(DIFFICULTY, true);
    }


    /** コンフィグを同期する。 */
    public static void syncConfig() {
        Jntm.logger.log(Level.INFO,"Syncing config");
        // 各項目の設定値を反映させる。
        // General
        isenabledTrayIcon = cfg.getBoolean("enabledTrayIcon", GENERAL, isenabledTrayIcon, "任务栏图标会显示当该值为true时.", "config.jntm.prop.enabledGenerator");
        // Difficulty
        //amountSmelting = (byte) cfg.getInt("amountSmelting", DIFFICULTY, amountSmelting, 1, Byte.MAX_VALUE, "Smelting amount of Aluminium Ingot from Aluminium Ore.", "config.jntm.prop.amountSmelting");
        // 設定内容をコンフィグファイルに保存する。
        cfg.save();
    }
}
