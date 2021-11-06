package sh.reece.core;

import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Extinguish implements CommandExecutor{

	String Section, Permission, StaffPermission;
	private final Main plugin;
	//private ConfigUtils configUtils;

	public Extinguish(Main instance) {
		plugin = instance;
		
		
		Section = "Core.Extinguish";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			//configUtils = plugin.getConfigUtils();
			plugin.getCommand("extinguish").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission"); // 
			StaffPermission = plugin.getConfig().getString(Section+".StaffPermission"); //
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(Util.color("&cYou do not have access to &n/" +cmd.getName()+"&c."));
			return true;
		} 

		Player target = (Player) sender;
		
		// if player tries to ext someone else & is a staff
		if(args.length > 0 && target.hasPermission(StaffPermission)) {	
			// set that target		
			if(Bukkit.getPlayer(args[0]) != null){
				target = Bukkit.getPlayer(args[0]);
			} else {
				Util.coloredMessage(sender, "&cPlayer &n"+args[0]+"&c is not online.");
				return true;
			}			
		}

		// remove fire from the player
		target.setFireTicks(0);	
		Util.coloredMessage(target, "&a[+] You have been extinguished!");
		
		if(!target.getName().equalsIgnoreCase(sender.getName())) {
			Util.coloredMessage(sender, "&a[+] Successfully Extinguished &n"+target.getName());
		}
		
		return true;
	}
}
