package cn.fhyjs.jntm.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Jvav_C extends Container {
    public EntityPlayer player;
    private ItemStack album;
    public boolean open=true;

    public Jvav_C(EntityPlayer player, World world){
        super();
        this.player = player;
        //JOptionPane.showMessageDialog(null,"4");
    }

    public Jvav_C() {

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
