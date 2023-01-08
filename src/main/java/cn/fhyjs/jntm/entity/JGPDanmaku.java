package cn.fhyjs.jntm.entity;

import net.minecraft.block.BlockJukebox;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.swing.*;

public class JGPDanmaku extends EntityThrowable {
    public float d=100;
    public JGPDanmaku (World world){
        super(world);
        setDamage(200f);
    }
    public void setDamage(float a){
        this.d=a;
    }
    public float getDamage(){
        return this.d;
    }
    public JGPDanmaku(World worldin, EntityLivingBase throwerIn){
        super(worldin,throwerIn);
        setDamage(200f);
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
            //JOptionPane.showMessageDialog(null,d);
            this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, (float) d/50, true);
        }
        
        this.world.setEntityState(this, (byte)3);
        this.setDead();

    }
}
