package sh.reece.tools;

import sh.reece.utiltools.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// TODO
public class Repair implements CommandExecutor {// ,TabCompleter,Listener {

	private String Section, SingleRepairPerm, AllRepairPerm;
	private double SingleRepairCost, AllRepairCost;
	private final Main plugin;
	private ConfigUtils configUtils;

	public Repair(Main instance) {
		plugin = instance;

		Section = "Core.Repair";

		// https://essinfo.xeya.me/permissions.html
		if (plugin.enabledInConfig(Section + ".Enabled")) {
			configUtils = plugin.getConfigUtils();

			plugin.getCommand("repair").setExecutor(this);

			SingleRepairPerm = plugin.getConfig().getString(Section + ".Single.Permission");
			AllRepairPerm = plugin.getConfig().getString(Section + ".All.Permission");

			SingleRepairCost = plugin.getConfig().getDouble(Section + ".Single.Cost");
			AllRepairCost = plugin.getConfig().getDouble(Section + ".All.Cost");
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;

		if (sender instanceof Player) {
			p = (Player) sender;
		} else {
			Util.coloredMessage(sender, "&c[!] Only players can fix items!");
			return true;
		}

		if (args.length == 0) {
			Util.coloredMessage(sender, "&fUsage: &c/" + label + " [all]");
			return true;
		}

		return true;
	}

	// public boolean checkPerm(Player p, String Perm, String PermFormat) {
	// // if fromConspole, then it lets console change the GM no matter their perm
	// // level
	// if (fromConsole || p.hasPermission(Perm)) {
	// Util.coloredMessage(p,
	// configUtils.lang("GAMEMODE_CHANGED").replace("%gamemode%", PermFormat));
	// Util.consoleMSG("&e&l[ServerTools]&f Changed " + p.getName() + "'s gamemode
	// to " + PermFormat);
	// return true;
	// }

	// Util.coloredMessage(p, "&cYou do not have access to " + PermFormat + "
	// &cmode!");
	// return false;
	// }

	private void repairHand(Player player) {
		final ItemStack item = player.getInventory().getItemInHand();

	}

	private void repairAll(Player player) {
		final ItemStack item = player.getInventory().getItemInHand();
		repairItems(player.getInventory().getContents(), player);

		if (player.hasPermission("essentials.repair.armor")) {
			repairItems(player.getInventory().getArmorContents(), player);
		}

		player.updateInventory();

	}

	private void repairItem(ItemStack item) {
		Material mat = item.getType();
		if (mat.isBlock() || mat.getMaxDurability() < 1) {
			// cant repair this item
		} else {
			if (item.getDurability() != 0) {
				// repair
				item.setDurability((short) 0);
			}
		}
	}

	private void repairItems(ItemStack[] items, Player player) {
		for (ItemStack item : items) {
			if (item == null || item.getType().isBlock() || item.getDurability() == 0) {
				continue;
			}

			// check if user has funds to fix

			// check if user can fix enchanted items here
			if (!item.getEnchantments().isEmpty() && !player.hasPermission("essentials.repair.enchanted")) {
				continue;
			}

			// charge user & send message

		}

	}

}
