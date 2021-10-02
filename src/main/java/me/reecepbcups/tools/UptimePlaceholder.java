package me.reecepbcups.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.reecepbcups.utiltools.Util;

public class UptimePlaceholder extends PlaceholderExpansion {

	//private final Main plugin;

	public UptimePlaceholder() {//Main instance) {
		//plugin = instance;
	}
	

	public String getIdentifier() {
		return "age";
	}

	public String getAuthor() {
		return "Reecepbcups";
	}

	public String getVersion() {
		return "1.0";
	}
	
	
	// Where the identifier would be epoch time ( %age_https://www.epochconverter.com/% )
	public String onPlaceholderRequest(Player player, String identifier) {
		
//		String str = "Mar 9 2021 9:36:00 CST";
//		SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm zzz");
//		Date date = null;
//		try {
//			date = df.parse(str);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		long epoch = date.getTime();
//		//System.out.println(epoch); // 1055545912454
        // %age_1622318400%
		
		if (identifier == null) {
			return null; 
		}			
		return Util.onPlaceholderRequest(identifier);
	}
}

