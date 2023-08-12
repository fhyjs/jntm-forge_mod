package cn.fhyjs.jntm.entity.ai;

import cn.fhyjs.jntm.item.ItemRope;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Loader;

public class EntityAiRope<T extends EntityLiving & IRangedAttackMob> extends EntityAIBase
{
    private final T entity;
    private final double moveSpeedAmp;
    private int attackCooldown;
    private final float maxAttackDistance;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public EntityAiRope(T entity, double moveSpeedAmp, int attackCooldown, float maxAttackDistance)
    {
        this.entity = entity;
        this.moveSpeedAmp = moveSpeedAmp;
        this.setAttackCooldown(attackCooldown);
        this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
        this.setMutexBits(3);
    }

    public void setAttackCooldown(int attackCooldown)
    {
        this.attackCooldown = attackCooldown;
    }

    public boolean shouldExecute()
    {
        return this.entity.getAttackTarget() != null && this.hasRope();
    }

    protected boolean hasRope()
    {
        if (Loader.isModLoaded("touhou_little_maid")){
            if (entity instanceof AbstractEntityMaid){
                for (int i = 0; i < ((AbstractEntityMaid) entity).getAvailableInv(true).getSlots(); i++) {
                    if (((AbstractEntityMaid) entity).getAvailableInv(true).getStackInSlot(i).getItem() instanceof ItemRope)
                        return true;
                }
            }
        }
        if (entity.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemRope || entity.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemRope){
            return true;
        }
        return false;
    }

    public boolean shouldContinueExecuting()
    {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.hasRope();
    }

    public void startExecuting()
    {
        super.startExecuting();
        this.entity.setSwingingArms(true);
    }

    public void resetTask()
    {
        super.resetTask();
        this.entity.setSwingingArms(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.entity.resetActiveHand();
    }
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        entity.attackEntityWithRangedAttack(target,distanceFactor);
        if (Loader.isModLoaded("touhou_little_maid")){
            if (entity instanceof AbstractEntityMaid){
                for (int i = 0; i < ((AbstractEntityMaid) entity).getAvailableInv(true).getSlots(); i++) {
                    if (((AbstractEntityMaid) entity).getAvailableInv(true).getStackInSlot(i).getItem() instanceof ItemRope) {
                        ItemStack is = ((AbstractEntityMaid) entity).getAvailableInv(true).getStackInSlot(i);
                        if (is.getTagCompound() != null && is.getTagCompound().hasKey("data")) {
                            continue;
                        }
                        is.setCount(is.getCount() - 1);
                        return;
                    }
                }
            }
        }
        if (entity.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemRope) {
            entity.getHeldItem(EnumHand.MAIN_HAND).setCount(entity.getHeldItem(EnumHand.MAIN_HAND).getCount()-1);
            return;
        }
        if (entity.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemRope) {
            entity.getHeldItem(EnumHand.OFF_HAND).setCount(entity.getHeldItem(EnumHand.OFF_HAND).getCount()-1);
        }
    }
    private int ac;
    public void updateTask()
    {
        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

        if (entitylivingbase != null)
        {
            double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
            boolean flag1 = this.seeTime > 0;

            if (flag != flag1)
            {
                this.seeTime = 0;
            }

            if (flag)
            {
                ++this.seeTime;
            }
            else
            {
                --this.seeTime;
            }

            if (d0 <= (double)this.maxAttackDistance && this.seeTime >= 20)
            {
                this.entity.getNavigator().clearPath();
                ++this.strafingTime;
            }
            else
            {
                this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20)
            {
                if ((double)this.entity.getRNG().nextFloat() < 0.3D)
                {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.entity.getRNG().nextFloat() < 0.3D)
                {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1)
            {
                if (d0 > (double)(this.maxAttackDistance * 0.75F))
                {
                    this.strafingBackwards = false;
                }
                else if (d0 < (double)(this.maxAttackDistance * 0.25F))
                {
                    this.strafingBackwards = true;
                }

                this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
            }
            else
            {
                this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            }

            if (hasRope())
            {
                if (!flag && this.seeTime < -60)
                {
                    this.entity.resetActiveHand();
                }
                else if (flag)
                {
                    ac++;
                    if (attackCooldown<ac) {
                        ac=0;
                        this.entity.resetActiveHand();
                        this.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(40));
                        this.attackTime = this.attackCooldown;
                    }
                }
            }
            else if (--this.attackTime <= 0 && this.seeTime >= -60)
            {
                this.entity.setActiveHand(EnumHand.MAIN_HAND);
            }
        }
    }
}
