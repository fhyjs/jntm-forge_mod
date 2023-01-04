package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.client.ClientProxy;
import cn.fhyjs.jntm.entity.kundan_st;
import cn.fhyjs.jntm.gui.Jntm_help;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.io.IOException;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class Jntm_help_item extends ItemEgg {
    public Jntm_help_item(){
        this.setRegistryName("jntm_help");
        this.setUnlocalizedName(Jntm.MODID + "help");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);
        // 播放声音
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEventRegistryHandler.ji, SoundCategory.NEUTRAL, 1F, 1);
        // world.isRemote 用于判断是服务端还是客户端，这里我们要做的逻辑显然应该只在服务端执行
        if (!world.isRemote) {
            //打开gui
            player.openGui(Jntm.instance, JntmGuiHandler.GUIs.HELP.getId(), world, (int) player.posX, (int) player.posY, (int) player.posZ);

        }
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
