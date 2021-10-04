package sh.reece.cmds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class FancyAnnounce implements CommandExecutor, TabCompleter {

	private Main plugin;
	private String Permission;
	private FileConfiguration config;
	public FancyAnnounce(Main plugin) {
	    this.plugin = plugin;
	    
	    this.Permission = plugin.getConfig().getString("FancyAnnounce.Permission");
	    
	    if (plugin.enabledInConfig("FancyAnnounce.Enabled")) {
	    	config = plugin.getConfig();
			plugin.getCommand("announce").setExecutor(this);
			plugin.getCommand("announce").setTabCompleter(this);
		}
	}
	
	private static List<String> possibleArugments = new ArrayList<String>();
	private static List<String> result = new ArrayList<String>();
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {		
		
		if(possibleArugments.isEmpty()) {
			for(String group : config.getConfigurationSection("FancyAnnounce.Groups").getKeys(false)) {
				possibleArugments.add(group);
			}
		}		
		result.clear();
		if(args.length == 1) {
			for(String a : possibleArugments) {
				if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}		
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		
		// permission
		if (!(sender.hasPermission(Permission))) {				
			sender.sendMessage(Util.color("&cNo Permission to use the announce command"));
			return true;			
		} 
			
		// if no arguments given
		if((args.length < 1)) {
			sender.sendMessage(Util.color("&c/Announce <Type>"));				
			sender.sendMessage("Types: " + config.getConfigurationSection("FancyAnnounce.Groups").getKeys(false).toString());			
			return true;
		} 
		
		String Section = "FancyAnnounce.Groups." + args[0]; // ex bosses
		//Integer args_number = config.getInt(Section + ".Arguments"); 
		String cmd_syntax = config.getString(Section + ".ArgumentsSyntax");
		
		if (!(config.getStringList(Section) != null)) {
			sender.sendMessage(Util.color("&cThe section \"" + Section + "\" could not be found D:"));
			return true;
		}
		
		if(!(args.length == cmd_syntax.split(" ").length+1)) {
			sender.sendMessage("Usage: /" + cmd.getName() + " " + args[0] + " " + cmd_syntax);
			return true;
		}
		
		for (String str : config.getStringList(Section + ".Message")) {
			
			for(String arg : args) {
				int argnum = Arrays.asList(args).indexOf(arg);
				str = str.replace("%arg-" + argnum + "%", args[argnum]);
				str = str.replace("%player%", sender.getName());
			}
			
			if(config.getString(Section + ".CenterMessage").equalsIgnoreCase("true")) {
				for (Player all : Bukkit.getOnlinePlayers()) {
					Util.sendCenteredMessage(all, str);
				}
			} else {
				Bukkit.broadcastMessage(str);
			}


		}	
		
		return true;
	}
	
	
}
