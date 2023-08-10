package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityRope extends EntityThrowable {
    private static final DataParameter<Integer> aliveTime = EntityDataManager.createKey(EntityRope.class, DataSerializers.VARINT);
    public Entity thrower;
    public NBTTagCompound entityTag;
    public String entityName;
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
        try {
            if (entityTag != null && entityName != null) {
                if (result.typeOfHit.equals(RayTraceResult.Type.ENTITY)&&result.entityHit.equals(thrower)) return;
                if (result.typeOfHit.equals(RayTraceResult.Type.ENTITY)){
                    ObfuscationReflectionHelper.setPrivateValue(RayTraceResult.class,result,new BlockPos(result.entityHit.posX,result.entityHit.posY, result.entityHit.posZ),"field_178783_e");
                }
                Entity entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityName)).newInstance(world);
                entity.readFromNBT(entityTag);
                entity.setPosition(result.getBlockPos().getX(), result.getBlockPos().getY()+1, result.getBlockPos().getZ());
                world.spawnEntity(entity);
                EntityItem eif = new EntityItem(world, posX, posY, posZ, new ItemStack(ItemRegistryHandler.itemRope));
                eif.setPosition(thrower.posX,thrower.posY,thrower.posZ);
                world.spawnEntity(eif);
                this.setDead();
                if (world instanceof WorldServer) {
                    WorldServer worldServer = (WorldServer) world;
                    worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY, posZ, 20, 0, 0, 0, 1, new int[0]);
                    worldServer.playSound(null, new BlockPos(posX, posY, posZ), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1, 1);
                    if (thrower instanceof EntityPlayer) {
                        ((EntityPlayer) thrower).sendStatusMessage(new TextComponentTranslation("tooltip.rope.succ", result.entityHit.getDisplayName().getFormattedText()).setStyle(new Style().setColor(TextFormatting.GREEN)), true);
                    }
                }
                return;
            }
            if (result.typeOfHit.equals(RayTraceResult.Type.ENTITY)) {
                if (result.entityHit.equals(thrower)) return;
                if (result.entityHit instanceof EntityPlayer) {
                    this.onImpact(new RayTraceResult(RayTraceResult.Type.BLOCK, new Vec3d(0, 0, 0), null, null));
                    return;
                }

                this.setDead();
                if (world instanceof WorldServer) {
                    WorldServer worldServer = (WorldServer) world;
                    worldServer.spawnParticle(EnumParticleTypes.LAVA, posX, posY, posZ, 20, 0, 0, 0, 1, new int[0]);
                    worldServer.playSound(null, new BlockPos(posX, posY, posZ), SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.PLAYERS, 1, 1);
                    if (thrower instanceof EntityPlayer) {
                        ((EntityPlayer) thrower).sendStatusMessage(new TextComponentTranslation("tooltip.rope.succ", result.entityHit.getDisplayName().getFormattedText()).setStyle(new Style().setColor(TextFormatting.GREEN)), true);
                    }
                }

                ItemStack is = new ItemStack(ItemRegistryHandler.itemRope, 1);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("data", result.entityHit.writeToNBT(new NBTTagCompound()));
                tag.setString("entity", EntityRegistry.getEntry(result.entityHit.getClass()).getRegistryName().toString());
                is.setTagCompound(tag);
                EntityItem eif = new EntityItem(world, posX, posY, posZ, is);
                eif.setPosition(thrower.posX,thrower.posY,thrower.posZ);
                world.spawnEntity(eif);
                result.entityHit.world.removeEntity(result.entityHit);
            }
            if (result.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                this.setDead();
                if (world instanceof WorldServer) {
                    WorldServer worldServer = (WorldServer) world;
                    worldServer.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 20, 0, 0, 0, 1, new int[0]);
                    worldServer.playSound(null, new BlockPos(posX, posY, posZ), SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.PLAYERS, 1, 1);
                    if (thrower instanceof EntityPlayer) {
                        ((EntityPlayer) thrower).sendStatusMessage(new TextComponentTranslation("tooltip.rope.fail").setStyle(new Style().setColor(TextFormatting.RED)), true);
                    }
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
            this.setDead();
        }
    }
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(aliveTime, 0);
    }

    public int getAliveTime() {
        return this.dataManager.get(aliveTime);
    }
    public void setAliveTime(int time) {
        this.dataManager.set(aliveTime, time);
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
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("aliveTime", getAliveTime());
        if (entityName!=null&&entityTag!=null){
            compound.setString("entity",entityName);
            compound.setTag("data",entityTag);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("aliveTime"))
            setAliveTime(compound.getInteger("aliveTime"));
        if (compound.hasKey("data"))
            entityTag=compound.getCompoundTag("data");
        if (compound.hasKey("entity"))
            entityName=compound.getString("entity");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        int liveTime=getAliveTime();
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
        setAliveTime(liveTime);
    }
}
