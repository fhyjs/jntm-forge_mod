package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.JGPDanmaku;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class JGPDTEX extends Item {
    public JGPDTEX() {
        super();
        this.setRegistryName("JGPDTEX");
        this.setUnlocalizedName(Jntm.MODID + "JGPDTEX");
        this.setMaxStackSize(64);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(I18n.format("tooltip.jgpdtex.n1"));
        super.addInformation(stack, player, tooltip, advanced);
    }
}
