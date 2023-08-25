package cn.fhyjs.jntm.utility;

import cn.fhyjs.jntm.screen.Screen;
import cn.fhyjs.jntm.screen.ScreenM;
import com.diamondpants.spritecraft.Blueprint;
import com.diamondpants.spritecraft.Generator;
import com.diamondpants.spritecraft.MaterialSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
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
    public void run() {
        try {
            Thread.sleep(1000);
            generator=new Generator();
            generator.getMaterialSet().setAll();
            generator.setMaxHeight(screen.height);
            generator.setMaxWidth(screen.width);
            generate(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("textures/gui/title/mojang.png"))),1);

            generate(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("jntm:textures/gui/mojang.png"))),1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
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

            for(int i = usedMaterials.length - 1; i >= 0; --i) {
                for(int j = usedMaterials[i].length - 1; j >= 0; --j) {
                    byte materialNum = usedMaterials[i][j];
                    if (materialNum != -128) {
                        if (materialNum==18)
                            materialNum=2;
                        tbos.put(new BlockPos(i,0,j+(usedMaterials[i].length)*thn),Block.getBlockById(materialSet.getMaterial(materialNum).getBlockID()).getStateFromMeta(materialSet.getMaterial(materialNum).getBlockData()));
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
}
