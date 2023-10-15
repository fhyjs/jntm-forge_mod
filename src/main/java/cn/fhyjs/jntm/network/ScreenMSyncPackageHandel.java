package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.screen.ScreenM;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

public class ScreenMSyncPackageHandel implements IMessageHandler<PacketUpdateScreenM, IMessage> {
    // 创建一个消息处理器类，用于处理客户端接收到的网络包
        @Override
        public IMessage onMessage(PacketUpdateScreenM message, MessageContext ctx) {
            if (ctx.side== Side.SERVER) {

            } else if (ctx.side == Side.CLIENT) {
                ScreenM screenM = new ScreenM("jntm_screens");
                screenM.readFromNBT(message.tagCompound);
                ObfuscationReflectionHelper.setPrivateValue(ScreenM.class,null,screenM,"Instance");
            }
            return null;
        }

}
