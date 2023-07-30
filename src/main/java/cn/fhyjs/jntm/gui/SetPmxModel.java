package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TEPmxModel;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import cn.fhyjs.jntm.network.PacketUpdateTileNBT;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import static cn.fhyjs.jntm.gui.CheckMDR.isInstallMMDR;

public class SetPmxModel extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation( "textures/gui/demo_background.png");
    TEPmxModel te;
    public SetPmxModel(TEPmxModel te) {
        super(new Jvav_C());
        this.xSize = 256;
        this.ySize = 256;
        this.te=te;
    }
    private GuiTextField textField;
    private GuiTextField textField1;
    private GuiTextField textField2;
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 256);
    }
    @Override
    public void updateScreen() {
        super.updateScreen();
        textField.updateCursorCounter();
        textField1.updateCursorCounter();
        textField2.updateCursorCounter();
    }
    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        if (!isInstallMMDR()){
            CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
        }
        buttonList.add(new GuiButton(0, guiLeft+xSize-35, guiTop+5,20, 20, "X"));
        textField = new GuiTextField(0, this.fontRenderer, guiLeft+12, guiTop+22, 100, 15){
            @Override
            public boolean textboxKeyTyped(char typedChar, int keyCode) {
                boolean b = super.textboxKeyTyped(typedChar, keyCode);
                te.modelName=textField.getText();
                CommonProxy.INSTANCE.sendToServer(new PacketUpdateTileNBT(te.getUpdateTag()));
                return b;
            }
        };
        textField.setMaxStringLength(114514);
        if (te.getModelName()!=null){
            textField.setText(te.getModelName());
        }

        textField1 = new GuiTextField(0, this.fontRenderer, guiLeft+12, guiTop+50, 100, 15){
            @Override
            public boolean textboxKeyTyped(char typedChar, int keyCode) {
                boolean b = super.textboxKeyTyped(typedChar, keyCode);
                te.redStoneActionName=textField1.getText();
                CommonProxy.INSTANCE.sendToServer(new PacketUpdateTileNBT(te.getUpdateTag()));
                return b;
            }
        };
        textField1.setMaxStringLength(114514);
        if (te.redStoneActionName!=null){
            textField1.setText(te.redStoneActionName);
        }

        textField2 = new GuiTextField(0, this.fontRenderer, guiLeft+12, guiTop+80, 100, 15){
            @Override
            public boolean textboxKeyTyped(char typedChar, int keyCode) {
                boolean b = super.textboxKeyTyped(typedChar, keyCode);
                te.idleActionName=textField2.getText();
                CommonProxy.INSTANCE.sendToServer(new PacketUpdateTileNBT(te.getUpdateTag()));
                return b;
            }
        };
        textField2.setMaxStringLength(114514);
        if (te.idleActionName!=null){
            textField2.setText(te.idleActionName);
        }
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        this.textField.textboxKeyTyped(typedChar, keyCode);
        this.textField1.textboxKeyTyped(typedChar, keyCode);
        this.textField2.textboxKeyTyped(typedChar, keyCode);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
        textField1.drawTextBox();
        textField2.drawTextBox();

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX,mouseY,mouseButton);
        textField1.mouseClicked(mouseX,mouseY,mouseButton);
        textField2.mouseClicked(mouseX,mouseY,mouseButton);
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
    public void handleInput() throws IOException {
        super.handleInput();
        te.markDirty();
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        exdrawString(10,10,1,0,I18n.format("gui.model.name"));
        exdrawString(10,40,1,0, Blocks.REDSTONE_BLOCK.getLocalizedName()+I18n.format("gui.action.name")+"(.vmd)");
        exdrawString(10,70,1,0, I18n.format("gui.action.idle.name")+"(.vmd)");

        File f = new File(FMLClientHandler.instance().getSavesDir().getParent() + "/KAIMyEntity", te.getModelName());
        if (!f.exists()||!f.isDirectory()) {
            exdrawString(130, 23, 1, 0xff0000, I18n.format("gui.file.notfound"));
            textField.setTextColor(0xff0000);
        }else {
            textField.setTextColor(0x00ff00);
        }
        File fa = new File(f,textField1.getText()+".vmd");
        if (!fa.exists()||!fa.isFile()) {
            exdrawString(130,  53, 1, 0xff0000, I18n.format("gui.file.notfound"));
            textField1.setTextColor(0xff0000);
        }else {
            textField1.setTextColor(0x00ff00);
        }
        File fia = new File(f,textField2.getText()+".vmd");
        if (!fia.exists()||!fia.isFile()) {
            exdrawString(130,  83, 1, 0xff0000, I18n.format("gui.file.notfound"));
            textField2.setTextColor(0xff0000);
        }else {
            textField2.setTextColor(0x00ff00);
        }
    }

    @Override
    public void onGuiClosed() {
        te.modelName=textField.getText();
        te.redStoneActionName=textField1.getText();
        te.idleActionName=textField2.getText();
        CommonProxy.INSTANCE.sendToServer(new PacketUpdateTileNBT(te.getUpdateTag()));
        super.onGuiClosed();
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
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
}
