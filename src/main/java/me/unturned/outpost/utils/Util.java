package me.unturned.outpost.utils;

import me.unturned.outpost.objects.Chance;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class Util {

    public static String colour(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static int getRandomItem(Chance[] chances) {
        int randomIndex = -1;
        double random = Math.random() * Arrays.stream(chances).mapToDouble(Chance::chance).sum();
        for (int i = 0; i < chances.length; ++i) {
            random -= chances[i].chance();
            if (random <= 0.0d) {
                randomIndex = i;
                break;
            }
        }
        return randomIndex;
    }
}
