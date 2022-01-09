package com.semivanilla.bounties;

import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.utils.utility.LocationUtils;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DataManager {

    private final Bounties plugin;
    private final HashMap<UUID, Bounty> activeBountyPlayer;
    private final List<String> exemptFromBounty;

    public DataManager(Bounties plugin) {
        this.plugin = plugin;
        this.activeBountyPlayer = new HashMap<>();
        this.exemptFromBounty = new ArrayList<String>();
    }

    public boolean isPlayerBounty(@NotNull Player player){
        return activeBountyPlayer.containsKey(player.getUniqueId());
    }

    public boolean isPlayerBounty(@NotNull UUID uuid){
        return activeBountyPlayer.containsKey(uuid);
    }

    public void createBountyForPlayer(Player killer,Player deadPlayer){
        final Bounty bounty = new Bounty(killer,(System.currentTimeMillis()+plugin.getConfiguration().durationInMillis())).markPlayerAsBounty();
        plugin.getCache().insertCache(bounty);
        activeBountyPlayer.put(killer.getUniqueId(),bounty);
    }

    /**
     * Used to remove a players bounty status
     * @param player
     */
    public void clearBountyForPlayer(@NotNull UUID player){
        final Bounty bounty = activeBountyPlayer.get(player);
        bounty.unmarkAsBounty();
        activeBountyPlayer.remove(player);
        plugin.getCache().removeCache(player);
    }

    public Bounty getPlayerBounty(@NotNull UUID player){
        return activeBountyPlayer.get(player);
    }

    /**
     * Used when players disconnects
     * @param uuid
     */
    public void unloadFromCache(@NotNull UUID uuid){
        activeBountyPlayer.remove(uuid);
    }

    public void addNewKill(@NotNull Player player,Player dead){
        if(!activeBountyPlayer.containsKey(player.getUniqueId()))
            return;
        final Bounty bounty = activeBountyPlayer.get(player.getUniqueId());
        bounty.addNewKill();
        plugin.getConfiguration().getExistingBountyMessage().forEach((m) -> {
            MiniMessageUtils.broadcast(m
                    .replaceAll("%kills%", String.valueOf(bounty.getCurrentKills()))
                    .replaceAll("%killer%", player.getName())
                    .replaceAll("%rand-loc%", LocationUtils.getRandomLocationFromARadius(player.getLocation(), 50))
                    .replaceAll("%dead-player%", dead.getName()));
        });
    }

    public void loadBountyFromExternalCache(@NotNull Player player){
        Optional<Bounty> bountyOptional = plugin.getCache().getBounty(player.getUniqueId());
        if(bountyOptional.isPresent()){
            Bounty bounty = bountyOptional.get();
            bounty.markPlayerAsBounty();
            activeBountyPlayer.put(player.getUniqueId(),bounty);
        }else plugin.getLogger().info("A cache for player does not seems to exist in storage");
    }

    public boolean isPlayerExemptedFromBounty(@NotNull String playerName){
       return exemptFromBounty.contains(playerName);
    }

    public void addToExemptFromBounty(@NotNull String playerName){
        exemptFromBounty.add(playerName);
    }

    public void removeFromExempt(@NotNull String playerName){
        if(isPlayerExemptedFromBounty(playerName))
            exemptFromBounty.remove(playerName);
    }

    public Collection<Bounty> getAllBounty(){
        return this.activeBountyPlayer.values();
    }

    public HashMap<UUID, Bounty> getActiveBountyPlayer() {
        return activeBountyPlayer;
    }
}
