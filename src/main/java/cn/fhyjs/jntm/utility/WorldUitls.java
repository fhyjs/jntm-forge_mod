package cn.fhyjs.jntm.utility;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;

public class WorldUitls {
    public static boolean destroyBlock(World world, BlockPos pos, boolean dropBlock)
    {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block.isAir(iblockstate, world, pos))
        {
            return false;
        }
        else
        {
            world.playEvent(2001, pos, Block.getStateId(iblockstate));

            if (dropBlock)
            {
                block.dropBlockAsItem(world, pos, iblockstate, 0);
            }

            return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }
    public static boolean setBlockState(World world,BlockPos pos, IBlockState newState, int flags,boolean rep)
    {
        synchronized (world) {
            if (world.isOutsideBuildHeight(pos)) {
                return false;
            } else if (!world.isRemote && ((WorldInfo) ObfuscationReflectionHelper.getPrivateValue(World.class, world, "field_72986_A")).getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
                return false;
            } else {
                Chunk chunk = world.getChunkFromBlockCoords(pos);

                pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
                net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;
                if (world.captureBlockSnapshots && !world.isRemote) {
                    blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(world, pos, flags);
                    world.capturedBlockSnapshots.add(blockSnapshot);
                }
                IBlockState oldState = world.getBlockState(pos);
                int oldLight = oldState.getLightValue(world, pos);
                int oldOpacity = oldState.getLightOpacity(world, pos);

                IBlockState iblockstate = chunk.setBlockState(pos, newState);

                if (iblockstate == null) {
                    if (blockSnapshot != null) world.capturedBlockSnapshots.remove(blockSnapshot);
                    return false;
                } else {
                    if (newState.getLightOpacity(world, pos) != oldOpacity || newState.getLightValue(world, pos) != oldLight) {
                        world.profiler.startSection("checkLight");
                        world.checkLight(pos);
                        world.profiler.endSection();
                    }

                    if (blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                    {
                        markAndNotifyBlock(world, pos, chunk, iblockstate, newState, flags, rep);
                    }
                    return true;
                }
            }
        }
    }
    // Split off from original setBlockState(BlockPos, IBlockState, int) method in order to directly send client and physic updates
    public static void markAndNotifyBlock(World world,BlockPos pos, @Nullable Chunk chunk, IBlockState iblockstate, IBlockState newState, int flags,boolean rep) {
        synchronized (world) {
            Block block = newState.getBlock();
            {
                {
                    if ((flags & 2) != 0 && (!world.isRemote || (flags & 4) == 0) && (chunk == null || chunk.isPopulated())) {
                        world.notifyBlockUpdate(pos, iblockstate, newState, flags);
                    }
                    if (!rep) return;
                    if (!world.isRemote && (flags & 1) != 0) {
                        world.notifyNeighborsRespectDebug(pos, iblockstate.getBlock(), true);

                        if (newState.hasComparatorInputOverride()) {
                            world.updateComparatorOutputLevel(pos, block);
                        }
                    } else if (!world.isRemote && (flags & 16) == 0) {
                        world.updateObservingBlocksAt(pos, block);
                    }
                }
            }
        }
    }
}
