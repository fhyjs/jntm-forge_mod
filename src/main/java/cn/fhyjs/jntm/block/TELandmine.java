package cn.fhyjs.jntm.block;


import cn.fhyjs.jntm.common.Ji_Exposion;
import com.google.gson.Gson;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TELandmine extends TileEntity implements ITickable {
    public double Thickness=0.1;
    public float Explosion_Strength;
    public boolean IsTriggered=false,TPlayer,CTriggered,Mode,Booming,HasCPos,Broadcast,Explosion;// Mode 1:按下时激发,0:松开时激发;HasCPos 1:有伪装,0:无伪装
    public List<Class<? extends Entity>> Triggers = new ArrayList<>();
    public int Fuse,CFuse;
    public String model;
    public BlockPos CamouflagePos;
    @Override
    public void update() {
        if (world.isRemote) return;
        if (getBlockType() instanceof BlockLandmine)
            ((BlockLandmine) getBlockType()).check(world,pos);
        List<Entity> EntitiesOn = new ArrayList<>(world.getEntitiesWithinAABBExcludingEntity(null,new AxisAlignedBB(getPos().getX(),getPos().getY(),getPos().getZ(),getPos().getX()+1,getPos().getY()+getThickness()+0.05, getPos().getZ()+1)));
        //System.out.println(EntitiesOn);
        IsTriggered=false;
        for (Entity entity : EntitiesOn) {
            if (TPlayer&&(entity.getClass() == EntityPlayerMP.class||entity.getClass()== EntityPlayerSP.class)) {
                IsTriggered = true;
                break;
            }
            if (Triggers.contains(entity.getClass())) {
                IsTriggered = true;
                break;
            }
        }
        checkbooms();
        if (Booming){
            boom();
        }
    }
    private void boom() {
        WorldServer worldServer = (WorldServer) world;
        if (CFuse>=0){
            CFuse--;
            worldServer.spawnParticle(EnumParticleTypes.SMOKE_LARGE,true, pos.getX()+0.5, pos.getY()+getThickness(), pos.getZ()+0.5, 1, 0, 0, 0d,0.02,0);
        }else {
            if (Broadcast) {
                FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(),"say "+ I18n.translateToLocalFormatted("tip.jntm.landmine.broadcast",
                        world.getBlockState(new BlockPos(pos.getX(),pos.getY(),pos.getZ())).getBlock().getLocalizedName(),
                        pos.getX(),
                        pos.getY(), 
                        pos.getZ()
                ));
            }
            if (Explosion) {
                Ji_Exposion.createExplosion(worldServer,null,pos.getX(),pos.getY(),pos.getZ(),Explosion_Strength,true);
            }
            Booming=false;
        }
        markDirty();
    }
    public void checkbooms(){
        if (Booming) return;
        if (CTriggered!=IsTriggered){
            world.playSound(null,getPos(), IsTriggered?SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON:SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS,.3f,.8f);
            if (CTriggered){ // 1 -> 0
                if (!Mode){
                    CFuse=Fuse;
                    Booming=true;
                }
                //System.out.println("UP");
            }
            if (!CTriggered){// 0 -> 1
                if (Mode){
                    CFuse=Fuse;
                    Booming=true;
                }
                //System.out.println("Down");

            }
            CTriggered=IsTriggered;
            markDirty();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setDouble("Thickness",getThickness());
        compound.setBoolean("IsTriggered",IsTriggered);
        compound.setBoolean("Tplayer",TPlayer);
        compound.setBoolean("CTriggered",CTriggered);
        List<String> es = new ArrayList<>();
        for (Class<? extends Entity> trigger : Triggers) {
            ResourceLocation rl = EntityList.getKey(trigger);
            if (rl==null||!EntityList.isRegistered(rl)) continue;
            es.add(rl.toString());
        }
        compound.setString("Triggers",new Gson().toJson(es));
        compound.setInteger("Fuse",Fuse);
        compound.setInteger("CFuse",CFuse);
        compound.setBoolean("Mode",Mode);
        compound.setBoolean("Booming",Booming);
        compound.setBoolean("HasCP",HasCPos);
        if (CamouflagePos!=null)
            compound.setIntArray("CamouflagePos",new int[]{CamouflagePos.getX(), CamouflagePos.getY(),CamouflagePos.getZ()});
        compound.setBoolean("Broadcast",Broadcast);
        compound.setBoolean("Explosion",Explosion);
        compound.setFloat("Explosion_Strength",Explosion_Strength);

        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        setThickness(compound.getDouble("Thickness"));
        IsTriggered=compound.getBoolean("IsTriggered");
        CTriggered=compound.getBoolean("CTriggered");
        TPlayer=compound.getBoolean("Tplayer");
        List<String> es = new Gson().fromJson(compound.getString("Triggers"),List.class);
        Triggers.clear();
        for (String e : es) {
            Class<? extends Entity> cl = EntityList.getClassFromName(e);
            if (cl==null) continue;
            Triggers.add(cl);
        }
        CFuse= compound.getInteger("CFuse");
        Fuse= compound.getInteger("Fuse");
        Mode= compound.getBoolean("Mode");
        Booming= compound.getBoolean("Booming");
        HasCPos= compound.getBoolean("HasCP");
        int[] ia = compound.getIntArray("CamouflagePos");
        if (ia.length>0)
            CamouflagePos=new BlockPos(ia[0],ia[1],ia[2]);
        else
            CamouflagePos=null;
        Broadcast=compound.getBoolean("Broadcast");
        Explosion=compound.getBoolean("Explosion");
        Explosion_Strength=compound.getFloat("Explosion_Strength");
    }

    public void setThickness(double thickness) {
        Thickness = thickness;
    }

    public double getThickness() {
        return Thickness;
    }
    @Override
    public @NotNull NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 4, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }
}
