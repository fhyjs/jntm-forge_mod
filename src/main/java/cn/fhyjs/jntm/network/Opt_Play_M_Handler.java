package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.block.TEJimPlayer;
import cn.fhyjs.jntm.block.TileEntityCxkImage;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.common.pstest;
import cn.fhyjs.jntm.gui.RSMPlayerG;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

public class Opt_Play_M_Handler implements IMessageHandler<Opt_Ply_Message, IMessage> {


    @Override
    public IMessage onMessage(Opt_Ply_Message message, MessageContext ctx) {
        System.out.println(message.opt);
        if(ctx.side == Side.SERVER) {
            switch (message.opt.split(" ")[0]) {
                case "getalljim":
                    String MP;
                    StringBuilder tmp;
                {
                    String[] temp;
                    temp = Loader.instance().getConfigDir().toURI().toString().split("/");
                    tmp = new StringBuilder();
                    for (int i = 1; i < temp.length - 1; i++) {
                        tmp.append(temp[i]).append("/");
                    }
                    tmp.append("jims/");
                    MP = tmp.toString();

                }//路径处理
                File mf = new File(MP);
                if (!mf.exists()){
                    mf.mkdir();
                }else {
                    if (!mf.isDirectory()){
                        mf.delete();
                        mf.mkdir();
                    }
                }
                File[] listOfFiles = mf.listFiles();
                StringBuilder MF = new StringBuilder();
                for (File listOfFile : listOfFiles) {
                    System.out.println("File:" + listOfFile.getName());
                    MF.append(listOfFile.getName().replaceAll(" ","\n"));
                    MF.append(" ");
                }
                CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(message.a, "alljim "+ MF), ctx.getServerHandler().player);
                break;
                case "playjim":
                    String[] tmp1 =message.opt.split(" ");
                    BlockPos tbp = new BlockPos(Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]),Integer.parseInt(tmp1[4]));
                    if (CommonProxy.jimplayers.get(tbp)!=null)
                        CommonProxy.jimplayers.get(tbp).stop();
                    CommonProxy.jimplayers.put(tbp,new pstest(null,ctx.getServerHandler().player.world,new BlockPos(Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]),Integer.parseInt(tmp1[4])),new File((getrunpath("jims/")+tmp1[1]).replaceAll("\n"," "))));
                    CommonProxy.jimplayers.get(tbp).start();
                    break;
                case "stopjim":
                    tmp1 =message.opt.split(" ");
                    if (CommonProxy.jimplayers.get(new BlockPos(Integer.parseInt(tmp1[1]),Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3])))==null)
                        break;
                    CommonProxy.jimplayers.get(new BlockPos(Integer.parseInt(tmp1[1]),Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]))).stop();
                    break;
                case "playjim_setfilename":
                    tmp1 =message.opt.split(" ");
                    ((TEJimPlayer)ctx.getServerHandler().player.world.getTileEntity(new BlockPos(Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]),Integer.parseInt(tmp1[4])))).count=(tmp1[1]).replaceAll("\n"," ");
                    ((TEJimPlayer)ctx.getServerHandler().player.world.getTileEntity(new BlockPos(Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]),Integer.parseInt(tmp1[4])))).markDirty();
                    CommonProxy.INSTANCE.sendToAll(new Opt_Ply_Message(ctx.getServerHandler().player, message.opt));
                    break;
                case "upload":
                    tmp1 =message.opt.split(" ");
                    String tmp2 = tmp1[1].replaceAll("#*#*"," ");

                    break;
            }
        }
        if(ctx.side == Side.CLIENT) {
            String[] tmp =message.opt.split(" ");
            String[] tmp1 = new String[tmp.length];
            switch (tmp[0]){
                case "alljim":
                    for (int i=1;i< tmp.length;i++)
                        tmp1[i-1]=tmp[i].replaceAll("\n"," ");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    RSMPlayerG.fln=tmp1;
                    break;
                case "playjim_setfilename":
                    tmp1 =message.opt.split(" ");
                    ((TEJimPlayer)Minecraft.getMinecraft().world.getTileEntity(new BlockPos(Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]),Integer.parseInt(tmp1[4])))).count=tmp1[1].replaceAll("\n"," ");
                    ((TEJimPlayer)Minecraft.getMinecraft().world.getTileEntity(new BlockPos(Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]),Integer.parseInt(tmp1[4])))).markDirty();
                    break;
            }
        }
        return null;
    }
    public String getrunpath(String sp){
        String MP;
        StringBuilder tmp;
        String[] temp;
        temp = Loader.instance().getConfigDir().toURI().toString().split("/");
        tmp = new StringBuilder();
        for (int i = 1; i < temp.length - 1; i++) {
            tmp.append(temp[i]).append("/");
        }
        tmp.append(sp).append("/");
        MP = tmp.toString();
        return MP;
    }
}
