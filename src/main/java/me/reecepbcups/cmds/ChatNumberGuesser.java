package me.reecepbcups.cmds;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class ChatNumberGuesser implements Listener{

	private static Main plugin;
	private String command, AdminPerm;

	public Boolean Running;
	public Integer Number;

	public ChatNumberGuesser(Main instance) {
		plugin = instance;

		String section = "Commands.ChatNumberGuess";
		
		if(plugin.enabledInConfig(section+".Enabled")) {
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

			command = "/"+plugin.getConfig().getString(section+".command");
			AdminPerm = plugin.getConfig().getString(section+".AdminPerm");
			Running = false;
		}
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent e) {

		//e.getPlayer().sendMessage(e.getMessage());

		if (!e.getMessage().toLowerCase().startsWith(command)){
			return;
		}

		Player p = e.getPlayer();

		if (!(p.hasPermission(AdminPerm))) {
			p.sendMessage(Util.color("&cNo Permission " + AdminPerm));
			return;			
		} 
		e.setCancelled(true);

		String[] args = e.getMessage().replace(command, "").split(" ");

		//p.sendMessage(args.length + "");

		if(args.length <= 1) {
			Util.coloredMessage(p, "&cUSAGE: /guess (start/stop)");
			return;        	
		}

		//p.sendMessage(args);

		if(args[1].equalsIgnoreCase("start")) {
			if(Running) {
				Util.coloredMessage(p, "&cGame already running!");
				return;
			} 

			if(args.length > 0) {   
				Number  = new Random().nextInt(100-1);
				Util.coloredMessage(p, "&7&o(( random number " + Number + " chosen ))");

				Running = true;


			}
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(Util.color(" &fPick a number between &a1 &f- &a100!"));
			Bukkit.broadcastMessage("");
		}

		if(args[1].equalsIgnoreCase("stop")) {
			if(Running) {
				Bukkit.broadcastMessage(Util.color("  &cThe chat guess game has been stopped by " + p.getName()));
			} 



		}


	}



	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (!Running) {
			return;
		}

		if (e.getMessage().equalsIgnoreCase(String.valueOf(Number))) {
			Running = false;

			String msg = "&c%player% has won the guessing game! The number was %number%.";
			msg = msg.replace("%player%", e.getPlayer().getName());
			msg = msg.replace("%number%", Number.toString());
			Bukkit.broadcastMessage(Util.color(msg));
		} 
	}









}
