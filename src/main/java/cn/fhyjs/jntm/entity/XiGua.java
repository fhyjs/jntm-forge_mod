package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.entity.danmaku.DanmakuColor;
import cn.fhyjs.jntm.entity.danmaku.DanmakuShoot;
import cn.fhyjs.jntm.entity.danmaku.DanmakuType;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class XiGua extends EntityMob implements IRangedAttackMob {
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(XiGua.class, DataSerializers.BOOLEAN);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false)
    {
        public void resetTask()
        {
            super.resetTask();
            XiGua.this.setSwingingArms(false);
        }
        public void startExecuting()
        {
            super.startExecuting();
            XiGua.this.setSwingingArms(true);
        }
    };
    public XiGua(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.setCombatTask();
    }
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEventRegistryHandler.wozhendehuixie;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEventRegistryHandler.xiatounan;
    }
    @Override
    public void playLivingSound()
    {
        super.playLivingSound();
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEventRegistryHandler.shuidonga;
    }
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();

        amf=-1;
        this.dataManager.register(SWINGING_ARMS, false);
        oamf=-1;
    }
    private int amf=-1,oamf=-1;
    public void setCombatTask()
    {
        if (this.getHealth()/this.getMaxHealth()<=0.5){
            amf=1;
        }else {
            amf=0;
        }
        if (amf!=oamf) {
            oamf = amf;
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.addTask(4, this.aiAttackOnCollide);
        }
    }
    @Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }
    @Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
    @Override
    protected void updateAITasks(){
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        this.setCombatTask();
    }
    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        //this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.tasks.addTask(7, new Boss_Cxk.LAT(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(6, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        //this.tasks.addTask(8, new AIFireballAttack(this));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        DanmakuShoot.posedShot(world,this,target.posX,target.posY,target.posZ,2,4,0,0.5f,0f, DanmakuType.BASKETBALL, DanmakuColor.GREEN.ordinal());
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(SWINGING_ARMS, swingingArms);
    }
}
