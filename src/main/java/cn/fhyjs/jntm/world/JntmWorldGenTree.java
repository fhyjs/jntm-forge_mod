package cn.fhyjs.jntm.world;

import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class JntmWorldGenTree  extends WorldGenTrees {
    public JntmWorldGenTree(boolean p_i2027_1_) {
        super(p_i2027_1_);
    }

    public JntmWorldGenTree(boolean notify, int minTreeHeightIn, IBlockState woodMeta, IBlockState p_i46446_4_, boolean growVines) {
        super(notify, minTreeHeightIn, woodMeta, p_i46446_4_, growVines);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        position=worldIn.getHeight(position);
        EntityTNTPrimed e = new EntityTNTPrimed(worldIn);
        e.setPosition(position.getX(),position.getY(),position.getZ());
        worldIn.spawnEntity(e);
        worldIn.setBlockState(position.up(5), Blocks.TNT.getDefaultState());
        return true;
    }
}
