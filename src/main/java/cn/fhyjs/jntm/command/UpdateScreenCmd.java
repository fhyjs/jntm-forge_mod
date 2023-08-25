package cn.fhyjs.jntm.command;

import cn.fhyjs.jntm.screen.ScreenM;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UpdateScreenCmd extends CommandBase {
    @Override
    public String getName() {
        return "screenm";
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try{
            if (args[0].equals("getid")){
                sender.sendMessage(new TextComponentString(String.valueOf(ScreenM.getInstance().screenAt(new BlockPos(parseInt(args[1]),parseInt(args[2]),parseInt(args[3]))).getId())));
            }else if (args[0].equals("size")){
                sender.sendMessage(new TextComponentString(String.valueOf(ScreenM.getInstance().screens.size())));
            }
        }catch (Throwable e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            sender.sendMessage(new TextComponentString(sw.toString().replaceAll("\t","    ").replaceAll("\r","")).setStyle(new Style().setColor(TextFormatting.RED)));
            sender.sendMessage(new TextComponentString(e.toString()).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
            if (e instanceof ArrayIndexOutOfBoundsException)
                sender.sendMessage(new TextComponentString(getUsage(sender)).setStyle(new Style().setColor(TextFormatting.GREEN)));
        }
    }
    @Override
    public String getUsage(ICommandSender sender)
    {
        return "usage: /screenm";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
