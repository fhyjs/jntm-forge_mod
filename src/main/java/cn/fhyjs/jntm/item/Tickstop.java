package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.tickratechanger.api.TickrateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.dedicated.ServerHangWatchdog;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.lang.reflect.InvocationTargetException;
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
        if (FMLCommonHandler.instance().getSide()!= Side.CLIENT) {
            try {
                for (Thread thread : ((Thread[]) ObfuscationReflectionHelper.findMethod(Thread.class, "getThreads", Thread[].class).invoke(null))) {
                    Object t = ObfuscationReflectionHelper.getPrivateValue(Thread.class,thread,"target");
                    if (t instanceof ServerHangWatchdog){
                        ((ServerHangWatchdog) t).maxTickTime=999999999;
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        if (!world.isRemote){
            if (TickrateAPI.getServerTickrate()<20)
                TickrateAPI.changeServerTickrate(40,true);
            else if (TickrateAPI.getServerTickrate()==20)
                TickrateAPI.changeServerTickrate(1,true);
            else if (TickrateAPI.getServerTickrate()>20)
                TickrateAPI.changeServerTickrate(20,true);
            if (FMLCommonHandler.instance().getSide()!= Side.CLIENT) {
                for (EntityPlayerMP playe1r : FMLServerHandler.instance().getServer().getPlayerList().getPlayers()) {
                    playe1r.sendMessage(new TextComponentString("current server tick is" + TickrateAPI.getServerTickrate()));
                }
            }else {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("current server tick is " + TickrateAPI.getServerTickrate()));
            }
        }
        // 互动成功，返回EnumActionResult.SUCCESS，item 是互动结束以后的 item
        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }
}
