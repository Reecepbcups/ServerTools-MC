package me.reecepbcups.events;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.event.Listener;

import me.reecepbcups.tools.Main;

public class DeathCooldown implements Listener {

	private Main plugin;
	private static List<String> deathCooldownWorlds;
	private HashMap<String, Date> CooldownHash;
	private int secondCooldown;
	
	public DeathCooldown(Main instance) {
		plugin = instance;
		
		if (plugin.EnabledInConfig("Misc.DeathCooldown.Enabled")) {	
			deathCooldownWorlds = Main.MAINCONFIG.getStringList("Misc.DeathCooldown.worlds");			
			secondCooldown = Main.MAINCONFIG.getInt("Misc.DeathCooldown.timeInSeconds");
			
			this.CooldownHash = new HashMap<String, Date>();
			
			// Bukkit.getServer().getPluginManager().registerEvents(this, plugin);	
		}
	}	
			
	
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
