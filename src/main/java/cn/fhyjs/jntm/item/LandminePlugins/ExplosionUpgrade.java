package cn.fhyjs.jntm.item.LandminePlugins;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.item.Item;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class ExplosionUpgrade extends Item implements LandminePB {
    public ExplosionUpgrade() {
        super();
        this.setRegistryName("landmine_explosion_upgrade");
        this.setUnlocalizedName(Jntm.MODID+".landmine_explosion_upgrade");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
}
