package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TEJimPlayer;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.common.pstest;
import cn.fhyjs.jntm.network.JntmMessage;
import cn.fhyjs.jntm.network.Opt_Ply_Message;
import cn.fhyjs.jntm.network.SCINMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.fhyjs.jntm.Jntm.proxy;
import static net.minecraft.client.gui.toasts.SystemToast.Type.TUTORIAL_HINT;

public class RSMPlayerG extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/help_bgi.png");
    private BlockPos bp;
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public RSMPlayerG(EntityPlayer player, World world, BlockPos bp) {
        super(new RSMPlayerC(player,world,bp));
        this.xSize = 256;
        this.ySize = 214;
        this.guiTop = (int) (sr.getScaledHeight()*0.08);
        this.guiLeft = (int) (sr.getScaledWidth()*0.21);
        this.player = player;
        this.bp = bp;
    }
        private int selected;
    FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }
    public void selectModIndex(int index)
    {
        this.selected = index;
        CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player, "playjim_setfilename "+ (sl.get(selected)).replaceAll(" ","\n")+" "+bp.getX()+" "+bp.getY()+" "+bp.getZ()));
    }
    public static String[] fln;
    public boolean modIndexSelected(int index)
    {
        return index == selected;
    }
    private GuiScrollingList gsl ;
    private final ArrayList<String> sl=new ArrayList<>();
    @Override
    public void initGui()
    {

        sl.clear();
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, guiLeft+xSize-25, guiTop+5,20, 20, "X"));
        buttonList.add(new GuiButton(1, guiLeft+10, guiTop+30,20, 20, I18n.format("gui.play")));
        buttonList.add(new GuiButton(2, guiLeft+30, guiTop+30,20, 20, I18n.format("gui.stop")));
        CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player,"getalljim"));
        int ti=0;
        while (true){
            ti++;
            if (fln!=null){

                break;
            }
            if (ti>=1000){
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                Minecraft.getMinecraft().getToastGui().add(new SystemToast(TUTORIAL_HINT,new TextComponentString(I18n.format("mod.jntm.name")), new TextComponentString(I18n.format("gui.toast.guifaild"))));
                return;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        gsl=new GL<>(this,sl,200,15) ;
        sl.addAll(Arrays.asList(fln));
        for (String i : sl){
            if (i!=null&&((TEJimPlayer) player.world.getTileEntity(bp)).getCount().contains(i)){
                selected = sl.indexOf(i);
                break;
            }
        }

    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        switch (parButton.id){
            case 0:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
            case 1:
                if (sl.get(selected)==null){
                    Minecraft.getMinecraft().getToastGui().add(new SystemToast(TUTORIAL_HINT,new TextComponentString(I18n.format("mod.jntm.name")), new TextComponentString(I18n.format("gui.toast.ns"))));
                    break;
                }
                CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player, "playjim "+ ( sl.get(selected)).replaceAll(" ","\n")+" "+bp.getX()+" "+bp.getY()+" "+bp.getZ()));
                break;
            case 2:
                CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player, "stopjim "+bp.getX()+" "+bp.getY()+" "+bp.getZ()));
                break;
        }
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.gsl != null)
            this.gsl.drawScreen(mouseX, mouseY, partialTicks);

        //super.renderHoveredToolTip(mouseX, mouseY);
        //JOptionPane.showMessageDialog(null,"3");
    }
    @Override
    public void handleMouseInput() throws IOException
    {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();
        if (this.gsl != null)
            this.gsl.handleMouseInput(mouseX, mouseY);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 214);

    }
    @Override
    protected void  drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        exdrawString(10,10,1,0,I18n.format("gui.jntm.rsmp.title"));
        exdrawString(30,ySize-10,1,0,I18n.format("gui.jntm.rsmp.chose"));
    }
    public void exdrawString(int x, int y, float size,int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }

}
