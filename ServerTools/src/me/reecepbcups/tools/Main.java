package me.reecepbcups.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


import me.reecepbcups.GUI.ChatColor;
import me.reecepbcups.GUI.FeaturesGUI;
import me.reecepbcups.GUI.NameColor;
import me.reecepbcups.GUI.ShopClickWorkAround;
import me.reecepbcups.GUI.Tags;
import me.reecepbcups.bungee.BungeeServerConnector;
import me.reecepbcups.chat.ChatCooldown;
import me.reecepbcups.chat.ChatEmotes;
import me.reecepbcups.chat.ChatFormat;
import me.reecepbcups.chat.JoinMOTD;
import me.reecepbcups.cmds.AltTP;
import me.reecepbcups.cmds.ChangeSlots;
import me.reecepbcups.cmds.ChatNumberGuesser;
import me.reecepbcups.cmds.ChatPoll;
import me.reecepbcups.cmds.Countdown;
import me.reecepbcups.cmds.DailyRewards;
import me.reecepbcups.cmds.Donation;
import me.reecepbcups.cmds.FancyAnnounce;
import me.reecepbcups.cmds.GiveAll;
import me.reecepbcups.cmds.Reclaim;
import me.reecepbcups.cmds.ReeceTools;
import me.reecepbcups.cmds.Rename;
import me.reecepbcups.cmds.ServerInfoCMDS;
import me.reecepbcups.cmds.StaffList;
import me.reecepbcups.cmds.Visibility;
import me.reecepbcups.cooldowns.EnderPearlCooldown;
import me.reecepbcups.cooldowns.GodAppleCooldown;
import me.reecepbcups.cooldowns.GoldenAppleCooldown;
import me.reecepbcups.core.AdminChat;
import me.reecepbcups.core.Broadcast;
import me.reecepbcups.core.ClearInv;
import me.reecepbcups.core.Compass;
import me.reecepbcups.core.Fly;
import me.reecepbcups.core.Gamemode;
import me.reecepbcups.core.God;
import me.reecepbcups.core.Heal;
import me.reecepbcups.core.Messaging;
import me.reecepbcups.core.Ping;
import me.reecepbcups.core.Spawn;
import me.reecepbcups.core.Speed;
import me.reecepbcups.core.TP;
import me.reecepbcups.core.Top;
import me.reecepbcups.core.Trash;
import me.reecepbcups.core.Workbench;
import me.reecepbcups.disabled.DisableBlazeDrowning;
import me.reecepbcups.disabled.DisableBookWriting;
import me.reecepbcups.disabled.DisableCactusDamage;
import me.reecepbcups.disabled.DisableCaneOnCane;
import me.reecepbcups.disabled.DisableCropTrample;
import me.reecepbcups.disabled.DisableDisconnectSpam;
import me.reecepbcups.disabled.DisableDragonEggTP;
import me.reecepbcups.disabled.DisableEndermanTP;
import me.reecepbcups.disabled.DisableFallDamage;
import me.reecepbcups.disabled.DisableGolemPoppies;
import me.reecepbcups.disabled.DisableGrassDecay;
import me.reecepbcups.disabled.DisableHunger;
import me.reecepbcups.disabled.DisableItemBurn;
import me.reecepbcups.disabled.DisableJLMsg;
import me.reecepbcups.disabled.DisableJockeys;
import me.reecepbcups.disabled.DisableLeaveDecay;
import me.reecepbcups.disabled.DisableMobAI;
import me.reecepbcups.disabled.DisablePhantomSpawn;
import me.reecepbcups.disabled.DisableStackablePotions;
import me.reecepbcups.disabled.DisableThowingItems;
import me.reecepbcups.disabled.DisableVillagerTrading;
import me.reecepbcups.disabled.DisableWaterBreakingRedstone;
import me.reecepbcups.disabled.DisableWeather;
import me.reecepbcups.disabled.DisableWitherBreak;
import me.reecepbcups.disabled.DisableWorldGuardGlitchBuilding;
import me.reecepbcups.disabled.ThreeHitGlitch;
import me.reecepbcups.events.AntiCraft;
import me.reecepbcups.events.BucketStacker;
import me.reecepbcups.events.CMDAlias;
import me.reecepbcups.events.CustomDeathMessages;
import me.reecepbcups.events.NoBedExplosion;
import me.reecepbcups.events.OnJoinCommands;
import me.reecepbcups.events.StackUnstackables;
import me.reecepbcups.events.Withdraw;
import me.reecepbcups.events.WorldEffects;
import me.reecepbcups.events.XPBottle;
import me.reecepbcups.moderation.ClearChat;
import me.reecepbcups.moderation.ColonInCommands;
import me.reecepbcups.moderation.CommandProtection;
import me.reecepbcups.moderation.CommandSpy;
import me.reecepbcups.moderation.Freeze;
import me.reecepbcups.moderation.MuteChat;
import me.reecepbcups.moderation.Report;
import me.reecepbcups.moderation.StaffAFK;
import me.reecepbcups.moderation.TPAll;
import me.reecepbcups.moderation.WhitelistBypass;
import me.reecepbcups.runnables.AutoBroadcast;
import me.reecepbcups.runnables.ClearLag;
import me.reecepbcups.runnables.LaunchPads;
import me.reecepbcups.runnables.ScheduledTask;
import me.reecepbcups.runnables.TimeChange;
import me.reecepbcups.utiltools.ConfigUpdater;
import me.reecepbcups.utiltools.Metrics;
import me.reecepbcups.utiltools.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;


public class Main extends JavaPlugin implements Listener {	

	public final String PREFIX = "&f&lSERVER &8»&r ";

	public static HashMap<String, String> ServerVariables = new HashMap<String, String>(); 
	public static List<String> ServerVariableKeys = new ArrayList<String>();

	public List<String> MODULES_LIST = new ArrayList<>();
	public Integer ENABLED_MODULES_NUMBER = 0;

	public static FileConfiguration MAINCONFIG; // used in files data is only needed to be read from

	private static Boolean isPAPIEnabled, isServerAgeEnabled;
	private static HashMap<String, String> LANG = new HashMap<String, String>();
	
	public static Chat chat = null; // used for Tags
	
	
	public void onEnable() {	


		// TODO: 
		
		loadConfig();			
		MAINCONFIG = getConfigFile("config.yml");
		
		//isPAPIEnabled = isPAPIEnabled(); // extra
		isServerAgeEnabled = false;						

		loadLocalServerVariableKeys();
		//myTime = outputTime(myTime, "Config & LocalServer Vars"); // 72MS

		if(isPAPIEnabled) {	// done in load config		
			String Section = "Misc.ServerAges";                
			if(EnabledInConfig(Section+".Enabled")) {
				(new UptimePlaceholder()).register();
				isServerAgeEnabled = true; //myTime = outputTime(myTime, "Server Age");
			}						
		}

		new DisableJLMsg(this); 
		new ReeceTools(this);			


		// Bungee
		new BungeeServerConnector(this);
		//myTime = outputTime(myTime, "Bungee Server Connector");

		//new DeathCooldown(this);
		new ScheduledTask(this);
		//myTime = outputTime(myTime, "Task At Time");

		new DisableStackablePotions(this);

		new Speed(this);
		//new Crate(this);

		new Vouchers(this);

		// COMMANDS
		new AltTP(this);
		new ChangeSlots(this);
		new ChatPoll(this);
		new CommandSpy(this);
		new DailyRewards(this);
		new Donation(this);
		new FancyAnnounce(this);
		new Rename(this);
		new ServerInfoCMDS(this);
		new TPAll(this);
		new Visibility(this);
		new Countdown(this);
		new Reclaim(this);		
		new ClearLag(this);
		new GiveAll(this);
		new StaffList(this);		

		// Core (Essentials Clone)
		// https://github.com/EssentialsX/Essentials/tree/2.x/Essentials/src/main/java/com/earth2me/essentials/commands
		new Fly(this);
		new TP(this);
		new Heal(this);
		new Gamemode(this);
		new ClearInv(this);
		new Broadcast(this);
		new AdminChat(this);
		new Workbench(this);
		new Compass(this);

		new Messaging(this);

		// test / add to config
		//new Enderchest(this);
		//new InvSee(this);
		new Trash(this);
		new Top(this);
		new God(this);		
		new Ping(this);

		// EVENTS
		new AntiCraft(this);
		new BucketStacker(this);
		new CMDAlias(this);
		new ChatColor(this);
		new ChatCooldown(this);
		new ChatEmotes(this);
		new ChatFormat(this);
		new ChatNumberGuesser(this);
		new ColonInCommands(this);
		new CustomDeathMessages(this);
		new OnJoinCommands(this);
		new JoinMOTD(this);
		new NoBedExplosion(this);
		new ShopClickWorkAround(this);
		new Spawn(this);
		new StackUnstackables(this);
		new WhitelistBypass(this);
		new WorldEffects(this);
		new DisableGolemPoppies(this);		
		new LaunchPads(this);
		new ThreeHitGlitch(this);

		// If vault is installed these will be allowed
		if(Util.isPluginInstalledOnServer("vault", "Withdraw")) {
			new Tags(this);
			new Withdraw(this);
			new XPBottle(this);
		} else {
			Util.consoleMSG("&eVault not installed. Tags, Withdraw, and XPBottle can not be enabled.");
		}				

		//Cooldown
		new EnderPearlCooldown(this);
		new GodAppleCooldown(this);
		new GoldenAppleCooldown(this);

		// Disabled
		new DisableBlazeDrowning(this);
		new DisableBookWriting(this);
		new DisableCactusDamage(this);
		new DisableCaneOnCane(this);
		new DisableCropTrample(this);
		new DisableDisconnectSpam(this);
		new DisableDragonEggTP(this);
		new DisableEndermanTP(this);
		new DisableFallDamage(this);
		new DisableGrassDecay(this);
		new DisableHunger(this);
		new DisableItemBurn(this);		
		new DisableJockeys(this);
		new DisableLeaveDecay(this);
		new DisableMobAI(this);
		new DisableThowingItems(this);
		new DisableVillagerTrading(this);
		new DisableWaterBreakingRedstone(this);
		new DisableWeather(this);
		new DisableWitherBreak(this);
		new DisableWorldGuardGlitchBuilding(this);
		new DisablePhantomSpawn(this);

		// Moderation
		new ClearChat(this);
		new CommandProtection(this);
		new Freeze(this);
		new MuteChat(this);
		new StaffAFK(this);
		new Report(this);


		// GUI's
		new FeaturesGUI(this);
		new ShopClickWorkAround(this);
		new NameColor(this);


		// Runnable Task Timers
		new AutoBroadcast(this);
		new TimeChange(this);


		// this fixes issue with variables not showing up
		new Holograms(this);


		Collections.sort(MODULES_LIST);
		FancyStartup();
	}





	public void onDisable() {
		this.saveDefaultConfig();
		MODULES_LIST.clear();;

		// discord webhook thing
		getServer().getScheduler().cancelTasks(this);

		ChangeSlots.saveNewChangeSlotsPlayers();
		DailyRewards.saveCooldownsToFile();	
		ChatColor.saveChatColorToFile();
		Holograms.removeAllStands();
		LaunchPads.stopLaunchpadChecking();

	}	


	public void loadConfig() {

		// BStats Metrics
		new Metrics(this, 11289);

		createConfig("config.yml");			
		getConfig().options().copyDefaults(true);	

		reloadLanguage();
		//List<String> ignoredSections = new ArrayList<String>();

		try {
			ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"), new ArrayList<String>());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		// done above
		isPAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
	
	public void reloadLanguage() {
		LANG.clear();
		createConfig("messages.yml");				
		FileConfiguration language = getConfigFile("messages.yml");
		for(String key : language.getKeys(false)) {
			LANG.put(key, language.getString(key));
		}
	}

	public static Boolean isPAPIEnabled() {
		return isPAPIEnabled;
	}

	public void loadLocalServerVariableKeys() {
		for(String key : MAINCONFIG.getConfigurationSection("PluginVariables").getKeys(false)){
			Main.ServerVariables.put(key, getConfig().getString("PluginVariables."+key));
			Main.ServerVariableKeys.add(key);
			//Util.consoleMSG("servertools - loaded variable " + key);
		}
	}

	public void FancyStartup() {
		String S = "&a   _____                            _______          _     \r\n" + 
				"&a  / ____|                          |__   __|        | |    \r\n" + 
				"&a | (___   ___ _ ____   _____ _ __     | | ___   ___ | |___ \r\n" + 
				"&a  \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|    | |/ _ \\ / _ \\| / __|\r\n" + 
				"&a  ____) |  __/ |   \\ V /  __/ |       | | (_) | (_) | \\__ \\\r\n" + 
				"&a |_____/ \\___|_|    \\_/ \\___|_|       |_|\\___/ \\___/|_|___/\r\n" + 
				"&b by Reecepbups. Version: " + getDescription().getVersion();	
		Util.consoleMSG(S);
	}

	// Configuration File Functions
	public FileConfiguration getConfigFile(String name) {
		return YamlConfiguration.loadConfiguration(new File(getDataFolder(), name));
	}

	public void createDirectory(String DirName) {
		File newDir = new File(getDataFolder(), DirName.replace("/", File.separator));
		if (!newDir.exists()){
			newDir.mkdirs();
		}
	}

	public void createConfig(String name) {
		File file = new File(getDataFolder(), name);

		if (!new File(getDataFolder(), name).exists()) {

			saveResource(name, false);
		}

		@SuppressWarnings("static-access")
		FileConfiguration configuration = new YamlConfiguration().loadConfiguration(file);
		if (!file.exists()) {
			try {
				configuration.save(file);
			}			
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createFile(String name) {
		File file = new File(getDataFolder(), name);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveConfig(FileConfiguration config, String name) {
		try {
			config.save(new File(getDataFolder(), name));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Boolean EnabledInConfig(String path) {
		String module = "";
		try {
			if(!getConfig().contains(path)) {
				Util.consoleMSG(Util.color("&c[TOOLS] " + path + " does not exist!!!"));						
				return false;
			}

			String pathInfo = replaceUnNeededInfo(path);
			Boolean isEnabled = false;

			if(MAINCONFIG.getString(path).equalsIgnoreCase("true")) {

				//if(!path.contains("Core.")) {} // does not show /fly, /workbench, etc					
				module = "&a"+pathInfo;
				ENABLED_MODULES_NUMBER+=1;

				isEnabled = true;
			} else {
				module = "&c"+pathInfo;	
			}			
			MODULES_LIST.add(module+"&f,&r ");

			return isEnabled;

		} catch (Exception e) {
			MODULES_LIST.add("&4"+module+"&f,&r ");
		}
		return false;

	}

	public static String replaceVariable(String line) {
		// if line contains a key from ServerVariableKeys (line has discord written in it)
		if(Arrays.stream(Main.ServerVariableKeys.toArray(new String[Main.ServerVariableKeys.size()]))
				.anyMatch(line::contains)) {

			//loop through keys, and if the line contains %TYPE%, replace it
			for (String key : Main.ServerVariableKeys) {				
				if(line.contains("%"+key+"%")) {
					line = line.replace("%"+key+"%", Main.ServerVariables.get(key));					
				}
			}
		}
		return line;
	}

	public static String replaceUnNeededInfo(String s) {		
		String[] replace = {".Enabled", "Disabled.Disable","Disabled.", "Events.", "Moderation.", "Cooldowns.", "Misc.", "Chat.", "Bungee.", "Commands.", "Core."};
		for(String Key : replace) {
			s = s.replace(Key, "");			
		}
		return s;		
	}
	public static void announcement(Boolean center, String line) {		
		String manipulatedLine = "";

		line = Main.replaceVariable(line).trim(); // puts in variables such as website

		if(line.contains("<command=")) {			
			String cmd = StringUtils.substringBetween(line, "<command=", "/>");			
			String actualmsg = Util.color(line.split("/>")[1]);

			if(actualmsg.contains("<center>")) {
				actualmsg = Util.centerMessage(actualmsg.replace("<center>", ""));
			}

			TextComponent message = new TextComponent(actualmsg);//line.replace("<command="+cmd+"/>", ""));						
			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));									
			Bukkit.getServer().spigot().broadcast(message);						
			return;
		}

		// if center is default OR it says to center that line
		if (line.contains("<center>") || center){
			manipulatedLine = Util.centerMessage(line.replace("<center>", ""));
		}

		if(manipulatedLine.equalsIgnoreCase("")) manipulatedLine = line;
		Bukkit.broadcastMessage(Util.color(manipulatedLine));

	}
	
	public static String LANG(String key) {
		return Util.color(LANG.get(key).replace("%prefix%", "&7[&eServerTools&7]&r"));
	}


}
