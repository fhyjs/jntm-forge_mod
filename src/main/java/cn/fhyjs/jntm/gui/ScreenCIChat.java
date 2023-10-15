package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ScreenCIChat extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation( "textures/gui/demo_background.png");
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    private GuiTextField textFieldCIURL;
    private GuiTextField textFieldCIINFO;
    private GuiTextField textFieldCIW;
    private GuiTextField textFieldCIH;
    public ScreenCIChat() {
        super(new Jvav_C());
        this.xSize = 256;
        this.ySize = 170;
        this.guiTop = (int) (sr.getScaledHeight()*0.08);
        this.guiLeft = (int) (sr.getScaledWidth()*0.21);
    }
    @Override
    public void updateScreen() {
        super.updateScreen();
        textFieldCIH.updateCursorCounter();
        textFieldCIINFO.updateCursorCounter();
        textFieldCIW.updateCursorCounter();
        textFieldCIURL.updateCursorCounter();
    }
    boolean lang_cn;
    @Override
    public void initGui() {
        super.initGui();
        lang_cn=mc.getLanguageManager().getCurrentLanguage().getLanguageCode().equalsIgnoreCase("zh_cn");
        buttonList.add(new GuiButton(0, guiLeft+xSize-35, guiTop+5,20, 20, "X"));
        buttonList.add(new GuiButton(1,guiLeft+xSize-32,guiTop+ySize-30,20,20, lang_cn?"发送":"Send"));
        buttonList.add(new GuiButton(1,guiLeft+xSize-64,guiTop+ySize-30,20,20, lang_cn?"添加":"Add"));
        textFieldCIURL = new GuiTextField(0, this.fontRenderer, guiLeft+12, guiTop+80, 100, 15);
        textFieldCIH = new GuiTextField(1, this.fontRenderer, guiLeft+12, guiTop+100, 20, 15);
        textFieldCIW = new GuiTextField(2, this.fontRenderer, guiLeft+60, guiTop+100, 20, 15);
        textFieldCIINFO = new GuiTextField(3, this.fontRenderer, guiLeft+12, guiTop+130, 100, 15);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString(lang_cn?"宽":"Width",35,105,0,true);
        fontRenderer.drawString("URL",15,70,0,true);
        fontRenderer.drawString(lang_cn?"高":"Height",85,105,0,true);
        fontRenderer.drawString(lang_cn?"图片信息":"Information",15,120,0,true);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textFieldCIINFO.mouseClicked(mouseX,mouseY,mouseButton);
        textFieldCIW.mouseClicked(mouseX,mouseY,mouseButton);
        textFieldCIH.mouseClicked(mouseX,mouseY,mouseButton);
        textFieldCIURL.mouseClicked(mouseX,mouseY,mouseButton);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        textFieldCIURL.drawTextBox();
        textFieldCIH.drawTextBox();
        textFieldCIW.drawTextBox();
        textFieldCIINFO.drawTextBox();

    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        this.textFieldCIINFO.textboxKeyTyped(typedChar, keyCode);
        if ((textFieldCIW.isFocused()||textFieldCIH.isFocused())&&typedChar>='0'&&typedChar<='9'||keyCode==Keyboard.KEY_BACK||keyCode==Keyboard.KEY_DELETE||keyCode==Keyboard.KEY_LEFT||keyCode==Keyboard.KEY_RIGHT) {
            this.textFieldCIH.textboxKeyTyped(typedChar, keyCode);
            this.textFieldCIW.textboxKeyTyped(typedChar, keyCode);
        }
        this.textFieldCIURL.textboxKeyTyped(typedChar, keyCode);
    }
    @Override
    public void handleKeyboardInput() throws IOException
    {
        char c0 = Keyboard.getEventCharacter();
        if (c0=='e') {
            this.keyTyped(c0,c0);
            return;
        }
        super.handleKeyboardInput();
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id){
            case 0:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
            case 1:

                break;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 170);
    }
}
