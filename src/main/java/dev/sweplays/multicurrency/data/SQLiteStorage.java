package dev.sweplays.multicurrency.data;

import com.google.gson.JsonElement;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;

import dev.sweplays.multicurrency.utilities.Utils;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLiteStorage extends DataStore {

    DatabaseManager databaseManager = MultiCurrency.getDatabaseManager();

    private final String SAVE_CURRENCY_SQL = "INSERT OR REPLACE INTO currencies (uuid, name, symbol, defaultBalance, payable, isDefault, material) VALUES (?,?,?,?,?,?,?)";
    private final String SAVE_ACCOUNT_SQL = "INSERT OR REPLACE INTO accounts (uuid, name, acceptingPayments, balanceData) VALUES (?,?,?,?)";

    public SQLiteStorage() {
        super("sqlite");
    }

    @Override
    public void initialize() {
        MultiCurrency.getInstance().getLogger().info("Creating tables in database if they do not exist...");

        databaseManager.execute("CREATE TABLE IF NOT EXISTS currencies (uuid VARCHAR(255) NOT NULL PRIMARY KEY, name VARCHAR(255), symbol VARCHAR(255), defaultBalance DOUBLE, payable BOOLEAN, isDefault BOOLEAN, material VARCHAR(255))");
        databaseManager.execute("CREATE TABLE IF NOT EXISTS accounts (uuid VARCHAR(255) NOT NULL PRIMARY KEY, name VARCHAR(255), acceptingPayments BOOLEAN, balanceData LONGTEXT NULL)");
        databaseManager.execute("CREATE TABLE IF NOT EXISTS balances (account VARCHAR(255), currency VARCHAR(255), balance DOUBLE)");
    }

    @Override
    public void saveAccount(Account account) {
        try {
            PreparedStatement statement = databaseManager.getPreparedStatement(SAVE_ACCOUNT_SQL);
            statement.setString(1, account.getOwnerUuid().toString());
            statement.setString(2, account.getOwnerName());
            statement.setBoolean(3, account.isAcceptingPayments());

            JSONObject jsonObject = new JSONObject();
            for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {
                jsonObject.put(currency.getUuid().toString(), account.getBalance(currency.getName()));
            }
            String json = jsonObject.toJSONString();
            statement.setString(4, json);
            statement.executeUpdate();

            MultiCurrency.getInstance().getLogger().info("Saved account: " + account.getOwnerName());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void deleteAccount(Account account) {
        try {
            PreparedStatement statement = databaseManager.getPreparedStatement("DELETE FROM accounts WHERE uuid = ? LIMIT 1");
            statement.setString(1, account.getOwnerUuid().toString());
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void createAccount(Account account) {
        try {
            PreparedStatement statement = databaseManager.getPreparedStatement(SAVE_ACCOUNT_SQL);
            statement.setString(1, account.getOwnerUuid().toString());
            statement.setString(2, account.getOwnerName());
            statement.setBoolean(3, account.isAcceptingPayments());

            JSONObject jsonObject = new JSONObject();
            for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {
                jsonObject.put(currency.getUuid().toString(), currency.getDefaultBalance());
            }
            String json = jsonObject.toJSONString();
            statement.setString(4, json);
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void loadCurrencies() {
        try {
            PreparedStatement statement = databaseManager.getPreparedStatement("SELECT * FROM currencies");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                String name = resultSet.getString("name");
                String symbol = resultSet.getString("symbol");
                double defaultBalance = resultSet.getDouble("defaultBalance");
                boolean payable = resultSet.getBoolean("payable");
                boolean isDefault = resultSet.getBoolean("isDefault");
                Material material = Material.valueOf(resultSet.getString("material"));
                Currency currency = new Currency(uuid, name);
                currency.setSymbol(symbol);
                currency.setDefaultBalance(defaultBalance);
                currency.setPayable(payable);
                currency.setDefault(isDefault);
                currency.setInventoryMaterial(material);

                MultiCurrency.getCurrencyManager().add(currency);
                MultiCurrency.getInstance().getLogger().info("Loaded currency: " + currency.getName());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Account returnAccountWithBalances(Account account) {
        return null;
    }

    /*
    @Override
    public Account returnAccountWithBalances(Account account) {
        if (account == null)
            return null;

        try {
            PreparedStatement statement = databaseManager.getPreparedStatement("SELECT * FROM balances WHERE uuid = ?");
            statement.setString(1, account.getOwnerUuid().toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String json = resultSet.getString("balanceData");

                if (json == null) {
                    return null;
                }

                JSONParser parser = new JSONParser();

                Object object = parser.parse(json);
                JSONArray jsonArray = (JSONArray) object;

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
                    for (Map.Entry<String, JsonElement> entry : entries) {

                    }
                }

                Currency currency = MultiCurrency.getCurrencyManager().getCurrency(UUID.fromString());
            }

        } catch (ParseException | SQLException exception) {
            exception.printStackTrace();
        }
        return account;
    }
     */

    @Override
    public List<Account> getOfflineAccounts() {
        List<Account> accounts = new ArrayList<>();

        try {
            PreparedStatement statement = databaseManager.getPreparedStatement("SELECT * FROM accounts");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.addAll(MultiCurrency.getAccountManager().getAccounts());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return accounts;
    }

    @Override
    public void saveCurrency(Currency currency) {
        try {
            PreparedStatement statement = databaseManager.getPreparedStatement(SAVE_CURRENCY_SQL);
            statement.setString(1, currency.getUuid().toString());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSymbol());
            statement.setDouble(4, currency.getDefaultBalance());
            statement.setBoolean(5, currency.isPayable());
            statement.setBoolean(6, currency.isDefault());
            statement.setString(7, currency.getInventoryMaterial().toString());
            statement.execute();

            MultiCurrency.getInstance().getLogger().info("Saved currency: " + currency.getName());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Account loadAccount(UUID uuid) {
        Account account = null;

        try {
            PreparedStatement statement = databaseManager.getPreparedStatement("SELECT * FROM accounts WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                account = new Account(uuid, resultSet.getString("name"));

                boolean payments = resultSet.getBoolean("acceptingPayments");
                String json = resultSet.getString("balanceData");

                account.setAcceptingPayments(payments);

                if (json == null) {
                    return null;
                }
                JSONParser jsonParser = new JSONParser();
                Object object = jsonParser.parse(json);
                JSONObject data = (JSONObject) object;

                for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {
                    Number amount = (Number) data.get(currency.getUuid().toString());
                    if (amount != null) {
                        account.getBalances().put(currency, amount.doubleValue());
                    } else {
                        account.getBalances().put(currency, currency.getDefaultBalance());
                    }
                }
            }
            resultSet.close();
        } catch (ParseException | SQLException exception) {
            exception.printStackTrace();
        }
        return account;
    }

    @Override
    public Account loadAccount(String name) {
        return null;
    }
}
