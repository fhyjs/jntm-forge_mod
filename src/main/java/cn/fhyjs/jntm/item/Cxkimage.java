package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.renderer.IItemWithMeshDefinition;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;
import static net.minecraftforge.fml.client.config.GuiUtils.drawTexturedModalRect;

public class Cxkimage extends Item implements IItemWithMeshDefinition {
    public Cxkimage() {
        super();
        this.setRegistryName("cxkimages");
        this.setUnlocalizedName(Jntm.MODID + "cxkimages");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return stack -> {
            if(stack.hasTagCompound()) {
                NBTTagCompound nbt = stack.getTagCompound();
                if(nbt.hasKey("expindustry:item_mold")) {
                    NBTTagCompound itemTags = nbt.getCompoundTag("expindustry:item_mold");
                    ItemStack result = new ItemStack(itemTags);
                    String imprintName = result.getUnlocalizedName();
                    String domain = "expindustry";
                    if(nbt.hasKey("expindustry:resourceDomain")) {
                        domain = nbt.getString("expindustry:resourceDomain");
                    }
                    if(imprintName.contains(":")) {
                        imprintName = imprintName.substring(imprintName.indexOf(':')+1);
                    }
                    imprintName = imprintName.replaceAll("tile.", "");
                    imprintName = imprintName.replaceAll("item.", "");
                    imprintName = imprintName.replaceAll("[Ii]ron", "");
                    ResourceLocation loc = new ResourceLocation(domain, "mold_"+imprintName);
                    ModelResourceLocation fullModelLocation = new ModelResourceLocation(loc, "inventory");
                    return fullModelLocation;
                }
            }
            return new ModelResourceLocation(new ResourceLocation("expindustry", "mold_clean"), "inventory");
        };
    }
}
