package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.pstest;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static net.minecraft.launchwrapper.LogWrapper.log;

@Mod.EventBusSubscriber
public class SoundEventRegistryHandler {
    public static SoundEvent soundEvents,xainga,ji,music_xjj,music_kdj,cxk_hurt,cxk_die,jntmyy,xamoob,fadongji,xiatounan,shuidonga,jiarenmen,wozhendehuixie;
    public static String prefix = "jntm";
    public static String[] arr_2 = {
            "xianga",
            "ji",
            "music_xjj",
            "music_kdj",
            "cxk_hurt",
            "cxk_die",
            "jntm",
            "xaboom",
            "fadongji"
    };
    @SubscribeEvent
    public static void onSoundEvenrRegistration(RegistryEvent.Register<SoundEvent> event) throws IOException, InvocationTargetException, IllegalAccessException {
        JsonParser d = new JsonParser();
        JsonElement e = d.parse(pstest.readLine(Jntm.class.getClassLoader().getResourceAsStream("assets/jntm/sounds.json")));
        Set<Map.Entry<String, JsonElement>> f = e.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : f) {
            SoundEvent se = new SoundEvent(new ResourceLocation(Jntm.MODID, stringJsonElementEntry.getKey()));
            se.setRegistryName(se.getSoundName());
            if(Arrays.asList(arr_2).contains(stringJsonElementEntry.getKey())) continue;
            event.getRegistry().register(se);
        }
        for (int i=0;i< arr_2.length;i++){
            log.info("jntm:RegSound("+i+"):{"+prefix+":"+arr_2[i]+"}");
            soundEvents = new SoundEvent(new ResourceLocation(prefix, arr_2[i]));
            xiatounan = new SoundEvent(new ResourceLocation(Jntm.MODID,"xiatounan"));
            shuidonga = new SoundEvent(new ResourceLocation(Jntm.MODID,"shuidonga"));
            jiarenmen = new SoundEvent(new ResourceLocation(Jntm.MODID,"jiarenmen"));
            wozhendehuixie = new SoundEvent(new ResourceLocation(Jntm.MODID,"wozhendehuixie"));
            event.getRegistry().register(soundEvents.setRegistryName(new ResourceLocation(prefix, arr_2[i])));
            if (arr_2[i]=="xianga"){
                xainga=soundEvents;
            }
            if (arr_2[i]=="ji"){
                ji=soundEvents;
            }
            if (arr_2[i]=="music_xjj"){
                music_xjj=soundEvents;
            }
            if (arr_2[i]=="music_kdj"){
                music_kdj=soundEvents;
            }
            if (arr_2[i]=="cxk_hurt"){
                cxk_hurt=soundEvents;
            }
            if (arr_2[i]=="cxk_die"){
                cxk_die=soundEvents;
            }
            if (arr_2[i]=="jntm") {
                jntmyy=soundEvents;
            }
            if (arr_2[i]=="xaboom") {
                xamoob=soundEvents;
            }
            if (arr_2[i]=="fadongji"){
                fadongji=soundEvents;
            }
        }

    }
}
