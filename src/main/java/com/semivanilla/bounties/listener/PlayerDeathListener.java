package com.semivanilla.bounties.listener;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.model.RewardRank;
import com.semivanilla.bounties.utils.utility.LocationUtils;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final Bounties plugin;

    public PlayerDeathListener(Bounties plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeathListener(PlayerDeathEvent event){
        final Player deadPlayer = event.getEntity();
        //Death can be of many reason, if there are no human player entity that killed a player.
        //it will not be of PvP
        if(deadPlayer.getKiller() == null)
            return;

        final Player killer = deadPlayer.getKiller();

        //Check if the player is exempted from the bounty system
        if(plugin.getDataManager().isPlayerExemptedFromBounty(killer.getName()) || plugin.getDataManager().isPlayerExemptedFromBounty(deadPlayer.getName()))
            return;

        event.setDeathMessage(null);

        if(plugin.getDataManager().isPlayerBounty(deadPlayer)){
            Bounty bounty = plugin.getDataManager().getPlayerBounty(deadPlayer.getUniqueId());
            final RewardRank rank = plugin.getConfiguration().getRewardMap().get(bounty.getCurrentLevel());
            rank.executeFor(killer,deadPlayer);
            plugin.getDataManager().clearBountyForPlayer(deadPlayer.getUniqueId());
            plugin.getConfiguration().getBountyClear().forEach((s) -> {
                MiniMessageUtils.broadcast(s
                        .replaceAll("%player%",deadPlayer.getName()
                                .replaceAll("%player_level%", String.valueOf(bounty.getCurrentLevel()))
                                .replaceAll("%player_kills%",String.valueOf(bounty.getCurrentKills()))
                                .replaceAll("%player-x%", String.valueOf(deadPlayer.getLocation().getX()))
                                .replaceAll("%player-y%", String.valueOf(deadPlayer.getLocation().getY()))
                                .replaceAll("%player-z%", String.valueOf(deadPlayer.getLocation().getZ()))
                                .replaceAll("%killer% ",killer.getName())
                        ));
            });
        }

        if(plugin.getDataManager().isPlayerBounty(killer.getUniqueId())){
            plugin.getDataManager().addNewKill(killer);
        }else {
            if(plugin.getDataManager().isPlayerBounty(deadPlayer)) {
                return;
            }

            plugin.getDataManager().createBountyForPlayer(killer,deadPlayer);
            plugin.getConfiguration().getNewBountyBroadcast().forEach((b) -> {
                if(b.contains("%rand-loc%")) {
                    MiniMessageUtils.broadcast(b
                            .replaceAll("%killer%", killer.getName())
                            .replaceAll("%rand-loc%", LocationUtils.getRandomLocationFromARadius(killer.getLocation(), 50))
                            .replaceAll("%dead-player%", deadPlayer.getName()));
                }else {
                    MiniMessageUtils.broadcast(b
                            .replaceAll("%killer%", killer.getName())
                            .replaceAll("%dead-player%", deadPlayer.getName()));
                }
            });
        }



    }
}
