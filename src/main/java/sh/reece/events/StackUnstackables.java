package sh.reece.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import sh.reece.tools.Main;

public class StackUnstackables implements Listener {

	private Main plugin;
	private List<Material> mats = new ArrayList<Material>();
	public StackUnstackables(Main plugin) {
		this.plugin = plugin;
		
		if (plugin.enabledInConfig("Events.StackUnstackables.Enabled")) {
			
			for(String _mat : plugin.getConfig().getStringList("Events.StackUnstackables.items")) {
				mats.add(Material.getMaterial(_mat.toUpperCase()));
			}
			
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
			
		}
		
		
		
	}
	
	@EventHandler
	public void onItemPickUp(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();

		//p.sendMessage(e.getItem().getItemStack().getAmount() + " < amount of items picked up");

		Material pickedUpItem = e.getItem().getItemStack().getType();
		int pickedUpItemAmount = e.getItem().getItemStack().getAmount();
		
		for (int slot = 0; slot<p.getInventory().getSize(); slot++){

			ItemStack invItem = p.getInventory().getItem(slot);

			if (invItem != null) {
				Material invType = invItem.getType();
				if (mats.contains(invType) && pickedUpItem == invType) {
					
					int inv_amount = invItem.getAmount();

					if(inv_amount < 64) {						
						p.getInventory().setItem(slot, new ItemStack(pickedUpItem, inv_amount+pickedUpItemAmount));
						//p.sendMessage("you got item" + pickedUpItem);
						//return;
					} else {

						if(inv_amount >= 64) {	
							//p.sendMessage("64 in this slot");
							// next slot, this one is full
							continue;
						}
						
					}
					e.setCancelled(true);
					e.getItem().remove();
				}
					
			}

		}
	}
}
