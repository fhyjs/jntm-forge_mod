package cn.fhyjs.jntm.utility;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class FTR implements IResourcePack {
    @Override
    public InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException {
        return DefaultResourcePack.class.getResourceAsStream(String.valueOf(p_110590_1_));
    }
    @Override
    public boolean resourceExists(ResourceLocation p_110589_1_) {
        return new File("D:/"+p_110589_1_.getResourcePath()+".png").exists();
    }

    @Override
    public Set getResourceDomains() {
        return ImmutableSet.of("jntm");
    }

    @Nullable
    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return ImageIO.read(DefaultResourcePack.class.getResourceAsStream("/" + (new ResourceLocation("pack.png")).getResourcePath()));
    }

    @Override
    public String getPackName() {
        return "jntm";
    }
}
