package sh.reece.events;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class CustomDeathMessages implements Listener, CommandExecutor {
	
	private Boolean ToggleDeathMSG;
	private String permission, deathFormat;
	
	private Main plugin;
	public CustomDeathMessages(Main instance) {
		plugin = instance;
		
		if (plugin.enabledInConfig("Chat.CustomDeathMessages.Enabled")) {
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);			
			
			permission = "toggledeath.use";
			
			deathFormat = "&7&o[-] %message%";
			
			plugin.getCommand("toggledeathmessages").setExecutor(this);
			ToggleDeathMSG = true;
		}
	}
	
	
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
//		Player killed = e.getEntity();
//      Player killer = e.getEntity().getKiller();
		
		if(deathFormat.length() == 0) {
			e.setDeathMessage("");
		}
		
		if(ToggleDeathMSG) {
			e.setDeathMessage(Util.color(deathFormat.replace("%message%", e.getDeathMessage())));
		} else {
			e.setDeathMessage("");
		}
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender.hasPermission(permission))) {		
			sender.sendMessage(Util.color("&cNo Permission to use ToggleDeath :("));
			return true;			
		} 

		if (args.length >= 0) {		
			Player p = (Player) sender;
			ToggleDeathMSG = !ToggleDeathMSG;
			Util.coloredMessage(p, "&fShowing Death Messages: " + ToggleDeathMSG);
		}
		
		return true; 
	}


}
