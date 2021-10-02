package me.reecepbcups.aFinishLater;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.neznamy.tab.api.EnumProperty;
import me.neznamy.tab.api.TABAPI;
import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class Nickname implements Listener, CommandExecutor {//,  {

	private static Main plugin;
	private FileConfiguration MainConfig, config;
	private String Section, Permission, FILENAME;
	
	// name, new name
	public static HashMap<Player, String> nicknames = new HashMap<Player, String>();
	
	public Nickname(Main instance) {
		plugin = instance;

		Section = "Misc.Nickname";                
		if(plugin.enabledInConfig(Section+".Enabled")) {

			MainConfig = plugin.getConfig();
			Permission = MainConfig.getString(Section+".Permission");

			// make sure TAB plugin is enabled? or move to its own thing	
			try {
				Bukkit.getServer().getPluginManager().getPlugin("TAB").getConfig();
			} catch (Exception e) {
				Util.consoleMSG("&e[ServerTools] &cTAB plugin is needed for this to work");
				return;
			}
						
			plugin.getCommand("nickname").setExecutor(this);
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
			TABAPI.enableUnlimitedNameTagModePermanently();

		}
	}
	
	@EventHandler
	public void playerColoredChatEvent(AsyncPlayerChatEvent e) {	
		Player p = e.getPlayer();

		if(nicknames.containsKey(p)) {
			String nick = nicknames.get(p);
			p.setCustomName(nick);
			p.setDisplayName(nick);
		    p.setCustomNameVisible(true);					
		}	
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!(sender.hasPermission(Permission))) {		
			sender.sendMessage(Util.color("&cNo Permission to use this command :("));
			return true;			
		} 
		Player p = (Player) sender;	
		
		if (args.length == 0) {
			Util.coloredMessage(p, "/nickname <set/clear> [name]");
			return true;
		}	
			
		switch(args[0]){
			// in the future maybe add /nickname hide (to hide from scoreboard)
			case "clear":
				clearNick(p);
				return true;	
			case "set":				
				String newNickname = "";				
				for (int i = 1; i < args.length; i++) {
					if(i+1 < args.length) {
						newNickname += args[i] + " ";
					} else {
						newNickname += args[i];
					}
		        }
								
				setNick(p, newNickname);
				return true;
			default:
				return false;		
		}
		
	}
	
	public static String getPossibleNickname(Player p) {
		if(nicknames.containsKey(p)) {
			return nicknames.get(p);
		}		
		return p.getName();
	}
	
	public void clearNick(Player p) {
		String name = p.getName();
		TABAPI.getPlayer(name).setValueTemporarily(EnumProperty.CUSTOMTABNAME, name);
		TABAPI.getPlayer(name).setValueTemporarily(EnumProperty.CUSTOMTAGNAME, name);
		
		if(nicknames.containsKey(p))
			nicknames.remove(p);
		
		Util.coloredMessage(p, "&a[✓] Nickname cleared");
	}
	
	public void setNick(Player p, String nick) {
		
		if(nick.length() == 0) {
			Util.coloredMessage(p, "&c[✖] You must supply a nickname!");
			return;
		}
		
		nick = Util.color(nick);
		
		nicknames.put(p, nick);
		setTabName(p, nick);
		setTagNick(p, nick);
		
		Util.coloredMessage(p, "&a[✓] Nicknamed to:&r " + nick);
	}
	
	public void setTabName(Player p, String nick) {
		nick = Util.color(nick);
		TABAPI.getPlayer(p.getName()).setValueTemporarily(EnumProperty.CUSTOMTABNAME, nick);
	}
	
	public void setTagNick(Player p, String nick) {
		TABAPI.getPlayer(p.getName()).setValueTemporarily(EnumProperty.CUSTOMTAGNAME, nick);
	}
	
	
}
