package com.semivanilla.bounties.api.events;

import com.semivanilla.bounties.api.enums.BountyStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BountyStatusChange extends Event {

    private final BountyStatus status;
    private final Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public BountyStatusChange(BountyStatus status, Player player) {
        this.status = status;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public BountyStatus getStatus() {
        return status;
    }

    public Player getPlayer() {
        return player;
    }
}
