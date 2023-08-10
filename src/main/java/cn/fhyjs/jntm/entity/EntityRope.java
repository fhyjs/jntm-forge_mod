package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityRope extends EntityThrowable {
    public Entity thrower;
    public int liveTime;
    public EntityRope(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F); // 设置实体大小
        this.setNoGravity(true);
    }
    public EntityRope(World worldIn,Entity entity) {
        this(worldIn);
        this.thrower=entity;
    }
    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.typeOfHit.equals(RayTraceResult.Type.ENTITY)){
            if (result.entityHit.equals(thrower)) return;

            this.setDead();
            if (world instanceof WorldServer){
                WorldServer worldServer = (WorldServer) world;
                worldServer.spawnParticle(EnumParticleTypes.LAVA,posX,posY,posZ,20,0,0,0,1,new int[0]);
                worldServer.playSound(null,new BlockPos(posX,posY,posZ), SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.PLAYERS,1,1);
                if (thrower instanceof EntityPlayer){
                    ((EntityPlayer) thrower).sendStatusMessage(new TextComponentTranslation("tooltip.rope.succ",result.entityHit.getDisplayName().getFormattedText()).setStyle(new Style().setColor(TextFormatting.GREEN)),true);
                }
            }
        }
        if (result.typeOfHit.equals(RayTraceResult.Type.BLOCK)){
            this.setDead();
            if (world instanceof WorldServer){
                WorldServer worldServer = (WorldServer) world;
                worldServer.spawnParticle(EnumParticleTypes.SMOKE_LARGE,posX,posY,posZ,20,0,0,0,1,new int[0]);
                worldServer.playSound(null,new BlockPos(posX,posY,posZ), SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.PLAYERS,1,1);
                if (thrower instanceof EntityPlayer){
                    ((EntityPlayer) thrower).sendStatusMessage(new TextComponentTranslation("tooltip.rope.fail").setStyle(new Style().setColor(TextFormatting.RED)),true);
                }
            }
        }
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        super.shoot(x, y, z, velocity*0.5f, inaccuracy);
        if (world instanceof WorldServer){
            WorldServer worldServer = (WorldServer) world;
            worldServer.playSound(null,new BlockPos(posX,posY,posZ), SoundEvent.REGISTRY.getObject(new ResourceLocation(Jntm.MODID,"prerope")), SoundCategory.PLAYERS,.5f,1);
        }
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        liveTime++;
        if (liveTime>100){
            setDead();
            if (world instanceof WorldServer){
                WorldServer worldServer = (WorldServer) world;
                worldServer.spawnParticle(EnumParticleTypes.SMOKE_LARGE,posX,posY,posZ,20,0,0,0,1,new int[0]);
                worldServer.playSound(null,new BlockPos(posX,posY,posZ), SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.PLAYERS,1,1);
                if (thrower instanceof EntityPlayer){
                    ((EntityPlayer) thrower).sendStatusMessage(new TextComponentTranslation("tooltip.rope.fail").setStyle(new Style().setColor(TextFormatting.RED)),true);
                }
            }
        }
    }
}
