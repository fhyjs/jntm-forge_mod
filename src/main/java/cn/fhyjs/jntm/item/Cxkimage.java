package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.item.Item;
import net.minecraftforge.common.property.Properties;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;
import static net.minecraftforge.fml.client.config.GuiUtils.drawTexturedModalRect;

public class Cxkimage extends Item {
    public Cxkimage() {
        super();
        this.setRegistryName("cxkimages");
        this.setUnlocalizedName(Jntm.MODID + "cxkimages");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);

    }
}
