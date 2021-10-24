package sh.reece.disabled;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

import sh.reece.tools.Main;

public class DisableIceMelt implements Listener {

	private static Main plugin;
	public DisableIceMelt(Main instance) {
        plugin = instance;
        
        if (plugin.enabledInConfig("Disabled.DisableIceMelt.Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    	}
	}
	
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onIceBlockMelt(BlockFadeEvent e) {
		if (e.getBlock().getType().equals(Material.ICE)){
			e.setCancelled(true); 
		}
	}
	
}
