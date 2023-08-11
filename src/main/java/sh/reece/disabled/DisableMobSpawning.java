package sh.reece.disabled;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import sh.reece.tools.Main;

public class DisableMobSpawning implements Listener {
    private static Main plugin;
	private FileConfiguration MAINCONFIG;
	private String Section;
	private List<String> worlds;

	public DisableMobSpawning(Main instance) {
		plugin = instance;        
		Section = "Disabled.DisableMobSpawning";        

		if(plugin.enabledInConfig(Section+".Enabled")) {

			MAINCONFIG = plugin.getConfig();               	
			worlds = MAINCONFIG.getStringList(Section+".worldsToDisable");

			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

		}
	}

    // CreatureSpawnEvent
	@EventHandler
	public void NoMobSpawning(EntitySpawnEvent e) {
        if (e.getEntity() instanceof Creature || e.getEntity() instanceof Monster) {
            if (worlds.isEmpty() || worlds.contains(e.getEntity().getLocation().getWorld().getName())) {
                e.setCancelled(true);
            }
        }
	}
}
