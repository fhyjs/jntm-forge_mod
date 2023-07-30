package cn.fhyjs.jntm.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateTileNBT implements IMessage {

    // 在这里定义需要同步的数据，例如 NBT Tag Compound
    // 假设 TE 类名为 MyTileEntity
    NBTTagCompound tagCompound;

    // 默认构造函数（必需）
    public PacketUpdateTileNBT() {
    }

    public PacketUpdateTileNBT(NBTTagCompound tagCompound) {
        this.tagCompound = tagCompound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tagCompound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tagCompound);
    }

}
