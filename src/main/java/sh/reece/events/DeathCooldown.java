package sh.reece.events;


import org.bukkit.event.Listener;

public class DeathCooldown implements Listener {

	// private Main plugin;
	// private static List<String> deathCooldownWorlds;
	// private HashMap<String, Date> CooldownHash;
	// private int secondCooldown;
	
	// public DeathCooldown(Main instance) {
	// 	plugin = instance;
		
	// 	if (plugin.enabledInConfig("Misc.DeathCooldown.Enabled")) {
	// 		deathCooldownWorlds = plugin.getConfig().getStringList("Misc.DeathCooldown.worlds");			
	// 		secondCooldown = plugin.getConfig().getInt("Misc.DeathCooldown.timeInSeconds");
			
	// 		this.CooldownHash = new HashMap<String, Date>();
			
	// 		// Bukkit.getServer().getPluginManager().registerEvents(this, plugin);	
	// 	}
	// }	
			
	
//	public void onPlayerDeathEvent(PlayerDeathEvent e) {
//		
//		if(e.getEntity() instanceof Player) {
//			Player p = e.getEntity().getPlayer();
//			
//			if(deathCooldownWorlds.contains(p.getLocation().getWorld().getName())) {
//				// add cooldown to player to not be able to switch to this world
//				
//				Util.cooldown(CooldownHash, secondCooldown, p.getName(), "&cAdded death event for");
//			}
//			
//		}
//		
//	}
//	
//	public void playerWorldSwitchEvent(PlayerTeleportEvent event) {
//		
//		if(deathCooldownWorlds.contains(event.getPlayer().getWorld().getName())) {
//			// if their cooldown is not over, deny.
//			
//			if(!(Util.cooldown(CooldownHash, secondCooldown, event.getPlayer().getName(), "&cYou can not teleport to this world for %timeleft% seconds"))) {
//				// User has cooldown	  
//				event.setCancelled(true);
//	    	}
//			
//		}
//		
//	}
	
	
	
}
