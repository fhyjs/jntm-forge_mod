package cn.fhyjs.jntm.network;

import io.netty.buffer.ByteBuf;
import jdk.nashorn.internal.runtime.NumberToString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class JntmMessage implements IMessage {
    public int a;
    public JntmMessage() {}
    public JntmMessage(int a) {
        this.a = a;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(a);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.a = buf.readInt();
    }
}
