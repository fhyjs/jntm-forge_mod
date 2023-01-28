package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.gui.*;
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
     * @param ID        GUI 的 ID
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
        if (ID==GUIs.Jvav.getId()||ID==GUIs.Insting.getId()||ID==GUIs.JFinish.getId()){
            return new Jvav_C(player, world);
        }
        return null;
    }

    /**
     * @param ID        GUI 的 ID
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
        if (ID == GUIs.JI_games.getId()) {
            return new Ji_games_GUI(player,world);
        }
        if (ID == GUIs.Jvav.getId()) {
            return new Jvav(player,world);
        }
        if (ID==GUIs.Insting.getId()){
            return new Jvav_insting(player,world);
        }
        if(ID==GUIs.JFinish.getId()){
            return new Jvav_Finish(player,world);
        }
        return null;
    }

    public enum GUIs {
        //无GUI
        NONE(-1),
        //鸡你太美帮助
        HELP(10),
        //鸡游戏
        JI_games(11),
        //Jvav
        Jvav(12),
        //Jvav2
        Insting(13),
        //jvav3
        JFinish(14);

        private int id;
        GUIs(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }
    }
}
