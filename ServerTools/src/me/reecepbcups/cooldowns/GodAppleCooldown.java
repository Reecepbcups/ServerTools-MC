package me.reecepbcups.cooldowns;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class GodAppleCooldown implements Listener {

	private static Main plugin;
	private final Integer cooldownSeconds;
	private final String CooldownMSG;
	private String InitEatenMSG;
	private HashMap<String, Date> AppleCooldown;
	
	public GodAppleCooldown(Main instance) {
        plugin = instance;
        
        this.cooldownSeconds = plugin.getConfig().getInt("Cooldowns.GodAppleCooldown.Seconds");
        this.AppleCooldown = new HashMap<String, Date>();
        
        this.CooldownMSG = plugin.getConfig().getString("Cooldowns.GodAppleCooldown.Message");
        this.InitEatenMSG = plugin.getConfig().getString("Cooldowns.GodAppleCooldown.StartCooldownMSG");
        InitEatenMSG = InitEatenMSG.replace("%seconds%", cooldownSeconds.toString());
        
        if (plugin.EnabledInConfig("Cooldowns.GodAppleCooldown.Enabled")) {				
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);			
		}
	}
	
	
//	if(!(Util.cooldown(CooldownHash, cooldownSeconds, p.getName(), CooldownMSG))) {
//		e.setCancelled(true);        	
//	}   
	
	@EventHandler
	public void Gapple_Cooldown(PlayerItemConsumeEvent e) {
		final Player p = e.getPlayer();
		final ItemStack TYPE = e.getItem();
		ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
		
		if (TYPE.getType().equals(Material.GOLDEN_APPLE) && TYPE.getData().equals(apple.getData())) {
			if(!(Util.cooldown(AppleCooldown, cooldownSeconds, p.getName(), CooldownMSG))) {
        		e.setCancelled(true);        	
        	} else {
        		if(InitEatenMSG.length() > 0) {
        			Util.coloredMessage(p, InitEatenMSG);
        		}
        		
        	}
		} 
	}
	
	
}
