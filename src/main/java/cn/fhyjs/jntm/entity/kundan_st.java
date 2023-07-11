package cn.fhyjs.jntm.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class kundan_st extends EntityEgg {
    public kundan_st(World worldIn) { super(worldIn); }
    public kundan_st(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
    public kundan_st(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.world.isRemote)
        {
            if (this.rand.nextInt(8) == 0)
            {
                int i = 1;

                if (this.rand.nextInt(32) == 0)
                {
                    i = 4;
                }

                for (int j = 0; j < i; ++j)
                {
                    cxk cxk = new cxk(this.world);
                    cxk.setGrowingAge(-24000);
                    cxk.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    this.world.spawnEntity(cxk);
                }
            }

            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }
}
