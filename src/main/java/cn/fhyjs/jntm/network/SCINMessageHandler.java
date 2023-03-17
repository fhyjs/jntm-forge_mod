package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.gui.Jntm_help_container;
import com.google.gson.Gson;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.patchouli.common.item.PatchouliItems;

public class SCINMessageHandler implements IMessageHandler<SCINMessage, IMessage> {
    private static ItemStack item1;
    private static NBTTagCompound nbtTagCompoundl =new NBTTagCompound();
    @Override
    public IMessage onMessage(SCINMessage message, MessageContext ctx) {
        System.out.println(message.a);
        Gson g=new Gson();
        ctx.getServerHandler().player.world.getTileEntity(g.fromJson(message.a,Type))
        return null;
    }

}
class Student {
    private String url;
    private int x,y,z;
    public Student(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString() {
        return "Student [ name: "+name+", age: "+ age+ " ]";
    }
}
