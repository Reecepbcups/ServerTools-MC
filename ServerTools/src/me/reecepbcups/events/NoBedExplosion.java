package me.reecepbcups.events;


import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class NoBedExplosion implements Listener {

	private static Main plugin;
	private String Section;

	public NoBedExplosion(Main instance) {
		plugin = instance;

		Section = "Events.NoBedExplosionInNether";                
		if(plugin.EnabledInConfig(Section+".Enabled")) {
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);    		
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().toString().toLowerCase().contains("bed")) {
				if(e.getClickedBlock().getLocation().getWorld().getEnvironment() == Environment.NETHER) {
					Util.coloredMessage(p, Main.LANG("NOBEDEXPLOSION"));
					e.setCancelled(true);
				}
			}
		}

	}
}
