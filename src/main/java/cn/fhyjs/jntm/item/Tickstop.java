package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.tickratechanger.api.TickrateAPI;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.swing.*;
import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class Tickstop extends Item {
    public Tickstop() {
        super();
        this.setRegistryName("tickstop");
        this.setUnlocalizedName(Jntm.MODID + "tickstop");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(I18n.format("tooltip.tickstop.n1"));
        super.addInformation(stack, player, tooltip, advanced);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);
        if (!world.isRemote)
            TickrateAPI.changeServerTickrate(Float.parseFloat(JOptionPane.showInputDialog("tick")),true);
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
