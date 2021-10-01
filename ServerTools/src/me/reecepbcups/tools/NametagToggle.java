package me.reecepbcups.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCommandExecutor;
import org.bukkit.command.TabExecutor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.reecepbcups.utiltools.Util;

public class NametagToggle implements CommandExecutor, TabExecutor {

	Main main;
	public Team playerset;

	public NametagToggle(Main main) {
		this.main = main;

		if(!Util.isVersion1_8()) {
			return;
		}
		
		Scoreboard score = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		Team t = score.getTeam("stoolssbtags");
		if (t == null)
			t = score.registerNewTeam("stoolssbtags"); 
		this.playerset = t;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 2 && 
				args[0].equalsIgnoreCase("nametag") && (sender.isOp() || sender.hasPermission("evnt.nametoggle")))
			if (args[1].equalsIgnoreCase("show")) {
				playerset.setNameTagVisibility(NameTagVisibility.ALWAYS);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTurned on name tag visibility"));
			} else if (args[1].equalsIgnoreCase("hide")) {
				playerset.setNameTagVisibility(NameTagVisibility.NEVER);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTurned off name tag visibility"));
			}  
		return true;

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1)
			return Collections.singletonList("nametag"); 
		if (args.length == 2 && 
				args[0].equals("nametag"))
			return Arrays.asList(new String[] { "show", "hide" }); 
		return null;
	}
}