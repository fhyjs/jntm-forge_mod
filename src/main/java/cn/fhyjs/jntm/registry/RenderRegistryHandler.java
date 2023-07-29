package cn.fhyjs.jntm.registry;


import cn.fhyjs.jntm.block.*;
import cn.fhyjs.jntm.entity.*;
import cn.fhyjs.jntm.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static cn.fhyjs.jntm.registry.ItemRegistryHandler.ggxdd;
import static cn.fhyjs.jntm.registry.ItemRegistryHandler.jgpdtex;

public class RenderRegistryHandler {
    public static void register(FMLInitializationEvent event)
    {
        //RenderItem IR= new RenderItem(Minecraft.getMinecraft().renderEngine, ObfuscationReflectionHelper.getPrivateValue(Minecraft.class,Minecraft.getMinecraft(),"modelManager"), ObfuscationReflectionHelper.getPrivateValue(Minecraft.class,Minecraft.getMinecraft(),"itemColors"));
        RenderingRegistry.registerEntityRenderingHandler(kundan_st.class,new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ggxdd,Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(cxk.class,new Rendercxk(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(JGPDanmaku.class,new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), jgpdtex,Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(CxkTnt_E.class,new RenderCkxTnt(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(Boss_Cxk.class,new RenderBossCxk(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(Ta_Danmaku.class, new EntityDanmakuRender(Minecraft.getMinecraft().getRenderManager()));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCxkImage.class, new RenderCxkImageTileEntity());
        RenderingRegistry.registerEntityRenderingHandler(XiGua.class,new RenderXigua(Minecraft.getMinecraft().getRenderManager()));
        ItemRegistryHandler.CXKIMAGE.setTileEntityItemStackRenderer(new CIIRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TEJimPlayer.class, new ListeningAnimatedTESR<>());
        ClientRegistry.bindTileEntitySpecialRenderer(TELandmine.class, new BLRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TEPmxModel.class, new KAIMyEntityTESR<>("reimu"));
    }
}
