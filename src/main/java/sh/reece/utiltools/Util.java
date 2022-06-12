package sh.reece.utiltools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;

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


public class Util {

	static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

	private final static int CENTER_PX = 144; // 154 default
	public static void sendCenteredMessage(final Player player, final String message){
		player.sendMessage(centerMessage(message));
	}

	public static String centerMessage(String message){
		if(message == null || message.equals("")) {
			//player.sendMessage("");
		}
		message = Util.color(message);
		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;
		for (final char c : message.toCharArray()){
			if (c == '�'){
				previousCode = true;
				continue;
			} else if (previousCode == true){
				previousCode = false;
				if (c == 'l' || c == 'L'){
					isBold = true;
					continue;
				} else {
					isBold = false;
				}
			} else{
				final DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}
		final int halvedMessageSize = messagePxSize / 2;
		final int toCompensate = CENTER_PX - halvedMessageSize;
		final int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		final StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate){
			sb.append(" ");
			compensated += spaceLength;
		}                                            
		return sb + message;
	}

	//Util.color(" &8[&r" + getProgressBar(TokensBal, tokensForReward, 40, '|', "&d", "&7") + "&8]  " + "&7&o((" + TokensBal + " / " + tokensForReward + "&7&o))");
	public static String getProgressBar(final int current, final int max, final int totalBars, final char symbol, final String string, final String string2) {
		float percent = (float) current / max;
		if(percent > 1.0) { percent = (float) 1.0; }
		final int progressBars = (int) (totalBars * percent);
		return Strings.repeat("" + string + symbol, progressBars) + Strings.repeat("" + string2 + symbol, totalBars - progressBars);
	}

	// Not tested yet (2/7)
	public void fillSlots(final Inventory inv, final ItemStack item) {
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, item);
			}
		} 
	}

	public static boolean isVersion1_8() {
		// 1.8 uses: e.getInventory().getName() && 1.9+ uses: e.getView().getTitle()
		return Bukkit.getServer().getClass().getPackage().getName().contains("1_8");
	}

	
	public static void removeItemFromPlayer(final Player player, final ItemStack item, final int amount) {
		if(item.getAmount() == 1 && isVersion1_8()) {
			player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
			return;
		}						
		item.setAmount(item.getAmount() - amount);
	}

	public static String argsToSingleString(final int startPoint, final String[] args) {
		final StringBuilder str = new StringBuilder();
		for (int i = startPoint; i < args.length; i++) {
			if (i+1 < args.length) {
				str.append(args[i]).append(" ");
			} else {
				str.append(args[i]);
			}
		}
		return str.toString();
	}
	
	public static boolean isPluginInstalledOnServer(final String pluginName, final String moduleName) {
		if (Bukkit.getServer().getPluginManager().getPlugin(pluginName)!= null) {
			return true;
		} else {
			if(!moduleName.equalsIgnoreCase("N/A")){
				String msg = "You need &n'" + pluginName + "'&c installed to use &n" + moduleName;
				Util.consoleMSG("&c" + msg + "&c with ServerTools!");
			}			
			return false;
		}
	}

	public static boolean zipFolder(String sourceDirPath, String zipFilePath, String[] SkipDirectories) {
		if (new File(zipFilePath).exists()) {
			// Main.logging(zipFilePath + " already exists, skipping");
			return false;
		}
		Path p;
		try {
			p = Files.createFile(Paths.get(zipFilePath));		

			try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
				Path pp = Paths.get(sourceDirPath); // $PluginFolder/plugins/ServerTools
				// Main.logging("Path pp " + pp.toString());
				Files.walk(pp)
					.filter(path -> !Files.isDirectory(path))
					.forEach(path -> {					
						String relative = pp.relativize(path).toString();						

						if(SkipDirectories.length > 0){
							for(int i = 0; i < SkipDirectories.length; i++){
								if(relative.startsWith(SkipDirectories[i])){
									return;
								}
							}
						}

						// Main.logging("relative " + pp.relativize(path).toString());
						ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
						try {
							zs.putNextEntry(zipEntry);
							Files.copy(path, zs);
							zs.closeEntry();
						} catch (IOException e) {
						System.err.println(e);
						}
					});
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}

	public static void unzipFile(String zipFilePath, String destDir) {
		// unzip the zipFilePath and overwrite the destination directory
		//Open the file 
        try(ZipFile file = new ZipFile(zipFilePath)) {
            FileSystem fileSystem = FileSystems.getDefault();
            //Get file entries
            Enumeration<? extends ZipEntry> entries = file.entries();
             
            //We will unzip files in this folder
            String uncompressedDirectory = destDir + File.separator;
            // Files.createDirectory(fileSystem.getPath(uncompressedDirectory));
             
            //Iterate over entries
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

				String uncompressedFileName = uncompressedDirectory + entry.getName();
                Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);

				Files.createDirectories(Paths.get(uncompressedFilePath.getParent().toString()));
				
                    InputStream is = file.getInputStream(entry);
                    BufferedInputStream bis = new BufferedInputStream(is);
					
					// Files.copy(is, uncompressedFilePath, StandardCopyOption.REPLACE_EXISTING);
                    // Files.write(uncompressedFilePath, bis.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
					// Files.createFile(uncompressedFilePath);

                    FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
					// System.out.println("uncomFName " + uncompressedFileName);
					// System.out.println("create uncomFPath " + uncompressedFilePath);
                    // while (bis.available() > 0) { // always returened 0
                        
						// fileOutput.write(bis.readAllBytes());

						byte[] bytes = ByteStreams.toByteArray(bis);						
						fileOutput.write(bytes);
                    // }
                    fileOutput.close();
                    // System.out.println("Written: " + entry.getName());
                // }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
	}


	// exp
	public static void setTotalExperience(final Player player, final int exp) {
		player.setExp(0.0F);
		player.setLevel(0);
		player.setTotalExperience(0);
		int amount = exp;
		while (amount > 0) {
			final int expToLevel = getExpAtLevel(player.getLevel());
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

	private static int getExpAtLevel(final int level) {
		if (level <= 15) {
			return 2 * level + 7;
		}
		if (level <= 30) {
			return 5 * level - 38;
		}

		return 9 * level - 158;
	}

	public static int getTotalExperience(final Player player) {
		int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
		int currentLevel = player.getLevel();
		while (currentLevel > 0) {
			currentLevel--;
			exp += getExpAtLevel(currentLevel);
		} 
		if (exp < 0) {
			exp = Integer.MAX_VALUE;
		}
		return exp;
	}

	public static String formatNumber(final double number) {
		final Format decimalFormat = new DecimalFormat("###,###.##");
		return decimalFormat.format(number);
	}

	public static String locationToString(final Player p){
		return locationToString(p.getLocation());
	}

	public static String locationToString(final Location loc) {
		final int x;
		final int y;
		final int z;
		final Location location = loc;

		final World w = location.getWorld();
		x = (int) location.getX();
		y = (int) location.getY();
		z = (int) location.getZ();
		final float yaw = location.getYaw();
		final float pitch = location.getPitch();

		return w.getName()+";"+x+";"+y+";"+z+";"+yaw+";"+pitch;
	}
	public static Location stringToLocation(final String Location) {
		final String[] loc = Location.split(";");
		return new Location(Bukkit.getWorld(loc[0]), 
				Double.parseDouble(loc[1]), 
				Double.parseDouble(loc[2])+0.1, 
				Double.parseDouble(loc[3]), 
				Float.parseFloat(loc[4]), 
				Float.parseFloat(loc[5]));
	}
	
	

	public static boolean cooldown(final Map<String, Date> cooldownHash, final Integer secondCooldown, final String playerName, final String cooldownMessage) {
		final long currentTime = new Date().getTime();

		if (cooldownHash.containsKey(playerName) && cooldownHash.get(playerName).getTime() >= currentTime) {
			final Player p = Bukkit.getServer().getPlayer(playerName);
			final long timeLeft = (cooldownHash.get(playerName).getTime() - currentTime) / 1000;

			p.sendMessage(Util.color(cooldownMessage.replace("%timeleft%", String.valueOf(timeLeft))));
			return false;

		} else {
			cooldownHash.remove(playerName); //Cooldown is over
		}

		if (!(cooldownHash.containsKey(playerName))) {
			final long mil_cooldown = secondCooldown * 1000L;
			cooldownHash.put(playerName , new Date(currentTime + mil_cooldown));
		}

		return true;				
	}

	public static Long cooldownSecondsLeft(final HashMap<String, Date> CooldownHash, final Integer SecondCooldown, final String PlayerName) {
		final long CURRENT_TIME = new Date().getTime();

		if (CooldownHash.containsKey(PlayerName) && CooldownHash.get(PlayerName).getTime() >= CURRENT_TIME) {			
			//Player p = Bukkit.getServer().getPlayer(PlayerName);
			final Long timeLeft = ((CooldownHash.get(PlayerName).getTime() - CURRENT_TIME) / 1000);
			return timeLeft;
		} 

		return 0L;
	}

	public int stringToInt(final String value) {
		return Integer.parseInt(value);
	}

	public String intToString(final int value) {
		return Integer.toString(value);
	}

	private static final Pattern HEX_PATTERN = Pattern.compile("([&]?)?(#[a-fA-f0-9]{6})");
	private static final Class<net.md_5.bungee.api.ChatColor> COLOR_CLASS = net.md_5.bungee.api.ChatColor.class;
	/**
	 * @author Y2K_
	 * 
	 * Added Hex Support for versions >= 1.16
	 */
	public static String color(String message) {
		// doesnt work bc of 1.8 backwars compatibility and having to be loaded in first
		// Lol you sure bruh
		if(MinecraftVersion.getVersion().isAboveOrEqual(MinecraftVersion.V1_16_R1)){
			Matcher matcher = HEX_PATTERN.matcher(message);
			try{
				Method chatColorOf = COLOR_CLASS.getMethod("of", String.class);
				while (matcher.find()) {
					String color = message.substring(matcher.start(), matcher.end());
					
					// System.out.println(color);

					message = message.replace(color, chatColorOf.invoke(COLOR_CLASS, matcher.group(0).replace("&#", "#")) + "");
					matcher = HEX_PATTERN.matcher(message);
				}
			} catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
				// do nothing
			}
		}
		if(message == null){
			message = "SERVERTOOLS_MESSAGE_NULL_ISSUE";
			consoleMSG("NULL ERROR: ");
			throw new NullPointerException("Null Message");
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	public static List<String> color(final List<String> list) {
		final List<String> colored = new ArrayList<>();
		for (final String s : list) {
			colored.add(color(s));
		}
		return colored;
	}

	public static void coloredMessage(final CommandSender sender, String message) {
		if (message.contains("\n")) {
			if (message.endsWith("\n")) {message+= " ";}
			
			for (final String line : message.split("\n")) {
				sender.sendMessage(color(line));
			}			
		} else {
			sender.sendMessage(color(message));
		}
	}

	public static void coloredBroadcast(final String msg) {
		Bukkit.broadcastMessage(Util.color(msg));
	}

	public static void console(final String command) {
		Bukkit.dispatchCommand(console, command);
	}

	public static void consoleMSG(final String consoleMsg) {
		Bukkit.getServer().getConsoleSender().sendMessage(Util.color(consoleMsg));
	}

// DATE AND TIME UTILITIES
	public static final long SEC = 1000L;
	public static final long MIN = 60000L;
	public static final long HOUR = 3600000L;
	public static final long DAYS = 86400000L;
	public static String placeholderTimeRequest(final String argument) {
		long timestamp;
		try {
			timestamp = Long.parseLong(argument) * 1000L;
		} catch (final NumberFormatException e) {
			return null;
		} 
		final long current = System.currentTimeMillis();
		if (timestamp > current) {
			timestamp -= current;
		} else {
			timestamp = current - timestamp;
		} 
		return formatTime(timestamp);
	}		  
	public static String formatTime(final long timestamp) {
		final long days = timestamp / 86400000L;
		final long hours = timestamp % 86400000L / 3600000L;
		final long minutes = timestamp % 3600000L / 60000L;
		final long seconds = timestamp % 60000L / 1000L;
		final StringBuilder formatted = new StringBuilder();
		if (days > 0L) {
			formatted.append(days).append("d ");
		}
		if (hours > 0L) {
			formatted.append(hours).append("h ");
		}
		if (minutes > 0L) {
			formatted.append(minutes).append("m ");
		}
		if (seconds > 0L || formatted.length() == 0) {
			formatted.append(seconds).append("s");
		}
		return formatted.toString().trim();
	}
	
	
	
	// =================================== ITEMS =========================================
	public static ItemStack createItem(final Material mat, final int amt, final int durability, final String name) {
		final ItemStack item = new ItemStack(mat, amt);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		//meta.setLore(lore); - List<String> lore
		if (durability != 0) {
			item.setDurability((short)durability);
		}
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack createItemWithLore(final Material mat, final int amt, final int durability, final String name, final List<String> lore) {
		final ItemStack item = new ItemStack(mat, amt);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		if (durability != 0) {
			item.setDurability((short)durability);
		}
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
	public static void remove(final Inventory inv, final Material mat, final int amt) {
		int amount = 0;
		final ItemStack[] arrayOfItemStack;
		final int j = (arrayOfItemStack = inv.getContents()).length;
		for (int i = 0; i < j; i++) {
			final ItemStack item = arrayOfItemStack[i];
			if (item != null && item.getType() == mat) {
				amount += item.getAmount();
			}
		} 
		inv.remove(mat);
		if (amount > amt) {
			inv.addItem(new ItemStack(mat, amount - amt));
		}
	}

	public static boolean isInt(final String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (final NumberFormatException numberFormatException) {
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
		private final int value;
		Pane(final int value) {
			this.value = value;
		}	    
		public int value() {
			return value;
		}
	}




	public static boolean isArmour(final Material m) {
		return Enchantment.PROTECTION_ENVIRONMENTAL.canEnchantItem(new ItemStack(m));
	}
	public static boolean isWeapon(final Material m) {
		return Enchantment.DAMAGE_ALL.canEnchantItem(new ItemStack(m));
	}

	public static boolean isTool(final Material m) {
		return Enchantment.DIG_SPEED.canEnchantItem(new ItemStack(m));
	}

	public static boolean isType(final Material m, final String name) {
		// where name is DIAMOND, GOLD, IRON, LEATHER, CHAIN, SWORD, _AXE, PICKAXE
		return m.toString().contains(name);
	}

	public static boolean isCompatable(String version){
		boolean compatable = false;
		if(version.contains("1.16") || version.contains("1.17") || version.contains("1.18")){
			compatable = true;
		}
		return compatable;
	}
}

