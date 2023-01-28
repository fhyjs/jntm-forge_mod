package cn.fhyjs.jntm.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Jvav_C extends Container {
    public static EntityPlayer player;
    private ItemStack album;
    public static boolean open=true;

    public Jvav_C(EntityPlayer player, World world){
        super();
        Jvav_C.open=true;
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
