package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.common.Ji_Exposion;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CxkTnt_E extends EntityTNTPrimed {
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(CxkTnt_E.class, DataSerializers.VARINT);
    private boolean ec;
    private EntityLivingBase[] elb;
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;
    private float ep;

    public CxkTnt_E(World worldIn)
    {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public CxkTnt_E(World worldIn, double x, double y, double z, EntityLivingBase igniter,float ep)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.setFuse(130);
        this.prevPosX = x;
        this.prevPosY = y;
        this.ep=ep;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }
    public CxkTnt_E(World worldIn, double x, double y, double z, EntityLivingBase igniter,float ep,EntityLivingBase[] elb,boolean ec)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.setFuse(130);
        this.prevPosX = x;
        this.prevPosY = y;
        this.ep=ep;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
        this.elb=elb;
        this.ec=ec;
    }
    @Override
    protected void entityInit()
    {
        this.dataManager.register(FUSE, Integer.valueOf(130));
    }
    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }
    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        --this.fuse;

        if (this.fuse <= 0)
        {
            this.setDead();

            if (!this.world.isRemote)
            {
                this.explode();
            }
        }
        else
        {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode()
    {
        this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEventRegistryHandler.xamoob, SoundCategory.BLOCKS, 2.0F, 1.0F);
        Explosion je = Ji_Exposion.createExplosion(world,this,this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, ep, true,elb);
        //this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, ep, true);
        int r=3;
        for (int i=0;i<r;i++){
            for(int j=0;j<r;j++) {
                cxk cxk = new cxk(world,elb);
                cxk.ep=ec;
                cxk.setLocationAndAngles(this.posX - i, this.posY, this.posZ-j, this.rotationYaw, 0.0F);
                this.world.spawnEntity(cxk);
                ((Ji_Exposion)je).IE.add(cxk);
            }
        }
        for (int i=0;i<r;i++){
            for(int j=0;j<r;j++) {
                cxk cxk = new cxk(world,elb);
                cxk.ep=ec;
                cxk.setLocationAndAngles(this.posX + i, this.posY, this.posZ-j, this.rotationYaw, 0.0F);
                this.world.spawnEntity(cxk);
                ((Ji_Exposion)je).IE.add(cxk);
            }
        }
        for (int i=0;i<r;i++){
            for(int j=0;j<r;j++) {
                EntityLivingBase cxk;
                if (ec){
                    cxk = new cxk(world,elb);
                    ((cn.fhyjs.jntm.entity.cxk) cxk).ep=ec;
                }else {
                    cxk = new EntityChicken(world);
                }
                cxk.setLocationAndAngles(this.posX - i, this.posY, this.posZ+j, this.rotationYaw, 0.0F);
                this.world.spawnEntity(cxk);
                ((Ji_Exposion)je).IE.add(cxk);
            }
        }
        for (int i=0;i<r;i++){
            for(int j=0;j<r;j++) {
                EntityLivingBase cxk;
                if (ec){
                    cxk = new cxk(world,elb);
                    ((cn.fhyjs.jntm.entity.cxk) cxk).ep=ec;
                }else {
                    cxk = new EntityChicken(world);
                }
                cxk.setLocationAndAngles(this.posX+i, this.posY, this.posZ+j, this.rotationYaw, 0.0F);
                this.world.spawnEntity(cxk);
                ((Ji_Exposion)je).IE.add(cxk);
            }
        }
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setShort("Fuse", (short)this.getFuse());
    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setFuse(compound.getShort("Fuse"));
    }
    @Override
    @Nullable
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }
    @Override
    public float getEyeHeight()
    {
        return 0.0F;
    }
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

    public int getFuseDataManager()
    {
        return ((Integer)this.dataManager.get(FUSE)).intValue();
    }

    public int getFuse()
    {
        return this.fuse;
    }
}
