package sh.reece.core;

import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Compass implements CommandExecutor{//,TabCompleter,Listener {

	String Section, Permission;
	private final Main plugin;
	private ConfigUtils configUtils;

	public Compass(Main instance) {
		plugin = instance;
		
		
		Section = "Core.Compass";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			configUtils = plugin.getConfigUtils();
			plugin.getCommand("compass").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");
		} else {
			AlternateCommandHandler.addDisableCommand("compass");
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(Util.color("&cYou do not have access to &n/" +cmd.getName()+"&c."));
			return true;
		} 

		Player p = (Player) sender;
		
		int bearing = (int) (p.getLocation().getYaw() + 180 + 360) % 360;
        String dir;
        if (bearing < 23) {
            dir = "North";
        } else if (bearing < 68) {
            dir = "North East";
        } else if (bearing < 113) {
            dir = "East";
        } else if (bearing < 158) {
            dir = "South East";
        } else if (bearing < 203) {
            dir = "South";
        } else if (bearing < 248) {
            dir = "South West";
        } else if (bearing < 293) {
            dir = "West";
        } else if (bearing < 338) {
            dir = "North West";
        } else {
            dir = "North";
        }

		Util.coloredMessage(p, configUtils.lang("COMPASS").replace("%dir%", dir).replace("%bearing%", bearing+""));
		
		return true;
	}
}
