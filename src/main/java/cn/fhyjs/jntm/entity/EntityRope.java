package cn.fhyjs.jntm.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityRope extends EntityThrowable {
    public EntityRope(World worldIn) {
        super(worldIn);
    }    @Override
    protected void onImpact(RayTraceResult result) {

    }
}
