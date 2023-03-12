package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityCxkImage;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;

import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.client.renderer.texture.DynamicTexture;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import net.minecraft.entity.Entity;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;


import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class RenderCxkImageTileEntity extends TileEntitySpecialRenderer<TileEntityCxkImage> {
    private ResourceLocation WITHER_SKELETON_TEXTURES;
    private BufferedImage bfi;
    private String url,uro="1145141919810";

    {
        try {
            bfi=ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png")));
        } catch (IOException e) {
            Jntm.logger.error(e.fillInStackTrace());
        }
    }

    private ModelBase skeletonHead ;

    public void setdul(String i){
        url=i;
    }
    @Override
    public void render(TileEntityCxkImage te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing enumfacing = EnumFacing.getFront(te.getBlockMetadata() & 7);
        url=te.getCount();


        if (!Objects.equals(url, uro)){
            skeletonHead = new  ModelTECiI(0, 0, 16, 16);
            uro=url;
            if (WITHER_SKELETON_TEXTURES!=null) {
                Minecraft.getMinecraft().getTextureManager().deleteTexture(WITHER_SKELETON_TEXTURES);
            }
            try {
                if (url!=null&&uro!="not_set"){
                    WITHER_SKELETON_TEXTURES = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages", new DynamicTexture(DBI.DBI(uro)));
                    if(WITHER_SKELETON_TEXTURES==null){
                        WITHER_SKELETON_TEXTURES = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages", new DynamicTexture(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png")))));
                    }
                }else {
                    WITHER_SKELETON_TEXTURES = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages", new DynamicTexture(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png")))));
                }
            } catch (IOException e) {
                Jntm.logger.error(e.fillInStackTrace());
                try {
                    WITHER_SKELETON_TEXTURES = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages", new DynamicTexture(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png")))));
                } catch (IOException ex) {
                    Jntm.logger.error(ex.fillInStackTrace());
                }
            }
        }

        this.renderSkull((float)x, (float)y, (float)z, enumfacing, (float)(1 * 360) / 16.0F, 1, null, destroyStage, 0);
    }
    public void renderSkull(float x, float y, float z, EnumFacing facing, float rotationIn, int skullType, @Nullable GameProfile profile, int destroyStage, float animateTicks) {
        ModelBase modelbase = this.skeletonHead;

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.bindTexture(WITHER_SKELETON_TEXTURES);
        if (facing == EnumFacing.UP) {
            GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        } else {
            switch (facing) {
                case NORTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.74F);
                    break;
                case SOUTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.26F);
                    rotationIn = 180.0F;
                    break;
                case WEST:
                    GlStateManager.translate(x + 0.74F, y + 0.25F, z + 0.5F);
                    rotationIn = 270.0F;
                    break;
                case EAST:
                default:
                    GlStateManager.translate(x + 0.26F, y + 0.25F, z + 0.5F);
                    rotationIn = 90.0F;
            }
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();

        modelbase.render((Entity) null, animateTicks, 0.0F, 0.0F, rotationIn, 0.0F, 0.0625F);
        GlStateManager.popMatrix();

    }
}
class DBI{
    // Java读取网络图片 BufferedImage
    public static BufferedImage DBI(String u) throws IOException {
        if(u=="not_set"){
            return null;
        }
        BufferedImage image = null;
            URL url = new URL(u);
            image = ImageIO.read(url);
        return image;
    }
}
