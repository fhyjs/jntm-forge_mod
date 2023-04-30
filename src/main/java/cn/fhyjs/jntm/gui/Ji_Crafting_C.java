package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityJiCrafting;
import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Ji_Crafting_C extends Container {
    private ItemStackHandler filterListInv;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public EntityPlayer entityPlayer;
    public InventoryCraftResult craftResult = new InventoryCraftResult();
    private TileEntityJiCrafting tejc;
    private Slot sc;
    public Ji_Crafting_C(EntityPlayer entityPlayer,IInventory playerInventory, BlockPos blockPos, World world) {
        tejc = ((TileEntityJiCrafting)(Objects.requireNonNull(world.getTileEntity(blockPos))));
        this.entityPlayer=entityPlayer;
        sc = new SlotCrafting(entityPlayer,craftMatrix, craftResult,0, 124, 35);
        filterListInv = tejc.getFilterList();
        addCTSlots();
        this.addSlotToContainer(sc);
        addPlayerSlots(playerInventory);
        for (int i=0;i<9;i++)
            craftMatrix.setInventorySlotContents(i,filterListInv.getStackInSlot(i));
    }
    @Override
    protected void slotChangedCraftingGrid(World world, EntityPlayer entityPlayer, InventoryCrafting inventoryCrafting, InventoryCraftResult inventoryCraftResult)
    {
        if (!world.isRemote)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)entityPlayer;
            ItemStack itemstack = ItemStack.EMPTY;
            IRecipe irecipe = CraftingManager.findMatchingRecipe(inventoryCrafting, world);

            if (irecipe != null && (irecipe.isDynamic() || !world.getGameRules().getBoolean("doLimitedCrafting") || entityplayermp.getRecipeBook().isUnlocked(irecipe)))
            {
                inventoryCraftResult.setRecipeUsed(irecipe);
                itemstack = irecipe.getCraftingResult(inventoryCrafting);
            }

            inventoryCraftResult.setInventorySlotContents(0, itemstack);
            for (int i=0;i<this.inventorySlots.size();i++){
                if (this.inventorySlots.get(i)==sc){
                    entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, i, itemstack));
                    return;
                }
            }
            entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
        }
    }
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        this.slotChangedCraftingGrid(entityPlayer.world, entityPlayer, this.craftMatrix, this.craftResult);
    }
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack stack = null;
        try {
             stack = super.slotClick(slotId, dragType, clickTypeIn, player);
        }catch (NullPointerException e){
            Jntm.logger.error(e);
        }
        // 禁阻一切对当前手持物品的交互，防止刷物品 bug
        if (slotId == 27 + player.inventory.currentItem) {
            return player.inventory.getStackInSlot(slotId);
        }
        tejc.setFilterList(filterListInv);
        for (int i=0;i<9;i++)
            craftMatrix.setInventorySlotContents(i,filterListInv.getStackInSlot(i));
        return stack;
    }

    private void addCTSlots() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new CTItemHandler(filterListInv, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
    }

    private void addPlayerSlots(IInventory playerInventory) {
        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 36) {
                if (!this.mergeItemStack(itemstack1, 36, 45, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    private static class CTItemHandler extends SlotItemHandler {
        private CTItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }
        @Override
        public int getSlotStackLimit() {
            return 64;
        }
    }
}
