package cn.fhyjs.jntm.network;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.client.ClientProxy;
import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.entity.spallcardentity.CustomSCE;
import cn.fhyjs.jntm.enums.Actions;
import cn.fhyjs.jntm.item.SpellCardBase;
import cn.fhyjs.jntm.registry.RecipeRegistryHandler;
import cn.fhyjs.jntm.screen.ScreenM;
import cn.fhyjs.jntm.tickratechanger.TickrateContainer;
import cn.fhyjs.jntm.utility.MediaPlayer;
import com.github.tartaricacid.touhoulittlemaid.client.event.BakeModelEvent;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.google.common.collect.Lists;
import javazoom.jl.player.advanced.AdvancedPlayer;
import net.katsstuff.teamnightclipse.danmakucore.entity.living.TouhouCharacter;
import net.katsstuff.teamnightclipse.danmakucore.entity.spellcard.Spellcard;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            if (event.getGui() instanceof GuiMainMenu) {
                for (Object value : MediaPlayer.tasks.values()) {
                    if (value instanceof AdvancedPlayer) {
                        ((AdvancedPlayer) value).stop();
                    }
                }
                poses.clear();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event){
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)&&Keyboard.isKeyDown(Keyboard.KEY_Q)){
            if (Loader.isModLoaded("touhou_little_maid"))
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(I18n.format("debug.tip.f3_z")));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)&&Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            if (Loader.isModLoaded("touhou_little_maid")){
                CustomResourcesLoader.reloadResources();
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§e"+I18n.format("debug.prefix")+"§fTLM"+I18n.format("debug.reload_resourcepacks.message")));
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)&&Keyboard.isKeyDown(Keyboard.KEY_Q)){
            if (Loader.isModLoaded("geckolib3"))
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(I18n.format("debug.tip.f3_m")));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)&&Keyboard.isKeyDown(Keyboard.KEY_M)) {
            if (Loader.isModLoaded("geckolib3")){
                GeckoLibCache.getInstance().onResourceManagerReload(Minecraft.getMinecraft().getResourceManager());
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§e"+I18n.format("debug.prefix")+"§fGeckoLib3"+I18n.format("debug.reload_resourcepacks.message")));
            }
        }
    }
    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        // 在这里执行你的自定义操作
        if (event.getGui() instanceof GuiChat) {
            ITextComponent itextcomponent = event.getGui().mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

            if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null)
            {
                this.handleComponentHover(event.getGui(),itextcomponent, event.getMouseX(), event.getMouseY());
            }
        }
    }
    protected void handleComponentHover(GuiScreen gui,ITextComponent component, int x, int y){
        if (component != null && component.getStyle().getHoverEvent() != null)
        {
            HoverEvent hoverevent = component.getStyle().getHoverEvent();

            if (hoverevent.getAction() == Actions.SHOW_IMAGE)
            {
                ClientProxy.drawHoveringImage();
            }

            GlStateManager.disableLighting();
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
    public static List<BlockPos> poses = new ArrayList<>();
    @SubscribeEvent
    public void onConfigGuiClosed(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Jntm.MODID)) {
            System.out.println(event.getConfigID());
            // 例如重新加载配置或执行其他相关的处理
            // ...
        }
    }
     static Map<World,Boolean> worldLoad = new HashMap<>();
    @SubscribeEvent
    public static void onWorldInit(WorldEvent.Load event){
        ScreenM.getInstance();
        worldLoad.put(event.getWorld(),false);
    }
    @SubscribeEvent
    public static void onWorldPotentialSpawns(WorldEvent.PotentialSpawns event){
        if (!worldLoad.get(event.getWorld())) {
            worldLoad.put(event.getWorld(),true);
            ScreenM.getInstance().init();
        }
    }
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
            if (toPlace==null) continue;
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
