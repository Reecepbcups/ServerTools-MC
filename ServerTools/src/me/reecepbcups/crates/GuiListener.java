package me.reecepbcups.crates;

import org.bukkit.event.Listener;

// new GUIListener to try and clean stuff up

// https://github.com/badbones69/Crazy-Crates/blob/6d1659cd865b7605e16c7bbce900dde512dc04a5/plugin/src/main/java/me/badbones69/crazycrates/cratetypes/CSGO.java#L79

public class GuiListener implements Listener {

	// probably dont use this at all
	
//	private static Main plugin;
//	private static Crate crate;
//	static String EmeraldMSG2 = Util.color("&bYou have &n1&b more option to choose");;
//
//	private final Boolean DEBUG = false;
//
//	// used to see what items the player has clicked. This fixes to make setPrevItem skip these numbers
//	// then get the length to test if player has clicked 2 slots
//	private HashMap<String, List<Integer>> slotsClicked = new HashMap<String, List<Integer>>();
//
//
//	private Random random;
//	private Long turnspeed;
//	// Player, InvClone
//	private static HashMap<String, Inventory> playersCrateInventory = new HashMap<String, Inventory>();
//
//	// Title -> Inventory
//	private static HashMap<String, Inventory> CrateGUIS = new HashMap<String, Inventory>();
//
//	// [player: Config_KEY] -- used when clicking items to have list prepicked
//	static HashMap<String, List<String>> playerRewards = new HashMap<String, List<String>>();
//
//	// NAME: [prevItem, finalItem]
//	static HashMap<String, List<ItemStack>> playerCrateItems = new HashMap<String, List<ItemStack>>(); 
//	// NAME: [index, slotNumber]
//	static HashMap<String, List<Integer>> playerCrateNumbers = new HashMap<String, List<Integer>>(); 
//
//	// player -> runnableIDForAnimation
//	private static HashMap<String, Integer> playerRunnableID = new HashMap<String, Integer>();
//
//	// TODO
//	// Fix winning item, then closing inv & getting a second win
//	
//	public GuiListener(Main instance) {
//		plugin = instance;
//		crate = new Crate();
//
//
//		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
//		random = new Random();		
//		turnspeed = 5L;		
//
//		// puts invs with correct titles into crateGUI to be used later
//		for(String key : Main.getCrateTitles()) {	
//			print("KEY " + key);
//			CrateGUIS.put(key, Crate.initializeCrate(key));
//		}
//	}
//
//	public void print(String str) {
//		if(DEBUG) 
//			Util.consoleMSG(str);		
//	}
//
//
//	public static void openCrateInv(Player p, String invName) { // String CRATE_NAME
//		//if(!playersCrateInventory.containsKey(player.getName())) {	
//
//		resetPlayersRunnableID(p.getName()); // sets to 0 so inv does not close in new menu
//
//		// creates a new inv with new name based off of the 
//		Inventory newInv = Bukkit.createInventory(null, 3*9, invName);;
//		newInv.setContents(CrateGUIS.get(invName).getContents());
//
//		playersCrateInventory.put(p.getName(), newInv);
//
//		// generates crates rewards before open
//		generateCrate(p, plugin.crateNameToID(invName));
//		p.openInventory(getOpenPlayerCrate(p));
//
//	}
//
//
//
//
//	@EventHandler
//	public void crateCloseEvent(InventoryCloseEvent e) {
//
//		Player p = (Player) e.getPlayer();
//		//String pName = p.getName();
//
//		String Title = "";
//		if(Util.isVersion1_8()) {
//			Title = e.getInventory().getName();	
//		} else {
//			Title = e.getView().getTitle();				
//		}
//		// checks if this is a crate based on title
//		if(Main.isCrateGUI(Title)) {
//			// give first to check if they have any items clicked and such
//			giveRandomRewardFromCrate(p, plugin.crateNameToID(Title));			
//			//removeAllOpenCrateValues(p);
//		}
//	}
//
//	public void removeAllOpenCrateValues(Player player) {
//		String pName = player.getName();
//
//		playersCrateInventory.remove(pName);		
//		playerCrateItems.remove(pName);
//		playerCrateNumbers.remove(pName);
//		playerRewards.remove(pName);
//		slotsClicked.remove(pName);	
//		
//		//public void stopRotationRunnable(Player p) {
//				// stops running task & closes players inv within 2 seconds
//
//		int runnableID = getPlayersRunnableID(player.getName());
//		if(runnableID != 0) {
//			Bukkit.getScheduler().cancelTask(runnableID);			
//
//			// broke when player would open back up really quick
//			// maybe add an open counter? or something idk
//			new BukkitRunnable() {
//				@Override
//				public void run() {	
//					// run again, and gets the players runnableID.
//					// if they opened a new inv within 2 seconds, this disables
//					// the closing of the new inventory
//					if(getPlayersRunnableID(pName) != 0) {	
//						print("Did not equal 0 for player " + pName);
//						if(player.getOpenInventory().getTopInventory().getViewers().contains((HumanEntity) player)) {							
//							player.closeInventory();	
//						}						
//					}
//				}
//			}.runTaskLater(plugin, 1*20L);
//		}
//		
//		// clear this after above runnable
//		playerRunnableID.remove(pName);
//	}
//		
//	
//
//
//	public static Set<String> getPlayersInInvs() {
//		return playersCrateInventory.keySet(); // called in main
//	}	
//
//	public Integer getIndex(String name){
//		return playerCrateNumbers.get(name).get(0);
//	}
//	public Integer setIndex(String name, int newIndex){
//		return playerCrateNumbers.get(name).set(0, newIndex);
//	}
//
//	public Integer getSlot(String name){
//		return playerCrateNumbers.get(name).get(1);
//	}
//	public Integer setSlot(String name, int newSlot){
//		return playerCrateNumbers.get(name).set(1, newSlot);
//	}
//
//	public ItemStack getPrevItem(String name){
//		return playerCrateItems.get(name).get(0);
//	}
//	public ItemStack setPrevItem(String name, ItemStack prevItem){	
//		return playerCrateItems.get(name).set(0, prevItem);
//	}
//	public ItemStack getFinalItem(String name){
//		return playerCrateItems.get(name).get(1);
//	}
//
//	private Integer getPlayersRunnableID(String PlayerName){
//		if(playerRunnableID.containsKey(PlayerName)) {
//			return playerRunnableID.get(PlayerName);
//		}
//		return 0;
//	}
//	private static void resetPlayersRunnableID(String PlayerName){
//		// this is done so it does not auto close inv
//		playerRunnableID.put(PlayerName, 0);
//	}
//
//	public static Inventory getOpenPlayerCrate(Player p ) {
//		return playersCrateInventory.get(p.getName());
//	}
//	// --------------------------
//
//
//	public static void generateCrate(Player p, String CrateName) {
//		List<ItemStack> items = new ArrayList<ItemStack>();
//		ItemStack item = getOpenPlayerCrate(p).getItem(crate.getFinalItemSlot());
//		items.add(item);	// prevItem   // both are black stained glass panes
//		items.add(item);	// finalItem 
//		playerCrateItems.put(p.getName(), items); 
//
//		List<Integer> ints = new ArrayList<Integer>();
//		ints.add(0);	// prevItem
//		ints.add(crate.getAllItemSlots().get(0));	// gets slot 10 to start animation from	
//		playerCrateNumbers.put(p.getName(), ints);
//
//
//		//public static void pickItemsForCrate(String playername, String Cratename) {	
//		List<String> rewards = new ArrayList<String>();
//		for(int i=0;i<=19;i++) {			 // 19 is total number of items
//			String itemkey = Main.getCrateWeightedRewards(CrateName).getRandom();
//			//Util.consoleMSG("Reward: "+i +"  - "+ itemkey + " obtained for crate " + Cratename);
//			rewards.add(itemkey);
//		}
//		playerRewards.put(p.getName(), rewards);
//	}
//
//
//	
//
//	int runnableID = 0;
//	public void startItemRotation(Player p, String Cratename) {			
//		//Util.consoleMSG(lastItem.getType().toString());		
//		String pname = p.getName();
//		Inventory pInv = getOpenPlayerCrate(p);
//		final List<Integer> CRATE_SLOTS = crate.getAllItemSlots();
//		
//		ItemStack SHINING_PANEl = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
//		addGlowing(SHINING_PANEl);
//		ItemStack NORMAL_PANEL = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
//		
//		// stop this for player somehow on inv close
//		runnableID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {	
//
//			@Override				
//			public void run() {	
//
//				
//				if(!playerRunnableID.containsKey(pname)) {
//					playerRunnableID.put(pname, runnableID);
//				}
//				
//
//				// if player closes inv, shut it off
//				try {
//					int itemIndex = playerCrateNumbers.get(pname).get(0);								
//
////					if(itemIndex == 0) {		
////						// sets last item the animation uses to the correct item
////						pInv.setItem(crate.getFinalItemSlot(), getFinalItem(pname));
////					} else {
////						// Removes slimeball from prev location & set it back to the item it was									
//						//crate.setInvItem(pInv, getPrevItem(pname), getPrevItem(pname).getItemMeta().getDisplayName(), CRATE_SLOTS.get(itemIndex-1));
////
//						//removeGlowing(getPrevItem(pname));
//						
////						if(slotsClicked.containsKey(pname)) {		
////							
////							// add 1 here?
////							if(!slotsClicked.get(pname).contains(getSlot(pname))) {
////
////								print("Set to Stained Glass Pane red");
////
////								// getPrevItem(pname)
////								crate.setInvItem(pInv, 
////										new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14), 
////										"", CRATE_SLOTS.get(itemIndex-1));								
////							}						
////						}
////					}	
//
//					
//					// when spinning, then do this
////					if(!slotsClicked.get(p.getName()).contains(getSlot(pname))) {
////						// add one here to getSlot?
////						setPanelToGlowing(p, Cratename, getSlot(pname));
////					} else {
////						setSlotToRewardItem(p, Cratename, getSlot(pname));
////					}					
//					
//					// 0 -> 18
//					int index = CRATE_SLOTS.get(getSlot(pname));
//					
//					
//					Material mat = pInv.getItem(index).getType();
//					
//					if(mat == Material.STAINED_GLASS_PANE) {
//						pInv.setItem(index, SHINING_PANEl);
//					} 
//					
//					if(index>=1) {
//						int Slot = CRATE_SLOTS.get(getSlot(pname))-1;
//						Material prevMat = pInv.getItem(Slot).getType();
//						
//						if(prevMat == Material.STAINED_GLASS_PANE) {
//							pInv.setItem(Slot, NORMAL_PANEL);
//						}
//						
//						
//						// removeGlowing(pInv.getItem(CRATE_SLOTS.get(getSlot(pname)-1)));
//					}
//
//
//					Util.consoleMSG("Slot, Index, ItemIndex: " + getSlot(pname) + " " + index);
//					
//					
////					if(index == -1) {
////						index = crate.getAllItemSlots().get(crate.getAllItemSlots().size()-1);
////					}
//
//					// moves index & slot to next so we can show that as the selected one
//					setIndex(pname, itemIndex+1); // maybe make a incIndex function
//					setSlot(pname, getSlot(pname)+1);
//					
//					// reset back to first index of the animation.
//					if(getIndex(pname) >= CRATE_SLOTS.size()) {		
//						Util.consoleMSG("Index set to 0 due to being higher than "+CRATE_SLOTS.size());
//						setIndex(pname, 0);
//					}
//
//				} catch (Exception e) {
//					// give random reward here to player
//					// GIVE REWARD HERE. Only works if they select 2 to start timer thing hmm		
//					// add some check if player opens crate, but does not select 2
//					
//					// add check here if player is in something, to then give the reward
//					
////					giveRandomRewardFromCrate(p, Cratename);	
//					return;
//				}
//			}
//		}, 0, turnspeed);
//	}
//
//
//	public void giveRandomRewardFromCrate(Player p, String CrateID) {
//		// If menu is closed without them having selected values
//
//		// if player did not click any, give random reward
//		// or
//		// // if player did not click 2 times, give random reward
//		String key = Main.getCrateWeightedRewards(CrateID).getRandom();
//		
//		if(!slotsClicked.containsKey(p.getName()) || slotsClicked.get(p.getName()).size() < 2) { // 					
//			runCommands(p, CrateID, key);
//		} 		
//		// stops to make sure they do not get double rewarded
//		removeAllOpenCrateValues(p);
//	}
//
//
//
//
//	@EventHandler
//	public void onInventoryClick(InventoryClickEvent event) {
//		Player p = (Player) event.getWhoClicked(); 
//		ItemStack clicked = event.getCurrentItem(); 
//
//		String Title = "";
//		if(Util.isVersion1_8()) {
//			Title = event.getInventory().getName();	
//		} else {
//			Title = event.getView().getTitle();				
//		}
//		// checks if this is a crate based on title
//		if(Main.isCrateGUI(Title)) { 
//			event.setCancelled(true); 
//		}
//
//		if(clicked == null) { return; }
//
//		String CrateID = plugin.crateNameToID(Title); //ex. vote
//		String name = event.getWhoClicked().getName(); // Reecepbcups
//		Inventory pInv = getOpenPlayerCrate(p);
//		Integer slot = event.getSlot();
//
//		//if(clicked.getType() == Material.STAINED_GLASS_PANE) { // if allowed slot is clicked
//		if(crate.getAllItemSlots().contains(slot)) {
//
//			// checks number of times player clicks on the glass pane
//			if(!slotsClicked.containsKey(name) || slotsClicked.get(name).size() == 0) {	
//				List<Integer> newList = new ArrayList<Integer>();
//				newList.add(slot+1);
//				slotsClicked.put(name, newList);
//			} else {
//				List<Integer> newList = slotsClicked.get(name);		
//				newList.add(slot+1); // addding 1 fixes issue with item spin
//				slotsClicked.put(name, newList);
//			}
//						
//			// when they have clicked 1 slot, show to click 1 more item
//			if(slotsClicked.get(name).size() == 1) {						
//				crate.setInvItem(pInv, new ItemStack(Material.EMERALD), EmeraldMSG2, 13);
//				Util.consoleMSG(slotsClicked.get(name).toString());
//				
//				// When player clicks on a panel, get that ID & showcase the prize for that slot
//				setSlotToRewardItem(p, CrateID, slot);
//			}
//			
//			// makes sure they only click the panels 2 times
//			if(slotsClicked.get(name).size() == 2) {	
//				// only done on 2								
//				crate.setInvItem(pInv, new ItemStack(Material.REDSTONE_BLOCK), "&7Click the button to stop the Rotation", 13);
//				setSlotToRewardItem(p, CrateID, slot);
//				
//				//if(!playerRunnableID.containsKey(p.getName())) {
//				startItemRotation(p, CrateID);
//				//}
//				Util.consoleMSG(slotsClicked.get(name).toString() + "and starting item rotation");
//			} 				
//
//			if(slotsClicked.get(name).size() > 2) {
//				Util.coloredMessage(p, "&cYou can only click 2 panels...");
//			}
//			event.setCancelled(true);
//		}
//
//
//		if(clicked.getType() == Material.REDSTONE_BLOCK) {
//			// removes stone button once clicked
//			pInv.setItem(13, null);
//
//			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
//
//				@Override				
//				public void run() {						
//					String key = getRewardKeyAtPlayersSlot(p, getSlot(p.getName())-1);  // -1 gets correct value
//					runCommands(p, CrateID, key);
//				}
//			},  random.nextInt(4)*turnspeed);
//		}
//	}
//
//	// used in onInventoryClick, and also should try to use for select spinner
//	public void setSlotToRewardItem(Player player, String CrateID, int Slot) {
//		String rewardKey = getRewardKeyAtPlayersSlot(player, Slot);
//
//		//Util.consoleMSG("GUIListener RewardKey: "+rewardKey);
//		ItemStack rewardItem = getItemStack(CrateID, rewardKey);
//
//		Material mat = rewardItem.getType();							
//		ItemMeta im = rewardItem.getItemMeta();
//
//		Inventory inv = getOpenPlayerCrate(player);
//		ItemStack item = inv.getItem(Slot);		
//		item.setItemMeta(im);	
//		item.setDurability((short)0);
//		item.setType(mat);
//		//item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
//	}
//	
//	public void setPanelToGlowing(Player player, int Slot) {
//
//		//Util.consoleMSG("GUIListener RewardKey: "+rewardKey);
//		ItemStack rewardItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
//
//		Material mat = rewardItem.getType();							
//		ItemMeta im = rewardItem.getItemMeta();
//
//		Inventory inv = getOpenPlayerCrate(player);
//		ItemStack item = inv.getItem(Slot);		
//		item.setItemMeta(im);	
//		item.setDurability((short)14);
//		item.setType(mat);
//
//		addGlowing(item);
//	}
//
//	
//	public void removeGlowing(ItemStack item) {
//		item.removeEnchantment(Enchantment.DAMAGE_ALL);
//	}
//	public void addGlowing(ItemStack item) {
//		item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
//	}
//
//
//	// runs the reward commands 
//	public void runCommands(Player player, String CrateID, String key) {
//		FileConfiguration crate = plugin.getConfigFile("crates"+File.separator+CrateID+".yml");		
//		for(String cmd : crate.getStringList("rewards."+key+".Comamands")) {
//			Util.console(cmd.replace("%player%", player.getName()));
//		}
//		//stopRotationRunnable(player);
//		removeAllOpenCrateValues(player);
//	}
//
//
//
//
//
//
//	private String getRewardKeyAtPlayersSlot(Player p, int Slot) {
//		// gets the key from the crate based on the slot clicked
//		int RewardNum = crate.getAllItemSlots().indexOf( Slot ); 
//		String s = null;
//		if(playerRewards.containsKey(p.getName())) {
//			s = playerRewards.get(p.getName()).get(RewardNum);				
//		}
//		return s; //"rare1";
//	}
//
//
//	// gets the ItemStack with all values from a Cratename & Config Key value
//	public ItemStack getItemStack(String CrateID, String Key) {		
//		FileConfiguration CRATECONFIG = plugin.getConfigFile("crates"+File.separator+CrateID+".yml");		
//		Material mat = Material.valueOf(CRATECONFIG.getString("rewards."+Key+".Material").toUpperCase());
//		String name = CRATECONFIG.getString("rewards."+Key+".Name");
//		List<String> lore = CRATECONFIG.getStringList("rewards."+Key+".Lore");		
//		ItemStack item = new ItemStack(mat);
//		item.setDurability((short) 0); // fixed blocks becoming purple/black
//		ItemMeta im = item.getItemMeta();
//		im.setDisplayName(Util.color(name));
//		im.setLore(Util.color(lore));
//		item.setItemMeta(im);		
//		return item;		
//	}





	// this would work for when actually showing items
//	public static ItemStack itemStackSubData(final String line) {		
//		String material = line;
//		int data = 0;
//		if(line.contains(":")) {
//			material = line.split(":")[0];
//			data = Integer.valueOf(line.split(":")[1]);
//		}			
//		ItemStack item = new ItemStack(Material.valueOf(material.toUpperCase()), 1, (byte)data);		
//		return item;
//	}


	// end
}