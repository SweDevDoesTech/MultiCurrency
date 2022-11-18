package dev.sweplays.multicurrency.utilities.interfaces;

public enum Messages {

    CURRENCY_ADD_SUCCESS("currency.add.success");
    //CURRENCY_ADD_SUCCESS("&r&lMulti&6&lCurrency &7> &aSuccessfully added %amount% of %currency% to %player%'s account.");

    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
