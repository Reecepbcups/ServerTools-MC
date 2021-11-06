package sh.reece.core;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hat implements CommandExecutor{

	String Section, Permission;
	private final Main plugin;
	//private ConfigUtils configUtils;

	public Hat(Main instance) {
		plugin = instance;
		Section = "Core.Hat";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			//configUtils = plugin.getConfigUtils();
			plugin.getCommand("hat").setExecutor(this);
			Permission = plugin.getConfig().getString(Section+".Permission"); 
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(Util.color("&cYou do not have access to &n/" +cmd.getName()+"&c."));
			return true;
		} 

		Player player = (Player) sender;

		// get current heald item slot
		int slot = player.getInventory().getHeldItemSlot();
		// get itemstack in that slot
		org.bukkit.inventory.ItemStack item = player.getInventory().getItem(slot);

		// check if player is wearing a helmet
		ItemStack helmet = player.getInventory().getHelmet();

		if(item == null || item.getType() == Material.AIR) {
			Util.coloredMessage(sender, "&c[!] You can not set your hat as nothing!");
			return true;

		} else if(helmet != null) {
			// if player is wearing a helmet, switch the helmet to the item in the players hand
			player.getInventory().setHelmet(item);
			player.getInventory().setItem(slot, helmet);

		} else {
			player.getInventory().setHelmet(item);
			player.getInventory().setItem(slot, null);			
		}

		Util.coloredMessage(sender, "&a[+] Hat has been set!");
		
		

		// player.getInventory().setHelmet(item);
		// 

		return true;
	}
}
