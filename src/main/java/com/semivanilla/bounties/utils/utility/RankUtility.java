package com.semivanilla.bounties.utils.utility;

import com.semivanilla.bounties.model.RewardRank;
import com.semivanilla.bounties.utils.UtilityManager;
import org.bukkit.entity.Player;

import java.util.Optional;

public class RankUtility {

    private UtilityManager manager;

    public RankUtility(UtilityManager manager) {
        this.manager = manager;
    }

    public Optional<RewardRank> matchPlayerRank(Player player){
        RewardRank rank = null;
        for(int i = manager.getJavaPlugin().getConfiguration().getMaxRewardToSearch(); i >= 1; i--){
            if(player.hasPermission("boh.reward."+i)){
                System.out.println("Player has permission "+i);
                if(manager.getJavaPlugin().getConfiguration().getRewardMap().containsKey(i)) {
                    System.out.println("Also contains key");
                    rank = manager.getJavaPlugin().getConfiguration().getRewardMap().get(i);
                    break;
                }
            }
        }
        return Optional.ofNullable(rank);
    }

}
