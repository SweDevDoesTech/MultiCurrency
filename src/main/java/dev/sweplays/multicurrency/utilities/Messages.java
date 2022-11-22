package dev.sweplays.multicurrency.utilities;

import dev.sweplays.multicurrency.MultiCurrency;

public enum Messages {

    PREFIX("plugin.prefix"),

    NO_DEFAULT_CURRENCY("currency.no-default-currency"),

    NO_PERMISSION("plugin.no-permission"),

    ADD_SUCCESS("currency.add.success"),
    ADD_SUCCESS_NO_TARGET("currency.add.success-no-target"),
    ADD_SUCCESS_TARGET("currency.add.success-target"),

    SET_SUCCESS("currency.set.success"),
    SET_SUCCESS_NO_TARGET("currency.set.success-no-target"),
    SET_SUCCESS_TARGET("currency.set.success-target"),

    REMOVE_SUCCESS("currency.remove.success"),
    REMOVE_SUCCESS_NO_TARGET("currency.remove.success-no-target"),
    REMOVE_SUCCESS_TARGET("currency.remove.success-target"),

    PAY_SUCCESS("currency.pay.success"),
    PAY_SUCCESS_TARGET("currency.pay.success-target"),
    PAY_ERROR("currency.pay.error"),
    PAY_NOT_ENOUGH("currency.pay.not-enough"),

    BALANCE("currency.balance"),
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
