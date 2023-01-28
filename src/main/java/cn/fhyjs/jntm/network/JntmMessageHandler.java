package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.gui.Jntm_help_container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.patchouli.common.item.PatchouliItems;

public class JntmMessageHandler implements IMessageHandler<JntmMessage, IMessage> {
    private static ItemStack item1;
    private static NBTTagCompound nbtTagCompoundl =new NBTTagCompound();
    /*
    0:关闭当前GUI
    1:获取帮助手册

    */
    @Override
    public IMessage onMessage(JntmMessage message, MessageContext ctx) {
        System.out.println(message.a);
        if (message.a==0) {
            ctx.getServerHandler().player.closeContainer();
            ctx.getServerHandler().player.closeScreen();
        }
        if (message.a==1) {
            item1 = PatchouliItems.book.getDefaultInstance();
            nbtTagCompoundl.setString("patchouli:book", "jntm:jntm");
            item1.setTagCompound(nbtTagCompoundl);
            ctx.getServerHandler().player.inventory.addItemStackToInventory(item1);
            Jntm_help_container.open=false;
        }
        if (message.a==2){
            ctx.getServerHandler().player.closeContainer();
            ctx.getServerHandler().player.closeScreen();
            ctx.getServerHandler().player.openGui(Jntm.instance, JntmGuiHandler.GUIs.Insting.getId(), ctx.getServerHandler().player.world, (int) ctx.getServerHandler().player.posX, (int) ctx.getServerHandler().player.posY, (int) ctx.getServerHandler().player.posZ);
        }
        return null;
    }

}
