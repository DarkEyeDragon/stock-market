package com.maldahleh.stockmarket.inventories.utils.paged.listeners;

import com.maldahleh.stockmarket.inventories.utils.paged.PagedInventory;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PagedInventoryListener implements Listener {
  private final PagedInventory inventory;

  @EventHandler
  public void onClick(InventoryClickEvent e) {
    inventory.handleClick(e);
  }

  @EventHandler
  public void onDrag(InventoryDragEvent e) {
    if (!inventory.hasInventory(e.getWhoClicked())) {
      return;
    }

    e.setCancelled(true);
  }

  @EventHandler
  public void onClose(InventoryCloseEvent e) {
    inventory.remove(e.getPlayer());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    inventory.remove(e.getPlayer());
  }
}
