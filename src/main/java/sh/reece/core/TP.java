package sh.reece.core;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TP implements CommandExecutor{//,TabCompleter,Listener {

	String Section, TP, TPA, TPHere;
	private final Main plugin;
	
	// to, from
	private final Map<Player, Player> currentRequest = new HashMap<>();
	
	public TP(Main instance) {
		plugin = instance;


		Section = "Core.Teleport";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("teleport").setExecutor(this);
			
			TP = plugin.getConfig().getString(Section+".Permissions.TP");
			TPA = plugin.getConfig().getString(Section+".Permissions.TPA");
			TPHere = plugin.getConfig().getString(Section+".Permissions.TPHere");
		}

	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		
		
		switch (label.toLowerCase()) {
		// done here as its just a single command, and has no args
		case "tpaccept":	
		case "tpyes":
			if(currentRequest.containsKey(p)) {
				// accepted teleport request	
				Player from = currentRequest.get(p);
				Util.coloredMessage(from, Main.lang("TELEPORT_HASACCEPTED").replace("%player%", p.getName()));
				Util.coloredMessage(p, Main.lang("TELEPORT_ACCEPTED").replace("%player%", from.getName()));
				from.teleport(p);
				currentRequest.remove(p);
				
			} else {
				Util.coloredMessage(p, Main.lang("TELEPORT_NOREQUEST"));
			}
			return true;	
			
			
		case "tpcancel":
		case "tpdeny":
		case "tpno":
			if(currentRequest.containsKey(p)) {
				Player from = currentRequest.get(p);
				Util.coloredMessage(from, Main.lang("TELEPORT_HASDENIED").replace("%player%", p.getName()));
				Util.coloredMessage(p, Main.lang("TELEPORT_DENIED").replace("%player%", from.getName()));
				currentRequest.remove(p);
			} else {
				Util.coloredMessage(p, Main.lang("TELEPORT_NOREQUEST"));
			}
			return true;	
			
		default:
			break;	
		}
		
		
		if (args.length == 0) {										
			Util.coloredMessage(p, "&fUsage: &c/"+label+" <player>");
		} else {
			Player target = Bukkit.getPlayer(args[0]);
			String output = "&cPlayer "+args[0]+" is not online.";
			
			if(target == p) {
				output = Main.lang("TELEPORT_SELF");
			} else if(target != null) {			
				// Util.consoleMSG(target.getName());
				
				switch (label.toLowerCase()) {
				case "tp":
				case "tpo":	
					if(checkPerm(p, label, TP)) {
						output = Main.lang("TELEPORT_TO").replace("%player%", args[0]);
						p.teleport(target);
					} else {
						return true;
					}
					break;

				case "tphere":
					if(checkPerm(p, label, TPHere)) {
						output = "&aTeleported &f" + args[0] + " &ato &fYou";					
						target.teleport(p);
						Util.coloredMessage(target,Main.lang("TELEPORT_TO").replace("%player%", p.getName()));
					} else {
						return true;
					}
					break;

				//  TPA TPACCEPT TPDENY
				case "tpa":					
					// check cooldown here, "You must wait a " + cooldown + " second cooldown in between teleport requests!"					
					if(checkPerm(p, label, TPA)) {
						sendRequest(p, target);	
						return true;
					} 					
					break;			
					
				default:
					break;
				}	
				
			}
			Util.coloredMessage(p, output);
			
		}
		return true;
	}	
	
	
	private void sendRequest(Player from, Player to) {
		Util.coloredMessage(from, Main.lang("TELEPORT_SENT_REQUEST").replace("%player%", to.getName()));
		
		Util.coloredMessage(to, Main.lang("TELEPORT_GOT_REQUEST1").replace("%player%", from.getName()));
		Util.coloredMessage(to, Main.lang("TELEPORT_GOT_REQUEST2").replace("%player%", from.getName()));
		currentRequest.put(to, from);
		
	}
	
	public boolean killRequest(Player p) {
	    if (currentRequest.containsKey(p)) {
	      Player loser = currentRequest.get(p);
	      if (loser != null) {
			  loser.sendMessage(Main.lang("TELEPORT_TIMEOUT"));
		  }
	      currentRequest.remove(p);
	      return true;
	    } 
	    return false;
	  }
	
	public boolean checkPerm(Player p, String CMD, String perm) {
		if (!p.hasPermission(perm)) {
			Util.coloredMessage(p, "&cYou do not have access to &n/"+CMD+"&c.");
			return false;
		} 		
		return true;
	}
	
}
