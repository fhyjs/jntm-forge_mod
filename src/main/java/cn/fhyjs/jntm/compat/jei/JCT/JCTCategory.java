package cn.fhyjs.jntm.compat.jei.JCT;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.registry.BlockRegistryHandler;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;

import java.util.List;


public class JCTCategory implements IRecipeCategory<IRecipeWrapper> {
    public static final String JI_CRAFTING = String.format("%s.jct", Jntm.MODID);
    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;

    public static final int width = 116;
    public static final int height = 54;

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;
    private final ICraftingGridHelper craftingGridHelper;

    public JCTCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
        background = guiHelper.createDrawable(location, 0, 60, width, height);
        icon = guiHelper.createDrawableIngredient(new ItemStack(BlockRegistryHandler.JI_CRAFTING_TABLE));
        localizedName = I18n.format("gui.jntm.category.jicraftingTable");
        craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
    }

    @Override
    public String getUid() {
        return JI_CRAFTING;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getModName() {
        return Jntm.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }


    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(craftOutputSlot, false, 94, 18);

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int index = craftInputSlot1 + x + (y * 3);
                guiItemStacks.init(index, true, x * 18, y * 18);
            }
        }

        if (recipeWrapper instanceof ICustomCraftingRecipeWrapper) {
            ICustomCraftingRecipeWrapper customWrapper = (ICustomCraftingRecipeWrapper) recipeWrapper;
            customWrapper.setRecipe(recipeLayout, ingredients);
            return;
        }

        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

        if (recipeWrapper instanceof IShapedCraftingRecipeWrapper) {
            IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) recipeWrapper;
            craftingGridHelper.setInputs(guiItemStacks, inputs, wrapper.getWidth(), wrapper.getHeight());
        } else {
            craftingGridHelper.setInputs(guiItemStacks, inputs);
            recipeLayout.setShapeless();
        }
        guiItemStacks.set(craftOutputSlot, outputs.get(0));

        if (recipeWrapper instanceof ICraftingRecipeWrapper) {
            ICraftingRecipeWrapper craftingRecipeWrapper = (ICraftingRecipeWrapper) recipeWrapper;
            ResourceLocation registryName = craftingRecipeWrapper.getRegistryName();
            if (registryName != null) {
                guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
                    if (slotIndex == craftOutputSlot) {
                        String recipeModId = registryName.getResourceDomain();

                        boolean modIdDifferent = false;
                        ResourceLocation itemRegistryName = ingredient.getItem().getRegistryName();
                        if (itemRegistryName != null) {
                            String itemModId = itemRegistryName.getResourceDomain();
                            modIdDifferent = !recipeModId.equals(itemModId);
                        }

                        if (modIdDifferent) {
                            String modName = Loader.instance().getCustomModProperties(recipeModId).get("name");
                            if (modName != null) {
                                tooltip.add(TextFormatting.GRAY + I18n.format("jei.tooltip.recipe.by", modName));
                            }
                        }

                        boolean showAdvanced = Minecraft.getMinecraft().gameSettings.advancedItemTooltips || GuiScreen.isShiftKeyDown();
                        if (showAdvanced) {
                            tooltip.add(TextFormatting.DARK_GRAY +I18n.format("jei.tooltip.recipe.id", registryName.toString()));
                        }
                    }
                });
            }
        }
    }

}
