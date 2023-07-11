package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.Ji_Exposion;
import cn.fhyjs.jntm.registry.JntmLootTableList;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.danmakucore.DanmakuCore;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.DanmakuTemplate;
import net.katsstuff.teamnightclipse.danmakucore.lib.LibColor;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibForms;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibShotData;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import scala.collection.JavaConversions;

import java.security.SecureRandom;
import java.util.Objects;

import static cn.fhyjs.jntm.registry.ItemRegistryHandler.ggxdd;
import static cn.fhyjs.jntm.registry.ItemRegistryHandler.rawkr;

public class cxk extends EntityChicken{
    public boolean ep=false;
    public int fuse;

    private EntityLivingBase[] elb;
    private static final DataParameter<Boolean> Ep = EntityDataManager.<Boolean>createKey(cxk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(cxk.class, DataSerializers.VARINT);
    public cxk(World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.setFuse(50);
    }
    public cxk(World worldIn,EntityLivingBase[] ebl) {
        super(worldIn);
        this.elb = ebl;
        this.setFuse(50);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (ep) {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (!this.hasNoGravity()) {
                this.motionY -= 0.03999999910593033D;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;

            if (this.onGround) {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
                this.motionY *= -0.5D;
            }

            --this.fuse;

            if (this.fuse <= 0) {
                this.setDead();

                if (!this.world.isRemote) {
                    this.explode();
                }
            } else {
                tick++; 
                this.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(1)),999,10));
                this.handleWaterMovement();
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
                if (!world.isRemote) {
                    if (tick>20) {
                        tick=0;
                        this.rotationYaw=360* new SecureRandom().nextFloat();
                        if(Jntm.IS_DC_Load) {
                            DanmakuTemplate.Builder temp = DanmakuTemplate.builder()
                                    .setSource(this)
                                    .setUser(this)
                                    .setWorld(world)
                                    .setMovementData(1D)
                                    .setShot(LibShotData.SHOT_FIRE
                                            .setCoreColor(LibColor.COLOR_VANILLA_WHITE)
                                            .setEdgeColor(LibColor.COLOR_SATURATED_YELLOW)
                                            .setDamage(2f)
                                            .scaleSize(10f)
                                            .setForm(LibForms.FIRE)
                                    );
                            Vector3 start_vec = new Vector3(this);
                            temp.setPos(start_vec);
                            DanmakuCore.proxy().spawnDanmaku(JavaConversions.asScalaBuffer(Lists.newArrayList(temp.build().asEntity())));
                        }
                    }
                }
            }
        }
    }
    private int tick=0;
    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(FUSE, 130);
        this.dataManager.register(Ep, false);
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setep(compound.getBoolean("ep"));
        this.setFuse(compound.getShort("Fuse"));
    }
    public void setFuse(int fuseIn)
    {
        this.dataManager.set(FUSE, fuseIn);
        this.fuse = fuseIn;
    }
    public void setep(boolean epIn)
    {
        this.dataManager.set(Ep, epIn);
        this.ep = epIn;
    }
    public boolean getep()
    {
        return this.ep;
    }
    private void explode() {
        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEventRegistryHandler.xamoob, SoundCategory.BLOCKS, 2.0F, 1.0F);
        Ji_Exposion.createExplosion(world, this, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, 4, true, elb);
    }
    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
        if (FUSE.equals(key))
        {
            this.fuse = this.getFuseDataManager();
        }
        if (Ep.equals(key))
        {
            this.ep = this.getEpDataManager();
        }
    }

    public int getFuseDataManager()
    {
        return ((Integer)this.dataManager.get(FUSE)).intValue();
    }
    public boolean getEpDataManager()
    {
        return ((Boolean)this.dataManager.get(Ep)).booleanValue();
    }

    public int getFuse()
    {
        return this.fuse;
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("ep", this.getep());
        compound.setShort("Fuse", (short)this.getFuse());
    }
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEventRegistryHandler.ji;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEventRegistryHandler.cxk_hurt;
    }
    @Override
    public cxk createChild(EntityAgeable ageable)
    {
        return new cxk(this.world);
    }
    @Override
    protected SoundEvent getDeathSound() {return SoundEventRegistryHandler.cxk_die;}
    @Override
    protected ResourceLocation getLootTable()
    {
        return JntmLootTableList.ENTITIES_CXK;
    }
    @Override
    protected Item getDropItem()
    {
        return rawkr;
    }
    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);

        if (!this.onGround && this.wingRotDelta < 1.0F)
        {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.wingRotation += this.wingRotDelta * 2.0F;

        if (!this.world.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0)
        {
            this.playSound(SoundEventRegistryHandler.ji, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(ggxdd, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }
}
