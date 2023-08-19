package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber
public class EntityRegistryHandler {

    @SubscribeEvent
    public static void onEntityRegistation(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(kundan_st.class)
                .id(new ResourceLocation(Jntm.MODID, "kundan_st"), 233)
                .name("kundan_st")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(cxk.class)
                .id(new ResourceLocation(Jntm.MODID, "cxk"), 110)
                .name("cxk")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(JGPDanmaku.class)
                .id(new ResourceLocation(Jntm.MODID, "JGPD"), 111)
                .name("JGPD")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(CxkTnt_E.class)
                .id(new ResourceLocation(Jntm.MODID, "cxktnte"), 112)
                .name("cxktnte")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(Boss_Cxk.class)
                .id(new ResourceLocation(Jntm.MODID, "bosscxk"), 113)
                .name("bosscxk")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(Ta_Danmaku.class)
                .id(new ResourceLocation(Jntm.MODID, "danmaku"), 114)
                .name("bosscxk")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(XiGua.class)
                .id(new ResourceLocation(Jntm.MODID, "xigua"), 115)
                .name("xigua")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityRope.class)
                .id(new ResourceLocation(Jntm.MODID, "rope"), 116)
                .name("rope")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityHeli.class)
                .id(new ResourceLocation(Jntm.MODID, "heli"), 117)
                .name("heli")
                .tracker(80, 3, false)
                .build()
        );
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityDlw.class)
                .id(new ResourceLocation(Jntm.MODID, "dlw"), 118)
                .name("dlw")
                .tracker(80, 3, false)
                .build()
        );
    }
}
