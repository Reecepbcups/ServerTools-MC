package sh.reece.cmds;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class GiveAll implements Listener, CommandExecutor {

	private static Main plugin;
	//private FileConfiguration config;
	private String Section;
	private Random rand;
	
	public GiveAll(Main instance) {
        plugin = instance;
        
        Section = "Commands.GiveAll";                
        if(plugin.enabledInConfig(Section+".Enabled")) {
        	
        	//config = plugin.getConfig();	

        	plugin.getCommand("giveall").setExecutor(this);	
        	rand = new Random();
    	} else {
			AlternateCommandHandler.addDisableCommand("giveall");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (!(sender.hasPermission("giveall.use"))) {		
			sender.sendMessage(Util.color("&cNo Permission to use "+label+" :("));
			return true;			
		}
		
		// Player p = (Player) sender;

		if (args.length < 2) {
			sendHelpMenu(sender);
			return true;
		}	

		
		// String essCMD = Util.argsToSingleString(0, args).toLowerCase();
		// get the material of of arg 1
		Material mat = Material.getMaterial(args[0].toUpperCase());
		if(mat == null) {
			sender.sendMessage(Util.color("&cInvalid Material " + args[0] + ". Try using /itemdb to get the item name"));
			return true;
		}

		final Integer amount = Integer.parseInt(args[1]);
		
		Util.coloredMessage(sender, "&aGiving &6" + amount + " &aof &6" + mat.name() + "&a to all players");
		
		String consoleCMD = String.format("minecraft:give @a %s %s", mat.toString().toLowerCase(), amount.toString());
		Util.console(consoleCMD);
		Util.log(consoleCMD);
		return true;				
	}
	
	public void sendHelpMenu(CommandSender s) {
		s.sendMessage("/giveall <item> <amount>");
	}
	
	
}
