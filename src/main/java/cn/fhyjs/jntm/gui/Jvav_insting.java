package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static cn.fhyjs.jntm.Jntm.MODID;

public class Jvav_insting extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(MODID, "textures/gui/insjvav/installing.png");
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public Jvav_insting(EntityPlayer player, World world) {
        super(new Jvav_C(player,world));
        this.xSize = 256;
        this.ySize = 256;
        this.guiTop = (int) (sr.getScaledHeight()*0.08);
        this.guiLeft = (int) (sr.getScaledWidth()*0.21);
        this.player = player;
    }
    public CbBtn cb1;
    @Override
    public void initGui()
    {
        // DEBUG
        System.out.println("Open GUI");
        buttonList.clear();
        //Keyboard.enableRepeatEvents(true);
        progress=new proadd();
        progress.start();
    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        switch (parButton.id){
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
        }

    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        //JOptionPane.showMessageDialog(null,"3");
    }
    private proadd progress;
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 245);
        //能量棒
        drawTexturedModalRect(guiLeft+23, guiTop+89, 20, 247, (int) (210*((float)progress.progress/100)), 7);
    }
    @Override
    protected void  drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        exdrawString(10,5,1,0,I18n.format("gui.jntm.jvav.title"));
        exdrawString(28,63,1.2f,0,I18n.format("gui.jntm.jvav.il1"));
        exdrawString(20,53,2f,0,I18n.format("gui.jntm.jvav.il2"));
        int i = 0;
        for (String string:fontRenderer.listFormattedStringToWidth(I18n.format("gui.jntm.jvav.il3"),xSize-50)) {
            exdrawString(8+5*i,105 + Math.round(i * fontRenderer.FONT_HEIGHT),1.2f,0x808080,string);
            i++;
        }
        i=0;
        for (String string:fontRenderer.listFormattedStringToWidth(I18n.format("gui.jntm.jvav.il4"),xSize-50)) {
            exdrawString(8+5*i,125 + Math.round(i * fontRenderer.FONT_HEIGHT),1.2f,0x808080,string);
            i++;
        }
        exdrawString(15,57,3f,0,I18n.format("gui.jntm.jvav.il5"));
        exdrawString(28,101,2f,0xffffff,I18n.format("gui.jntm.jvav.il6"));
    }
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
    @Override
    public void onGuiClosed()
    {
        if (this.mc.player != null)
        {
            this.inventorySlots.onContainerClosed(this.mc.player);
        }
        progress.stop();
    }
}

class proadd extends Thread{
    int progress;
    public static ad jf = new ad("AD");
    public proadd() {
    }
    //run方法是每个线程运行过程中都必须执行的方法
    @Override
    public void run() {
        for (int i = 0; i < 101; i++) {
            progress=i;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                 Jntm.logger.error(e);
            }
            if(i==25||i==50||i==95){
                jf.setPreferredSize(new Dimension(744,352));
                jf.setLocationRelativeTo(null);// 使窗口显示在屏幕中央
                jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jf.setAlwaysOnTop(true); //窗体置顶
                final JPanel panel= new JPanel() {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;

                        InputStream imgIS = Jntm.class.getClassLoader().getResourceAsStream("assets/jntm/textures/gui/insjvav/ad.png");
                        Image image = null;
                        try {
                            image = ImageIO.read(imgIS);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        g2d.drawImage(image, 0, 0,744,352, this);

                    }
                };
                //panel.setBounds(10, 10, 400, 300);
                jf.add(panel);
                jf.pack();
                jf.setVisible(true);

            }
            while(jf.run){
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    Jntm.logger.error(e);
                }
            }
        }
        this.stop();
    }
}
class ad extends JFrame{
    public boolean run;
    public ad(String a){
        super(a);
        run=false;
    }
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        run=true;
    }
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        super.processWindowEvent(e);

        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            switch (this.getDefaultCloseOperation()) {
                case HIDE_ON_CLOSE:
                    setVisible(false);
                    break;
                case DISPOSE_ON_CLOSE:
                    dispose();
                    run=false;
                    proadd.jf = new ad("AD");
                    break;
                case EXIT_ON_CLOSE:
                    // This needs to match the checkExit call in
                    // setDefaultCloseOperation
                    //System.exit(0);
                    break;
                case DO_NOTHING_ON_CLOSE:
                default:
            }
        }
    }
}
