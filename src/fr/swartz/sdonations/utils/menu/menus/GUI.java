package fr.swartz.sdonations.utils.menu.menus;

import fr.swartz.sdonations.utils.menu.items.VirtualItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract interface GUI {
	
  public abstract GUI setItem(int paramInt, VirtualItem paramVirtualItem);
  public abstract GUI addItem(VirtualItem paramVirtualItem);
  public abstract void open(Player paramPlayer);
  public abstract void apply(Inventory paramInventory, Player paramPlayer);
  public abstract void onInventoryClick(InventoryClickEvent paramInventoryClickEvent);
  public abstract void onInventoryClose(InventoryCloseEvent paramInventoryCloseEvent);
}
