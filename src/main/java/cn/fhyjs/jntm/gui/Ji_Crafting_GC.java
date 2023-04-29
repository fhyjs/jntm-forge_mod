package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;

import cn.fhyjs.jntm.common.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Ji_Crafting_GC extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/container/dispenser.png");
    private static final ResourceLocation ICON = new ResourceLocation(Jntm.MODID, "textures/gui/wireless_io.png");
    private static final ResourceLocation SLOT = new ResourceLocation(Jntm.MODID, "textures/gui/wireless_io_slot_config.png");

    private static final int SLOT_NUM = 47;
    private GuiButtonToggle ioModeToggle;
    private GuiButtonToggle filterModeToggle;
    private boolean isInput;
    private boolean isBlacklist;

    public Ji_Crafting_GC(IInventory playerInventory, BlockPos blockPos, World world) {
        super(new Ji_Crafting_C(playerInventory, blockPos,world));
        //isInput = ItemWirelessIO.isInputMode(wirelessIO);
        //isBlacklist = ItemWirelessIO.isBlacklist(wirelessIO);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();

        ioModeToggle = new GuiButtonToggle(1, guiLeft + 26, guiTop + 35, 12, 16, isInput);
        ioModeToggle.initTextureValues(44, 0, -12, 16, ICON);
        filterModeToggle = new GuiButtonToggle(2, guiLeft + 136, guiTop + 26, 16, 16, isBlacklist);
        filterModeToggle.initTextureValues(72, 0, -16, 16, ICON);
        GuiButtonImage configButton = new GuiButtonImage(3, guiLeft + 136, guiTop + 44, 16, 16,
                88, 0, 16, ICON);

        addButton(ioModeToggle);
        addButton(filterModeToggle);
        addButton(configButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        String ioModeText = isInput ?
                I18n.format("tooltips.touhou_little_maid.wireless_io.io_mode.input") :
                I18n.format("tooltips.touhou_little_maid.wireless_io.io_mode.output");
        String filterModeText = isBlacklist ?
                I18n.format("tooltips.touhou_little_maid.wireless_io.filter_mode.blacklist") :
                I18n.format("tooltips.touhou_little_maid.wireless_io.filter_mode.whitelist");
        fontRenderer.drawString(ioModeText, guiLeft - fontRenderer.getStringWidth(ioModeText) - 5, guiTop + 5, 0xffffff);
        fontRenderer.drawString(filterModeText, guiLeft - fontRenderer.getStringWidth(filterModeText) - 5, guiTop + 15, 0xffffff);

        boolean xInRange;
        boolean yInRange;

        xInRange = (guiLeft + 26) < mouseX && mouseX < (guiLeft + 38);
        yInRange = (guiTop + 35) < mouseY && mouseY < (guiTop + 51);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.wireless_io.io_mode"), mouseX, mouseY);
        }

        xInRange = (guiLeft + 136) < mouseX && mouseX < (guiLeft + 152);
        yInRange = (guiTop + 26) < mouseY && mouseY < (guiTop + 42);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.wireless_io.filter_mode"), mouseX, mouseY);
        }

        xInRange = (guiLeft + 136) < mouseX && mouseX < (guiLeft + 152);
        yInRange = (guiTop + 44) < mouseY && mouseY < (guiTop + 60);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.wireless_io.config_slot"), mouseX, mouseY);
        }

        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            isInput = !isInput;
            ioModeToggle.setStateTriggered(isInput);
            //CommonProxy.INSTANCE.sendToServer(new WirelessIOGuiMessage(isInput, isBlacklist));
        }
        if (button.id == 2) {
            isBlacklist = !isBlacklist;
            filterModeToggle.setStateTriggered(isBlacklist);
            //CommonProxy.INSTANCE.sendToServer(new WirelessIOGuiMessage(isInput, isBlacklist));
        }
        if (button.id == 3) {
            //if (mc.player.getHeldItemMainhand().getItem() == MaidItems.WIRELESS_IO) {
            //    mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiConfigSlot(mc.player.getHeldItemMainhand())));
            //}
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        GlStateManager.color(1, 1, 1);
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        mc.getTextureManager().bindTexture(ICON);
        this.drawTexturedModalRect(guiLeft + 24, guiTop + 17, 0, 0, 16, 16);
        this.drawTexturedModalRect(guiLeft + 24, guiTop + 53, 16, 0, 16, 16);
    }

}
