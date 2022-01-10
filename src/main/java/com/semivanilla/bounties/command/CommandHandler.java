package com.semivanilla.bounties.command;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.command.commands.BountyCommand;
import com.semivanilla.bounties.model.Bounty;
import me.mattstudios.mf.base.CommandManager;

import java.util.stream.Collectors;

public class CommandHandler {

    private final Bounties plugin;
    private final CommandManager manager;

    public CommandHandler(Bounties plugin) {
        this.plugin = plugin;
        this.manager = new CommandManager(plugin);
    }

    public void registerCommands(){
        registerOthers();
        manager.register(
                new BountyCommand(this),
                new com.semivanilla.bounties.command.commands.Bounties(this)
        );
    }

    private void registerOthers(){
        manager.getCompletionHandler().register("#bounties", input -> {
            return plugin.getDataManager().getAllBounty().stream().map(Bounty::getPlayerName).collect(Collectors.toList());
        });
    }

    public Bounties getPlugin() {
        return plugin;
    }
}
