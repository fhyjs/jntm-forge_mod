package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.entity.XiGua;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderXigua extends RenderBiped<XiGua> {
    private static final ResourceLocation TEX = new ResourceLocation("jntm:textures/entity/xigua.png");
    public RenderXigua(RenderManager renderManagerIn) {
        super(renderManagerIn,new ModelXigua(),.5f);
    }
    @Override
    protected ResourceLocation getEntityTexture(XiGua entity)
    {
        return TEX;
    }
}
