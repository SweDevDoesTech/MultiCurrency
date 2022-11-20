package dev.sweplays.multicurrency.utilities;

import dev.sweplays.multicurrency.MultiCurrency;

public enum Messages {

    PREFIX("plugin.prefix"),

    CURRENCY_ADD_SUCCESS("currency.add.success"),
    CURRENCY_ADD_SUCCESS_TARGET("currency.add.success_target"),

    CURRENCY_REMOVE_SUCCESS("currency.remove.success"),
    CURRENCY_REMOVE_SUCCESS_TARGET("currency.remove.success_target"),
    ;

    private final String path;

    Messages(String message) {
        this.path = message;
    }

    private String getRaw() {
        return MultiCurrency.getFileManager().getMessagesFile().getYamlConfiguration().getString(path);
    }

    private String getReplaced() {
        return MultiCurrency.getFileManager().getMessagesFile().getYamlConfiguration().getString(path)
                .replace("{prefix}", PREFIX.getRaw());
    }

    public String get() {
        return Utils.colorize(getReplaced());
    }

    public String get(Object... args) {
        return String.format(Utils.colorize(getReplaced()), args);
    }
}
