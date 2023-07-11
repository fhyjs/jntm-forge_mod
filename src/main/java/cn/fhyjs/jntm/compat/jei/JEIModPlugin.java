package cn.fhyjs.jntm.compat.jei;

import cn.fhyjs.jntm.compat.jei.JCT.JCTCategory;
import cn.fhyjs.jntm.compat.jei.JCT.JCTMaker;
import cn.fhyjs.jntm.gui.Ji_Crafting_C;
import cn.fhyjs.jntm.gui.Ji_Crafting_GC;
import cn.fhyjs.jntm.registry.BlockRegistryHandler;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapedRecipesWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class JEIModPlugin implements IModPlugin {
    public static IStackHelper STACKS;
    public static IIngredientRegistry INGREDIENT_REGISTRY;
    public static IRecipeTransferHandlerHelper RECIPE_TRANSFER_HANDLERS;
    public static IGuiHelper GUIS;
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        List<IRecipeCategory> categories = new ArrayList<>();
        categories.add(new JCTCategory(guiHelper));
        if (!categories.isEmpty()) {
            for (IRecipeCategory category : categories) {
                registry.addRecipeCategories(category);
            }
        }
    }
    @Nullable
    public static IJeiHelpers jeiHelpers;
    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        STACKS = registry.getJeiHelpers().getStackHelper();
        GUIS = registry.getJeiHelpers().getGuiHelper();
        RECIPE_TRANSFER_HANDLERS = registry.getJeiHelpers().recipeTransferHandlerHelper();
        INGREDIENT_REGISTRY = registry.getIngredientRegistry();
        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        registry.addRecipes(JCTMaker.getValidRecipes(jeiHelpers), JCTCategory.JI_CRAFTING);

        registry.addRecipeClickArea(Ji_Crafting_GC.class, 88, 32, 28, 23, JCTCategory.JI_CRAFTING);
        recipeTransferRegistry.addRecipeTransferHandler(Ji_Crafting_C.class, JCTCategory.JI_CRAFTING, 37, 9, 1, 36);
        registry.addRecipeCatalyst(new ItemStack(BlockRegistryHandler.JI_CRAFTING_TABLE), JCTCategory.JI_CRAFTING);

        registry.handleRecipes(ShapedOreRecipe.class, recipe -> new ShapedOreRecipeWrapper(jeiHelpers, recipe), JCTCategory.JI_CRAFTING);
        registry.handleRecipes(ShapedRecipes.class, recipe -> new ShapedRecipesWrapper(jeiHelpers, recipe), JCTCategory.JI_CRAFTING);
        registry.handleRecipes(ShapelessOreRecipe.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), JCTCategory.JI_CRAFTING);
        registry.handleRecipes(ShapelessRecipes.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), JCTCategory.JI_CRAFTING);
    }
}
