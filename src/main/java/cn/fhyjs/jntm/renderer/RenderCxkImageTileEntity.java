package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.TileEntityCxkImage;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.network.SCINMessage;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Objects;

import static net.minecraft.client.gui.toasts.SystemToast.Type.TUTORIAL_HINT;

public class RenderCxkImageTileEntity extends TileEntitySpecialRenderer<TileEntityCxkImage> {
    private BufferedImage bfi;
    public static final Hashtable<TileEntityCxkImage,String> map=new Hashtable<>(),mapo=new Hashtable<>();
    public static final Hashtable<TileEntityCxkImage,ResourceLocation> rlm=new Hashtable<>();

    {
        try {
            bfi=ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png")));
        } catch (IOException e) {
            Jntm.logger.error(e.fillInStackTrace());
        }
    }

    private ModelBase skeletonHead ;
    private BlockPos bp;
    @Override
    public void render(TileEntityCxkImage te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumFacing enumfacing = EnumFacing.getFront(te.getBlockMetadata() & 7);
        map.put(te,te.getCount());

        if (!Objects.equals(map.get(te), mapo.get(te))){
            skeletonHead = new  ModelTECiI(0, 0, 16, 16);
            mapo.put(te,map.get(te));
            if (rlm.get(te)!=null) {
                Minecraft.getMinecraft().getTextureManager().deleteTexture(rlm.get(te));
            }
            try {
                if (map.get(te)!=null&& !Objects.equals(mapo.get(te), "")){
                        rlm.put(te,Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages", new DynamicTexture(Objects.requireNonNull(DBI.DBI(mapo.get(te))))));
                    if(rlm.get(te)==null){
                        rlm.put(te,Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages", new DynamicTexture(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png"))))));
                    }
                }else {
                    rlm.put(te, Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages", new DynamicTexture(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png"))))));
                }
            } catch (IOException e) {
                bp=te.getPos();
                CommonProxy.INSTANCE.sendToServer(new SCINMessage( "{\"x\":"+bp.getX()+", \"y\":"+bp.getY()+",\"z\":"+bp.getZ()+",\"url\":\""+""+"\"}"));
                Jntm.logger.error(e.fillInStackTrace());
                Minecraft.getMinecraft().getToastGui().add(new SystemToast(TUTORIAL_HINT,new TextComponentString(I18n.format("mod.jntm.name")), new TextComponentString(I18n.format("gui.toast.RError"))));
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§5"+I18n.format("gui.toast.RError")+"§f:"+e));
                try {
                    rlm.put(te, Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cxkimages", new DynamicTexture(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png"))))));
                } catch (IOException ex) {
                    Jntm.logger.error(ex.fillInStackTrace());
                }
            }
        }

        this.renderSkull((float)x, (float)y, (float)z, enumfacing, (float)(360) / 16.0F, 1, null, destroyStage, 0,rlm.get(te));
    }
    public void renderSkull(float x, float y, float z, EnumFacing facing, float rotationIn, int skullType, @Nullable GameProfile profile, int destroyStage, float animateTicks,ResourceLocation rl) {
        ModelBase modelbase = this.skeletonHead;

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.bindTexture(rl);
        if (facing == EnumFacing.UP) {
            GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        } else {
            switch (facing) {
                case NORTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z +0.25);
                    rotationIn=0;
                    break;
                case SOUTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + .72F);
                    rotationIn = 180.0F;
                    break;
                case WEST:
                    GlStateManager.translate(x + .29F, y + 0.25F, z + 0.5F);
                    rotationIn = 270.0F;
                    break;
                case EAST:
                default:
                    GlStateManager.translate(x + 0.72F, y + 0.25F, z + 0.5F);
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
