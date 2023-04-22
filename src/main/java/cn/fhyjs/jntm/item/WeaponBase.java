package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

public class WeaponBase extends ItemSword {
    public WeaponBase(String name, CreativeTabs tab, ToolMaterial material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(tab);
        setMaxStackSize(1);
    }
}
