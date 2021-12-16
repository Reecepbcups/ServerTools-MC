package sh.reece.core;

import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor{

	String Section, Permission;
	private final Main plugin;
	private ConfigUtils configUtils;
	
	public Warp(Main instance) {
		plugin = instance;
		
		
		Section = "Core.Warps";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			configUtils = plugin.getConfigUtils();
			plugin.getCommand("warp").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");

			configUtils.createConfig("warps.yml");
		} else {
			AlternateCommandHandler.addDisableCommand("warp");
			AlternateCommandHandler.addDisableCommand("setwarp");
			AlternateCommandHandler.addDisableCommand("delwarp");
			AlternateCommandHandler.addDisableCommand("warps");
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!sender.hasPermission(Permission)) {
			Util.coloredMessage(sender, "&cYou do not have access to &n/" +cmd.getName()+"&c.");
			return true;
		} 

		// /warp, /warps, /setwarp <name>, /delwarp <name>

		// check if the label name is warps
		if(cmd.getName().equalsIgnoreCase("warps")){
			int page = 0;
			if(args.length > 0) {
				if(Util.isInt(args[0])){
					page = Integer.parseInt(args[0]);
				}
			}
			showWarps(sender, page);
		}


		if(cmd.getName().equalsIgnoreCase("setwarp")){
			if(args.length > 0) {
				Player p = (Player) sender;
				setWarp(p, args[0]);
			} else {
				Util.coloredMessage(sender, "Usage: /setwarp <warp>");
			}
			return true;			
		}

		if(cmd.getName().equalsIgnoreCase("delwarp")){
			if(args.length > 0) {
				delwarp(sender, args[0]);
			} else {
				Util.coloredMessage(sender, "Usage: /delwarp <warp>");
			}
			return true;			
		}
		

		// if args is 0 OR they enter /warp #, send player warp list of keys, where # is the page
		if(args.length == 0) {			
			showWarps(sender, 0);

		} else if(args.length == 1 ){

			if(Util.isInt(args[0])) {
				showWarps(sender, Integer.parseInt(args[0]));

			} else {
				Player p = (Player) sender;
				teleportPlayer(p, args[0]);
			}
		}
		
		return true;
	}

	private void setWarp(Player p, String name){
		// check if name is a number
		if(Util.isInt(name)) {
			Util.coloredMessage(p, "&cYou can not set a warp as a number.");
			return;
		}

		if(getWarps().contains(name)){
			Util.coloredMessage(p, "&cThere is already a warp set as this name!");
		}
		
		String warpLoc = Util.locationToString(p);

		// save warpLoc to config as key: string
		FileConfiguration f = configUtils.getConfigFile("warps.yml");
		f.set(name, warpLoc);
		configUtils.saveConfig(f, "warps.yml");
	}

	private void delwarp(CommandSender sender, String name){

		if(!getWarps().contains(name)){
			Util.coloredMessage(sender, "&cThis warp does not exist!");
			return;
		}

	}

	private void showWarps(CommandSender sender, int page) {
		
		Util.coloredMessage(sender, "&f&lWarps:");

		if(page > getWarps().size()/20) { // where 20 is warps per page
			Util.coloredMessage(sender, "&cThis Page does not exist.");
			return;
		}

		// loop through keys and print them with comma delimited with StringBuilder
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(String key : getWarps()) { // this likely will not work
			if(i == 20*page) {
				break;
			}
			if(i > 20*page) {
				sb.append(", ");
			}
			sb.append(key);
			i++;
		}
		Util.coloredMessage(sender, "&f"+sb.toString());
	}


	private void teleportPlayer(Player player, String warp) {
		
		Location warpLoc = getWarpLocation(warp);

		// check that warpLoc is not null, if it is, tell them this is not a warp location
		if(warpLoc != null) {
			player.teleport(warpLoc);
			Util.coloredMessage(player, "&6Teleported to &n"+warp+"&6.");
		} else {
			Util.coloredMessage(player, "&cThis is not a warp location.");
			return;
		}

		// // check that the block under warpLoc is not air or lava
		if(warpLoc.getBlock().getType().isSolid()){
			
			// BukkitRunnable for 5 second delay before teleporting player unless they have permission to bypass
			if(!player.hasPermission("core.bypass.delay")) {
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						player.teleport(warpLoc);
					}
				}, 20*5);

			} else {
				player.teleport(warpLoc);
			}

		} else {
			Util.coloredMessage(player, "&cThis warp location is unsafe!");
		}
		
	}

	private Location getWarpLocation(String warp){
		FileConfiguration warps = configUtils.getConfigFile("warps.yml");
		// // get warp location from config string
		String warpLocation = warps.getString(Section+"."+warp);
		return Util.stringToLocation(warpLocation);
	}

	private Set<String> getWarps(){
		FileConfiguration warps = configUtils.getConfigFile("warps.yml");
		return warps.getKeys(false);
	}


	// tab complete


	// 	String adminChatMSG = configUtils.lang("ADMINCHAT")
		// 			.replace("%player%", sender.getName())
		// 			.replace("%msg%", Util.argsToSingleString(0, args));
}
