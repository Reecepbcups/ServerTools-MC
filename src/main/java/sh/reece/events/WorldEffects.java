package sh.reece.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sh.reece.tools.Main;

public class WorldEffects implements Listener {// CommandExecutor

	private static Main plugin;
	//private FileConfiguration config;
	private String Section;

	// WorldName, <PotionEffectName, LevelEffect>
	private Map<String, Map<String, Integer>> world_effect = new HashMap<String, Map<String, Integer>>();
	// private HashMap<Player, Map<String, Integer>> affectedPlayers = new HashMap<Player, Map<String, Integer>>();

	// USER, World
	private HashMap<UUID, String> affectedPlayers = new HashMap<UUID, String>();
	
	public WorldEffects(Main instance) {
        plugin = instance;
        
        Section = "Events.WorldEffects";                
        if(plugin.enabledInConfig(Section+".Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);   
    		
    		for(String wEff : plugin.getConfig().getStringList(Section+".worlds")) {
				String[] wEffSplit = wEff.split(":");

				int value = 1;
				if(wEffSplit.length > 2) {									
					try { // effect value level	
						value = Integer.valueOf(wEffSplit[2]);
					} catch (Exception e) {
						Main.logging(wEffSplit[2] + " is not a valid number");
					}
				}

				// gets or creates a list of effects & values to add too
				// List<List<Object>> eff = world_effect.get(wEffSplit[0]);
				// if(eff == null) {
				// 	eff = new ArrayList<List<Object>>();
				// }

				Map<String, Integer> eff = world_effect.get(wEffSplit[0]);
				if(eff == null) {
					eff = new HashMap<String, Integer>();
				}

				// creates a single effect & its strength value
				// List<Object> potionEffect = new ArrayList<Object>();
				// potionEffect.add(wEffSplit[1]); // NIGHT_VISION
				// potionEffect.add(value); // 1
				eff.put(wEffSplit[1], value);

				// adds the potion to the eff list, which is a list of all effects for a given world
				// eff.add(potionEffect);

				// saves the new list to the world_effect map
				world_effect.put(wEffSplit[0], eff);
				Main.logging("WorldEffect: " + wEffSplit[0] + " " + wEffSplit[1] + " " + value);
			}
			
		}
    	
	}
	
	@EventHandler
	public void playerChangeWorldEvent(PlayerChangedWorldEvent e) {	
		addEffect(e.getPlayer());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {	
		addEffect(e.getPlayer());				
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {	
		removeEffect(e.getPlayer());				
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {	
		removeEffect(e.getPlayer());				
	}
	
	
	public void addEffect(Player p) {
		String w = p.getWorld().getName();
		
		removeEffect(p); // removes precious effect from last world change, if any
		
		if(world_effect.containsKey(w)) {	
			
			// gets effects for world
			Map<String, Integer> potionEffects = world_effect.get(w);

			// iterates through all effects for a given world
			for (String potionkey : potionEffects.keySet()) {
				// String worldname = potionkey;
				int value = potionEffects.get(potionkey);

				PotionEffectType potion = PotionEffectType.getByName(potionkey.toUpperCase());
				p.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE, value));
				// affectedPlayers.put(p, potion);
				
				Main.logging("Added: " + potionkey + " to " + p.getName());
			}

			// adds all potion effects to memory so we can clear on change / kick
			affectedPlayers.put(p.getUniqueId(), w);			
		}
	}
	
	public void removeEffect(Player p) {
		if(affectedPlayers.containsKey(p.getUniqueId())) {
			// for(Entry<String, Integer> effects : affectedPlayers.get(p).entrySet()){
			// 	PotionEffectType potion = PotionEffectType.getByName(effects.getKey().toUpperCase());
			// 	p.removePotionEffect(potion);
			// 	Main.logging("Removed: " + effects.getKey() + " from " + p.getName());
			// }

			String worldName = p.getWorld().getName();
			for(Entry<String, Integer> effects : world_effect.get(worldName).entrySet()) {
				PotionEffectType potion = PotionEffectType.getByName(effects.getKey().toUpperCase());
				p.removePotionEffect(potion);
				Main.logging("Removed: " + effects.getKey() + " from " + p.getName());
			}

			affectedPlayers.remove(p.getUniqueId());

		}
	}
	
	
}
