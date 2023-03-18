package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityCxkImage;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.gui.Jntm_help_container;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.patchouli.common.item.PatchouliItems;

public class SCINMessageHandler implements IMessageHandler<SCINMessage, IMessage> {
    private static ItemStack item1;
    private static NBTTagCompound nbtTagCompoundl =new NBTTagCompound();
    @Override
    public IMessage onMessage(SCINMessage message, MessageContext ctx) {
        System.out.println(message.a);
        Gson g=new Gson();
        Student s = g.fromJson(message.a, Student.class);
        if(ctx.side == Side.SERVER) {
            TileEntityCxkImage te = (TileEntityCxkImage) ctx.getServerHandler().player.world.getTileEntity(new BlockPos(s.x, s.y, s.z));
            if (te != null) {
                te.Seturl(s.url);
            }
            CommonProxy.INSTANCE.sendToAll(new SCINMessage(message.a));
        }
        if(ctx.side == Side.CLIENT) {
            TileEntityCxkImage te = (TileEntityCxkImage) Minecraft.getMinecraft().world.getTileEntity(new BlockPos(s.x, s.y, s.z));
            if (te != null) {
                te.Seturl(s.url);
            }
        }
        return null;
    }

}
class Student {
    public String url;
    public int x,y,z;
    public Student(){}
}
