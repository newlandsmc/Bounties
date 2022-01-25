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

import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerDeathListener implements Listener {

    private final Bounties plugin;

    public PlayerDeathListener(Bounties plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeathListener(PlayerDeathEvent event){
        final Player deadPlayer = event.getEntity();

        //This is a combat logged situation
        if(deadPlayer == null)
            return;

        boolean deadPlayerWasBounty = false;
        //Death can be of many reason, if there is no human player entity that killed a player.
        //it will not be of PvP
        if(deadPlayer.getKiller() == null)
            return;

        final Player killer = deadPlayer.getKiller();

        event.setDeathMessage(null);

        //Check if the player is exempted from the bounty system
        if(plugin.getDataManager().isPlayerExemptedFromBounty(killer.getName()) || plugin.getDataManager().isPlayerExemptedFromBounty(deadPlayer.getName()))
            return;

        if(killer.hasPermission("bounty.bypass") || deadPlayer.hasPermission("bounty.bypass"))
            return;
        

        if(plugin.getDataManager().isPlayerBounty(deadPlayer)) {
            Bounty bounty = plugin.getDataManager().getPlayerBounty(deadPlayer.getUniqueId());
            plugin.getDataManager().addBountyPlayerKill(killer.getUniqueId());
            RewardRank rank = null;
            if(plugin.getConfiguration().getRewardMap().containsKey(bounty.getCurrentKills())){
                rank = plugin.getConfiguration().getRewardMap().get(bounty.getCurrentKills());
            }else {
                try {
                    final int closestMatch = plugin.getConfiguration().getSortedRewardList().lower(bounty.getCurrentKills());
                    rank = plugin.getConfiguration().getRewardMap().get(closestMatch);
                }catch (Exception e){
                    plugin.getLogger().info("The plugin is unable to determine a closer kill reward for "+bounty.getCurrentKills()+" kills");
                }
            }

            if(rank != null) {
                rank.executeFor(killer, deadPlayer);
                plugin.getDataManager().clearBountyForPlayer(deadPlayer.getUniqueId());

                for(String s : plugin.getConfiguration().getBountyClear()){
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            MiniMessageUtils.broadcast(s
                                    .replace("%dead_player%", deadPlayer.getName())
                                    .replace("%killer%", killer.getName())
                                    .replace("%player_kills%", String.valueOf(bounty.getCurrentKills()))
                                    .replace("%player_x%", String.valueOf(deadPlayer.getLocation().getX()))
                                    .replace("%player_y%", String.valueOf(deadPlayer.getLocation().getY()))
                                    .replace("%player_z%", String.valueOf(deadPlayer.getLocation().getZ()))
                            );
                        }
                    },plugin.getConfiguration().getCooldownTicks());
                }
            }
            deadPlayerWasBounty = true;
        }else {
            (plugin.getConfiguration().getRewardToKillNonBounty()).executeFor(killer,deadPlayer);
        }

        //If the dead player is bounty we don't need to bother about other adding a new kill or creating a new bounty on
        //the killer's head
        if(deadPlayerWasBounty) {
            return;
        }

        if(plugin.getDataManager().isPlayerBounty(killer.getUniqueId())){
            plugin.getDataManager().addNewKill(killer,deadPlayer);
        }else {
            plugin.getDataManager().createBountyForPlayer(killer);
            for(String b : plugin.getConfiguration().getNewBountyBroadcast()){
                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        MiniMessageUtils.broadcast(b
                                .replace("%killer%", killer.getName())
                                .replace("%rand_loc%", LocationUtils.getRandomLocationFromARadius(killer.getLocation(), 50))
                                .replace("%dead_player%", deadPlayer.getName()));
                    }
                },plugin.getConfiguration().getCooldownTicks());
            }
        }
    }
}
