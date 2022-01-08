package com.semivanilla.bounties;

import com.semivanilla.bounties.model.RewardRank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class TestClass implements Listener {

    private Bounties plugin;

    public TestClass(Bounties plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        onPlayer(event.getPlayer());
    }

    private void onPlayer(Player player){
        Optional<RewardRank> o = plugin.getUtilsManager().getRankUtility().matchPlayerRank(player);
        System.out.println("Is present "+o.isPresent());
        if(o.isPresent()){
            System.out.println(o.get().getRankLevel()+" "+o.get().getRewardsToProcess());
            o.get().executeFor(player,player);
        }
    }
}
