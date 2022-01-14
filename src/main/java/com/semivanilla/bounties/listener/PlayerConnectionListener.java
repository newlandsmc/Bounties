package com.semivanilla.bounties.listener;

import com.semivanilla.bounties.Bounties;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final Bounties plugin;

    public PlayerConnectionListener(Bounties plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();

        if(plugin.getDataManager().isPlayerBounty(player)) {
            return;
        }

        if(plugin.getCache().containsCacheForPlayer(player.getUniqueId())) {
            plugin.getDataManager().loadBountyFromExternalCache(player);
        }

        plugin.getDataManager().loadBountyKillsFor(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();
        if(plugin.getDataManager().isPlayerBounty(player)){
            plugin.getCache().updateBounty(plugin.getDataManager().getPlayerBounty(player.getUniqueId()));
            plugin.getDataManager().saveBountyKillsOf(player.getUniqueId());
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getDataManager().unloadFromCache(player.getUniqueId());
                    plugin.getDataManager().unloadBountyKillsFor(player.getUniqueId());
                }
            }, 20L *plugin.getConfiguration().getCacheSec()); //A small cache measure for some quick player reconnects
        }
    }

}
