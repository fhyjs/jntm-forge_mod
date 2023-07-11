package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class JGPDanmaku extends EntityThrowable {
    public static float d=100;
    public JGPDanmaku (World world){
        super(world);
        setDamage(200f);
    }
    public void setDamage(float a){
        d=a;
    }
    public float getDamage(){
        return d;
    }
    public JGPDanmaku(World worldin, EntityLivingBase throwerIn){
        super(worldin,throwerIn);
        setDamage(200f);
    }
    public JGPDanmaku(World worldIn, double x, double y, double z)
    {
        super(worldIn,x,y,z);
        this.setPosition(x, y, z);
    }
    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), d);
        }

        if (!this.world.isRemote)
        {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEventRegistryHandler.xamoob, SoundCategory.PLAYERS, 4.5F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
            //JOptionPane.showMessageDialog(null,d);
            this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, (float) d/50, true);
        }
        
        this.world.setEntityState(this, (byte)3);
        this.setDead();

    }
}
