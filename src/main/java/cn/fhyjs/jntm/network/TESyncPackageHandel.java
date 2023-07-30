package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

public class TESyncPackageHandel implements IMessageHandler<PacketUpdateTileNBT, IMessage> {
    // 创建一个消息处理器类，用于处理客户端接收到的网络包
        @Override
        public IMessage onMessage(PacketUpdateTileNBT message, MessageContext ctx) {
            int x = message.tagCompound.getInteger("x");
            int y = message.tagCompound.getInteger("y");
            int z = message.tagCompound.getInteger("z");
            if (ctx.side== Side.SERVER) {
                Objects.requireNonNull(ctx.getServerHandler().player.world.getTileEntity(new BlockPos(x, y, z))).handleUpdateTag(message.tagCompound);
                for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                    CommonProxy.INSTANCE.sendTo(message, player);
                }
            } else if (ctx.side == Side.CLIENT) {
                Objects.requireNonNull(Minecraft.getMinecraft().world.getTileEntity(new BlockPos(x, y, z))).handleUpdateTag(message.tagCompound);
            }
            return null;
        }

}
