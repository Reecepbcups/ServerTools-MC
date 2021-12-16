package sh.reece.core;

import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class Enchant implements CommandExecutor {// ,TabCompleter,Listener {

	private String Section, Permission;
	private final Main plugin;
	// private ConfigUtils configUtils;

	public Enchant(Main instance) {
		plugin = instance;

		Section = "Core.Enchant";

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section + ".Enabled")) {
			// configUtils = plugin.getConfigUtils();

			plugin.getCommand("enchant").setExecutor(this);
			Permission = plugin.getConfig().getString(Section + "Permission");
		} else {
			AlternateCommandHandler.addDisableCommand("enchant");
		}

	}

	// change to a listener event so we take over default minecraft

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			Util.coloredMessage(sender, "&c[!] Only players can enchant items!");
			return true;
		} 

		Player p = (Player) sender;

		if (!p.hasPermission(Permission)) {
			Util.coloredMessage(p, "&c[!] You don't have permission to enchant items!");
			return true;
		}


		if (args.length == 0) { // -> /enchant 			
			Util.coloredMessage(sender, "&c[!] Usage: &7/enchant <enchantment> [level]");
			return true;
		} 

		if(args.length >= 1){ // -> /enchant <enchantment> [level]		
			
			ItemStack is = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
			Util.consoleMSG(is.getType() + " " + is.getItemMeta().getDisplayName());

			String enchantName = args[0].toUpperCase();
			if(checkValid(enchantName, p, is)){
				int level = 1;
				if(args.length == 2) { level = Integer.parseInt(args[1]); }

				// enchant the item with unsafe enchantment level 
				is.addUnsafeEnchantment(Enchantment.getByName(enchantName), level);
			}

			return true;
		}

		return true;
	}

	private boolean checkValid(String enchantName, Player p, ItemStack is){
		Util.consoleMSG("Valid Enchant Check");

		// check if Enchantment.values() contains args[0]
		if(!(Enchantment.getByName(enchantName) != null)) {
			Util.coloredMessage(p, "&c[!] &7" + enchantName + " &cis not a valid enchantment!");
			return false;
		}

		// check if player has item in hand. Allows enchanting nonTool items
		if(is == null || is.getType() == Material.AIR) {
			Util.coloredMessage(p, "&c[!] &7You need to hold an item in your hand!");
			return false;
		}

		return true;
	}

}
