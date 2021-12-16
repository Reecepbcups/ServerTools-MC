package sh.reece.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class Trash implements CommandExecutor{//,TabCompleter,Listener {

	String Section;
	private Main plugin;
	public Trash(Main instance) {
		this.plugin = instance;
		
		
		Section = "Core.Trash";        

		// https://essinfo.xeya.me/permissions.html
		if(plugin.enabledInConfig(Section+".Enabled")) {
			plugin.getCommand("trash").setExecutor(this);
		} else {
			AlternateCommandHandler.addDisableCommand("trash");
		}
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		

		Player p = (Player)sender;				
		p.openInventory(Bukkit.getServer().createInventory((InventoryHolder) p, 54, Util.color("&lTrash Bin")));
		
		return true;
	}
}
