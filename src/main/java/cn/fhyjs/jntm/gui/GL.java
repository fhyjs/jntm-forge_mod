package cn.fhyjs.jntm.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.client.GuiSlotModList;
import net.minecraftforge.fml.common.ModContainer;

import java.util.ArrayList;

public class GL<T extends RSMPlayerG> extends GuiScrollingList {
    private final T parent;
    private final ArrayList<String> mods;
    public GL(T parent, ArrayList<String> mods, int listWidth, int slotHeight) {
        super(parent.mc,listWidth,parent.height, 90, parent.getGuiTop()+parent.getYSize()-10, parent.getGuiLeft()+20, slotHeight, parent.width, parent.height);
        this.parent = parent;
        this.mods = mods;
    }

    @Override
    protected int getSize() {
        return mods.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        ((RSMPlayerG)this.parent).selectModIndex(index);
    }

    @Override
    protected boolean isSelected(int index) {
        return ((RSMPlayerG)this.parent).modIndexSelected(index);
    }

    @Override
    protected void drawBackground() {
    }
    @Override
    protected int getContentHeight()
    {
        return (this.getSize()) * 35 + 1;
    }
    @Override
    protected void drawSlot(int idx, int right, int top, int height, Tessellator tess){
        FontRenderer font     = ((RSMPlayerG)this.parent).getFontRenderer();
        if (mods.get(idx)==null){
            mods.remove(idx);
            return;
        }
        font.drawString(font.trimStringToWidth(mods.get(idx),    listWidth - 10), this.left + 3 , top +  2, 0xFFFFFF);
    }
}
