package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.CxkTnt_E;
import cn.fhyjs.jntm.entity.Modelcxk;
import cn.fhyjs.jntm.entity.cxk;
import cn.fhyjs.jntm.registry.BlockRegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class Rendercxk extends RenderLiving<cxk> {
    public static final ResourceLocation TEXTURES = new ResourceLocation(Jntm.MODID + ":textures/entity/cxk.png");

    public Rendercxk(RenderManager p_i47211_1_)
    {
        super(p_i47211_1_, new Modelcxk(), 0.3F);
    }

    protected ResourceLocation getEntityTexture(cxk entity)
    {
        return TEXTURES;
    }
    @Override
    public void doRender(cxk entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected float handleRotationFloat(EntityChicken livingBase, float partialTicks)
    {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
