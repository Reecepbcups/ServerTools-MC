package me.reecepbcups.disabled;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import me.reecepbcups.tools.Main;

public class DisableDisconnectSpam implements Listener {
	
	private Main plugin;
	public DisableDisconnectSpam(Main instance) {
	    this.plugin = instance;
	    
	    if (plugin.EnabledInConfig("Disabled.DisableDisconnectSpamKick.Enabled")) {				
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);			
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (e.getReason() == "disconnect.spam") {
			e.setCancelled(true);
		}
	}

}
