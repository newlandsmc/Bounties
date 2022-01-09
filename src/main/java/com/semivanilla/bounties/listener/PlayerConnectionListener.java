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
            System.out.println("Is in internal cache");
            return;
        }

        if(plugin.getCache().containsPlayer(player.getUniqueId())) {
            plugin.getDataManager().loadBountyFromExternalCache(player);
            System.out.println("Loading from external cache");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();
        if(plugin.getDataManager().isPlayerBounty(player)){
            plugin.getCache().updateBounty(plugin.getDataManager().getPlayerBounty(player.getUniqueId()));
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getDataManager().unloadFromCache(player.getUniqueId());
                }
            },20*30); //A small cache measure for some quick player reconnects
        }
    }

}
