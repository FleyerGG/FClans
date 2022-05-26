package ru.fleyer.fclans;

import com.earth2me.essentials.Essentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.fleyer.fclans.MySQL.MySQL;
import ru.fleyer.fclans.utils.ClanManager;
import ru.fleyer.fclans.utils.ClanWars;

public final class FClans extends JavaPlugin{
    private static FClans instance;
    public static MySQL mysql;
    private boolean placeholder;
    private static FileConfiguration configuration;
    private static ClanManager clanManager;
    private static Economy economy;
    public static Essentials essentials;

    public static FClans getInstance() {
        return instance;
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    public static ClanManager getClanManager() {
        return clanManager;
    }

    public static Economy getEconomy() {
        return economy;
    }

    private void later(Runnable runnable, int time) {
        Bukkit.getScheduler().runTaskLater(this, runnable, time);
    }

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        configuration = this.getConfig();
        this.setupEconomy();
        ClanAdmin.loadLocs();
        //Пока что берем из кфг
        mysql = new MySQL("пока что нема","пока что нема","пока что нема","пока что нема","пока что нема");
        this.later(() -> {
            clanManager = new ClanManager();
            System.out.println("[FClans] Инициализация кланов");
        }, 5);
        this.later(() -> clanManager.load(), 6);
        this.later(() -> {
            clanManager.updateStat();
            System.out.println("[FClans] Обновление статистики");
        }, 7);
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        this.getCommand("clan").setExecutor(new ClanCommand());
        this.getCommand("clanadmin").setExecutor(new ClanAdmin());
        ClanWars.war = false;
        ClanWars.inwar.clear();
        ClanWars.clans.clear();
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholder = true;
            new Placeholder(this).register();
        }
    }

    public void onDisable() {
        ClanWars.war = false;
        ClanWars.inwar.clear();
        ClanWars.clans.clear();
    }

    private void setupEconomy() {
        RegisteredServiceProvider economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = (Economy)economyProvider.getProvider();
        }
    }

    static {
        economy = null;
        essentials = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
    }
}

