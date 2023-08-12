package cn.fhyjs.jntm.compat.tlm;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.EntityRope;
import cn.fhyjs.jntm.entity.ai.EntityAiRope;
import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TaskRope implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(Jntm.MODID, "rope");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ItemRegistryHandler.itemRope);
    }

    @Override
    public void onRangedAttack(AbstractEntityMaid maid, EntityLivingBase target, float distanceFactor) {
        IMaidTask.super.onRangedAttack(maid, target, distanceFactor);
        EntityRope entityRope = new EntityRope(maid.world,maid);
        entityRope.setPosition(maid.posX,maid.posY+maid.getEyeHeight(),maid.posZ);
        entityRope.shoot(maid,maid.rotationPitch,maid.rotationYaw,0,1.5f,0.5f);
        maid.world.spawnEntity(entityRope);
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid) {
        if (maid.getAttackTarget() != null) {
            return MaidSoundEvent.MAID_FIND_TARGET;
        } else {
            return MaidSoundEvent.MAID_ATTACK;
        }
    }

    @Override
    public EntityAIBase createAI(AbstractEntityMaid maid) {
        return new EntityAiRope<>(maid, .6f, 20, 50);
    }

    @Override
    public boolean isAttack() {
        return true;
    }
}
