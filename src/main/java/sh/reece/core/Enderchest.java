package sh.reece.core;

import java.util.ArrayList;
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

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class Enderchest implements CommandExecutor, Listener {//,TabCompleter,Listener {

	String Section, Permission, ViewOthers, ModifyOthers;
	private Main plugin;
	public Enderchest(Main instance) {
		this.plugin = instance;
		
		
		Section = "Core.Enderchest";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("enderchest").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");			
			ViewOthers = plugin.getConfig().getString(Section+".ViewOthers");
			ModifyOthers = plugin.getConfig().getString(Section+".ModifyOthers");
			
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		}
		
	}
	
	List<UUID> allowModify = new ArrayList<UUID>();
	// add event to stop editing other enderchest unless it is their own
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
	
		Player p = (Player) sender;
		Player target = p;
		
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(Util.color("&cYou do not have access to &n/" +label+"&c."));
			return true;
		} 


		if(args.length >= 1) {
			
			if(!sender.hasPermission(ViewOthers)) {
				Util.coloredMessage(p, "&f[!] &cYou can not view &f" + args[0] + "'s&c enderchest");
				return true;
			} 
			
			target = Bukkit.getPlayer(args[0]);						
		}
		
		// if player can modify enderchest OR it is their own
		if (target == p || p.hasPermission(ModifyOthers)) {
			allowModify.add(p.getUniqueId());
		}	
		
		p.closeInventory();
		p.openInventory(target.getEnderChest());
		
		if(target != p) {
			Util.coloredMessage(p, "&f[!] &aOpening " + args[0] + " ender chest");
		}
		
		
		return true;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickEvent(final InventoryClickEvent event){		
		if (event.getView().getTopInventory().getType() == InventoryType.ENDER_CHEST) {
			Player p = (Player) event.getWhoClicked();
			
			Util.consoleMSG("clicked");
			
			if(!allowModify.contains(p.getUniqueId())) {
				event.setCancelled(true);
				Util.coloredMessage(p, "&f[!] &cYou can not modify others ender chest");
			}
		}

	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInvClose(final InventoryCloseEvent e){	
		
		if(e.getInventory().getType() == InventoryType.ENDER_CHEST) {
			allowModify.remove(e.getPlayer().getUniqueId());
		}
		
	}

	
}
