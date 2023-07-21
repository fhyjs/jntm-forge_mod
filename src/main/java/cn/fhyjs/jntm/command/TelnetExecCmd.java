package cn.fhyjs.jntm.command;

import cn.fhyjs.jntm.utility.TelnetServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class TelnetExecCmd extends CommandBase {
    @Override
    public String getName() {
        return "jtnrun";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/jtnrun <func_name> [arg1] [arg2] [argn]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length<1){
            sender.sendMessage(new TextComponentString("useage:"+getUsage(sender)));
        }
        String arga = "";
        for (String arg : args) {
            arga+=" ";
            arga+=arg;
        }
        TelnetServer.funcs.get(args[0]).send("#"+arga.substring(1));
    }
}
