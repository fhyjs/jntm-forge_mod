package cn.fhyjs.jntm.compat.jei.JCT;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.plugins.vanilla.crafting.ShapedRecipesWrapper;
import net.minecraft.item.crafting.ShapedRecipes;

public class J_ShapedRecipesWrapper extends ShapedRecipesWrapper {
    public J_ShapedRecipesWrapper(IJeiHelpers jeiHelpers, ShapedRecipes recipe) {
        super(jeiHelpers, recipe);
    }
}
