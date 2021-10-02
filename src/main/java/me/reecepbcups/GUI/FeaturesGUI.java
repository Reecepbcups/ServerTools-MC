package me.reecepbcups.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class FeaturesGUI implements Listener {

	private static Main plugin;
	private static Inventory featuresInv;
	private static FileConfiguration config;
	private String command, InvName, DefaultItemNameColor, DefaultItemIfNotSet;
	private int rows;
	 
	public FeaturesGUI(Main instance) {
		plugin = instance;

		if (plugin.enabledInConfig("FeaturesGUI.Enabled")) {
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);	
			
			plugin.createConfig("FeaturesGUI.yml");
			config = plugin.getConfigFile("FeaturesGUI.yml");		
			command = "/"+config.getString("Command");
			
			InvName = Util.color(config.getString("Name"));
			rows = config.getInt("Rows")*9;
			
			DefaultItemNameColor = config.getString("DefaultItemNameColor");
	 		DefaultItemIfNotSet = config.getString("DefaultItemIfNotSet");
	 		
	 		featuresInv = Bukkit.createInventory(null, rows, InvName);
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        
        if (!e.getMessage().toLowerCase().startsWith(command)){
            return;
        }
        
        // This auto updates it on every open
      	config = plugin.getConfigFile("FeaturesGUI.yml");
      		
 		Set<String> keys = config.getConfigurationSection("Items").getKeys(false);	
 		 		
 		int i = 0;
 		for(String key : keys) {
 			
 			String item = config.getString("Items."+key+".Item");		
 			//String name = config.getString("Items."+key+".Name");
 			String name = key;
 			List<String> lores = config.getStringList("Items."+key+".Lore");
 			
 			
 			if(name.startsWith("_")) name = name.substring(1);
 			
 			if(item == null) item = DefaultItemIfNotSet;

 			
 			if(!DefaultItemNameColor.equalsIgnoreCase("")) {
 				name = DefaultItemNameColor + name;
 			}
 			
 			createDisplay(featuresInv, Material.getMaterial(item.toUpperCase()), i, name, lores);
 			i+=1;
 		}
 		p.openInventory(featuresInv);
 		
 		e.setCancelled(true);
 		return;
        
        
    }

	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		// Player player = (Player) event.getWhoClicked(); 
		ItemStack clicked = event.getCurrentItem(); 
		//Inventory inventory = event.getInventory(); 
		
//		if (event.getInventory() == null || clicked == null || clicked.getType() == Material.AIR) {
//			return;
//		}
		
		Boolean InvNameMatch = false;
		String InvName = Util.color(config.getString("Name"));
		
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
			if(clicked == null) {
				return;
			}
			
			event.setCancelled(true);
		}
	}
	
	
	public static void createDisplay(Inventory inv, Material material, int Slot, String name, List<String> list) {
		ArrayList<String> Lore = new ArrayList<String>();
		
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Util.color(name));
				
		String DefaultLoreColor = config.getString("DefaultLoreColor");
		
		for(String l : list) {
			
			if(!DefaultLoreColor.equalsIgnoreCase("")) {
				l = DefaultLoreColor + l;
			}
						
			Lore.add(Util.color(l));
		}
		
		meta.setLore(Lore);
		item.setItemMeta(meta);
		 
		inv.setItem(Slot, item); 
		 
	}


	
}
