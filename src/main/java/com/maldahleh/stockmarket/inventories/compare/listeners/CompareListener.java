package com.maldahleh.stockmarket.inventories.compare.listeners;

import com.maldahleh.stockmarket.inventories.compare.CompareInventory;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class CompareListener implements Listener {
  private final CompareInventory inventory;

  @EventHandler
  public void onClick(InventoryClickEvent e) {
    if (!inventory.hasActiveInventory(e.getWhoClicked())) {
      return;
    }

    e.setCancelled(true);
  }

  @EventHandler
  public void onDrag(InventoryDragEvent e) {
    if (!inventory.hasActiveInventory(e.getWhoClicked())) {
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
