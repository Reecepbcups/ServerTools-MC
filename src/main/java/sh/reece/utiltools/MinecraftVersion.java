package sh.reece.utiltools;

import org.bukkit.Bukkit;

public enum MinecraftVersion {
    
    UNKNOWN, 
    V1_8_R1, 
    V1_8_R2, 
    V1_8_R3, 
    V1_9_R1, 
    V1_9_R2,
    V1_10_R1, 
    V1_11_R1, 
    V1_12_R1, 
    V1_13_R1, 
    V1_13_R2, 
    V1_14_R1, 
    V1_15_R1, 
    V1_16_R1, 
    V1_16_R2, 
    V1_16_R3, 
    V1_17_R1, 
    V1_18_R1;

    public static MinecraftVersion getVersion(){
        return MinecraftVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].toUpperCase());
    }

    public boolean isAbove(MinecraftVersion compare){
        return ordinal() > compare.ordinal();
    }

    public boolean isAboveOrEqual(MinecraftVersion compare) {
        return (ordinal() >= compare.ordinal());
    }

    public boolean isBelow(MinecraftVersion compare){
        return (ordinal() < compare.ordinal());
    }

    public boolean isBelowOrEqual(MinecraftVersion compare){
        return (ordinal() <= compare.ordinal());
    }

    public boolean isEqual(MinecraftVersion compare){
        return (ordinal() == compare.ordinal());
    }
}
