package cn.fhyjs.jntm.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTEL extends ModelBase {
    public ModelRenderer renderer;
    public ModelTEL(int i, int i1, int i2, int i3,int h) {
        super();
        this.textureWidth = i2;
        this.textureHeight = i3;
        this.renderer = new ModelRenderer(this, i, i1);
        //this.renderer.addBox(0, 0, 0, 16, 16, 16, 0.0F);
        this.renderer.addBox(0,0,0,16,h,16);
        this.renderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.renderer.render(scale);
    }
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.renderer.rotateAngleY = netHeadYaw * 0.017453292F;
        this.renderer.rotateAngleX = headPitch * 0.017453292F;
    }
}
