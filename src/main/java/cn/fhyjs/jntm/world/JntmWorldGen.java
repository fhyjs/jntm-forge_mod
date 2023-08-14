package cn.fhyjs.jntm.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class JntmWorldGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        //new JntmWorldGenTree(true).generate(world,random,new BlockPos(chunkX,world.getHeight(chunkX,chunkZ),chunkZ));
    }
}
