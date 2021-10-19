package sh.reece.tools;

import sh.reece.GUI.*;
import sh.reece.bungee.BungeeServerConnector;
import sh.reece.chat.*;
import sh.reece.cmds.*;
import sh.reece.cooldowns.EnderPearlCooldown;
import sh.reece.cooldowns.GodAppleCooldown;
import sh.reece.cooldowns.GoldenAppleCooldown;
import sh.reece.core.*;
import sh.reece.disabled.*;
import sh.reece.events.*;
import sh.reece.moderation.*;
import sh.reece.runnables.*;
import sh.reece.utiltools.ConfigUpdater;
import sh.reece.utiltools.Metrics;
import sh.reece.utiltools.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Main extends JavaPlugin implements Listener {	

	public final String PREFIX = "&f&lSERVER &8»&r ";

	public static final HashMap<String, String> SERVER_VARIABLES = new HashMap<>();
	public static final List<String> SERVER_VARIABLE_KEYS = new ArrayList<>();

	public final List<String> modulesList = new ArrayList<>();
	public Integer enabledModulesNumber = 0;

	public static FileConfiguration MAINCONFIG; // used in files data is only needed to be read from

	private static boolean isPAPIEnabled, isServerAgeEnabled;
	private static final HashMap<String, String> LANG = new HashMap<>();
	
	public static Chat chat = null; // used for Tags
	
	public void onEnable() {
		loadConfig();			
		MAINCONFIG = getConfigFile("config.yml");

		isServerAgeEnabled = false;

		loadLocalServerVariableKeys();

		if (isPAPIEnabled) {	// done in load config
			final String Section = "Misc.ServerAges";
			if (enabledInConfig(Section+".Enabled")) {
				(new UptimePlaceholder()).register();
				isServerAgeEnabled = true;
			}						
		}

		new DisableJLMsg(this); 
		new ReeceTools(this);			


		// Bungee
		new BungeeServerConnector(this);

		new ScheduledTask(this);

		new DisableStackablePotions(this);

		new Speed(this);

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
		new Enderchest(this);
		new InvSee(this);

		// test / add to config
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
		if (Util.isPluginInstalledOnServer("vault", "Withdraw")) {
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


		Collections.sort(modulesList);
		fancyStartup();
	}





	public void onDisable() {
		saveDefaultConfig();
		modulesList.clear();

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

		try {
			ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"), new ArrayList<String>());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		

		// done above
		isPAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
	
	public void reloadLanguage() {
		LANG.clear();
		createConfig("messages.yml");				
		final FileConfiguration language = getConfigFile("messages.yml");
		for(final String key : language.getKeys(false)) {
			LANG.put(key, language.getString(key));
		}
	}

	public static Boolean isPAPIEnabled() {
		return isPAPIEnabled;
	}

	public void loadLocalServerVariableKeys() {
		for(String key : MAINCONFIG.getConfigurationSection("PluginVariables").getKeys(false)){
			Main.SERVER_VARIABLES.put(key, getConfig().getString("PluginVariables."+key));
			Main.SERVER_VARIABLE_KEYS.add(key);
		}
	}

	public void fancyStartup() {
		final String S = "&a   _____                            _______          _     \r\n" +
				"&a  / ____|                          |__   __|        | |    \r\n" + 
				"&a | (___   ___ _ ____   _____ _ __     | | ___   ___ | |___ \r\n" + 
				"&a  \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|    | |/ _ \\ / _ \\| / __|\r\n" + 
				"&a  ____) |  __/ |   \\ V /  __/ |       | | (_) | (_) | \\__ \\\r\n" + 
				"&a |_____/ \\___|_|    \\_/ \\___|_|       |_|\\___/ \\___/|_|___/\r\n" + 
				"&b by Reecepbups. Version: " + getDescription().getVersion();	
		Util.consoleMSG(S);
	}

	// Configuration File Functions
	public FileConfiguration getConfigFile(final String name) {
		return YamlConfiguration.loadConfiguration(new File(getDataFolder(), name));
	}

	public void createDirectory(final String DirName) {
		final File newDir = new File(getDataFolder(), DirName.replace("/", File.separator));
		if (!newDir.exists()){
			newDir.mkdirs();
		}
	}

	public void createConfig(final String name) {
		final File file = new File(getDataFolder(), name);

		if (!new File(getDataFolder(), name).exists()) {

			saveResource(name, false);
		}

		@SuppressWarnings("static-access") final FileConfiguration configuration = new YamlConfiguration().loadConfiguration(file);
		if (!file.exists()) {
			try {
				configuration.save(file);
			}			
			catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createFile(final String name) {
		final File file = new File(getDataFolder(), name);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch(final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveConfig(final FileConfiguration config, final String name) {
		try {
			config.save(new File(getDataFolder(), name));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean serverAgeEnabled(){
		return isServerAgeEnabled;
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

			if (MAINCONFIG.getString(path).equalsIgnoreCase("true")) {

				//if(!path.contains("Core.")) {} // does not show /fly, /workbench, etc					
				module = "&a"+pathInfo;
				enabledModulesNumber +=1;

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
	
	public static String lang(final String key) {
		return Util.color(LANG.get(key).replace("%prefix%", "&7[&eServerTools&7]&r"));
	}


}
