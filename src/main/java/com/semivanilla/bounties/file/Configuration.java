package com.semivanilla.bounties.file;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.RewardRank;
import de.leonhard.storage.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Configuration {

    private final Bounties plugin;
    private Config config;

    //Plugin Prefix
    private String pluginPrefix;

    //Bounty duration
    private long bountyDuration;

    //Rewards
    private final HashMap<Integer, RewardRank> rewardMap = new HashMap<>();

    private TreeSet<Integer> sortedRewardList;

    //Messages
    private List<String> newBountyBroadcast,bountyClear,existingBountyMessage;
    public Configuration(Bounties plugin) {
        this.plugin = plugin;
    }

    public boolean initConfig(){
        config = plugin.getUtilsManager().getFileUtility().createConfiguration();
        return config != null;
    }

    public void loadConfigData(){
        this.pluginPrefix = config.getString("prefix");
        this.bountyDuration = config.getInt("bounty-duration-in-sec");
        this.config.singleLayerKeySet("rewards.level").forEach((kills) -> {
            rewardMap.put(Integer.parseInt(kills), new RewardRank(Integer.parseInt(kills), config.getStringList("rewards.level."+kills)));
        });
        sortedRewardList = new TreeSet<>(rewardMap.keySet().stream().sorted().toList());

        this.newBountyBroadcast = config.getStringList("messages.new-bounty-broadcast");
        this.bountyClear = config.getStringList("messages.player-bounty-released-broadcast");
        this.existingBountyMessage = config.getStringList("messages.existing-bounty-broadcast");
    }



    public String getPluginPrefix() {
        return pluginPrefix;
    }

    public HashMap<Integer, RewardRank> getRewardMap() {
        return rewardMap;
    }

    public long durationInMillis(){
        return TimeUnit.SECONDS.toMillis(this.bountyDuration);
    }

    public List<String> getNewBountyBroadcast() {
        return newBountyBroadcast;
    }

    public List<String> getBountyClear() {
        return bountyClear;
    }

    public List<String> getExistingBountyMessage() {
        return existingBountyMessage;
    }

    public TreeSet<Integer> getSortedRewardList() {
        return sortedRewardList;
    }
}
