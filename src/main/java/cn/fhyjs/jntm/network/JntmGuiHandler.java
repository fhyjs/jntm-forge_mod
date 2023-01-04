package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.gui.Jntm_help;
import cn.fhyjs.jntm.gui.Jntm_help_container;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import javax.swing.*;

public class JntmGuiHandler implements IGuiHandler {
    /**
     * @param ID     MAIN_GUI 的 ID
     * @param player    玩家
     * @param world     世界
     * @param x  x
     * @param y y
     * @param z  z
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if (ID == GUIs.HELP.getId()) {
            return new Jntm_help_container(player, world);
        }
        return null;
    }

    /**
     * @param ID     MAIN_GUI 的 ID
     * @param player    玩家
     * @param world     世界
     * @param x  x
     * @param y y
     * @param z  z
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if (ID == GUIs.HELP.getId()) {

            return new Jntm_help(player, world);
        }
        return null;
    }

    public enum GUIs {
        //无GUI
        NONE(-1),
        //鸡你太美帮助
        HELP(10);

        private int id;
        GUIs(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }
    }
}
