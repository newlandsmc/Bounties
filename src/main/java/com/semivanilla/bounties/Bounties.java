package com.semivanilla.bounties;

import com.semivanilla.bounties.data.DataImpl;
import com.semivanilla.bounties.data.LocalDataStorage;
import com.semivanilla.bounties.data.cache.CacheImpl;
import com.semivanilla.bounties.data.cache.JsonCache;
import com.semivanilla.bounties.command.CommandHandler;
import com.semivanilla.bounties.file.Configuration;
import com.semivanilla.bounties.gui.BountyMenu;
import com.semivanilla.bounties.listener.PlayerConnectionListener;
import com.semivanilla.bounties.listener.PlayerDeathListener;
import com.semivanilla.bounties.manager.DataManager;
import com.semivanilla.bounties.manager.PlaceholderManager;
import com.semivanilla.bounties.task.ExpiryTask;
import com.semivanilla.bounties.utils.UtilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bounties extends JavaPlugin {

    private static Bounties plugin;
    private UtilityManager utilsManager;
    private Configuration configuration;
    private CacheImpl cache;
    private DataImpl dataStorage;
    private DataManager dataManager;
    private CommandHandler commandHandler;
    private BountyMenu menu;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        utilsManager = new UtilityManager(this);
        configuration = new Configuration(this);
        cache = new JsonCache(this);
        dataManager = new DataManager(this);
        commandHandler = new CommandHandler(this);
        dataStorage = new LocalDataStorage(this);

        if(!configuration.initConfig()){
            getLogger().severe("Unable to create/fetch config file. The plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //Loads the configuration and sets the prefix immediately
        configuration.loadConfigData();
        utilsManager.getMessageUtils().setPrefix(configuration.getPluginPrefix());

        //Intialize it as the configuration needed to be loaded for proper value call
        menu = new BountyMenu(this);

        //Configuration should be loaded first even before loading cache as in future if one need to set a storage type
        if(!cache.initCacheSystem()){
            getLogger().severe("Unable to create/initiate cache system. The plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(!dataStorage.initCacheSystem()){
            getLogger().severe("Unable to create/initiate data-storage system. The plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this),this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this),this);

        commandHandler.registerCommands();
        if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new PlaceholderManager(this).register();
        //new TestClass(this);
        new ExpiryTask(this).runTaskTimerAsynchronously(this,60,40);
    }



    public void handleReload(){
        if(!configuration.initConfig()){
            getLogger().severe("Unable to create/fetch config file. The plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        configuration.loadConfigData();
        utilsManager.getMessageUtils().setPrefix(configuration.getPluginPrefix());

    }

    @Override
    public void onDisable() {
        cache.stop();
        dataStorage.stop();
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

    public BountyMenu getMenu() {
        return menu;
    }

    public DataImpl getDataStorage() {
        return dataStorage;
    }
}
