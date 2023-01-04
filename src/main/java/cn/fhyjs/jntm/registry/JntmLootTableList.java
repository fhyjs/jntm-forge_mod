package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.Jntm;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;

import java.io.File;
import java.util.Collections;
import java.util.Set;

public class JntmLootTableList  {
    private static final Set<ResourceLocation> LOOT_TABLES = Sets.<ResourceLocation>newHashSet();
    private static final Set<ResourceLocation> READ_ONLY_LOOT_TABLES = Collections.<ResourceLocation>unmodifiableSet(LOOT_TABLES);
    public static final ResourceLocation ENTITIES_CXK = register("entities/cxk");

    private static ResourceLocation register(String id)
    {
        return register(new ResourceLocation(Jntm.MODID, id));
    }
    public static ResourceLocation register(ResourceLocation id)
    {
        if (LOOT_TABLES.add(id))
        {
            return id;
        }
        else
        {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> getAll()
    {
        return READ_ONLY_LOOT_TABLES;
    }

    public static boolean test()
    {
        LootTableManager loottablemanager = new LootTableManager((File)null);

        for (ResourceLocation resourcelocation : READ_ONLY_LOOT_TABLES)
        {
            if (loottablemanager.getLootTableFromLocation(resourcelocation) == LootTable.EMPTY_LOOT_TABLE)
            {
                return false;
            }
        }

        return true;
    }
}
