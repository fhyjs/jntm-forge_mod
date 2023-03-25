package cn.fhyjs.jntm.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static cn.fhyjs.jntm.registry.ItemRegistryHandler.cookedcxk_item;
import static cn.fhyjs.jntm.registry.ItemRegistryHandler.rawkr;

public class SmeltingRegistryHandler {
    public static void register(){
        FurnaceRecipes.instance().addSmelting(rawkr,new ItemStack(BlockRegistryHandler.BLOCK_cookedcxk),10f);
    }
}
