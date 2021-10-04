package sh.reece.core;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Ping implements CommandExecutor{//,TabCompleter,Listener {

	String Section;
	private final Main plugin;
	public Ping(Main instance) {
		plugin = instance;
		
		
		Section = "Core.Ping";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("ping").setExecutor(this);
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player)sender;
		
		int ping;
		try {
			Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
			ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
			ping = -1;
			return true;
		}
		
		Util.coloredMessage(p, Main.lang("PING").replace("%ping%", ping+""));
		return true;
		
	}
}
