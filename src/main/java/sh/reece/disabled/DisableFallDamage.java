package sh.reece.disabled;

//import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import sh.reece.tools.Main;

public class DisableFallDamage implements Listener {

	private static Main plugin;
	
	private String permission;
	//private List<String> worlds;
	
	public DisableFallDamage(Main instance) {
        plugin = instance;
        
        if (plugin.enabledInConfig("Disabled.DisableFallDamage.Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    		
    		
    		permission = plugin.getConfig().getString("Disabled.DisableFallDamage.Permission");
    		
//    		worlds = new ArrayList<String>();
//    		
//    		for(String world : plugin.getConfig().getStringList("Disabled.DisableFallDamage.Worlds")) {
//    			if(Bukkit.getWorld(world) == null) {
//    				Util.consoleMSG("INVALID WORLD: " + world + " (( capitalization matters ))");
//    				continue;
//    			}
//    			worlds.add(world);
//    		}
    		
    		
    	}
	}
	
	@EventHandler
	public void damageEvent(EntityDamageEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();

			if(e.getCause() == DamageCause.FALL) {
				if(permission.length() == 0 || p.hasPermission(permission)) {
					//if(worlds.contains(p.getWorld().getName())) {
					e.setCancelled(true);
					//}
				}
			}

		}
	}
	
	
}
