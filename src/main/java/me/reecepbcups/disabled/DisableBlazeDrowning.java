package me.reecepbcups.disabled;

import org.bukkit.Bukkit;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.reecepbcups.tools.Main;

public class DisableBlazeDrowning implements Listener {

	private static Main plugin;
	public DisableBlazeDrowning(Main instance) {
        plugin = instance;
        
        if (plugin.enabledInConfig("Disabled.DisableBlazeDrowning.Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    	}
	}
	
	
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		Entity ent = e.getEntity();
		if (ent instanceof Blaze && e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
			e.setCancelled(true); 
		}	      
	}
	
}
