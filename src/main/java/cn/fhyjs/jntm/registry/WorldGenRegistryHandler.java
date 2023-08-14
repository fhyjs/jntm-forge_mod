package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.world.JntmWorldGen;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class WorldGenRegistryHandler {
    public static void run(){
        GameRegistry.registerWorldGenerator(new JntmWorldGen(),1);
        PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, p_apply_1_ -> p_apply_1_.getMetadata() < 4);
    }
}
