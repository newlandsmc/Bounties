package com.semivanilla.bounties.model;

import com.semivanilla.bounties.Bounties;
import de.leonhard.storage.sections.FlatFileSection;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bounty {

    private final Player player;
    private final long time;
    private static final String META_VALUE = "bounty";
    private int currentKills;
    private int currentLevel;

    public Bounty(Player player, long time) {
        this.player = player;
        this.time = time;
        this.currentKills = 1;
        this.currentLevel = 0;
    }

    private Bounty(Player player, long time, int currentKills, int currentLevel) {
        this.player = player;
        this.time = time;
        this.currentKills = currentKills;
        this.currentLevel = currentLevel;
    }

    public Player getPlayer() {
        return player;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedRemainingTime(){
        return DateFormat.getDateTimeInstance().format(new Date(this.time-System.currentTimeMillis()));
    }

    public UUID getPlayerUUID(){
        return player.getUniqueId();
    }

    public Bounty markPlayerAsBounty(){
        player.setMetadata(META_VALUE, new FixedMetadataValue(Bounties.getPlugin(), this.time));
        return this;
    }

    public void unmarkAsBounty(){
        player.removeMetadata(META_VALUE,Bounties.getPlugin());
        //Remove
    }

    public int getCurrentKills() {
        return currentKills;
    }

    public void addNewKill(){
        this.currentKills++;
    }

    public Map<String, Object> serializeForJsonCache(){
        final HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("remaining",this.time);
        stringObjectHashMap.put("kills",this.currentKills);
        stringObjectHashMap.put("level",this.currentLevel);
        return stringObjectHashMap;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public static Bounty buildFrom(@NotNull Player player, FlatFileSection section){
        final int level = section.getInt("level");
        if(Bounties.getPlugin().getConfiguration().getRewardMap().containsKey(level))
            return new Bounty(player,section.getLong("remaining"),section.getInt("kills"),level);
        else {
            //TODO
            Bounties.getPlugin().getLogger().info("The cached level of player "+player.getName()+" seems like invalid or not found in loaded config. The plugin will reset his level to 0. When he reaches the next matched level, he will be updated into new level");
            return new Bounty(player,section.getLong("remaining"),section.getInt("kills"),0);
        }
    }
}
