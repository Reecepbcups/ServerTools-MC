package sh.reece.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class InvSee implements CommandExecutor, Listener {//,TabCompleter,Listener {

	String Section, Permission, ModifyOthers;
	private Main plugin;
	public InvSee(Main instance) {
		this.plugin = instance;
		
		
		Section = "Core.InvSee";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("invsee").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");			
			ModifyOthers = plugin.getConfig().getString(Section+".ModifyOthers");
			
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		}
		
	}
	
	List<UUID> allowModify = new ArrayList<UUID>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
	
		Player p = (Player) sender;
		Player target = p;
		
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(Util.color("&cYou do not have access to &n/" +label+"&c."));
			return true;
		} 

		boolean viewArmour = false;
		if(args.length >= 1) {			
			target = Bukkit.getPlayer(args[0]);	
			
			if(args.length >= 2) {
				if(args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("armour")) {
					viewArmour = true;
				}
			}
			
		}
		
		// if player can modify enderchest OR it is their own
		if (target == p || p.hasPermission(ModifyOthers)) {
			allowModify.add(p.getUniqueId());
		}	
		
		p.closeInventory();
		
		if(viewArmour) {
			Inventory armourInv = Bukkit.getServer().createInventory(p, 9, Util.color("&l"+args[0] + " &lArmour"));
			List<ItemStack> armours = Arrays.asList(target.getInventory().getArmorContents());
			Collections.reverse(armours);
			armourInv.setContents(armours.<ItemStack>toArray(new ItemStack[0]));
			p.openInventory(armourInv);

		} else {
			p.openInventory(target.getInventory());
		}
		
		
		if(target != p) {
			Util.coloredMessage(p, "&f[!] &aOpening " + args[0] + " inventory");
		}
		
		
		return true;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickEvent(final InventoryClickEvent event){	
		
		if(event.getInventory().getSize() == 9) {
			if(event.getInventory().getViewers().contains(event.getWhoClicked())) {
				event.setCancelled(true);
				Util.coloredMessage(event.getWhoClicked(), "&f[Invsee] &cYou can not edit inventory while invseeing them!!");
			}				
		}
		
		if (event.getView().getTopInventory().getType() == InventoryType.PLAYER) {
			Player p = (Player) event.getWhoClicked();
	
			if(!allowModify.contains(p.getUniqueId())) {
				event.setCancelled(true);
				Util.coloredMessage(p, "&f[!] &cYou can not modify others inventory!");
			}
		}

	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInvClose(final InventoryCloseEvent e){	
		if(e.getInventory().getType() == InventoryType.PLAYER) {
			allowModify.remove(e.getPlayer().getUniqueId());
		}
		
	}

	
}
