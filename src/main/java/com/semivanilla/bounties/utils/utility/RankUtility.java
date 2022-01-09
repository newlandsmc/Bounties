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

    /*public Optional<RewardRank> matchPlayerRank(Player player){
        RewardRank rank = null;
        for(int i = manager.getJavaPlugin().getConfiguration().getMaxRewardToSearch(); i >= 1; i--){
            if(player.hasPermission("boh.reward."+i)){
                if(manager.getJavaPlugin().getConfiguration().getRewardMap().containsKey(i)) {
                    rank = manager.getJavaPlugin().getConfiguration().getRewardMap().get(i);
                    break;
                }
            }
        }

        if(rank == null)
            rank = manager.getJavaPlugin().getConfiguration().getRewardMap().get(0);

        return Optional.ofNullable(rank);
    }*/

}

