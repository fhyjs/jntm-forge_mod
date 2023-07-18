package cn.fhyjs.jntm.item.LandminePlugins;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.item.Item;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class WatcherUpgrade extends Item implements LandminePB {
    public WatcherUpgrade() {
        super();
        this.setRegistryName("landmine_watcher_upgrade");
        this.setUnlocalizedName(Jntm.MODID+".landmine_watcher_upgrade");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
}
