package me.reecepbcups.moderation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class MuteChat implements Listener, CommandExecutor {

	private static Main plugin;
	public Boolean ChatEnabled;
	public String MuteChatPerm, MuteChatBypassPerm, ENABLED, DISABLED;
	
	public MuteChat(Main instance) {
        plugin = instance;
        
    	this.ChatEnabled = true;
    	
        if (plugin.EnabledInConfig("Moderation.MuteChat.Enabled")) {	
        	
        	this.MuteChatPerm = plugin.getConfig().getString("Moderation.MuteChat.Permission.Use");	
        	this.MuteChatBypassPerm = plugin.getConfig().getString("Moderation.MuteChat.Permission.Bypass");
        	
        	this.ENABLED = plugin.getConfig().getString("Moderation.MuteChat.Messages.Enabled");
        	this.DISABLED = plugin.getConfig().getString("Moderation.MuteChat.Messages.Disable");
        	
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);	
			plugin.getCommand("mutechat").setExecutor(this);
		}
	}
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {		
		if(!ChatEnabled) {			
			if(!(event.getPlayer().hasPermission(MuteChatBypassPerm))) {
				event.getPlayer().sendMessage(Main.LANG("MUTECHAT_ISMUTED"));
				event.setCancelled(true);
			}	
		}
		
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mutechat")) {
        	
        	if(!(sender.hasPermission(MuteChatPerm))) {
        		sender.sendMessage(Util.color("&cYou do not have permission to use this command!"));
        		return true;
        	}
        	
            ChatEnabled = !ChatEnabled;
                        
            String option;
    		
            if(ChatEnabled) {
            	option = ENABLED;
            } else {
            	option = DISABLED;
            }                                  
    		
    		for (Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(Util.color(option.replace("%player%", sender.getName())));
			}
           
        }
        return true;
    }
	
	
	
}
