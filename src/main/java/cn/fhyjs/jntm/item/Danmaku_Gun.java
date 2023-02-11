package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class Danmaku_Gun extends Item {
    public  Danmaku_Gun(){
        this.setRegistryName("danmakugan");
        this.setUnlocalizedName(Jntm.MODID + "danmakugan");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(I18n.format("tooltip.danmaku_gun.n1"));
        super.addInformation(stack, player, tooltip, advanced);
    }
}
