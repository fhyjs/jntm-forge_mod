package cn.fhyjs.jntm.utility;


import cn.fhyjs.jntm.Jntm;
import com.google.gson.*;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Ji_Crafting_Recipe {
    private static final Logger LOGGER = Jntm.logger;
    private static int nextAvailableId;
    public static final Map<ResourceLocation, IRecipe> register = new HashMap<>();

    public static boolean init()
    {
        try
        {
            return parseJsonRecipes();
        }
        catch (Throwable var1)
        {
            return false;
        }
    }

    private static boolean parseJsonRecipes()
    {
        FileSystem filesystem = null;
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        boolean flag1;

        try
        {
            URL url = Ji_Crafting_Recipe.class.getResource("/assets/.jiassetsroot");

            if (url != null)
            {
                URI uri = url.toURI();
                Path path;

                if ("file".equals(uri.getScheme()))
                {
                    path = Paths.get(Ji_Crafting_Recipe.class.getResource("/assets/jntm/recipes/ji_crafting").toURI());
                }
                else
                {
                    if (!"jar".equals(uri.getScheme()))
                    {
                        LOGGER.error("Unsupported scheme " + uri + " trying to list all recipes");
                        boolean flag2 = false;
                        return flag2;
                    }

                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath("/assets/jntm/recipes/ji_crafting");
                }

                Iterator<Path> iterator = Files.walk(path).iterator();

                while (iterator.hasNext())
                {
                    Path path1 = iterator.next();

                    if ("json".equals(FilenameUtils.getExtension(path1.toString())))
                    {
                        Path path2 = path.relativize(path1);
                        String s = FilenameUtils.removeExtension(path2.toString()).replaceAll("\\\\", "/");
                        ResourceLocation resourcelocation = new ResourceLocation(Jntm.MODID,s);
                        BufferedReader bufferedreader = null;

                        try
                        {
                            boolean flag;

                            try
                            {
                                bufferedreader = Files.newBufferedReader(path1);
                                register(s, parseRecipeJson((JsonObject) JsonUtils.fromJson(gson, bufferedreader, JsonObject.class)));
                            }
                            catch (JsonParseException jsonparseexception)
                            {
                                LOGGER.error("Parsing error loading recipe " + resourcelocation, (Throwable)jsonparseexception);
                                flag = false;
                                return flag;
                            }
                            catch (IOException ioexception)
                            {
                                LOGGER.error("Couldn't read recipe " + resourcelocation + " from " + path1, (Throwable)ioexception);
                                flag = false;
                                return flag;
                            }
                        }
                        finally
                        {
                            IOUtils.closeQuietly((Reader)bufferedreader);
                        }
                    }
                }

                return true;
            }

            LOGGER.error("Couldn't find .mcassetsroot");
            flag1 = false;
        }
        catch (IOException | URISyntaxException urisyntaxexception)
        {
            LOGGER.error("Couldn't get a list of all recipe files", (Throwable)urisyntaxexception);
            flag1 = false;
            return flag1;
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)filesystem);
        }

        return flag1;
    }

    private static IRecipe parseRecipeJson(JsonObject p_193376_0_)
    {
        String s = JsonUtils.getString(p_193376_0_, "Jtype");

        if ("crafting_shaped".equals(s))
        {
            return ShapedRecipes.deserialize(p_193376_0_);
        }
        else if ("crafting_shapeless".equals(s))
        {
            return ShapelessRecipes.deserialize(p_193376_0_);
        }
        else
        {
            throw new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
        }
    }

    //Forge: Made private use GameData/Registry events!
    private static void register(String name, IRecipe recipe)
    {
        register(new ResourceLocation(name), recipe);
    }

    //Forge: Made private use GameData/Registry events!
    private static void register(ResourceLocation name, IRecipe recipe)
    {
        if (register.containsKey(name))
        {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + name);
        }
        else
        {
            recipe.setRegistryName(name);
            register.put(recipe.getRegistryName(),recipe);
        }
    }

    public static ItemStack findMatchingResult(InventoryCrafting craftMatrix, World worldIn)
    {
        for (IRecipe irecipe : register.values())
        {
            if (irecipe.matches(craftMatrix, worldIn))
            {
                return irecipe.getCraftingResult(craftMatrix);
            }
        }

        return ItemStack.EMPTY;
    }



}
