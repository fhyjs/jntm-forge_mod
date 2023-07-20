package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityJiCrafting;
import cn.fhyjs.jntm.item.LandminePlugins.LandminePB;
import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import cn.fhyjs.jntm.utility.Ji_Crafting_Recipe;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LandMineConC extends Container {
    private final World world;
    public final EntityPlayer entityPlayer;
    public Slot LandmineShot;
    public final InternalStores stores = new InternalStores("default");
    public final List<ItemStack> plugins = new ArrayList<>();
    public final BlockPos pos;
    public LandMineConC(EntityPlayer entityPlayer, IInventory playerInventory, BlockPos blockPos, World world) {
        super();
        this.pos = blockPos;
        this.world = world;
        this.entityPlayer=entityPlayer;
        addPlayerSlots(playerInventory);
        this.addSlotToContainer(LandmineShot=new Slot(stores,0,103,111){
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                if (stores.getStackInSlot(0).getItem() != ItemRegistryHandler.ItemLandmine) {
                    return;
                }
                clearplugin();
                onPut();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem()==ItemRegistryHandler.ItemLandmine;
            }
        });
        for (int i=0;i<2;i++)
            for (int j = 0; j < 4; j++) {
                Slot shot = new Slot(stores,this.inventorySlots.size()-36, 97+j*18, 15+i*18) {
                    @SideOnly(Side.CLIENT)
                    @Override
                    public boolean isEnabled(){
                        return Minecraft.getMinecraft().currentScreen instanceof LandMineConG && ((LandMineConG) Minecraft.getMinecraft().currentScreen).isPut;
                    }
                    @Override
                    public boolean isItemValid(ItemStack stack) {
                        if (stack.getItem()==ItemRegistryHandler.camouflageUpgrade)
                            return stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("pos");
                        return stack.getItem() instanceof LandminePB;
                    }
                };
                this.addSlotToContainer(shot);
            }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (clickTypeIn.equals(ClickType.QUICK_MOVE)) return ItemStack.EMPTY;
        ItemStack itemStack = super.slotClick(slotId, dragType, clickTypeIn, player);

        checkPlugins();
        for (int i = 0; i < plugins.size(); i++) {
            ItemStack stack = plugins.get(i);
            if (stack.getItem()==itemStack.getItem()){
                itemStack = this.slotClick(slotId, dragType, clickTypeIn, player);
            }
        }
        CheckLM();

        return itemStack;
    }
    public NBTTagCompound LmNbt;
    public void onPut(){
        //Jntm.unsafe.loadFence();
        stores.setActivityTab("default");
        if (LandmineShot.getStack().getItem() == ItemRegistryHandler.ItemLandmine){
            if (LmNbt==null){
                LmNbt=LandmineShot.getStack().getTagCompound();
                if (LmNbt==null){
                    LmNbt=new NBTTagCompound();
                    LmNbt.setTag("BlockEntityTag",new NBTTagCompound());
                }
            }
            NBTTagCompound coreNbt = LmNbt.getCompoundTag("BlockEntityTag");
            if (coreNbt.hasKey("Broadcast")&&coreNbt.getBoolean("Broadcast"))
                addplugin(new ItemStack(ItemRegistryHandler.watcherUpgrade));
            if (coreNbt.hasKey("Explosion")&&coreNbt.getBoolean("Explosion"))
                addplugin(new ItemStack(ItemRegistryHandler.explosionUpgrade));
            if (coreNbt.hasKey("HasCP")&&coreNbt.getBoolean("HasCP")) {
                ItemStack is = new ItemStack(ItemRegistryHandler.camouflageUpgrade);
                is.setTagCompound(new NBTTagCompound());
                if (is.getTagCompound() != null) {
                    is.getTagCompound().setIntArray("pos",coreNbt.getIntArray("CamouflagePos"));
                }
                addplugin(is);
            }
        }
        checkPlugins();
    }
    public void checkPlugins(){
        plugins.clear();
        for (int i = 37; i < 45; i++) {
            ItemStack is = this.inventorySlots.get(i).getStack();
            if (is.getItem() instanceof ItemAir) continue;
            plugins.add(is);
        }
    }
    public void addplugin(ItemStack is){
        for (int i = 37; i < 45; i++) {
            ItemStack iss = this.inventorySlots.get(i).getStack();
            if (iss.getItem() instanceof ItemAir){
                stores.getItemStacks().set(i-36,is);
                return;
            }
        }
    }
    private void CheckLM(){
        stores.setActivityTab("default");
        ItemStack landmine;
        if ((landmine=stores.getItemStack(0)).getItem()==ItemRegistryHandler.ItemLandmine){
            LmNbt = landmine.getTagCompound();
        }else {
            LmNbt = null;
        }
    }
    public boolean hasPlugin(Item item){
        for (int i = 0; i < plugins.size(); i++) {
            if (plugins.get(i).getItem()==item) return true;
        }
        return false;
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
            this.addSlotToContainer(new Slot(playerInventory, l, 36 + l * 18, 195));
        }
    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public void clearplugin() {
        for (int i = 37; i < 45; i++) {
            this.stores.getItemStacks().add(i-36,ItemStack.EMPTY);
        }
    }
}
