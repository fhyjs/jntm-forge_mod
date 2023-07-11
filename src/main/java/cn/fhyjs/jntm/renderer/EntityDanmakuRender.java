package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.Ta_Danmaku;
import cn.fhyjs.jntm.entity.danmaku.DanmakuColor;
import cn.fhyjs.jntm.entity.danmaku.DanmakuType;
import cn.fhyjs.jntm.utility.RenderHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class EntityDanmakuRender extends Render<Ta_Danmaku> {
    public static final Factory FACTORY = new Factory();
    private static final ResourceLocation SINGLE_PLANE_DANMAKU = new ResourceLocation(Jntm.MODID, "textures/entity/singe_plane_danmaku.png");
    private static final ResourceLocation AMULET_DANMAKU = new ResourceLocation(Jntm.MODID, "textures/entity/amulet_danmaku.png");
    private static final ResourceLocation STAR_DANMAKU = new ResourceLocation(Jntm.MODID, "textures/entity/star_danmaku.png");
    private static final ResourceLocation PETAL_DANMAKU = new ResourceLocation(Jntm.MODID, "models/entity/petal.obj");
    private static final ResourceLocation KNIFE_TOP_DANMAKU = new ResourceLocation(Jntm.MODID, "models/entity/knife_top.obj");
    private static final ResourceLocation KNIFE_BOTTOM_DANMAKU = new ResourceLocation(Jntm.MODID, "models/entity/knife_bottom.obj");
    private static final ResourceLocation BULLET_DANMAKU = new ResourceLocation(Jntm.MODID, "models/entity/bullet_danmaku.obj");
    private static final ResourceLocation RING = new ResourceLocation(Jntm.MODID, "models/entity/ring.obj");
    private static final ResourceLocation GOSSIP = new ResourceLocation(Jntm.MODID, "models/entity/gossip.obj");
    private static final ResourceLocation KUNAI_DANMAKU = new ResourceLocation(Jntm.MODID, "models/entity/kunai.obj");
    private static final ResourceLocation RAINDROP_DANMAKU = new ResourceLocation(Jntm.MODID, "models/entity/raindrop.obj");
    private static final ResourceLocation ARROWHEAD_DANMAKU = new ResourceLocation(Jntm.MODID, "models/entity/arrowhead.obj");
    private static final ResourceLocation BUTTERFLY_DANMAKU = new ResourceLocation(Jntm.MODID, "models/entity/butterfly.obj");
    private static final ResourceLocation GLOWEY_BALL_DANMAKU = new ResourceLocation(Jntm.MODID, "textures/entity/glowey_ball.png");
    private static final ResourceLocation BASKETBALL_DANMAKU = new ResourceLocation(Jntm.MODID, "textures/entity/basketball.png");
    private static List<BakedQuad> PETAL_QUADS;
    private static List<BakedQuad> KNIFE_TOP_QUADS;
    private static List<BakedQuad> KNIFE_BOTTOM_QUADS;
    private static List<BakedQuad> BULLET_QUADS;
    private static List<BakedQuad> RING_QUADS;
    private static List<BakedQuad> GOSSIP_QUADS;
    private static List<BakedQuad> KUNAI_QUADS;
    private static List<BakedQuad> RAINDROP_QUADS;
    private static List<BakedQuad> ARROWHEAD_QUADS;
    private static List<BakedQuad> BUTTERFLY_QUADS;

    public EntityDanmakuRender(RenderManager renderManagerIn) {
        super(renderManagerIn);
        PETAL_QUADS = getObjQuads(PETAL_DANMAKU);
        KNIFE_TOP_QUADS = getObjQuads(KNIFE_TOP_DANMAKU);
        KNIFE_BOTTOM_QUADS = getObjQuads(KNIFE_BOTTOM_DANMAKU);
        BULLET_QUADS = getObjQuads(BULLET_DANMAKU);
        RING_QUADS = getObjQuads(RING);
        GOSSIP_QUADS = getObjQuads(GOSSIP);
        KUNAI_QUADS = getObjQuads(KUNAI_DANMAKU);
        RAINDROP_QUADS = getObjQuads(RAINDROP_DANMAKU);
        ARROWHEAD_QUADS = getObjQuads(ARROWHEAD_DANMAKU);
        BUTTERFLY_QUADS = getObjQuads(BUTTERFLY_DANMAKU);
    }

    private static List<BakedQuad> getObjQuads(ResourceLocation obj) {
        try {
            IModel model = OBJLoader.INSTANCE.loadModel(obj);
            IBakedModel bakedModel = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
            return bakedModel.getQuads(null, null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    @Override
    public boolean isMultipass() {
        return super.isMultipass();
    }

    @Override
    public boolean shouldRender(Ta_Danmaku danmaku, @Nonnull ICamera camera, double camX, double camY, double camZ) {
        if (danmaku.getType() == DanmakuType.MASTER_SPARK) {
            return true;
        }
        return super.shouldRender(danmaku, camera, camX, camY, camZ);
    }

    @Override
    public void doRender(@Nonnull Ta_Danmaku entity, double x, double y, double z, float entityYaw, float partialTicks) {
        DanmakuType type = entity.getType();
        switch (type) {
            case PELLET:
            case BALL:
            case ORBS:
            case BIG_BALL:
            case BUBBLE:
            case HEART:
                renderSinglePlaneDanmaku(entity, x, y, z, type);
                break;
            case BASKETBALL:
                renderSinglePlaneDanmakuEx(entity, x, y, z, type.getSize(),BASKETBALL_DANMAKU,32,32,32,0,0);
                break;
            case JELLYBEAN:
                renderJellyBeanDanmaku(entity, x, y, z);
                break;
            case AMULET:
                renderAmuletDanmaku(entity, x, y, z);
                break;
            case STAR:
            case BIG_STAR:
                renderStarDanmaku(entity, x, y, z, partialTicks);
                break;
            case PETAL:
                renderPetalDanmaku(entity, x, y, z);
                break;
            case KNIFE:
                renderKnifeDanmaku(entity, x, y, z);
                break;
            case MASTER_SPARK:
                renderMasterSparkDanmaku(entity, x, y, z, partialTicks);
                break;
            case BULLET:
                renderObjDanmaku(entity, x, y, z, BULLET_QUADS, 0.2f, 0.25f, 180);
                break;
            case KUNAI:
                renderObjDanmaku(entity, x, y, z, KUNAI_QUADS, 0.15f, 0.2f, 0);
                break;
            case RAINDROP:
                renderObjDanmaku(entity, x, y, z, RAINDROP_QUADS, 0.25f, 0.3f, 0);
                break;
            case ARROWHEAD:
                renderObjDanmaku(entity, x, y, z, ARROWHEAD_QUADS, 0.2f, 0.25f, 0);
                break;
            case BUTTERFLY:
                renderButterflyDanmaku(entity, x, y, z, partialTicks);
                break;
            case GLOWEY_BALL:
                renderGloweyBallDanmaku(entity, x, y, z, type);
                break;
            default:
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull Ta_Danmaku entity) {
        return SINGLE_PLANE_DANMAKU;
    }
    private void renderSinglePlaneDanmakuEx(Ta_Danmaku entity, double x, double y, double z, double s,ResourceLocation RL,double w,double l,double is,double spu,double spv) {

        // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX,
                1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufBuilder = tessellator.getBuffer();

        bufBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        this.renderManager.renderEngine.bindTexture(RL);

        bufBuilder.pos(-s, s, 0).tex((spu + 0) / w, (spv + 0) / l).endVertex();
        bufBuilder.pos(-s, -s, 0).tex((spu + 0) / w, (spv + is) / l).endVertex();
        bufBuilder.pos(s, -s, 0).tex((spu + is) / w, (spv + is) / l).endVertex();
        bufBuilder.pos(s, s, 0).tex((spu + is) / w, (spv + 0) / l).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    private void renderSinglePlaneDanmaku(Ta_Danmaku entity, double x, double y, double z, DanmakuType type) {
        // 获取相关数据
        int color = entity.getColor();
        // 材质宽度
        int w = 416;
        // 材质长度
        int l = 192;

        // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）
        double pStartU = 32 * color;
        double pStartV = 32 * type.ordinal();

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX,
                1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufBuilder = tessellator.getBuffer();

        bufBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        this.renderManager.renderEngine.bindTexture(SINGLE_PLANE_DANMAKU);

        bufBuilder.pos(-type.getSize(), type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 0) / l).endVertex();
        bufBuilder.pos(-type.getSize(), -type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 32) / l).endVertex();
        bufBuilder.pos(type.getSize(), -type.getSize(), 0).tex((pStartU + 32) / w, (pStartV + 32) / l).endVertex();
        bufBuilder.pos(type.getSize(), type.getSize(), 0).tex((pStartU + 32) / w, (pStartV + 0) / l).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    private void renderJellyBeanDanmaku(Ta_Danmaku entity, double x, double y, double z) {
        int color = entity.getColor();
        float yaw = entity.rotationYaw + 90;
        float pitch = -entity.rotationPitch;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        {
            GlStateManager.translate(x, y + 0.3, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch, 0, 0, 1);
            GlStateManager.scale(0.5f, 0.25f, 0.25f);
            GlStateManager.color(255, 255, 255);
            GlStateManager.callList(RenderHelper.SPHERE_OUT_INDEX);
            GlStateManager.callList(RenderHelper.SPHERE_IN_INDEX);
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y + 0.3, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch, 0, 0, 1);
            GlStateManager.scale(0.6f, 0.3f, 0.3f);
            GlStateManager.color(DanmakuColor.getFloatRed(color), DanmakuColor.getFloatGreen(color), DanmakuColor.getFloatBlue(color));
            GlStateManager.callList(RenderHelper.SPHERE_OUT_INDEX);
            GlStateManager.callList(RenderHelper.SPHERE_IN_INDEX);
        }
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderAmuletDanmaku(Ta_Danmaku entity, double x, double y, double z) {
        DanmakuType type = DanmakuType.AMULET;
        int color = entity.getColor();
        float yaw = entity.rotationYaw;
        float pitch = 90 - entity.rotationPitch;
        // 材质宽度
        int w = 416;
        double pStartU = 32 * color;

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y + 0.12, z);
        GlStateManager.rotate(yaw, 0, 1, 0);
        GlStateManager.rotate(pitch, 1, 0, 0);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufBuilder = tessellator.getBuffer();
        this.renderManager.renderEngine.bindTexture(AMULET_DANMAKU);

        bufBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufBuilder.pos(-type.getSize(), type.getSize(), 0).tex((pStartU + 0) / w, 0).endVertex();
        bufBuilder.pos(-type.getSize(), -type.getSize(), 0).tex((pStartU + 0) / w, 1).endVertex();
        bufBuilder.pos(type.getSize(), -type.getSize(), 0).tex((pStartU + 32) / w, 1).endVertex();
        bufBuilder.pos(type.getSize(), type.getSize(), 0).tex((pStartU + 32) / w, 0).endVertex();
        tessellator.draw();

        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    private void renderStarDanmaku(Ta_Danmaku entity, double x, double y, double z, float partialTicks) {
        // 获取相关数据
        int color = entity.getColor();
        DanmakuType type = entity.getType();
        // 材质宽度
        int w = 416;
        // 材质长度
        int l = 64;

        // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）
        double pStartU = 32 * color;
        double pStartV = 32 * (type == DanmakuType.BIG_STAR ? 0 : 1);

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX,
                1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((entity.ticksExisted + partialTicks) * 5, 0, 0, 1);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufBuilder = tessellator.getBuffer();

        bufBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        this.renderManager.renderEngine.bindTexture(STAR_DANMAKU);

        bufBuilder.pos(-type.getSize(), type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 0) / l).endVertex();
        bufBuilder.pos(-type.getSize(), -type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 32) / l).endVertex();
        bufBuilder.pos(type.getSize(), -type.getSize(), 0).tex((pStartU + 32) / w, (pStartV + 32) / l).endVertex();
        bufBuilder.pos(type.getSize(), type.getSize(), 0).tex((pStartU + 32) / w, (pStartV + 0) / l).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    private void renderPetalDanmaku(Ta_Danmaku entity, double x, double y, double z) {
        int color = entity.getColor();
        float yaw = entity.rotationYaw - 90;
        float pitch = entity.rotationPitch;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buff = tessellator.getBuffer();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch, 0, 0, 1);
            GlStateManager.scale(0.1f, 0.1f, 0.1f);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : PETAL_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0xffffffff);
            }
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y - 0.02, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch, 0, 0, 1);
            GlStateManager.scale(0.12f, 0.12f, 0.12f);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : PETAL_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0xaa_00_00_00 + color);
            }
            tessellator.draw();
        }

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderKnifeDanmaku(Ta_Danmaku entity, double x, double y, double z) {
        int color = entity.getColor();
        float yaw = entity.rotationYaw;
        float pitch = -entity.rotationPitch;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        GlStateManager.translate(x, y + 0.1, z);
        GlStateManager.rotate(yaw, 0, 1, 0);
        GlStateManager.rotate(pitch, 1, 0, 0);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.scale(0.06f, 0.06f, 0.06f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buff = tessellator.getBuffer();
        {
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : KNIFE_TOP_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0xeeffffff);
            }
            tessellator.draw();
        }
        {
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : KNIFE_BOTTOM_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0xfe_00_00_00 + color);
            }
            tessellator.draw();
        }

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderMasterSparkDanmaku(Ta_Danmaku entity, double x, double y, double z, float partialTicks) {
        int color = entity.getColor();
        float yaw = entity.rotationYaw;
        float pitch = 90 - entity.rotationPitch;
        float num = (float) (20 * (Math.atan((entity.ticksExisted + partialTicks - 50) / 2) + 1.6));

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buff = tessellator.getBuffer();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch, 1, 0, 0);
            GlStateManager.scale(num * 0.1, 16f, num * 0.1);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : BULLET_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0xffffffff);
            }
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch, 1, 0, 0);
            GlStateManager.scale(num * 0.11, 16f, num * 0.11);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : BULLET_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0xaa_00_00_00 + color);
            }
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch, 1, 0, 0);
            GlStateManager.scale(num * 0.115, 16f, num * 0.115);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : BULLET_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0x22_00_00_00 + color);
            }
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 0.5f, 0);
            GlStateManager.rotate(pitch, 1, 0, 0);
            GlStateManager.scale(num * 0.2, num * 0.2, num * 0.2);
            GlStateManager.translate(0, 2, 0);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : RING_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0x33_000000 + DanmakuColor.RED.getRgb());
            }
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 0.5f, 0);
            GlStateManager.rotate(pitch, 1, 0, 0);
            GlStateManager.scale(num * 0.15, num * 0.15, num * 0.15);
            GlStateManager.translate(0, 1.2, 0);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : RING_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0x33_000000 + DanmakuColor.GREEN.getRgb());
            }
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 0.5f, 0);
            GlStateManager.rotate(pitch, 1, 0, 0);
            GlStateManager.scale(num * 0.08, num * 0.08, num * 0.08);
            GlStateManager.translate(0, 0.2, 0);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : RING_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0x33_000000 + DanmakuColor.YELLOW.getRgb());
            }
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 0.5f, 0);
            GlStateManager.rotate(pitch, 1, 0, 0);
            GlStateManager.scale(0.6, 0.6, 0.6);
            GlStateManager.translate(0, -0.6, 0);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : GOSSIP_QUADS) {
                LightUtil.renderQuadColor(buff, quad, 0xff_01_01_01);
            }
            tessellator.draw();
        }

        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderObjDanmaku(Ta_Danmaku entity, double x, double y, double z, List<BakedQuad> quadList, float scaleIn, float scaleOut, float pitchOffset) {
        int color = entity.getColor();
        float yaw = entity.rotationYaw;
        float pitch = 90 - entity.rotationPitch;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buff = tessellator.getBuffer();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch + pitchOffset, 1, 0, 0);
            GlStateManager.scale(scaleIn, scaleIn, scaleIn);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : quadList) {
                LightUtil.renderQuadColor(buff, quad, 0xffffffff);
            }
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(yaw, 0, 1, 0);
            GlStateManager.rotate(pitch + pitchOffset, 1, 0, 0);
            GlStateManager.scale(scaleOut, scaleOut, scaleOut);
            GlStateManager.translate(0, (scaleIn - scaleOut) * 8, 0);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : quadList) {
                LightUtil.renderQuadColor(buff, quad, 0xaa_00_00_00 + color);
            }
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    private void renderButterflyDanmaku(Ta_Danmaku entity, double x, double y, double z, float partialTicks) {
        int color = entity.getColor();
        float yaw = entity.rotationYaw;
        float pitch = 180 - entity.rotationPitch;
        int argb = 0x99_00_00_00 + color;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y + 0.1, z);
        GlStateManager.rotate(yaw, 0, 1, 0);
        GlStateManager.rotate(pitch, 1, 0, 0);
        GlStateManager.scale(0.1f, 0.1f, 0.1f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buff = tessellator.getBuffer();
        GlStateManager.rotate(10 * MathHelper.cos((entity.ticksExisted + partialTicks) / 4) + 20, 0, 0, 1);
        {
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : BUTTERFLY_QUADS) {
                LightUtil.renderQuadColor(buff, quad, argb);
            }
            tessellator.draw();
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.1, z);
        GlStateManager.rotate(yaw, 0, 1, 0);
        GlStateManager.rotate(pitch, 1, 0, 0);
        GlStateManager.scale(0.1f, 0.1f, 0.1f);
        GlStateManager.rotate(160 - 10 * MathHelper.cos((entity.ticksExisted + partialTicks) / 4), 0, 0, 1);
        {
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : BUTTERFLY_QUADS) {
                LightUtil.renderQuadColor(buff, quad, argb);
            }
            tessellator.draw();
        }

        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderGloweyBallDanmaku(Ta_Danmaku entity, double x, double y, double z, DanmakuType type) {
        // 获取相关数据
        int color = entity.getColor();
        // 材质宽度
        int w = 832;
        // 材质长度
        int l = 64;

        // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）
        double pStartU = 64 * color;
        double pStartV = 0;

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.04f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX,
                1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufBuilder = tessellator.getBuffer();

        bufBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        this.renderManager.renderEngine.bindTexture(GLOWEY_BALL_DANMAKU);

        bufBuilder.pos(-type.getSize(), type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 0) / l).endVertex();
        bufBuilder.pos(-type.getSize(), -type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 64) / l).endVertex();
        bufBuilder.pos(type.getSize(), -type.getSize(), 0).tex((pStartU + 64) / w, (pStartV + 64) / l).endVertex();
        bufBuilder.pos(type.getSize(), type.getSize(), 0).tex((pStartU + 64) / w, (pStartV + 0) / l).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    public static class Factory implements IRenderFactory<Ta_Danmaku> {
        @Override
        public Render<? super Ta_Danmaku> createRenderFor(RenderManager manager) {
            return new EntityDanmakuRender(manager);
        }
    }
}
