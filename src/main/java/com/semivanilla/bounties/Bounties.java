package com.semivanilla.bounties;

import com.semivanilla.bounties.file.Configuration;
import com.semivanilla.bounties.utils.UtilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bounties extends JavaPlugin {

    private UtilityManager utilsManager;
    private Configuration configuration;

    @Override
    public void onEnable() {
        // Plugin startup logic
        utilsManager = new UtilityManager(this);
        configuration = new Configuration(this);

        if(!configuration.initConfig()){
            getLogger().severe("Unable to create/fetch config file. The plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Loads the configuration and sets the prefix immediately
        configuration.loadConfigData();
        utilsManager.getMessageUtils().setPrefix(configuration.getPluginPrefix());

        new TestClass(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public UtilityManager getUtilsManager() {
        return utilsManager;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
