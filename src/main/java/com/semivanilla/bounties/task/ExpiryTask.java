package com.semivanilla.bounties.task;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.Bounty;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ExpiryTask extends BukkitRunnable {

    private final Bounties plugin;

    public ExpiryTask(Bounties plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        final long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<UUID, Bounty>> bountyIterator = plugin.getDataManager().getActiveBountyPlayer().entrySet().iterator();
        while (bountyIterator.hasNext()){
            Map.Entry<UUID,Bounty> currentEntry = bountyIterator.next();
            if(currentTime > currentEntry.getValue().getTime()){
                currentEntry.getValue().unmarkAsBounty();
                plugin.getCache().removeCache(currentEntry.getValue().getPlayerUUID());
                bountyIterator.remove();
            }
        }
    }
}
