package cn.fhyjs.jntm.block;


import com.google.gson.Gson;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TELandmine extends TileEntity implements ITickable {
    public double Thickness=0.1;
    public boolean IsTriggered=false,TPlayer,CTriggered;
    public List<Class<? extends Entity>> Triggers = new ArrayList<>();
    public int Fuse,CFuse;
    @Override
    public void update() {
        if (world.isRemote) return;
        List<Entity> EntitiesOn = new ArrayList<>(world.getEntitiesWithinAABBExcludingEntity(null,new AxisAlignedBB(getPos().getX(),getPos().getY(),getPos().getZ(),getPos().getX()+1,getPos().getY()+getThickness()+0.05, getPos().getZ()+1)));
        //System.out.println(EntitiesOn);
        IsTriggered=false;
        for (Entity entity : EntitiesOn) {
            if (TPlayer&&(entity.getClass()== EntityPlayerSP.class|| entity.getClass() == EntityPlayerMP.class)) {
                IsTriggered = true;
                break;
            }
            if (Triggers.contains(entity.getClass())) {
                IsTriggered = true;
                break;
            }
        }
        checkbooms();
        if (IsTriggered){

        }
    }
    public void checkbooms(){
        if (CTriggered!=IsTriggered){
            if (CTriggered){ // 1 -> 0
                System.out.println("UP");
            }
            if (!CTriggered){// 0 -> 1
                System.out.println("Down");

            }
            CTriggered=IsTriggered;
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
