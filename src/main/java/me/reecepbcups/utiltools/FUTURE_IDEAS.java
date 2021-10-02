package me.reecepbcups.utiltools;

public class FUTURE_IDEAS {

	
	

	
	
// BLACKLIST LISTENER WITH FAKE KICK INFO
//	public class BlackListListener implements Listener {
//		  private final Set<String> uuids = Sets.newHashSet();
//		  
//		  private final Set<String> ips = Sets.newHashSet();
//		  
//		  private final Set<String> names = Sets.newHashSet();
//		  
//		  public BlackListListener() {
//		    this.uuids.add("a4e224b6-b5a3-4969-8893-7d0d863738b8");
//		    this.names.add("UltimqteGames");
//		    Bukkit.getOnlinePlayers().stream().filter(player -> isBlacklisted(player)).forEach(player -> player.kickPlayer("java.net.ConnectException: Connection timed out: no further information"));
//		  }
//		  
//		  private boolean isBlacklisted(Player player) {
//		    return isBlacklisted(player, player.getAddress().getAddress().getHostAddress());
//		  }
//		  
//		  private boolean isBlacklisted(Player player, String ip) {
//		    String name = player.getName();
//		    String uuid = player.getUniqueId().toString();
//		    return !(!this.uuids.contains(uuid) && !this.ips.contains(ip) && !this.names.contains(name));
//		  }
//		  
//		  @EventHandler
//		  private void onLogin(PlayerLoginEvent e) {
//		    Player player = e.getPlayer();
//		    if (isBlacklisted(player, e.getRealAddress().getHostAddress()))
//		      e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Utils.colorize("java.net.ConnectException: Connection timed out: no further information")); 
//		  }
//		}
	
	
// auto respawn
//	  @EventHandler
//	  private void onPlayerDeath(PlayerDeathEvent event) {
//	    Bukkit.getScheduler().runTaskLater(plugin, () -> event.getEntity().spigot().respawn(), 5L);
//	  }
    
	
// stop snow melt
//	@EventHandler
//	  private void onMelt(BlockFadeEvent e) {
//	    if (e.getBlock().getType() == Material.SNOW_BLOCK || e.getBlock().getType() == Material.SNOW)
//	      e.setCancelled(true); 
//	  }
	
	// insta mobkill
	
	// playtime
	
	
	// snowball hit - example:
//	@EventHandler
//	  public void onSnowballHit(ProjectileHitEvent event) {
//	    if (event.getEntity() instanceof org.bukkit.entity.Snowball) {
//	      Projectile projectile = event.getEntity();
//	      if (projectile instanceof org.bukkit.entity.Player)
//	        return; 
//	      Location entityLoc = projectile.getLocation();
//	      Vector vec = projectile.getVelocity();
//	      Location bLoc = new Location(entityLoc.getWorld(), entityLoc.getX() + vec.getX(), entityLoc.getY() + vec.getY(), entityLoc.getZ() + vec.getZ());
//	      if (bLoc.getBlock().getType() == Material.SNOW_BLOCK)
//	        bLoc.getBlock().setType(Material.AIR); 
//	    } 
//	  }
	
	
	
	
	// season starter bundles
//	bundle at SOTW.
//	list of people
	
//	Reecepbcups:
//	  - /bundle give Reecepbcups IslandBundle 1
//
//then on join, if config.getKeys contains playername, wait 5 seconds, then run commands. set to null after.
	
	
	
	
	
	
	
	
	
	
	
	
	
}
