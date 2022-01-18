package com.semivanilla.bounties.model;

import com.google.common.base.Objects;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import org.apache.commons.lang.StringUtils;
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

    public RewardRank(List<String> rewardsToProcess) {
        this.rankLevel = 0;
        this.rewardsToProcess = rewardsToProcess;
    }

    public int getRankLevel() {
        return rankLevel;
    }

    public void executeFor(@NotNull Player killer, @NotNull Player deadGuy){
        rewardsToProcess.forEach((reward) -> {
            final String[] rewardArgs = reward.split(":");
            if(rewardArgs.length > 1) {
                switch (rewardArgs[0].toUpperCase()) {
                    case "[XP]":
                        giveXP(killer, Integer.parseInt(rewardArgs[1]));
                        break;
                    case "[XPLVL]":
                        giveXPLevel(killer, Integer.parseInt(rewardArgs[1]));
                        break;
                    case "[MESSAGE]":
                        final String message = reward.substring(("[MESSAGE]:").length())
                                .replaceAll("%dead-bounty%",deadGuy.getName())
                                .replaceAll("%killer%",killer.getName())
                                ;
                        sendMessage(killer, message);
                        break;
                    case "[CONSOLE]":
                        final String consoleCommand = reward.substring(("[CONSOLE]:").length())
                                .replaceAll("%dead-bounty%",deadGuy.getName())
                                .replaceAll("%killer%",killer.getName())
                                ;
                        executeCommand(consoleCommand);
                        break;
                    case "[PLAYER]":
                        final String command = reward.substring(("[PLAYER]:").length())
                                .replaceAll("%dead-bounty%",deadGuy.getName())
                                .replaceAll("%killer%",killer.getName())
                                ;
                        executeCommand(killer,command);
                        break;
                    case "[BROADCAST]":
                        final String broadcastMessage = reward.substring(("[BROADCAST]:").length())
                                .replaceAll("%dead-bounty%",deadGuy.getName())
                                .replaceAll("%killer%",killer.getName())
                                ;
                        broadcastMessage(broadcastMessage);
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

    private void broadcastMessage( String message){
        if(StringUtils.isBlank(message))
            return;
        MiniMessageUtils.broadcast(message);
    }

    public List<String> getRewardsToProcess() {
        return rewardsToProcess;
    }

    public static RewardRank buildFrom(@NotNull List<String> strings){
        return new RewardRank(strings);
    }

    @Override
    public String toString() {
        return "RewardRank{" +
                "rankLevel=" + rankLevel +
                ", rewardsToProcess=" + rewardsToProcess +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RewardRank that = (RewardRank) o;
        return rankLevel == that.rankLevel && Objects.equal(rewardsToProcess, that.rewardsToProcess);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rankLevel, rewardsToProcess);
    }
}

