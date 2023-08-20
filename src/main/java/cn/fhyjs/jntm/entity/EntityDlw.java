package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.utility.MediaPlayer;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

@Optional.Interface(iface = "software.bernie.geckolib3.core.IAnimatable",modid = "geckolib3")
public class EntityDlw extends EntityIronGolem implements IAnimatable {
    protected static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.dlw.walk", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.dlw.idle", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    protected static final AnimationBuilder ATT_ANIM = new AnimationBuilder().addAnimation("animation.dlw.attack", ILoopType.EDefaultLoopTypes.LOOP);
    private final Object factory;
    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 2.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 1.2D, 32.0F));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityLiving.class, 10, false, false, input -> input != null && IMob.VISIBLE_MOB_SELECTOR.apply(input)));
    }
    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(20 + this.rand.nextInt(30)));
        return flag&&super.attackEntityAsMob(entityIn);
    }
    @Override
    public boolean canAttackClass(Class <? extends EntityLivingBase> cls)
    {
        return cls == EntityCreeper.class || super.canAttackClass(cls);
    }
    public EntityDlw(World worldIn) {
        super(worldIn);
        this.setSize(1.4F, 4.7F);
        if (Loader.isModLoaded("geckolib3"))
           factory = new AnimationFactory(this);
        else
            factory=null;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 4)
        {
            att=true;
        }
        super.handleStatusUpdate(id);
    }
    protected boolean att;
    @Optional.Method(modid = "geckolib3")
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "Walking", 5, this::walkAnimController));
        data.addAnimationController(new AnimationController<>(this, "Idle", 5, this::idleAnimController));
        data.addAnimationController(new AnimationController<>(this, "Attack", 5, this::attAnimController));
    }
    protected <E extends IAnimatable> PlayState attAnimController(final AnimationEvent<E> event) {
        if (att) {
            event.getController().setAnimation(ATT_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }
    protected <E extends IAnimatable> PlayState idleAnimController(final AnimationEvent<E> event) {
        if (!event.isMoving()) {
            att=false;
            if (rand.nextDouble()>0.999) {
                event.getController().markNeedsReload();
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
