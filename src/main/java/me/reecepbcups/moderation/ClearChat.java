package me.reecepbcups.moderation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class ClearChat implements CommandExecutor {

	private static Main plugin;
	public String ClearChatPerm;
	public Integer ClearChatLoops;
	
	public ClearChat(Main instance) {
	        plugin = instance;	
	        
	        if (plugin.enabledInConfig("Moderation.ClearChat.Enabled")) {
	        	
	        	this.ClearChatPerm = plugin.getConfig().getString("Moderation.ClearChat.Permission");
	 	        this.ClearChatLoops = plugin.getConfig().getInt("Moderation.ClearChat.Messages.lines");
	 	        
				plugin.getCommand("clearchat").setExecutor(this);
			}	
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearchat")) {
            
        	if(!(sender.hasPermission(ClearChatPerm))) {
        		sender.sendMessage(Util.color("&cYou do not have permission to use this command!"));
        		return true;
        	}
            
        	for(int i = 0; i < ClearChatLoops; ++i) {
        		Bukkit.broadcastMessage(" ");
        	}   
        	
        	Bukkit.broadcastMessage(Util.color(plugin.getConfig().getString("Moderation.ClearChat.Messages.msg").replace("%player%", sender.getName())));

    		
           
        }
        return true;
    }

}
