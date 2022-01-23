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
import java.util.concurrent.TimeUnit;

public class Bounty {

    private final Player player;
    private long time;
    private static final String META_VALUE = "bounty";
    private int currentKills;

    public Bounty(Player player, long time) {
        this.player = player;
        this.time = time;
        this.currentKills = 1;
    }

    private Bounty(Player player, long time, int currentKills) {
        this.player = player;
        this.time = time;
        this.currentKills = currentKills;
    }

    public Player getPlayer() {
        return player;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedRemainingTime(){
        String returnTime = "0";
        final long remainingTime = this.time - System.currentTimeMillis();
        final int hr = (int) TimeUnit.MILLISECONDS.toHours(remainingTime);

        if(hr >= 1){
            returnTime = hr+" Hours ";
        }else {
            final int mins = (int) TimeUnit.MILLISECONDS.toMinutes(remainingTime);
            if(mins > 1)
                returnTime = mins+" Minutes";
            else return "Less than a minute";
        }

        return returnTime;
    }

    public UUID getPlayerUUID(){
        return player.getUniqueId();
    }

    public Bounty markPlayerAsBounty(){
        updateMetaDataFor(Bounties.getPlugin().getConfiguration().getSquareMapRadiusFor(this.currentKills));
        return this;
    }

    public void unmarkAsBounty(){
        player.removeMetadata(META_VALUE,Bounties.getPlugin());
    }

    public int getCurrentKills() {
        return currentKills;
    }

    public void addNewKill(long updatedTime){
        this.currentKills++;
        this.time = updatedTime;
        updateMetaDataFor(Bounties.getPlugin().getConfiguration().getSquareMapRadiusFor(this.currentKills));
    }

    public void updateMetaDataFor(int value){
        player.setMetadata(META_VALUE, new FixedMetadataValue(Bounties.getPlugin(), value));
    }

    public Map<String, Object> serializeForJsonCache(){
        final HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("remaining",this.time);
        stringObjectHashMap.put("kills",this.currentKills);
        return stringObjectHashMap;
    }

    public void setCurrentKills(int currentKills) {
        this.currentKills = currentKills;
    }

    public static Bounty buildFrom(@NotNull Player player, FlatFileSection section){
        return new Bounty(player,section.getLong("remaining"),section.getInt("kills"));
    }

    public String getPlayerName(){
        return this.player.getName();
    }
}
