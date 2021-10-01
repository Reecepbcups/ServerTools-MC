package me.reecepbcups.disabled;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class DisableStackablePotions implements Listener {

	private static Main plugin;
	private String Section;
	
	public DisableStackablePotions(Main instance) {
        plugin = instance;
        
       Section = "Disabled.DisableStackablePotions";                
       if(plugin.EnabledInConfig(Section+".Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);    		
    	}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void plashEvent(PotionSplashEvent e) {	
		//Util.consoleMSG("PotionSplashEvent");		
		if(e.getPotion().getShooter() instanceof Player) {
			Player shooter = (Player) e.getPotion().getShooter();

			if(shooter.getInventory().getItemInHand().getAmount() > 1){
				Util.coloredMessage(shooter, Main.LANG("DISABLED_STACKED_POTIONS"));
				e.setCancelled(true);
				
				// gives player their potion back
				ItemStack newStack = shooter.getInventory().getItemInHand().clone();
				newStack.setAmount(1);				
				shooter.getInventory().addItem(newStack);
			}
		}
		
	}
}
