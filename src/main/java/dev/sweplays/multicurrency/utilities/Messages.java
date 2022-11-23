package dev.sweplays.multicurrency.utilities;

import dev.sweplays.multicurrency.MultiCurrency;

public enum Messages {

    PREFIX("plugin.prefix"),

    ERROR("plugin.error"),

    NO_PERMISSION("plugin.no-permission"),

    PAY_USAGE("commands.pay-usage"),

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
    PAY_NOT_ENOUGH("currency.pay.not-enough"),

    BALANCE("currency.balance"),
    BALANCE_TARGET("currency.balance-target"),

    NO_DEFAULT_CURRENCY("currency.no-default-currency"),
    CURRENCY_NOT_FOUND("currency.currency-not-found"),
    PLAYER_NOT_FOUND("currency.player-not-found"),

    PAYMENTS_ON("currency.payments-on"),
    PAYMENTS_OFF("currency.payments-off"),
    TARGET_PAYMENTS_OFF("currency.target-payments-off"),
    MINIMUM_PAYMENT("currency.minimum-payment"),

    CREATE_SUCCESS("currency.create-success"),
    CREATE_ERROR("currency.create-error"),

    ONLY_NUMBERS("currency.only-numbers"),

    UNDER_ZERO("currency.under-zero"),
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
