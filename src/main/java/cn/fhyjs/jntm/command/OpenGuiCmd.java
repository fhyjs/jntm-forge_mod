package cn.fhyjs.jntm.command;

import cn.fhyjs.jntm.utility.TelnetServer;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandTP;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenGuiCmd extends CommandBase {
    @Override
    public String getName() {
        return "opengui";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/opengui player modID guiID x y z";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            List<EntityPlayerMP> l = getPlayers(server, sender, args[0]);
            for (EntityPlayerMP entityPlayerMP : l) {
                entityPlayerMP.openGui(Loader.instance().getIndexedModList().get(args[1]).getMod(), parseInt(args[2]), entityPlayerMP.world, parseInt(args[3]), parseInt(args[4]), parseInt(args[5]));
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
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length > 3 && args.length <= 6)
        {
            return getTabCompletionCoordinate(args, 3, targetPos);
        }
        else if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else if (args.length == 2)
        {
            List<String> mn = new ArrayList<>();
            for (ModContainer modContainer : Loader.instance().getActiveModList()) {
                mn.add(modContainer.getModId());
            }
            return getListOfStringsMatchingLastWord(args,mn);
        }
        return Collections.emptyList();
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
