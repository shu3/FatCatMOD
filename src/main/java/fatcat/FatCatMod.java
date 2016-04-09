package fatcat;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import fatcat.gui.GuiStatusHandler;
import fatcat.model.CatSkinLoader;

@Mod(modid = FatCatMod.MODID, version = FatCatMod.VERSION)
public class FatCatMod {
    public static final String MODID = "fatcat";
    public static final String VERSION = "1.0.3";
	public static final int STATUS_GUI_ID = 0;
    public static Item egg;
    public static Item unko;
    public static Item brush;
    public static Item furball;
    public static Item feather_toy;

    /* デバッグモード */
	public static boolean DEBUG = true;
	
	// 育成モードをオフにするオプション
	public static boolean breeding_mode = true;
	
	// ロギング
	public static boolean logging = false;

    /** This is the starting index for all of our mod's item IDs */
    private static int modEntityIndex = 0;

	@Instance(MODID)
	public static FatCatMod instance;
	@SidedProxy(clientSide = "fatcat.ClientProxy", serverSide = "fatcat.CommonProxy")
	public static CommonProxy proxy;
	
	private Map<String, String> skinMap;
	private List<String> skinTypes;
	private CatSkinLoader skinLoader;

    @EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
    	egg = new ItemFatCatEgg().setUnlocalizedName("fatcat_egg");
    	unko = new ItemFatCatUnko().setUnlocalizedName("fatcat_unko");
    	brush = new ItemCatBrush().setUnlocalizedName("fatcat_brush");
    	furball = new ItemFurball().setUnlocalizedName("furball");
    	feather_toy = new ItemFeatherToy().setUnlocalizedName("fatcat_feather_toy");
    	GameRegistry.registerItem(egg, egg.getUnlocalizedName().substring(5));
    	GameRegistry.registerItem(unko, unko.getUnlocalizedName().substring(5));
    	GameRegistry.registerItem(brush, brush.getUnlocalizedName().substring(5));
    	GameRegistry.registerItem(furball, furball.getUnlocalizedName().substring(5));
    	GameRegistry.registerItem(feather_toy, feather_toy.getUnlocalizedName().substring(5));
    	
    	EntityRegistry.registerModEntity(EntityFatCat.class, "FatCat", ++modEntityIndex, this, 64, 10, true);
    	EntityRegistry.registerModEntity(EntityItemUnko.class, "FatCatUnko", ++modEntityIndex, this, 64, 10, true);

    	GameRegistry.addRecipe(
    			new ItemStack(brush, 1),
    			"BT ", "BT ", " T ",
    			'B', Blocks.hay_block, 'T', Items.stick);
    	GameRegistry.addRecipe(
    			new ItemStack(feather_toy, 1),
    			" F ", " F ", " T ",
    			'F', furball, 'T', Items.stick);
    	
    	// Get a fatcat egg via fishing.
    	FishingHooks.addTreasure(new WeightedRandomFishable(new ItemStack(egg), 1));
    	ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(egg, 1, 0), 1, 1, 15));
    	ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(egg, 1, 0), 1, 1, 15));
    	ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(new ItemStack(egg, 1, 0), 1, 1, 7));
    	
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());

    	config.load();
    	Property breeding_mode_property = config.get(Configuration.CATEGORY_GENERAL, "BreedingMode", true);
    	breeding_mode_property.comment = "Breeding MODE(true/false): FatCat status is fixed if you disable this option";
    	breeding_mode = breeding_mode_property.getBoolean(true);
    	Property logging_mode_property = config.get(Configuration.CATEGORY_GENERAL, "Logging", false);
    	logging_mode_property.comment = "logging for debug";
    	logging = logging_mode_property.getBoolean(false);
     	Property debug_property = config.get(Configuration.CATEGORY_GENERAL, "Debug", false);
    	debug_property.comment = "debugging mode for development";
    	DEBUG = debug_property.getBoolean(false);
    	config.save();
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	proxy.registerRenderers();
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiStatusHandler());
    }

	public List<String> getSkinTypes() {
		return skinLoader.getSkinTypes();
	}

	public Map<String, String> getSkinMap() {
		return skinLoader.getSkinMap();
	}
	
	public void setSkinLoader(CatSkinLoader loader) {
		this.skinLoader = loader;
	}
}