package cn.fhyjs.jntm.entity;

import net.katsstuff.danmakucore.data.ShotData;
import net.katsstuff.danmakucore.item.ItemDanmaku;
import net.katsstuff.danmakucore.item.ItemDanmaku$;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import scala.Some;

public class Boss_Cxk extends EntityMob {
    public Boss_Cxk(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }
    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        //this.tasks.addTask(4, new EntityAIAttackMe lee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.tasks.addTask(7, new AIFireballAttack(this));
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

            if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen(entitylivingbase))
            {
                World world = this.parentEntity.world;
                ++this.attackTimer;

                if (this.attackTimer == 10)
                {
                    world.playEvent((EntityPlayer)null, 1015, new BlockPos(this.parentEntity), 0);
                }

                if (this.attackTimer == 20)
                {
                    ItemDanmaku dm = new ItemDanmaku();
                    NBTTagCompound ntc = new NBTTagCompound();
                    NBTTagCompound sd = new NBTTagCompound();
                    ntc.setInteger("custom",0);
                    ntc.setFloat("gravityx",0);
                    ntc.setFloat("gravityy",0);
                    ntc.setFloat("gravityz",0);
                    ntc.setFloat("speed",0.3f);
                    ntc.setString("variant","danmakucore:circle");
                    sd.setInteger("color",16383998);
                    sd.setInteger("coreColor",16777215);
                    sd.setInteger("delay",0);
                    sd.setInteger("end",80);
                    sd.setFloat("damage",5);
                    sd.setFloat("sizeX",0.3f);
                    sd.setFloat("sizeY",0.3f);
                    sd.setFloat("sizeZ",0.3f);
                    sd.setString("form","danmakucore:circle");
                    sd.setString("subEntity","danmakucore:default");

                    ntc.setTag("shotData",sd);
                    ItemStack dmis = dm.getDefaultInstance();
                    dmis.setTagCompound(ntc);
                    ShotData shot = net.katsstuff.danmakucore.data.ShotData.fromNBTItemStack(dmis);
                    if (!world.isRemote) {
                        ItemDanmaku$.MODULE$.shootDanmaku(dmis, world, new Some(parentEntity), new Some(parentEntity.getActiveHand()), true, new Vector3(parentEntity.posX, parentEntity.posY + 1.9 - (double)(shot.sizeY() / (float)2), parentEntity.posZ), new Vector3( parentEntity), (double)(shot.sizeZ() / (float)3 * (float)2));
                    }

                    //shot.form().playShotSound(null, shot);
                }
            }
            else if (this.attackTimer > 0)
            {
                --this.attackTimer;
            }


        }
    }
}
