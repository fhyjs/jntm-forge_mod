package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.pstest;
import cn.fhyjs.jntm.entity.kundan_st;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import cn.fhyjs.jntm.tickratechanger.TickrateChanger;
import cn.fhyjs.jntm.tickratechanger.TickrateCommand;
import cn.fhyjs.jntm.tickratechanger.api.TickrateAPI;
import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.danmakucore.DanmakuCore;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.DanmakuTemplate;
import net.katsstuff.teamnightclipse.danmakucore.lib.LibColor;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibForms;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibShotData;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import scala.collection.JavaConversions;

import javax.swing.*;
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
        DanmakuTemplate.Builder temp = DanmakuTemplate.builder()
                .setSource(player)
                .setUser(player)
                .setWorld(world)
                .setMovementData(1D)
                .setShot(LibShotData.SHOT_LASER
                        .setCoreColor(LibColor.COLOR_VANILLA_WHITE)
                        .setEdgeColor(LibColor.COLOR_SATURATED_YELLOW)
                        .setDamage(2f)
                        .scaleSize(10f)
                        .setForm(LibForms.LASER)
                );
        Vector3 start_vec = new Vector3(player);
        temp.setPos(start_vec);
        DanmakuCore.proxy().spawnDanmaku(JavaConversions.asScalaBuffer(Lists.newArrayList(temp.build().asEntity())));
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
