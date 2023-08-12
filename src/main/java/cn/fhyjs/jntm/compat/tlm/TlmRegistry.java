package cn.fhyjs.jntm.compat.tlm;

import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.util.ItemDefinition;
import com.github.tartaricacid.touhoulittlemaid.internal.task.TaskAttack;

public class TlmRegistry {
    public static void run(){
        LittleMaidAPI.registerTask(new TaskRope());
        LittleMaidAPI.registerBauble(ItemDefinition.of(ItemRegistryHandler.itemRopeEmptier),new BaubleRopeEmptier());
    }
}
