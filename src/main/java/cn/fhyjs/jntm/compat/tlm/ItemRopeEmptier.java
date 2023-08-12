package cn.fhyjs.jntm.compat.tlm;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.item.Item;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class ItemRopeEmptier extends Item {
    public ItemRopeEmptier(){
        super();
        this.setRegistryName("ropeemptier");
        this.setUnlocalizedName(Jntm.MODID + ".tml.ropeemptier");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
}
