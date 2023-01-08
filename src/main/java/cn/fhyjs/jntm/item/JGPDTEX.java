package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.item.Item;
import net.minecraftforge.common.property.Properties;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class JGPDTEX extends Item {
    public JGPDTEX() {
        super();
        this.setRegistryName("JGPDTEX");
        this.setUnlocalizedName(Jntm.MODID + "JGPDTEX");
        this.setMaxStackSize(64);
        this.setCreativeTab(jntm_Group);
    }
}
