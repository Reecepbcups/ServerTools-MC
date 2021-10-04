package sh.reece.moderation;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;

public class Report implements CommandExecutor {

	private static Main plugin;
	private final String Section;
	private final HashMap<String, Date> CooldownHash = new HashMap<String, Date>();
	private Integer CooldownSeconds;
	private String perm, CooldownMSG, ReportSuccess;	
			
	
	public Report(Main instance) {
        plugin = instance;
        
        Section = "Moderation.Report";                
        if(plugin.enabledInConfig(Section+".Enabled")) {

        	perm = "report.notify";
        	
        	CooldownSeconds = plugin.getConfig().getInt(Section+".Cooldown");
        	if(CooldownSeconds == null) {
        		CooldownSeconds = 15;
        	}
        	CooldownMSG = plugin.getConfig().getString(Section+".CooldownMSG");
        	ReportSuccess = plugin.getConfig().getString(Section+".ReportSuccess");
        	
        	plugin.getCommand("report").setExecutor(this);		
    	}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;

		if (args.length <= 1) {
			sendHelpMenu(p);
			return true;
		}		
		
		if(!Util.cooldown(CooldownHash, CooldownSeconds, p.getName(), CooldownMSG)) {
			// User has cooldown	  
    		
    	} else {
    		// report player to online staff    		
    		Player target = Bukkit.getPlayer(args[0]);
    		if(target != null) {
    			
    			if(args[0].equalsIgnoreCase(p.getName())) {
    				Util.coloredMessage(p, Main.lang("REPORT_SELF"));
    				return true;
    			}
    			
    			ReportSuccess = ReportSuccess
    					.replace("%reporter%", p.getName())
    					.replace("%offender%", args[0])
    					.replace("%reason%", Util.argsToSingleString(1, args));
    			
    			Bukkit.broadcast(Util.color(ReportSuccess), perm);
    			Util.coloredMessage(p, Main.lang("REPORT_SUCCESS").replace("%target%", args[0]));
    		} else {
    			Util.coloredMessage(p, Main.lang("REPORT_OFFLINE").replace("%target%", args[0]));
    		}
    		
    	}
		return true;
		
		
	}
	
	public void sendHelpMenu(Player p) {
		Util.coloredMessage(p, "&f/report &7<player> <reason>");
	}
	
	
	
}
