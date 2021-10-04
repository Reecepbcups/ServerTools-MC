package sh.reece.disabled;

import sh.reece.tools.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisableCaneOnCane implements Listener {

	private static Main plugin;
	public DisableCaneOnCane(Main instance) {
        plugin = instance;
        
        if (plugin.enabledInConfig("Disabled.DisableCaneTowers.Enabled")) {
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);			
    	}
	}
	
	
	
	
	@EventHandler
	public void onBookWrite(BlockPlaceEvent e) {
		Block b = e.getBlock().getLocation().add(0,-1,0).getBlock();
		
		//e.getPlayer().sendMessage(b.getType() + " : " + e.getPlayer().getItemInHand().getType().toString());
		
		if(e.getPlayer().getItemInHand().getType() == Material.SUGAR_CANE) {
			if(b.getType() == Material.SUGAR_CANE_BLOCK) {
				e.getPlayer().sendMessage(Main.lang("DISABLED_CANE_ON_CANE"));
				e.setCancelled(true);
			}
			
		}
		
		
		
		
	}
	
	
}
