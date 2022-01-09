package com.semivanilla.bounties.data;

import com.semivanilla.bounties.Bounties;
import de.leonhard.storage.Json;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocalDataStorage implements DataImpl{

    private final Bounties plugin;
    private Json data;

    public LocalDataStorage(Bounties plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean initCacheSystem() {
        data = plugin.getUtilsManager().getFileUtility().createJson("data.json");
        return data != null;
    }

    @Override
    public boolean containsStorageForPlayer(@NotNull UUID player) {
        if(data.contains(player.toString()))
            return true;
        else return false;
    }

    @Override
    public void insertPlayerData(@NotNull UUID player) {
        data.set(player.toString(),0);
    }

    @Override
    public void addBountyKill(@NotNull UUID player) {
        data.set(player.toString(),data.getInt(player.toString())+1);
    }

    @Override
    public void setPlayerKill(@NotNull UUID player, int kills) {
        data.set(player.toString(),kills);
    }

    @Override
    public void removePlayerKill(@NotNull UUID player) {
        data.remove(player.toString());
    }

    @Override
    public int getPlayerData(@NotNull UUID player) {
        return data.getInt(player.toString());
    }

    @Override
    public void stop() {
        plugin.getDataManager().getPlayerBountyKills().keySet().forEach((key) -> {
            plugin.getDataManager().saveBountyKillsOf(key);
        });
    }
}
