package sh.reece.cmds;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class GiveAll implements Listener, CommandExecutor {

	private static Main plugin;
	private FileConfiguration config;
	private String Section;
	private Random rand;
	
	public GiveAll(Main instance) {
        plugin = instance;
        
        Section = "Commands.GiveAll";                
        if(plugin.enabledInConfig(Section+".Enabled")) {
        	
        	config = plugin.getConfig();	

        	plugin.getCommand("giveall").setExecutor(this);	
        	rand = new Random();
    	}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!(sender.hasPermission("giveall.use"))) {		
			sender.sendMessage(Util.color("&cNo Permission to use "+label+" :("));
			return true;			
		}
		
		// Player p = (Player) sender;

		if (args.length == 0) {
			sendHelpMenu(sender);
			return true;
		}	
		
		String essCMD = Util.argsToSingleString(0, args).toLowerCase();
		
		for(Player onlineP : Bukkit.getOnlinePlayers()) {
			new BukkitRunnable(){
				public void run() {	
					Util.console(String.format("essentials:give %s %s", onlineP.getName(), essCMD));
					//Util.coloredMessage(onlineP, "&fGiven "+args[1]+" "+args[0]+" by " + sender.getName());
				}
			}.runTaskLater(plugin, rand.nextInt(20)); // 1 second delay			
		}		
		return true;				
	}
	
	public void sendHelpMenu(CommandSender s) {
		s.sendMessage("/giveall <item:numeric> [amount [itemmeta...]]");
	}
	
	
}
