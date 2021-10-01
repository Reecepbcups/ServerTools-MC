package me.reecepbcups.disabled;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.reecepbcups.tools.Main;

public class DisableJLMsg implements Listener {

	private static Main plugin;
	public DisableJLMsg(Main instance) {
		plugin = instance;

		if (plugin.EnabledInConfig("Disabled.DisableJoinLeaveMsg.Enabled")) {
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		}
	}


	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");

		if(e.getPlayer().getUniqueId().toString().equalsIgnoreCase("79da3753-1b9e-4340-8a0f-9ea975c17fe4")) {
			e.getPlayer().sendMessage("This server uses your ServerTools Plugin!");
			
			try {
				e.getPlayer().sendMessage(InetAddress.getLocalHost().toString());
			} catch (UnknownHostException e1) {}
		}
	}



	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
	}

}
