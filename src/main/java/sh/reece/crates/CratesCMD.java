package sh.reece.crates;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sh.reece.tools.Main;
import sh.reece.utiltools.Util;

public class CratesCMD implements CommandExecutor  {

	private Main plugin;
	//private FileConfiguration config;
	public CratesCMD(Main instance) {
		this.plugin = instance;
		//config = plugin.getConfig();
		
		plugin.getCommand("crate").setExecutor(this);
	    //plugin.getCommand("tools").setTabCompleter(this);
		
	}
	
	
	public void helpMenu(CommandSender sender) {

		if(sender.hasPermission(Crate.getAdminPerm())) {			
			sendSenderMSG(sender, "&f- &e/crate set <crate>");
			sendSenderMSG(sender, "&f- &e/crate give <player> <crate> [amount]");
			sendSenderMSG(sender, "&7- &e/crate open <crate>");	
		}		
	}
	FileConfiguration f;
	
	public void sendSenderMSG(CommandSender sender, String msg) {
		sender.sendMessage(Util.color(msg));	
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
				
		//Player p = (Player) sender;	
				
		if(!sender.hasPermission(Crate.getAdminPerm())) {
			sender.sendMessage(Util.color("&4[!] &cNo Permission to run the crates command!"));			
			return true;
		}
		
		if(args.length == 0) {
			this.helpMenu(sender);
			return true;
		}
		
		if(args.length >= 1) {
			
			switch (args[0].toLowerCase()) {
			case "give":				
				if(!(args.length >= 3)) {
					helpMenu(sender);
					break;
				}

				Player target = Bukkit.getPlayer(args[1]);
				String crateKey = args[2];
				int amount = 1;
				if(args.length == 4) {
					amount = Integer.valueOf(args[3]);
				}	
				
				ItemStack key = Crate.getKey(crateKey);
				if(key == null) {
					sendSenderMSG(sender, "&c[!] The key " + crateKey + " does not exsist!");
					return true;
				}
				ItemStack keyStack = key.clone();
				keyStack.setAmount(amount);
				
				target.getInventory().addItem(keyStack);				
				sendSenderMSG(sender, "&a[!] Added "+amount+" "+crateKey+" to "+target.getPlayer().getName());
				Util.coloredMessage(target, "&a[!] You received "+amount+"x "+crateKey+" crate key");
				break;	
				
			case "set":				
				if(!(sender instanceof Player)) {
					sendSenderMSG(sender, "You cant do this.");
					return true;
				}
				
				if(!(args.length >=2)) {
					sender.sendMessage("/crate set <crate>");
					return true;
				}
				
				String crateName = args[1];

				if(!Crate.getKeys().contains(crateName)) {
					
					sendSenderMSG(sender, "&cThe crate " + crateName + " is not defined in a config file!");
					return true;
				}

				Block targetBlock = ((Player) sender).getTargetBlock((Set<Material>) null, 100);
				Location bl = targetBlock.getLocation();	
				
				if(Crate.getCrateLocations().contains(bl)) {
					sendSenderMSG(sender, "&c[!] There is already a crate at this location!");
					return true;
				} 

				Crate.addCrateLocation(crateName, bl);		
				Util.consoleMSG("set block to "+crateName + bl);
				Util.coloredMessage(((Player) sender), "&aSet the crate: "+crateName+" to the "+targetBlock.getType());

				f = plugin.getConfigFile("crates"+File.separator+Crate.getCrateAtLocation(bl)+".yml");	
				Util.consoleMSG(Crate.getCrateAtLocation(bl));
				
				List<String> locations = f.getStringList("CrateLocations");
				locations.add(Crate.locationToStringFormat(bl));
				f.set("CrateLocations", locations);
				plugin.saveConfig(f, "crates"+File.separator+Crate.getCrateAtLocation(bl)+".yml");				
				break;
				
			case "open":	
				String cName = args[1];

				if(!Crate.getKeys().contains(cName)) {
					Util.coloredMessage(((Player) sender), "&cCAN NOT OPEN: " + cName + " is not defined in a config file!");
					return true;
				}
			
				//GuiListener.openCrateInv(((Player) sender), Util.color(plugin.getConfigFile("crates"+File.separator+cName+".yml").getString("Name")));
				
			default:
				break;
			}
			
		}
		return true;
	}
	
	
	
	
}
