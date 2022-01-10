package com.semivanilla.bounties.command.commands;

import com.semivanilla.bounties.command.CommandHandler;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("bounties")
public class Bounties extends CommandBase {

    private final CommandHandler handler;

    public Bounties(CommandHandler handler) {
        this.handler = handler;
    }

    @Default
    @Permission("bounty.command.list")
    public void defaultCommand(final Player player){
        handler.getPlugin().getMenu().openMenu(player);
    }

}
