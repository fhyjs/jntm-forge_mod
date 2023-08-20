package cn.fhyjs.jntm.renderer;// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.EntityDlw;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelDlw extends AnimatedGeoModel<EntityDlw> {
	private static final ResourceLocation modelResource = new ResourceLocation(Jntm.MODID, "geo/dlw.geo.json");
	private static final ResourceLocation textureResource = new ResourceLocation(Jntm.MODID, "textures/entity/daliwang.png");
	private static final ResourceLocation animationResource = new ResourceLocation(Jntm.MODID, "animations/dlw.animation.json");

	@Override
	public void setLivingAnimations(EntityDlw entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = getAnimationProcessor().getBone("head");
		if (head != null && customPredicate != null) {
			@SuppressWarnings("unchecked")
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
	}

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