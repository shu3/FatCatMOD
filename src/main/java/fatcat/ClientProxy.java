package fatcat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import fatcat.gui.GuiStatusHandler;
import fatcat.model.RenderFatCat;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
        registerEntityRenderers();
    	registerItemRenderers();
	}
	
	@Override
	public void spawnParticle(EnumParticleTypes type, final double posX, final double posY, final double posZ, double verX, double verY, double verZ, int number) {
		spawnParticle(type, posX, posY, posZ, verX, verY, verZ, number, new int[0]);
	}

	@Override
	public void spawnParticle(EnumParticleTypes type, final double posX, final double posY, final double posZ, double verX, double verY, double verZ, int number, int ... options) {
//		System.out.println("ClientProxy(spawnParticle): type="+type);
		for (int i = 0; i < number; i++) {
			// spawnParticle
			Minecraft.getMinecraft().renderGlobal.func_180442_a(type.func_179348_c(), type.func_179344_e(),posX, posY, posZ, verX, verY, verZ, options);
        }
	}
	
	private void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFatCat.class, new RenderFatCat(Minecraft.getMinecraft().getRenderManager()));
	}
	
	
	private void registerItemRenderers() {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(FatCatMod.egg, 0, new ModelResourceLocation(FatCatMod.MODID + ":" + FatCatMod.egg.getUnlocalizedName().substring(5), "inventory"));
		renderItem.getItemModelMesher().register(FatCatMod.unko, 0, new ModelResourceLocation(FatCatMod.MODID + ":" + FatCatMod.unko.getUnlocalizedName().substring(5), "inventory"));
		renderItem.getItemModelMesher().register(FatCatMod.brush, 0, new ModelResourceLocation(FatCatMod.MODID + ":" + FatCatMod.brush.getUnlocalizedName().substring(5), "inventory"));
		renderItem.getItemModelMesher().register(FatCatMod.furball, 0, new ModelResourceLocation(FatCatMod.MODID + ":" + FatCatMod.furball.getUnlocalizedName().substring(5), "inventory"));
		renderItem.getItemModelMesher().register(FatCatMod.feather_toy, 0, new ModelResourceLocation(FatCatMod.MODID + ":" + FatCatMod.feather_toy.getUnlocalizedName().substring(5), "inventory"));
	}
}
