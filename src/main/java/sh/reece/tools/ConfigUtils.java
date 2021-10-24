package sh.reece.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sh.reece.utiltools.ConfigUpdater;
import sh.reece.utiltools.Metrics;
import sh.reece.utiltools.Util;

public class ConfigUtils {
    
    private Main plugin;

	private final HashMap<String, String> LANG = new HashMap<>();

    public ConfigUtils(Main instance){
        plugin = instance;
    }

    public void loadConfig() {

		// BStats Metrics
		new Metrics(plugin, 11289);

		createConfig("config.yml");			
		plugin.getConfig().options().copyDefaults(true);	

		reloadLanguage(plugin.getConfig().getString("Language"));

		try {
			ConfigUpdater.update(plugin, "config.yml", new File(plugin.getDataFolder(), "config.yml"), new ArrayList<String>());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		plugin.setPAPIStatus(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"));

        _loadLocalServerVariableKeys();
	}

    private void _loadLocalServerVariableKeys() {
		for(String key : plugin.getConfig().getConfigurationSection("PluginVariables").getKeys(false)){
			Main.SERVER_VARIABLES.put(key, plugin.getConfig().getString("PluginVariables."+key));
			Main.SERVER_VARIABLE_KEYS.add(key);
		}
	}

    public FileConfiguration getConfigFile(final String name) {
		return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), name));
	}

	public void createDirectory(final String DirName) {
		final File newDir = new File(plugin.getDataFolder(), DirName.replace("/", File.separator));
		if (!newDir.exists()){
			newDir.mkdirs();
		}
	}

	public FileConfiguration createConfig(final String name) {
		final File file = new File(plugin.getDataFolder(), name);

		if (!new File(plugin.getDataFolder(), name).exists()) {

			plugin.saveResource(name, false);
		}

		final FileConfiguration configuration = getConfigFile(name);
		if (!file.exists()) {
			try {
				configuration.save(file);
			}			
			catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return configuration;
	}

	public void createFile(final String name) {
		final File file = new File(plugin.getDataFolder(), name);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch(final Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void saveConfig(final FileConfiguration config, final String name) {
		try {
			config.save(new File(plugin.getDataFolder(), name));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

    public void reloadLanguage(String lang) {
		LANG.clear();

		createDirectory("translations");
		createConfig("translations/"+lang+".yml");				
		final FileConfiguration language = getConfigFile("translations/"+lang+".yml");
		for(final String key : language.getKeys(false)) {
			LANG.put(key, language.getString(key));
		}
	}
	// public HashMap<String, String> getlang(){
	// 	return LANG;
	// }
	public String lang(final String key) {
		return Util.color(LANG.get(key).replace("%prefix%", "&7[&eServerTools&7]&r"));
	}

}
