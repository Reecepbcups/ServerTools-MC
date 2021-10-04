package sh.reece.cmds;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class DailyRewards implements CommandExecutor {
	
	// UUID, UNIXTIMESTAMP
	public static Map<String,Long> PlayerCooldown = new HashMap<String, Long>();
	private List<String> rewards;
	private long COOLDOWN_SECONDS;
	private String msg = "";
	private static String FILENAME;
	public static FileConfiguration CooldownData;
	
	private static Boolean wasPluginEnabled;
	
	private static Main plugin;
	public DailyRewards(Main instance) {
	    plugin = instance;
	    wasPluginEnabled = false;
	    
	    final String section = "Commands.DailyRewards";
	    if (plugin.enabledInConfig(section+".Enabled")) {
	    	wasPluginEnabled = true;
	    	
			plugin.getCommand("reward").setExecutor(this);

			plugin.createDirectory("DATA");			
			FILENAME = File.separator + "DATA" + File.separator + "DailyRewardCooldown.yml";
			plugin.createFile(FILENAME);
			CooldownData = plugin.getConfigFile(FILENAME);
											
			loadCooldownsToMemory();
			
			rewards = plugin.getConfig().getStringList(section+".rewards");
			COOLDOWN_SECONDS= 86400;
		}
	}
	
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	long unixTime = System.currentTimeMillis() / 1000L;

    	if (cmd.getName().equalsIgnoreCase("reward")) {
    		
    		//Util.console("say " + unixTime);
    		
    		Player p = (Player) sender;

    		if(args.length >= 1) {
    			
    			switch (args[0].toLowerCase()) {
    			
    			case "help":    				
    				msg = "\n &8- &a/reward &f<help, reset, clearall, save>";    				
					p.sendMessage(Util.color(msg));
					p.sendMessage("");
					return true;
					
    			case "clearall":    				
    				msg = "\n &8- &aClearing all cooldowns";      				
					p.sendMessage(Util.color(msg));
					p.sendMessage("");
					PlayerCooldown.clear();
					return true;
					
    			case "save":    				
    				msg = "\n &8- &aSaved to file";      				
					p.sendMessage(Util.color(msg));
					p.sendMessage("");
					saveCooldownsToFile();
					return true;
    			
				case "reset":
					if(args.length >= 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if(target != null && target.hasPlayedBefore()) {
							PlayerCooldown.remove(ipAddressFormat(target));
							msg = "&aPlayer &2" + args[1] + "&a successfuly removed from the cooldown";
						} else {
							msg = "&cPlayer &4" + args[1] + "&c does not seem to be on / have played before";
						}										
					} else {
						msg = "&c/reward reset <IGN>";
					}					
					p.sendMessage(Util.color(msg));
					
					return true;

				default:
					p.sendMessage("/reward <help, reset, clearall, save>");
					break;
				}
    		}
    		
    		String IP_ADDR = ipAddressFormat(p);
    		
    		
    		if(canPlayerClaim(IP_ADDR, unixTime)) {
    			msg = "\n  &a[&2+&a] &aClaiming Daily Reward...\n";
    			PlayerCooldown.put(IP_ADDR, unixTime);
    			
    			for(String reward : rewards) {
    				Util.console(reward.replace("%player%", p.getName()));
    			}
    			
    		} else {
    			msg = "\n  &cALREADY CLAIMED! Next Claim: \n&7&o" + 
    					"  " + new Date((long) (PlayerCooldown.get(IP_ADDR) + COOLDOWN_SECONDS)*1000);
    		}
  
    		p.sendMessage(Util.color(msg));
    		p.sendMessage("");
    		
    	}
    	return true;
    }
    
   private String ipAddressFormat(Player p) {
	   return p.getAddress().getAddress().toString().replace("", "_").substring(1);
   }
    
    public boolean canPlayerClaim(String ip, Long EpochTime) {
    	// EpochTime is newer - the old time from player = time elapsed
    	if(!PlayerCooldown.containsKey(ip)) {
    		return true;
    	}
    	Long SecondsElapsedSinceCommandRunLast = EpochTime - PlayerCooldown.get(ip); //
    	//Util.console("say " + PlayerCooldownTime + " || " + SecondsElapsedSinceCommandRunLast);
    	if(SecondsElapsedSinceCommandRunLast >= COOLDOWN_SECONDS) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static void saveCooldownsToFile() {
    	
    	if(wasPluginEnabled) {
    		for (Map.Entry<String,Long> entry : PlayerCooldown.entrySet()) {
        	    CooldownData.set("cooldowns." + entry.getKey(), entry.getValue().toString());
        	}
        	plugin.saveConfig(CooldownData, FILENAME);
    	}
    	
    	
    }
    
    public static void loadCooldownsToMemory() {
    	
    	if(!CooldownData.contains("cooldowns")) {
    		return;
    	}
    	
    	for (String key : CooldownData.getConfigurationSection("cooldowns").getKeys(false)) {
    	   PlayerCooldown.put(key, Long.valueOf(CooldownData.get("cooldowns."+key).toString()));
    	}
    }
    
    
    
    
}
