package cn.fhyjs.jntm.compat.tlm;

import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.internal.task.TaskAttack;

public class TlmRegistry {
    public static void run(){
        LittleMaidAPI.registerTask(new TaskRope());
    }
}
