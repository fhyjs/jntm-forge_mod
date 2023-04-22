package cn.fhyjs.jntm.item.weapon;

import cn.fhyjs.jntm.item.CardBase;
import cn.fhyjs.jntm.item.WeaponBase;
import cn.fhyjs.jntm.utility.DanmakuUtils;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibForms;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class Gohei extends WeaponBase {
    public Gohei(String name, CreativeTabs tab) {
        super(name, tab, Item.ToolMaterial.DIAMOND);
        setMaxStackSize(1);
        setMaxDamage(3299);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tooltip.gohei.n1"));
        super.addInformation(stack, player, tooltip, advanced);
    }
    public void onShot(World worldIn, EntityPlayer player) {
        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == this){
            if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof CardBase){
                CardBase cb = (CardBase) player.getHeldItem(EnumHand.OFF_HAND).getItem();
                cb.onShotUse(worldIn,player,EnumHand.OFF_HAND);
            }
            else {
                DanmakuUtils.shotDanmaku(player,worldIn,LibForms.TALISMAN);
                player.getCooldownTracker().setCooldown(this,5);
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        onShot(worldIn,player);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
    {
        onShot(worldIn,player);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
    }


}
