package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.client.ClientProxy;
import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.entity.spallcardentity.CustomSCE;
import cn.fhyjs.jntm.item.SpellCardBase;
import cn.fhyjs.jntm.registry.RecipeRegistryHandler;
import cn.fhyjs.jntm.tickratechanger.TickrateContainer;
import net.katsstuff.teamnightclipse.danmakucore.entity.living.TouhouCharacter;
import net.katsstuff.teamnightclipse.danmakucore.entity.spellcard.Spellcard;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        if(postInit && event.getGui() instanceof GuiMainMenu && !played&&ConfigCore.isenabledTrayIcon)
        {
            played = true;
            ClientProxy.TIl.displayMessage(I18n.format("mod.jntm.name"), I18n.format("jntm.tips.mcsf"), TrayIcon.MessageType.INFO);//弹出一个info级别消息框

        }
    }
    @SubscribeEvent
    public static void onSpellCardRegister(RegistryEvent.Register<Spellcard> event){
        event.getRegistry().register(new SpellCardBase<>("t_card", CustomSCE.class, TouhouCharacter.REIMU_HAKUREI));
    }
    @SubscribeEvent
    public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event){
        RecipeRegistryHandler.reg(event);
    }
    @Mod.EventHandler
    public void start(FMLServerStartingEvent event) {
        TickrateContainer.TC.start(event);
    }
    @Mod.EventHandler
    public void imc(FMLInterModComms.IMCEvent event) {
        TickrateContainer.TC.imc(event);
    }
    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) {
        TickrateContainer.TC.chat(event);
    }
    @SubscribeEvent
    public void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        TickrateContainer.TC.disconnect(event);
    }
    @SubscribeEvent
    public void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        TickrateContainer.TC.connect(event);
    }
    @SubscribeEvent
    public void connect(PlayerEvent.PlayerLoggedInEvent event) {
        TickrateContainer.TC.connect(event);
    }
    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent event) {
        TickrateContainer.TC.key(event);
    }
    public List<BlockPos> poses = new ArrayList<>();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void highlightGhostBlock(DrawBlockHighlightEvent event) {
        Entity entity = event.getPlayer();
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) event.getPartialTicks();
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.getPartialTicks();
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) event.getPartialTicks();

        IBlockState stateToRender = Block.getBlockFromName("minecraft:glass").getDefaultState();

        BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

        for (BlockPos toPlace : new ArrayList<>(poses)) { //positions is just a Collection filled with BlockPos
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
            GlStateManager.translate(-d0, -d1, -d2);
            GlStateManager.translate(toPlace.getX(), toPlace.getY(), toPlace.getZ());
            GlStateManager.rotate(-90, 0f, 1f, 0f);
            GL14.glBlendColor(1f, 1f, 1f, 0.6f);
            renderer.renderBlockBrightness(stateToRender, 1f);
            GL14.glBlendColor(1f, 1f, 1f, 1f);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
