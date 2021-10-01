package me.reecepbcups.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class Workbench implements CommandExecutor{//,TabCompleter,Listener {

	String Section, Permission;
	private Main plugin;
	public Workbench(Main instance) {
		this.plugin = instance;
		
		
		Section = "Core.Workbench";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.EnabledInConfig(Section+".Enabled")) {
			plugin.getCommand("workbench").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(Util.color("&cYou do not have access to &n/" +label+"&c."));
			return true;
		} 
		
		((Player) sender).openWorkbench(null, true);
		return true;
	}
}
