package cn.fhyjs.jntm.entity;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.world.World;

public class EntityHeli extends EntityBoat {
    public EntityHeli(World worldIn) {
        super(worldIn);
        this.setSize(2.375F, 3.5625F);
    }
}
