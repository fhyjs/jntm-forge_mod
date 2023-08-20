package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.entity.EntityDlw;
import com.sun.javafx.sg.prism.NodeEffectInput;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class RenderDlw extends GeoEntityRenderer<EntityDlw> {
    public RenderDlw(RenderManager context) {
        super(context, new ModelDlw());
    }
    @Override
    public void doRender(EntityDlw entity, double x, double y, double z, float entityYaw, float partialTicks) {
        try {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);

        }catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
