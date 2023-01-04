package cn.fhyjs.jntm.registry;

import net.minecraftforge.fml.common.registry.GameRegistry;

import static cn.fhyjs.jntm.registry.ItemRegistryHandler.cookedcxk_item;
import static cn.fhyjs.jntm.registry.ItemRegistryHandler.rawkr;

public class SmeltingRegistryHandler {
    public static void register(){
        GameRegistry.addSmelting(rawkr, cookedcxk_item.getDefaultInstance(),10f);
    }
}
