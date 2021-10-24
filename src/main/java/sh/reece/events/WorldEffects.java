package sh.reece.events;

import java.util.HashMap;

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
	private HashMap<String, String> world_effect = new HashMap<String, String>();
	private HashMap<Player, PotionEffectType> affectedPlayers = new HashMap<Player, PotionEffectType>();;
	
	public WorldEffects(Main instance) {
        plugin = instance;
        
        Section = "Events.WorldEffects";                
        if(plugin.enabledInConfig(Section+".Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);   
    		
    		for(String wEff : plugin.getConfig().getStringList(Section+".worlds")) {
    			world_effect.put(wEff.split(":")[0], wEff.split(":")[1]);
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
			PotionEffectType potion = PotionEffectType.getByName(world_effect.get(w).toUpperCase());
			p.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE, 1));
			affectedPlayers.put(p, potion);
		}
	}
	
	public void removeEffect(Player p) {
		if(affectedPlayers.containsKey(p)) {
			p.removePotionEffect(affectedPlayers.get(p));
		}
	}
	
	
}
