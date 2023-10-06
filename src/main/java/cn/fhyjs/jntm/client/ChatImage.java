package cn.fhyjs.jntm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ChatImage {
    private final static Map<Object, ResourceLocation> bufferedImagesRl = new HashMap<>();
    public List<String> information;
    public int height,width;
    public ImageStatus status;
    public ResourceLocation image;
    public ChatImage(){
        status=ImageStatus.NEW;
    }
    public void getImage(URL url){
        status=ImageStatus.WAITING;
        if (bufferedImagesRl.containsKey(url)){
            image = bufferedImagesRl.get(url);
            status=ImageStatus.OK;
        }
        new ImageGetter(url).start();
    }
    public static void clearCache(){
        bufferedImagesRl.clear();
    }
    private class ImageGetter extends Thread{
        URL url=null;
        public ImageGetter(URL url){
            this.url=url;
        }
        @Override
        public void run() {
            try {
                if (url!=null) {
                    BufferedImage bi = ImageIO.read(url);
                    writeImage(bi);
                }
            } catch (Exception e) {
                e.printStackTrace();
                status=ImageStatus.ERROR;
            }
        }
        private void writeImage(BufferedImage bi) throws InterruptedException {
            if (bi != null) {
                bi=scaleImage(bi,width,height);
                AtomicReference<ResourceLocation> rl1 = new AtomicReference<>();
                BufferedImage finalBi = bi;
                Runnable task = () -> rl1.set(Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("chat_image_" + bufferedImagesRl.size(), new DynamicTexture(Objects.requireNonNull(finalBi))));
                IThreadListener mainThread = Minecraft.getMinecraft();
                mainThread.addScheduledTask(task);
                while (rl1.get()==null) {
                    Thread.sleep(10);
                }
                ResourceLocation rl = rl1.get();
                bufferedImagesRl.put(url, rl);
                image = rl;
                status = ImageStatus.OK;
            } else {
                status = ImageStatus.ERROR;
            }
        }
        public  BufferedImage scaleImage(BufferedImage originalImage, int newWidth, int newHeight) {
            // 创建一个新的 BufferedImage，用于存储缩放后的图像
            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

            // 获取图形上下文对象
            Graphics2D g2d = scaledImage.createGraphics();

            // 使用抗锯齿渲染
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // 创建仿射变换对象并进行非等比例缩放
            double scaleX = (double)newWidth / originalImage.getWidth();
            double scaleY = (double)newHeight / originalImage.getHeight();
            AffineTransform transform = AffineTransform.getScaleInstance(scaleX, scaleY);
            g2d.drawImage(originalImage, transform, null);

            // 释放图形上下文资源
            g2d.dispose();

            return scaledImage;
        }
    }
    public enum ImageStatus{
        OK,
        ERROR,
        WAITING,
        NEW
    }
}
