package cn.fhyjs.jntm.renderer;// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.EntityDlw;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDlw extends AnimatedGeoModel<EntityDlw> {
	private static final ResourceLocation modelResource = new ResourceLocation(Jntm.MODID, "geo/dlw.geo.json");
	private static final ResourceLocation textureResource = new ResourceLocation(Jntm.MODID, "textures/entity/daliwang.png");
	private static final ResourceLocation animationResource = new ResourceLocation(Jntm.MODID, "animations/dlw.animation.json");

	@Override
	public ResourceLocation getModelLocation(EntityDlw object) {
		return modelResource;
	}

	@Override
	public ResourceLocation getTextureLocation(EntityDlw object) {
		return textureResource;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityDlw object) {
		return animationResource;
	}
}