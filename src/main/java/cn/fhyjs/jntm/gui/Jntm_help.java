package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import javafx.stage.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.input.Keyboard;
import sun.security.mscapi.CPublicKey;
import vazkii.patchouli.common.base.Patchouli;
import vazkii.patchouli.common.item.PatchouliItems;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.fhyjs.jntm.Jntm.proxy;
import static net.minecraft.client.gui.toasts.SystemToast.Type.TUTORIAL_HINT;

public class Jntm_help extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/help_bgi.png");
    public GuiButton gethelpbook,buttonfbc1,buttonfbc2,buttonDone,buttonopenmcmod_jei,buttonopenmcmod_patchouli,buttonopenmodp;
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public Jntm_help(EntityPlayer player, World world) {
        super(new Jntm_help_container(player,world));
        this.xSize = 256;
        this.ySize = 214;
        this.guiTop = (int) (sr.getScaledHeight()*0.08);
        this.guiLeft = (int) (sr.getScaledWidth()*0.21);
        this.player = player;
    }
    @Override
    public void initGui()
    {
        // DEBUG
        System.out.println("Open Jntm_help GUI");
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        buttonDone = new GuiButton(0, guiLeft+xSize-25, guiTop+5,20, 20, "X");
        buttonList.add(buttonDone);

        buttonopenmcmod_jei = new GuiButton(0, guiLeft+15, guiTop+90,60, 20, I18n.format("gui.jntm.help.dl_jei"));
        buttonList.add(buttonopenmcmod_jei);

        buttonopenmodp = new GuiButton(0, guiLeft+155, guiTop+90,60, 20, I18n.format("gui.jntm.help.openmodp"));
        buttonList.add(buttonopenmodp);

        buttonopenmcmod_patchouli = new GuiButton(0, guiLeft+85, guiTop+90,65, 20, I18n.format("gui.jntm.help.dl_patchouli"));
        buttonList.add(buttonopenmcmod_patchouli);

        gethelpbook = new GuiButton(0, guiLeft+15, guiTop+30,60, 20, I18n.format("gui.jntm.help.get_book"));
        gethelpbook.enabled=false;
        buttonList.add(gethelpbook);

        buttonfbc1 = new GuiButton(0, guiLeft+15, guiTop+140,60, 20, I18n.format("gui.jntm.help.fbc1"));
        buttonList.add(buttonfbc1);

        buttonfbc2 = new GuiButton(0, guiLeft+85, guiTop+140,60, 20, I18n.format("gui.jntm.help.fbc2"));
        buttonList.add(buttonfbc2);
    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        if (parButton == buttonDone)
        {
            CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
            return;
        }
        if (parButton == gethelpbook){
            CommonProxy.INSTANCE.sendToServer(new JntmMessage(1));
            Minecraft.getMinecraft().getToastGui().add(new SystemToast(TUTORIAL_HINT,new TextComponentString(I18n.format("mod.jntm.name")), new TextComponentString(I18n.format("gui.toast.AHB"))));
            return;
        }
        if (parButton == buttonopenmcmod_jei){
            proxy.openurl("https://www.mcmod.cn/class/459.html");
        }
        if (parButton == buttonopenmcmod_patchouli){
            proxy.openurl("https://www.mcmod.cn/class/1388.html");
        }
        if (parButton == buttonopenmodp){
            proxy.OpenFE(MP);
        }
        if (parButton==buttonfbc1){
            proxy.openurl("https://github.com/fhyjs/jntm-forge_mod/issues/");
        }
        if (parButton==buttonfbc2){
            proxy.openurl("https://hh27849339.icoc.ws/msgBoard.jsp");
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
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 214);

    }
    private String text1,MP;
    @Override
    protected void  drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("gui.jntm.help.title"), 110, 10, 0xffffff);
        if (!Loader.isModLoaded("patchouli")) {
            text1 = I18n.format("gui.jntm.help.patchouli") + I18n.format("gui.jntm.help.notload");
            gethelpbook.enabled = false;
        } else {
            text1 = I18n.format("gui.jntm.help.patchouli") + I18n.format("gui.jntm.help.load");
            gethelpbook.enabled = true;
        }
        this.fontRenderer.drawString(I18n.format("gui.jntm.help.desc"), 8, 20, 4210752);
        this.fontRenderer.drawString(text1, 8, 50, 0x0080ff);
        this.fontRenderer.drawString(I18n.format("gui.jntm.help.desc1"), 8, 60, 4210752);
        StringBuilder tmp;
        {
            String[] temp;
            temp = Loader.instance().getConfigDir().toURI().toString().split("/");
            tmp = new StringBuilder();
            for (int i = 1; i < temp.length - 1; i++) {
                tmp.append(temp[i]).append("/");
            }
            tmp.append("mods/");
            MP = tmp.toString();

        }//路径处理
        int i = 0;
        for (String string:fontRenderer.listFormattedStringToWidth(I18n.format("gui.jntm.help.desc3") + tmp + I18n.format("gui.jntm.help.desc3_1"),xSize-10)) {
            this.fontRenderer.drawString(string,8+5*i,70 + Math.round(i * fontRenderer.FONT_HEIGHT),4210752);
            i++;
        }
        this.fontRenderer.drawString(I18n.format("gui.jntm.help.desc5"), 8, 130, 0x0dff00);
    }
}
