package cn.fhyjs.jntm.screen;

import cn.fhyjs.jntm.item.ItemScreenBuilder;
import cn.fhyjs.jntm.utility.BlockDrawsGenerator;
import cn.fhyjs.jntm.utility.WorldUitls;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Screen {
    public final World world;
    public BlockPos pos;
    public int width, height;
    public EnumFacing.Axis facing;
    public Screen(BlockPos bp, int w, int h, EnumFacing.Axis facing, World world) {
        this.pos=bp;
        this.width=w;
        this.height = h;
        this.facing=facing;
        this.world=world;
    }
    public Screen(NBTTagCompound nbtTagCompound){
        pos=ItemScreenBuilder.readBlockFromNBT(nbtTagCompound,"pos");
        width=nbtTagCompound.getInteger("width");
        height=nbtTagCompound.getInteger("height");
        facing= EnumFacing.Axis.byName(nbtTagCompound.getString("facing"));
        world=FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(nbtTagCompound.getInteger("dim"));
    }
    public void init(){
        switch (facing) {
            case X:
                for (int z= pos.getZ();z<width+ pos.getZ();z++)
                    for (int y= pos.getY();y<height+ pos.getY();y++)
                        world.setBlockState(new BlockPos(pos.getX(),y,z), Blocks.WOOL.getDefaultState(), Constants.BlockFlags.SEND_TO_CLIENTS| Constants.BlockFlags.RERENDER_MAIN_THREAD);
                break;
            case Y:
                for (int x= pos.getX();x<width+ pos.getX();x++)
                    for (int z= pos.getZ();z<height+ pos.getZ();z++)
                        world.setBlockState(new BlockPos(x,pos.getY(),z), Blocks.WOOL.getDefaultState(),Constants.BlockFlags.SEND_TO_CLIENTS| Constants.BlockFlags.RERENDER_MAIN_THREAD);
                break;
            case Z:
                for (int x= pos.getX();x<width+ pos.getX();x++)
                    for (int y= pos.getY();y<height+ pos.getY();y++)
                        world.setBlockState(new BlockPos(x,y, pos.getZ()), Blocks.WOOL.getDefaultState(),Constants.BlockFlags.SEND_TO_CLIENTS| Constants.BlockFlags.RERENDER_MAIN_THREAD);
                break;
        }
        ScreenM.getInstance().generators.put(this,new BlockDrawsGenerator(this));
    }
    public NBTTagCompound getNbtTag(){
        NBTTagCompound nbt = new NBTTagCompound();
        ItemScreenBuilder.writeBlockPos2NBT(nbt,"pos",pos);
        nbt.setString("facing",facing.toString());
        nbt.setInteger("width",width);
        nbt.setInteger("height",height);
        nbt.setInteger("dim",world.provider.getDimension());
        return nbt;
    }
    public int getId(){
        if (ScreenM.getInstance().screens.contains(this)) return ScreenM.getInstance().screens.indexOf(this);
        return -1;
    }
    @Override
    public String toString() {
        return getNbtTag().toString();
    }

    public void rm() {
        switch (facing) {
            case X:
                for (int z= pos.getZ();z<width+ pos.getZ();z++)
                    for (int y= pos.getY();y<height+ pos.getY();y++)
                        world.setBlockState(new BlockPos(pos.getX(),y,z), Blocks.AIR.getDefaultState(), Constants.BlockFlags.SEND_TO_CLIENTS| Constants.BlockFlags.RERENDER_MAIN_THREAD);
                break;
            case Y:
                for (int x= pos.getX();x<width+ pos.getX();x++)
                    for (int z= pos.getZ();z<height+ pos.getZ();z++)
                        world.setBlockState(new BlockPos(x,pos.getY(),z), Blocks.AIR.getDefaultState(),Constants.BlockFlags.SEND_TO_CLIENTS| Constants.BlockFlags.RERENDER_MAIN_THREAD);
                break;
            case Z:
                for (int x= pos.getX();x<width+ pos.getX();x++)
                    for (int y= pos.getY();y<height+ pos.getY();y++)
                        world.setBlockState(new BlockPos(x,y, pos.getZ()), Blocks.AIR.getDefaultState(),Constants.BlockFlags.SEND_TO_CLIENTS| Constants.BlockFlags.RERENDER_MAIN_THREAD);
                break;
        }
    }
}
