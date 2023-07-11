package cn.fhyjs.jntm.compat.jei.JCT;

import cn.fhyjs.jntm.utility.Ji_Crafting_Recipe;
import mezz.jei.Internal;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import mezz.jei.startup.StackHelper;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class JCTMaker {
    public JCTMaker() {
    }


    public static List<IRecipe> getValidRecipes(IJeiHelpers jeiHelpers) {
        JCTMaker.CraftingRecipeValidator<ShapedOreRecipe> shapedOreRecipeValidator = new JCTMaker.CraftingRecipeValidator<>((recipex) -> {
            return new ShapedOreRecipeWrapper(jeiHelpers, recipex);
        });
        JCTMaker.CraftingRecipeValidator<ShapedRecipes> shapedRecipesValidator = new JCTMaker.CraftingRecipeValidator<>((recipex) -> {
            return new J_ShapedRecipesWrapper(jeiHelpers, recipex);
        });
        JCTMaker.CraftingRecipeValidator<ShapelessOreRecipe> shapelessOreRecipeValidator = new JCTMaker.CraftingRecipeValidator<>((recipex) -> {
            return new ShapelessRecipeWrapper<>(jeiHelpers, (IRecipe) recipex);
        });
        JCTMaker.CraftingRecipeValidator<ShapelessRecipes> shapelessRecipesValidator = new JCTMaker.CraftingRecipeValidator<>((recipex) -> {
            return new ShapelessRecipeWrapper<>(jeiHelpers, (IRecipe) recipex);
        });
        StackHelper stackHelper = Internal.getStackHelper();
        Iterator<IRecipe> recipeIterator = CraftingManager.REGISTRY.iterator();
        List<IRecipe> validRecipes = new ArrayList();
        List<IRecipe> JrecipeIterator = new ArrayList<>( Ji_Crafting_Recipe.register.values());
        while(recipeIterator.hasNext()) {
            IRecipe recipe = (IRecipe)recipeIterator.next();
            if (recipe instanceof ShapedOreRecipe) {
                if (shapedOreRecipeValidator.isRecipeValid((ShapedOreRecipe)recipe, stackHelper)) {
                    validRecipes.add(recipe);
                }
            } else if (recipe instanceof ShapedRecipes) {
                if (shapedRecipesValidator.isRecipeValid((ShapedRecipes)recipe, stackHelper)) {
                    validRecipes.add(recipe);
                }
            } else if (recipe instanceof ShapelessOreRecipe) {
                if (shapelessOreRecipeValidator.isRecipeValid((ShapelessOreRecipe)recipe, stackHelper)) {
                    validRecipes.add(recipe);
                }
            } else if (recipe instanceof ShapelessRecipes) {
                if (shapelessRecipesValidator.isRecipeValid((ShapelessRecipes)recipe, stackHelper)) {
                    validRecipes.add(recipe);
                }
            } else {
                validRecipes.add(recipe);
            }
        }

        for (IRecipe recipe : JrecipeIterator){
            if (recipe instanceof ShapedOreRecipe) {
                if (shapedOreRecipeValidator.isRecipeValid((ShapedOreRecipe)recipe, stackHelper)) {
                    validRecipes.add(recipe);
                }
            } else if (recipe instanceof ShapedRecipes) {
                if (shapedRecipesValidator.isRecipeValid((ShapedRecipes)recipe, stackHelper)) {
                    validRecipes.add(recipe);
                }
            } else if (recipe instanceof ShapelessOreRecipe) {
                if (shapelessOreRecipeValidator.isRecipeValid((ShapelessOreRecipe)recipe, stackHelper)) {
                    validRecipes.add(recipe);
                }
            } else if (recipe instanceof ShapelessRecipes) {
                if (shapelessRecipesValidator.isRecipeValid((ShapelessRecipes)recipe, stackHelper)) {
                    validRecipes.add(recipe);
                }
            } else {
                validRecipes.add(recipe);
            }
        }

        return validRecipes;
    }

    private static final class CraftingRecipeValidator<T extends IRecipe> {
        private static final int INVALID_COUNT = -1;
        private static final int CANT_DISPLAY = -2;
        private final IRecipeWrapperFactory<T> recipeWrapperFactory;

        public CraftingRecipeValidator(IRecipeWrapperFactory<T> recipeWrapperFactory) {
            this.recipeWrapperFactory = recipeWrapperFactory;
        }

        public boolean isRecipeValid(T recipe, StackHelper stackHelper) {
            ItemStack recipeOutput = recipe.getRecipeOutput();
            if (recipeOutput != null && !recipeOutput.isEmpty()) {
                List<Ingredient> ingredients = recipe.getIngredients();
                if (ingredients == null) {
                    String recipeInfo = this.getInfo(recipe);
                    Log.get().error("Recipe has no input Ingredients. {}", recipeInfo);
                    return false;
                } else {
                    int inputCount = getInputCount(ingredients, stackHelper);
                    String recipeInfo;
                    if (inputCount == -2) {
                        recipeInfo = this.getInfo(recipe);
                        Log.get().warn("Recipe contains ingredients that can't be understood or displayed by JEI: {}", recipeInfo);
                        return false;
                    } else if (inputCount == -1) {
                        return false;
                    } else if (inputCount > 9) {
                        recipeInfo = this.getInfo(recipe);
                        Log.get().error("Recipe has too many inputs. {}", recipeInfo);
                        return false;
                    } else if (inputCount == 0) {
                        recipeInfo = this.getInfo(recipe);
                        Log.get().error("Recipe has no inputs. {}", recipeInfo);
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                if (!recipe.isDynamic()) {
                    String recipeInfo = this.getInfo(recipe);
                    Log.get().error("Recipe has no output. {}", recipeInfo);
                }

                return false;
            }
        }

        private String getInfo(T recipe) {
            IRecipeWrapper recipeWrapper = this.recipeWrapperFactory.getRecipeWrapper(recipe);
            return ErrorUtil.getInfoFromRecipe(recipe, recipeWrapper);
        }

        protected static int getInputCount(List<Ingredient> ingredientList, StackHelper stackHelper) {
            int inputCount = 0;

            for(Iterator var3 = ingredientList.iterator(); var3.hasNext(); ++inputCount) {
                Ingredient ingredient = (Ingredient)var3.next();
                List<ItemStack> input = stackHelper.getMatchingStacks(ingredient);
                if (input == null) {
                    return -1;
                }

                if (ingredient instanceof OreIngredient && input.isEmpty()) {
                    return -1;
                }

                if (!ingredient.isSimple() && input.isEmpty()) {
                    return -2;
                }
            }

            return inputCount;
        }
    }
}
