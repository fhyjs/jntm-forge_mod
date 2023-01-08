package cn.fhyjs.jntm.registry;


import cn.fhyjs.jntm.entity.JGPDanmaku;
import cn.fhyjs.jntm.entity.cxk;
import cn.fhyjs.jntm.entity.kundan_st;

import cn.fhyjs.jntm.item.JGPDTEX;
import cn.fhyjs.jntm.renderer.Rendercxk;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

import static cn.fhyjs.jntm.registry.ItemRegistryHandler.*;

public class RenderRegistryHandler {
    public static void register()
    {
        //坤蛋（实体）
        RenderingRegistry.registerEntityRenderingHandler(kundan_st.class,new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ggxdd,Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(cxk.class,new Rendercxk(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(JGPDanmaku.class,new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), jgpdtex,Minecraft.getMinecraft().getRenderItem()));

    }
}
