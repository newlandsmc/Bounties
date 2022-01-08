package com.semivanilla.bounties.file;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.RewardRank;
import de.leonhard.storage.Config;

import java.util.HashMap;

public class Configuration {

    private final Bounties plugin;
    private Config config;

    //Plugin Prefix
    private String pluginPrefix;

    //Bounty duration
    private long bountyDuration;

    //Rewards
    private final HashMap<Integer, RewardRank> rewardMap = new HashMap<>();
    private int maxRewardToSearch;

    public Configuration(Bounties plugin) {
        this.plugin = plugin;
    }

    public boolean initConfig(){
        config = plugin.getUtilsManager().getFileUtility().createConfiguration();
        return config != null;
    }

    public void loadConfigData(){
        this.pluginPrefix = config.getString("prefix");
        this.maxRewardToSearch = config.getInt("rewards.max-rank");
        this.bountyDuration = config.getInt("bounty-duration-in-sec");
        this.config.singleLayerKeySet("rewards.level").forEach((level) -> {
            rewardMap.put(Integer.parseInt(level), new RewardRank(Integer.parseInt(level), config.getStringList("rewards.level."+level)));
        });
    }

    public String getPluginPrefix() {
        return pluginPrefix;
    }

    public HashMap<Integer, RewardRank> getRewardMap() {
        return rewardMap;
    }

    public int getMaxRewardToSearch() {
        return maxRewardToSearch;
    }
}
