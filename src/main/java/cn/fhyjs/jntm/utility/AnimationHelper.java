package cn.fhyjs.jntm.utility;

import cn.fhyjs.jntm.Jntm;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

public class AnimationHelper {
    private AnimationHelper() {}

    public static boolean transitionSafely(IAnimationStateMachine asm, String newState) {
        if (!asm.currentState().equals(newState)) {
            try {
                asm.transition(newState);
                return true;
            }
            catch (IllegalArgumentException ex) {
                Jntm.logger.error("Could not transition ASM from state {} to state {}", asm.currentState(), newState);
                return false;
            }
        }
        else
            return false;
    }

}
