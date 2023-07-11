package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class InsJvav extends ItemEgg {
    public InsJvav(){
        this.setRegistryName("insjvav");
        this.setUnlocalizedName(Jntm.MODID + "insjvav");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);
        // 播放声音
        //world.playSound(null, player.posX, player.posY, player.posZ, SoundEventRegistryHandler.ji, SoundCategory.NEUTRAL, 1F, 1);
        // world.isRemote 用于判断是服务端还是客户端，这里我们要做的逻辑显然应该只在服务端执行
        if (!world.isRemote) {
            //打开gui
            player.openGui(Jntm.instance, JntmGuiHandler.GUIs.Jvav.getId(), world, (int) player.posX, (int) player.posY, (int) player.posZ);

        }
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
