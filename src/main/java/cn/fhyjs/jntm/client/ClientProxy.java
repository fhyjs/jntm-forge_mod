package cn.fhyjs.jntm.client;

import cn.fhyjs.jntm.Event.RenderTooltipImageEvent;
import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.network.EventHandler;
import cn.fhyjs.jntm.registry.RenderRegistryHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;
import static net.minecraftforge.fml.client.config.GuiUtils.drawTexturedModalRect;


public class ClientProxy extends CommonProxy {
    @SideOnly(Side.CLIENT)
    @Override
    public void onModelRegistry(ModelRegistryEvent event){
        super.onModelRegistry(event);
    }
    public static List<ModelRegistryObj> modelsToReg = new ArrayList<ModelRegistryObj>();
    public static List<ModelBakeObj> modelsToBake = new ArrayList<ModelBakeObj>();
    @Override
    public String getCB() throws IOException, UnsupportedFlavorException {
        super.getCB();
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
            }
        }
        return ret;
    }
    public static void  drawHoveringImage(GuiScreen guiScreen, @NotNull ChatImage chatImage, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font)
    {
        if (chatImage!=null)
        {
            List<String> textLines = new ArrayList<>(chatImage.information);

            RenderTooltipImageEvent.Pre event = new RenderTooltipImageEvent.Pre(chatImage, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, font);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return;
            }
            mouseX = event.getX();
            mouseY = event.getY();
            screenWidth = event.getScreenWidth();
            screenHeight = event.getScreenHeight();
            maxTextWidth = event.getMaxWidth();
            font = event.getFontRenderer();

            GlStateManager.pushMatrix();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int tooltipTextWidth = 0;

            for (String textLine : textLines)
            {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth)
                {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth)
            {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2)
                    {
                        tooltipTextWidth = mouseX - 12 - 8;
                    }
                    else
                    {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth)
            {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap)
            {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<String>();
                for (int i = 0; i < textLines.size(); i++)
                {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0)
                    {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine)
                    {
                        int lineWidth = font.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth)
                        {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2)
                {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                }
                else
                {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12 - chatImage.height;
            int tooltipHeight = 8;

            if (textLines.size() > 1)
            {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2; // gap between title lines and next lines
                }
            }

            if (tooltipY < 4)
            {
                tooltipY = 4;
            }
            else if (tooltipY + tooltipHeight + 4 > screenHeight)
            {
                tooltipY = screenHeight - tooltipHeight - 4;
            }

            int imageX=tooltipX+1;
            int imageY=tooltipY+1;
            tooltipTextWidth+= chatImage.width+1;
            tooltipHeight+= chatImage.height+1;

            final int zLevel = 300;
            int backgroundColor = 0xF0100010;
            int borderColorStart = 0x505000FF;
            int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            RenderTooltipImageEvent.Color colorEvent = new RenderTooltipImageEvent.Color(chatImage, tooltipX, tooltipY, font, backgroundColor, borderColorStart, borderColorEnd);
            MinecraftForge.EVENT_BUS.post(colorEvent);
            backgroundColor = colorEvent.getBackground();
            borderColorStart = colorEvent.getBorderStart();
            borderColorEnd = colorEvent.getBorderEnd();
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);
            GlStateManager.popMatrix();

            MinecraftForge.EVENT_BUS.post(new RenderTooltipImageEvent.PostBackground(chatImage, tooltipX, tooltipY, font, tooltipTextWidth, tooltipHeight));
            int tooltipTop = tooltipY;

            // 获取渲染引擎
            Minecraft mc = Minecraft.getMinecraft();

            if (chatImage.status!= ChatImage.ImageStatus.OK&&chatImage.status!= ChatImage.ImageStatus.WAITING&&chatImage.status!= ChatImage.ImageStatus.ERROR){
                chatImage.getImage(chatImage.source);
            }
            if (chatImage.status == ChatImage.ImageStatus.OK) {

                ResourceLocation resourceLocation = chatImage.getImage();
                if (resourceLocation==null){
                    textLines.add("§4"+ net.minecraft.client.resources.I18n.format("image.fail"));
                }else {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1F, 1F, 1.0F); // 缩放因子，根据需求调整
                    GlStateManager.color(1, 1, 1, 1);
                    mc.getTextureManager().bindTexture(resourceLocation); // 绑定材质
                    // 绘制材质
                    Gui.drawModalRectWithCustomSizedTexture(imageX, imageY, 0, 0, chatImage.width, chatImage.height, chatImage.width, chatImage.height);
                    GlStateManager.popMatrix();
                }
            }
            if (chatImage.status == ChatImage.ImageStatus.WAITING){
                textLines.add("§4"+ net.minecraft.client.resources.I18n.format("image.waiting"));
            }
            if (chatImage.status == ChatImage.ImageStatus.ERROR){
                textLines.add("§4"+ net.minecraft.client.resources.I18n.format("image.fail"));
            }

            //显示文字
            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber)
            {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, (float)tooltipX, (float)tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount)
                {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }
            MinecraftForge.EVENT_BUS.post(new RenderTooltipImageEvent.PostText(chatImage, tooltipX, tooltipTop, font, tooltipTextWidth, tooltipHeight));

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    public Image[] TIs;
    public List<StateMapObj> statesToMap = new ArrayList<StateMapObj>();
    public proadd nt=new proadd();
    public static TrayIcon TIl;
    public  SystemTray tray = SystemTray.getSystemTray();

    @Override
    public void preInit(FMLPreInitializationEvent event)  {
        super.preInit(event);

        // 注册自定义协议处理程序
        URL.setURLStreamHandlerFactory(new ChatImage.ChatImageHandlerFactory());

        Display.setTitle(I18n.translateToLocal("window.jntmtitle.name")+Display.getTitle());
        if(SystemTray.isSupported()&& ConfigCore.isenabledTrayIcon) {

            TIs=new Image[17];
            for (int i=0;i< TIs.length;i++){
                try {
                    Image image = null;
                    image = ImageIO.read(Objects.requireNonNull(Jntm.class.getClassLoader().getResourceAsStream("assets/jntm/textures/gui/st/"+i+".png")));
                    if (image != null) {
                        TIs[i] =image;
                    }
                } catch (IOException | NullPointerException e) {
                    Jntm.logger.error(e);
                }
            }

            TIl=new TrayIcon(TIs[0],Display.getTitle());
            TIl.setImageAutoSize(true);//设置图像自适应
            try {
                tray.add(TIl);
            } catch (AWTException e) {
                Jntm.logger.error(e);
            }
            nt.start();
        }else {
            Jntm.logger.error("SystemTray Is NOT Supported,Or it's disable in config");
        }
        try {
            if (ConfigCore.isenabledRP) {
                GameConfig gc = new GameConfig(Paths.get(getrunpath("") + "options.txt"));
                gc.addResourcePack("jntm.zip", "jntm.zip");
                gc.writeToFile();
                List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao");
                defaultResourcePacks.add(JRP);
                //defaultResourcePacks.add(new FMLFolderResourcePack());
                ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), defaultResourcePacks, "field_110449_ao");
                FMLClientHandler.instance().refreshResources();
                ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png"))))), "field_152354_ay");
            }
        } catch (Exception e) {
            Jntm.logger.error(new RuntimeException(e));
        }

        List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao");
        defaultResourcePacks.add(new FolderResourcePack(new File(Jntm.class.getClassLoader().getResource("assets/jntm_pack-11.1.4").getFile())));
        //defaultResourcePacks.add(new FMLFolderResourcePack());
        ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), defaultResourcePacks, "field_110449_ao");
        FMLClientHandler.instance().refreshResources();
    }
    public static Jntm_RP JRP = new Jntm_RP();
    @SideOnly(Side.CLIENT)
    @Override
    public void init(FMLInitializationEvent event){
        super.init(event);
        RenderRegistryHandler.register(event);

    }
    public static class ModelRegistryObj {
        final Item item;
        final int meta;
        final ModelResourceLocation resource;

        public ModelRegistryObj(Item i, int m, ModelResourceLocation loc) {
            item = i;
            meta = m;
            resource = loc;
        }
    }
    public static class ModelBakeObj {
        final Item item;
        final ResourceLocation resource;
        final ItemMeshDefinition meshDefinition;
        public ModelBakeObj(Item i,  ModelResourceLocation location, ItemMeshDefinition itemMeshDefinition) {
            item = i;
            resource = location;
            meshDefinition = itemMeshDefinition;
        }
    }
    public static class StateMapObj {
        final Block block;
        final StateMapperBase map;
        public StateMapObj(Block b, StateMapperBase mapper) {
            block = b;
            map = mapper;
        }

    }
    @Override
    public void registerItemRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
    @Override
    public void registerItems(ModelRegistryEvent e) {
        super.registerItems(e);
        for(StateMapObj obj : statesToMap) {
            ModelLoader.setCustomStateMapper(obj.block, obj.map);
        }
        for(ModelBakeObj obj : modelsToBake) {
            ModelLoader.setCustomMeshDefinition(obj.item, obj.meshDefinition);
            if(obj.resource == null) {
                NonNullList<ItemStack> subItems = NonNullList.create();
                obj.item.getSubItems(CreativeTabs.SEARCH, subItems);
                for(ItemStack stack : subItems) {
                    ModelBakery.registerItemVariants(obj.item, obj.meshDefinition.getModelLocation(stack));
                }
            }
            else {
                ModelBakery.registerItemVariants(obj.item, obj.resource); // Ensure the custom model is loaded and prevent the default model from being loaded
            }
        }
        for(ModelRegistryObj obj : modelsToReg) {
            ModelLoader.setCustomModelResourceLocation(obj.item, obj.meta, obj.resource);
        }
    }
    @Override
    public void postInit(FMLPostInitializationEvent event){
        super.postInit(event);

        EventHandler.postInit = true;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void openhelpGui(GuiScreen e)
    {
        Minecraft.getMinecraft().displayGuiScreen(e);
    }
    @Nullable
    @Override
    public IAnimationStateMachine loadAsm(final ResourceLocation loc, final ImmutableMap<String, ITimeValue> parameters) {
        return ModelLoaderRegistry.loadASM(loc, parameters);
    }
    @Override
    // 刷新所有方块的方法
    public void refreshAllBlocks() {
        if (Minecraft.getMinecraft().world != null) {
            // 刷新渲染器，触发重新渲染所有方块
            Minecraft.getMinecraft().renderGlobal.updateChunks(0);
        }
    }
    public String getrunpath(String sp){
        String MP;
        StringBuilder tmp;
        String[] temp;
        temp = Loader.instance().getConfigDir().toURI().toString().split("/");
        tmp = new StringBuilder();
        for (int i = 1; i < temp.length - 1; i++) {
            tmp.append(temp[i]).append("/");
        }
        tmp.append(sp).append("/");
        MP = tmp.toString();
        return MP;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void openurl(String s){
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(URI.create(s));
                }
            }
        } catch (IOException | InternalError e) {
            e.printStackTrace();
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void OpenFE(String e) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        File dirToOpen = null;
        dirToOpen = new File(e);
        desktop.open(dirToOpen);
    }

    @Override
    public void showToase(String type, String i18n, String i18nt) {
        Minecraft.getMinecraft().getToastGui().add(new net.minecraft.client.gui.toasts.SystemToast(net.minecraft.client.gui.toasts.SystemToast.Type.valueOf(type),new TextComponentString(net.minecraft.client.resources.I18n.format(i18n)), new TextComponentString(net.minecraft.client.resources.I18n.format(i18nt))));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void OpenWB() throws IOException { }
    @Override
    @SideOnly(Side.CLIENT)
    public void regitem_end(){
        super.regitem_end();

    }
    class proadd extends Thread{
        @Override
        public void run(){
            while (true){
                for (int i=0;i< TIs.length;i++){
                    TIl.setImage(TIs[i]);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Jntm.logger.error(e);
                    }
                }
            }
        }
    }
}