package me.reecepbcups.utiltools;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.reecepbcups.tools.Main;

public class TEMPLATE implements Listener, CommandExecutor {

	private static Main plugin;
	private FileConfiguration config;
	private String Section;
	
	public TEMPLATE(Main instance) {
        plugin = instance;
        
        Section = "Chat.TEMPLATE_OPTION";                
        if(plugin.EnabledInConfig(Section+".Enabled")) {
        	
        	config = plugin.getConfig();
        	//Permission = MainConfig.getString(Section+".permission");
        	
//        	// plugins/ServerTools/DATA
//        	plugin.createDirectory("DATA");
//        	FILENAME = File.separator + "DATA" + File.separator + "ChatColor.yml";
//        	plugin.createFile(FILENAME);
//        	config = plugin.getConfigFile(FILENAME);	

        	plugin.getCommand("COMMAND_NAME").setExecutor(this);
    		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);    		
    	}
	}
	
	@EventHandler
	public void playerColoredChatEvent(AsyncPlayerChatEvent e) {	
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString();			
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!(sender.hasPermission("some.permission"))) {		
			sender.sendMessage(Util.color("&cNo Permission to use "+label+" :("));
			return true;			
		}
		
		Player p = (Player) sender;

		if (args.length == 0) {
			sendHelpMenu(p);
			return true;
		}	
		
		switch(args[0]){
			// /command clear
			case "clear":				
				return true;	
			case "set":
				return true;
			default:
				sendHelpMenu(p);
				return true;		
		}		
	}
	
	public void sendHelpMenu(Player p) {
		Util.coloredMessage(p, "&f/command &7<args>");
	}
	
	
	
}
