package com.semivanilla.bounties.data;

import com.semivanilla.bounties.file.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DataImpl {

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
     * @param player The UUID of the player that need to be checked
     * @return boolean If the player exists or not
     * TODO return {@link java.util.concurrent.CompletableFuture} instead of plain boolean
     */
    boolean containsStorageForPlayer(@NotNull UUID player);

    /**
     * Add the data for the player if the storage does not have the player registered
     * The no of kills should be default to 0
     * @param player The UUID of the player that need to be added.
     */
    void insertPlayerData(@NotNull UUID player);

    /**
     * Adds a kill to the bounty player. This method should increment the current value to 1
     * @param player The UUID of the player that need to be added.
     */
    void addBountyKill(@NotNull UUID player);

    /**
     * Sets the kill of a player to a specified amount.
     * This is also called when the player leaves the server to save the data to what he has currently
     * @param player The UUID of the player that need to be set
     * @param kills No of kills
     */
    void setPlayerKill(@NotNull UUID player, int kills);

    /**
     * Remove the kill data from the database
     * NOTE: Didn't added it yet anywhere
     * @param player The UUID of the player that need to be set
     */
    void removePlayerKill(@NotNull UUID player);

    /**
     * Get the no of kills a player has
     * @param player The UUID of the player that need to be set
     * @return int no of kills a player has
     * TODO return {@link java.util.concurrent.CompletableFuture} instead of plain int
     */
    int getPlayerData(@NotNull UUID player);

    /**
     * Called when the server stops.
     * Can be used to save all the data of the online player and close the active connection if there is any
     */
    void stop();

}
