package cn.fhyjs.jntm.screen;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.WorldSavedDataCallableSave;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScreenM extends WorldSavedData {
    public List<Screen> screens = new ArrayList<>();
    private static ScreenM Instance;
    public ScreenM(String name) {
        super(name);
    }
    @NotNull
    public static ScreenM getInstance(){
        if (Instance == null) {
            Instance = (ScreenM) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().loadData(ScreenM.class, "jntm_screens");
            if (Instance == null) {
                Instance = new ScreenM("jntm_screens");
                FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().setData("jntm_screens", Instance);
            }
        }
        return Instance;
    }
    public Screen Build(BlockPos bp, int w, int h, EnumFacing.Axis facing, World world){
        return new Screen(bp,w,h, facing,world);
    }
    public Screen Build(NBTTagCompound nbtTagCompound){
        return new Screen(nbtTagCompound);
    }
    public void addScreen(Screen screen){
        screens.add(screen);
        screen.init();
        markDirty();
    }
    public void init(){
        for (Screen screen : screens) {
            screen.init();
        }
    }
    public Screen screenAt(BlockPos blockPos){
        for (Screen screen : screens) {
            switch (screen.facing) {
                case X:
                    for (int z= screen.pos.getZ();z<screen.width+ screen.pos.getZ();z++)
                        for (int y= screen.pos.getY();y<screen.height+ screen.pos.getY();y++)
                            if(new BlockPos(screen.pos.getX(),y,z).equals(blockPos)){
                                return screen;
                            }
                    break;
                case Y:
                    for (int x= screen.pos.getX();x<screen.width+ screen.pos.getX();x++)
                        for (int z= screen.pos.getZ();z<screen.height+ screen.pos.getZ();z++)
                            if(new BlockPos(x,screen.pos.getY(),z).equals(blockPos)) {
                                return screen;
                            }
                    break;
                case Z:
                    for (int x= screen.pos.getX();x<screen.width+ screen.pos.getX();x++)
                        for (int y= screen.pos.getY();y<screen.height+ screen.pos.getY();y++)
                            if(new BlockPos(x,y, screen.pos.getZ()).equals(blockPos)) {
                                return screen;
                            }
                    break;
            }
        }
        return null;
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        for (String s : nbt.getKeySet()) {
            screens.add(new Screen(nbt.getCompoundTag(s)));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        for (Screen screen : screens) {
            compound.setTag(String.valueOf(screen.getId()),screen.getNbtTag());
        }
        return compound;
    }

    public void rmScreen(Screen screen) {
        screens.remove(screen);
        screen.rm();
        markDirty();
    }
}
