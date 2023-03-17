package cn.fhyjs.jntm.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SCINMessage implements IMessage {
    public String a;
    public SCINMessage() {}
    public SCINMessage(String a) {
        this.a = a;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,a);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.a = ByteBufUtils.readUTF8String(buf);
    }
}
