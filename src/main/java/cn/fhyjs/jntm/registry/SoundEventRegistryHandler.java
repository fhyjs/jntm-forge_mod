package cn.fhyjs.jntm.registry;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

import static net.minecraft.launchwrapper.LogWrapper.log;

@Mod.EventBusSubscriber
public class SoundEventRegistryHandler {
    public static SoundEvent soundEvents,xainga,ji,music_xjj,music_kdj,cxk_hurt,cxk_die,jntmyy,xamoob,fadongji;
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
    public static void onSoundEvenrRegistration(RegistryEvent.Register<SoundEvent> event) {
        for (int i=0;i< arr_2.length;i++){
            log.info("jntm:RegSound("+i+"):{"+prefix+":"+arr_2[i]+"}");
            soundEvents = new SoundEvent(new ResourceLocation(prefix, arr_2[i]));
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
