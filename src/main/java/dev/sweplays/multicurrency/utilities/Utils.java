package dev.sweplays.multicurrency.utilities;

import dev.sweplays.multicurrency.MultiCurrency;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

public class Utils {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String format(double money) {
        NumberFormat format = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        format.setGroupingUsed(true);
        format.setMinimumFractionDigits(1);

        return format.format(money);
    }
}
