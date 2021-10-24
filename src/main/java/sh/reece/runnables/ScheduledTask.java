package sh.reece.runnables;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;


public class ScheduledTask implements CommandExecutor{

	private static Main plugin;
	private FileConfiguration config;
	private String Section, permision;
	//private Set<String> keys;
	private Boolean debug = false;
	
	private static List<Integer> runnableIDs = new ArrayList<Integer>();
	private ConfigUtils ConfigUtils;

	public ScheduledTask(Main instance) {
        plugin = instance;
        
        Section = "Misc.ScheduledTask";                
        if(plugin.enabledInConfig(Section+".Enabled")) {

			ConfigUtils = plugin.getConfigUtils();

        	plugin.getCommand("scheduledtask").setExecutor(this);
        	loadAllTimingRunnables();
    		//Bukkit.getServer().getPluginManager().registerEvents(this, plugin);    		
    	}
	}
	
	public void loadAllTimingRunnables() {
		if(ConfigUtils.getConfigFile("config.yml").getBoolean(Section+".Debug")) {
    		debug = true;
    	}
    	
		permision = plugin.getConfig().getString(Section+".Permission");
		
    	ConfigUtils.createConfig("ScheduledTask.yml");
    	config = ConfigUtils.getConfigFile("ScheduledTask.yml");
    	Set<String> taskKeys = config.getKeys(false);
    	
    	if(taskKeys.size() > 0) {
    		//keys = config.getConfigurationSection("TaskAtTime.sections").getKeys(false);
    		for(String task : taskKeys) {
    			
    			String exactTime = config.getString(task+".Time");
    			List<String> command = config.getStringList(task+".Command");
    			
    			if(exactTime == null) {
    				long RepeatSeconds = config.getLong(task+".Repeat");
    				
    				long initDelay = 0;
    				if(config.contains(task+".Delay")) {
    					initDelay = config.getLong(task+".Delay");
    				}
    				
    				taskToRunEvery(RepeatSeconds, initDelay, command);
    				continue;
    			}
    			
    			createTaskToRun(exactTime, command);
    		}
    	} else {
    		Util.consoleMSG("&cYou do not seem to have any values in your ScheduledTask config!");
    	}    	
	}
	
	
	
	public void createTaskToRun(String TIME, List<String> CMDS) {
		LocalTime time1 = LocalTime.now();
		LocalTime time = LocalTime.parse(TIME);
		Long timeUntilRun;
		timeUntilRun = time1.until(time, ChronoUnit.SECONDS);
		
		// if it has passed, set it for the next day :D
		if(timeUntilRun<0) {
			timeUntilRun = 86400 - time.until(time1, ChronoUnit.SECONDS);
		}
		
		if(debug) {
			System.out.println("["+timeUntilRun+" sec] Scheduled: " + CMDS.toString());
		}
				
		int id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    public void run() {  CMDS.forEach(cmd -> Util.console(cmd)); }
		}, 2+timeUntilRun*20L);
		runnableIDs.add(id);
	}
	
	
	public void taskToRunEvery(Long SecondsToRunEvery, Long Delay,  List<String> CMDS) {	
		if(debug) {
			System.out.println("["+SecondsToRunEvery+" sec Repeating] Scheduled: " + CMDS.toString());
		}

		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
		    public void run() { CMDS.forEach(cmd -> Util.console(cmd)); }
		}, Delay*20L, SecondsToRunEvery*20L);
		runnableIDs.add(id);
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission(permision)) {
			sender.sendMessage("No permission: "+permision);
			return true;
		} 
		if (args.length == 0) {
			sender.sendMessage(Util.color("/scheduledtask reload"));
			return true;
		} 
		
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {

			if(runnableIDs.size() > 0)
				runnableIDs.forEach(id -> Bukkit.getScheduler().cancelTask(id));
			
			runnableIDs.clear();
			loadAllTimingRunnables();
			sender.sendMessage(Util.color("&aScheduledTask Reloaded!"));
		}
		
		return true;
	}
	
	
	
	
}
