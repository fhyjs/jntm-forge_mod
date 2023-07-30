package cn.fhyjs.jntm.block;


import cn.fhyjs.jntm.interfaces.IhasMMDRender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import org.jetbrains.annotations.NotNull;

public class TEPmxModel extends TileEntity implements ITickable, IhasMMDRender {
    public String modelName,idleActionName,redStoneActionName,currentActionName;
    @Override
    public void update() {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        if (modelName!=null)
            compound.setString("modelName",modelName);
        if (currentActionName!=null)
            compound.setString("actionName",currentActionName);
        if (idleActionName!=null)
            compound.setString("idleActionName",idleActionName);
        if (redStoneActionName!=null)
            compound.setString("redStoneActionName",redStoneActionName);

        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        modelName=compound.getString("modelName");
        currentActionName=compound.getString("actionName");
        redStoneActionName=compound.getString("redStoneActionName");
        idleActionName=compound.getString("idleActionName");
    }
    @Override
    public @NotNull NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 4, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public String getActionName() {
        return currentActionName;
    }
}
