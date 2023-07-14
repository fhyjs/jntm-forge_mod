package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.block.BlockLandmine;
import cn.fhyjs.jntm.block.TELandmine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import scala.tools.reflect.FastTrack;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BLRender extends TileEntitySpecialRenderer<TELandmine> {
    @Override
    public void render(TELandmine te, double x, double y, double z, float partialTicks, int destroyStage, float partial)
    {
        /*
        if (!te.HasCPos){
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GL11.glTranslated(x, y, z);
            bindTexture(new ResourceLocation("jntm:textures/block/landmine.png"));
            new ModelTEL(0,0,16,16, (int) (te.getThickness()*16)).render((Entity) null, 0, 0.0F, 0.0F, 0, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
            return;
        }
         */
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        renderTileEntityFast(te, x, y, z, partialTicks, destroyStage, partial, buffer);
        buffer.setTranslation(0, 0, 0);

        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void renderTileEntityFast(TELandmine te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        Class<?> VLClass = null;
        for (Class<?> declaredClass : ModelLoader.class.getDeclaredClasses()) {
            if (declaredClass.getName().contains("VanillaLoader")) VLClass=declaredClass;
        }
        ModelLoader MLI = null;
        try {
            Method m = (VLClass.getDeclaredMethod("getLoader"));
            m.setAccessible(true);
            MLI = (ModelLoader) m.invoke(VLClass.getField("INSTANCE").get(null));
            //System.out.println(MLI);
        } catch (Throwable e) {
            Jntm.logger.error(e);
        }
        BlockModelShapes BMS = ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class,MLI,"blockModelShapes");
        BlockPos bp = te.CamouflagePos;
        boolean HCP = te.HasCPos&&bp!=null;
        IBakedModel modelBaked = null;
        if (HCP)
            modelBaked = ((RegistrySimple<ModelResourceLocation, IBakedModel>) ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class, MLI, "bakedRegistry")).getObject(BMS.getBlockStateMapper().getVariants(getWorld().getBlockState(bp).getBlock()).get(getWorld().getBlockState(bp)));
        else {
            //modelBaked = ((RegistrySimple<ModelResourceLocation, IBakedModel>) ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class, MLI, "bakedRegistry")).getObject(BMS.getBlockStateMapper().getVariants(Blocks.WOODEN_PRESSURE_PLATE).get(BMS.getBlockStateMapper().getVariants(Blocks.WOODEN_PRESSURE_PLATE).values().toArray()[(int) (Math.random() * BMS.getBlockStateMapper().getVariants(Blocks.WOODEN_PRESSURE_PLATE).size())]));
            String rn = "landmine_h"+(te.getThickness()*16);
            Map<String, String> BUILT_IN_MODELS = ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class,null,"field_177600_d");
            if (!BUILT_IN_MODELS.containsKey(rn)) {
                try {
                    BUILT_IN_MODELS.put(rn,cn.fhyjs.jntm.common.pstest.readLine(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("jntm:models/block/landmine.json"))).replaceAll("\"\\{height}\"",String.valueOf((te.getThickness()*16))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            IModel model = loadModel(VLClass,new ResourceLocation("jntm:models/block/landmine_test"));
            model = loadModel(VLClass,new ResourceLocation("minecraft:builtin/"+rn));
            //model.asVanillaModel().get().name="jntm:models/block/landmine";
            modelBaked=model.bake(model.getDefaultState(),DefaultVertexFormats.BLOCK, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        GlStateManager.enableRescaleNormal();
        //GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        buffer.setTranslation(x - te.getPos().getX(), y - te.getPos().getY()+0.001, z - te.getPos().getZ());

        if (HCP&&modelBaked!=null)
            Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(te.getWorld(),modelBaked,te.getWorld().getBlockState(bp),te.getPos(), buffer,false);
        else if (modelBaked != null)
            Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(te.getWorld(),modelBaked,te.getWorld().getBlockState(te.getPos()).getBlock().getDefaultState().withProperty(BlockLandmine.NOTUSE,true),te.getPos(), buffer,false);


        //ModelBase model = new ModelTEL(0, 0, 16, 16,te,bq);
        //model.render(null, 0, 0.0F, 0.0F, 0, 0.0F, 0.0625F);
        //GlStateManager.scale(0.1,0.1,0.1);
        GlStateManager.popMatrix();
    }
    private IModel loadModel(Class<?> VLClass, ResourceLocation rl){
        try {
            return (IModel) ((VLClass.getField("INSTANCE").get(null)).getClass().getMethod("loadModel",ResourceLocation.class).invoke((VLClass.getField("INSTANCE").get(null)),rl));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            throw new ReportedException(CrashReport.makeCrashReport(e,"Jntm Occurrence an error in loading model"));
        }
    }
}
