package me.reecepbcups.cooldowns;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.reecepbcups.utiltools.Util;
import me.reecepbcups.tools.Main;

public class EnderPearlCooldown implements Listener {

	private static Main plugin;
	private final Integer cooldownSeconds;
	private final String CooldownMSG;
	private HashMap<String, Date> CooldownHash;
	
	public EnderPearlCooldown(Main instance) {
        plugin = instance;
        
        this.cooldownSeconds = plugin.getConfig().getInt("Cooldowns.EnderPearlCooldown.Seconds");
        this.CooldownHash = new HashMap<String, Date>();
        
        this.CooldownMSG = plugin.getConfig().getString("Cooldowns.EnderPearlCooldown.Message");
        
        if (plugin.EnabledInConfig("Cooldowns.EnderPearlCooldown.Enabled")) {				
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);			
		}
	}
	
	
	
	@EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();

        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand().getType() == Material.ENDER_PEARL) {
            	
            	if(!(Util.cooldown(CooldownHash, cooldownSeconds, p.getName(), CooldownMSG))) {
            		e.setCancelled(true);        	
            	}                 
            }
        }
        return;
    }	
}
