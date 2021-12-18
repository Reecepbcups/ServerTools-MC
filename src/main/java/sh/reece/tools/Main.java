package sh.reece.tools;

import sh.reece.utiltools.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


// TODO
/*
	Finish Warp.java
	Finish  /enchant with unsafe enchants (add to loader)

	warps - https://github.com/IzanamiNeko/LobbySystem2021
	https://github.com/DownThePark/SetHome
*/

public class Main extends JavaPlugin implements Listener {	

	public final String PREFIX = "&f&lSERVER &8»&r ";

	public static final HashMap<String, String> SERVER_VARIABLES = new HashMap<>();
	public static final List<String> SERVER_VARIABLE_KEYS = new ArrayList<>();

	public final List<String> modulesList = new ArrayList<>();

	private static boolean isPAPIEnabled;
	
	public static Chat chat = null; // used for Tags
	private Loader loader;
	private ConfigUtils configUtils;

	public void onEnable() {
		loader = new Loader(this);

		configUtils = new ConfigUtils(this);
		configUtils.loadConfig();			
		loader.setMarking("Configurations");	
		
		loader.loadPlaceholderAPI();
		loader.loadCommands();
		loader.loadCore();
		loader.loadEvents();
		loader.loadVaultDependentPlugins();
		loader.loadCooldowns();
		loader.loadToggleableFeatures();
		loader.loadModeration();
		loader.loadGUIs();
		loader.loadRunnableTask();
		
		Collections.sort(modulesList);
		loader.output();

		// Must be last, loads aliases for commands disabled in stools.
		// Ex: If we disable Fly in Core.Fly.Enabled, this will make sure
		// on the /fly command, other plugins will take hold such as Essentials.
		// Fixes plugin.yml usage showing up only for disabled commands.
		new AlternateCommandHandler(this);
	}

	public void onDisable() {
		loader.unloadAll();
	}	

	public Boolean isPAPIEnabled() {
		return isPAPIEnabled;
	}
	public void setPAPIStatus(boolean state){
		isPAPIEnabled = state;
	}

	public ConfigUtils getConfigUtils(){
		return configUtils;
	}

	public Boolean enabledInConfig(final String path) {
		String module = "";
		try {
			if (!getConfig().contains(path)) {
				Util.consoleMSG(Util.color("&c[TOOLS] " + path + " does not exist!!!"));						
				return false;
			}

			final String pathInfo = replaceUnNeededInfo(path);
			boolean isEnabled = false;

			if (getConfig().getString(path).equalsIgnoreCase("true")) {

				//if(!path.contains("Core.")) {} // does not show /fly, /workbench, etc					
				module = "&a"+pathInfo;

				isEnabled = true;
			} else {
				module = "&c"+pathInfo;	
			}			
			modulesList.add(module+"&f,&r ");

			return isEnabled;

		} catch (final Exception e) {
			modulesList.add("&4"+module+"&f,&r ");
		}
		return false;

	}

	public static String replaceVariable(String line) {
		// if line contains a key from ServerVariableKeys (line has discord written in it)
		if (Arrays.stream(SERVER_VARIABLE_KEYS.toArray(new String[SERVER_VARIABLE_KEYS.size()])).anyMatch(line::contains)) {

			//loop through keys, and if the line contains %TYPE%, replace it
			for (String key : SERVER_VARIABLE_KEYS) {
				if(line.contains("%"+key+"%")) {
					line = line.replace("%"+key+"%", SERVER_VARIABLES.get(key));
				}
			}
		}
		return line;
	}

	public static String replaceUnNeededInfo(String s) {		
		final String[] replace = {".Enabled", "Disabled.Disable","Disabled.", "Events.", "Moderation.", "Cooldowns.", "Misc.", "Chat.", "Bungee.", "Commands.", "Core."};
		for(final String Key : replace) {
			s = s.replace(Key, "");			
		}
		return s;		
	}
	public static void announcement(final Boolean center, String line) {
		String manipulatedLine = "";

		line = replaceVariable(line).trim(); // puts in variables such as website

		if (line.contains("<command=")) {
			final String cmd = StringUtils.substringBetween(line, "<command=", "/>");
			String actualMessage = Util.color(line.split("/>")[1]);

			if(actualMessage.contains("<center>")) {
				actualMessage = Util.centerMessage(actualMessage.replace("<center>", ""));
			}

			final TextComponent message = new TextComponent(actualMessage);
			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));									
			Bukkit.getServer().spigot().broadcast(message);			
			return;
		}

		// if center is default OR it says to center that line
		if (line.contains("<center>") || center){
			manipulatedLine = Util.centerMessage(line.replace("<center>", ""));
		}

		if (manipulatedLine.equalsIgnoreCase("")) {
			manipulatedLine = line;
		}
		Bukkit.broadcastMessage(Util.color(manipulatedLine));

	}
	
	
	// creqate new logger
	public static Logger logger = Logger.getLogger("ServerTools");
	public static void logging(final String message) {
		logger.info(message);
	}


}
