package me.reecepbcups.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.reecepbcups.tools.Main;

public class AutoBroadcast extends BukkitRunnable {

	private static Main plugin;
	//private FileConfiguration config;
	private String Section;
	List<String> header, footer, groups; 
	private Boolean center;
	private int size, id;
	
	private List<List<String>> messages;
	
	public AutoBroadcast(Main instance) {
        plugin = instance;
        
        Section = "AutoBroadcast";                
        if(plugin.EnabledInConfig(Section+".Enabled")) {	
    		
        	int intervalDelay = plugin.getConfig().getInt(Section+".Interval");
        	
        	header = plugin.getConfig().getStringList(Section+".Header");
        	footer = plugin.getConfig().getStringList(Section+".Footer");
        	groups = new ArrayList<String>(plugin.getConfig().getConfigurationSection(Section+".Messages").getKeys(false)); 
        	
//    		// 1,2,3,4,5,6		
        	center = plugin.getConfig().getString(Section+".centerall").equalsIgnoreCase("true");
        	size = groups.size();
        	
        	// loads all messages into a list of a list in memory
        	messages = new ArrayList<List<String>>();        	
        	for(int i = 0; i < size; i++) {
        		messages.add(plugin.getConfig().getStringList("AutoBroadcast.Messages."+groups.get(i)));
        	}
        	runTaskTimer(plugin, 0, intervalDelay*20);
    	}
	}		    		

	@Override
	public void run() {
		if (size == id) {
			id = 0;
		} else {						
			for(String line : header) {
				Main.announcement(center, line);
			}	
			for(String line : messages.get(id)){//+groupsReal.get(id))) {					
				if(line.equalsIgnoreCase("") || line.equalsIgnoreCase(" ")) {line = "&r";}						
				Main.announcement(center, line);							
			}
			
			for(String line : footer) {
				Main.announcement(center, line);
			}						
			id++;
		}  
	}
	
	
	
}
