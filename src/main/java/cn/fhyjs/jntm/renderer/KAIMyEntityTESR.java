package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.gui.CheckMDR;
import cn.fhyjs.jntm.interfaces.IhasMMDRender;
import com.kAIS.KAIMyEntity.NativeFunc;
import com.kAIS.KAIMyEntity.renderer.MMDAnimManager;
import com.kAIS.KAIMyEntity.renderer.MMDModelManager;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.Map;

public class KAIMyEntityTESR<T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
    private Map<TileEntity,String> oldmN = new HashMap<>();
    private Map<TileEntity,String> oldaN = new HashMap<>();
    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage,  float alpha)
    {
        super.render(te, x, y, z,partialTicks,destroyStage,alpha);
        try {
            if (!CheckMDR.isInstallMMDR()) {
                renderHT(te, new TextComponentString("KaiMyEntity mod is not install!!"), x, y, z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-90, 0f, 1f, 0f);
                Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.STAINED_GLASS.getStateFromMeta(14), 1f);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                return;
            }
            if ((!(te instanceof IhasMMDRender))) {
                renderHT(te, new TextComponentString("Internal Error!!"), x, y, z);
                return;
            }
            if (((IhasMMDRender) te).getModelName() == null || ((IhasMMDRender) te).getModelName().equals("")) {
                renderHT(te, new TextComponentString("Model name is not set!!"), x, y, z);
                return;
            }
            MMDModelManager.Model model = MMDModelManager.GetModelOrInPool(te, ((IhasMMDRender) te).getModelName(), false);
            if (!oldmN.containsKey(te)) oldmN.put(te, ((IhasMMDRender) te).getModelName());
            if (!oldmN.get(te).equals(((IhasMMDRender) te).getModelName())) {
                oldmN.put(te, ((IhasMMDRender) te).getModelName());
                MMDModelManager.models.remove(te);
                model = null;
            }
            if (model != null) {
                if (((IhasMMDRender) te).getActionName() != null)
                    AnimStateChangeOnce(te, model, ((IhasMMDRender) te).getActionName());
                model.unuseTime = 0;
                int ya;
                switch (getWorld().getBlockState(te.getPos()).getValue(BlockDirectional.FACING).getOpposite()) {
                    case NORTH:
                        ya = 180;
                        break;
                    case SOUTH:
                        ya = 0;
                        break;
                    case WEST:
                        ya = 90;
                        break;
                    case EAST:
                        ya = -90;
                        break;
                    default:
                        ya = 0;
                        break;
                }
                GlStateManager.enableNormalize();
                model.model.Render(x + .5, y + .1, z + .5, ya);
                GlStateManager.disableNormalize();
            } else {
                renderHT(te, new TextComponentString(String.format("No model named '%s'!!", ((IhasMMDRender) te).getModelName())), x, y, z);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    public void renderHT(T te,ITextComponent itextcomponent,double x, double y, double z){

        if (itextcomponent != null && this.rendererDispatcher.cameraHitResult != null && te.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos()))
        {
            this.setLightmapDisabled(true);
            this.drawNameplate(te, itextcomponent.getFormattedText(), x, y, z, 12);
            this.setLightmapDisabled(false);
        }
    }
    String oldan;
    void AnimStateChangeOnce(TileEntity te,MMDModelManager.Model model, String animName)
    {
        if (!oldaN.containsKey(te)) oldaN.put(te,"");
        if (!oldaN.get(te).equals(animName)){
            oldaN.put(te,animName);
            model.model.ResetPhysics();
            model.model.ChangeAnim(MMDAnimManager.GetAnimModel(model.model, animName), 0);
            model.model.ResetPhysics();
        }
    }
}
