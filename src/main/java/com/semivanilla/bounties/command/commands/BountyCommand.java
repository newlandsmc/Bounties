package com.semivanilla.bounties.command.commands;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.command.CommandHandler;
import com.semivanilla.bounties.command.CommandResponse;
import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Command("bounties")
public class BountyCommand extends CommandBase {

    private final CommandHandler handler;

    public BountyCommand(CommandHandler handler) {
        this.handler = handler;
    }
    private final HashMap<String, String> commandHelp = new HashMap<>(){{
        put("/bounty add player", "Adds a bounty to the player");
        put("/bounty add player mins","Adds a bounty to the player for specified time");
        put("/bounty remove player","Removes the bounty from player");
        put("/bounty bypass player","Adds them to the bypass list. Once added, they won't be bounty-ed nor will release a bounty");
        put("/bounty reload","Reloads the plugin. This will only reload the plugin configuration, not its cache");
        put("/bounty set player kills","Sets the amount of kills a bounty have");
    }};

    @Default
    public void defaultCommand(final Player player){
        handler.getPlugin().getMenu().openMenu(player);
    }

    @SubCommand("help")
    public void onHelp(final CommandSender sender){
        MiniMessageUtils.sendMessage(sender,handler.getPlugin().getConfiguration().getHelpHeader());
        commandHelp.forEach((command,desc) -> {
            MiniMessageUtils.sendMessage(sender,handler.getPlugin().getConfiguration().getHelpMessage(command,desc));
        });
        MiniMessageUtils.sendMessage(sender,handler.getPlugin().getConfiguration().getHelpFotter());
    }

    @SubCommand("add")
    @Permission("bounty.command.add")
    @Completion({"#players"})
    public void addBounty(final CommandSender sender, final Player player, @Optional Integer time){
        if(player == null) {
            MiniMessageUtils.sendMessage(sender, CommandResponse.INVALID_ARGS.getMessage());
            return;
        }

        if(handler.getPlugin().getDataManager().isPlayerBounty(player)){
            MiniMessageUtils.sendMessage(sender,CommandResponse.ALREADY_BOUNTY_ON_HEAD.getMessage());
            return;
        }

        if(handler.getPlugin().getDataManager().isPlayerExemptedFromBounty(player.getName())){
            MiniMessageUtils.sendMessage(sender,CommandResponse.PLAYER_IS_EXEMPTED.getMessage());
        }

        if(time != null){
            final Long duration = TimeUnit.MINUTES.toMillis(time);
            handler.getPlugin().getDataManager().createBountyForPlayer(player,duration);
        }else {
            handler.getPlugin().getDataManager().createBountyForPlayer(player);
        }

        MiniMessageUtils.sendMessage(sender,CommandResponse.CREATED_BOUNTY.getMessage());
    }

    @SubCommand("remove")
    @Permission("bounty.command.remove")
    @Completion({"#bounties"})
    public void removeBounty(final CommandSender sender, final Player player){
        if(player == null){
            MiniMessageUtils.sendMessage(sender, CommandResponse.INVALID_ARGS.getMessage());
            return;
        }

        if(!handler.getPlugin().getDataManager().isPlayerBounty(player)){
            MiniMessageUtils.sendMessage(sender,CommandResponse.NO_BOUNTY_ON_PLAYER.getMessage());
            return;
        }

        handler.getPlugin().getDataManager().clearBountyForPlayer(player.getUniqueId());
        MiniMessageUtils.sendMessage(sender,CommandResponse.REMOVED_BOUNTY.getMessage());

    }

    @SubCommand("bypass")
    @Permission("bounty.command.bypass")
    @Completion({"#players"})
    public void onBypass(final CommandSender sender, final Player player){
        if(player == null){
            MiniMessageUtils.sendMessage(sender, CommandResponse.INVALID_ARGS.getMessage());
            return;
        }

        if(handler.getPlugin().getDataManager().isPlayerBounty(player)){
            MiniMessageUtils.sendMessage(sender,CommandResponse.HAVE_A_ACTIVE_BOUNTY.getMessage());
            return;
        }

        if(handler.getPlugin().getDataManager().isPlayerExemptedFromBounty(player.getName())){
            handler.getPlugin().getDataManager().removeFromExempt(player.getName());
            MiniMessageUtils.sendMessage(sender,CommandResponse.REMOVED_EXEMPTION.getMessage());
        }else {
            handler.getPlugin().getDataManager().addToExemptFromBounty(player.getName());
            MiniMessageUtils.sendMessage(sender,CommandResponse.ADDED_EXEMPTION.getMessage());
        }
    }

    @SubCommand("reload")
    @Permission("bounty.command.reload")
    public void onReloadCommand(final CommandSender sender){
        handler.getPlugin().handleReload();
        MiniMessageUtils.sendMessage(sender,CommandResponse.PLUGIN_RELOADED.getMessage());
    }

    @SubCommand("set")
    @Permission("bounty.command.set")
    @Completion({"#players"})
    public void onSetCommand(final CommandSender sender, final Player player, final Integer kills){
        if(player == null || kills == null){
            MiniMessageUtils.sendMessage(sender,CommandResponse.INVALID_ARGS.getMessage());
        }

        if(!handler.getPlugin().getDataManager().isPlayerBounty(player)){
            MiniMessageUtils.sendMessage(sender,CommandResponse.NO_BOUNTY_ON_PLAYER.getMessage());
            return;
        }

        handler.getPlugin().getDataManager().setKillOnBounty(player.getUniqueId(),kills);
        MiniMessageUtils.sendMessage(sender,CommandResponse.SET_PLAYER_BOUNTY_KILLS.getMessage().replaceAll("%player%",player.getName()).replaceAll("%kills%", String.valueOf(kills)));
    }
}
