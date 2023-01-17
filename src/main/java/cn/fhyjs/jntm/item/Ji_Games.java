package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.kundan_st;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import java.io.IOException;
import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;
import static cn.fhyjs.jntm.Jntm.proxy;

public class Ji_Games extends Item {
    public Ji_Games() {
        super();
        this.setRegistryName("jigames");
        this.setUnlocalizedName(Jntm.MODID + "jigames");
        this.setMaxStackSize(16);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(I18n.format("tooltip.jigames.n1"));
        super.addInformation(stack, player, tooltip, advanced);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEventRegistryHandler.ji, SoundCategory.NEUTRAL, 1F, 1);
        // world.isRemote 用于判断是服务端还是客户端，这里我们要做的逻辑显然应该只在服务端执行
        // 这个字段的细节在第七章有详细阐述。
        //if (!world.isRemote) {
            //打开gui
            player.openGui(Jntm.instance, JntmGuiHandler.GUIs.JI_games.getId(), world, (int) player.posX, (int) player.posY, (int) player.posZ);

        //}
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
