package cn.fhyjs.jntm.client;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Jntm_RP implements IResourcePack {

    public static final Map<String,String> map=new HashMap<>();
    public static void la(String a,String b){map.put(a,b);}
    public Jntm_RP (){
        la("minecraft:lang/zh_cn.lang","assets/jntm/lang/zh_ji.lang");
        la("minecraft:gui/title/mojang.png","assets/jntm/gui/mojang.png");
    }
    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        return Jntm.class.getClassLoader().getResourceAsStream(map.get(location.toString()));
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        return map.containsKey(location.toString());
    }

    @Override
    public Set<String> getResourceDomains() {
        Set<String> DEFAULT_RESOURCE_DOMAINS = new HashSet<>();
        for (String s:map.keySet()){
            ResourceLocation rl=new ResourceLocation(s);
            DEFAULT_RESOURCE_DOMAINS.add(rl.getResourceDomain());
        }
        return DEFAULT_RESOURCE_DOMAINS;
    }

    @Nullable
    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return null;
    }

    @Override
    public String getPackName() {
        return "Jntm";
    }
}
