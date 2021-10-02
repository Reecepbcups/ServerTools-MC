package me.reecepbcups.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class TimeChange extends BukkitRunnable {

	private static Main plugin;
	private FileConfiguration config;
	private String Section;
	
	List<World> dayWorlds, nightWorlds;
	Boolean allWorlds;
	int SecondSync;
	
	public TimeChange(Main instance) {
        plugin = instance;
        
        Section = "Disabled.DisableTimeChange";                
        if(plugin.enabledInConfig(Section+".Enabled")) {
        	
        	config = plugin.getConfig();	

        	SecondSync = 1;//config.getInt(Section+".SecondSync");
        	allWorlds = false; //config.getString(Section+".DayInAllWorlds").equalsIgnoreCase("true");
        	dayWorlds = getNonNullWorlds(config.getStringList(Section+".DayWorlds"));
        	nightWorlds = getNonNullWorlds(config.getStringList(Section+".NightWorlds"));        	

        	runTaskTimer(plugin, 0, SecondSync*20);
    	}
	}		    		

	
	
	@Override
	public void run() {
		if(allWorlds) {
			Bukkit.getWorlds().stream().forEach(world -> world.setTime(4000));
			return;
		}
		
		dayWorlds.stream().forEach(world -> world.setTime(4000));
		nightWorlds.stream().forEach(world -> world.setTime(16000));				
		
	}
	
	public List<World> getNonNullWorlds(List<String> worlds){
		// worlds from String to Worlds
		List<World> worldList = new ArrayList<>();		

		worlds.stream().forEach(world -> {
			if(Bukkit.getWorld(world) != null) {
				worldList.add(Bukkit.getWorld(world));
			} else {
				Util.consoleMSG("&cWorld: " + world + " is not a world!");
			}
		});
		
		return worldList;
	}
}
