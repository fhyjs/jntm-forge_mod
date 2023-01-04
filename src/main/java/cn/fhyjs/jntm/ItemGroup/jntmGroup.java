package cn.fhyjs.jntm.ItemGroup;

import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class jntmGroup extends CreativeTabs {

    public static final jntmGroup jntm_Group=new jntmGroup();
    //添加创造物品栏示例

    public jntmGroup() {
        super("jntm");
        //设置名字
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemRegistryHandler.ggxdd);
        //设置创造物品栏的封面
    }
    @Override
    public boolean hasSearchBar(){
        return false;
        //添加搜索框
    }



}
