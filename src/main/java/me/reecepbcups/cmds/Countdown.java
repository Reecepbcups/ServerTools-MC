package me.reecepbcups.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class Countdown implements CommandExecutor {//,  {

	private static Main plugin;
	private Integer count;
	
	public Countdown(Main instance) {
        plugin = instance;
        
        if(plugin.enabledInConfig("Commands.Countdown.Enabled")) {
        	plugin.getCommand("countdown").setExecutor(this);
    	}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!(sender.hasPermission("countdown.use"))) {		
			sender.sendMessage(Util.color("&cNo Permission to use countdown :("));
			return true;			
		} 
		
		// /countdown ##

		// countdown with 10 seconds
		if(args.length == 0) {			
			sender.sendMessage(Util.color("&c/coutndown [seconds] [Reason For Countdown]"));
		} else {
			
			if(args.length == 1) {
				if(Util.isInt(args[0])) {
					runnable(Integer.valueOf(args[0]));
				}
			} else {
				// /countdown 10 Adding Lava in 10 seconds!
				if(Util.isInt(args[0])) {
					Util.coloredBroadcast(plugin.PREFIX+Util.argsToSingleString(1, args));
					runnable(Integer.valueOf(args[0]));
				} else {
					// /countdown Adding Lava in 10 seconds!
					sender.sendMessage(Util.color("&c/coutndown [seconds] [Reason For Countdown]"));				
				}
			}
			
		}
		return true;
	}

	String color = "&f&l";
	public void runnable(int start) {
		count = start;					
		new BukkitRunnable() {
			@Override
			public void run() {
				//methods		
				if(count==3) { color="&c&l"; }
				else if(count==2) { color="&e&l"; }
				else if(count==1) { color="&a&l"; }
				
				if(count<=1) {
					cancel();
				}
				
				Util.coloredBroadcast(plugin.PREFIX+color+ count);
				count--;
			}
		}.runTaskTimer(plugin, 0, 20L);
	}

	
}
