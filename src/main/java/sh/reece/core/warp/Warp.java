package sh.reece.core.warp;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import sh.reece.tools.ConfigUtils;
import sh.reece.utiltools.Util;

public class Warp {
    
    private String name;
    private Location location;
    private String permission = null;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Warp(String name, Location location, String permission) {
        this.name = name;
        this.setLocation(location);
        this.setPermission(permission);
    }    

    public String getName() {
        return name;
    }

    public void saveToConfig() {
        configSet(this.name, Util.locationToString(getLocation()), getPermission());
    }

    public void removeFromConfig() {
        configSet(this.name, null, null);
    }

    private void configSet(String name, String location, String permission) {
        FileConfiguration warps = ConfigUtils.getInstance().getConfigFile("Warps.yml");
        warps.set(this.name + ".permission", permission);
        warps.set(this.name + ".location", location);
        ConfigUtils.getInstance().saveConfig(warps, "Warps.yml");
    }

}
