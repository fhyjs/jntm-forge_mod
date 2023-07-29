package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.block.*;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class BlockRegistryHandler {
    public static final cookedcxk BLOCK_cookedcxk = new cookedcxk();
    public static final CxkTNT BLOCK_CxkTnt = new CxkTNT();
    public static final Cxkimage CXKIMAGE = new Cxkimage();
    public static final JimPlayerBlock JIM_PLAYER_BLOCK = new JimPlayerBlock();
    public static final Ji_Crafting_Table JI_CRAFTING_TABLE = new Ji_Crafting_Table();
    public static final BlockLandmine blockLandmine = new BlockLandmine();
    public static final BlockLandmineConfigurator blockLandmineConfigurator = new BlockLandmineConfigurator();
    public static final BlockPmxModel blockPmxModel = new BlockPmxModel();
    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(BLOCK_cookedcxk);
        registry.register(BLOCK_CxkTnt);
        registry.register(CXKIMAGE);
        registry.register(JIM_PLAYER_BLOCK);
        registry.register(JI_CRAFTING_TABLE);
        registry.register(blockLandmine);
        registry.register(blockLandmineConfigurator);
        registry.register(blockPmxModel);
    }
}
