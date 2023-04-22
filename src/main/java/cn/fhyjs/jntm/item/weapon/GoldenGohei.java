package cn.fhyjs.jntm.item.weapon;

import cn.fhyjs.jntm.item.WeaponBase;
import cn.fhyjs.jntm.utility.QuoteLib;
import net.minecraft.creativetab.CreativeTabs;


public class GoldenGohei extends WeaponBase {
    public GoldenGohei(String name, CreativeTabs tab) {
        super(name, tab, QuoteLib.UseLessMaterial);
        setMaxDamage(1);
        setMaxStackSize(1);
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }
}
