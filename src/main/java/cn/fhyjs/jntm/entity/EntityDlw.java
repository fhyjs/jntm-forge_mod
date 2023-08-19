package cn.fhyjs.jntm.entity;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@Optional.Interface(iface = "software.bernie.geckolib3.core.IAnimatable",modid = "geckolib3")
public class EntityDlw extends EntityIronGolem implements IAnimatable {
    protected static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.dlw.walk", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.dlw.idle", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    private final Object factory;
    public EntityDlw(World worldIn) {
        super(worldIn);
        this.setSize(1.4F, 4.7F);
        if (Loader.isModLoaded("geckolib3"))
           factory = new AnimationFactory(this);
        else
            factory=null;
    }
    @Optional.Method(modid = "geckolib3")
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "Walking", 5, this::walkAnimController));
        data.addAnimationController(new AnimationController<>(this, "Idle", 5, this::idleAnimController));
    }
    protected <E extends IAnimatable> PlayState idleAnimController(final AnimationEvent<E> event) {
        if (!event.isMoving()) {
            if (rand.nextDouble()>0.999) {
                event.getController().setAnimation(null );
                event.getController().setAnimation(IDLE_ANIM);
            }
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }
    protected <E extends IAnimatable> PlayState walkAnimController(final AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(WALK_ANIM);

            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }
    @Optional.Method(modid = "geckolib3")
    @Override
    public AnimationFactory getFactory() {
        return (AnimationFactory) this.factory;
    }
}
