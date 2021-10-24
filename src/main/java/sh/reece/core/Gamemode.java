package sh.reece.core;

import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor{//,TabCompleter,Listener {

	String Section, Creative, Survival, Spectator, Adventure;
	private final Main plugin;
	private ConfigUtils configUtils;
	
	public Gamemode(Main instance) {
		plugin = instance;

		
		Section = "Core.Gamemode";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			configUtils = plugin.getConfigUtils();

			plugin.getCommand("gamemode").setExecutor(this);
			
			Creative = plugin.getConfig().getString(Section+".Permissions.Creative");
			Survival = plugin.getConfig().getString(Section+".Permissions.Survival");
			Spectator = plugin.getConfig().getString(Section+".Permissions.Spectator");
			Adventure = plugin.getConfig().getString(Section+".Permissions.Adventure");
		}
		
	}
	
	private boolean fromConsole;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		fromConsole = false;
		
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
						Util.consoleMSG("&fUsage: &c/"+label+" <gamemode> <player>");
					} 
					return true;
				}
			} else {
				Util.consoleMSG("&fUsage: &c/gamemode <gamemode> <player>");
				Util.consoleMSG("&fUsage: &c/gm(a/c/s/sp) <player>");
				return true;
			}
			fromConsole = true;			
		}
		
		switch (label.toLowerCase()) {
		case "gms":
			survival(p);
			return true;
		case "gmc":
			creative(p);
			return true;
		case "gma":
			adventure(p);
			return true;
		case "gmsp":
			spectator(p);
			return true;
		default:
			break;
		}
		
		if (args.length == 0) {										
			Util.coloredMessage(sender, "&fUsage: &c/"+label+" <survival,creative,spectator,adventure>");
			return true;
		}
		
		if(args.length >= 1) {			
			switch (args[0].toLowerCase()) {
			case "creative":
			case "0":
			case "c":
				creative(p);			
				break;

			case "survival":
			case "1":
			case "s":
				survival(p);			
				break;
				
			case "adventure":
			case "2":
			case "a":
				adventure(p);
				break;
				
			case "spectator":
			case "3":
			case "sp":
				spectator(p);
				break;
				
			default:
				break;
			}		
		}		
		return true;
	}
	

	public boolean survival(Player target) {
		if(checkPerm(target, Survival, configUtils.lang("GAMEMODE_SURVIVAL"))) {
			target.setGameMode(GameMode.SURVIVAL);
			return true;
		} 
		return false;
	}
	public boolean creative(Player target) {
		if(checkPerm(target, Creative, configUtils.lang("GAMEMODE_CREATIVE"))) {
			target.setGameMode(GameMode.CREATIVE);
			return true;
		} 
		return false;
	}
	public boolean adventure(Player target) {
		if(checkPerm(target, Adventure, configUtils.lang("GAMEMODE_ADVENTURE"))) {
			target.setGameMode(GameMode.ADVENTURE);
			return true;
		}  
		return false;
	}
	public boolean spectator(Player target) {
		if(checkPerm(target, Spectator, configUtils.lang("GAMEMODE_SPECTATOR"))) {
			target.setGameMode(GameMode.SPECTATOR);
			return true;
		} 
		return false;
	}
	
	
	
	
	public boolean checkPerm(Player p, String Perm, String PermFormat) {
		// if fromConspole, then it lets console change the GM no matter their perm level
		if(fromConsole || p.hasPermission(Perm)) {	
			Util.coloredMessage(p, configUtils.lang("GAMEMODE_CHANGED").replace("%gamemode%", PermFormat));
			Util.consoleMSG("&e&l[ServerTools]&f Changed " + p.getName() +"'s gamemode to "+ PermFormat);
			return true;
		} 
		
		Util.coloredMessage(p, "&cYou do not have access to "+PermFormat+" &cmode!");
		return false;
	}
	
	
}
