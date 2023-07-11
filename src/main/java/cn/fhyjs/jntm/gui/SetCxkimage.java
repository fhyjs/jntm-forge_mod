package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityCxkImage;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import cn.fhyjs.jntm.network.SCINMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static cn.fhyjs.jntm.Jntm.proxy;

public class SetCxkimage extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/help_bgi.png");
    private static ResourceLocation WITHER_SKELETON_TEXTURES = null;
    private EntityPlayer player;
    private TileEntityCxkImage te;
    private GuiTextField textField=null ;
    private GuiButton btn;
    private String uri,uro;
    BlockPos bp;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    private String er;

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
        buttonList.add(btn=new GuiButton(1,guiLeft+10,guiTop+40,100,20,I18n.format("gui.done")));
        buttonList.add(new GuiButton(0,guiLeft+150,guiTop+40,100,20,I18n.format("gui.cancel")));
        textField = new GuiTextField(0, this.fontRenderer, guiLeft+35, guiTop+22, 200, 15);
        textField.setMaxStringLength(114514);
        btn.enabled=false;
        if (!Objects.equals(te.count, "")){
            textField.setText(te.count);
        }
        buttonList.add(new GuiButton(2,guiLeft+10, guiTop+20,20,20,I18n.format("gui.paste")));
    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        switch (parButton.id){
            case 0:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
            case 1:
                CommonProxy.INSTANCE.sendToServer(new SCINMessage( "{\"x\":"+bp.getX()+", \"y\":"+bp.getY()+",\"z\":"+bp.getZ()+",\"url\":\""+textField.getText()+"\"}"));
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
            case 2:
                try {
                    textField.setText(proxy.getCB());
                } catch (UnsupportedFlavorException e) {
                    Jntm.logger.error(new RuntimeException(e));
                }
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
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        textField.drawTextBox();
        //JOptionPane.showMessageDialog(null,"3");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 256);
        if (WITHER_SKELETON_TEXTURES!=null){
            mc.getTextureManager().bindTexture(WITHER_SKELETON_TEXTURES);
            this.drawTexturedModalRect(guiLeft+10, guiTop+70, 0, 0, 233, 170);
        }
    }
    @Override
    protected void  drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        exdrawString(100,5,1,0,I18n.format("gui.jntm.sci.title"));
        uri=textField.getText();
        if(!Objects.equals(uri, uro)){
            uro=uri;
            try {
                WITHER_SKELETON_TEXTURES = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages_t", new DynamicTexture(Objects.requireNonNull(DBI(uro,150,150  ))));
                er= "OK!";
                btn.enabled=true;

            } catch (IOException | NullPointerException | IllegalArgumentException e) {
                WITHER_SKELETON_TEXTURES=null;
                er= String.valueOf(e);
                btn.enabled=false;
            }
        }
        exdrawString(10,240,1f,0xff0800, String.valueOf(er));
    }
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
    public static BufferedImage DBI (String u,int w,int h) throws IOException,NullPointerException,IllegalArgumentException {
        if(u=="not_set"){
            return null;
        }
        BufferedImage image = null;
        URL url = new URL(u);
        image = ImageIO.read(url);
        Image tmp = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0,w,h, null);
        g2d.dispose();
        return dimg;
    }
}
