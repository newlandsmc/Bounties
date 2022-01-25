package com.semivanilla.bounties.data.cache;

import com.semivanilla.bounties.file.Configuration;
import com.semivanilla.bounties.model.Bounty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * This interface has been written with updatability in mind
 * Later if one want to implement a SQL or NoSQL Storage System for the plugin
 * One can implement this class with minimal effort
 */

public interface CacheImpl {

    /**
     * This method is used to initialize the data storage system the plugin features.
     * This method is called after initializing {@link Configuration#loadConfigData()} method. So one can call data even
     * from the configuration
     *
     * NOTE: If the connection fails, one should return false so that the plugin can be properly disabled
     * @return boolean Whether the connection or cache storage was successful
     */
    boolean initCacheSystem();

    /**
     * This method is used to check whether a player with provided UUID exists in the storage/
     * @param playerID The UUID of the player that need to be checked
     * @return boolean If the player exists or not
     * TODO return {@link java.util.concurrent.CompletableFuture} instead of plain boolean
     */
    boolean containsCacheForPlayer(@NotNull UUID playerID);

    /**
     * Add the data for the player if the storage does not have the player registered
     * @param bounty The bounty object
     */
    void insertCache(@NotNull Bounty bounty);

    /**
     * Removes the current bounty of the player
     * @param uuid The UUID of the player
     */
    void removeCache(@NotNull UUID uuid);

    /**
     * Returns an Optional value for bounty
     * @param uuid The UUID of the player
     * @return Optional value for bounty
     * @see {@link Optional}
     * @see {@link Bounty}
     */
    Optional<Bounty> getBounty(@NotNull UUID uuid);

    /**
     * Returns an optional parameter which can be used to gen them from a player object
     * NOTE: This is important as sometimes the anti-combat plugin triggers the event with NPC and the player object can
     * only be passed from the event
     * @param player
     * @return
     */
    Optional<Bounty> getBounty(@NotNull Player player);

    /**
     * Updates the details of Bounty.
     * This method is called when a player leaves the server
     * @param bounty The bounty object
     */
    void updateBounty(@NotNull Bounty bounty);

    /**
     * Called when the server stops.
     * Can be used to save all the data of the online player and close the active connection if there is any
     */
    void stop();
}
