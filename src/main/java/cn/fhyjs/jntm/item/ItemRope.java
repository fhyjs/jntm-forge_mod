package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.EntityRope;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
        if (!(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("data")&&stack.getTagCompound().hasKey("entity"))){
            tooltip.add(I18n.format("tooltip.rope.empty"));
        }else {
            try {
                Entity entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(stack.getTagCompound().getString("entity"))).newInstance(player);
                entity.readFromNBT(stack.getTagCompound().getCompoundTag("data"));
                tooltip.add(String.format("<%s>(+NBT)", entity.getDisplayName().setStyle(new Style().setColor(TextFormatting.YELLOW)).getFormattedText()));
            }catch (NullPointerException e){
                stack.setTagCompound(null);
            }
        }
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
            if (item.getTagCompound() != null) {
                rope.readEntityFromNBT(item.getTagCompound());
            }
            world.spawnEntity(rope);
        }
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        // 因为这是个可以无限丢的雪球，所以这里数量没有减去 1。减去 1 的话丢出去就会少一个。
        player.setHeldItem(hand,new ItemStack(item.getItem(),item.getCount()-1));
        // 自然地，减去 2 的话丢出去就会少两个。
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
