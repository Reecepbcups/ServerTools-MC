package me.reecepbcups.disabled;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import me.reecepbcups.tools.Main;

public class DisableMobAI implements Listener {//, CommandExecutor {

	private static Main plugin;
	private FileConfiguration MainConfig;
	private String Section;
	private List<String> worlds;

	public DisableMobAI(Main instance) {
		plugin = instance;        
		Section = "Disabled.DisableMobAI";        

		if(plugin.EnabledInConfig(Section+".Enabled")) {   

			MainConfig = plugin.getConfig();               	
			worlds = MainConfig.getStringList(Section+".worldsToDisable");

			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

		}
	}

	@EventHandler
	public void MobAI(EntityTargetLivingEntityEvent e) {
		if (worlds.contains(e.getEntity().getLocation().getWorld().getName())) {
			e.setCancelled(true); 
		}
	}
	
}
