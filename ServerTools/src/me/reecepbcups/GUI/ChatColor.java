package me.reecepbcups.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class ChatColor implements Listener, CommandExecutor {

	public String command, perm;
	public static String InvName;
	public static String FILENAME;
	public static Inventory ColorINV;
	public static FileConfiguration config;

	public Random rand = new Random();
	public static Boolean isEnabled;

	public static HashMap<String, String> ChatColorHash = new HashMap<String, String>();

	// 2, 12, 15 - REMOVED= pink1, brown, black
	public static List<String> Values = Arrays.asList("&fWhite", "&6Orange", "&dPink", "&bAqua","&eYellow","&aLime", "&dPink","&8Dark Gray","&7Gray", "&3Cyan","&5Purple","&1Blue","&fBrown","&2Green","&cRed","&0Black");
	public static List<String> EMPTY_LORE = new ArrayList<String>();
	public static List<String> RainbowColors;

	public static Main plugin;
	public ChatColor(Main instance) {
		plugin = instance;

		isEnabled = false;

		// v - also in the main class
		if (plugin.EnabledInConfig("Chat.ChatColor.Enabled")) {										
			isEnabled = true;

			// plugins/ServerTools/DATA
			plugin.createDirectory("DATA");
			FILENAME = File.separator + "DATA" + File.separator + "ChatColor.yml";
			plugin.createFile(FILENAME);
			config = plugin.getConfigFile(FILENAME);	

			command = "/chatcolor";
			perm = "Chatcolor.";	

			InvName = Main.LANG("CHATCOLOR_GUI");			
			RainbowColors = plugin.getConfig().getStringList("Chat.ChatColor.RainbowColors");
			
			// Creates
			initCreateInv();

			loadToMemory();
			
			plugin.getCommand("chatcolor").setExecutor(this);
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;

		p.openInventory(ColorINV);
		return true;
	}

	public static void initCreateInv() {
		ColorINV = Bukkit.createInventory(null, 2*9, InvName); 

		int loop = 0;
		for(int i: Arrays.asList(0,1,3,4,5,6,7,8,9,10,11,13,14)) { 	

			String name = Values.get(i);
			String color = name.substring(0,2);

			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add(color + Main.LANG("CHATCOLOR_INFO"));
			lore.add(color + Main.LANG("CHATCOLOR_COLOR") + name.substring(2,name.length()));
			lore.add(color + Main.LANG("CHATCOLOR_ACCESS"));
			lore.add("");
			lore.add(Main.LANG("CHATCOLOR_SELECT"));

			createDisplay(ColorINV, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)i ), 
					loop, color+"&l[!] "+ name, lore);

			loop+=1;
		}
		createDisplay(ColorINV, new ItemStack( Material.EXP_BOTTLE), loop, Main.LANG("CHATCOLOR_RAINBOW"), EMPTY_LORE);
		
		// light gray - fixes from people putting in items
		createDisplay(ColorINV, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)8 ), 14, " ", EMPTY_LORE);
		createDisplay(ColorINV, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)8 ), 15, " ", EMPTY_LORE);
		createDisplay(ColorINV, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)8 ), 16, " ", EMPTY_LORE);
		createDisplay(ColorINV, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)8 ), 17, " ", EMPTY_LORE);
		
	}

	public String getColor(String uuid) {
		if(ChatColorHash.containsKey(uuid)) {
			return ChatColorHash.get(uuid);
		} 		
		return null;		
	}
	
	public void setColor(Player p, String color, String msg) {

		// removes the "[!] " = 4 chars
		if(!color.equalsIgnoreCase("Rainbow")) {
			color = color.substring(4,color.length());
		}
		
		
		String uuid = p.getUniqueId().toString();
		String colorcode = "";

		switch (color) {
		case "White": colorcode="&f"; break;		
		case "Orange": colorcode="&6"; break;			
		case "Pink": colorcode="&d"; break;
		case "Aqua": colorcode="&b"; break;
		case "Yellow": colorcode="&e"; break;
		case "Lime": colorcode="&a"; break;
		case "Dark Gray": colorcode="&8"; break;
		case "Gray": colorcode="&7"; break;
		case "Cyan": colorcode="&3"; break;
		case "Purple": colorcode="&5"; break;
		case "Blue": colorcode="&1"; break;
		case "Green": colorcode="&2"; break;
		case "Red": colorcode="&c"; break;
		case "Rainbow": 
			rainbowFormat(msg);
			colorcode="Rainbow";
			break;
		}		
		//config.set("Data." + uuid, colorcode) && plugin.saveConfig(config, FILENAME);

		ChatColorHash.put(uuid, colorcode);

		String colorFormat = colorcode + color;		
		if(colorcode.equalsIgnoreCase("Rainbow")) {
			colorFormat = rainbowFormat(color);
		}		
		Util.coloredMessage(p, Main.LANG("CHATCOLOR_SET").replace("%color%", colorFormat));

	}

	public String rainbowFormat(String msg) {		
		String _final = "";

		for(String l : msg.split("")) { // hey!

			if(l != " ") {
				_final += RainbowColors.get(rand.nextInt(RainbowColors.size())) + l;
			} else {
				_final += " ";
			}		
		}
		return _final;

	}

	public void loadToMemory() {		
		if(config.getConfigurationSection("Data") != null) {
			for(String uuid : config.getConfigurationSection("Data").getKeys(false)) {
				ChatColorHash.put(uuid, config.getString("Data."+uuid));
			}
		}
	}

	public static void saveChatColorToFile() {		
		if(isEnabled) {			
			if(ChatColorHash.keySet().size() > 0) {
				for(String uuid : ChatColorHash.keySet()) {
					config.set("Data." + uuid, ChatColorHash.get(uuid));
				}
				plugin.saveConfig(config, FILENAME);
			}					
		}				
	}

	@EventHandler
	public void playerColoredChatEvent(AsyncPlayerChatEvent e) {				
		String color = getColor(e.getPlayer().getUniqueId().toString());
		
		if(color != null) {
			String OrginMsg = e.getMessage();
			String msg = "";
			
			if(color.equalsIgnoreCase("Rainbow")) {
				msg = rainbowFormat(OrginMsg);				
			} else {
				msg = color + OrginMsg;
			}
			e.setMessage(Util.color(msg));
		} 
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Boolean InvNameMatch = false;
		
		//if(Bukkit.getServer().getClass().getPackage().getName().contains("1_8")) {
		if(Util.isVersion1_8()) {
			if(event.getInventory().getName().equalsIgnoreCase(InvName)){
				InvNameMatch = true;
			}			
		} else {
			if(event.getView().getTitle().equalsIgnoreCase(InvName)) {
				InvNameMatch = true;
			}			
		}
		
		if(InvNameMatch) {

			ItemStack clicked = event.getCurrentItem(); 
			if(clicked == null || !clicked.hasItemMeta()) {return;}

			Player p = (Player) event.getWhoClicked(); 
			String DisplayName = clicked.getItemMeta().getDisplayName();

			if(DisplayName.equalsIgnoreCase(" ")) {
				event.setCancelled(true); // if its the last inv areas where there is no colors set
				return;
			}
			
			String chatpermission = perm + org.bukkit.ChatColor.stripColor(DisplayName);			
			if(p.hasPermission(chatpermission)){
				setColor(p, chatpermission.split(perm)[1], "");
			} else {
				p.sendMessage(Util.color("&4&l[!]&c You do not have permission to use &N" + chatpermission));
			}

			p.closeInventory();
			event.setCancelled(true);
		}
	}

	public static void createDisplay(Inventory inv, ItemStack itemStack, int Slot, String name, List<String> list) { // 

		ItemStack item = itemStack;
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Util.color(name));			

		ArrayList<String> Lore = new ArrayList<String>();		
		if(list.size() > 0) {
			for(String l : list) {					
				Lore.add(Util.color(l));
			}
		}
		meta.setLore(Lore);		
		
		item.setItemMeta(meta);

		inv.setItem(Slot, item); 

	}	
}