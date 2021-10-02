package me.reecepbcups.runnables;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class ClearLag extends BukkitRunnable implements CommandExecutor  {

	private static Main plugin;
	private FileConfiguration config;
	private String Section, ClearSoonMSG, ClearedMSG;
	private int delay;
	List<Integer> warningTimes = Arrays.asList(new Integer[]{5, 10, 30, 60, 120});
	private Boolean firstRun;
	private Boolean AutoClearMobs;
	
	public ClearLag(Main instance) {
        plugin = instance;
        
        Section = "Misc.ClearLag";                
        if(plugin.enabledInConfig(Section+".Enabled")) {
        	
        	config = plugin.getConfig();	

        	Boolean AutoClearItems = config.getString(Section+".AutoClearItems.Enabled").equalsIgnoreCase("true");
        	
        	AutoClearMobs = config.getString(Section+".AutoClearItems.ClearMobs").equalsIgnoreCase("true");
        	
        	delay = config.getInt(Section+".AutoClearItems.ClearDelay");
        	firstRun = true;
        	
        	if(AutoClearItems) {        		
        		runTaskLater(plugin, 0*20);
        	}
        	
        	ClearSoonMSG = config.getString(Section+".AutoClearItems.ClearSoonMSG");
        	ClearedMSG = config.getString(Section+".AutoClearItems.ClearedMSG");
        	
        	
        	plugin.getCommand("clearlag").setExecutor(this);
        	
    	}
	}
	
	private int test = delay;
	@Override
	public void run() {
		
		new BukkitRunnable(){
			public void run() {	
				
				if(test<=0) {					
					clearItemsInAllWorlds();
					test = delay;
					return;
				} 
				
				if(warningTimes.contains(test)) {
					Util.coloredBroadcast(ClearSoonMSG.replace("%seconds%", test+""));
				}
								
				test-=5;
				
													
			}
		}.runTaskTimer(plugin, 0, 5*20L); 
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!(sender.hasPermission("tools.clearlag"))) {		
			sender.sendMessage(Util.color("&cNo Permission to use "+label+" :("));
			return true;			
		}
		
		Player p = (Player) sender;

		if (args.length == 0) {
			sendHelpMenu(p);
			return true;
		}	
		
		switch(args[0]){
			// /clearlag clear
			case "clear":				
				clearItemsInAllWorlds();
				Util.coloredMessage(p, "&aYou cleared all items on the ground");
				return true;	
				
			case "radius":
			case "rad":
			case "r":				
				if(args.length < 2) {
					sendHelpMenu(p);
					return true;
				}
				
				int radius = Integer.parseInt(args[1]);
				
				for(Entity e : p.getNearbyEntities(radius, radius, radius)) {
					if(e instanceof Item) {
						e.remove();
					}
				}
				
				return true;
				
				
			default:
				sendHelpMenu(p);
				return true;		
		}		
	}
	
	public void clearItemsInAllWorlds() {
		
		if(firstRun) {
			firstRun = !firstRun;
			return;
		}
		
		for(World w : Bukkit.getWorlds()) {

			for(Entity e : w.getEntities()) {
				if(e instanceof Item) {
					e.remove();
				}

				if(AutoClearMobs) { // clear all mobs if enabled
					if(e instanceof Animals || e instanceof Monster) {
						if(e.getCustomName() == null) {
							e.remove(); // remove non named entities
						}

					}
				}
			}
		}	
		Util.coloredBroadcast(ClearedMSG);
	}

	
	public void sendHelpMenu(Player p) {
		Util.coloredMessage(p, "&f/clearlag &7clear");
		Util.coloredMessage(p, "&f/clearlag &7radius <blocks>");
	}
	
	
	
}
