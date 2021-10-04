// package sh.reece.crates;

// import java.io.File;
// import java.util.List;

// import org.bukkit.Bukkit;
// import org.bukkit.Location;
// import org.bukkit.configuration.file.FileConfiguration;
// import org.bukkit.entity.Player;
// import org.bukkit.event.EventHandler;
// import org.bukkit.event.Listener;
// import org.bukkit.event.block.BlockBreakEvent;

// import sh.reece.tools.Main;
// import sh.reece.utiltools.Util;

// public class CrateBreak implements Listener {

// 	private Main plugin;
// 	public CrateBreak(Main instance) {
// 		plugin = instance;
// 		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
// 	}
	
// 	@EventHandler
// 	public void breakCrate(BlockBreakEvent e) {
// 		Location l = e.getBlock().getLocation();
// 		//Util.consoleMSG("TropicalCrates - blockbreakevent");
		
// 		if(Crate.getCrateLocations().contains(l)) {
// 			Player p = e.getPlayer();
							
			
// 			if(p.hasPermission(Crate.getAdminPerm()) || p.isOp()) {
				
// 				if(!p.isSneaking()) {
// 					Util.coloredMessage(p, "&cYou must be sneaking to break this crate.");
// 					e.setCancelled(true);
// 					return;
// 				}
				
// 				//Util.consoleMSG("TropicalCrates - crate broken");
// 				String locFormat = Crate.locationToStringFormat(l);
// 				String crateName = Crate.getCrateAtLocation(l);
				
// 				Util.coloredMessage(p, "&c[!] " + crateName + " has been broken at " + locFormat);				
				
				
// 				// removes the string location from the file
// 				String file = "crates"+File.separator+crateName+".yml";
// 				FileConfiguration f = plugin.getConfigFile(file);
// 				List<String> locations = f.getStringList("CrateLocations");
// 				locations.remove(locFormat);
// 				f.set("CrateLocations", locations);
// 				plugin.saveConfig(f, file);
				
// 				// them removes it from memory
// 				Crate.removeCrateAtLocation(l);
				
				
// 			} else {
// 				// send msg - you can not break a crate				
// 				Util.coloredMessage(p, "&c[!] You do not have permission to break this crate!");				
// 				e.setCancelled(true);
// 			}
// 		}
// 	}
	
// }
