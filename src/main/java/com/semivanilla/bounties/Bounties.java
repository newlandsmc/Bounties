package com.semivanilla.bounties;

import com.semivanilla.bounties.cache.CacheImpl;
import com.semivanilla.bounties.cache.JSONCache;
import com.semivanilla.bounties.file.Configuration;
import com.semivanilla.bounties.listener.PlayerConnectionListener;
import com.semivanilla.bounties.listener.PlayerDeathListener;
import com.semivanilla.bounties.utils.UtilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bounties extends JavaPlugin {

    private static Bounties plugin;
    private UtilityManager utilsManager;
    private Configuration configuration;
    private CacheImpl cache;
    private DataManager dataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        utilsManager = new UtilityManager(this);
        configuration = new Configuration(this);
        cache = new JSONCache(this);
        dataManager = new DataManager(this);

        if(!configuration.initConfig()){
            getLogger().severe("Unable to create/fetch config file. The plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //Loads the configuration and sets the prefix immediately
        configuration.loadConfigData();
        if(!configuration.validateConfigurationOptions()){
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        utilsManager.getMessageUtils().setPrefix(configuration.getPluginPrefix());

        //Configuration should be loaded first even before loading cache as in future if one need to set a storage type
        if(!cache.initCacheSystem()){
            getLogger().severe("Unable to create/initiate cache system. The plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this),this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this),this);
        new TestClass(this);
    }

    @Override
    public void onDisable() {
        cache.stop();
    }

    public static Bounties getPlugin() {
        return plugin;
    }

    public UtilityManager getUtilsManager() {
        return utilsManager;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public CacheImpl getCache() {
        return cache;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
