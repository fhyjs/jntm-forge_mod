package cn.fhyjs.jntm.entity.spallcardentity;

import cn.fhyjs.jntm.entity.CxkTnt_E;
import net.katsstuff.teamnightclipse.danmakucore.entity.spellcard.EntitySpellcard;
import net.katsstuff.teamnightclipse.danmakucore.entity.spellcard.Spellcard;
import net.katsstuff.teamnightclipse.danmakucore.entity.spellcard.SpellcardEntity;
import net.katsstuff.teamnightclipse.danmakucore.lib.LibSounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import scala.Option;

public class CustomSCE extends SpellcardEntity {
    public CustomSCE(Spellcard spellcard, EntitySpellcard cardEntity, Option<EntityLivingBase> target) {
        super(spellcard, cardEntity, target);
    }

    @Override
    public void onSpellcardUpdate() {
        if (!this.world().isRemote) {
            double x = posTarget().get().x();
            double y = posTarget().get().y();
            double z = posTarget().get().z();
            int danmakuLevelMultiplier = this.danmakuLevel().getMultiplier();
            if (this.time() == 1) {
                world().playSound(null, new BlockPos(this.posUser().toVec3d()), LibSounds.ENEMY_POWER,SoundCategory.PLAYERS, 0.2F, 1.0F);
            }

            if (this.time() > 40) {

                int time40 = this.time() % 40;
                if (time40 == 10) {
                    world().playSound(null,new BlockPos(this.posUser().toVec3d()), LibSounds.SHOT1,SoundCategory.PLAYERS, 0.2F, 1.0F);
                }
                EntityLivingBase[] elb = new EntityLivingBase[1];
                elb[0]=user();
                for (int i = -3; i<=3; i++) {
                    world().spawnEntity(new CxkTnt_E(world(), x +i, y, z +time()-40-3, user(), 1F, elb,true));
                }
            }
        }
    }
}
