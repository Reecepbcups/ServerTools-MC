package me.reecepbcups.disabled;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.reecepbcups.tools.Main;

public class ThreeHitGlitch implements Listener {

	private static Main plugin;
	public ThreeHitGlitch(Main instance) {
        plugin = instance;
        
        String Section = "Misc.ThreeHitGlitch";                
        if(plugin.EnabledInConfig(Section+".Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);    		
    	}
	}
	
	@EventHandler
	public void hurt(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player)event.getDamager();
			if (player.getInventory().getItemInHand().getType() == Material.AIR)
				event.setDamage(1.0D); 
		} 
	}
	
	
	
}
