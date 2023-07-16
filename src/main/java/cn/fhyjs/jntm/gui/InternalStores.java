package cn.fhyjs.jntm.gui;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;

import java.util.*;

public class InternalStores implements IInventory {
    Map<String, List<ItemStack>> listMap = new HashMap<>();
    Map<String, String> currentTab = new HashMap<>();
    Map<ItemStack, List<Item>> canPuts = new HashMap<>();
    private int timesChanged;
    public void addCanPuts(int id,Item... cap){
        if (!canPuts.containsKey(getItemStack(id))) canPuts.put(getItemStack(id),new ArrayList<>());
        canPuts.get(getItemStack(id)).addAll(Arrays.asList(cap));
    }
    public InternalStores(String defaultName){
        if(!this.addnewtab(defaultName,false))
            throw new ReportedException(CrashReport.makeCrashReport(new IllegalStateException("There already was a list with the same name!"),"There already was a list with the same name!But why?!"));
        setActivityTab(defaultName);
    }
    public boolean addnewtab(String name,boolean override){
        if (listMap.containsKey(name)){
            if (override)
                listMap.remove(name);
            else
                return false;
        }
        listMap.put(name,new ArrayList<>());
        return true;
    }
    public boolean rmtab(String name){
        if (!listMap.containsKey(name))
            return false;
        listMap.remove(name);
        return true;
    }
    public void setActivityTab(String name){
        if (!listMap.containsKey(name)) throw new ReportedException(CrashReport.makeCrashReport(new IllegalStateException("There is no label with a name like this"),"There is no label with a name like this!"));
        currentTab.put(Thread.currentThread().getName(), name);
    }
    public List<ItemStack> getItemStacks(){
        return listMap.get(currentTab.get(Thread.currentThread().getName()));
    }
    public ItemStack getItemStack(int pos){
        try {
            if (getItemStacks().get(pos)==null) getItemStacks().add(pos,ItemStack.EMPTY);
        }catch (Throwable e){
            getItemStacks().add(pos,ItemStack.EMPTY);
        }
        finally {
            return getItemStacks().get(pos);

        }
    }
    public ItemStack getItemStack(int pos,String tab){
        if (listMap.get(tab) == null) listMap.get(tab).add(pos,ItemStack.EMPTY);
        return listMap.get(tab).get(pos);
    }
    public void addItemStack(ItemStack is){
        listMap.get(currentTab.get(Thread.currentThread().getName())).add(is);
    }
    @Override
    public int getSizeInventory() {
        return getItemStacks().size();
    }

    @Override
    public boolean isEmpty() {
        return getItemStacks().isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return getItemStack(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        List<ItemStack> list = getItemStacks();
        return list != null && !list.get(index).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return getItemStacks().set(index,ItemStack.EMPTY);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        getItemStack(index);
        getItemStacks().set(index,stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    public int getTimesChanged()
    {
        return this.timesChanged;
    }
    @Override
    public void markDirty() {
        ++this.timesChanged;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return !player.isDead;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (canPuts.containsKey(getItemStack(index))) {
            if (canPuts.get(getItemStack(index)).contains(stack.getItem()))
                return false;
        }
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        getItemStacks().clear();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
