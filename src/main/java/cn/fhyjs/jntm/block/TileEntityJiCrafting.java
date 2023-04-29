package cn.fhyjs.jntm.block;


import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TileEntityJiCrafting extends TileEntity {
    private static final int FILTER_LIST_SIZE = 9;
    private static final String FILTER_LIST_TAG = "ItemFilterList";
    private NBTTagCompound tag=new NBTTagCompound();
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setTag(FILTER_LIST_TAG,getTag());
        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        setTag(compound.getCompoundTag(FILTER_LIST_TAG));
    }
    public ItemStackHandler getFilterList() {
        WirelessIOHandler handler = new WirelessIOHandler(FILTER_LIST_SIZE);
        if (getTag() != null) {
            handler.deserializeNBT(getTag());
        }
        this.markDirty();
        return handler;
    }
    public void setFilterList(ItemStackHandler itemStackHandler) {
        setTag(itemStackHandler.serializeNBT());
        this.markDirty();
    }
    public void setTag(NBTTagCompound n){
        this.tag=n;
    }
    public NBTTagCompound getTag(){
        return this.tag;
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
    private static class WirelessIOHandler extends ItemStackHandler {
        private WirelessIOHandler(int size) {
            super(size);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }
}
