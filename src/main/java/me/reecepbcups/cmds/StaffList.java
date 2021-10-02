package me.reecepbcups.cmds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;

import me.reecepbcups.tools.Main;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import me.reecepbcups.utiltools.Util;

public class StaffList implements CommandExecutor {

	private static Main plugin;
	private FileConfiguration config;
	private String Section;
	private Set<String> groups;	
	
	// owner: "&8&l<&d&lOWNER&8&l> &f» &d"
	private HashMap<String, String> groupFormating = new HashMap<String, String>();
	
	public StaffList(Main instance) { // https://gyazo.com/fa06848c6e11a9a7b209cd2a30242b4b
        plugin = instance;
        
        Section = "Commands.StaffList";                
        if(plugin.enabledInConfig(Section+".Enabled")) {
        	
        	config = plugin.getConfig();

        	// owner, developer, manager, admin, srmod, mod, jrmod, helper
        	// change this to only get defined group in groups config
        	
        	// grabs non cached copy of config file to not post MemorySection errors in chat.
        	
        	if(plugin.getConfigFile("config.yml").contains(Section+".groups")) {
        		groups = plugin.getConfigFile("config.yml").getConfigurationSection(Section+".groups").getKeys(false);
        	} else {
        		Util.consoleMSG("&c[!] &4NO GROUPS DEFINED AT " + Section+".groups");
        		return;
        	}    	
        	//Util.consoleMSG("StaffList Group Keys " + groups);
        	
        	
        	for(String group : groups) { // group formating cache        		
        		groupFormating.put(group, config.getString(Section+".groups."+group));          		        		      	
        	}        
        	
        	plugin.getCommand("stafflist").setExecutor(this);   		
    	}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {				
		
		if(sender instanceof ConsoleCommandSender) {
			Util.consoleMSG("You can not run this command, try 'online' instead");
			return true;			
		}
		
		Player p = (Player) sender;

		// owner, ['reece', 'phasha']
		HashMap<String, Set<String>> staff = new HashMap<String, Set<String>>();
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			User u = LuckPermsProvider.get().getUserManager().getUser(online.getUniqueId());
			String mainGroup = u.getPrimaryGroup().toString(); // staff ranks
			
			//Util.consoleMSG(online.getName() + " main group: " + mainGroup);
			
			// if their main group is not in the groups list
			// for example, default
			if(!groups.contains(mainGroup)) {
				continue;
			}
			
			// if they are vanished, don't show in list
			if(isVanished(online) || online.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				continue;
			}
			
			// if staff does not have that staff key, add to new list and do that
			if(!staff.containsKey(mainGroup)) {
				Set<String> newgroup = new HashSet<String>();
				newgroup.add(online.getName());
				staff.put(mainGroup, newgroup);
				
				Util.consoleMSG("Added " + mainGroup + " key to staff hash");
			} else { 
				// if staff hash has that key already
				// adds user to hashmap if they have the rank
				staff.get(mainGroup).add(online.getName());
			}
		}
		
		// show staff - debugging
//		for(String staffkeys : staff.keySet()) {
//			Util.consoleMSG("key " + staffkeys);
//			Util.consoleMSG("players " + staff.get(staffkeys).toString());
//		}
		
		
		// Outputting to user		
		Util.coloredMessage(p, " ");		
		// owner: "&8&l<&d&lOWNER&8&l> &f» &d"
		for(String group : groups) {
			String finalOutput = "";
			
			String title = groupFormating.get(group);
			
			finalOutput+=title;
			
			// if no staff, set to N/A
			if(!staff.containsKey(group)) {
				finalOutput+=" &c&oN/A";				
			} else {
				// for StaffPlayer in that group, append to group
				for(String SPlayer : staff.get(group)) {
					finalOutput += SPlayer+" ";
				}
			}			
			Util.coloredMessage(p, finalOutput);
		}		
		Util.coloredMessage(p, " ");
		
		return true;
		
		
		
		
	}
	
	private boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
}
