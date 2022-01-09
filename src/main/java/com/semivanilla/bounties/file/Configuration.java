package com.semivanilla.bounties.file;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.RewardRank;
import de.leonhard.storage.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    //Rewards
    private final HashMap<Integer,Integer> levelMap = new HashMap<>();
    private final HashMap<Integer,String> levelUpMessages = new HashMap<>();
    private String levelupDefaultMessage;

    //Messages
    private List<String> newBountyBroadcast,bountyClear;
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
        this.config.singleLayerKeySet("rewards.level").forEach((level) -> {
            rewardMap.put(Integer.parseInt(level), new RewardRank(Integer.parseInt(level), config.getStringList("rewards.level."+level)));
        });
        this.config.singleLayerKeySet("levels.kills").forEach((kills) ->{
            levelMap.put(Integer.parseInt(kills),config.getInt("levels.kills."+kills));
        });
        this.config.singleLayerKeySet("levels.messages").forEach((level) -> {
            if(!level.equals("default")) {
                levelUpMessages.put(Integer.parseInt(level), config.getString("levels.messages."+level));
            }
        });
        this.levelupDefaultMessage = config.getString("levels.messages.default");

        this.newBountyBroadcast = config.getStringList("messages.new-bounty-broadcast");
        this.bountyClear = config.getStringList("messages.player-bounty-released-broadcast");
    }

    public boolean validateConfigurationOptions(){
        plugin.getLogger().info("############################################");
        plugin.getLogger().info(" -> Validating Configs");
        boolean valid = true;
        if(!config.contains("levels.messages.default")){
            config.getOrDefault("levels.messages.default","&c%player% &bfreached %level%");
            plugin.getLogger().warning("The default message for rank level should not be removed. If you wish not to have a message, you can simple leave it blank");
        }
        valid = levelMap.values().stream().noneMatch(l -> !rewardMap.containsKey(l));
        if(!valid){
            plugin.getLogger().severe("The levels up configuration has missing levels in the reward value");
            return false;
        }

        //At last
        if(valid){
            plugin.getLogger().info("The configuration is valid");
        }
        plugin.getLogger().info("############################################");
        return valid;
    }

    public String getPluginPrefix() {
        return pluginPrefix;
    }

    public HashMap<Integer, RewardRank> getRewardMap() {
        return rewardMap;
    }

    public HashMap<Integer, Integer> getLevelMap() {
        return levelMap;
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

    public HashMap<Integer, String> getLevelUpMessages() {
        return levelUpMessages;
    }

    public String getLevelupDefaultMessage() {
        return levelupDefaultMessage;
    }
}
