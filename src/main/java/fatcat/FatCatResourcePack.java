package fatcat;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

class FatCatResourcePack implements IResourcePack
{

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException
    {
        if (!resourceExists(resourceLocation)) return null;
        return new FileInputStream(new File(FatCatMod.fatcatResourceDir, resourceLocation.getResourcePath()));
    }

    @Override
    public boolean resourceExists(ResourceLocation resourceLocation)
    {
        return new File(FatCatMod.fatcatResourceDir, resourceLocation.getResourcePath()).exists();
    }

    @Override
    public Set getResourceDomains()
    {
        return ImmutableSet.of("fatcat");
    }

    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException
    {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException
    {
        return null;
    }

    @Override
    public String getPackName()
    {
        return "FatCatResourcePack";
    }

}
