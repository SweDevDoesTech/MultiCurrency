package dev.sweplays.multicurrency.files;

import dev.sweplays.multicurrency.MultiCurrency;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesFile {

    @Getter
    File file;

    @Getter
    YamlConfiguration yamlConfiguration;

    FileConfiguration fileConfiguration;

    public MessagesFile() {
        file = new File(MultiCurrency.getInstance().getDataFolder(), "messages.yml");
        yamlConfiguration = new YamlConfiguration();
        fileConfiguration.getDefaults();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }

    }
}
