package cn.fhyjs.jntm.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandShowSeed;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GetSideCmd extends CommandBase {
    @Override
    public String getName() {
        return "side";
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        sender.sendMessage(new TextComponentString(FMLCommonHandler.instance().getSide().name()));
    }
    @Override
    public String getUsage(ICommandSender sender)
    {
        return "usage: /side";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
