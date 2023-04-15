package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.block.CxkTNT;
import cn.fhyjs.jntm.block.Cxkimage;
import cn.fhyjs.jntm.block.JimPlayerBlock;
import cn.fhyjs.jntm.block.cookedcxk;
import cn.fhyjs.jntm.item.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber
public class BlockRegistryHandler {
    public static final cookedcxk BLOCK_cookedcxk = new cookedcxk();
    public static final CxkTNT BLOCK_CxkTnt = new CxkTNT();
    public static final Cxkimage CXKIMAGE = new Cxkimage();
    public static final JimPlayerBlock JIM_PLAYER_BLOCK = new JimPlayerBlock();
    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(BLOCK_cookedcxk);
        registry.register(BLOCK_CxkTnt);
        registry.register(CXKIMAGE);
        registry.register(JIM_PLAYER_BLOCK);
        GameRegistry.registerTileEntity(JIM_PLAYER_BLOCK.getTileEntityClass(), JIM_PLAYER_BLOCK.getRegistryName().toString());
        GameRegistry.registerTileEntity(CXKIMAGE.getTileEntityClass(), CXKIMAGE.getRegistryName().toString());
    }
}
