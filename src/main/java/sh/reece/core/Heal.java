package sh.reece.core;

import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor{//,TabCompleter,Listener {

	String Section, Heal, HealOthers, Feed;
	private final Main plugin;
	private ConfigUtils configUtils;

	public Heal(Main instance) {
		plugin = instance;
		
		
		Section = "Core.Heal";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			configUtils = plugin.getConfigUtils();

			
			plugin.getCommand("heal").setExecutor(this);
			plugin.getCommand("feed").setExecutor(this);
			
			
			Heal = plugin.getConfig().getString(Section+".Permissions.Heal");
			HealOthers = plugin.getConfig().getString(Section+".Permissions.HealOthers");
			Feed = plugin.getConfig().getString(Section+".Permissions.Feed");
		} else {
			AlternateCommandHandler.addDisableCommand("heal");			
			AlternateCommandHandler.addDisableCommand("feed");			
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;

		if(sender instanceof Player) {
			p = (Player) sender;
		}
		
		if(sender instanceof ConsoleCommandSender) {
			// gets the last element in the array, and checks if that is a player
			if(args.length>0) {
				String possibleName = args[args.length-1];
				if(Bukkit.getPlayer(possibleName) != null) {
					p = Bukkit.getPlayer(possibleName);
				} else {					
					if(args.length == 2) {
						Util.consoleMSG("&fUsage: &c/"+label+" <player>");
					} 
					return true;
				}
			} else {
				// if its not healall, let them know commands to run
				if(!label.equalsIgnoreCase("healall")){
					Util.consoleMSG("&fUsage: &c/"+label+" <player>");
					return true;
				}
				
			
			}
			
		}
		
		if(label.equalsIgnoreCase("feed")) {
			if(checkPerm(sender, cmd.getName(), Feed)) {
				Util.coloredMessage(p, configUtils.lang("HEAL_FED"));
				p.setFoodLevel(20);
				p.setSaturation(2);
			}			
		} else if(label.equalsIgnoreCase("heal")) {			
			if(checkPerm(sender, cmd.getName(), Heal)) {
				heal(p);
			}			
		} else if(label.equalsIgnoreCase("healall")) {
			if(checkPerm(sender, cmd.getName(), HealOthers)) {
				Bukkit.getOnlinePlayers().forEach(target -> heal(target));
			}
		} 
		
		return true;
	}
	
	public void heal(Player p) {
		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(20);
		Util.coloredMessage(p, configUtils.lang("HEAL_HEALED"));
	}
	
	public boolean checkPerm(CommandSender p, String CMD, String perm) {
		if (!p.hasPermission(perm)) {
			Util.coloredMessage(p, "&cYou do not have access to &n/"+CMD+"&c.");
			return false;
		} 		
		return true;
	}
	
}
