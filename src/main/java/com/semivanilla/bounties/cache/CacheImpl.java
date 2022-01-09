package com.semivanilla.bounties.cache;

import com.semivanilla.bounties.model.Bounty;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * This interface has been written with updatability in mind
 * Later if one want to implement a SQL or NoSQL Storage System for the plugin
 * One can implement this class with minimal effort
 */

public interface CacheImpl {

    boolean initCacheSystem();

    boolean containsPlayer(@NotNull UUID playerID);

    void insertCache(@NotNull Bounty bounty);

    void removeCache(@NotNull UUID uuid);

    Optional<Bounty> getBounty(@NotNull UUID uuid);

    void updateBounty(@NotNull Bounty bounty);

    void stop();
}
