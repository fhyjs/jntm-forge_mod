package cn.fhyjs.jntm.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CxkTnt_E extends EntityTNTPrimed {
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(CxkTnt_E.class, DataSerializers.VARINT);
    public CxkTnt_E(World worldIn) {
        super(worldIn);
    }
    public CxkTnt_E(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
        super(worldIn, x, y, z, igniter);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }
    @Override
    public void setFuse(int fuseIn)
    {
        this.dataManager.set(FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }
    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (FUSE.equals(key))
        {
            this.fuse = this.getFuseDataManager();
        }
    }
    @Override
    public int getFuseDataManager()
    {
        return ((Integer)this.dataManager.get(FUSE)).intValue();
    }
    @Override
    protected void entityInit()
    {
        this.dataManager.register(FUSE, Integer.valueOf(80));
    }
}
