package cn.fhyjs.jntm.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityRope extends EntityThrowable {
    public Entity thrower;
    public EntityRope(World worldIn) {
        super(worldIn);
        this.setSize(0.1F, 0.1F); // 设置实体大小
        this.setNoGravity(true);
    }
    public EntityRope(World worldIn,Entity entity) {
        this(worldIn);
        this.thrower=entity;
    }
    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.typeOfHit.equals(RayTraceResult.Type.BLOCK)){
            this.setDead();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,posX,posY,posZ,1,1,1,0);
        }
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        super.shoot(x, y, z, velocity*0.5f, inaccuracy);
    }
}
