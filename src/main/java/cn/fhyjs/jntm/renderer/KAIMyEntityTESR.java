package cn.fhyjs.jntm.renderer;

import com.kAIS.KAIMyEntity.renderer.MMDAnimManager;
import com.kAIS.KAIMyEntity.renderer.MMDModelManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class KAIMyEntityTESR<T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage,  float alpha)
    {
        super.render(te, x, y, z,partialTicks,destroyStage,alpha);

        MMDModelManager.Model model = MMDModelManager.GetModelOrInPool(te, modelName, false);

        if (model != null)
        {
            AnimStateChangeOnce(model, MMDModelManager.EntityState.Idle, "idle");
            model.unuseTime = 0;
            model.model.Render(x, y, z, 0);
        }
    }

    public KAIMyEntityTESR(String modelName)
    {
        this.modelName = modelName;
    }
    protected String modelName;

    void AnimStateChangeOnce(MMDModelManager.Model model, MMDModelManager.EntityState targetState, String animName)
    {
        MMDModelManager.ModelWithEntityState m = (MMDModelManager.ModelWithEntityState)model;
        if (m.state != targetState)
        {
            m.state = targetState;
            model.model.ChangeAnim(MMDAnimManager.GetAnimModel(model.model, animName), 0);
        }
    }
}
