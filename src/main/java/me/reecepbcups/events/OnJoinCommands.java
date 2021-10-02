package me.reecepbcups.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class OnJoinCommands implements Listener {

	
	private static Main plugin;
	private List<String> FirstJoinCMDS, PlayerRunOnJoin;
	private Boolean isFirstJoinEnabled, isPlayerRunEnabled;
	
	public OnJoinCommands(Main instance) {
		plugin = instance;

		String section = "Misc.OnJoinCommands";
		if (plugin.enabledInConfig(section+".Enabled")) {

			//FileConfiguration config = Main.MAINCONFIG;
			FirstJoinCMDS = Main.MAINCONFIG.getStringList(section+".FirstUniqueJoin.CMDS");
			PlayerRunOnJoin = Main.MAINCONFIG.getStringList(section+".PlayerRunCommands.CMDS");

			isFirstJoinEnabled = Main.MAINCONFIG.getString(section+".FirstUniqueJoin.Enabled").equalsIgnoreCase("true");
			isPlayerRunEnabled = Main.MAINCONFIG.getString(section+".PlayerRunCommands.Enabled").equalsIgnoreCase("true");

			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		}
	}
	
	
	
	@EventHandler
	public void PlayerCommand(PlayerJoinEvent event) {
		Player p = (Player) event.getPlayer();
		
		// First Join commands enabled & Player has not played before
		if(isFirstJoinEnabled) {			
			if(!(p.hasPlayedBefore())){																
				new BukkitRunnable() {
					@Override
					public void run() {								
						FirstJoinCMDS.stream().forEach(cmd -> Util.console(cmd.replace("%player%", p.getName())));				
					}				
				}.runTaskLater(plugin, 10L);
				
			}			
		}
		
		if(isPlayerRunEnabled) {
			new BukkitRunnable() {
				@Override
				public void run() {								
					PlayerRunOnJoin.stream().forEach(command -> p.performCommand(command.replace("%player%", p.getName())));				
				}				
			}.runTaskLater(plugin, 10L);
		}
	}
	
}
