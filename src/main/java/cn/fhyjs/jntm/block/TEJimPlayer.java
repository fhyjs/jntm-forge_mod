package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.interfaces.IAnimatedTile;
import cn.fhyjs.jntm.utility.AnimationHelper;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockSeaLantern;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TEJimPlayer extends TileEntity implements ITickable, IAnimatedTile {
    public String count="12312312aaasssqqqqwwww";
    public boolean f1=false;
    protected static final float ANIM_TIME = 0.5F;
    protected TimeValues.VariableValue openTime;
    @Nullable
    public IAnimationStateMachine asm;
    @CapabilityInject(IAnimationStateMachine.class)
    public static Capability<IAnimationStateMachine> ANIMATION_CAPABILITY = null;
    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }
    public TEJimPlayer(){
        openTime = new TimeValues.VariableValue(-1);
        if (CommonProxy.McInited) {
            asm = Jntm.proxy.loadAsm(new ResourceLocation(Jntm.MODID, "asms/block/jimplayer.json"),ImmutableMap.<String, ITimeValue>of("anim_time", new TimeValues.VariableValue(ANIM_TIME), "open_time", openTime));
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
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityAnimation.ANIMATION_CAPABILITY)
            return true;
        else
            return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityAnimation.ANIMATION_CAPABILITY)
            return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
        else
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
    public void onOpenInventory() {
        if (!world.isRemote) {
            world.playSound(null, pos, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, 1.0F);
            world.addBlockEvent(pos, getBlockType(), 1, 1);
        }
    }

    public void onCloseInventory() {
        if (!world.isRemote) {
            world.playSound(null, pos, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, 1.0F);
            world.addBlockEvent(pos, getBlockType(), 1, 0);
        }
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
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            float time = Animation.getWorldTime(world, Animation.getPartialTickTime());
            float partialProgress = openTime.apply(time) < 0.0F ? 0.0F :
                    MathHelper.clamp(ANIM_TIME - (time - openTime.apply(time)), 0.0F, ANIM_TIME);
            openTime.setValue(time - partialProgress);
            AnimationHelper.transitionSafely(asm, type == 1 ? "opening" : "closing");
            return true;
        }

        return super.receiveClientEvent(id,type);
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
    public void handleEvents(float time, Iterable<Event> pastEvents){
            Jntm.logger.debug(this+"handleEvents");
    }
    @Override
    public void update() {

    }
}
