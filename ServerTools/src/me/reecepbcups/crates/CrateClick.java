package me.reecepbcups.crates;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class CrateClick implements Listener {

	private Main plugin;
	Inventory preview = Bukkit.createInventory(null, 6*9, Util.color("&lPreview Crate..."));
	public CrateClick(Main instance) {
		plugin = instance;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

		for(int slot : Arrays.asList(0,1,2,3,4,5,6,7,8, 9,18,27,36,45, 46,47,  51,52,53, 44,35,26,17)) {
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
			createDisplay(preview, item, slot, " ", new ArrayList<String>());
		}
	}

	@EventHandler
	public void playerClicksCrate(PlayerInteractEvent e) {
		// open crate
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {			
			//Util.consoleMSG("right clicked block " + e.getMaterial());			

			Location cLoc = e.getClickedBlock().getLocation();
			if(Crate.getCrateLocations().contains(cLoc)) {
				//Util.consoleMSG("Clicked on a crate at a location");

				String crate = Crate.getCrateAtLocation(cLoc);
				//Util.consoleMSG(crate);

				Player p = e.getPlayer();
				ItemStack handItem = p.getInventory().getItemInHand();
				if(handItem.isSimilar((Crate.getKey(crate)))) {
					//Util.consoleMSG("Player clicked with correct key!");				
					Util.removeItemFromPlayer(p, handItem, 1);

					FileConfiguration f = plugin.getConfigFile("crates"+File.separator+crate+".yml");				
					//GuiListener.openCrateInv(p, Util.color(f.getString("Name")));

				} else {
					Util.coloredMessage(p, "&c[!] You do not have a " + crate + " crate key!");
					p.setVelocity(p.getLocation().getDirection().multiply(-1));
					p.setVelocity(new Vector(p.getVelocity().getX(), 0.25D, p.getVelocity().getZ()));
				}
				
				e.setCancelled(true);				
			}			
		}
	} 

	HashMap<String, Integer> playerPageNumber = new HashMap<String, Integer>();

	@EventHandler // Player Preview Inv
	public void playerWantsPreview(PlayerInteractEvent e) {		
		if(e.getAction() == Action.LEFT_CLICK_BLOCK) {			
			//Util.consoleMSG("left clicked block (peview)" + e.getMaterial());			

			Location cLoc = e.getClickedBlock().getLocation();
			if(Crate.getCrateLocations().contains(cLoc)) {
				//Util.consoleMSG("Open preview of crate at location");

				String crate = Crate.getCrateAtLocation(cLoc);
				//FileConfiguration f = plugin.getConfigFile("crates"+File.separator+crate+".yml");				
				//GuiListener.openCrateInv(e.getPlayer(), Util.color(f.getString("Name")));

				playerPageNumber.put(e.getPlayer().getName(), 1);
				createPreviewGUI(e.getPlayer(), crate);

				e.setCancelled(true);
			}
		}		
	}


	List<Integer> allowedSlots = Arrays.asList(
			10,11,12,13,14,15,16,
			19,20,21,22,23,24,25,
			28,29,30,31,32,33,34,
			37,38,39,40,41,42,43
			);
	int pageSize = allowedSlots.size();


	public void createPreviewGUI(Player p, String CrateName) {	
		FileConfiguration f = plugin.getConfigFile("crates"+File.separator+CrateName+".yml");				
		Set<String> keys = f.getConfigurationSection("rewards").getKeys(false); // rare1, rare2, rare3 etc

		/*
		 * You already have a collection of towns, let's call that "list".
Let's say you also have an integer variable that is called "page", which is initialized with the value 0.
The inventory size is 54. The last line won't be used, as I place the control elements there in this example.

For any page, that is greater or equal than 0, loop this logic:
You get an index, that starts at 0, and is incremented every iteration.
If the index + page*45 (45 because 54-1*9, as the last line is unused) is greater than or equal to the size of your list, end the loop.
From list, get what is at (index + page*45), and place it in inventory slot index.

This will get you pages with 45 items per page. Adapt to your needs. 
You will most likely also need some items to control the page variable. 
Page should never be lower than 0 or greater than (size of list / 45) - 1.
		 */

		//int pages = (keys.size() / pageSize);		
		//Util.consoleMSG("Pages: " +pages);

		showPage(p, CrateName, keys);

		createDisplay(preview, new ItemStack(Material.FEATHER), 48, "&7<< Previous Page", new ArrayList<String>());
		createDisplay(preview, new ItemStack(Material.PAPER), 49, "&cCLOSE", new ArrayList<String>());


		p.openInventory(preview);
	}
	// player -> [CrateID: [keys1,keys2, keys3]]
	@SuppressWarnings("unchecked")
	public void showPage(Player p, String CrateName, Set<String> keys) {

		int page = playerPageNumber.get(p.getName());

		int slot = 0; 		
		for(String key : keys) {
			if(slot >= pageSize) {	
				createDisplay(preview, new ItemStack(Material.FEATHER), 50, "&7Next Page >>", new ArrayList<String>());
				continue;
			}	

			Material mat = (Material) getRewardObject(CrateName, key).get(0);
			ItemStack item = new ItemStack(mat);
			ItemMeta im = item.getItemMeta();
			im.setDisplayName((String) getRewardObject(CrateName, key).get(1));
			im.setLore((List<String>) getRewardObject(CrateName, key).get(2));
			item.setItemMeta(im);

			try {
				preview.setItem(allowedSlots.get(slot), item);
			} catch (Exception e) {
				continue;
			}

			if( slot <= pageSize) {
				slot++;
			} 
		}
	}


	@EventHandler
	public void playerClickInPreviewEvent(InventoryClickEvent e) {
		String Title = null;
		if(Util.isVersion1_8()) {
			Title = e.getInventory().getName();					
		} else {
			Title = e.getView().getTitle();						
		}	

		if(Title.equalsIgnoreCase(Util.color("&lPreview Crate..."))) {
			e.setCancelled(true);
		}

		if(e.getSlot() == 49) {
			e.getView().getPlayer().closeInventory();
		}

		if(e.getSlot() == 50) {
			String name = e.getWhoClicked().toString();
			int currentPage = playerPageNumber.get(name);
			playerPageNumber.put(name, currentPage+1);

			//showPage(e.getWhoClicked(), pagemenus.get(e.getWhoClicked().toString()));

			//e.getView().getPlayer().sendMessage("&cPages not yet added...");
		}
	}


	public List<Object> getRewardObject(String Cratename, String key) {
		FileConfiguration f = plugin.getConfigFile("crates"+File.separator+Cratename+".yml");
		Material mat = Material.valueOf(f.getString("rewards."+key+".Material").toUpperCase());
		String name = Util.color(f.getString("rewards."+key+".Name"));
		List<String> Lore = f.getStringList("rewards."+key+".Lore");

		List<Object> rewardItem = new ArrayList<Object>();
		rewardItem.add(mat);
		rewardItem.add(name);
		rewardItem.add(Util.color(Lore));
		return rewardItem;	
	}



	public static void createDisplay(Inventory inv, ItemStack itemStack, int Slot, String name, List<String> list) {
		ArrayList<String> Lore = new ArrayList<String>();

		ItemStack item = new ItemStack(itemStack);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Util.color(name));

		for(String l : list) {					
			Lore.add(Util.color(l));
		}

		meta.setLore(Lore);
		item.setItemMeta(meta);

		inv.setItem(Slot, item); 		 
	}


}