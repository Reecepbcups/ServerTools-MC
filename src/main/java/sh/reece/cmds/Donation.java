package sh.reece.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class Donation implements CommandExecutor {

	
	private final String PERMISSION;
	private final List<String> MESSAGE;
	private List<String> FinalMSG = new ArrayList<String>();
	
	private Main plugin;
	public Donation(Main instance) {
	    this.plugin = instance;
	    
	    //this.HEADER = plugin.getConfig().getString("Donation.Header");
	    this.MESSAGE = plugin.getConfig().getStringList("Donation.Message");
	    //this.FOOTER = plugin.getConfig().getString("Donation.Footer");
	    
	    this.PERMISSION = plugin.getConfig().getString("Donation.Permission");

	    if (plugin.enabledInConfig("Donation.Enabled")) {
			plugin.getCommand("donation").setExecutor(this);
		} else {
			AlternateCommandHandler.addDisableCommand("donation");
		}
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// permission
		if (!(sender.hasPermission(PERMISSION))) { 			
			sender.sendMessage(Util.color("&cNo Permission to use the donation command"));
			return true;			
		} 
			
		// if no arguments given
		if((args.length < 2)) {
			sender.sendMessage(Util.color("&c/Donation <all/player> <IGN> <Package>"));
			return true;
		} 
		
		String announceType = args[0]; // all or player (who to announce too)
		String Reciver = args[1];
		
		String PackageName = "";				
		for (int i = 2; i < args.length; i++) {
			if(i+1 < args.length) {
				PackageName += args[i] + " ";
			} else {
				PackageName += args[i];
			}
        }
		
		
		FinalMSG.clear();			
		for(String str : MESSAGE) {
			// plugin.replaceText replaces things such as %store% with main config stuff
			FinalMSG.add(Main.replaceVariable(str.replace("%player%", Reciver).replace("%package%", PackageName)));
		}
		
		if(announceType.equalsIgnoreCase("player")) {			
			Player p = Bukkit.getPlayer(Reciver);
			if(p != null && p.isOnline()) {				
				sendPlayerLine(p);				
			}			
		} else {			
			for(Player allPlayers : Bukkit.getOnlinePlayers()) {				
				sendPlayerLine(allPlayers);						
			}
		}
		
		return true;
	}
	
	public void sendPlayerLine(Player p) {
		for(String line : FinalMSG) {
			Util.sendCenteredMessage(p, line);
		}	
	}

}
