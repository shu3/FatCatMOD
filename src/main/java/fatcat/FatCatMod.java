package fatcat;

import fatcat.entitiy.EntityFatCat;
import fatcat.entitiy.EntityItemTurd;
import fatcat.gui.GuiStatusHandler;
import fatcat.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod(modid = FatCatMod.ID)
public class FatCatMod
{

    public static final String ID = "fatcat";

    public static final int STATUS_GUI_ID = 0;

    public static Item egg;
    public static Item turd;
    public static Item brush;
    public static Item furBall;
    public static Item featherToy;

    /* デバッグモード */
    public static boolean DEBUG = true;

    // 育成モードをオフにするオプション
    public static boolean breeding_mode = true;

    // ロギング
    static boolean logging = false;

    @Mod.Instance(ID)
    public static FatCatMod instance;

    @SidedProxy(clientSide = "fatcat.ClientProxy", serverSide = "fatcat.CommonProxy")
    public static CommonProxy proxy;

    /**
     * This is the starting index for all of our mod's item IDs
     */
    private static int modEntityIndex = 0;
    static final File fatcatResourceDir = new File("fatcatResource");
    public Map<Integer, String> skinMap;
    public List<Integer> skinTypes;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        event.getModMetadata().version = event.getVersionProperties().getProperty("fatcat.version");

        if (!fatcatResourceDir.exists()) fatcatResourceDir.mkdirs();

        egg = new ItemFatCatEgg().setUnlocalizedName(ID + "_" + "egg");
        turd = new ItemFatCatTurd().setUnlocalizedName(ID + "_" + "turd");
        brush = new ItemCatBrush().setUnlocalizedName(ID + "_" + "brush");
        furBall = new ItemFurBall().setUnlocalizedName(ID + "_" + "furBall");
        featherToy = new ItemFeatherToy().setUnlocalizedName(ID + "_" + "featherToy");

        GameRegistry.registerItem(egg, egg.getUnlocalizedName().substring(5));
        GameRegistry.registerItem(turd, turd.getUnlocalizedName().substring(5));
        GameRegistry.registerItem(brush, brush.getUnlocalizedName().substring(5));
        GameRegistry.registerItem(furBall, furBall.getUnlocalizedName().substring(5));
        GameRegistry.registerItem(featherToy, featherToy.getUnlocalizedName().substring(5));

        EntityRegistry.registerModEntity(EntityFatCat.class, "FatCat", ++modEntityIndex, this, 64, 10, true);
        EntityRegistry.registerModEntity(EntityItemTurd.class, "FatCatTurd", ++modEntityIndex, this, 64, 10, true);

        GameRegistry.addRecipe(new ItemStack(brush, 1),
                "BT ",
                "BT ",
                " T ",
                'B', Blocks.hay_block, 'T', Items.stick);
        GameRegistry.addRecipe(new ItemStack(featherToy, 1),
                " F ",
                " F ",
                " T ",
                'F', furBall, 'T', Items.stick);

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

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiStatusHandler());
        List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "defaultResourcePacks");
        defaultResourcePacks.add(new FatCatResourcePack());
        initSkinMap();
    }

    private void initSkinMap()
    {
        skinMap = new HashMap<>();
        ArrayList<String> files = new ArrayList<>();
        URL path = DefaultResourcePack.class.getResource("/assets/fatcat/textures/models/cat/");
        String protocol = path.getProtocol();

        if ("file".equals(protocol))
        {
            File modelDir = new File(path.getPath());
            for (File f : modelDir.listFiles())
            {
                if (f.isDirectory())
                {
                    for (File skin : f.listFiles())
                    {
                        files.add(skin.toURI().toString());
                    }
                }
            }
//			System.out.println(files.toString());
        }
        else if ("jar".equals(protocol))
        {
            JarURLConnection jarUrlConnection;
            try
            {
                jarUrlConnection = (JarURLConnection) path.openConnection();
                try (JarFile jarFile = jarUrlConnection.getJarFile())
                {
                    for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();)
                    {
                        JarEntry entry = e.nextElement();
                        files.add(entry.getName());
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Error: unsupported protocol: " + protocol);
        }
        for (File file : fatcatResourceDir.listFiles())
        {
            String name = file.getName();
            files.add(name);
        }
        skinTypes = detectSkinFiles(files);
    }

    private ArrayList<Integer> detectSkinFiles(ArrayList<String> files)
    {
        ArrayList<Integer> types = new ArrayList<>();
        Pattern integerRx = Pattern.compile(".*?/(\\d+)-.*\\.png$");
        Pattern nameRx = Pattern.compile(".*assets/fatcat/textures/models/cat/(.*\\.png)$");
        for (String png : files)
        {
            Matcher m = nameRx.matcher(png);
//			System.out.println("m=<"+m+">,png=<"+png+">");
            if (m.find())
            {
                String name = m.group(1);
                Matcher m1 = integerRx.matcher(name);
                if (m1.find())
                {
                    Integer i = Integer.parseInt(m1.group(1));
                    if (name.contains("joke"))
                    {
                        i += 1000;
                    }
//					System.out.println("name=<"+name+">,i=<"+i.toString()+">");
                    skinMap.put(i, "fatcat:textures/models/cat/" + name);
                    types.add(i);
                }
            }
            else
            {
                Integer i = Integer.parseInt(png.substring(0, 3));
                if (i < 97)
                {
                    proxy.log("detectSkinFiles() : Number that identifies the texture should be 96 and more. but found : <%s>", png);
                    continue;
                }
                skinMap.put(i, "fatcat:" + png);
                types.add(i);
            }
        }
        java.util.Collections.sort(types);
        return types;
    }

}