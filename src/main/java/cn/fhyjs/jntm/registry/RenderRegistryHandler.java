package cn.fhyjs.jntm.registry;


import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.Cxkimage;
import cn.fhyjs.jntm.block.TileEntityCxkImage;
import cn.fhyjs.jntm.entity.*;

import cn.fhyjs.jntm.item.JGPDTEX;
import cn.fhyjs.jntm.renderer.*;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Map;

import static cn.fhyjs.jntm.registry.ItemRegistryHandler.*;

public class RenderRegistryHandler {
    public static void register(FMLPreInitializationEvent event)
    {
        //RenderItem IR= new RenderItem(Minecraft.getMinecraft().renderEngine, ObfuscationReflectionHelper.getPrivateValue(Minecraft.class,Minecraft.getMinecraft(),"modelManager"), ObfuscationReflectionHelper.getPrivateValue(Minecraft.class,Minecraft.getMinecraft(),"itemColors"));
        RenderingRegistry.registerEntityRenderingHandler(kundan_st.class,new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ggxdd,Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(cxk.class,new Rendercxk(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(JGPDanmaku.class,new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), jgpdtex,Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(CxkTnt_E.class,new RenderCkxTnt(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(Boss_Cxk.class,new RenderBossCxk(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(Ta_Danmaku.class, new EntityDanmakuRender(Minecraft.getMinecraft().getRenderManager()));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCxkImage.class, new RenderCxkImageTileEntity());
    }
}
