package com.semivanilla.bounties.file;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.RewardRank;
import de.leonhard.storage.Config;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Configuration {

    private final Bounties plugin;
    private FileConfiguration config;

    //Plugin Prefix
    private String pluginPrefix;

    //Bounty duration
    private long bountyDuration;
    private int cacheSec;

    //Rewards
    private final HashMap<Integer, RewardRank> rewardMap = new HashMap<>();

    //Placeholder Message
    private String placeholderTagReplace;
    private String placeholderNoOnline;
    private String placeholderOnline;
    private RewardRank rewardToKillNonBounty;

    //GUI
    private String GUIName,previousName,nextName,namePlaceholder;
    private int guiSize;
    private List<String> lorePlaceholder;

    private final HashMap<Integer,Integer> squareMapRadius = new HashMap<>();

    private TreeSet<Integer> sortedRewardList,sortedMapRadius;

    //Messages
    private int cooldownTicks;
    private List<String> helpHeader,helpFotter;
    private String helpMessage;
    private List<String> newBountyBroadcast,bountyClear,existingBountyMessage;
    public Configuration(Bounties plugin) {
        this.plugin = plugin;
    }

    public boolean initConfig(){
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        return config != null;
    }

    public void loadConfigData(){
        this.rewardMap.clear();
        this.squareMapRadius.clear();

        this.pluginPrefix = config.getString("prefix");
        this.bountyDuration = config.getInt("bounty-duration-in-sec");
        this.cacheSec = config.getInt("cache-player-data-after-the-leave-in-sec");
        this.rewardToKillNonBounty = RewardRank.buildFrom(config.getStringList("xp-for-killing-non-bounty"));

        this.config.getConfigurationSection("rewards.level").getKeys(false).forEach((kills) -> {
            rewardMap.put(Integer.parseInt(kills), new RewardRank(Integer.parseInt(kills), config.getStringList("rewards.level."+kills)));
        });
        sortedRewardList = new TreeSet<>(rewardMap.keySet().stream().sorted().toList());

        this.config.getConfigurationSection("square-map-radius").getKeys(false).forEach((kills) -> {
            squareMapRadius.put(Integer.parseInt(kills),config.getInt("square-map-radius."+kills));
        });
        sortedMapRadius = new TreeSet<>(squareMapRadius.keySet().stream().sorted().toList());

        this.placeholderTagReplace = ChatColor.translateAlternateColorCodes('&',config.getString("placeholder-tag-message"));
        this.placeholderNoOnline = ChatColor.translateAlternateColorCodes('&',config.getString("placeholder-tag-bounty-online-formatted.zero-online"));
        this.placeholderOnline = ChatColor.translateAlternateColorCodes('&',config.getString("placeholder-tag-bounty-online-formatted.online"));

        this.GUIName = config.getString("gui.name");
        this.guiSize = config.getInt("gui.rows");
        this.previousName = config.getString("gui.previous");
        this.nextName = config.getString("gui.next");
        this.namePlaceholder = config.getString("gui.name-player-placeholder");
        this.lorePlaceholder = config.getStringList("gui.lore-placeholder");

        this.cooldownTicks = config.getInt("interval-during-each-message-in-ticks");
        this.newBountyBroadcast = config.getStringList("messages.new-bounty-broadcast");
        this.bountyClear = config.getStringList("messages.player-bounty-released-broadcast");
        this.existingBountyMessage = config.getStringList("messages.existing-bounty-broadcast");
        this.helpHeader = config.getStringList("messages.help-message.header");
        this.helpFotter = config.getStringList("messages.help-message.fotter");
        this.helpMessage = config.getString("messages.help-message.comand-description");
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

    public String getPlaceholderTagReplace() {
        return placeholderTagReplace;
    }

    public String getGUIName() {
        return GUIName;
    }

    public String getPreviousName() {
        return previousName;
    }

    public String getNextName() {
        return nextName;
    }

    public int getGuiSize() {
        return guiSize;
    }

    public String getNamePlaceholder() {
        return namePlaceholder;
    }

    public List<String> getLorePlaceholder(int kills, String timeLeft) {
        final ArrayList<String> strings = new ArrayList<>();
        lorePlaceholder.forEach((s) -> {
            strings.add(s.replace("%kills%",String.valueOf(kills))
                    .replace("%timeleft%",timeLeft)
                    .replace("%fxp%",getFXPIfPresent(kills))
            );
        });
        return strings;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public int getSquareMapRadiusFor(int kills){

        try {
            return this.squareMapRadius.get(sortedMapRadius.lower(kills));
        }catch (Exception e){
            return this.squareMapRadius.get(sortedMapRadius.first());
        }
    }

    public String getPlaceholderNoOnline() {
        return placeholderNoOnline;
    }

    public String getPlaceholderOnline(int onlineCount) {
        return placeholderOnline.replace("%online%",String.valueOf(onlineCount));
    }

    public List<String> getHelpHeader() {
        return helpHeader;
    }

    public List<String> getHelpFotter() {
        return helpFotter;
    }

    public String getHelpMessage(String command, String description) {
        return helpMessage.replace("%command%",command).replace("%description%",description);
    }

    public int getCacheSec() {
        return cacheSec;
    }

    public RewardRank getRewardToKillNonBounty() {
        return rewardToKillNonBounty;
    }

    private String getFXPIfPresent(int kills){
        RewardRank rank = null;
        if(rewardMap.containsKey(kills))
            rank = rewardMap.get(kills);
        else rank = plugin.getConfiguration().getRewardMap().get(getSortedRewardList().lower(kills));

        if(rank == null)
            return "";

        Optional<String> xpString = rank.getFXPIfPresent();

        return xpString.orElse("");

    }
}
