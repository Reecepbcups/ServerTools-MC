// package sh.reece.crates;

// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Set;

// import org.bukkit.Bukkit;
// import org.bukkit.Location;
// import org.bukkit.Material;
// import org.bukkit.World;
// import org.bukkit.configuration.file.FileConfiguration;
// import org.bukkit.configuration.file.YamlConfiguration;
// import org.bukkit.enchantments.Enchantment;
// import org.bukkit.inventory.ItemStack;
// import org.bukkit.inventory.meta.ItemMeta;

// import sh.reece.tools.Main;
// import sh.reece.utiltools.Util;

// /*
//  * TODO
//  * Notes to self
//  * Make this plugin be a working crate plugin, and use no statics.
//  * No animations or crate menu, but there will need to be a preview menu.
//  * Add virtual keys for offline players?
//  * Needs to be added to the config for toggling off and on, copy from Tropical
//  */


// /*
//  * name: TropicalCrates
// version: 1.0.3
// author: Reecepbcups
// main: sh.reece.tropicalcrates.Main
// description: Crate plugin made custom for Tropical
// #softdepend: [LuckPerms, Vault, PlaceHolderAPI, TAB, WorldGuard]

// commands:
//   crate:
//     description: Used for admin crate commands 
//     usage: Syntax error!
//     aliases: [crates, cc]
    
    
//     config.yml:
    
//     AdminPermission: tropicalcrates.admin
    
//  */




// public class Crate {

// 	private static HashMap<String, ItemStack> CrateKeys = new HashMap<String, ItemStack>();
	
// 	// {"&8Vote Crate Chest" -> vote}. This way on click in inv, it knows player is in the vote crate
// 	private static HashMap<String, String> crateNameToID = new HashMap<String, String>();
	
// 	// {Location -> vote}
// 	private static HashMap<Location, String> CrateLocations = new HashMap<Location, String>();

// 	// https://gamedev.stackexchange.com/questions/162976/how-do-i-create-a-weighted-collection-and-then-pick-a-random-element-from-it
// 	// {crate: ["cmd" 1.0, ]}.getRandom()
// 	public static HashMap<String, WeightedRandom<String>> crateCommands = new HashMap<String, WeightedRandom<String>>();
	
	
	
// 	private Main plugin;
	
// 	private static String ADMIN_PERMISSION;
// 	public Crate(Main plugin) {
// 		this.plugin = plugin;
// //		new CrateBreak(this);
// //		new CrateClick(this);
// //		new CratesCMD(this);
		
// //		if(doesDirExists("crates")) {
// //			// creates crates dir if not already done
// //			createDirectory("crates");
// //			// not sure if this works on linux??
// //			plugin.saveResource("crates\\vote.yml", false);
// //			
// //		} else {
// //			// here as well incase it is, we just dont want to run it 2 times
// //			createDirectory("crates");
// //		}	
// //		this.loadCratesIntoHashes();
// //		ADMIN_PERMISSION = getConfig().getString("AdminPermission");
// 	}
	
// 	private void loadCratesIntoHashes() {
// 		// creates the keys and itemstacks used for them
// 		for (File crate : this.getDirectoryFiles("crates")) {
			
// 			FileConfiguration _crate = configUtils.getConfigFile("crates/"+crate.getName());			
// 			Material mat = Material.valueOf(_crate.getString("Key.Material"));
// 			if(mat == null) {
// 				mat = Material.NETHER_STAR;
// 			}
			
// 			String nameOfCrate = crate.getName().replace(".yml", ""); // "vote"
// 			String keyName = Util.color(_crate.getString("Key.Name")); // '&7Basic Crate Key'
// 			String CrateGUIName = Util.color(_crate.getString("Name")); // "&lVote Crate Chest"						
			
// 			crateNameToID.put(CrateGUIName, nameOfCrate);
			
// 			List<String> keyLore = Util.color(_crate.getStringList("Key.Lore"));						
			
// 			// crate key
// 			ItemStack keyItem = new ItemStack(mat, 1);
// 			ItemMeta im = keyItem.getItemMeta();
// 			im.setDisplayName(keyName);
// 			im.setLore(keyLore);
// 			keyItem.setItemMeta(im);
// 			keyItem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
			
// 			if(_crate.contains("CrateLocations")) {				
// 				// CrateLocations:
// 				//   - world, -1.0, 64.0, 20.0	
// 				// adds all crate locations to the hashmap on load
// 				for(String locString : _crate.getStringList("CrateLocations")) {										
// 					CrateLocations.put(stringToLocation(locString), nameOfCrate);
// 				}
// 			}			
			
// 			CrateKeys.put(nameOfCrate, keyItem);
// 			loadSingleCratesitems(nameOfCrate);
			
// 			//Util.consoleMSG("Loaded Crate: " + crate.getName());
// 		}		
// 	}
	
// 	// loads all crate keys & their weightings
// 	public void loadSingleCratesitems(final String CrateName) {		
// 		// Moch-up
// 		// HashMap<String, WeightedRandom<String>> somehash = new HashMap<String, WeightedRandom<String>>();
// //		WeightedRandom<String> itemDrops = new WeightedRandom<>();		
// //				
// //		itemDrops.addEntry("10 Gold",  5.0);
// //		itemDrops.addEntry("Sword",   20.0);
// //		itemDrops.addEntry("Shield",  1000.0);
// //		itemDrops.addEntry("Armor",   20.0);
// //		itemDrops.addEntry("Potion",  10.0);		
// //		somehash.put("test", itemDrops);		
// //		// drawing random entries from it
// //		for (int i = 0; i < 20; i++) {
// //		    Main.logging(somehash.get("test").getRandom());
// //		}		
// 		WeightedRandom<String> itemDropRewards = new WeightedRandom<>();			
// 		FileConfiguration crateConfig = configUtils.getConfigFile("crates"+File.separator+CrateName+".yml");		
// 		for(String key : crateConfig.getConfigurationSection("rewards").getKeys(false)) {
// 			// 'rare1', 'rare2', etc
// 			//Util.consoleMSG("Added key '"+key+"' to crate " + CrateName);
// 			itemDropRewards.addEntry(key, crateConfig.getDouble("rewards."+key+".Chance"));
// 		}		
// 		crateCommands.put(CrateName, itemDropRewards);			
// 	}
	

// 	public static String locationToStringFormat(Location l) {					
// 		return l.getWorld().getName()+", "+l.getX()+", "+l.getY()+", "+l.getZ();
// 	}
	
// 	public static Location stringToLocation(String location) {
// 		String[] loc = location.split(", ");
// 		World w = Bukkit.getWorld(loc[0]);
// 		if(w != null) {
// 			return new Location(w, 
// 					Double.valueOf(loc[1]), 
// 					Double.valueOf(loc[2]), 
// 					Double.valueOf(loc[3]));
// 		}			
// 		return null;
// 	}
	
	
// //	public void onDisable() {
// //		this.saveDefaultConfig();
// //		getServer().getScheduler().cancelTasks(this);
// //		
// //		// close any invs on reload
// //		for(String pInInv : GuiListener.getPlayersInInvs()) {
// //			Player p = Bukkit.getPlayer(pInInv);
// //			// give random prize to player from that crate
// //			p.closeInventory();
// //		}
// //		
// //	}


// 	// getters and setters
// 	public static WeightedRandom<String> getCrateWeightedRewards(String Cratename){
// 		return crateCommands.get(Cratename);
// 	}
	
// 	public static String getAdminPerm() {
// 		return ADMIN_PERMISSION;
// 	}
	
// 	public String crateNameToID(String Title) {
// 		return crateNameToID.get(Title);		
// 	}
// 	public static Set<String> getCrateTitles() {
// 		return crateNameToID.keySet();		
// 	}
// 	public static ItemStack getKey(String keyname) {
// 		return CrateKeys.get(keyname);
// 	}	
// 	public static Set<String> getKeys() {
// 		return CrateKeys.keySet();
// 	}
// 	public static Set<Location> getCrateLocations() {
// 		return CrateLocations.keySet(); // Change to a CrateManager later
// 	}	
// 	public static void removeCrateAtLocation(Location loc) {
// 		CrateLocations.remove(loc);	
// 		// remove from config in CratebreakEvent
// 	}	
// 	public static String getCrateAtLocation(Location loc) {
// 		return CrateLocations.get(loc);
// 	}
// 	public static void addCrateLocation(String keyname, Location loc) {
// 		CrateLocations.put(loc, keyname);
// 	}
// 	public static Boolean isCrateGUI(String GUITitle) {
// 		if(crateNameToID.keySet().contains(sh.reece.utiltools.Util.color(GUITitle))) {
// 			return true;
// 		}
// 		return false;
// 	}
	
	
// 	// Configuration File Functions
// 		public FileConfiguration getConfigFile(String name) {
// 			return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), name));
// 		}

// 		public void createDirectory(String DirName) {
// 			File newDir = new File(plugin.getDataFolder(), DirName.replace("/", File.separator));
// 			if (!newDir.exists()){
// 				newDir.mkdirs();
// 			}
// 		}
		
		
// 		public void saveFileFromPluginToOther(String FileInjar, String outputfolder) {
// 			InputStream is = getClass().getResourceAsStream(FileInjar);

// 			try {
// 				OutputStream os = new FileOutputStream(outputfolder);
// 				byte[] buffer = new byte[4096];
// 				int length;
// 				while ((length = is.read(buffer)) > 0) {		    
// 					os.write(buffer, 0, length);			
// 				}
// 				os.close();
// 				is.close();
// 			} catch (IOException e) {
// 				e.printStackTrace();
// 			}
// 		}
		
		
// 		public Boolean doesDirExists(String DirName) {
// 			File DIR = new File(plugin.getDataFolder(), DirName.replace("/", File.separator));
// 			if (!DIR.exists()){
// 				return true;
// 			}
// 			return false;
// 		}
		
// 		public File[] getDirectoryFiles(String DirName) { 
// 			return new File(plugin.getDataFolder().getAbsolutePath()+File.separator+DirName).listFiles();	
// 		}

// 		public void createConfig(String name) {
// 			File file = new File(plugin.getDataFolder(), name);

// 			if (!new File(plugin.getDataFolder(), name).exists()) {

// 				plugin.saveResource(name, false);
// 			}

// 			@SuppressWarnings("static-access")
// 			FileConfiguration configuration = new YamlConfiguration().loadConfiguration(file);
// 			if (!file.exists()) {
// 				try {
// 					configuration.save(file);
// 				}			
// 				catch (IOException e) {
// 					e.printStackTrace();
// 				}
// 			}
// 		}

// 		public void createFile(String name) {
// 			File file = new File(plugin.getDataFolder(), name);

// 			if (!file.exists()) {
// 				try {
// 					file.createNewFile();
// 				} catch(Exception e) {
// 					e.printStackTrace();
// 				}
// 			}
// 		}

// 		public void saveConfig(FileConfiguration config, String name) {
// 			try {
// 				config.save(new File(plugin.getDataFolder(), name));
// 			} catch (IOException e) {
// 				e.printStackTrace();
// 			}
// 		}
	
	
// }
