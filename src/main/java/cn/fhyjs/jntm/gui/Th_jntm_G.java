package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.client.window.Th_jntmW;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Th_jntm_G extends GuiContainer {
    private final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/help_bgi.png");
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public Th_jntm_G(EntityPlayer player, World world) {
        super(new Jvav_C(player,world));
        this.xSize = 256;
        this.ySize = 256;
        this.guiTop = (int) (sr.getScaledHeight()*0.08);
        this.guiLeft = (int) (sr.getScaledWidth()*0.21);
        this.player = player;
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
            this.mc.player.closeScreen();
        }

        this.checkHotbarKeys(keyCode);

        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack())
        {
            if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode))
            {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
            }
            else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode))
            {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
            }
        }
    }
    public Thread mglw;
    @Override
    public void initGui()
    {
        // DEBUG
        System.out.println("Open GUI");
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, guiLeft+xSize-25, guiTop+5,20, 20, "X"));
        mglw=new Th_jntmW();
        mglw.start();
    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        switch (parButton.id){
            case 0:
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
    Minecraft client=Minecraft.getMinecraft();
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 256);
        //game
        ScaledResolution res = new ScaledResolution(client);
        double scaleW = client.displayWidth / res.getScaledWidth_double();
        double scaleH = client.displayHeight / res.getScaledHeight_double();

        GL11.glPushMatrix();
        //GL11.glScissor((int)((guiLeft+10)      * scaleW), (int)(client.displayHeight - ((guiTop+100) * scaleH)), (int)((xSize-10) * scaleW), (int)((ySize-10) * scaleH));
        //GlStateManager.translate(10,10,120);
        square(0,0,50,50,new byte[]{0,0,0});
        GL11.glPopMatrix();
    }
    public static void square(int x, int y, int w, int h, byte[] rgb) {
        glColor3b(rgb[0], rgb[1], rgb[2]);
        glBegin(GL_QUADS);
        glVertex2i(x, y);
        glVertex2i(x + w, y);
        glVertex2i(x + w, y + h);
        glVertex2i(x,y+h);
        glEnd();

    }
    @Override
    protected void  drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
}
