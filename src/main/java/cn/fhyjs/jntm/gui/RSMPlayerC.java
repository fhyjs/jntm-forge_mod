package cn.fhyjs.jntm.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RSMPlayerC extends Container {
    public EntityPlayer player;
    private ItemStack album;

    public RSMPlayerC(EntityPlayer player, World world, BlockPos bp){
        super();
        this.player = player;
        //JOptionPane.showMessageDialog(null,"4");
    }
    public void save(){

    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        //JOptionPane.showMessageDialog(null,"5");
    }
}
