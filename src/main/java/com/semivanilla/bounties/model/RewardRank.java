package com.semivanilla.bounties.model;

import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RewardRank {

    private final int rankLevel;
    private final List<String> rewardsToProcess;

    public RewardRank(int rankLevel, List<String> rewardsToProcess) {
        this.rankLevel = rankLevel;
        this.rewardsToProcess = rewardsToProcess;
    }


    public int getRankLevel() {
        return rankLevel;
    }

    public void executeFor(@NotNull Player player, @NotNull Player killed){
        rewardsToProcess.forEach((reward) -> {
            final String[] rewardArgs = reward.split(":");
            if(rewardArgs.length > 1) {
                switch (rewardArgs[0].toUpperCase()) {
                    case "[XP]":
                        giveXP(player, Integer.parseInt(rewardArgs[1]));
                        break;
                    case "[XPLVL]":
                        giveXPLevel(player, Integer.parseInt(rewardArgs[1]));
                        break;
                    case "[MESSAGE]":
                        final String message = reward.substring(("[MESSAGE]:").length());
                        sendMessage(player, message);
                        break;
                    case "[CONSOLE]":
                        final String consoleCommand = reward.substring(("[CONSOLE]:").length());
                        executeCommand(consoleCommand);
                        break;
                    case "[PLAYER]":
                        final String command = reward.substring(("[PLAYER]:").length());
                        executeCommand(player,command);
                        break;
                    default:
                }
            }
        });
    }

    private void executeCommand(String command){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
    }

    private void executeCommand(@NotNull Player player, String command){
        player.performCommand(command);
    }

    private void sendMessage(@NotNull Player player, String message){
        MiniMessageUtils.sendSimpleMessage(player,message);
    }

    private void giveXP(@NotNull Player player, int xpToGive){
        player.giveExp(xpToGive);
    }

    private void giveXPLevel(@NotNull Player player, int xpLevel){
        player.giveExpLevels(xpLevel);
    }

    public List<String> getRewardsToProcess() {
        return rewardsToProcess;
    }
}

