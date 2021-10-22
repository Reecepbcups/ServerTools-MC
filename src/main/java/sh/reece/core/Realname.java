package sh.reece.core;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Realname implements CommandExecutor {// ,TabCompleter,Listener {

	// realname, fake name
	//private HashMap<UUID, String> nicks = new HashMap<UUID, String>();

	private String Section, Permission;
	private final Main plugin;


	// TODO
	// does not work yet as /realname <Player> - we can not get nickname yet.
	// as we store via UUID.

	public Realname(Main instance) {
		this.plugin = instance;

		Section = "Core.Realname";

		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("realname").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission");
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission(Permission)) {
			Util.coloredMessage(sender, "&cYou do not have access to &n/" + label + "&c.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Util.color("&fUsage: &c/" + label + " <player>"));

		} else if (args.length >= 1) {


			Player target = Bukkit.getPlayer(args[0]);

			if(target == null){
				Util.coloredMessage(sender, "&c" + args[0] + " &fis not online!");

			} else {

				Util.coloredMessage(sender, "User " + args[0] + " real name is " + target.getName());

			}

		}
		
		return false;
	}		
}
