package cn.fhyjs.jntm.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import static cn.fhyjs.jntm.registry.ItemRegistryHandler.rawkr;

public class SmeltingRegistryHandler {
    public static void register(){
        FurnaceRecipes.instance().addSmelting(rawkr,new ItemStack(BlockRegistryHandler.BLOCK_cookedcxk),10f);
    }
}
