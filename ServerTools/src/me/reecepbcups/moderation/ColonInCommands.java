package me.reecepbcups.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class ColonInCommands implements Listener{

	private String perm;
	
	private Main plugin;
	public ColonInCommands(Main instance) {
		plugin = instance;
		
		if(plugin.EnabledInConfig("Moderation.NoColonInCommands.Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    		perm = plugin.getConfig().getString("Moderation.NoColonInCommands.BypassPerm");
    	}
		
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		
		if(e.isCancelled()) {
			return;
		}		
		
		//if (e.getMessage().split(" ")[0].contains(":")) {
		if(e.getMessage().indexOf(":") != -1) {
			if(!e.getPlayer().hasPermission(perm)) {
				e.getPlayer().sendMessage(Main.LANG("NO_COLONS_IN_COMMANDS"));
				e.setCancelled(true);			
			} 
		}
	}

	
	
	
}
