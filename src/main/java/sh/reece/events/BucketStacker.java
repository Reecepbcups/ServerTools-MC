package sh.reece.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class BucketStacker implements Listener{//, CommandExecutor {

	private static Main plugin;
	//private FileConfiguration config;
	private String Section;

	private List<Material> transMaterials;
	private WorldGuardPlugin wg;
	private SuperiorSkyblock superior;
	private Boolean worldguard, superiorskyblock;


	public BucketStacker(Main instance) {
		plugin = instance;

		Section = "Misc.BucketStacker";                
		if(plugin.enabledInConfig(Section+".Enabled")) {

			worldguard = false;
			superiorskyblock = false;

			//config = plugin.getConfigFile("config.yml");
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

			if(Main.MAINCONFIG.contains(Section+".worldguard") && 
					Main.MAINCONFIG.getString(Section+".worldguard").equalsIgnoreCase("true")) {
				worldguard = true;
				if(Util.isPluginInstalledOnServer("WorldGuard", "BucketStacker")) {
					wg = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
				}
			} 

			if(Main.MAINCONFIG.contains(Section+".superiorskyblock") && 
					Main.MAINCONFIG.getString(Section+".superiorskyblock").equalsIgnoreCase("true")) {				
				if(Util.isPluginInstalledOnServer("SuperiorSkyblock2", "BucketStacker")) {
					superiorskyblock = true;
					superior = (SuperiorSkyblock) Bukkit.getServer().getPluginManager().getPlugin("SuperiorSkyblock2");
				}
			}
						
			transMaterials = new ArrayList<>(
					Arrays.asList(new Material[] { // Material.BEETROOT_BLOCK
							Material.LONG_GRASS, Material.YELLOW_FLOWER, Material.RED_MUSHROOM, Material.RED_ROSE, Material.DOUBLE_PLANT, Material.BROWN_MUSHROOM, Material.NETHER_WARTS, Material.NETHER_STALK, Material.CROPS, Material.CARROT, 
							Material.POTATO, Material.AIR, 							
							// fixes issue with placing water on water
							Material.STATIONARY_WATER, Material.WATER, Material.LAVA, Material.STATIONARY_LAVA })
					);

		} 
	}


//	public void bucketUse(PlayerBucketEmptyEvent e) {
//		
//	}
//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void FillupEvent(PlayerBucketFillEvent e) {
//		if(e.getItemStack().getType() == Material.WATER_BUCKET) {
//			if(e.getItemStack().getAmount() == 2) {
//				e.getItemStack().setAmount(e.getItemStack().getAmount()+1);
//			}
//		}		
//	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onUse(PlayerInteractEvent e) {
		Block block = e.getClickedBlock();
		Player p = e.getPlayer();
		
		if (e.isCancelled() || e.getItem() == null) {
			return; //Util.consoleMSG("BucketPlace Was Canceld");
		}

		// idk why I added this, just broke it
//		Material iType = e.getItem().getType();
//		if (iType != Material.WATER_BUCKET || iType != Material.LAVA_BUCKET || iType != Material.BUCKET) {			
//			return;
//		}		
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getItem().getAmount() <= 1) {
			return; 
		}
		if (!transMaterials.contains(block.getType())) {
			block = block.getRelative(e.getBlockFace());
			
			// double checks that a block face does not contain block
			if (!transMaterials.contains(block.getType())) {
				return;
			}
		} 
		
		ItemStack theirItem = e.getItem();
		if (theirItem.getType() == Material.LAVA_BUCKET) {
			if(block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
				e.setCancelled(true);
				return;
			}

			if(canPlayerPlaceBucketAtBlock(p, block)) {
				block.setType(Material.LAVA);
				removeBucketFromInv(p, theirItem);				
			}

			e.setCancelled(true);

		} 
		if (theirItem.getType() == Material.WATER_BUCKET) {
			// block.getType() == Material.WATER || 
			if(block.getType() == Material.STATIONARY_WATER) {
				e.setCancelled(true);
				return;
			}
				

			if(canPlayerPlaceBucketAtBlock(p, block)) {
				block.setType(Material.WATER);
				removeBucketFromInv(p, theirItem);				
			}

			e.setCancelled(true);
		} 
	}
	
	public Boolean canPlayerPlaceBucketAtBlock(Player player, Block block) {
		if(superiorskyblock) {
			// Island is = superior.getGrid().getIslandAt(block.getLocation());
			SuperiorPlayer sp = superior.getPlayers().getSuperiorPlayer(player.getUniqueId());
			Island is = superior.getGrid().getIslandAt(block.getLocation());
			
			// player does not have bypass perm
			if(!player.hasPermission("superior.admin")) {
				if(is.isCoop(sp) || is.isMember(sp)) {
					return true;
				} else {
					Util.coloredMessage(player, "&c[!] You can only place buckets on your own island!");
					return false;
				}
			}			
		}
		
		if(worldguard) {
			if(!wg.canBuild(player, block)) {
				//Util.consoleMSG("BucketPlace You can not build here due to being worldguard ");
				Util.coloredMessage(player, "You can not place stacked buckets here!");
				return false;
			}
			return true;
		}
		
		return false;
	}
	
	public void removeBucketFromInv(Player p, ItemStack item) {
		item.setAmount(item.getAmount() - 1);
		if (p.getGameMode() != GameMode.CREATIVE) {			
			if(p.getInventory().firstEmpty() != -1) {
				p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.BUCKET, 1) });
			} else {
				p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.BUCKET, 1));
			}
		}
	}
}


