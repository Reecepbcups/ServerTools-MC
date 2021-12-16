package sh.reece.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class ClearInv implements CommandExecutor{//,TabCompleter,Listener {

	String Section, Permission;
	private Main plugin;
	public ClearInv(Main instance) {
		this.plugin = instance;
		
		
		Section = "Core.ClearInv";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("clearinv").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");
			//plugin.getCommand("rename").setTabCompleter(this);
			//plugin.getServer().getPluginManager().registerEvents(this, plugin);
		} else {
			AlternateCommandHandler.addDisableCommand("clearinv");
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		//Player p = (Player) sender;
		
		Player target;
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage("&cYou do not have access to &n/" +cmd.getName()+"&c.");
			return true;
		} 
				
		if (args.length == 0) {	
			
			if(sender instanceof Player){
				clearinv((Player) sender);	
			} else {
				Util.consoleMSG("&fUsage: /"+cmd.getName()+" <player>");
			}	
								
			
		} else if(args.length == 1) {
			
			if(sender.hasPermission(Permission+".others")) {
				
				target = Bukkit.getPlayer(args[0]);
				String output = "&f[&c!&f] &cTarget " + args[0] + " is not online.";
				
				if(target != null) {
					clearinv(target);
					output = "&f[&c!&f] &eCleared " + target.getDisplayName() + "&e inventory";
				}				
				Util.coloredMessage(sender, output);				
			}
		}
		return true;
	}
	
	
	public void clearinv(Player p) {
		p.getInventory().clear();
		Util.coloredMessage(p, "&7[&c!&7] &fYour inventory has been cleared.");
	}
	
}
