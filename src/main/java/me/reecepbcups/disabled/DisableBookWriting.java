package me.reecepbcups.disabled;

import me.reecepbcups.tools.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

public class DisableBookWriting implements Listener {

	private static Main plugin;
	public DisableBookWriting(Main instance) {
	        plugin = instance;
	        
	        if(plugin.enabledInConfig("Disabled.DisableBookWriting.Enabled")) {
				Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
			}
	}
	
	@EventHandler
	public void onBookWrite(PlayerEditBookEvent e) {
		e.getPlayer().sendMessage(Main.lang("DISABLED_BOOKWRITING"));
		e.setCancelled(true);			
	}
	
	
	
}
