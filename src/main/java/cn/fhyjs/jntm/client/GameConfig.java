package cn.fhyjs.jntm.client;

import cn.fhyjs.jntm.Jntm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameConfig {
    private static final Gson GSON = new Gson();
    private static final Type STRING_LIST_TYPE = new TypeToken<List<String>>() {
    }.getType();
    protected Map<String, String> configs = new LinkedHashMap<>();
    private final Path configFile;

    public GameConfig(Path configFile) throws Exception {
        this.configFile = configFile;
        if (!Files.exists(configFile)) {
            return;
        }
        this.configs = FileUtils.readLines(configFile.toFile(), StandardCharsets.UTF_8).stream()
                .map(it -> it.split(":", 2))
                .filter(it -> it.length == 2)
                .collect(Collectors.toMap(it -> it[0], it -> it[1], (a, b) -> a, LinkedHashMap::new));
    }

    public void writeToFile() throws Exception {
        FileUtils.writeLines(configFile.toFile(), "UTF-8", configs.entrySet().stream()
                .map(it -> it.getKey() + ":" + it.getValue()).collect(Collectors.toList()));
    }

    public void addResourcePack(String baseName, String resourcePack) {
        List<String> resourcePacks = GSON.fromJson(
                configs.computeIfAbsent("resourcePacks", it -> "[]"), STRING_LIST_TYPE);
        //If resource packs already contains target resource pack, nothing to do
        if (resourcePacks.contains(resourcePack)) {
            return;
        }
        //Remove other Minecraft Mod Language Pack
        resourcePacks = resourcePacks.stream().filter(it -> !it.contains(baseName)).collect(Collectors.toList());
        resourcePacks.add(resourcePack);
        configs.put("resourcePacks", GSON.toJson(resourcePacks));
        Jntm.logger.info(String.format("Resource Packs: %s", configs.get("resourcePacks")));
//        configs.put("lang", "zh_cn");
    }
}
