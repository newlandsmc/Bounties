package com.semivanilla.bounties.manager;

import com.semivanilla.bounties.Bounties;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderManager extends PlaceholderExpansion {

    private Bounties javaPlugin;

    public PlaceholderManager(Bounties javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bs";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Alen_Alex";
    }

    @Override
    public @NotNull String getVersion() {
        return javaPlugin.getDescription().getVersion();
    }

    @NotNull
    public String getName(){
        return "Bounties";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        if(params.equalsIgnoreCase("tag")){
            if(javaPlugin.getDataManager().isPlayerBounty(player.getUniqueId()))
                return javaPlugin.getConfiguration().getPlaceholderTagReplace();
            else return "";
        }

        if(params.equalsIgnoreCase("kills")){
            if(javaPlugin.getDataManager().isPlayerBounty(player.getUniqueId()))
                return String.valueOf(javaPlugin.getDataManager().getPlayerBounty(player.getUniqueId()).getCurrentKills());
            else return "0";
        }

        if(params.equalsIgnoreCase("timeleft")){
            if(javaPlugin.getDataManager().isPlayerBounty(player.getUniqueId())){
                return String.valueOf(javaPlugin.getDataManager().getPlayerBounty(player.getUniqueId()).getFormattedRemainingTime());
            }else return "0";
        }

        if(params.equalsIgnoreCase("level")){
            if(javaPlugin.getDataManager().isPlayerBounty(player.getUniqueId())){
                if(javaPlugin.getConfiguration().getRewardMap().containsKey(javaPlugin.getDataManager().getPlayerBounty(player.getUniqueId()).getCurrentKills()))
                    return String.valueOf(javaPlugin.getDataManager().getPlayerBounty(player.getUniqueId()).getCurrentKills());
                final String level = String.valueOf(javaPlugin.getConfiguration().getSortedRewardList().lower(javaPlugin.getDataManager().getPlayerBounty(player.getUniqueId()).getCurrentKills()));
                if(level == null)
                    return "";
                else return level;
            }else return "0";
        }

        if(params.equalsIgnoreCase("isbounty")){
            if(javaPlugin.getDataManager().isPlayerBounty(player.getUniqueId())){
                return String.valueOf(true);
            }else return String.valueOf(false);
        }

        if(params.equalsIgnoreCase("bountykills")){
            if(javaPlugin.getDataManager().getPlayerBountyKills().containsKey(player.getUniqueId()))
                return String.valueOf(javaPlugin.getDataManager().getBountyKillsOf(player.getUniqueId()));
            else return "0";
        }

        if(params.equalsIgnoreCase("online")){
            return String.valueOf(javaPlugin.getDataManager().getActiveBountyPlayer().size());
        }

        if(params.equalsIgnoreCase("online_formatted")){
            final int count = javaPlugin.getDataManager().getActiveBountyPlayer().size();
            if(count == 0)
                return javaPlugin.getConfiguration().getPlaceholderNoOnline();
            else return javaPlugin.getConfiguration().getPlaceholderOnline(count);
        }

        return null;
    }


}
