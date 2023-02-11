package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.entity.danmaku.DanmakuColor;
import cn.fhyjs.jntm.entity.danmaku.DanmakuShoot;
import cn.fhyjs.jntm.entity.danmaku.DanmakuType;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Some;

public class Boss_Cxk extends EntityMob implements IRangedAttackMob {
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(Boss_Cxk.class, DataSerializers.BOOLEAN);
    private final Danmdku_A<Boss_Cxk> aiArrowAttack = new Danmdku_A<Boss_Cxk>(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false)
    {
        public void resetTask()
        {
            super.resetTask();
            Boss_Cxk.this.setSwingingArms(false);
        }
        public void startExecuting()
        {
            super.startExecuting();
            Boss_Cxk.this.setSwingingArms(true);
        }
    };
    public Boss_Cxk(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.setCombatTask();
    }
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000.0D);
    }
    @Override
    protected void entityInit()
    {
        super.entityInit();
        amf=-1;
        this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
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
            this.tasks.removeTask(this.aiArrowAttack);
            if (amf==1) {
                if (this.world != null && !this.world.isRemote) {
                    this.aiArrowAttack.setAttackCooldown(5);
                    this.tasks.addTask(4, this.aiArrowAttack);
                }
            }else {
                if (this.world != null && !this.world.isRemote) {
                    this.tasks.addTask(4, this.aiAttackOnCollide);
                }
            }
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
        this.tasks.addTask(7, new LAT(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(6, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        //this.tasks.addTask(8, new AIFireballAttack(this));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        DanmakuShoot.posedShot(world,this,target.posX,target.posY,target.posZ,2,4,0,0.5f,0f, DanmakuType.BASKETBALL, DanmakuColor.GREEN.ordinal());
    }

    @SideOnly(Side.CLIENT)
    public boolean isSwingingArms()
    {
        return ((Boolean)this.dataManager.get(SWINGING_ARMS)).booleanValue();
    }

    public void setSwingingArms(boolean swingingArms)
    {
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
    }

    static class LAT extends EntityAIBase{
        private final EntityMob parentEntity;

        public LAT(EntityMob ghast)
        {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute()
        {
            return this.parentEntity.getAttackTarget() != null;
        }

        public void startExecuting()
        {

        }

        public void resetTask()
        {

        }

        public void updateTask()
        {
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            assert entitylivingbase != null;
            if ( this.parentEntity.canEntityBeSeen(entitylivingbase))
            {
                this.parentEntity.getLookHelper().setLookPosition(this.parentEntity.getAttackTarget().posX,this.parentEntity.getAttackTarget().posY+1,this.parentEntity.getAttackTarget().posZ,(float)this.parentEntity.getHorizontalFaceSpeed(), (float)this.parentEntity.getVerticalFaceSpeed());
            }
        }
    }

    static class AIFireballAttack extends EntityAIBase
    {
        private final EntityMob parentEntity;
        public int attackTimer;
        public AIFireballAttack(EntityMob ghast)
        {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute()
        {
            return this.parentEntity.getAttackTarget() != null;
        }

        public void startExecuting()
        {
            this.attackTimer = 0;
        }

        public void resetTask()
        {

        }

        public void updateTask()
        {
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double d0 = 64.0D;
            this.parentEntity.getLookHelper().setLookPosition(this.parentEntity.getAttackTarget().posX,this.parentEntity.getAttackTarget().posY+1,this.parentEntity.getAttackTarget().posZ,(float)this.parentEntity.getHorizontalFaceSpeed(), (float)this.parentEntity.getVerticalFaceSpeed());
            if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen(entitylivingbase))
            {
                World world = this.parentEntity.world;
                ++this.attackTimer;
                if (this.attackTimer == 10)
                {
                    double d1 = 4.0D;
                    Vec3d vec3d = this.parentEntity.getLook(1.0F);
                    double d2 = entitylivingbase.posX - (this.parentEntity.posX + vec3d.x * 4.0D);
                    double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + this.parentEntity.posY + (double)(this.parentEntity.height / 2.0F));
                    double d4 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3d.z * 4.0D);
                    DanmakuShoot.posedShot(world,parentEntity,this.parentEntity.getAttackTarget().posX,this.parentEntity.getAttackTarget().posY,this.parentEntity.getAttackTarget().posZ,2,4,0,0.5f,0f, DanmakuType.BASKETBALL, DanmakuColor.GREEN.ordinal());

                    this.attackTimer = -7;

                }
            }
            else if (this.attackTimer > 0)
            {
                --this.attackTimer;
            }


        }
    }
    static class Danmdku_A<T extends EntityMob & IRangedAttackMob> extends EntityAIAttackRangedBow{
        private final T entity;
        private final double moveSpeedAmp;
        private int attackCooldown;
        private final float maxAttackDistance;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;
        public Danmdku_A(T p_i47515_1_, double p_i47515_2_, int p_i47515_4_, float p_i47515_5_) {
            super(p_i47515_1_, p_i47515_2_, p_i47515_4_, p_i47515_5_);
            this.entity = p_i47515_1_;
            this.moveSpeedAmp = p_i47515_2_;
            this.attackCooldown = p_i47515_4_;
            this.maxAttackDistance = p_i47515_5_ * p_i47515_5_;
            this.setMutexBits(3);
        }
        @Override
        public void setAttackCooldown(int p_189428_1_)
        {
            this.attackCooldown = p_189428_1_;
        }
        @Override
        public boolean shouldExecute()
        {
            return this.entity.getAttackTarget() != null && this.isBowInMainhand();
        }
        @Override
        protected boolean isBowInMainhand()
        {
            return true;
        }
        @Override
        public boolean shouldContinueExecuting()
        {
            return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowInMainhand();
        }
        @Override
        public void startExecuting()
        {
            super.startExecuting();
            ((IRangedAttackMob)this.entity).setSwingingArms(true);
        }
        @Override
        public void resetTask()
        {
            super.resetTask();
            ((IRangedAttackMob)this.entity).setSwingingArms(false);
            this.seeTime = 0;
            this.attackTime = -1;
            this.entity.resetActiveHand();
        }
        @Override
        public void updateTask()
        {
            EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

            if (entitylivingbase != null)
            {
                double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
                boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
                boolean flag1 = this.seeTime > 0;

                if (flag != flag1)
                {
                    this.seeTime = 0;
                }

                if (flag)
                {
                    ++this.seeTime;
                }
                else
                {
                    --this.seeTime;
                }

                if (d0 <= (double)this.maxAttackDistance && this.seeTime >= 20)
                {
                    this.entity.getNavigator().clearPath();
                    ++this.strafingTime;
                }
                else
                {
                    this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20)
                {
                    if ((double)this.entity.getRNG().nextFloat() < 0.3D)
                    {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.entity.getRNG().nextFloat() < 0.3D)
                    {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1)
                {
                    if (d0 > (double)(this.maxAttackDistance * 0.75F))
                    {
                        this.strafingBackwards = false;
                    }
                    else if (d0 < (double)(this.maxAttackDistance * 0.25F))
                    {
                        this.strafingBackwards = true;
                    }

                    this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
                }
                else
                {
                    this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
                }

                //if (this.entity.isHandActive())
                {
                    if (!flag && this.seeTime < -60)
                    {
                        this.entity.resetActiveHand();
                    }
                    else if (flag&&this.attackTime<=0)
                    {
                        int i = 40;

                        this.entity.resetActiveHand();
                        ((IRangedAttackMob)this.entity).attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }else {
                        attackTime--;
                    }
                }
                //else if (--this.attackTime <= 0 && this.seeTime >= -60)
                {
                    this.entity.setActiveHand(EnumHand.MAIN_HAND);
                }
            }
        }
    }
}