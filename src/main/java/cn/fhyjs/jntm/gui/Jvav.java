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

import java.io.IOException;

public class Jvav extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/jvav_bgi.png");
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public Jvav(EntityPlayer player, World world) {
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
        buttonList.add(new Cibtn(0, guiLeft+xSize-100, guiTop+ySize-25  ,30, 20, I18n.format("gui.jntm.jvav.cal"),new ResourceLocation("jntm:textures/gui/insjvav/close.png"),new ResourceLocation("jntm:textures/gui/insjvav/btn_o.png"),0));
        buttonList.add(new Cibtn(1, guiLeft+xSize-60, guiTop+ySize-25  ,30, 20, I18n.format("gui.jntm.jvav.inst"),new ResourceLocation("jntm:textures/gui/insjvav/close.png"),new ResourceLocation("jntm:textures/gui/insjvav/btn_o.png"),0));
        cb1 =new CbBtn(2,guiLeft+20, guiTop+ySize-20,10,false);
        buttonList.add(cb1);
    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        switch (parButton.id){
            case 0:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
            case 1:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(2));
                break;
            case 2:
                cb1.setchd(!cb1.getchd());
                break;
        }

    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
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
        exdrawString(40,40,2,0,I18n.format("gui.jntm.jvav.welcome"));
        int i = 0;
        for (String string:fontRenderer.listFormattedStringToWidth(I18n.format("gui.jntm.jvav.desc1"),xSize-15)) {
            this.fontRenderer.drawString(string,8+5*i,110 + Math.round(i * fontRenderer.FONT_HEIGHT),0);
            i++;
        }
        exdrawString(30,160,1,0,I18n.format("gui.jntm.jvav.desc2"));
        exdrawString(30,170,1,0,I18n.format("gui.jntm.jvav.desc3"));
        exdrawString(30,236,1,0,I18n.format("gui.jntm.jvav.cof"));
    }
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
}
