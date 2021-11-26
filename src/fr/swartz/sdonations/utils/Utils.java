package fr.swartz.sdonations.utils;

import fr.swartz.sdonations.SDonations;
import org.bukkit.ChatColor;

import java.io.File;

public class Utils {

    public static File getFormatedFile(String fileName) {
        return new File(SDonations.getInstance().getDataFolder(), fileName);
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
