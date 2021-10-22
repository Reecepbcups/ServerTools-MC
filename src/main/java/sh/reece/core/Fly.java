package sh.reece.core;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor{//,TabCompleter,Listener {

	String Section, Permission;
	private final Main plugin;
	public Fly(Main instance) {
		plugin = instance;
		
		
		Section = "Core.Fly";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("fly").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");
			//plugin.getCommand("rename").setTabCompleter(this);
			//plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission(Permission)) {
			Util.coloredMessage(sender, "&cYou do not have access to &n/" +cmd.getName()+"&c.");
			return true;
		} 

		
		//Player p = (Player) sender;
		if (args.length == 0) {		
			if(sender instanceof Player) {
				toggleFlying((Player) sender);	
			} else {
				Util.consoleMSG("&fUsage: &c/fly <player>");
			}
								
			return true;
		}
		
		if(args.length == 1) {
			if(sender.hasPermission(Permission+".others")) {
				
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					Util.coloredMessage(sender, "&f[&c!&f] &cTarget " + args[0] + " is not online.");
					return true;
				}
				
				toggleFlying(target);
				Util.coloredMessage(sender, "&f[&c!&f] &eToggled " + args[0] + " flight mode to &e" + target.getAllowFlight());
			}
		}
		
		
		return true;
	}
	
	
	public void toggleFlying(Player p) {
		if(p.getAllowFlight()) {
			p.setAllowFlight(false); // makes sure if they are not flying and they toggle, that it saves
			//p.setFlying(false);
			Util.coloredMessage(p, Main.lang("FLY_DISABLED").replace("%player%", p.getDisplayName()));
		} else {
			p.setAllowFlight(true);
			//p.setFlying(true);
			Util.coloredMessage(p, Main.lang("FLY_ENABLED").replace("%player%", p.getDisplayName()));
		}
	}
	
}
