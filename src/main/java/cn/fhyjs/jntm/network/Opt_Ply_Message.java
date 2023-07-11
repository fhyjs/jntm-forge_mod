package cn.fhyjs.jntm.network;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class Opt_Ply_Message implements IMessage {
    public   String opt;
    public EntityPlayer a;
    public  String out;

    public Opt_Ply_Message() {
    }

    public Opt_Ply_Message(EntityPlayer a, String opt) {
        this.a = a;
        this.opt=opt;
    }

    @Override
    public void toBytes(ByteBuf buf){
        Gson gson = new Gson();
        User user = new User();
        if (a!=null)
            user.uuid= String.valueOf(a.getEntityId());
        user.opt=opt;
        out=gson.toJson(user);
        ByteBufUtils.writeUTF8String(buf,out);

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        Gson gson = new Gson();
        out = ByteBufUtils.readUTF8String(buf);
        User user = gson.fromJson(out, User.class);
        if (user.uuid!=null)
            if (FMLCommonHandler.instance().getSide()== Side.SERVER)
                a= (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getEntityByID(Integer.parseInt(user.uuid));
            else
                a= (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(Integer.parseInt(user.uuid));
        opt=user.opt;
    }
    private class User {
        //省略其它
        public String uuid;
        public String opt;
    }
}
