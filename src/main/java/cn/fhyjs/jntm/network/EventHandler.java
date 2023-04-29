package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.client.ClientProxy;
import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.entity.spallcardentity.CustomSCE;
import cn.fhyjs.jntm.item.SpellCardBase;
import net.katsstuff.teamnightclipse.danmakucore.entity.living.TouhouCharacter;
import net.katsstuff.teamnightclipse.danmakucore.entity.spellcard.Spellcard;
import net.katsstuff.teamnightclipse.danmakucore.item.ItemSpellcard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

@Mod.EventBusSubscriber
public class EventHandler {
    public static Boolean postInit;
    public static boolean played = false;
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Jntm.MODID))
            ConfigCore.syncConfig();
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public void onGuiOpen(GuiOpenEvent event)
    {
        if(postInit && event.getGui() instanceof GuiMainMenu && !played)
        {
            played = true;
            ClientProxy.TIl.displayMessage(I18n.format("mod.jntm.name"), I18n.format("jntm.tips.mcsf"), TrayIcon.MessageType.INFO);//弹出一个info级别消息框

        }
    }
    @SubscribeEvent
    public static void onSpellCardRegister(RegistryEvent.Register<Spellcard> event){
        event.getRegistry().register(new SpellCardBase<>("t_card", CustomSCE.class, TouhouCharacter.REIMU_HAKUREI));
    }
}
