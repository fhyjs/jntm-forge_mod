package cn.fhyjs.jntm.block;


import cn.fhyjs.jntm.Jntm;
import javafx.embed.swing.JFXPanel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.swing.*;

public class TileEntityCxkImage extends TileEntity implements ITickable {

    public String count="";

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setString("url", this.count);
        markDirty();
        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.count = compound.getString("url");
       // JOptionPane.showMessageDialog(null, count);
    }

    public String getCount() {
        return this.count;
    }

    public void Seturl() {

        markDirty();
    }
    private IBlockState getState() {
        return world.getBlockState(pos);
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

    private void sendUpdates() {
        getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
        getWorld().notifyBlockUpdate(getPos(), getState(), getState(), 3);
        getWorld().scheduleBlockUpdate(getPos(),this.getBlockType(),0,0);
        markDirty();
    }
    @SideOnly(Side.CLIENT)
    public void receiveUpdatePacket(NBTTagCompound nbt) {

    }
    @Override
    public void update() {
        sendUpdates();
    }
}
