package sh.reece.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.md_5.bungee.api.ChatColor;

public class _aClickGlassBreak_ToDo {

	// Configuration File Functions
//		public void loadConfig() {
//			// make sure to have config.yml in your root directory
//			saveDefaultConfig();
//			getConfig().options().copyDefaults(true);
//			saveConfig();		
//			loadBlocksToAllow();
//		}
//		
//		private void loadBlocksToAllow() {
//			for (String s : getConfig().getStringList("Blocks")) {
//				Material.matchMaterial(s);
//			}
//			
//		}
//		 
//		boolean CanPlayerBuild(Player player, Block block) {
//			return getWorldGuard().canBuild(player, block);
//		}
//
//		// Strings (other file > plugin.loadString("message.help"))
//		public String loadString (String config_value){
//			return ChatColor.translateAlternateColorCodes('&', getConfig().getString(config_value));			
//		}
//	
	
//	private WorldGuardPlugin getWorldGuard() {
//	    Plugin wg = getServer().getPluginManager().getPlugin("WorldGuard");
//
//	    // WorldGuard may not be loaded
//	    if (wg == null || !(wg instanceof WorldGuardPlugin)) {
//	        //May disable Plugin
//	        return null; // Maybe you want throw an exception instead
//	    }
//
//	    return (WorldGuardPlugin) wg;
//	}
	
	/*
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerEvent(PlayerInteractEvent e) {
		
		// if other plugin overriden - such as skyblock or worldguard
		if(e.isCancelled()) {
			e.getPlayer().sendMessage("You can not place this here!");
			e.setCancelled(true);
			return;
		}
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			Block b = e.getClickedBlock();
			//p.sendMessage(ChatColor.GREEN + "You have clicked the block: " + b.getType());
			
			// for type of block that you can use the "Item" on (default string)
			for (String blocktypes : plugin.getConfig().getStringList("Blocks")) {
				// if that block you click is in the list of blocks you can click on
				if(b.getType().toString().contains(blocktypes)) {
					String Item = plugin.loadString("Material").toUpperCase();

					if(p.getInventory().getItemInHand().getType().equals(Material.valueOf(Item))) {
						//p.sendMessage(ChatColor.GREEN + b.getType() + " - " +  e.getClickedBlock().getData());

						// If WorldGuard Support Enabled
						if (plugin.loadString("enable-worldguard-support").contentEquals("true")) {
							if (!(plugin.CanPlayerBuild(p, b))) {
								p.sendMessage(plugin.loadString("WorldGuardCantBreakHere"));
								e.setCancelled(true);
								return;
							} 

						} 
							
						doAction(p, b);
						


					} 
				}


			}			

		}

	}		
	
	public void doAction(Player p, Block b) {
		@SuppressWarnings("deprecation")
		ItemStack itemStack = new ItemStack(b.getType(), 1, (Byte) b.getData());
		p.getInventory().addItem(itemStack);
		b.setType(Material.AIR);				
	}
	*/
	
}
