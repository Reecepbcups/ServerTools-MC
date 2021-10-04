package sh.reece.moderation;

import java.io.File;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class StaffAFK implements CommandExecutor, Listener {
		
	//LuckPerms luckPerms = LuckPermsProvider.get();
	private Main plugin;
	public String StaffAFKGroup, Permission;
	public Collection<String> staffranks;
	public static String FILE_NAME;
	
	public FileConfiguration config, MAINCONFIG;
	
	public StaffAFK(Main plugin) {
	    this.plugin = plugin;
	    
	    if (plugin.enabledInConfig("Moderation.StaffAFK.Enabled")) {
	    	
	    	
	    	
	    	MAINCONFIG = plugin.getConfig();
	    	this.StaffAFKGroup = MAINCONFIG.getString("Moderation.StaffAFK.StaffAFKGroup");
		    this.staffranks = MAINCONFIG.getStringList("Moderation.StaffAFK.StaffGroups");
		    this.Permission = MAINCONFIG.getString("Moderation.StaffAFK.Permission");

		    FILE_NAME = File.separator + "DATA" + File.separator + "StaffAFKDatabase.yml";
		    plugin.createDirectory("DATA");			
			plugin.createFile(FILE_NAME);
			config = plugin.getConfigFile(FILE_NAME);					
		    
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
				plugin.saveConfig(config, FILE_NAME);
				
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		
		if(!p.hasPermission(Permission)) {
			p.sendMessage("NO permission to use staffafk: " + Permission);
			return true;
		}
		
		if(!p.hasPermission(Permission)) {
			p.sendMessage(Util.color("&cYou do not have access to use StaffAFK!"));
			return true;
		}
		
		User user = LuckPermsProvider.get().getUserManager().getUser(p.getUniqueId());
		// luckPerms.getUserManager().getUser(p.getUniqueId());
		
		if(getPlayerGroup(p, staffranks) == null && !isPlayerInGroup(p, StaffAFKGroup)) {			
			p.sendMessage("You are not in the staff ranks group");
			return true;
		}
		
		// if user is not yet in the config file
		if(config.getString(p.getUniqueId().toString()) == null) {

			config.set(p.getUniqueId().toString()+".name", p.getName()); // only used for staff management behind the scenes
			config.set(p.getUniqueId().toString()+".primarygroup", user.getPrimaryGroup().toString());
			
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

			p.sendMessage(Util.color("\n&eAdded you to StaffAFK\n"));
			
			
			
		} else {
			p.sendMessage(Util.color("\n&eSetting you back to your group: " + config.get(p.getUniqueId().toString()+".primarygroup")));
			
			for(String removeCMD : MAINCONFIG.getStringList("Moderation.StaffAFK.RemoveAFK")) {
				removeCMD = removeCMD.replace("%player%", p.getName());				
				removeCMD = removeCMD.replace("%PlayerConfigPrimarygroup%", config.getString(p.getUniqueId()+".primarygroup"));
				removeCMD = removeCMD.replace("%StaffAFKGroupName%", StaffAFKGroup);
				Util.console(removeCMD);
			}
			config.set(p.getUniqueId().toString(), null);
		}
		
		plugin.saveConfig(config, FILE_NAME);
		
		
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
