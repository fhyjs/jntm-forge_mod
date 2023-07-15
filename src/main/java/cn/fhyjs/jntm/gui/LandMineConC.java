package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityJiCrafting;
import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import cn.fhyjs.jntm.utility.Ji_Crafting_Recipe;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class LandMineConC extends Container {
    private final World world;
    public EntityPlayer entityPlayer;
    public Slot LandmineShot;
    public InternalStores stores = new InternalStores("default");
    public final BlockPos pos;
    public LandMineConC(EntityPlayer entityPlayer, IInventory playerInventory, BlockPos blockPos, World world) {
        super();
        this.pos = blockPos;
        this.world = world;
        this.entityPlayer=entityPlayer;
        addPlayerSlots(playerInventory);
        this.addSlotToContainer(LandmineShot=new Slot(stores,0,103,111){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem()==ItemRegistryHandler.ItemLandmine;
            }
        });
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        stores.setActivityTab("default");
        if (!world.isRemote) {
            world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY()+1, pos.getZ(), stores.getItemStack(0)));
        }
        super.onContainerClosed(playerIn);
    }
    private void addPlayerSlots(IInventory playerInventory) {
        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 36 + i1 * 18, 137 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(playerInventory, l, 36 + l * 18, 196));
        }
    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
