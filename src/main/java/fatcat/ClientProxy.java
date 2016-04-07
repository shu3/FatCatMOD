package fatcat;

import fatcat.entitiy.EntityFatCat;
import fatcat.model.RenderFatCat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{

    @Override
    public void registerRenderers()
    {
        registerEntityRenderers();
        registerItemRenderers();
    }

    @Override
    public void spawnParticle(EnumParticleTypes type, final double posX, final double posY, final double posZ, double verX, double verY, double verZ, int number)
    {
        spawnParticle(type, posX, posY, posZ, verX, verY, verZ, number, new int[0]);
    }

    @Override
    public void spawnParticle(EnumParticleTypes type, final double posX, final double posY, final double posZ, double verX, double verY, double verZ, int number, int... options)
    {
//		System.out.println("ClientProxy(spawnParticle): type="+type);
        for (int i = 0; i < number; i++)
        {
            // spawnParticle
            Minecraft.getMinecraft().renderGlobal.spawnParticle(type.getParticleID(), type.func_179344_e(), posX, posY, posZ, verX, verY, verZ, options);
        }
    }

    private void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityFatCat.class, new RenderFatCat(Minecraft.getMinecraft().getRenderManager()));
    }

    private void registerItemRenderers()
    {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(FatCatMod.egg, 0, new ModelResourceLocation(FatCatMod.ID + ":" + FatCatMod.egg.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(FatCatMod.turd, 0, new ModelResourceLocation(FatCatMod.ID + ":" + FatCatMod.turd.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(FatCatMod.brush, 0, new ModelResourceLocation(FatCatMod.ID + ":" + FatCatMod.brush.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(FatCatMod.furBall, 0, new ModelResourceLocation(FatCatMod.ID + ":" + FatCatMod.furBall.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(FatCatMod.featherToy, 0, new ModelResourceLocation(FatCatMod.ID + ":" + FatCatMod.featherToy.getUnlocalizedName().substring(5), "inventory"));
    }

}
