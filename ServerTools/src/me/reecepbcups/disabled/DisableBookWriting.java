package me.reecepbcups.disabled;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class DisableBookWriting implements Listener {

	private static Main plugin;
	public DisableBookWriting(Main instance) {
	        plugin = instance;
	        
	        if(plugin.EnabledInConfig("Disabled.DisableBookWriting.Enabled")) {
				Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
			}
	}
	
	@EventHandler
	public void onBookWrite(PlayerEditBookEvent e) {
		e.getPlayer().sendMessage(Main.LANG("DISABLED_BOOKWRITING"));
		e.setCancelled(true);			
	}
	
	
	
}
