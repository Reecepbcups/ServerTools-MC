package me.reecepbcups.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class Broadcast implements CommandExecutor{//,TabCompleter,Listener {

	String Section, Permission;
	private Main plugin;
	public Broadcast(Main instance) {
		this.plugin = instance;
		
		
		Section = "Core.Broadcast";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.EnabledInConfig(Section+".Enabled")) {
			plugin.getCommand("broadcast").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		

		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(Util.color("&cYou do not have access to &n/" +cmd.getName()+"&c."));
			return true;
		} 
		
		if (args.length == 0) {										
			sender.sendMessage(Util.color("&fUsage: &c/"+label+" <message>"));
			return true;
		} 
		
		String broadcastMSG = Main.LANG("BROADCAST").replace("%msg%", Util.argsToSingleString(0, args));
		Util.coloredBroadcast(broadcastMSG);			

		
		return true;
	}
}
