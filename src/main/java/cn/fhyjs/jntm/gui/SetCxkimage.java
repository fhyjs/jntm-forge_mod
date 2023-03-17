package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityCxkImage;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import cn.fhyjs.jntm.network.SCINMessage;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class SetCxkimage extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/help_bgi.png");
    private EntityPlayer player;
    private TileEntityCxkImage te;
    private GuiTextField textField=null ;
    BlockPos bp;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public SetCxkimage(EntityPlayer player, World world, BlockPos bp) {
        super(new SetCxkimage_C(player,world,bp));
        this.bp=bp;
        this.xSize = 256;
        this.ySize = 256;
        this.guiTop = (int) (sr.getScaledHeight()*0.08);
        this.guiLeft = (int) (sr.getScaledWidth()*0.21);
        this.player = player;
        this.te= (TileEntityCxkImage) world.getTileEntity(bp);
    }
    @Override
    public void updateScreen() {
        super.updateScreen();
        textField.updateCursorCounter();

    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            Jntm.logger.fatal(e);
        }
    }
    @Override
    public void handleInput() {
        int key;
        int num;
        while(Keyboard.next()) {
            if (Keyboard.getEventKey() == 1) {
                this.mc.displayGuiScreen((GuiScreen)null);
                return;
            }

            boolean pressed = Keyboard.getEventKeyState();
            pressed = this.textField.isFocused();
            key = Keyboard.getEventCharacter();
            num = Keyboard.getEventKey();

            if (pressed) {
                this.textField.textboxKeyTyped((char)key, num);
            }
        }

        while(Mouse.next()) {
            int btn = Mouse.getEventButton();
            boolean pressed = Mouse.getEventButtonState();
            key = Mouse.getEventX();
            num = Mouse.getEventY();
            if (pressed) {
                int x = key * this.width / this.mc.displayWidth;
                int y = this.height - num * this.height / this.mc.displayHeight - 1;

                try {
                    this.mouseClicked(x, y, btn);
                } catch (Throwable var9) {
                    var9.printStackTrace();
                }

                this.textField.mouseClicked(x, y, btn);
            }
        }

    }
    @Override
    public void initGui()
    {
        // DEBUG
        System.out.println("Open GUI");
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(1,140,150,100,20,I18n.format("gui.done")));
        buttonList.add(new GuiButton(0,260,150,100,20,I18n.format("gui.cancel")));
        textField = new GuiTextField(0, this.fontRenderer, 0, 0, 100, 10);
    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        switch (parButton.id){
            case 0:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
            case 1:
                CommonProxy.INSTANCE.sendToServer(new SCINMessage( "{\"x\":"+bp.getX()+", \"y\":"+bp.getY()+",\"z\":"+bp.getZ()+",\"url\":\""+textField.getText()+"\"}"));
                break;
        }

    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            Jntm.logger.fatal(e);
        }
        //textField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);

        //JOptionPane.showMessageDialog(null,"3");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 256);
    }
    @Override
    protected void  drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        exdrawString(10,5,1,0,I18n.format("gui.jntm.jvav.title"));
    }
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
}
