package fatcat.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import fatcat.FatCatMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class CatSkinLoader implements IResourceManagerReloadListener
{
	private Map<String, String> skinMap;
	private List<String> skinTypes;

	public CatSkinLoader() {
		skinMap = new HashMap<String, String>();
		skinTypes = new ArrayList<String>();
		((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager) {
		this.rebuildSkinMap();
	}

	public Map<String, String> getSkinMap() {
		return skinMap;
	}

	public List<String> getSkinTypes() {
		return skinTypes;
	}
	
	private void rebuildSkinMap() {
		skinMap.clear();
		skinTypes.clear();
		initSkinMap();
	}

    private void initSkinMap() {
		ArrayList<String> files = new ArrayList<String>();
		URL path = DefaultResourcePack.class.getResource("/assets/fatcat/textures/models/cat/");
		String protocol = path.getProtocol();
		
		if ("file".equals(protocol)) {
			File modelDir = new File(path.getPath());
			addSkinFilePaths(files, modelDir);
//			System.out.println(files.toString());
		} else if ("jar".equals(protocol)) {
	        JarURLConnection jarUrlConnection = null;
	        JarFile jarFile = null;
	        try {
	        	try {
	        		jarUrlConnection = (JarURLConnection)path.openConnection();

	        		jarFile = jarUrlConnection.getJarFile();
	        		for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
	        			JarEntry entry = e.nextElement();
	        			files.add(entry.getName());
	        		}

	        	} finally {
	        		if (jarFile != null) {
	        			jarFile.close();
	        		}
	        	}
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
		} else {
			System.out.println("Error: unsupported protocol: " + protocol);
		}

		getResourcePackSkinFiles(files);
		detectSkinFiles(files, skinTypes);
	}
	public void getResourcePackSkinFiles(ArrayList<String> files) {
		ResourcePackRepository repo = Minecraft.getMinecraft().getResourcePackRepository();
		final List<ResourcePackRepository.Entry> entries = repo.getRepositoryEntries();
		final ResourceLocation cat_folder = new ResourceLocation("fatcat:textures/models/cat/");
        List<File> resource_pack_files = Arrays.asList(repo.getDirResourcepacks().listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				for (ResourcePackRepository.Entry entry : entries) {
					if (!pathname.getName().endsWith(".zip") || entry.getResourcePack().resourceExists(cat_folder)) {
						return pathname.getName().contains(entry.getResourcePackName());
					}
				}
				return false;
			}
		}));
        
        for (File file : resource_pack_files) {
        	if (file.isDirectory()) {
        		File assetDir = new File(file.getPath() + "/assets/fatcat/textures/models/cat/");
        		addSkinFilePaths(files, assetDir);
        	}
        	else if (file.getName().endsWith(".zip")) {
        		ZipFile zip = null;
				try {
					zip = new ZipFile(file);
					Enumeration<? extends ZipEntry> enu = zip.entries();
					while(enu.hasMoreElements()){
						ZipEntry element = ((ZipEntry)enu.nextElement());
						files.add(element.getName());
					}
				} catch (ZipException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		}
	}
 
	private void addSkinFilePaths(ArrayList<String> files, File modelDir) {
		for (File f : modelDir.listFiles()) {
			if (f.isDirectory()) {
				if (f.getName().contains("addon")) {
					for (File addon_dir: f.listFiles()) {
						if (addon_dir.isDirectory()) {
							for (File addon_skin : addon_dir.listFiles()) {
								files.add(addon_skin.toURI().toString());
							}
						}
					}
				}
				for (File skin : f.listFiles()) {
					files.add(skin.toURI().toString());
				}
			}
		}
	}

   
	private void detectSkinFiles(ArrayList<String> files, List<String> types) {
		Pattern integerRx = Pattern.compile(".*?/(\\d+)-.*\\.png$");
		Pattern nameRx = Pattern.compile(".*assets/fatcat/textures/models/cat/(.*\\.png)$");
		Pattern addonIdRx = Pattern.compile("addon/(.*?)/");
		for (String png : files) {
			Matcher m = nameRx.matcher(png);
//			System.out.println("m=<"+m+">,png=<"+png+">");
			if (m.find()) {
				String name = m.group(1);
				Matcher m1 = integerRx.matcher(name);
				if (m1.find()) {
					Integer id = Integer.parseInt(m1.group(1));
					if (name.contains("addon")) {
						Matcher m2 = addonIdRx.matcher(name);
						if (m2.find()) {
							String addonId = m2.group(1);
							String idStr = "addon-" + addonId + "-" + id.toString();
//							System.out.println("name=<"+name+">,i=<"+idStr+">");
							skinMap.put(idStr, "fatcat:textures/models/cat/" + name);
							types.add(idStr);
						}
					}
					else {
						if (name.contains("joke")) {
							id += 1000;
						} 
//						System.out.println("name=<"+name+">,i=<"+id.toString()+">");
						skinMap.put(id.toString(), "fatcat:textures/models/cat/" + name);
						types.add(id.toString());
					}
				}
			}
		}
		java.util.Collections.sort(types);
	}
}
