package sh.reece.cmds;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.List;
import java.util.Set;

public class Reclaim implements Listener, CommandExecutor {

	private static Main plugin;
	private FileConfiguration config, reclaimcnfg;
	private final String Section;
	private String FILENAME;
	private Set<String> RECLAIM_PERMISSIONS;
	private List<String> usedMemberReclaims;
	private int srtIDXforUsrOut; // used in showcasing msg in chat

	public Reclaim(Main instance) {
		plugin = instance;

		Section = "Commands.reclaim";                
		if(plugin.enabledInConfig(Section+".Enabled")) {

			config = plugin.getConfig();

			//        	// plugins/ServerTools/DATA
			plugin.createDirectory("DATA");
			FILENAME = File.separator + "DATA" + File.separator + "Reclaim.yml";
			plugin.createFile(FILENAME);
			reclaimcnfg = plugin.getConfigFile(FILENAME);	

			RECLAIM_PERMISSIONS = config.getConfigurationSection(Section+".permissions").getKeys(false); 
			srtIDXforUsrOut = config.getInt(Section+".BeginAtIndex");
			plugin.getCommand("reclaim").setExecutor(this);    		    	
		}
	}

	//	// add on join
	//	public void onPlayerJoin(PlayerJoinEvent e) {
	//		// bukkit runnable 10 second delay
	//		Player p = e.getPlayer();
	//		User user = giveMeADamnUser(p.getUniqueId());
	//		if(groups.contains(user.getPrimaryGroup())){
	//			Util.coloredMessage(p, "&f&l[!] &fYou have a package to redeem! &7&o(/reclaim)");
	//		}
	//	}

	private String getPlayersGroupIfAny(Player p) {
		for(String perm : RECLAIM_PERMISSIONS) {
			if(p.hasPermission(perm.replace("_", ""))) { // group.king
				Util.consoleMSG(p.getPlayer().getName()+" has perm "+perm);
				return perm;
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if(p.isOp()) { Util.coloredMessage(p, "&4&l[!] &cRemeber you are OPPED"); }

		// deny use if they have already claimed in the past FOR their rank
		if(reclaimcnfg.getStringList("USED").contains(p.getUniqueId().toString())) {	
			
			// change this to:
			// reclaimcnfg.getConfigurationSection("USED").getKeys(false); // RANKS 
			// if String key in ^ contains  p.getUniqueId().toString(), return true;
			// this allows for someone to be lower iron rank, but then claim to be 
			// a higher rank such as dragon. Probably a bad idea to do this
			// provided then they would get so many items.
			
			
			Util.coloredMessage(p, Main.lang("RECLAIM_ALREADY_CLAIMED"));
			return true;
		} 

		String permission = getPlayersGroupIfAny(p);
		if(permission == null) {
			Util.coloredMessage(p, Main.lang("RECLAIM_NOTHING"));
			return true;
		}
		
		runCMDS(p, permission);
		return true;
		
	}

	private void runCMDS(Player p, String permission) {
		for(String command : config.getStringList(Section+".permissions."+permission)) {
			//Util.consoleMSG("CMD: " + command);
			Util.console(command.replace("%player%", p.getName()));
		}
		Util.coloredMessage(p, Main.lang("RECLAIM_RECLAIMED")
				.replace("%permission%", permission.substring(srtIDXforUsrOut).toUpperCase()));

		usedMemberReclaims = reclaimcnfg.getStringList("USED");
		usedMemberReclaims.add(p.getUniqueId().toString());
		reclaimcnfg.set("USED", usedMemberReclaims);
		plugin.saveConfig(reclaimcnfg, FILENAME);
	}

}
