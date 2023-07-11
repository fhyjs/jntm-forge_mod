package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.entity.Boss_Cxk;
import cn.fhyjs.jntm.entity.ModelBossCxk;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderBossCxk extends RenderBiped<Boss_Cxk> {
    private static final ResourceLocation TEX = new ResourceLocation("jntm:textures/entity/bosscxk.png");
    public RenderBossCxk(RenderManager renderManagerIn) {
        super(renderManagerIn,new ModelBossCxk(),.5f);
    }
    @Override
    protected ResourceLocation getEntityTexture(Boss_Cxk entity)
    {
        return TEX;
    }
}
