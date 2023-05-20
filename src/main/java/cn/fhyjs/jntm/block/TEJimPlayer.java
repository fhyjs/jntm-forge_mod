package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockSeaLantern;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TEJimPlayer extends TileEntity implements ITickable {
    public String count="12312312aaasssqqqqwwww";
    public boolean f1=false;
    public static TimeValues.VariableValue ticksValue = new TimeValues.VariableValue(100f);
    @Nullable
    public IAnimationStateMachine asm;
    @CapabilityInject(IAnimationStateMachine.class)
    private static Capability<IAnimationStateMachine> ANIMATION_CAPABILITY;
    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }
    public TEJimPlayer(){
        if (CommonProxy.McInited) {
            asm = Jntm.proxy.loadAsm(new ResourceLocation(Jntm.MODID, "asms/block/jimplayer.json"), ImmutableMap.of("blowing_cycle", TEJimPlayer.ticksValue));
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setBoolean("b",this.f1);
        compound.setString("s", this.count);
        markDirty();
        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.f1 = compound.getBoolean("b");
        this.count = compound.getString("s");
        // JOptionPane.showMessageDialog(null, count);
    }
    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        return capability == ANIMATION_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if(capability == ANIMATION_CAPABILITY) {
            return ANIMATION_CAPABILITY.cast(asm);
        }

        return super.getCapability(capability, facing);
    }
    public String getCount() {
        return this.count;
    }

    public void Seturl(String e) {
        this.count=e;
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
    public void handleEvents(float time, Iterable<Event> pastEvents){
        for (Event event : pastEvents) {
            Jntm.logger.error(111);
        }
    }

    @Override
    public void update() {

    }
}
