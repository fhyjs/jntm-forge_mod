package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.EntityRope;
import cn.fhyjs.jntm.entity.kundan_st;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;


public class ItemRope extends ItemEgg {
    public ItemRope(){
        this.setRegistryName("rope");
        this.setUnlocalizedName(Jntm.MODID + ".rope");
        this.setMaxStackSize(64);
        this.setCreativeTab(jntm_Group);
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(I18n.format("tooltip.rope.n1"));
        super.addInformation(stack, player, tooltip, advanced);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);
        // 播放雪球被抛出去的声音,这个会在后面的章节中详细解释
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.PLAYERS, 1F, 1);
        // world.isRemote 用于判断是服务端还是客户端，这里我们要做的逻辑显然应该只在服务端执行
        // 这个字段的细节在第七章有详细阐述。
        if (!world.isRemote) {
            //生成雪球实体——关于实体的内容在第八章会详细解释，我们现在丢雪球就好了
            EntityRope rope = new EntityRope(world,player);
            rope.setPosition(player.posX,player.posY+player.getEyeHeight(),player.posZ);
            rope.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 0.2F);
            world.spawnEntity(rope);
        }
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        // 因为这是个可以无限丢的雪球，所以这里数量没有减去 1。减去 1 的话丢出去就会少一个。
        item.shrink(1); // 数量 - 1
        // 自然地，减去 2 的话丢出去就会少两个。
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
