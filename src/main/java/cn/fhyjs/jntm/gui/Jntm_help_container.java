package cn.fhyjs.jntm.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.patchouli.common.item.PatchouliItems;

import javax.swing.*;

public class Jntm_help_container extends Container {
    public static EntityPlayer player;
    private ItemStack album;
    public static boolean open=true;

    public Jntm_help_container(EntityPlayer player, World world){
        super();
        Jntm_help_container.open=true;
        this.player = player;
        //JOptionPane.showMessageDialog(null,"4");
    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return open;
    }
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        //JOptionPane.showMessageDialog(null,"5");
    }
}
