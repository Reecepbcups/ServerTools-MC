package me.reecepbcups.disabled;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;

import me.reecepbcups.tools.Main;

public class DisableWeather implements Listener {

	private List<String> worlds;
	
	private Main plugin;
	public DisableWeather(Main instance) {
		plugin = instance;
		
		if (plugin.EnabledInConfig("Disabled.DisableWeather.Enabled")) {				
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
			worlds = plugin.getConfig().getStringList("Disabled.DisableWeather.worlds");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	  public void WeatherChangeEvent(WeatherChangeEvent event) {
	    if (!event.toWeatherState())
	      return; 
	    if (worlds.contains(event.getWorld().getName())) {
	      event.setCancelled(true);
	      event.getWorld().setWeatherDuration(0);
	      event.getWorld().setThundering(false);
	    } 
	  }
	
	
}
