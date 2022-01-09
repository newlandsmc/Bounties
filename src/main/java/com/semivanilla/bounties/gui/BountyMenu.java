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
                .title(Component.text("§cBounties"))
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        paginatedGui.setItem(6,1,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        paginatedGui.setItem(6,2,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        paginatedGui.setItem(6, 3, ItemBuilder.from(Material.HOPPER).name(MiniMessageUtils.transform("<color:#FFE400>« Previous")).asGuiItem(event -> paginatedGui.previous()));
        paginatedGui.setItem(6,4,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        paginatedGui.setItem(6,5,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        paginatedGui.setItem(6,6,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        paginatedGui.setItem(6, 7, ItemBuilder.from(Material.HOPPER).name(MiniMessageUtils.transform("<color:#FFE400>» Next")).asGuiItem(event -> paginatedGui.next()));
        paginatedGui.setItem(6,8,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        paginatedGui.setItem(6,9,ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
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
                            .name(MiniMessageUtils.transform("<color:#00FFF0>"+b.getPlayerName()))
                            .lore(MiniMessageUtils.transform("<color:#00FFF0>Time left <color:#A9A7A2>» "+b.getFormattedRemainingTime()),
                                    MiniMessageUtils.transform("<color:#00FFF0>Current Kills <color:#A9A7A2>» "+b.getCurrentKills()))
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
