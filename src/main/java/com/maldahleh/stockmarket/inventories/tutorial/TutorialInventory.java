package com.maldahleh.stockmarket.inventories.tutorial;

import com.maldahleh.stockmarket.StockMarket;
import com.maldahleh.stockmarket.inventories.tutorial.listeners.TutorialListener;
import com.maldahleh.stockmarket.utils.Utils;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TutorialInventory {
  private final Set<UUID> activeViewers;
  private final Inventory tutorialInventory;

  public TutorialInventory(StockMarket plugin, ConfigurationSection section) {
    this.activeViewers = new HashSet<>();
    this.tutorialInventory = Bukkit.createInventory(null, section
        .getInt("inventory.size"), Utils.color(section.getString("inventory.name")));

    for (String key : section.getConfigurationSection("items").getKeys(false)) {
      tutorialInventory.setItem(Integer.valueOf(key), Utils.createItemStack(section
          .getConfigurationSection("items." + key)));
    }

    Bukkit.getServer().getPluginManager().registerEvents(new TutorialListener(this), plugin);
  }

  public void openInventory(Player player) {
    player.openInventory(tutorialInventory);
    activeViewers.add(player.getUniqueId());
  }

  public boolean isActive(HumanEntity entity) {
    return activeViewers.contains(entity.getUniqueId());
  }

  public void remove(HumanEntity entity) {
    activeViewers.remove(entity.getUniqueId());
  }
}
