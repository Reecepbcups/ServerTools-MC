package sh.reece.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.clip.placeholderapi.PlaceholderAPI;
import sh.reece.utiltools.Util;
import sh.reece.tools.AlternateCommandHandler;
import sh.reece.tools.ConfigUtils;
import sh.reece.tools.Main;

public class Holograms implements CommandExecutor, Listener, TabCompleter {

	// on click of amour stand, if armour stand has text & armour stands are around up (y+ & -):
	// /minecraft:kill @e[type=armor_stand,r=3] remove those armour stands within that location
	
	private static Main plugin;
	private FileConfiguration HoloConfig;
	private String Section;
	private Boolean papiSupport;
	
	private Set<String> holoKeys;
	public HashMap<Location, Integer> EntitiyIDs = new HashMap<Location, Integer>();
	public DecimalFormat df = new DecimalFormat("#.###");	
	private String permission;
	private Player randomPlayer;
	private ConfigUtils configUtils;
	
	public Holograms(Main instance) {
        plugin = instance;
        
        Section = "Misc.Holograms";
        randomPlayer = null;
        
        if(plugin.enabledInConfig(Section+".Enabled")) {

			configUtils = plugin.getConfigUtils();

        	papiSupport = plugin.isPAPIEnabled();       	
        	
        	configUtils.createConfig("Holograms.yml");
        	loadInit();
        	permission = "hologram.admin";
        	plugin.getCommand("holograms").setExecutor(this);	
        	plugin.getCommand("holograms").setTabCompleter(this);
    	} else {
			AlternateCommandHandler.addDisableCommand("holograms");
			AlternateCommandHandler.addDisableCommand("hologram");
			AlternateCommandHandler.addDisableCommand("holo");
		}
        
        // on ChunkUnloadEvent, ChunkLoadEvent
        // kill or spawn armour stands in that area
        
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) {
		if(randomPlayer == null) {
			randomPlayer = e.getPlayer();
			
			new BukkitRunnable() {
				@Override
				public void run() {
					loadInit();
				}
			}.runTaskLater(plugin, 10L);
		}
	}

	
	public void loadInit() {
		HoloConfig = configUtils.getConfigFile("Holograms.yml");
		// load in all keys here for stands on startup
    	// on disable delete all armour stands at locations given in config        	
    	holoKeys = HoloConfig.getKeys(false); // [skyblock]
    	
    	if(papiSupport) { // && randomPlayer == null < not needed i dont think
    		randomPlayer = Bukkit.getOnlinePlayers().stream().skip(0).findFirst().orElse(null);
    		//Util.consoleMSG(randomPlayer+"");
    	} 
    	
		// spawn holograms only if holokeys has more than 1
		if(holoKeys.size() >= 1){
			removeAllStands();        	
    		spawnAllHolos();
		}
    	
	}
	
	private List<String> possibleArugments = new ArrayList<String>();
	private List<String> result = new ArrayList<String>();
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {		
		if(possibleArugments.isEmpty()) {
			possibleArugments.add("create");
			possibleArugments.add("remove");			
			possibleArugments.add("show"); 
			possibleArugments.add("hide");
			possibleArugments.add("list"); 
			possibleArugments.add("teleport"); 
			possibleArugments.add("removenear");
		}		
		result.clear();
		if(args.length == 1) {
			
			for(String a : possibleArugments) {
				if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);			
				}
			}
			return result;
		}	
		if(args[0].equalsIgnoreCase("remove") && args.length == 2) {
			for(String a : holoKeys) {
				if(a.toLowerCase().startsWith(args[1].toLowerCase())) {
					result.add(a);			
				}
			}
			return result;
		}
		if((args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) && args.length == 2) {
			for(String a : holoKeys) {
				if(a.toLowerCase().startsWith(args[1].toLowerCase())) {
					result.add(a);			
				}
			}
			return result;
		}
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {	
		
		if(sender instanceof ConsoleCommandSender) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					loadInit();
					Util.consoleMSG("Reloaded Holograms!");
				}
			} else {
				Util.consoleMSG("/holo reload");
			}
			return true;
		}
		
		if (!(sender.hasPermission(permission))) {		
			sender.sendMessage(Util.color("&cNo Permission to use "+label+" :("));
			return true;			
		}
		
		Player p = (Player) sender;

		if (args.length == 0) {
			sendHelpMenu(p);
			return true;
		}	
		
		switch(args[0]){
			// /holo create KEY
			case "create":		
				if(args.length < 2) {
					sendHelpMenu(p);
				} else {
					
					if(holoKeys.contains(args[1])) {
						Util.coloredMessage(p, "&4[!] &cThe key " + args[1] + " already exist!");
					} else {
						createNewHolo(p.getLocation(), args[1]);
					}
				}
				return true;	
				
			case "tp":
			case "teleport":
				if(args.length < 2) {
					sendHelpMenu(p);
				} else {
					
					if(holoKeys.contains(args[1])) {
						Util.coloredMessage(p, "&4[!] &cTeleported to " + args[1] + " location");
						
						p.teleport(getLocFromConfig(args[1]));
						
					} else {
						Util.coloredMessage(p, "&c[!] Holo" + args[1] + " does not exsist!");
					}
				}
				return true;
				
				
			case "delete":
			case "remove":
				if(args.length < 2) {
					sendHelpMenu(p);
				} else {					
					if(!holoKeys.contains(args[1])) {
						Util.coloredMessage(p, "&4[!] &cThe key " + args[1] + " does not exsist!");
					} else {
						removeSingleKey(args[1]);
						Util.coloredMessage(p, "&2[!] &aSuccessfully removed " + args[1]);
					}
				}				
				return true;
			case "hide":
				if(args.length < 2) {
					sendHelpMenu(p);
				} else {					
					if(!holoKeys.contains(args[1])) {
						Util.coloredMessage(p, "&4[!] &cThe key " + args[1] + " does not exsist!");
					} else {
						hideHolo(args[1]);
						Util.coloredMessage(p, "&2[!] &aSuccessfully hid " + args[1]);
					}
				}				
				return true;
			case "show":
				if(args.length < 2) {
					sendHelpMenu(p);
				} else {					
					if(!holoKeys.contains(args[1])) {
						Util.coloredMessage(p, "&4[!] &cThe key " + args[1] + " does not exsist!");
					} else {
						spawnHolo(args[1]);
						Util.coloredMessage(p, "&2[!] &aSuccessfully shown " + args[1]);
					}
				}				
				return true;
			case "list":	
				Util.coloredMessage(p, "\n&e&lSERVERTOOLS HOLOGRAMS\n ");
				for(String key : holoKeys) {
					Util.coloredMessage(p, "&b&l"+key+"&7: &f" + HoloConfig.getString(key+".location"));
				}
				return true;
				
			case "removenear":	
				Util.coloredMessage(p, "\n&aRemoved armour stands within a radius of 2\n ");
				p.performCommand("minecraft:kill @e[type=armor_stand,r=2]");
				return true;
				
			case "reload":	
				Util.coloredMessage(p, "&aServertools holograms reloaded!");
				loadInit();
				return true;
			default:
				sendHelpMenu(p);
				return true;		
		}		
	}
	
	public void sendHelpMenu(Player p) {
		String[] MSG = new String[] {
			"&7create <name>", "&7remove <name>", "&7show/hide <name>", "&7teleport <name>","&7list",
			"&7removenear", "&7reload",
		};

		Util.coloredMessage(p, "&e&lServerTools Holograms");
		for (int i = 1; i < MSG.length; i++) {			
			Util.coloredMessage(p, "&f/hologram " + MSG[i]);
		}
		
	}
	
	public void spawnHolo(String key) {		
		Location loc = getLocFromConfig(key).clone().subtract(0, 2, 0);
		World world = loc.getWorld();
		//Util.consoleMSG("Loading in hologram: " + key);
		ArmorStand as;
		
		for(String line : getLinesFromConfig(key)) {
			loc = loc.clone().subtract(0, 0.25, 0);	
			
			String msg = Util.color(Main.replaceVariable(line));
			if(papiSupport && randomPlayer != null) { 
				// sets a random online to be the msg.
				// so this adds support for none player specific Placeholders like %server_uptime%
				msg = PlaceholderAPI.setPlaceholders(randomPlayer, msg);
			}						
			
			
			if(msg.length() > 0) {
				//ArmorStand as = world.spawn(loc, ArmorStand.class); // <- slower by 36%
				as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
				EntitiyIDs.put(loc, as.getEntityId());
				as.setCustomName(msg);
				as.setGravity(false);
				as.setCanPickupItems(false);
				as.setCustomNameVisible(true);
				as.setVisible(false);		
				as.setMarker(true);	// very small collision box				
			}				
		}
		
	}

	
	
	public void createNewHolo(Location l, String newKey) {
		HoloConfig.set(newKey+".location", locationToStringFormat(l));
		HoloConfig.set(newKey+".lines", Arrays.asList("&fEdit this line in", "&bthe Holograms.yml"));
		configUtils.saveConfig(HoloConfig, "Holograms.yml");
		spawnHolo(newKey);
		holoKeys.add(newKey);
	}
	
	public String locationToStringFormat(Location l) {					
		return l.getWorld().getName()+", "+df.format(l.getX())+", "+df.format(l.getY())+", "+df.format(l.getZ());
	}
	
	public Location getLocFromConfig(String key) {
		String[] loc = HoloConfig.getString(key+".location").split(", ");
		World w = Bukkit.getWorld(loc[0]);
		if(w != null) {
			return new Location(w, 
					Double.valueOf(loc[1]), 
					Double.valueOf(loc[2]), 
					Double.valueOf(loc[3]));
		}			
		return null;
	}
	public List<String> getLinesFromConfig(String key) {	
		return HoloConfig.getStringList(key+".lines");
	}
	
	
	public void removeAllStands(){
		//Entity[] grabEntities = getLocFromConfig(key).getChunk().getEntities();
		
		
		
		for(Location locs : EntitiyIDs.keySet()) {
			for(Entity e : locs.getWorld().getNearbyEntities(locs, 3, 5, 3)) {
				if(e instanceof ArmorStand) {
					if (e.isCustomNameVisible()) {							
						//Util.consoleMSG("Removed " + e.getCustomName() + " ID:" + e.getEntityId());
						e.remove();							
					}
				}
			}
		}
	}
	
	public void removeSingleKey(String key) {
		hideHolo(key);
		HoloConfig.set(key, null);
		holoKeys.remove(key);
		configUtils.saveConfig(HoloConfig, "Holograms.yml");		
	}
	
	public void hideHolo(String key) {
		// removes armour stand from view in the server
		Location l = getLocFromConfig(key);		
		for(Entity e : l.getWorld().getNearbyEntities(l, 0, 7, 0)) {
			if(e instanceof ArmorStand) {
				if (e.isCustomNameVisible()) {							
					e.remove();							
				}
			}
		}
	}
	
	
	public void spawnAllHolos() {

		for(String key : holoKeys) {
			try {
				spawnHolo(key);
			} catch (Exception e) {
				 Util.consoleMSG("&c[!] Could not load Hologram " + key);
			}
    		
    	}
	}
	
	public void spawnHoloItem(Location l, ItemStack item) {
		// spawn new armour stand at location, but bring back some
		// and try to center more
		
		l = l.clone().subtract(0, 2, 0);
//		ArmorStand as = l.getWorld().spawn(l, ArmorStand.class);
//		EntitiyIDs.put(l, as.getEntityId());
//		as.setGravity(false);
//		as.setCanPickupItems(false);
//		as.setCustomNameVisible(false);
//		as.setVisible(false);
//		as.setItemInHand(item);
//		as.setRightArmPose(new EulerAngle(-3.25, -0, -2.5));
//		EntitiyIDs.put(l, as.getEntityId());
		ArmorStand as = l.getWorld().spawn(l, ArmorStand.class);
		EntitiyIDs.put(l, as.getEntityId());
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setVisible(false);
		Entity e = l.getWorld().dropItem(l, item);
		as.setPassenger(e);
		
	}
}
