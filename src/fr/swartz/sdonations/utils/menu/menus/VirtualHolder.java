package fr.swartz.sdonations.utils.menu.menus;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter @Setter
public class VirtualHolder implements InventoryHolder {

  private GUI gui;
  private Inventory inventory;
  private int nextPage;
  
  public VirtualHolder(GUI gui, Inventory inventory) {
    this.gui = gui;
    this.inventory = inventory;
    this.nextPage = -1;
  }
}
