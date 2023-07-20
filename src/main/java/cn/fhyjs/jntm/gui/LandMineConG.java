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
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
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

import static cn.fhyjs.jntm.Jntm.proxy;

public class LandMineConG extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Jntm.MODID, "textures/gui/landminecondgui.png");
    private EntityPlayer player;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    private GuiSlider ThicknessSlider;
    private GuiSlider FuseSlider,ExplosionStrengthSlider;
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
        buttonList.add(this.ExplosionStrengthSlider = new GuiSlider(-1,guiLeft+10,guiTop+82,80,10,I18n.format("gui.jntm.landminecfg.btn.Explosionstrength")+":","",0,16,0,true,true));


    }
    @Override
    protected void actionPerformed(GuiButton parButton) throws IOException {
        switch (parButton.id){
            case 0:
                if (isPut)
                    mouseReleased(0,0,114514);
                else
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
    protected void mouseReleased(int mouseX, int mouseY, int state) {//114514: Save & Exit
        if (state!=114514)
            super.mouseReleased(mouseX, mouseY, state);
        if (coreNbt==null||!isPut) return;
        coreNbt.setDouble("Thickness",ThicknessSlider.getValue());
        coreNbt.setDouble("Fuse",FuseSlider.getValue());
        coreNbt.setFloat("Explosion_Strength", (float) ExplosionStrengthSlider.getValue());
        coreNbt.setBoolean("Explosion",false);
        coreNbt.setBoolean("Broadcast",false);
        coreNbt.setBoolean("HasCP", false);
        for (int i = 0; i < container.plugins.size(); i++) {
            ItemStack itemStack = container.plugins.get(i);
            if (itemStack.getItem()==ItemRegistryHandler.watcherUpgrade)
                coreNbt.setBoolean("Broadcast",true);
            if (itemStack.getItem()==ItemRegistryHandler.explosionUpgrade)
                coreNbt.setBoolean("Explosion",true);
            if (itemStack.getItem()==ItemRegistryHandler.camouflageUpgrade) {
                coreNbt.setBoolean("HasCP", true);
                coreNbt.setIntArray("CamouflagePos",itemStack.getTagCompound().getIntArray("pos"));
            }
        }
        if (container.LmNbt==null) {
            proxy.showToase(SystemToast.Type.TUTORIAL_HINT.name(),"mod.jntm.name","gui.jntm.landminecfg.error");
            return;
        }
        container.LmNbt.setTag("BlockEntityTag",coreNbt);

        CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(null,(state!=114514?"setlandminedata ":"setlandminedataexit ")+ container.LmNbt));
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        if (mc.gameSettings.keyBindInventory.isActiveAndMatches(i)||i==1){
            if (isPut)
                mouseReleased(0,0,114514);
            else
                CommonProxy.INSTANCE.sendToServer(new JntmMessage(0));
            return;
        }
        super.handleKeyboardInput();
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
        exdrawString(180,30,1,0,I18n.format("gui.jntm.landminecfg.plugin.tip"));
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
            if (guiButton == ExplosionStrengthSlider) continue;
            guiButton.enabled=isPut;
        }
        if(oisPut!=isPut){
            oisPut=isPut;
            if (isPut) onPut();
            if (!isPut) onPick();
        }
        ExplosionStrengthSlider.enabled = (coreNbt!=null&&isPut&&coreNbt.hasKey("Explosion")&&coreNbt.getBoolean("Explosion"));
    }

    private void onPick() {
        container.plugins.clear();
        initGui();
    }

    NBTTagCompound coreNbt;
    private void onPut() {
        if (container.LandmineShot.getStack().getItem()!= ItemRegistryHandler.ItemLandmine) return;
        container.clearplugin();
        container.onPut();
        //CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(null,"clearlandminepulgin"));
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
        if (coreNbt.hasKey("Explosion_Strength")){
            ExplosionStrengthSlider.setValue(coreNbt.getDouble("Explosion_Strength"));
            ExplosionStrengthSlider.updateSlider();
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
        if (x>=ExplosionStrengthSlider.x&&x<=ExplosionStrengthSlider.x+ExplosionStrengthSlider.width&&y>=ExplosionStrengthSlider.y&&y<=ExplosionStrengthSlider.y+ExplosionStrengthSlider.height)
            if (ExplosionStrengthSlider.enabled)
                this.drawHoveringText(String.format("(~%.2f TNT)",ExplosionStrengthSlider.getValue()/4), x, (int) (y-ExplosionStrengthSlider.height*.5));
            else if (isPut)
                this.drawHoveringText(I18n.format("gui.jntm.landminecfg.ess.tooltip"), x, (int) (y-ExplosionStrengthSlider.height*.5));
    }

    public void exdrawString(int x, int y, float size, int color, String str){
        GL11.glPushMatrix(); //Start new matrix
        GL11.glScalef(size,size,size); //scale it to 0.5 size on each side. Must be float e.g.: 2.0F
        this.fontRenderer.drawString(str, x, y, color); //fr - fontRenderer
        GL11.glPopMatrix(); //End this matrix
    }
}
