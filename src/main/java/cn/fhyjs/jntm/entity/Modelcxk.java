package cn.fhyjs.jntm.entity;// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Modelcxk extends ModelBase {
	private final ModelRenderer head;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer body;
	private final ModelRenderer rightLeg;
	private final ModelRenderer leftLeg;
	private final ModelRenderer rightWing;
	private final ModelRenderer leftWing;
	private final ModelRenderer bill;
	private final ModelRenderer chin;

	public Modelcxk() {
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 15.0F, -4.0F);
		head.cubeList.add(new ModelBox(head, 22, 11, -2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, 0.9599F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 18, 0, -6.0F, -6.0F, -1.0F, 6, 2, 2, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, -0.6109F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 24, 4, -1.0F, -7.0F, -1.0F, 5, 2, 2, 0.0F, false));

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 16.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 0, -3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F, false));

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 12, 24, -1.0F, 0.0F, -3.0F, 3, 5, 3, 0.0F, false));

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 24, -1.0F, 0.0F, -3.0F, 3, 5, 3, 0.0F, false));

		rightWing = new ModelRenderer(this);
		rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
		rightWing.cubeList.add(new ModelBox(rightWing, 14, 14, 0.0F, 0.0F, -3.0F, 1, 4, 6, 0.0F, false));

		leftWing = new ModelRenderer(this);
		leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);
		leftWing.cubeList.add(new ModelBox(leftWing, 0, 14, -1.0F, 0.0F, -3.0F, 1, 4, 6, 0.0F, false));

		bill = new ModelRenderer(this);
		bill.setRotationPoint(0.0F, 15.0F, -4.0F);
		bill.cubeList.add(new ModelBox(bill, 8, 14, -2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F, false));

		chin = new ModelRenderer(this);
		chin.setRotationPoint(0.0F, 15.0F, -4.0F);
		chin.cubeList.add(new ModelBox(chin, 24, 24, -1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		head.render(f5);
		body.render(f5);
		rightLeg.render(f5);
		leftLeg.render(f5);
		rightWing.render(f5);
		leftWing.render(f5);
		bill.render(f5);
		chin.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}