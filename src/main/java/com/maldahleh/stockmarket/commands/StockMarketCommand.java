package com.maldahleh.stockmarket.commands;

import com.maldahleh.stockmarket.config.Messages;
import com.maldahleh.stockmarket.inventories.InventoryManager;
import com.maldahleh.stockmarket.processor.StockProcessor;
import com.maldahleh.stockmarket.utils.Utils;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@AllArgsConstructor
public class StockMarketCommand implements CommandExecutor {
  private final Plugin plugin;
  private final StockProcessor stockProcessor;
  private final InventoryManager inventoryManager;
  private final Messages messages;

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s,
      String[] strings) {
    if (!(commandSender instanceof Player)) {
      commandSender.sendMessage("Stocks - You must be a player to use this command.");
      return true;
    }

    Player player = (Player) commandSender;
    if (!player.hasPermission("stockmarket.use")) {
      messages.sendNoPermission(player);
      return true;
    }

    if (strings.length == 0) {
      messages.sendHelpMessage(player);
      return true;
    }

    if (strings.length == 1 && strings[0].equalsIgnoreCase("help")) {
      messages.sendHelpMessage(player);
      return true;
    }

    if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
      if (!player.hasPermission("stockmarket.list")) {
        messages.sendNoPermission(player);
        return true;
      }

      inventoryManager.openListInventory(player);
      return true;
    }

    if (strings.length == 1 && strings[0].equalsIgnoreCase("tutorial")) {
      if (!player.hasPermission("stockmarket.tutorial")) {
        messages.sendNoPermission(player);
        return true;
      }

      inventoryManager.openTutorialInventory(player);
      return true;
    }

    if (strings.length == 2 && strings[0].equalsIgnoreCase("lookup")) {
      if (!player.hasPermission("stockmarket.lookup")) {
        messages.sendNoPermission(player);
        return true;
      }

      inventoryManager.openLookupInventory(player, strings[1]);
      return true;
    }

    if (strings.length == 2 && strings[0].equalsIgnoreCase("compare")
        && strings[1].contains(",")) {
      if (!player.hasPermission("stockmarket.compare")) {
        messages.sendNoPermission(player);
        return true;
      }

      String[] symbols = strings[1].split(",");
      if (symbols.length > 3) {
        messages.sendCompareMax(player);
        return true;
      }

      inventoryManager.openCompareInventory(player, symbols);
      return true;
    }

    if (strings.length == 1 && strings[0].equalsIgnoreCase("portfolio")) {
      if (!player.hasPermission("stockmarket.portfolio")) {
        messages.sendNoPermission(player);
        return true;
      }

      inventoryManager.openPortfolioInventory(player);
      return true;
    }

    if (strings.length == 2 && strings[0].equalsIgnoreCase("portfolio")) {
      if (!player.hasPermission("stockmarket.portfolio.other")) {
        messages.sendNoPermission(player);
        return true;
      }

      Player target = Bukkit.getPlayer(strings[1]);
      if (target != null) {
        inventoryManager.openPortfolioInventory(player, target.getUniqueId());
        return true;
      }

      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
        if (offlinePlayer == null) {
          messages.sendInvalidPlayer(player);
          return;
        }

        inventoryManager.openPortfolioInventory(player, offlinePlayer.getUniqueId());
      });
      return true;
    }

    if (strings.length == 1 && strings[0].equalsIgnoreCase("transactions")) {
      if (!player.hasPermission("stockmarket.transactions")) {
        messages.sendNoPermission(player);
        return true;
      }

      inventoryManager.openTransactionInventory(player);
      return true;
    }

    if (strings.length == 2 && strings[0].equalsIgnoreCase("transactions")) {
      if (!player.hasPermission("stockmarket.transactions.other")) {
        messages.sendNoPermission(player);
        return true;
      }

      Player target = Bukkit.getPlayer(strings[1]);
      if (target != null) {
        inventoryManager.openTransactionInventory(player, target.getUniqueId());
        return true;
      }

      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
        if (offlinePlayer == null) {
          messages.sendInvalidPlayer(player);
          return;
        }

        inventoryManager.openTransactionInventory(player, offlinePlayer.getUniqueId());
      });
      return true;
    }

    if (strings.length == 3 && strings[0].equalsIgnoreCase("buy")) {
      Integer quantity = Utils.getInteger(strings[2]);
      if (quantity == null || quantity <= 0) {
        messages.sendInvalidQuantity(player);
        return true;
      }

      stockProcessor.buyStock(player, strings[1], quantity);
      return true;
    }

    if (strings.length == 3 && strings[0].equalsIgnoreCase("sell")) {
      Integer quantity = Utils.getInteger(strings[2]);
      if (quantity == null || quantity <= 0) {
        messages.sendInvalidQuantity(player);
        return true;
      }

      stockProcessor.sellStock(player, strings[1], quantity);
      return true;
    }

    messages.sendInvalidSyntax(player);
    return true;
  }
}
