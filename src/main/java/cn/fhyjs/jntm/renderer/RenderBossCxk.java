package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.entity.Boss_Cxk;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import cn.fhyjs.jntm.entity.ModelBossCxk;


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
