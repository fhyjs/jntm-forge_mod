package cn.fhyjs.jntm.command;

import cn.fhyjs.jntm.utility.WorldUitls;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IdSetblockCmd extends CommandSetBlock {
    @Override
    public String getName() {
        return "id_"+super.getName();
    }
        @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (args.length < 4)
            {
                throw new WrongUsageException("commands.setblock.usage", new Object[0]);
            }
            else
            {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
                BlockPos blockpos = parseBlockPos(sender, args, 0, false);
                Block block = Block.getBlockById(Integer.parseInt(args[3]));
                IBlockState iblockstate;

                if (args.length >= 5)
                {
                    iblockstate = convertArgToBlockState(block, args[4]);
                }
                else
                {
                    iblockstate = block.getDefaultState();
                }

                World world = sender.getEntityWorld();

                if (!world.isBlockLoaded(blockpos))
                {
                    throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
                }
                else
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    boolean flag = false;

                    if (args.length >= 7 && block.hasTileEntity(iblockstate))
                    {
                        String s = buildString(args, 6);

                        try
                        {
                            nbttagcompound = JsonToNBT.getTagFromJson(s);
                            flag = true;
                        }
                        catch (NBTException nbtexception)
                        {
                            throw new CommandException("commands.setblock.tagError", new Object[] {nbtexception.getMessage()});
                        }
                    }

                    if (args.length >= 6)
                    {
                        if ("destroy".equals(args[5]))
                        {
                            world.destroyBlock(blockpos, true);

                            if (block == Blocks.AIR)
                            {
                               // notifyCommandListener(sender, this, "commands.setblock.success", new Object[0]);
                                return;
                            }
                        }
                        else if ("keep".equals(args[5]) && !world.isAirBlock(blockpos))
                        {
                            throw new CommandException("commands.setblock.noChange", new Object[0]);
                        }
                    }

                    TileEntity tileentity1 = world.getTileEntity(blockpos);

                    if (tileentity1 != null && tileentity1 instanceof IInventory)
                    {
                        ((IInventory)tileentity1).clear();
                    }

                    if (!WorldUitls.setBlockState(world,blockpos, iblockstate, 2,false))
                    {
                        throw new CommandException("commands.setblock.noChange", new Object[0]);
                    }
                    else
                    {
                        if (flag)
                        {
                            TileEntity tileentity = world.getTileEntity(blockpos);

                            if (tileentity != null)
                            {
                                nbttagcompound.setInteger("x", blockpos.getX());
                                nbttagcompound.setInteger("y", blockpos.getY());
                                nbttagcompound.setInteger("z", blockpos.getZ());
                                tileentity.readFromNBT(nbttagcompound);
                            }
                        }

                        //world.notifyNeighborsRespectDebug(blockpos, iblockstate.getBlock(), false);
                        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                       // notifyCommandListener(sender, this, "commands.setblock.success", new Object[0]);
                    }
                }
            }
    }

}
