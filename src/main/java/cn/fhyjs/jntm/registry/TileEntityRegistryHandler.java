package cn.fhyjs.jntm.registry;

import net.minecraftforge.fml.common.registry.GameRegistry;

import static cn.fhyjs.jntm.registry.BlockRegistryHandler.*;

public class TileEntityRegistryHandler {
    public static void reg(){
        GameRegistry.registerTileEntity(JIM_PLAYER_BLOCK.getTileEntityClass(), JIM_PLAYER_BLOCK.getRegistryName());
        GameRegistry.registerTileEntity(CXKIMAGE.getTileEntityClass(), CXKIMAGE.getRegistryName());
        GameRegistry.registerTileEntity(JI_CRAFTING_TABLE.getTileEntityClass(), JI_CRAFTING_TABLE.getRegistryName());
        GameRegistry.registerTileEntity(blockLandmine.getTileEntityClass(), blockLandmine.getRegistryName());
        GameRegistry.registerTileEntity(blockPmxModel.getTileEntityClass(), blockPmxModel.getRegistryName());
    }
}
