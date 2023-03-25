package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CIIRender extends TileEntityItemStackRenderer {
    static final ResourceLocation SHIELD_TEXTURE = new ResourceLocation(Jntm.MODID,"");
    private final ModelShield model_Shield = new CIIModel();
    @Override
    public void renderByItem(ItemStack itemStackIn,float f) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(SHIELD_TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        this.model_Shield.render();
        GlStateManager.popMatrix();
    }
}
