package com.semivanilla.bounties.gui;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BountyMenu {

    private final Bounties plugin;
    private final PaginatedGui paginatedGui;

    public BountyMenu(Bounties plugin) {
        this.plugin = plugin;
        this.paginatedGui = Gui
                .paginated()
                .title(MiniMessageUtils.transform(plugin.getConfiguration().getGUIName()))
                .rows(plugin.getConfiguration().getGuiSize()+1)
                .disableAllInteractions()
                .create();

        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1, 1,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1,2,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1, 3, ItemBuilder.from(Material.HOPPER).name(MiniMessageUtils.transform(plugin.getConfiguration().getPreviousName())).asGuiItem(event -> paginatedGui.previous()));
        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1,4,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1,5,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1,6,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1, 7, ItemBuilder.from(Material.HOPPER).name(MiniMessageUtils.transform(plugin.getConfiguration().getNextName())).asGuiItem(event -> paginatedGui.next()));
        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1,8,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
        paginatedGui.setItem(plugin.getConfiguration().getGuiSize()+1,9,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
    }

    public void openMenu(Player player){
        prepareMenu(player).thenAccept(gui -> {
           plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
               @Override
               public void run() {
                gui.open(player);
               }
           });
        });
    }

    private CompletableFuture<PaginatedGui> prepareMenu(Player player){
        return CompletableFuture.supplyAsync(new Supplier<PaginatedGui>() {
            @Override
            public PaginatedGui get() {
                paginatedGui.clearPageItems();

                plugin.getDataManager().getAllBounty().forEach((b) -> {
                    final GuiItem item = ItemBuilder
                            .skull()
                            .owner(b.getPlayer())
                            .name(MiniMessageUtils.transform(
                                    plugin.getConfiguration().getNamePlaceholder().replace("%player%",b.getPlayerName())))
                            .lore(MiniMessageUtils.transform(plugin.getConfiguration().getLorePlaceholder(b.getCurrentKills(),b.getFormattedRemainingTime())))
                            .asGuiItem(event -> {
                               if(player.hasPermission("bounty.admin")){
                                   player.teleport(b.getPlayer().getLocation());
                               }
                            });
                    paginatedGui.addItem(item);
                });

                return paginatedGui;
            }
        });
    }
}
