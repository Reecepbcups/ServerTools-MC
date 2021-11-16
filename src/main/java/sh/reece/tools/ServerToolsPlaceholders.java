package sh.reece.tools;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import sh.reece.GUI.ChatColor;
import sh.reece.cmds.Visibility;
import sh.reece.moderation.CommandSpy;
import sh.reece.moderation.StaffAFK;
import sh.reece.utiltools.Util;

public class ServerToolsPlaceholders extends PlaceholderExpansion {

	public ServerToolsPlaceholders() { }
	

	public String getIdentifier() {
		return "stools"; // %stools_
	}

	public String getAuthor() {
		return "Reecepbcups";
	}

	public String getVersion() {
		return "1.0";
	}
	
	
	public String onPlaceholderRequest(Player player, String identifier) {
		
		if (identifier == null) {
			return null; 
		}		
		
		String[] args = identifier.split("_");

		switch (args[0]) {

			case "isvisible": // %stools_isvisible%				
				return Visibility.isPlayerHidden(player) ? "true" : "false";
				
				
			case "age": // %stools_age_1622318400%
				// 2nd identifier = epoch time ( %stools_age_https://www.epochconverter.com/% )			
				if(args.length == 2){
					return Util.placeholderTimeRequest(args[1]);
				} 
				return "%stools_age_<EPOCHTIME>%";
			
			case "commandspy":
				return CommandSpy.isWatching(player.getUniqueId()) ? "on" : "off";

			case "chatcolor": // returns & code "&e" for example
				return ChatColor.getColor(player.getUniqueId().toString());

			case "staffafk":
				return StaffAFK.isStaffAfk(player.getUniqueId()) ? "true" : "false";

			default:
				return "STOOLS-PAPI-ERROR";
				
		}
	}
}

