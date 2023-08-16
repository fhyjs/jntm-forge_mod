package cn.fhyjs.jntm.renderer;
// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import cn.fhyjs.jntm.entity.EntityHeli;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHeli extends ModelBase {
	public final ModelRenderer p;
	public final ModelRenderer bone_propeller;
	public final ModelRenderer bone_propeller2;
	public final ModelRenderer Body;
	public final ModelRenderer main;
	public final ModelRenderer tail;
	public final ModelRenderer box;
	public final ModelRenderer bone3;
	public final ModelRenderer wheels;
	public final ModelRenderer bone;
	public final ModelRenderer bone6;
	public final ModelRenderer top;
	public final ModelRenderer bone4;
	public final ModelRenderer bone5;

	public ModelHeli() {
		textureWidth = 256;
		textureHeight = 256;

		p = new ModelRenderer(this);
		p.setRotationPoint(0.0F, -16.0F, 12.0F);
		p.cubeList.add(new ModelBox(p, 0, 36, 2.0F, 5.0F, 42.0F, 3, 2, 2, 0.0F, false));
		p.cubeList.add(new ModelBox(p, 30, 35, -1.0F, 0.0F, -1.0F, 2, 3, 2, 0.0F, false));

		bone_propeller = new ModelRenderer(this);
		bone_propeller.setRotationPoint(0.0F, 0.0F, 0.0F);
		p.addChild(bone_propeller);
		setRotationAngle(bone_propeller, 0.0F, 3.1416F, 0.0F);
		bone_propeller.cubeList.add(new ModelBox(bone_propeller, 0, 0, -31.0F, -1.0F, -3.0F, 62, 1, 6, 0.0F, false));

		bone_propeller2 = new ModelRenderer(this);
		bone_propeller2.setRotationPoint(5.5F, 6.0F, 43.0F);
		p.addChild(bone_propeller2);
		setRotationAngle(bone_propeller2, 0.0F, -0.0436F, 0.0F);
		bone_propeller2.cubeList.add(new ModelBox(bone_propeller2, 32, 19, -0.5F, -7.0F, -1.0F, 1, 14, 2, 0.0F, false));

		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 17.0F, 0.0F);
		

		main = new ModelRenderer(this);
		main.setRotationPoint(0.0F, 0.0F, 18.0F);
		Body.addChild(main);
		

		tail = new ModelRenderer(this);
		tail.setRotationPoint(0.0F, -19.0F, 1.0F);
		main.addChild(tail);
		

		box = new ModelRenderer(this);
		box.setRotationPoint(0.0F, 0.0F, 0.0F);
		main.addChild(box);
		box.cubeList.add(new ModelBox(box, 0, 80, -8.0F, -13.0F, -34.0F, 1, 13, 34, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 24, 60, -8.0F, -26.0F, -16.0F, 1, 13, 1, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 56, 7, -8.0F, -26.0F, -3.0F, 2, 13, 2, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 28, 47, 7.0F, -26.0F, -16.0F, 1, 13, 1, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 48, 7, 6.0F, -26.0F, -3.0F, 2, 13, 2, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 0, 47, -7.0F, -1.0F, -34.0F, 14, 1, 32, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 58, 46, 7.0F, -13.0F, -34.0F, 1, 13, 34, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 0, 7, -8.0F, -13.0F, -35.0F, 16, 13, 1, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 0, 47, -5.0F, -12.0F, -38.0F, 11, 10, 3, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 48, 34, -8.0F, -14.0F, -35.0F, 16, 1, 7, 0.0F, false));
		box.cubeList.add(new ModelBox(box, 0, 21, -7.0F, -13.0F, -2.0F, 14, 13, 2, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.0F, -17.0F);
		box.addChild(bone3);
		setRotationAngle(bone3, -0.7854F, 0.0F, 0.0F);
		

		wheels = new ModelRenderer(this);
		wheels.setRotationPoint(-2.0F, 1.0F, -7.0F);
		Body.addChild(wheels);
		

		bone = new ModelRenderer(this);
		bone.setRotationPoint(-4.0F, 7.0F, 0.0F);
		wheels.addChild(bone);
		bone.cubeList.add(new ModelBox(bone, 14, 36, 0.0F, -8.0F, 11.0F, 1, 6, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 18, 36, 0.0F, -8.0F, 0.0F, 1, 6, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 94, 55, -1.0F, -3.0F, -4.0F, 1, 2, 20, 0.0F, false));

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(7.0F, 7.0F, 0.0F);
		wheels.addChild(bone6);
		bone6.cubeList.add(new ModelBox(bone6, 10, 36, 0.0F, -8.0F, 11.0F, 1, 6, 1, 0.0F, false));
		bone6.cubeList.add(new ModelBox(bone6, 34, 7, 0.0F, -8.0F, 0.0F, 1, 6, 1, 0.0F, false));
		bone6.cubeList.add(new ModelBox(bone6, 36, 80, 1.0F, -3.0F, -4.0F, 1, 2, 20, 0.0F, false));

		top = new ModelRenderer(this);
		top.setRotationPoint(0.0F, -4.0F, 0.0F);
		top.cubeList.add(new ModelBox(top, 48, 7, -8.0F, -6.0F, -2.0F, 16, 1, 19, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 94, 34, -5.0F, -9.0F, 3.0F, 11, 3, 18, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 70, 93, -7.0F, -6.0F, 17.0F, 14, 12, 14, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 99, 7, -5.0F, -6.0F, 31.0F, 10, 3, 11, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 0, 7, -3.0F, -7.0F, 21.0F, 5, 2, 38, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 48, 27, -17.0F, -6.0F, 42.0F, 33, 2, 5, 0.0F, false));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(0.0F, 0.0F, -4.0F);
		top.addChild(bone4);
		setRotationAngle(bone4, 0.7854F, 0.0F, 0.0F);
		bone4.cubeList.add(new ModelBox(bone4, 116, 55, 7.0F, -2.1213F, -12.9497F, 1, 1, 19, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 112, 77, -8.0F, -2.1213F, -12.9497F, 1, 1, 19, 0.0F, false));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, -4.0F, 17.0F);
		top.addChild(bone5);
		setRotationAngle(bone5, -0.7418F, 0.0F, 0.0F);
		bone5.cubeList.add(new ModelBox(bone5, 117, 110, -6.0F, -3.1044F, 7.126F, 12, 22, 9, 0.0F, false));
		bone5.cubeList.add(new ModelBox(bone5, 0, 60, -4.0F, -12.008F, 11.1639F, 8, 11, 4, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if (entity instanceof EntityHeli&&entity.getControllingPassenger()!=null) {
			bone_propeller.rotateAngleY += (float) (0.05 * entity.posY/10);
			bone_propeller2.rotateAngleX += (float) (0.05 * entity.posY/10);
			if (bone_propeller.rotateAngleY > 360) bone_propeller.rotateAngleY = 0;
			if (bone_propeller2.rotateAngleX > 360) bone_propeller2.rotateAngleX = 0;
		}
		p.render(f5);
		Body.render(f5);
		top.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}