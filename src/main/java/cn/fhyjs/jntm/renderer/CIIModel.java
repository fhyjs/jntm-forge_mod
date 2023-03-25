package cn.fhyjs.jntm.renderer;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShield;

public class CIIModel extends ModelShield {
    public ModelRenderer plate;
    public ModelRenderer plate2;
    public ModelRenderer plate3;
    public ModelRenderer plate4;
    public ModelRenderer plate5;
    public ModelRenderer plate6;
    public ModelRenderer plate7;
    public ModelRenderer handle;

    public CIIModel()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.plate2 = new ModelRenderer(this,0,0);
        this.plate2.addBox(-6F,-11F,-2.0F,11,19,1,0.0F);
        this.plate3 = new ModelRenderer(this,0,20);
        this.plate3.addBox(-5F,8F,-2.0F,9,1,1,0.0F);
        this.plate4 = new ModelRenderer(this,0,22);
        this.plate4.addBox(-4F,9F,-2.0F,7,1,1,0.0F);
        this.plate5 = new ModelRenderer(this,0,24);
        this.plate5.addBox(-3F,10F,-2.0F,5,2,1,0.0F);
        this.plate6 = new ModelRenderer(this,0,27);
        this.plate6.addBox(-2F,12F,-2.0F,3,1,1,0.0F);
        this.plate7 = new ModelRenderer(this,0,29);
        this.plate7.addBox(-1F,13F,-2.0F,1,1,1,0.0F);
        this.handle = new ModelRenderer(this, 26, 0);
        this.handle.addBox(-1.0F, -3.0F, -1.0F, 2, 6, 6, 0.0F);
    }
    public void render()
    {
        this.handle.render(0.0625F);
        this.plate2.render(0.0625F);
        this.plate3.render(0.0625F);
        this.plate4.render(0.0625F);
        this.plate5.render(0.0625F);
        this.plate6.render(0.0625F);
        this.plate7.render(0.0625F);
    }
}
