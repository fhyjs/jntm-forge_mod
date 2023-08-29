package cn.fhyjs.jntm.utility;

import cn.fhyjs.jntm.command.FormScreenBackend;
import cn.fhyjs.jntm.screen.Screen;
import cn.fhyjs.jntm.screen.ScreenM;
import com.diamondpants.spritecraft.Blueprint;
import com.diamondpants.spritecraft.Generator;
import com.diamondpants.spritecraft.MaterialSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockDrawsGenerator extends Thread{
    Map<Integer,Map<BlockPos,IBlockState>> blockToSet = new HashMap<>();
    Map<Integer,Map<BlockPos,IBlockState>> oldBlockToSet = new HashMap<>();
    Generator generator;
    Screen screen;
    public BlockDrawsGenerator(Screen screen){
        super();
        this.screen=screen;
        this.start();
    }
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        this.stop();
    }
    FormScreenBackend backend;
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            generator=new Generator();
            generator.getMaterialSet().setAll();
            generator.setMaxHeight(screen.height);
            generator.setMaxWidth(screen.width);
            // 创建Robot对象
            Robot robot = new Robot();

            // 指定要截图的区域，这里是整个屏幕
            backend = new FormScreenBackend(0,0,screen.width*50,screen.height*50);
            backend.setVisible(true);
            backend.setTitle("Screen-"+screen.getId());
            while (!isInterrupted()){
                Rectangle screenRect = backend.getBounds();
                if (FMLCommonHandler.instance().getMinecraftServerInstance()==null){
                    this.finalize();
                }
                // 通过Robot对象获取屏幕截图
                BufferedImage screenshot = robot.createScreenCapture(screenRect);
                    processImage(screenshot);
            }
            backend.dispose();
        } catch (Throwable e) {
            e.printStackTrace();
        }finally {

        }
    }
    public void processImage(BufferedImage originalImage){
        int numBlocks = 8; // 设置要分割的块数

        // 计算每个子图像的宽度和高度
        int subImageWidth = originalImage.getWidth() / numBlocks;
        int subImageHeight = originalImage.getHeight();
        List<Thread> threads = new ArrayList<>();
        // 逐个分割并保存子图像
        for (int i = 0; i < numBlocks; i++) {
            BufferedImage subImage = originalImage.getSubimage(i * subImageWidth, 0, subImageWidth, subImageHeight);
            threads.add(new Worker(subImage,i));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void generate(BufferedImage inputImage, int thn){
        if (!blockToSet.containsKey(thn)) blockToSet.put(thn,new HashMap<>());
        if (!oldBlockToSet.containsKey(thn)) oldBlockToSet.put(thn,new HashMap<>());
        Map<BlockPos, IBlockState> tbos = blockToSet.get(thn);
        Map<BlockPos, IBlockState> tobos = oldBlockToSet.get(thn);
        try {
            Blueprint blueprint = generator.run(inputImage);
            MaterialSet materialSet = blueprint.getMaterialSet();
            byte[][] usedMaterials = blueprint.getUsedMaterials();

            for(int i = 0; i < usedMaterials.length; ++i) {
                for(int j = 0; j < usedMaterials[1].length; ++j) {
                    byte materialNum = usedMaterials[i][j];
                    if (materialNum != -128) {
                        if (materialNum==18)
                            materialNum=2;
                        if (screen.facing.equals(EnumFacing.Axis.Y))
                            tbos.put(new BlockPos(i,0,j+(usedMaterials[i].length)*(thn-1)),Block.getBlockById(materialSet.getMaterial(materialNum).getBlockID()).getStateFromMeta(materialSet.getMaterial(materialNum).getBlockData()));
                        if (screen.facing.equals(EnumFacing.Axis.X))
                            tbos.put(new BlockPos(0,i,j+(usedMaterials[i].length)*(thn-1)),Block.getBlockById(materialSet.getMaterial(materialNum).getBlockID()).getStateFromMeta(materialSet.getMaterial(materialNum).getBlockData()));
                        if (screen.facing.equals(EnumFacing.Axis.Z))
                            tbos.put(new BlockPos(j+(usedMaterials[i].length)*(thn),i,0),Block.getBlockById(materialSet.getMaterial(materialNum).getBlockID()).getStateFromMeta(materialSet.getMaterial(materialNum).getBlockData()));
                    }
                }
            }
            boolean c = false;
            for (BlockPos bp : tbos.keySet()) {
                if (tobos.containsKey(bp)&&tbos.get(bp).equals(tobos.get(bp))) {
                    continue;
                }
                tobos.put(bp,tbos.get(bp));
                ScreenM.getInstance().updateScreen(screen,new Vec3i(bp.getX(),bp.getY(),bp.getZ()),tbos.get(bp));
                c=true;
            }
            if (c) {
                oldBlockToSet.put(thn,new HashMap<>(tbos));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private class Worker extends Thread{
        private final BufferedImage image;
        private final int thn;

        public Worker(BufferedImage image,int thn) {
            this.image=image;
            this.thn=thn;
        }
        @Override
        public void run() {
            generate(image,thn);
        }
    }
    public static BufferedImage flip(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }
}
