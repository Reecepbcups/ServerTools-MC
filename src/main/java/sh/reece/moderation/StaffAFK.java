package sh.reece.moderation;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class StaffAFK implements CommandExecutor, Listener {
		
	//LuckPerms luckPerms = LuckPermsProvider.get();
	// private Main plugin;
	public String StaffAFKGroup, Permission;
	public Collection<String> staffRanks;
	public static String FILE_NAME;
	
	public FileConfiguration config, MAINCONFIG;
	private ConfigUtils ConfigUtils;

	// used for PAPI
	private static Set<UUID> staffWhoAreAFK = new HashSet<UUID>();
	public static boolean isStaffAfk(UUID uuid) {
		return staffWhoAreAFK.contains(uuid);
	}


	public StaffAFK(Main plugin) {
	    // this.plugin = plugin;	    
	    if (plugin.enabledInConfig("Moderation.StaffAFK.Enabled")) {
	    	
	    	ConfigUtils = plugin.getConfigUtils();
	    	
	    	MAINCONFIG = plugin.getConfig();
	    	this.StaffAFKGroup = MAINCONFIG.getString("Moderation.StaffAFK.StaffAFKGroup");
		    this.staffRanks = MAINCONFIG.getStringList("Moderation.StaffAFK.StaffGroups");
		    this.Permission = MAINCONFIG.getString("Moderation.StaffAFK.Permission");

		    FILE_NAME = File.separator + "DATA" + File.separator + "StaffAFKDatabase.yml";
		    ConfigUtils.createDirectory("DATA");			
			ConfigUtils.createFile(FILE_NAME);
			config = ConfigUtils.getConfigFile(FILE_NAME);					
		    
			plugin.getCommand("staffafk").setExecutor(this);
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		}
	}
	
	@EventHandler
  	public void StaffLeave(PlayerQuitEvent e) {		
		
		Player p = e.getPlayer();
		
		if(e.getPlayer().hasPermission(Permission)) {
						
			if(config.getString(p.getUniqueId().toString()) != null) {
				
				Util.console(MAINCONFIG.getString("Moderation.StaffAFK.RemoveAFK.1").replace("%player%", p.getName()).replace("%PlayerConfigPrimarygroup%", config.getString(p.getUniqueId().toString()+".primarygroup")));
				Util.console(MAINCONFIG.getString("Moderation.StaffAFK.RemoveAFK.2").replace("%player%", p.getName()).replace("%StaffAFKGroupName%", StaffAFKGroup));
				
				config.set(p.getUniqueId().toString(), null);
				ConfigUtils.saveConfig(config, FILE_NAME);

				staffWhoAreAFK.remove(p.getUniqueId());
				
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		UUID uuid = p.getUniqueId();
		
		if(!p.hasPermission(Permission)) {
			p.sendMessage("NO permission to use staffafk: " + Permission);
			return true;
		}
		
		if(!p.hasPermission(Permission)) {
			p.sendMessage(Util.color("&cYou do not have access to use StaffAFK!"));
			return true;
		}

		User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
		
		if(getPlayerGroup(p, staffRanks) == null && !isPlayerInGroup(p, StaffAFKGroup)) {			
			p.sendMessage("You are not in the staff ranks group");
			return true;
		}
		
		// if user is not yet in the config file
		if(config.getString(uuid.toString()) == null) {

			config.set(uuid.toString()+".name", p.getName()); // only used for staff management behind the scenes
			config.set(uuid.toString()+".primarygroup", user.getPrimaryGroup().toString());
			config.set(uuid.toString()+".isOp", p.isOp());
			
			for(String giveCMD : MAINCONFIG.getStringList("Moderation.StaffAFK.GiveAFK")) {
				giveCMD = giveCMD.replace("%player%", p.getName());
				
				if(giveCMD.contains("%StaffAFKGroupName%")) {
					giveCMD = giveCMD.replace("%StaffAFKGroupName%", StaffAFKGroup);
				}
				
				if(giveCMD.contains("%UsersPrimaryGroup%")) {
					giveCMD = giveCMD.replace("%UsersPrimaryGroup%", user.getPrimaryGroup().toString());
				}				
				
				Util.console(giveCMD);								
			}

			String output = "\n&eAdded you to StaffAFK\n";

			if(p.isOp()) { // remove op from them for now, reapply on unafk
				p.setOp(false);
				output += "&7&o(Removed you from OP as well)\n";
			}

			staffWhoAreAFK.add(p.getUniqueId());
			Util.coloredMessage(sender, output);					

		} else {
			String primaryGroup = config.getString(p.getUniqueId()+".primarygroup");

			String output = "\n&eYou are back to your group: " + primaryGroup;
						
			for(String removeCMD : MAINCONFIG.getStringList("Moderation.StaffAFK.RemoveAFK")) {
				removeCMD = removeCMD.replace("%player%", p.getName());				
				removeCMD = removeCMD.replace("%PlayerConfigPrimarygroup%", primaryGroup);
				removeCMD = removeCMD.replace("%StaffAFKGroupName%", StaffAFKGroup);
				Util.console(removeCMD);
			}

			if(config.getBoolean(p.getUniqueId().toString()+".isOp")) {
				p.setOp(true);
				output += "\n&a&o(Reapplied OP status)\n";
			}

			config.set(p.getUniqueId().toString(), null);
			staffWhoAreAFK.remove(p.getUniqueId());
			Util.coloredMessage(sender, output);
		}
		
		ConfigUtils.saveConfig(config, FILE_NAME);
		return true;
	}
	
	public static boolean isPlayerInGroup(Player player, String group) {
	    return player.hasPermission("group." + group); //&& !(player.isOp());
	}
	
	public static String getPlayerGroup(Player player, Collection<String> possibleGroups) {
	    for (String group : possibleGroups) {
	        if (player.hasPermission("group." + group)) {
	            return group;
	        }
	    }
	    return null;
	}

}
