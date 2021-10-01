package me.reecepbcups.cmds;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class ServerInfoCMDS implements Listener {//, CommandExecutor {

	private static Main plugin;
	private FileConfiguration config;
	private String Section;
	private Set<String> commands;
	
	public ServerInfoCMDS(Main instance) {
        plugin = instance;
        
        Section = "ServerInfoCMDS";        
        
        if(plugin.EnabledInConfig(Section+".Enabled")) {
        	
        	config = plugin.getConfig();
        	
        	// ex. [discord, buy]
        	commands = config.getConfigurationSection("ServerInfoCMDS.Commands").getKeys(false);
        	
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    		
    	}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();        
        String lowerMSG = e.getMessage().toLowerCase();

        String cmd = lowerMSG.split(" ")[0].replace("/", "");
        if(lowerMSG.startsWith("/") && commands.contains(cmd)) {
        	
        	if(config.getString(Section+".Commands."+cmd+".enabled").equalsIgnoreCase("true")) {
        		for(String s : config.getStringList("ServerInfoCMDS.Commands."+cmd+".message")){
        			
        			if(Main.isPAPIEnabled()) {
						s = PlaceholderAPI.setPlaceholders(p, s);
					}
        			
            		Util.coloredMessage(p, Main.replaceVariable(s));
            	}
        	}
        	e.setCancelled(true);
        }           
    }
	
	
	
	
}
