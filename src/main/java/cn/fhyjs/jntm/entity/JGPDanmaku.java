package cn.fhyjs.jntm.entity;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class JGPDanmaku extends EntityArrow {
    public JGPDanmaku(World worldin){
        super(worldin);
        this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        this.setSize(0.5F, 0.5F);
    }

    protected ItemStack getArrowStack() {
        return null;
    }
}
