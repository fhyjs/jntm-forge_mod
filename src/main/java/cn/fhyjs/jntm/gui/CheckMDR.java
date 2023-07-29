package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;

public class CheckMDR extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation( "textures/gui/demo_background.png");
    public CheckMDR() {
        super(new Jvav_C());
        this.xSize = 256;
        this.ySize = 256;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 256);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        if (isInstallMMDR()){
            CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
        }
        //ScaledResolution sr = new ScaledResolution(mc);
        //this.guiTop = (int) (sr.getScaledHeight()*0.1);
        //this.guiLeft = (int) (sr.getScaledWidth()*0.1);
        buttonList.add(new GuiButton(0, guiLeft+xSize-35, guiTop+5,20, 20, "X"));
        buttonList.add(new GuiButton(1, guiLeft+10, guiTop+45,50, 20, I18n.format("btn.install")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.exdrawString(10,10,1.5f,0, I18n.format("gui.jntm.plugin.not","kaimyentity"));
        this.exdrawString(10,25,1.2f,0, I18n.format("gui.jntm.plugin.future.not","kaimyentity"));
        for (GuiButton guiButton : buttonList) {
            if (guiButton.id==1){
                guiButton.enabled=!new File(FMLClientHandler.instance().getSavesDir().getParent()+"/mods/KAIMyEntity-1.12.2-BUILD_2023.07.28.jar").exists();
                if (!guiButton.enabled) {
                    this.exdrawString(10,60,1.2f,0xff0000, I18n.format("gui.tip.restart"));
                }
                break;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id){
            case 0:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
            case 1:
                cn.fhyjs.jntm.utility.Dlf.run("http://fhyjs.eu.org/jntm/KAIMyEntity-1.12.2-BUILD_2023.07.28.jar", FMLClientHandler.instance().getSavesDir().getParent()+"/mods/KAIMyEntity-1.12.2-BUILD_2023.07.28.jar",Jntm.logger);
                cn.fhyjs.jntm.utility.Dlf.run("http://fhyjs.eu.org/jntm/rt.zip", FMLClientHandler.instance().getSavesDir().getParent()+"/rt.zip",Jntm.logger);
                cn.fhyjs.jntm.utility.unzip.run(FMLClientHandler.instance().getSavesDir().getParent()+"/rt.zip",FMLClientHandler.instance().getSavesDir().getParent(),"",true,Jntm.logger);
                new File(FMLClientHandler.instance().getSavesDir().getParent()+"/rt.zip").delete();
                break;
        }
    }

    public static boolean isInstallMMDR(){
        return Loader.isModLoaded("kaimyentity");
    }
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
}
