package me.reecepbcups.core;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.List;

public class Spawn implements Listener, CommandExecutor {

	private static Main plugin;
	private final String Section;
	private String Permission;

	private static FileConfiguration Config;
	private static String spawnLoc;

	private static List<String> voidDisabledWorlds = new ArrayList<String>();
	private String voidmsg, voidTPEnabled, spawnOnInitJoin, spawnFirstUniqueJoinOnly;

    // TODO:
    // add a spawn area option maybe? where you get 2 points to spawn between
    // helps with larger player counts

	public Spawn(Main instance) {
		plugin = instance;

		Section="Core.Spawn"; // change to Core.
		if (plugin.enabledInConfig(Section+".Enabled")) {
			plugin.createConfig("spawn.yml");
			Config = plugin.getConfigFile("spawn.yml");
			spawnLoc = Config.getString("spawn.location");

			Permission = plugin.getConfig().getString(Section+".Permission"); // to send others to spawn
			
			spawnOnInitJoin = plugin.getConfig().getString(Section+".onJoinInstantly");
			spawnFirstUniqueJoinOnly = plugin.getConfig().getString(Section+".spawnFirstUniqueJoinOnly");
			
			voidTPEnabled = plugin.getConfig().getString(Section+".teleportWhenInVoid.enabled");			
			voidmsg = plugin.getConfig().getString(Section+".teleportWhenInVoid.message");
			voidDisabledWorlds = plugin.getConfig().getStringList(Section+".teleportWhenInVoid.disabledWorlds");

			plugin.getCommand("spawn").setExecutor(this);
			plugin.getCommand("setspawn").setExecutor(this);
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		}
	}

	// spawn & /setspawn command
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;

		if (command.getLabel().equalsIgnoreCase("setspawn")) {

			if (!player.hasPermission("spawn.admin")) {
				Util.coloredMessage(sender, "&cYou can not use the setspawn command! :(");
				return true;
			} 
			
			Location l = player.getLocation();
			spawnLoc = l.getWorld().getName()+";"+l.getX()+";"+l.getY()+";"+l.getZ()+";"+l.getYaw()+";"+l.getPitch();

			Config.set("spawn.location", spawnLoc);
			plugin.saveConfig(Config, "spawn.yml");
			Util.coloredMessage(sender, Main.lang("SPAWN_SET"));
			return true;			
		}


		if (command.getLabel().equalsIgnoreCase("spawn")) {
			
			String output = "";
			if (spawnLoc == "" || spawnLoc == null) {
				output = Main.lang("SPAWN_NONE");
				
			} else {
				if(args.length == 0) {
					output = Main.lang("SPAWN_TP");
					player.teleport(getSpawnLocation());

				} else {	
					
					if(sender.hasPermission(Permission)) {
						Player target = Bukkit.getPlayer(args[0]);
						
						if(target != null) {
							output = "&7&l[&c&l!&7&l] &fSent " + args[0] + " to spawn!";
							target.teleport(getSpawnLocation());
							Util.coloredMessage(target, Main.lang("SPAWN_SENT_TO_SPAWN").replace("%sender%", sender.getName()));
						}	
					} else {
						output = "&7&l[&c&l!&7&l] &cYou dont have permission for that... &7("+Permission+")";
					}	
				}
			}
			
			Util.coloredMessage(sender, output);
			
			
			
			
			
			return true;
		}
		return true;
	}

	@EventHandler //(priority = EventPriority.NORMAL)
	public void onDamageFromVoid(EntityDamageEvent e) {
		// when player takes damage in void, teleport them to spawn
		// if they were hurt because of void & are a player
		if(e.getCause() == DamageCause.VOID && e.getEntity() instanceof Player){

			// if void TP is enabled
			if(voidTPEnabled.equalsIgnoreCase("true")) {
				Player p = (Player) e.getEntity();

				// if worlds is not null and player is not in the world which we did not enable
				if (voidDisabledWorlds != null && 
					voidDisabledWorlds.contains(p.getLocation().getWorld().getName())) {
					return;
				} 

				// get spawn location and move them there
				p.teleport(getSpawnLocation());
				e.setCancelled(true);
				Util.coloredMessage(p, voidmsg);
			}
		}
	}

	@EventHandler //(priority = EventPriority.HIGHEST)
	public void playerKillEvent(PlayerRespawnEvent e) {
		e.setRespawnLocation(getSpawnLocation());
	}
	
	// if player has not played before, force their spawn to be
	// in the right place
	@EventHandler //(priority = EventPriority.HIGHEST)
	public void playerFirstTime(PlayerSpawnLocationEvent e) {
		if(!e.getPlayer().hasPlayedBefore()) {
			e.setSpawnLocation(getSpawnLocation());
		}		
	}

	@EventHandler //(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if((!p.hasPlayedBefore())) {
			if(spawnFirstUniqueJoinOnly.equalsIgnoreCase("true")) {
				sendToSpawn(p);		
				return;
			}			
		}
		
		
		if(spawnOnInitJoin.equalsIgnoreCase("true")) {
			sendToSpawn(p);
		}
	}

	public void sendToSpawn(Player p) {
		new BukkitRunnable() {
            @Override
            public void run() {
            	p.teleport(getSpawnLocation());
            }	           
        }.runTaskLater(plugin, 2L);
	}
	
	public Location getSpawnLocation() {
		String[] loc = spawnLoc.split(";");
		Location location = new Location(Bukkit.getWorld(loc[0]),
				Double.parseDouble(loc[1]), 
				Double.parseDouble(loc[2])+0.5, 
				Double.parseDouble(loc[3]), 
				Float.parseFloat(loc[4]), 
				Float.parseFloat(loc[5]));
		return location;
	}
}
