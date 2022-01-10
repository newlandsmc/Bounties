package com.semivanilla.bounties.manager;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.api.enums.BountyStatus;
import com.semivanilla.bounties.api.events.BountyStatusChange;
import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.utils.utility.LocationUtils;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DataManager {

    private final Bounties plugin;
    private final HashMap<UUID, Bounty> activeBountyPlayer;
    private final List<String> exemptFromBounty;
    private final HashMap<UUID, Integer> playerBountyKills;

    public DataManager(Bounties plugin) {
        this.plugin = plugin;
        this.activeBountyPlayer = new HashMap<>();
        this.playerBountyKills = new HashMap<>();
        this.exemptFromBounty = new ArrayList<String>();
    }

    public boolean isPlayerBounty(@NotNull Player player){
        return activeBountyPlayer.containsKey(player.getUniqueId());
    }

    public boolean isPlayerBounty(@NotNull UUID uuid){
        return activeBountyPlayer.containsKey(uuid);
    }

    public void createBountyForPlayer(Player killer){
        final Bounty bounty = new Bounty(killer,(System.currentTimeMillis()+plugin.getConfiguration().durationInMillis())).markPlayerAsBounty();
        plugin.getCache().insertCache(bounty);
        activeBountyPlayer.put(killer.getUniqueId(),bounty);
        final BountyStatusChange event = new BountyStatusChange(BountyStatus.PLAYER_BECAME_BOUNTY,killer);
        plugin.getServer().getPluginManager().callEvent(event);
    }
    public void createBountyForPlayer(Player killer,long duration){
        final Bounty bounty = new Bounty(killer,(System.currentTimeMillis()+duration)).markPlayerAsBounty();
        plugin.getCache().insertCache(bounty);
        activeBountyPlayer.put(killer.getUniqueId(),bounty);
        final BountyStatusChange event = new BountyStatusChange(BountyStatus.PLAYER_BECAME_BOUNTY,killer);
        plugin.getServer().getPluginManager().callEvent(event);
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
        final BountyStatusChange event = new BountyStatusChange(BountyStatus.PLAYER_NO_LONGER_BOUNTY,bounty.getPlayer());
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public Bounty getPlayerBounty(@NotNull UUID player){
        return activeBountyPlayer.get(player);
    }

    public void setKillOnBounty(@NotNull UUID uuid, int kills){
        activeBountyPlayer.get(uuid).setCurrentKills(kills);
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


        for(String s : plugin.getConfiguration().getExistingBountyMessage()){
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    MiniMessageUtils.broadcast(s
                            .replace("%kills%", String.valueOf(bounty.getCurrentKills()))
                            .replace("%killer%", player.getName())
                            .replace("%rand_loc%", LocationUtils.getRandomLocationFromARadius(player.getLocation(), 50))
                            .replace("%dead_player%", dead.getName()));
                }
            },plugin.getConfiguration().getCooldownTicks());

        }
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Methods for playerBountyKills Storage
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadBountyKillsFor(@NotNull UUID uuid){
        if(plugin.getDataStorage().containsStorageForPlayer(uuid)){
            this.playerBountyKills.put(uuid,plugin.getDataStorage().getPlayerData(uuid));
        }else {
            plugin.getDataStorage().insertPlayerData(uuid);
            this.playerBountyKills.put(uuid,plugin.getDataStorage().getPlayerData(uuid));
        }
    }

    public void saveBountyKillsOf(@NotNull UUID uuid){
        plugin.getDataStorage().setPlayerKill(uuid,playerBountyKills.get(uuid));
    }

    public int getBountyKillsOf(@NotNull UUID uuid){
        return playerBountyKills.get(uuid);
    }

    public void setBountyKills(@NotNull UUID uuid, int kills){
        playerBountyKills.put(uuid,kills);
        saveBountyKillsOf(uuid);
    }

    public void unloadBountyKillsFor(@NotNull UUID uuid){
        playerBountyKills.remove(uuid);
    }

    public void addBountyPlayerKill(@NotNull UUID uuid){
        this.playerBountyKills.put(uuid,this.playerBountyKills.get  (uuid)+1);
        plugin.getDataStorage().addBountyKill(uuid);
    }

    public HashMap<UUID, Integer> getPlayerBountyKills() {
        return playerBountyKills;
    }
}
