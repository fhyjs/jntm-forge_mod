package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TEJimPlayer;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.common.pstest;
import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.gui.LandMineConC;
import cn.fhyjs.jntm.gui.RSMPlayerG;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.fhyjs.jntm.Jntm.proxy;

public class Opt_Play_M_Handler implements IMessageHandler<Opt_Ply_Message, IMessage> {
    public Map<EntityPlayer,String[]> uploader=new HashMap<>();
    public Map<EntityPlayerMP, String> uploader_tmp=new HashMap<EntityPlayerMP, String>();
    public Opt_Play_M_Handler(){
        super();
    }
    @Override
    public IMessage onMessage(Opt_Ply_Message message, MessageContext ctx) {
        message.opt=message.opt.replace("\r","");
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
                    ((TEJimPlayer) Objects.requireNonNull(ctx.getServerHandler().player.world.getTileEntity(new BlockPos(Integer.parseInt(tmp1[2]), Integer.parseInt(tmp1[3]), Integer.parseInt(tmp1[4]))))).count=(tmp1[1]).replaceAll("\n"," ");
                    ((TEJimPlayer) Objects.requireNonNull(ctx.getServerHandler().player.world.getTileEntity(new BlockPos(Integer.parseInt(tmp1[2]), Integer.parseInt(tmp1[3]), Integer.parseInt(tmp1[4]))))).markDirty();
                    CommonProxy.INSTANCE.sendToAll(new Opt_Ply_Message(ctx.getServerHandler().player, message.opt));
                    break;
                case "upload":
                    tmp1 =message.opt.split(" ");
                    if (!ConfigCore.isenabledUP){
                        CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(ctx.getServerHandler().player, "toast gui.toast.unallow"),ctx.getServerHandler().player);
                        break;
                    }
                    String tmp2 = tmp1[2];
                    String[] t3 = uploader.get(ctx.getServerHandler().player);
                    t3[Integer.parseInt(tmp1[1])]=tmp2;
                    uploader.put(ctx.getServerHandler().player,t3);
                    CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(ctx.getServerHandler().player, "reply upload "+tmp1[1]),ctx.getServerHandler().player);
                    break;
                case "pre_upload":
                    if (!ConfigCore.isenabledUP){
                        CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(ctx.getServerHandler().player, "toast gui.toast.unallow"),ctx.getServerHandler().player);
                        break;
                    }
                    tmp1 =message.opt.split(" ");
                    uploader.put(ctx.getServerHandler().player,new String[Integer.parseInt(tmp1[1])]);
                    CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(ctx.getServerHandler().player, "reply "+message.opt),ctx.getServerHandler().player);
                    break;
                case "end_upload":
                    if (!ConfigCore.isenabledUP){
                        CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(ctx.getServerHandler().player, "toast gui.toast.unallow"),ctx.getServerHandler().player);
                        break;
                    }
                    tmp1 =message.opt.split(" ");
                    t3 = uploader.get(ctx.getServerHandler().player);
                    tmp2="";
                    for (String s:t3){
                        tmp2+=s;
                    }
                    uploader_tmp.put(ctx.getServerHandler().player,tmp2.replaceAll("#*#*"," "));
                    uploader.remove(ctx.getServerHandler().player);
                    CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(ctx.getServerHandler().player, "reply ok"),ctx.getServerHandler().player);
                    break;
                case "write_to_file":
                    if (!ConfigCore.isenabledUP){
                        CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(ctx.getServerHandler().player, "toast gui.toast.unallow"),ctx.getServerHandler().player);
                        break;
                    }
                    tmp1 =message.opt.split(" ");
                    String path=tmp1[1];
                    boolean overwrite = true;
                    if (Objects.equals(tmp1[1], "jim")) {
                        path = getrunpath("jims/");
                        overwrite=false;
                    }
                    try {
                        CommonProxy.fileManager.write_file(path+tmp1[2],uploader_tmp.get(ctx.getServerHandler().player),overwrite);
                    } catch (IOException e) {
                        Jntm.logger.error(new RuntimeException(e));
                    }
                    CommonProxy.INSTANCE.sendTo(new Opt_Ply_Message(ctx.getServerHandler().player, "reply ok"),ctx.getServerHandler().player);
                    break;
                case "setlandminedata":{
                    if (!(ctx.getServerHandler().player.openContainer instanceof LandMineConC)) break;
                    tmp1 =message.opt.split(" ");
                    try {
                        ((LandMineConC) ctx.getServerHandler().player.openContainer).stores.setActivityTab("default");
                        ((LandMineConC) ctx.getServerHandler().player.openContainer).LandmineShot.getStack().setTagCompound(JsonToNBT.getTagFromJson(tmp1[1]));
                    } catch (Throwable e) {
                        Jntm.logger.warn(new RuntimeException(e));
                    }
                    break;
                }
                case "clearlandminepulgin":{
                    if (!(ctx.getServerHandler().player.openContainer instanceof LandMineConC)) break;
                    try {
                        ((LandMineConC) ctx.getServerHandler().player.openContainer).clearplugin();
                    } catch (Throwable e) {
                        StringWriter sw=new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        Jntm.logger.warn(sw);
                    }
                    break;
                }
                case "setlandminedataexit":{
                    if (!(ctx.getServerHandler().player.openContainer instanceof LandMineConC)) break;
                    tmp1 =message.opt.split(" ");
                    try {
                        ((LandMineConC) ctx.getServerHandler().player.openContainer).stores.setActivityTab("default");
                        ((LandMineConC) ctx.getServerHandler().player.openContainer).LandmineShot.getStack().setTagCompound(JsonToNBT.getTagFromJson(tmp1[1]));
                        ctx.getServerHandler().player.closeContainer();
                        ctx.getServerHandler().player.closeScreen();
                    } catch (Throwable e) {
                        Jntm.logger.warn(new RuntimeException(e));
                    }
                    break;
                }
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
                    if ((Minecraft.getMinecraft().currentScreen)instanceof RSMPlayerG)
                        ((RSMPlayerG)Minecraft.getMinecraft().currentScreen).fln=tmp1;
                    break;
                case "playjim_setfilename":
                    tmp1 =message.opt.split(" ");
                    ((TEJimPlayer) Objects.requireNonNull(Minecraft.getMinecraft().world.getTileEntity(new BlockPos(Integer.parseInt(tmp1[2]), Integer.parseInt(tmp1[3]), Integer.parseInt(tmp1[4]))))).count=tmp1[1].replaceAll("\n"," ");
                    ((TEJimPlayer)Minecraft.getMinecraft().world.getTileEntity(new BlockPos(Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]),Integer.parseInt(tmp1[4])))).markDirty();
                    break;
                case "reply":
                    tmp1 =message.opt.split(" ");
                    if ((Minecraft.getMinecraft().currentScreen)instanceof RSMPlayerG)
                        ((RSMPlayerG)Minecraft.getMinecraft().currentScreen).reply=message.opt.substring(6);
                    break;
                case "toast":
                    tmp1 =message.opt.split(" ");
                    proxy.showToase("TUTORIAL_HINT",tmp1[1],"gui.toast.guifaild");

                    break;
            }
        }
        return null;
    }
    public static String reply ;
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
