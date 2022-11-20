package dev.sweplays.multicurrency.utilities;

import dev.sweplays.multicurrency.MultiCurrency;
import org.bukkit.Bukkit;

public class SchedulerUtils {

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(MultiCurrency.getInstance(), runnable);
    }

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(MultiCurrency.getInstance(), runnable);
    }

    public static void runLater(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(MultiCurrency.getInstance(), runnable, delay);
    }
}