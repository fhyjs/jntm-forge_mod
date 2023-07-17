package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.JntmMessage;
import cn.fhyjs.jntm.network.Opt_Ply_Message;
import cn.fhyjs.jntm.registry.ItemRegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemAir;
import net.minecraft.nbt.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class LandMineConG extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/landminecondgui.png");
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    private GuiSlider ThicknessSlider;
    private GuiSlider FuseSlider;
    public LandMineConG(EntityPlayer entityPlayer, IInventory playerInventory, BlockPos blockPos, World world) {
        super(new LandMineConC(entityPlayer, playerInventory, blockPos, world));
        this.xSize = 256;
        this.ySize = 256;
        this.guiTop = (int) (sr.getScaledHeight()*0.08);
        this.guiLeft = (int) (sr.getScaledWidth()*0.21);
        this.container = (LandMineConC) inventorySlots;
    }
    LandMineConC container;
    @Override
    public void initGui()
    {
        super.initGui();
        // DEBUG
        System.out.println("Open GUI");
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0,guiLeft+207,guiTop+3,20,20,"X"));
        buttonList.add(this.ThicknessSlider = new GuiSlider(-1,guiLeft+10,guiTop+12,80,10,I18n.format("gui.jntm.landminecfg.btn.thickness")+":",I18n.format("gui.jntm.landminecfg.btn.thickness.end"),0.1,1,0.1,true,true));
        buttonList.add(new GuiButton(1,guiLeft+10,guiTop+25,80  ,20,"N/A"));
        buttonList.add(this.FuseSlider = new GuiSlider(-1,guiLeft+10,guiTop+47,80,10,I18n.format("gui.jntm.landminecfg.btn.fuse")+":","tick",0,200,1,true,true));
        buttonList.add(new GuiButton(2,guiLeft+10,guiTop+60,80  ,20,"N/A"));

    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        switch (parButton.id){
            case 0:
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
                break;
            case 1:
                coreNbt.setBoolean("Tplayer",!coreNbt.getBoolean("Tplayer"));
                for (GuiButton guiButton : buttonList) {
                    if (guiButton.id==1){
                        guiButton.displayString=I18n.format("gui.jntm.landminecfg.btn.forplayer")+":"+coreNbt.getBoolean("Tplayer");
                    }
                }
                break;
            case 2:
                coreNbt.setBoolean("Mode",!coreNbt.getBoolean("Mode"));
                for (GuiButton guiButton : buttonList) {
                    if (guiButton.id==2){
                        guiButton.displayString=I18n.format("gui.jntm.landminecfg.btn.mode."+coreNbt.getBoolean("Mode"));
                    }
                }
                break;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (coreNbt==null) return;
        coreNbt.setDouble("Thickness",ThicknessSlider.getValue());
        coreNbt.setDouble("Fuse",FuseSlider.getValue());
        if (container.LmNbt==null) return;
        CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(null,"setlandminedata "+ container.LmNbt));
    }

    boolean isPut,oisPut;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
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
        exdrawString(12,2,1,0,I18n.format("gui.jntm.landminecfg.title"));
        if (container.LmNbt != null&&container.LmNbt.hasKey("display")) {
            NBTBase list;
            if((list = container.LmNbt.getCompoundTag("display").getTag("Lore"))!=null&&list instanceof NBTTagList) {
                StringBuilder sb = new StringBuilder();
                for (int i=0;i<((NBTTagList) list).tagCount();i++){
                    sb.append(((NBTTagList) list).getStringTagAt(i));
                }
                exdrawString(130,120,1,0xac04ac,sb.toString());
            }
        }
        if (container.LandmineShot.getStack().getItem() instanceof ItemAir){
            exdrawString(130,120,1,0xff0000,I18n.format("gui.jntm.landminecfg.nolm"));
            drawRect(9,11,200,110,0xC8000000);
            isPut=false;
        }else isPut=true;
        for (GuiButton guiButton : buttonList) {
            if (guiButton.id==0) continue;
            guiButton.enabled=isPut;
        }
        if(oisPut!=isPut){
            oisPut=isPut;
            if (isPut) onPut();
        }
    }
    NBTTagCompound coreNbt;
    private void onPut() {
        if (container.LandmineShot.getStack().getItem()!= ItemRegistryHandler.ItemLandmine) return;
        if (container.LmNbt==null){
            container.LmNbt=new NBTTagCompound();
            container.LmNbt.setTag("BlockEntityTag",new NBTTagCompound());
        }
        coreNbt = container.LmNbt.getCompoundTag("BlockEntityTag");
        if (coreNbt.hasKey("Thickness")){
            ThicknessSlider.setValue(coreNbt.getDouble("Thickness"));
            ThicknessSlider.updateSlider();
        }
        if (!coreNbt.hasKey("Tplayer")) {
            coreNbt.setBoolean("Tplayer",false);
        }
        for (GuiButton guiButton : buttonList) {
            if (guiButton.id==1){
                guiButton.displayString=I18n.format("gui.jntm.landminecfg.btn.forplayer")+":"+coreNbt.getBoolean("Tplayer");
            }
        }
        if (coreNbt.hasKey("Fuse")){
            FuseSlider.setValue(coreNbt.getDouble("Fuse"));
            FuseSlider.updateSlider();
        }
        if (!coreNbt.hasKey("Mode")) {
            coreNbt.setBoolean("Mode",false);
        }
        for (GuiButton guiButton : buttonList) {
            if (guiButton.id==2){
                guiButton.displayString=I18n.format("gui.jntm.landminecfg.btn.mode."+coreNbt.getBoolean("Mode"));
            }
        }
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);
        onPut();
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        super.renderHoveredToolTip(x, y);
        if (x>=FuseSlider.x&&x<=FuseSlider.x+FuseSlider.width&&y>=FuseSlider.y&&y<=FuseSlider.y+FuseSlider.height&&FuseSlider.enabled)
            this.drawHoveringText(String.format("(~%.2fs)",FuseSlider.getValue()*0.05), x, (int) (y-FuseSlider.height*.5));
    }

    public void exdrawString(int x, int y, float size, int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
}
