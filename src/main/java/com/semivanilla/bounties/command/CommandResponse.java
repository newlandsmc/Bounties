package com.semivanilla.bounties.command;

public enum CommandResponse {
    ALREADY_BOUNTY_ON_HEAD("§cThe player already has a bounty on his head"),
    SET_PLAYER_BOUNTY_KILLS("§aSuccessfully set the kills for player %player% to %kills%"),
    HAVE_A_ACTIVE_BOUNTY("§cThe player has an active bounty on his head. He can't be exempted now!"),
    REMOVED_EXEMPTION("§aPlayer has been removed from bypass"),
    ADDED_EXEMPTION("§aPlayer has been added to get bypassed from bounties."),
    CREATED_BOUNTY("§aBounty has been created!"),
    PLAYER_IS_EXEMPTED("§cThis player has been added to the exempted list, Remove the player from §6/bounty bypass player"),
    NO_BOUNTY_ON_PLAYER("§cThe player does not have a bounty on his head"),
    ADDED_BOUNTY_ON_PLAYER("§aBounty has been added on player"),
    INVALID_ARGS("§cThe command is invalid/unrecognizable"),
    PLUGIN_RELOADED("§aReload complete")
    ;

    private final String message;

    CommandResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
