package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TEJimPlayer;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.network.JntmMessage;
import cn.fhyjs.jntm.network.Opt_Ply_Message;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Queue;

import static net.minecraft.client.gui.toasts.SystemToast.Type.TUTORIAL_HINT;

public class RSMPlayerG extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/help_bgi.png");
    private BlockPos bp;
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static boolean isNotEmpty(Queue<Object> queue) {
        return queue != null && !queue.isEmpty();
    }
    public RSMPlayerG(EntityPlayer player, World world, BlockPos bp) {
        super(new RSMPlayerC(player,world,bp));
        CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player,"getalljim"));
        while (true){
            EnumMap<Side, FMLEmbeddedChannel> t = ObfuscationReflectionHelper.getPrivateValue(SimpleNetworkWrapper.class,CommonProxy.INSTANCE,"channels");
            Queue<Object> inboundMessages = ObfuscationReflectionHelper.getPrivateValue(EmbeddedChannel.class,t.get(Side.CLIENT),"inboundMessages");
            Queue<Object> outboundMessages = ObfuscationReflectionHelper.getPrivateValue(EmbeddedChannel.class,t.get(Side.CLIENT),"outboundMessages");
            if (!(isNotEmpty(inboundMessages) || isNotEmpty(outboundMessages)))
                break;
        }
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
    public boolean modIndexSelected(int index)
    {
        return index == selected;
    }
    private GuiScrollingList gsl ;
    private final ArrayList<String> sl=new ArrayList<>();
    public String reply;
    public String[] fln;
    public void wait_reply(String r){
        int ti=0;
        while (true){
            ti++;
            if (reply!=null&&reply.equals(r)){
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
    }
    @Override
    public void initGui()
    {

        sl.clear();
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, guiLeft+xSize-25, guiTop+5,20, 20, "X"));
        buttonList.add(new GuiButton(1, guiLeft+10, guiTop+30,20, 20, I18n.format("gui.play")));
        buttonList.add(new GuiButton(2, guiLeft+30, guiTop+30,20, 20, I18n.format("gui.stop")));
        buttonList.add(new GuiButton(3, guiLeft+getXSize()-90, guiTop+40,40, 20, I18n.format("gui.upload")));

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
        for (GuiButton gb:buttonList){
            if(gb.id==3){
                gb.enabled= ConfigCore.isenabledUP;
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
            case 3:
                String fp = CommonProxy.fileManager.GetFilePath();
                if (fp==null){
                    Minecraft.getMinecraft().getToastGui().add(new SystemToast(TUTORIAL_HINT,new TextComponentString(I18n.format("mod.jntm.name")), new TextComponentString(I18n.format("gui.toast.nofilechoosed"))));
                    break;
                }
                String filename = JOptionPane.showInputDialog(null,I18n.format("gui.input.filename"));
                String file=readfile(fp).replaceAll(" ", "#*#*");
                byte[][] t=splitBytes(file.getBytes(),1000);
                int time = Arrays.asList(t).size();
                CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player, "pre_upload " + time));
                wait_reply("pre_upload " + time);
                for (int i=0;i<time;i++){
                    String st=new String(t[i]);
                    CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player, "upload " + i + " " + st));
                    wait_reply("upload " + i);
                }
                CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player, "end_upload"));
                wait_reply("ok");
                CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(player, "write_to_file jim "+filename));
                wait_reply("ok");
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
        }
    }
    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        fln=null;
        reply=null;
    }
    public static String readfile(String fn) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fn));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
// 删除最后一个新行分隔符
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

       return stringBuilder.toString();
    }

    /**
     * 拆分byte数组
     *
     * @param bytes
     *            要拆分的数组
     * @param size
     *            要按几个组成一份
     * @return
     */
    public byte[][] splitBytes(byte[] bytes, int size) {
        double splitLength = Double.parseDouble(size + "");
        int arrayLength = (int) Math.ceil(bytes.length / splitLength);
        byte[][] result = new byte[arrayLength][];
        int from, to;
        for (int i = 0; i < arrayLength; i++) {

            from = (int) (i * splitLength);
            to = (int) (from + splitLength);
            if (to > bytes.length)
                to = bytes.length;
            result[i] = Arrays.copyOfRange(bytes, from, to);
        }
        return result;
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
    public static int getBLength(String s) {
        return s.getBytes().length;
    }
}
