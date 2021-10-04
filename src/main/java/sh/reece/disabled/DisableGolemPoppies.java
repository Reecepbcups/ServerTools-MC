package sh.reece.disabled;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import sh.reece.tools.Main;

public class DisableGolemPoppies implements Listener {

	private static Main plugin;
	private FileConfiguration config;
	private String Section;
	
	public DisableGolemPoppies(Main instance) {
        plugin = instance;
        
        Section = "Disabled.DisableGolemPoppies";                
        if(plugin.enabledInConfig(Section+".Enabled")) {
        	
        	config = plugin.getConfig();
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);    		
    	}
	}
	
	@EventHandler
	public void removeRoses(EntityDeathEvent e) {
		
		if (e.getEntity().getType() == EntityType.IRON_GOLEM) {
			e.getDrops().removeIf(itemstack -> (itemstack.getType() == Material.RED_ROSE));
		}
	     
	}
	
	
}
