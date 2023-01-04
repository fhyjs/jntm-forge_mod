package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.kundan_st;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;
import static sun.audio.AudioPlayer.player;

public class ggxdd extends ItemEgg {
    public  ggxdd(){
        this.setRegistryName("cxksegg");
        this.setUnlocalizedName(Jntm.MODID + "cxksegg");
        this.setMaxStackSize(16);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);
        // 播放雪球被抛出去的声音——这个会在后面的章节中详细解释
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEventRegistryHandler.ji, SoundCategory.NEUTRAL, 1F, 1);
        // world.isRemote 用于判断是服务端还是客户端，这里我们要做的逻辑显然应该只在服务端执行
        // 这个字段的细节在第七章有详细阐述。
        if (!world.isRemote) {
            //生成雪球实体——关于实体的内容在第八章会详细解释，我们现在丢雪球就好了
            EntityEgg kundan = new kundan_st(world,player);
            kundan.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity(kundan);
        }
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        // 因为这是个可以无限丢的雪球，所以这里数量没有减去 1。减去 1 的话丢出去就会少一个。
        item.shrink(1); // 数量 - 1
        // 自然地，减去 2 的话丢出去就会少两个。
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
