package me.reecepbcups.disabled;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import me.reecepbcups.tools.Main;

public class DisableGolemPoppies implements Listener {

	private static Main plugin;
	private FileConfiguration config;
	private String Section;
	
	public DisableGolemPoppies(Main instance) {
        plugin = instance;
        
        Section = "Disabled.DisableGolemPoppies";                
        if(plugin.EnabledInConfig(Section+".Enabled")) {
        	
        	config = plugin.getConfig();
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);    		
    	}
	}
	
	@EventHandler
	public void removeRoses(EntityDeathEvent e) {
		
		if (e.getEntity().getType() == EntityType.IRON_GOLEM) {
			e.getDrops().removeIf(itemstack -> (itemstack.getType() == Material.RED_ROSE));
		}
	     
	}
	
	
}
