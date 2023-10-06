package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.client.ChatImage;
import cn.fhyjs.jntm.enums.Actions;
import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.danmakucore.DanmakuCore;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.DanmakuTemplate;
import net.katsstuff.teamnightclipse.danmakucore.lib.LibColor;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibForms;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibShotData;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.server.FMLServerHandler;
import scala.collection.JavaConversions;

import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

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
        if (world.isRemote) {
            ChatImage.ChatImageData cid = new ChatImage.ChatImageData();
            cid.h=150;
            cid.w=100;
            cid.url="https://i0.hdslb.com/bfs/archive/2baa938d85ba419e0c6a6da9e14cd4cd27fb977e.jpg@92w_92h_1c_100q.jpg";
            cid.information="这是ChatImage实例";
            ((EntityPlayerSP) player).sendChatMessage(cid.toString());
        }
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
