package com.semivanilla.bounties.data;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DataImpl {

    boolean initCacheSystem();

    boolean containsStorageForPlayer(@NotNull UUID player);

    void insertPlayerData(@NotNull UUID player);

    void addBountyKill(@NotNull UUID player);

    void setPlayerKill(@NotNull UUID player, int kills);

    void removePlayerKill(@NotNull UUID player);

    int getPlayerData(@NotNull UUID player);

    void stop();

}
