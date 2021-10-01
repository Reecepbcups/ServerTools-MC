package me.reecepbcups.moderation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class Freeze implements Listener, CommandExecutor {

	private static Main plugin;
	public Boolean ChatEnabled;
	public String FreezePerm;
	public List<UUID> frozenPlayerList;
	private List<String> Messages;
	
	public Freeze(Main instance) {
        plugin = instance;
        
    	this.FreezePerm = plugin.getConfig().getString("Moderation.Freeze.Permission");	
    	
    	this.frozenPlayerList = new ArrayList<UUID>();
            	
    	this.Messages = plugin.getConfig().getStringList("Moderation.Freeze.Message");
    	
        if (plugin.EnabledInConfig("Moderation.Freeze.Enabled")) {				
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);	
			plugin.getCommand("Freeze").setExecutor(this);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// not needed, registering cmd does it dummy
		if(!(cmd.getName().equalsIgnoreCase("freeze") || cmd.getName().equalsIgnoreCase("ss"))) {
			return true;
		}
		
		
		if(!sender.hasPermission(FreezePerm)) {
  			Util.coloredMessage((Player)sender, "&cNo permission to use this command!");
  			return true;
		}
		
		if(args.length != 1) {  
			Util.coloredMessage((Player)sender, "&f[!] &c/freeze <player> &7| &c/unfreeze <player>");
  			return true;
		}

		
		OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
		
		if(p == null || !p.hasPlayedBefore()) {		
			Util.coloredMessage((Player)sender, "&cError! Player not found");
	  		return true;
		}
		
		if(frozenPlayerList.contains(p.getUniqueId())) {
			frozenPlayerList.remove(p.getUniqueId());
	  		Util.coloredMessage((Player)sender, "&a&n"+args[0]+"&a unfrozen!");
	  		
	  		if(Bukkit.getServer().getPlayer(p.getName()) != null) {
	  			Player player = (Player) p;
	  			Util.coloredMessage(player, Main.LANG("FREEZE_UNFROZEN"));
	  		}
	  		
		} else {
	  		frozenPlayerList.add(p.getUniqueId());
	  		Util.coloredMessage((Player)sender, "&c&n"+args[0]+"&c has been frozen!");
	  		
	  		if(Bukkit.getServer().getPlayer(p.getName()) != null) {
	  			Player player = (Player) p;
	  			
	  			if(Messages != null && Messages.size() > 0) {
	  				Messages.forEach(msg -> Util.coloredMessage(player, Main.replaceVariable(msg)));
	  			}
	  			
	  		}

	  		
		}
		return true;
	}
	
	
	
	// change this to be a runnable in the future
	@EventHandler
  	public void onMove(PlayerMoveEvent e) {
  		if(frozenPlayerList.contains(e.getPlayer().getUniqueId())) {
  			e.setCancelled(true);
  		}
  	}
	
	@EventHandler
  	public void onDrop(PlayerDropItemEvent e) {
  		if(frozenPlayerList.contains(e.getPlayer().getUniqueId())) {
  			e.setCancelled(true);
  		}
  	}
	@EventHandler
  	public void onDrop(PlayerPickupItemEvent e) {
  		if(frozenPlayerList.contains(e.getPlayer().getUniqueId())) {
  			e.setCancelled(true);
  		}
  	}
	
	@EventHandler
  	public void Damage(EntityDamageEvent e) {		
		if (e.getEntity() instanceof Player){
			if(frozenPlayerList.contains(e.getEntity().getUniqueId())) {
	  			e.setCancelled(true);
	  		}
		}  	
  	}
	
	@EventHandler
  	public void announceLogout(PlayerQuitEvent e) {		
		if(frozenPlayerList.contains(e.getPlayer().getUniqueId())) {
	  		Bukkit.broadcast(e.getPlayer().getName() + " logged out while frozen!", FreezePerm);
	  	}
	}
	@EventHandler
  	public void announceReLogin(PlayerJoinEvent e) {		
		if(frozenPlayerList.contains(e.getPlayer().getUniqueId())) {
	  		Bukkit.broadcast(e.getPlayer().getName() + " has logged back in while frozen!", FreezePerm);
	  	}
	}
	
	@EventHandler
  	public void playerChat(AsyncPlayerChatEvent e) {		
		if(frozenPlayerList.contains(e.getPlayer().getUniqueId())) {
	  		e.getPlayer().sendMessage(Main.LANG("FREEZE_DENYCHAT"));
	  		e.setCancelled(true);
	  	}
	}

	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if(frozenPlayerList.contains(e.getPlayer().getUniqueId())) {
			e.getPlayer().sendMessage(Main.LANG("FREEZE_DENYTP"));
			e.setCancelled(true);
		}
	}
	
	
}
