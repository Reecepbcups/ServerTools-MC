package sh.reece.core;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminChat implements CommandExecutor{//,TabCompleter,Listener {

	String Section, Permission;
	private final Main plugin;
	public AdminChat(Main instance) {
		plugin = instance;
		
		
		Section = "Core.AdminChat";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("adminchat").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Player p = (Player) sender;
		
		if (!sender.hasPermission(Permission)) {
			Util.coloredMessage(sender, "&cYou do not have access to &n/" +cmd.getName()+"&c.");
			return true;
		} 
				
		if (args.length == 0) {										
			sender.sendMessage(Util.color("&fUsage: &c/"+label+" <message>"));
		} else if(args.length >= 1) {
			

			String adminChatMSG = Main.lang("ADMINCHAT")
					.replace("%player%", sender.getName())
					.replace("%msg%", Util.argsToSingleString(0, args));
			
			Bukkit.broadcast(adminChatMSG, Permission);	
			Util.consoleMSG(adminChatMSG);
		}
		
		return true;
	}
}
