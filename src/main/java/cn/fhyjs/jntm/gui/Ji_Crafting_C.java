package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityJiCrafting;
import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import cn.fhyjs.jntm.utility.Ji_Crafting_Recipe;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ji_Crafting_C extends Container {
    public EntityPlayer entityPlayer;
    private final TileEntityJiCrafting tejc ;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();
    private Slot sc;
    public Ji_Crafting_C(EntityPlayer entityPlayer,IInventory playerInventory, BlockPos blockPos, World world) {
        this.entityPlayer=entityPlayer;
        tejc=(TileEntityJiCrafting) world.getTileEntity(blockPos);
        setCraftMatrix();
        sc=new SC(entityPlayer, this.craftMatrix, this.craftResult, 0, 124, 35);
        this.addSlotToContainer(sc);
        addPlayerSlots(playerInventory);
        addCTSlots();
    }
    private void addCTSlots() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
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
        setFilterList();
        return stack;
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
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
    public void setCraftMatrix(){
        int i=0;
        for (ItemStack itemStack : tejc.getFilterList()){
            craftMatrix.setInventorySlotContents(i,tejc.getFilterList().get(i));
            i++;
        }
    }
    public void setFilterList(){
        NonNullList<ItemStack> a = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0;i<craftMatrix.getSizeInventory();i++){
            a.set(i,craftMatrix.getStackInSlot(i));
        }
        tejc.setFilterList(a);
    }
    @Override
    protected void slotChangedCraftingGrid(World world, EntityPlayer entityPlayer, InventoryCrafting inventoryCrafting, InventoryCraftResult inventoryCraftResult)
    {
        if (!world.isRemote)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)entityPlayer;
            ItemStack itemstack=getJiCraftingL(inventoryCrafting,inventoryCraftResult,world,entityplayermp);
            inventoryCraftResult.setInventorySlotContents(0, itemstack);
            for (int i=0;i<this.inventorySlots.size();i++){
                if (this.inventorySlots.get(i)==sc){
                    entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, i, itemstack));
                    return;
                }
            }
        }
    }
    public ItemStack getJiCraftingL(InventoryCrafting ic,InventoryCraftResult icr,World world,EntityPlayerMP entityPlayerMP){
        ItemStack stack = ItemStack.EMPTY;
        IRecipe irecipe = CraftingManager.findMatchingRecipe(ic, world);

        if (irecipe != null && (irecipe.isDynamic() || !world.getGameRules().getBoolean("doLimitedCrafting") || entityPlayerMP.getRecipeBook().isUnlocked(irecipe)))
        {
            icr.setRecipeUsed(irecipe);
            stack = irecipe.getCraftingResult(ic);
        }

        if (stack != ItemStack.EMPTY)
            return stack;
        stack=Ji_Crafting_Recipe.findMatchingResult(ic,world);
        return stack;
    }
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        this.slotChangedCraftingGrid(entityPlayer.world, entityPlayer, this.craftMatrix, this.craftResult);

    }
    private class SC extends SlotCrafting{

        public SC(EntityPlayer p_i45790_1_, InventoryCrafting p_i45790_2_, IInventory p_i45790_3_, int p_i45790_4_, int p_i45790_5_, int p_i45790_6_) {
            super(p_i45790_1_, p_i45790_2_, p_i45790_3_, p_i45790_4_, p_i45790_5_, p_i45790_6_);
        }
        @Override
        public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
            this.onCrafting(stack);
            net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);

            IRecipe irecipe = CraftingManager.findMatchingRecipe(craftMatrix, entityPlayer.world);
            NonNullList<ItemStack> nonnulllist = null;
            if (irecipe != null) {
                nonnulllist = CraftingManager.getRemainingItems(this.craftMatrix, thePlayer.world);
            } else {
                nonnulllist = Ji_Crafting_Recipe.getRemainingItems(this.craftMatrix, thePlayer.world);
            }
            net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

            for (int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
                ItemStack itemstack1 = nonnulllist.get(i);

                if (!itemstack.isEmpty()) {
                    this.craftMatrix.decrStackSize(i, 1);
                    itemstack = this.craftMatrix.getStackInSlot(i);
                }

                if (!itemstack1.isEmpty()) {
                    if (itemstack.isEmpty()) {
                        this.craftMatrix.setInventorySlotContents(i, itemstack1);
                    } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                        itemstack1.grow(itemstack.getCount());
                        this.craftMatrix.setInventorySlotContents(i, itemstack1);
                    } else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
                        this.player.dropItem(itemstack1, false);
                    }
                }
            }

            return stack;
        }
    }
}
