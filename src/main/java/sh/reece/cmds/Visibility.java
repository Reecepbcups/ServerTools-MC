package sh.reece.cmds;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class Visibility implements Listener, CommandExecutor, TabCompleter {

	private static Main plugin;
	//private FileConfiguration config;
	private final String Section;
	private String Permission;
	private Boolean allowUsage;

	public Visibility(Main instance) {
		plugin = instance;

		Section = "Misc.Visibility";                
		if(plugin.enabledInConfig(Section+".Enabled")) {

			//config = plugin.getConfig();
			Permission = plugin.getConfig().getString(Section+".bypassPerm");

			plugin.getCommand("visibility").setExecutor(this);
			plugin.getCommand("visibility").setTabCompleter(this);
			allowUsage = true;  		
		}
	}

	private static final List<String> possibleArugments = new ArrayList<String>();
	private static final List<String> result = new ArrayList<String>();
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(possibleArugments.isEmpty()) {
			possibleArugments.add("all");
			possibleArugments.add("staff");
			possibleArugments.add("toggleusage"); 
			possibleArugments.add("forceshowall");
					
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
		Player p = (Player) sender;

		if (args.length == 0) {
			sendHelpMenu(p);
			return true;
		}	

		switch(args[0]){
		// /command clear
		case "all":	
			toggleView(p, false);
			return true;	
			
		case "staff":
			toggleView(p, true);
			return true;
			
		
		case "toggleusage":
			
			if(!sender.hasPermission("visibility.staff")) {
				Util.coloredMessage((Player) sender, "&cNo Perms to access toggleusage &7&o(( visibility.staff ))");
				return true;
			}
			
			allowUsage = !allowUsage;
			Util.coloredMessage(p, "&bVisibility plugin usage allowed: " + allowUsage);
			return true;
			
		case "forceshowall":
			
			if(!sender.hasPermission("visibility.staff")) {
				Util.coloredMessage((Player) sender, "&cNo Perms to access toggleusage &7&o(( visibility.staff ))");
				return true;
			}
			
			for(Player pOnline : Bukkit.getOnlinePlayers()) {
				toggleView(pOnline, false);
			}
			return true;
			
		default:
			sendHelpMenu(p);
			return true;		
		}		
	}
	
	public void toggleView(Player p, Boolean hidePlayer) {
		if(allowUsage == false) {
			Util.coloredMessage(p, Main.lang("VISIBILITY_DISABLED"));
			return;
		}
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online != p) {				
				if(hidePlayer) {
					if(!online.hasPermission(Permission)) {
						p.hidePlayer(online);
					}
				} else {
					p.showPlayer(online);
				}								
			}
		}	
		
		if(hidePlayer) {
			Util.coloredMessage(p, Main.lang("VISIBILITY_STAFF_ONLY"));
		} else {
			Util.coloredMessage(p, Main.lang("VISIBILITY_EVERYONE"));
		}
		
		
	}

	public void sendHelpMenu(Player p) {
		Util.coloredMessage(p, "&f&m---------------------------");
		Util.coloredMessage(p, "&f/visibility &7all");
		Util.coloredMessage(p, "&f/visibility &7staff");

		if(p.hasPermission("visibility.staff")) {
			Util.coloredMessage(p, "&f/visibility &7toggleusage");
			Util.coloredMessage(p, "&f/visibility &7forceshowall");
		}
		Util.coloredMessage(p, "&f&m---------------------------");
	}



}
