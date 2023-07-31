package cn.fhyjs.jntm.block;


import cn.fhyjs.jntm.interfaces.IhasMMDRender;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;
@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TEPmxModel extends TileEntity implements ITickable, IhasMMDRender, SimpleComponent {
    public String modelName,idleActionName,redStoneActionName,currentActionName="idle";
    public boolean oldstate;
    @Override
    public void update() {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TEPmxModel){
            if (world.isBlockPowered(pos)!=oldstate) {
                oldstate=world.isBlockPowered(pos);
                if (oldstate){
                    this.currentActionName=redStoneActionName;
                }else {
                    this.currentActionName=idleActionName;
                }
            }
        }
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
    @Override
    public String getComponentName() {
        return "fancy_thing";
    }
    @Callback
    @Optional.Method(modid = "opencomputers")
    public Object[] greet(Context context, Arguments args) {
        return new Object[]{String.format("Hello, %s!", args.checkString(0))};
    }

}
