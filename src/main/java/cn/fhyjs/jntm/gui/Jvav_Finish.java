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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

import static cn.fhyjs.jntm.Jntm.MODID;

public class Jvav_Finish extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(MODID, "textures/gui/insjvav/jvav_finish.png");
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public Jvav_Finish(EntityPlayer player, World world) {
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
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new Cibtn(0, guiLeft+xSize-20, guiTop+4,13, 13, "X",new ResourceLocation("jntm:textures/gui/insjvav/close.png"),new ResourceLocation("jntm:textures/gui/insjvav/close_o.png"),0));
        buttonList.add(new Cibtn(0, guiLeft+70, guiTop+ySize-60,90, 40, I18n.format("gui.done"),new ResourceLocation("jntm:textures/gui/insjvav/close.png"),new ResourceLocation("jntm:textures/gui/insjvav/btn_o.png"),0));
    }
    @Override
    protected void actionPerformed(GuiButton parButton){
        switch (parButton.id){
            case 0:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(4));
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
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
    }
    @Override
    protected void  drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        exdrawString(10,5,1,0,I18n.format("gui.jntm.jvav.title"));
        exdrawString(15,27,3f,0,I18n.format("gui.jntm.jvav.il7"));
        exdrawString(32,61,2f,0,I18n.format("gui.jntm.jvav.il8"));
    }
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
    @Override
    public void onGuiClosed() {
        if (this.mc.player != null) {
            this.inventorySlots.onContainerClosed(this.mc.player);
        }
        this.actionPerformed(new GuiButton(0,0,0,""));
    }
}