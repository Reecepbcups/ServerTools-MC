package sh.reece.aFinishLater;


public class NametagToggle { //implements CommandExecutor, TabExecutor {

	// private Main main;
	// public Team playerset;

	// public NametagToggle(Main instance) {
	// 	main = instance;

	// 	if(!Util.isVersion1_8()) {
	// 		return;
	// 	}
		
	// 	Scoreboard score = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
	// 	Team t = score.getTeam("stoolssbtags");
	// 	if (t == null)
	// 		t = score.registerNewTeam("stoolssbtags"); 
	// 	this.playerset = t;
	// }

	// public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	// 	if (args.length >= 2 && 
	// 			args[0].equalsIgnoreCase("nametag") && (sender.isOp() || sender.hasPermission("evnt.nametoggle")))
	// 		if (args[1].equalsIgnoreCase("show")) {
	// 			playerset.setNameTagVisibility(NameTagVisibility.ALWAYS);
	// 			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTurned on name tag visibility"));
	// 		} else if (args[1].equalsIgnoreCase("hide")) {
	// 			playerset.setNameTagVisibility(NameTagVisibility.NEVER);
	// 			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTurned off name tag visibility"));
	// 		}  
	// 	return true;

	// }

	// @Override
	// public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
	// 	if (args.length == 1)
	// 		return Collections.singletonList("nametag"); 
	// 	if (args.length == 2 && 
	// 			args[0].equals("nametag"))
	// 		return Arrays.asList(new String[] { "show", "hide" }); 
	// 	return null;
	// }
}