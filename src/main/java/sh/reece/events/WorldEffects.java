package sh.reece.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sh.reece.tools.Main;

public class WorldEffects implements Listener {// CommandExecutor

	private static Main plugin;
	//private FileConfiguration config;
	private String Section;

	// WorldName, <PotionEffect, LevelEffect>
	private Map<String, List<Object>> world_effect = new HashMap<String, List<Object>>();
	private HashMap<Player, PotionEffectType> affectedPlayers = new HashMap<Player, PotionEffectType>();;
	
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

				List<Object> potionEffect = new ArrayList<Object>();
				potionEffect.add(wEffSplit[1]); // NIGHT_VISION
				potionEffect.add(value); // 1

    			world_effect.put(wEffSplit[0], potionEffect);		
				Main.logging("WorldEffect: " + wEffSplit[0] + " " + wEffSplit[1] + " " + value);
			}
		}
    	
	}
	
	@EventHandler
	public void playerColoredChatEvent(PlayerChangedWorldEvent e) {	
		addEffect(e.getPlayer());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {	
		addEffect(e.getPlayer());				
	}
	
	
	public void addEffect(Player p) {
		String w = p.getWorld().getName();
		
		removeEffect(p);// removes precious effect from last world change, if any
		
		if(world_effect.containsKey(w)) {	
			
			List<Object> potionEffect = world_effect.get(w);
			String worldname = (String) potionEffect.get(0);
			int value = (int) potionEffect.get(1);

			PotionEffectType potion = PotionEffectType.getByName(worldname.toUpperCase());
			p.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE, value));
			affectedPlayers.put(p, potion);
		}
	}
	
	public void removeEffect(Player p) {
		if(affectedPlayers.containsKey(p)) {
			p.removePotionEffect(affectedPlayers.get(p));
		}
	}
	
	
}
