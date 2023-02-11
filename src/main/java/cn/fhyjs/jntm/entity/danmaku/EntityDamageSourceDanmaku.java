package cn.fhyjs.jntm.entity.danmaku;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityDamageSourceDanmaku extends EntityDamageSourceIndirect {
    public EntityDamageSourceDanmaku(Entity source, @Nullable Entity indirectEntityIn) {
        super("magic", source, indirectEntityIn);
        this.setDamageBypassesArmor();
        this.setMagicDamage();
        this.setProjectile();
    }

    @Nonnull
    @Override
    public ITextComponent getDeathMessage(EntityLivingBase victim) {
        if (getTrueSource() != null) {
            int index = victim.world.rand.nextInt(3) + 1;
            System.out.print(victim.getDisplayName());
            return new TextComponentTranslation(String.format("%1$s %2$s %3$s",victim.getDisplayName().getFormattedText(), I18n.format("death.jntm.attack.danmaku.1"),I18n.format("death.jntm.attack.danmaku.2")),
                    victim.getDisplayName(), getTrueSource().getDisplayName());
        }
        return new TextComponentString("");
    }
}
