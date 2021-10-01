package me.reecepbcups.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class God implements CommandExecutor, Listener {//,TabCompleter,Listener {

	String Section, Permission;
	private Main plugin;
	public God(Main instance) {
		this.plugin = instance;

		Section = "Core.God";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.EnabledInConfig(Section+".Enabled")) {
			plugin.getCommand("god").setExecutor(this);
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
			
			Permission = plugin.getConfig().getString(Section+".Permission");
		}
		
	}
	
	private static List<Player> GODS = new ArrayList<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(Util.color("&cYou do not have access to &n/" +label+"&c."));
			return true;
		} 
		
		Player p = (Player) sender;
		
		boolean b = (GODS.contains(p) ? GODS.remove(p) : GODS.add(p));
		Util.coloredMessage(p, "&f[!] &fGod mode " + (GODS.contains(p) ? "&aenabled" : "&cdisabled") + "&f.");		
		return true;
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=false)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && GODS.contains(e.getEntity())) {
			e.setCancelled(true);
			((Player) e.getEntity()).setHealth(((Player) e.getEntity()).getMaxHealth());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		GODS.remove(e.getPlayer());
	}
}
