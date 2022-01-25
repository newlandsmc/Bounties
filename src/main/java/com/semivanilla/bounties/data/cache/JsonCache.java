package com.semivanilla.bounties.data.cache;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.Bounty;
import de.leonhard.storage.Json;
import de.leonhard.storage.sections.FlatFileSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class JsonCache implements CacheImpl {

    private final Bounties plugin;
    private Json cache;

    public JsonCache(Bounties plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean initCacheSystem() {
        cache = plugin.getUtilsManager().getFileUtility().createJson("cache.json");
        return cache != null;
    }

    @Override
    public boolean containsCacheForPlayer(@NotNull UUID playerID) {
        final long currentTime = System.currentTimeMillis();
        if(cache.contains(playerID.toString())){
            if(currentTime >= cache.getLong(playerID.toString()+".remaining")) {
                this.removeCache(playerID);
                return false;
            } else return true;
        } else return false;
    }

    @Override
    public void insertCache(@NotNull Bounty bounty) {
        cache.set(bounty.getPlayerUUID().toString(),bounty.serializeForJsonCache());
    }

    @Override
    public void removeCache(@NotNull UUID uuid) {
        if(cache.contains(uuid.toString()))
            cache.remove(uuid.toString());
    }

    @Override
    public Optional<Bounty> getBounty(@NotNull UUID uuid) {
        final Player player = plugin.getServer().getPlayer(uuid);
        if(player == null)
            return Optional.empty();
        final Bounty bounty = Bounty.buildFrom(player, new FlatFileSection(this.cache,uuid.toString()));
        return Optional.of(bounty);
    }

    @Override
    public Optional<Bounty> getBounty(@NotNull Player player){
        final Bounty bounty = Bounty.buildFrom(player, new FlatFileSection(this.cache,player.getUniqueId().toString()));
        return Optional.of(bounty);
    }

    @Override
    public void updateBounty(@NotNull Bounty bounty) {
        if(!containsCacheForPlayer(bounty.getPlayerUUID()))
            insertCache(bounty);
        else {
            cache.remove(bounty.getPlayerUUID().toString());
            cache.set(bounty.getPlayerUUID().toString(),bounty.serializeForJsonCache());
        }
    }

    @Override
    public void stop() {
        //This method was written if in future a database is needed!
        plugin.getLogger().info("Saving data of current bounties to cache.json");
        plugin.getDataManager().getAllBounty().forEach(this::updateBounty);
    }
}
