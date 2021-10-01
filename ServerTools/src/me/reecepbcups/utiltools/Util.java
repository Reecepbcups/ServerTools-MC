package me.reecepbcups.utiltools;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.Strings;


public class Util {

	static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

	private final static int CENTER_PX = 144; // 154 default
	public static void sendCenteredMessage(Player player, String message){	                                      
		player.sendMessage(centerMessage(message));
	}

	public static String centerMessage(String message){
		if(message == null || message.equals("")); //player.sendMessage("");
		message = ChatColor.translateAlternateColorCodes('&', message);
		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;
		for(char c : message.toCharArray()){
			if(c == '�'){
				previousCode = true;
				continue;
			}else if(previousCode == true){
				previousCode = false;
				if(c == 'l' || c == 'L'){
					isBold = true;
					continue;
				}else isBold = false;
			}else{
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}
		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while(compensated < toCompensate){
			sb.append(" ");
			compensated += spaceLength;
		}                                            
		return(sb.toString() + message);
	}

	//Util.color(" &8[&r" + getProgressBar(TokensBal, tokensForReward, 40, '|', "&d", "&7") + "&8]  " + "&7&o((" + TokensBal + " / " + tokensForReward + "&7&o))");
	public static String getProgressBar(int current, int max, int totalBars, char symbol, String string, String string2) {
		float percent = (float) current / max;
		if(percent > 1.0) { percent = (float) 1.0; }
		int progressBars = (int) (totalBars * percent);
		return Strings.repeat("" + string + symbol, progressBars) + Strings.repeat("" + string2 + symbol, totalBars - progressBars);
	}

	// Not tested yet (2/7)
	public void fillSlots(Inventory inv, ItemStack item) {
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR))
				inv.setItem(i, item); 
		} 
	}

	public static boolean isVersion1_8() {
		// 1.8 uses: e.getInventory().getName() && 1.9+ uses: e.getView().getTitle()
		return Bukkit.getServer().getClass().getPackage().getName().contains("1_8");
	}

	
	public static void removeItemFromPlayer(Player player, ItemStack item, int amount) {
		if(item.getAmount() == 1 && isVersion1_8()) {
			player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
			return;
		}						
		item.setAmount(item.getAmount() - amount);
	}

	public static String argsToSingleString(int StartPoint, String[] args) {
		String str = "";
		for (int i = StartPoint; i < args.length; i++) {
			if(i+1 < args.length) {
				str += args[i] + " ";
			} else {
				str += args[i];
			}
		}
		return str;
	}
	
	public static boolean isPluginInstalledOnServer(String PluginName, String ServerToolsModeleName) {
		if(Bukkit.getServer().getPluginManager().getPlugin(PluginName)!=null) {
			//Util.consoleMSG(PluginName + " found for " + ServerToolsModeleName);
			return true;
		} else {
			Util.consoleMSG("&cYou need &n'"+PluginName+"'&c installed to use &n"+ServerToolsModeleName+"&c with ServerTools!");
			return false;
		}
	}


	// exp
	public static void setTotalExperience(Player player, int exp) {
		player.setExp(0.0F);
		player.setLevel(0);
		player.setTotalExperience(0);
		int amount = exp;
		while (amount > 0) {
			int expToLevel = getExpAtLevel(player.getLevel());
			amount -= expToLevel;
			if (amount >= 0) {
				player.giveExp(expToLevel);
				continue;
			} 
			amount += expToLevel;
			player.giveExp(amount);
			amount = 0;
		} 
	}

	private static int getExpAtLevel(int level) {
		if (level <= 15)
			return 2 * level + 7; 
		if (level >= 16 && level <= 30)
			return 5 * level - 38; 
		return 9 * level - 158;
	}

	public static int getTotalExperience(Player player) {
		int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
		int currentLevel = player.getLevel();
		while (currentLevel > 0) {
			currentLevel--;
			exp += getExpAtLevel(currentLevel);
		} 
		if (exp < 0)
			exp = Integer.MAX_VALUE; 
		return exp;
	}

	public static String formatNumber(double number) {
		Format DECIMAL_FORMAT = new DecimalFormat("###,###.##");
		return DECIMAL_FORMAT.format(number);		
	}

	//	public static void drawBox(Location loc1, Location loc2, int radius) {	    
	//		World w = loc1.getWorld();
	//		int x = loc1.getBlockX();
	//		int z = loc1.getBlockZ();
	//		World w2 = loc2.getWorld();
	//		int x2 = loc2.getBlockX()+1;
	//		int z2 = loc2.getBlockZ()+1;		
	//		int thickness, iSize, kSize;
	//		if(radius<=16) {
	//			thickness=3;
	//			iSize=4;
	//			kSize=2;
	//		} else {
	//			thickness=2;
	//			iSize=9;
	//			kSize=3;
	//		}
	//		// import org.bukkit.Particle.DustOptions;
	//		DustOptions DUST = new Particle.DustOptions(Color.RED, thickness);
	////		DustOptions DUST2 = new Particle.DustOptions(Color.BLUE, thickness);
	//		for (int i = 60; i <= 255; i+=iSize) {
	//			// spawns straight up  
	//			// in between lines
	//			for(int k=0; k<=radius;k+=kSize) {
	//				// add worldborderapi red coloring
	//				w2.spawnParticle(Particle.REDSTONE, x2-k, i, z, 0, 16.0D, 0.0D, 0.0D, 10.0D, DUST, true);
	//				w2.spawnParticle(Particle.REDSTONE, x2, i, z+k, 0, 128.0D, 0.0D, 0.0D, 10.0D, DUST, true);				
	//				w2.spawnParticle(Particle.REDSTONE, x, i, z+k, 0, 128.0D, 0.0D, 0.0D, 10.0D, DUST, true);
	//				w2.spawnParticle(Particle.REDSTONE, x2-k, i, z2, 0, 256.0D, 0.0D, 0.0D, 10.0D, DUST, true);				
	//			}
	//		} 		
	//	}
	
	//world;4;68;-2;294.75;20.699995
	public static String locationToString(Player p) {  
		int x, y, z;
		Location pL = p.getLocation();

		World w = pL.getWorld();
		x = (int)pL.getX();
		y = (int)pL.getY();
		z = (int)pL.getZ();		
		float yaw = pL.getYaw();
		float pitch = pL.getPitch();

		return w.getName()+";"+x+";"+y+";"+z+";"+yaw+";"+pitch;
	}
	public static Location stringToLocation(String Location) {  
		String[] loc = Location.split(";");
		return new Location(Bukkit.getWorld(loc[0]), 
				Double.parseDouble(loc[1]), 
				Double.parseDouble(loc[2])+0.1, 
				Double.parseDouble(loc[3]), 
				Float.parseFloat(loc[4]), 
				Float.parseFloat(loc[5]));
	}
	
	

	public static boolean cooldown(final HashMap<String, Date> CooldownHash, final Integer SecondCooldown, final String PlayerName, final String CooldownMessage) { 
		//		private HashMap<String, Date> CooldownHash;
		//		
		//		// inside of the Constructor
		//		this.CooldownHash = new HashMap<String, Date>();

		//		Integer CooldownSeconds = 5;	
		
		//		if(!Util.cooldown(CooldownHash, CooldownSeconds, p.getName(), CooldownMSG)) {
		//			// User has cooldown	  
		//    		e.setCancelled(true);
		//    	} else {
		//    		// Thing to run if no cooldown  
		//    	}

		long CURRENT_TIME = new Date().getTime();

		if (CooldownHash.containsKey(PlayerName) && CooldownHash.get(PlayerName).getTime() >= CURRENT_TIME) {			
			Player p = Bukkit.getServer().getPlayer(PlayerName);
			Long timeLeft = ((CooldownHash.get(PlayerName).getTime() - CURRENT_TIME) / 1000);
			p.sendMessage(Util.color(CooldownMessage.replace("%timeleft%", timeLeft.toString())));
			//Util.console("bc return false. On cooldown");
			return false;

		} else {
			CooldownHash.remove(PlayerName); //Cooldown is over			
		}

		if(!(CooldownHash.containsKey(PlayerName))) {	
			long mil_cooldown = SecondCooldown * 1000;
			CooldownHash.put(PlayerName , new Date(CURRENT_TIME + mil_cooldown));	    	
		}

		return true;				
	}

//	public static boolean pvpCooldown(final HashMap<String, Date> CooldownHash, final Integer SecondCooldown, final String PlayerName, final String CooldownMessage) { 
//		// needs work
//		if (CooldownHash.containsKey(PlayerName) && CooldownHash.get(PlayerName).getTime() >= new Date().getTime()) {			
//			return false;
//
//		} else {
//			CooldownHash.remove(PlayerName); //Cooldown is over			
//		}
//
//		if(!(CooldownHash.containsKey(PlayerName))) {	
//			long mil_cooldown = SecondCooldown * 1000;
//			CooldownHash.put(PlayerName , new Date(new Date().getTime() + mil_cooldown));	    	
//		}
//
//		return true;				
//	}

	public static Long cooldownSecondsLeft(final HashMap<String, Date> CooldownHash, final Integer SecondCooldown, final String PlayerName) {
		long CURRENT_TIME = new Date().getTime();

		if (CooldownHash.containsKey(PlayerName) && CooldownHash.get(PlayerName).getTime() >= CURRENT_TIME) {			
			//Player p = Bukkit.getServer().getPlayer(PlayerName);
			Long timeLeft = ((CooldownHash.get(PlayerName).getTime() - CURRENT_TIME) / 1000);
			return timeLeft;
		} 

		return 0L;
	}

	public int stringToInt(String value){return Integer.parseInt(value);}
	public String intToString(int value){return Integer.toString(value);}

	private static final Pattern HEX_PATTERN = Pattern.compile("&(#\\w{6})");
	public static String color(String message) {		
		// doesnt work bc of 1.8 backwars compatibility and having to be loaded in first
//		Matcher matcher = HEX_PATTERN.matcher(ChatColor.translateAlternateColorCodes('&', message));
//		StringBuffer buffer = new StringBuffer();
//		while (matcher.find()) {
//	        matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(matcher.group(1)).toString());	        
//	    }
//		return matcher.appendTail(buffer).toString();	
		
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	public static List<String> color(List<String> list) {
		List<String> colored = new ArrayList<>();
		for (String s : list)
			colored.add(color(s)); 
		return colored;
	}
	
	
	public static void coloredMessage(Player player, String message) {		
		if(message.contains("\n")) {
			if(message.endsWith("\n")) {message+= " ";}
			
			for(String line : message.split("\n")) {
				player.sendMessage(color(line));
			}			
		} else {
			player.sendMessage(color(message));
		}
	}
	public static void coloredMessage(CommandSender sender, String message) {		
		if(message.contains("\n")) {
			if(message.endsWith("\n")) {message+= " ";}
			
			for(String line : message.split("\n")) {
				sender.sendMessage(color(line));
			}			
		} else {
			sender.sendMessage(color(message));
		}
	}

	public static void coloredBroadcast(String msg) {Bukkit.broadcastMessage(Util.color(msg));}
	


	public static String UUIDToPlayername(String uuid){		
		if(uuid.length() == 36 && Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() != null) {
			return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
		}
		return null;
	}


	public static void console(String command) {
		Bukkit.dispatchCommand(console, command);
	}
	public static void consoleMSG(String consoleMsg) {Bukkit.getServer().getConsoleSender().sendMessage(Util.color(consoleMsg));}

// DATE AND TIME UTILITIES
	public static final long SEC = 1000L;
	public static final long MIN = 60000L;
	public static final long HOUR = 3600000L;
	public static final long DAYS = 86400000L;
	public static String onPlaceholderRequest(String argument) {
		long timestamp;
		try {
			timestamp = Long.parseLong(argument) * 1000L;
		} catch (NumberFormatException e) {
			return null;
		} 
		long current = System.currentTimeMillis();
		if (timestamp > current) {
			timestamp -= current;
		} else {
			timestamp = current - timestamp;
		} 
		return formatTime(timestamp);
	}		  
	public static String formatTime(long timestamp) {
		long days = timestamp / 86400000L;
		long hours = timestamp % 86400000L / 3600000L;
		long minutes = timestamp % 3600000L / 60000L;
		long seconds = timestamp % 60000L / 1000L;
		StringBuilder formatted = new StringBuilder();
		if (days > 0L)
			formatted.append(days).append("d "); 
		if (hours > 0L)
			formatted.append(hours).append("h "); 
		if (minutes > 0L)
			formatted.append(minutes).append("m "); 
		if (seconds > 0L || formatted.length() == 0)
			formatted.append(seconds).append("s"); 
		return formatted.toString().trim();
	}
	
	
	
	// =================================== ITEMS =========================================
	public static ItemStack createItem(Material mat, int amt, int durability, String name) {
		ItemStack item = new ItemStack(mat, amt);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		//meta.setLore(lore); - List<String> lore
		if (durability != 0)
			item.setDurability((short)durability); 
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack createItemWithLore(Material mat, int amt, int durability, String name, List<String> lore) {
		ItemStack item = new ItemStack(mat, amt);
		ItemMeta meta = item.getItemMeta();				
		meta.setDisplayName(name);
		meta.setLore(lore);
		if (durability != 0)
			item.setDurability((short)durability); 
		item.setItemMeta(meta);
		return item;
	}
//	public static ItemStack createHead(String owner, String name, String... lore) {
//		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
//		SkullMeta meta = (SkullMeta)item.getItemMeta();
//		meta.setOwner(owner);
//		meta.setDisplayName(name);
//		List<String> l = new ArrayList<>();
//		String[] arrayOfString;
//		int j = (arrayOfString = lore).length;
//		for (int i = 0; i < j; i++) {
//			String s = arrayOfString[i];
//			l.add(s);
//		} 
//		meta.setLore(l);
//		item.setItemMeta((ItemMeta)meta);
//		return item;
//	}

	// ================================= CONFIG ==============================
	// put this in Main.class
	//	public FileConfiguration getConfigFile(String name) {
	//		return YamlConfiguration.loadConfiguration(new File(getDataFolder(), name));
	//	}
	//
	//	public void createDirectory(String DirName) {
	//		File newDir = new File(getDataFolder(), DirName.replace("/", File.separator));
	//		if (!newDir.exists()){
	//			newDir.mkdirs();
	//		}
	//	}
	//
	//	public void createConfig(String name) {
	//		File file = new File(getDataFolder(), name);
	//
	//		if (!new File(getDataFolder(), name).exists()) {
	//
	//			saveResource(name, false);
	//		}
	//
	//		@SuppressWarnings("static-access")
	//		FileConfiguration configuration = new YamlConfiguration().loadConfiguration(file);
	//		if (!file.exists()) {
	//			try {
	//				configuration.save(file);
	//			}			
	//			catch (IOException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//	}
	//
	//	public void createFile(String name) {
	//		File file = new File(getDataFolder(), name);
	//
	//		if (!file.exists()) {
	//			try {
	//				file.createNewFile();
	//			} catch(Exception e) {
	//				e.printStackTrace();
	//			}
	//		}
	//	}	
	//	public void saveConfig(FileConfiguration config, String name) {
	//		try {
	//			config.save(new File(getDataFolder(), name));
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}



	// ================================== INV =================================
	public static void remove(Inventory inv, Material mat, int amt) {
		int amount = 0;
		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = inv.getContents()).length;
		for (int i = 0; i < j; i++) {
			ItemStack item = arrayOfItemStack[i];
			if (item != null && item.getType() == mat)
				amount += item.getAmount(); 
		} 
		inv.remove(mat);
		if (amount > amt)
			inv.addItem(new ItemStack[] { new ItemStack(mat, amount - amt) }); 
	}

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException numberFormatException) {
			return false;
		} 
	}  

	public enum Pane {
		WHITE(0),
		ORANGE(1),
		MAGENTA(2),
		LIGHT_BLUE(3),
		YELLOW(4),
		LIME(5),
		PINK(6),
		GRAY(7),
		LIGHT_GRAY(8),
		CYAN(9),
		PURPLE(10),
		BLUE(11),
		BROWN(12),
		GREEN(13),
		RED(14),
		BLACK(15);	    
		private int value;	    
		Pane(int value) {
			this.value = value;
		}	    
		public int value() {
			return this.value;
		}
	}




	public static boolean isArmour(Material m) {
		return Enchantment.PROTECTION_ENVIRONMENTAL.canEnchantItem(new ItemStack(m));
	}
	public static boolean isWeapon(Material m) {
		return Enchantment.DAMAGE_ALL.canEnchantItem(new ItemStack(m));
	}

	public static boolean isTool(Material m) {
		return Enchantment.DIG_SPEED.canEnchantItem(new ItemStack(m));
	}

	public static boolean isType(Material m, String name) {
		// where name is DIAMOND, GOLD, IRON, LEATHER, CHAIN, SWORD, _AXE, PICKAXE
		return m.toString().contains(name);
	}





//	public static String getName(EntityType e) {
//		if (e.equals(EntityType.PIG_ZOMBIE))
//			return "Zombie Pigman"; 
//		if (!e.toString().contains("_"))
//			return String.valueOf(e.toString().substring(0, 1).toUpperCase()) + e.toString().substring(1).toLowerCase(); 
//		String[] split = e.toString().split("_");
//		String name = "";
//		String[] arrayOfString1;
//		int j = (arrayOfString1 = split).length;
//		for (int i = 0; i < j; i++) {
//			String s = arrayOfString1[i];
//			name = String.valueOf(name) + s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ";
//		} 
//		return name.trim();
//	}

//	public static EntityType getEntity(String e) {
//		if (e.equalsIgnoreCase("Zombie Pigman"))
//			return EntityType.PIG_ZOMBIE; 
//		e = e.replaceAll(" ", "_");
//		if (!e.contains("_"))
//			return EntityType.valueOf(e.toUpperCase()); 
//		String[] split = e.toString().split(" ");
//		String name = "";
//		String[] arrayOfString1;
//		int j = (arrayOfString1 = split).length;
//		for (int i = 0; i < j; i++) {
//			String s = arrayOfString1[i];
//			name = String.valueOf(name) + s.toUpperCase() + "_";
//		} 
//		return EntityType.valueOf(name.substring(0, name.length() - 1));
//	}



}

