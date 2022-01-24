package com.semivanilla.bounties.model;

import com.google.common.base.Objects;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class RewardRank {

    private static final String XP_TAG = "[XP]";
    private static final String XP_TAG_CONCAT = "[XP]:";
    private static final String XP_LVL_TAG = "[XPLVL]";
    private static final String XP_LVL_TAG_CONCAT = "[XPLVL]:";
    private static final String MESSAGE_TAG = "[MESSAGE]";
    private static final String MESSAGE_TAG_CONCAT = "[MESSAGE]:";
    private static final String CONSOLE_TAG = "[CONSOLE]";
    private static final String CONSOLE_TAG_CONCAT = "[CONSOLE]:";
    private static final String PLAYER_TAG = "[PLAYER]";
    private static final String PLAYER_TAG_CONCAT = "[PLAYER]:";
    private static final String BROADCAST_TAG = "[BROADCAST]";
    private static final String BROADCAST_TAG_CONCAT = "[BROADCAST]:";
    private static final String FXP_TAG = "[FXP]";
    private static final String FXP_TAG_CONCAT = "[FXP]:";

    private static final String COMMAND_FXP_ADD = "sk xp add %s fighting %d";
    private static final String COMMAND_FXP_REMOVE = "sk xp remove %s fighting %d";

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
                    case XP_TAG:
                        giveXP(killer, Integer.parseInt(rewardArgs[1]));
                        break;
                    case XP_LVL_TAG:
                        giveXPLevel(killer, Integer.parseInt(rewardArgs[1]));
                        break;
                    case MESSAGE_TAG:
                        final String message = reward.substring((MESSAGE_TAG_CONCAT).length())
                                .replaceAll("%dead-bounty%",deadGuy.getName())
                                .replaceAll("%killer%",killer.getName())
                                ;
                        sendMessage(killer, message);
                        break;
                    case CONSOLE_TAG:
                        final String consoleCommand = reward.substring((CONSOLE_TAG_CONCAT).length())
                                .replaceAll("%dead-bounty%",deadGuy.getName())
                                .replaceAll("%killer%",killer.getName())
                                ;
                        executeCommand(consoleCommand);
                        break;
                    case PLAYER_TAG:
                        final String command = reward.substring((PLAYER_TAG_CONCAT).length())
                                .replaceAll("%dead-bounty%",deadGuy.getName())
                                .replaceAll("%killer%",killer.getName())
                                ;
                        executeCommand(killer,command);
                        break;
                    case BROADCAST_TAG:
                        final String broadcastMessage = reward.substring((BROADCAST_TAG_CONCAT).length())
                                .replaceAll("%dead-bounty%",deadGuy.getName())
                                .replaceAll("%killer%",killer.getName())
                                ;
                        broadcastMessage(broadcastMessage);
                        break;
                    case FXP_TAG:
                        executeCommand(String.format(COMMAND_FXP_ADD,killer.getName(),Integer.parseInt(rewardArgs[1])));
                        executeCommand(String.format(COMMAND_FXP_REMOVE,deadGuy.getName(),Integer.parseInt(rewardArgs[1])));
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

    public Optional<String> getFXPIfPresent(){
        Optional<String> FXPPresent = rewardsToProcess.stream().filter(s -> s.startsWith(FXP_TAG)).findFirst();
        if(FXPPresent.isEmpty())
            return Optional.empty();

        return Optional.ofNullable((FXPPresent.get().substring(FXP_TAG_CONCAT.length())));
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

